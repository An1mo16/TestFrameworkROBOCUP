package sk.fiit.jim.init;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static sk.fiit.jim.log.LogType.INIT;

import sk.fiit.jim.agent.moves.*;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;
import sk.fiit.robocup.library.math.MathExpressionEvaluator;

/**
 *  Loads the moves (low skills) from all XML files in a specific directory. Can also store their parsed
 *  form in a cache file (./movecache) for faster loading.
 *  Populates {@link LowSkills} and {@link Phases} with the objects of loaded low skills and phases.
 *  
 *  TODO: is there any reason why this class is not a singleton? Especially when LowSkills and Phases are?
 */
public class SkillsFromXmlLoader{
	
	File currentlyProcessed;
	File directoryWithXmls;
	Phase currentPhase;
	Set<String> requiredPhases = new HashSet<String>();
	Map<String, Double> constants = new HashMap<String, Double>();
	
	/**
	 * Creates a new loader object
	 * @param directoryWithXmls the directory from which the low skills will be loaded
	 */
	public SkillsFromXmlLoader(File directoryWithXmls){
		this.directoryWithXmls = directoryWithXmls;
	}

	/**
	 * Loads skills from xml files. 
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public void load()  throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		if (!directoryWithXmls.isDirectory())
			throw new IllegalArgumentException(directoryWithXmls.getAbsolutePath()+" is NOT a directory");
		
		//ignore non-xml files
		File[] xmlFiles = directoryWithXmls.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.endsWith(".xml") && !name.startsWith(".");
			}
		});
		
		File cache = new File("./movecache");
		//ak uz cache existuje, skontroluje sa, ci sa od jej vytvorenia zmenil nejaky xml subor
		boolean currentCacheExists = true;
		if (cache.exists()) {
			for (File xml : xmlFiles) {
				if (xml.lastModified() > cache.lastModified())
					currentCacheExists = false;
			}
		}else{
			currentCacheExists = false;
		}
		
		if (currentCacheExists) {
			loadMoveCache();
		}else{
			long xmlStart = System.currentTimeMillis();
			for (File xml : xmlFiles)
				loadMovesFrom(xml);
			long xmlEnd = System.currentTimeMillis();
			double seconds = (xmlEnd - xmlStart) / 1000.0;
			Log.log(INIT, "Took %s seconds", new Double(seconds).toString());
			
			saveMoveCache();
	
			for (String phase : requiredPhases)
				if (!Phases.exists(phase))
					Log.error(LogType.INIT, "Phase %s is referenced, yet undeclared", phase);
		}
	}
	
	//saves low skills and phases (not constants!) into a cache file
	private void saveMoveCache() throws IOException {
		File cache = new File("./movecache");
		DataOutputStream cacheOut = new DataOutputStream(new FileOutputStream(cache));
		
		for (LowSkill skill : LowSkills.getAll()) {
			cacheOut.writeUTF("skill");
			cacheOut.writeUTF(skill.name);
			cacheOut.writeUTF(skill.initialPhase);
			cacheOut.writeBoolean(skill.getExtendedFromMove() != null);
			if (skill.getExtendedFromMove() != null)
				cacheOut.writeUTF(skill.getExtendedFromMove());
			cacheOut.writeBoolean(skill.getDescription() != null);
			if (skill.getDescription() != null)
			cacheOut.writeUTF(skill.getDescription());
			cacheOut.writeBoolean(skill.getAuthor() != null);
			if (skill.getAuthor() != null)
				cacheOut.writeUTF(skill.getAuthor());
			cacheOut.writeInt(skill.getType().size());
			for (String s : skill.getType()) {
				cacheOut.writeUTF(s);
			}
		}
		
		for (Phase phase : Phases.getAll()) {
			cacheOut.writeUTF("phase");
			cacheOut.writeUTF(phase.name);			
			if (phase.isFinal)
				cacheOut.writeUTF("final");
			if (phase.next != null) {
				cacheOut.writeUTF("next");
				cacheOut.writeUTF(phase.next);
			}
			cacheOut.writeUTF("duration");
			cacheOut.writeDouble(phase.duration);			
			if (phase.finalizationPhase != null) {
				cacheOut.writeUTF("finalize");
				cacheOut.writeUTF(phase.finalizationPhase);
			}
			cacheOut.writeUTF("effectors");
			for (EffectorData e : phase.effectors) {
				cacheOut.writeUTF(e.effector.name());
				cacheOut.writeDouble(e.endAngle);
			}
			cacheOut.writeUTF("effend");
			cacheOut.writeUTF("phaseend");
		}
		cacheOut.writeUTF("end");
		cacheOut.close();
	}

	//loads low skills and phases (not constants!) from a cache file
	private void loadMoveCache() throws IOException {
		Log.log(LogType.INIT, "Loading moves from cache");
		long xmlStart = System.currentTimeMillis();
		
		File cache = new File("./movecache");
		DataInputStream cacheIn = new DataInputStream(new FileInputStream(cache));
		//ci sme narazili na string "end", ten ma byt na konci
		boolean endFlag = false;
		while (!endFlag) {
			//vzdy ide najprv string urcujuci typ nacitaneho objektu
			String command = cacheIn.readUTF();
			//ak je to skill, staci precitat nasledujuce 2 stringy
			if ("skill".equals(command)) {
				LowSkill lowSkill = new LowSkill();
				lowSkill.name = cacheIn.readUTF();
				lowSkill.initialPhase = cacheIn.readUTF();
				boolean hasExtended = cacheIn.readBoolean();
				if (hasExtended)
					lowSkill.setExtendedFromMove(cacheIn.readUTF());
				boolean hasDescription = cacheIn.readBoolean();
				if (hasDescription)
					lowSkill.setDescription(cacheIn.readUTF());
				boolean hasAuthor = cacheIn.readBoolean();
				if (hasAuthor)
					lowSkill.setAuthor(cacheIn.readUTF());
				int numTypes = cacheIn.readInt();
				for (int i=0; i<numTypes; i++)
					lowSkill.getType().add(cacheIn.readUTF());
				LowSkills.addSkill(lowSkill);
			}else if ("phase".equals(command)) {
				//fazy su zlozitejsie
				Phase phase = new Phase();
				//meno vzdy ide prve
				phase.name = cacheIn.readUTF();
				phase.effectors = new ArrayList<EffectorData>();
				//ci sme narazili na "phaseend", znaciace koniec tejto fazy
				boolean endPhase = false;
				while (!endPhase) {
					String phaseCmd = cacheIn.readUTF();
					if ("final".equals(phaseCmd)) {
						phase.isFinal = true;
					}else if ("next".equals(phaseCmd)) {
						phase.next = cacheIn.readUTF();
					}else if ("duration".equals(phaseCmd)) {
						phase.duration = cacheIn.readDouble();
					}else if ("finalize".equals(phaseCmd)) {
						phase.finalizationPhase = cacheIn.readUTF();
					//efektorov moze byt rozny pocet
					}else if ("effectors".equals(phaseCmd)) {
						boolean endEff = false;
						//preto je na ich konci string "effend"
						while (!endEff) {
							String effCmd = cacheIn.readUTF();
							if ("effend".equals(effCmd)) {
								endEff = true;
							}else{
								EffectorData tag = new EffectorData();
								tag.effector = Joint.valueOf(effCmd);
								tag.endAngle = cacheIn.readDouble();
								phase.effectors.add(tag);
							}
						}
					}else if ("phaseend".equals(phaseCmd)) {
						endPhase = true;
					}else break;
				}
				Phases.addPhase(phase);
			}else if ("end".equals(command)) {
				endFlag = true;
			}
		}
		cacheIn.close();
		Log.log(LogType.INIT, "Moves loaded");
		long xmlEnd = System.currentTimeMillis();
		double seconds = (xmlEnd - xmlStart) / 1000.0;
		Log.log(INIT, "Took %s seconds", new Double(seconds).toString());
	}

	private void loadMovesFrom(File xml) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		Log.log(LogType.INIT, "Loading moves from %s", xml.getAbsolutePath());
		currentlyProcessed = xml;
		//in order to avoid creating whitespace elements due to \n\t characters
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
		dbf.setIgnoringElementContentWhitespace(true);
		Document document = dbf.newDocumentBuilder().parse(xml);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		//load constants
		NodeList constants = (NodeList) xpath.compile("/robot/constants/constant").evaluate(document, XPathConstants.NODESET);
		for (Node constant = constants.item(0); constant != null ; constant = constant.getNextSibling())
			appendToConstants(constant);
		
		//load low skills
		NodeList lowSkills = (NodeList) xpath.compile("/robot/low_skills/low_skill").evaluate(document, XPathConstants.NODESET);
		for (Node lowSkill = lowSkills.item(0); lowSkill != null ; lowSkill = lowSkill.getNextSibling())
			LowSkills.addSkill(createSkill(lowSkill));
		
		//load phases
		NodeList phases = (NodeList) xpath.compile("/robot/phases/phase").evaluate(document, XPathConstants.NODESET);
		for (Node phase = phases.item(0); phase != null ; phase = phase.getNextSibling())
			Phases.addPhase(createPhase(phase));
	}
	
	private String getNodeAttribute(Node phaseNode, String attribute){
		return phaseNode.getAttributes().getNamedItem(attribute).getNodeValue();
	}

	private boolean hasAttribute(Node phaseNode, String attribute){
		return phaseNode.getAttributes().getNamedItem(attribute) != null;
	}

	private void appendToConstants(Node constant){
		String tagName = constant.getNodeName();
		if (!"constant".equals(tagName))
			return;
		String name = getNodeAttribute(constant, "name");
		String value = getNodeAttribute(constant, "value");
		constants.put(name, Double.valueOf(value));
	}

	private LowSkill createSkill(Node lowSkillNode) {
		String tagName = lowSkillNode.getNodeName();
		if (!"low_skill".equals(tagName))
			return null;
		if (!hasAttribute(lowSkillNode, "name"))
			throw new IllegalStateException("Low skill without name encountered in "+currentlyProcessed.getAbsolutePath());
		
		if (!hasAttribute(lowSkillNode, "firstPhase"))
			throw new IllegalStateException("Low skill without firstPhase encountered in "+currentlyProcessed.getAbsolutePath());
		
		String skillName = getNodeAttribute(lowSkillNode, "name");
		Log.debug(LogType.INIT, "Loading low skill %s", skillName);
		String initialPhase = getNodeAttribute(lowSkillNode, "firstPhase");
		LowSkill lowSkill = new LowSkill();
		
		if(lowSkillNode.getChildNodes().getLength()>0){
			for(Node node=lowSkillNode.getChildNodes().item(0);node!=null;node=node.getNextSibling()){
				if(node.getNodeName().equals("type")){
					lowSkill.getType().add(node.getTextContent());
				}
				else if(node.getNodeName().equals("author")){
					lowSkill.setAuthor(node.getTextContent());
				}
				else if(node.getNodeName().equals("extendedFromMove")){
					lowSkill.setExtendedFromMove(node.getTextContent());
				}
				else if(node.getNodeName().equals("description")){
					lowSkill.setDescription(node.getTextContent());
				}
			}
		}
		
		
		lowSkill.initialPhase = initialPhase;
		lowSkill.name = skillName;
		Log.debug(LogType.INIT, String.format("Low skill %s: initialPhase: %s", lowSkill.name, lowSkill.initialPhase));
		requiredPhases.add(initialPhase);
		return lowSkill;
	}
	
	private Phase createPhase(Node phaseNode) {
		if (!"phase".equals(phaseNode.getNodeName()))
			return null;
		
		Phase phase = new Phase();
		currentPhase = phase;
		phase.effectors = new ArrayList<EffectorData>();
		phase.name = phaseNode.getAttributes().getNamedItem("name").getNodeValue();
		Log.debug(LogType.INIT, "Loading phase %s", phase.name);
		
		populatePhaseAttributes(phaseNode, phase);
		
		for (int i = 0 ; i < phaseNode.getChildNodes().getLength() ; i++){
			Node child = phaseNode.getChildNodes().item(i);
			
			if ("effectors".equalsIgnoreCase(child.getNodeName()))
				appendEffectorsToPhase(child);
			
			if ("duration".equals(child.getNodeName())) {
				phase.duration = roundToNearestTwenty(calculateNumericValue(child.getFirstChild().getTextContent()));
			}
			
			if ("finalize".equalsIgnoreCase(child.getNodeName())){
				phase.finalizationPhase = child.getTextContent().trim();
				requiredPhases.add(child.getTextContent().trim());
			}
		}
		validate(phase);
		return phase;
	}

	private void populatePhaseAttributes(Node phaseNode, Phase phase) {
		if (hasAttribute(phaseNode, "isFinal"))
			phase.isFinal = true;
		if (hasAttribute(phaseNode, "skipIfFlag"))
			phase.skipIfFlag = new SkipFlag(getNodeAttribute(phaseNode, "skipIfFlag"));
		if (hasAttribute(phaseNode, "setFlagTrue"))
			phase.setFlagTrue = new SkipFlag(getNodeAttribute(phaseNode, "setFlagTrue"));
		if (hasAttribute(phaseNode, "setFlagFalse"))
			phase.setFlagFalse = new SkipFlag(getNodeAttribute(phaseNode, "setFlagFalse"));
		if (hasAttribute(phaseNode, "next")){
			phase.next = getNodeAttribute(phaseNode, "next");
			requiredPhases.add(phase.next);
		}
	}

	private void validate(Phase phase){
		if (phase.name == null || phase.name.isEmpty())
			throw new IllegalArgumentException("Phase declared in "+currentlyProcessed.getAbsolutePath()+" has empty or no name");
		
		if (phase.duration == 0.0)
			throw new IllegalArgumentException("Phase "+phase.name+" in "+currentlyProcessed.getAbsolutePath()+" has no duration");
		
		if (phase.next == null && !phase.isFinal)
			throw new IllegalArgumentException("Phase "+phase.name+" in "+currentlyProcessed.getAbsolutePath()+" has no follower");
		
		if (phase.finalizationPhase == null && phase.isFinal)
			throw new IllegalArgumentException("Phase "+phase.name+" in "+currentlyProcessed.getAbsolutePath()+" has no finalization phase");
	}

	/**
	 * Agent receives and sends data in a 20ms discrete tick. Therefore, we have to have 
	 * a timespan rounded to the nearest multiple of 20.
	 */
	private double roundToNearestTwenty(Double supplied)
	{
		Double calculated = Double.valueOf(supplied);
		if (supplied.intValue() % 20 >= 10 || supplied < 20)
			calculated = supplied - (supplied.intValue() % 20) + 20.0;
		else
			calculated = supplied - (supplied.intValue() % 20);
		
		if (supplied.intValue() != Double.valueOf(supplied).intValue())
			Log.log(LogType.INIT, "Phase(%s) duration truncated to nearest multiple of 20ms: %s => %.2f", currentPhase.name, supplied, calculated);
		
		return calculated / 1000.0;
	}

	/**
	 * Populates phases' effector list 
	 */
	private void appendEffectorsToPhase(Node effectorsNode) {
		for (int i = 0 ; i < effectorsNode.getChildNodes().getLength() ; i++)
		{
			Node effectorNode = effectorsNode.getChildNodes().item(i);
			//whitespace node, ignore it
			if ("#text".equals(effectorNode.getNodeName()) || "#comment".equals(effectorNode.getNodeName())) continue;
			
			if (effectorNode.getAttributes().getNamedItem("end") == null)
				throw new IllegalStateException("Effector without an end position encountered in phase "+currentPhase.name);
			Joint joint = Joint.valueOf(effectorNode.getNodeName().toUpperCase());
			EffectorData tag = new EffectorData();
			tag.effector = joint;
			String endAngle = getNodeAttribute(effectorNode, "end");
			Double endPosition = calculateNumericValue(endAngle);
			Double trimmed = joint.trim(endPosition);
			if (endPosition.intValue() != trimmed.intValue()){
				Log.log(INIT, "Joint %s trimmed from %.2f to %.2f", joint.toString(), endPosition, trimmed);
			}
				
			tag.endAngle = trimmed;
			currentPhase.effectors.add(tag);
		}
	}

	private Double calculateNumericValue(String endAngle){
		for (String constant : constants.keySet())
			endAngle = endAngle.replace(constant, constants.get(constant).toString());
		return new MathExpressionEvaluator(endAngle).getDouble();
	}
}