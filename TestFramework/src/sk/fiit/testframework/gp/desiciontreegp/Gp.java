package sk.fiit.testframework.gp.desiciontreegp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sk.fiit.jim.Settings;
import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.DtWalkToBallTest;


/**
 * 
 * @author J�lius Skrisa
 *
 */
public class Gp {
	public int populationLength;
	public List<String> Population = null; 
	public List<List<Double>> PopulationData = null;
	private double maxFitness;
	private double goalFitness;
	private String maxFitnessFile;
	private Random rand;
	private int LOG_FITNESS = 1000;
	private String PATH = Settings.getString("desicion_file_path");
	
	double mutationRate = 0.02;
	double insertConditionRate = 0.50;
	double deleteConditionRate = 0.30;
	int maxSizeList = 150;
	int n = 0;
	int random = 0;
	
	private Map<String, Integer> agentVariables = new HashMap<String, Integer>();
	private Map<String, Integer> desicions = new HashMap<String, Integer>();
	private Map<String, Integer> functions = new HashMap<String, Integer>();
	private Map<String, Integer> parameters = new HashMap<String, Integer>();
	private Map<String, Integer> angles = new HashMap<String, Integer>();
	private Map<String, Integer> treeDepth = new HashMap<String, Integer>();
	private Map<String, Integer> lowSkills = new HashMap<String, Integer>();
	
	private double TreeParameterSeparator = 16.0;
	private String pathOfDt;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	/*
	 * path - cesta + prefix xml suborov 
	 * goalf - cielova fitness honota
	 * popLength - velkost populacie
	 */
	public Gp(String path, double goalF, int popLength){
		agentVariables.put("r", 101);
		agentVariables.put("x", 102);
		agentVariables.put("y", 103);
		agentVariables.put("xr", 104);
		agentVariables.put("yr", 105);
				
		desicions.put("lowerThen", 110);
		desicions.put("biggerThen", 111);
		desicions.put("else", 112);

		treeDepth.put("lowerLevel", -50);
		treeDepth.put("sameLevel", -51);
		treeDepth.put("higherLevel", -52);
		
		functions.put("straight", 20);
		functions.put("right_and_distant", 21);
		functions.put("right_and_distant_less", 22);
		functions.put("right_and_distant_bit", 23);
		functions.put("left_and_distant", 24);
		functions.put("left_and_distant_less", 25);
		functions.put("left_and_distant_bit", 26);

		parameters.put("closeDistance", 1);
		parameters.put("x1", 2);
		parameters.put("x2", 3);
		parameters.put("y1", 4);
		parameters.put("y2", 5);
		parameters.put("r1", 6);
		
		angles.put("straightRange1", 7);
		angles.put("straightRange2", 8);
		angles.put("rightRange", 9);
		angles.put("rightRange2", 10);
		angles.put("rightRange3", 11);
		angles.put("leftRange", 12);
		angles.put("leftRange2", 13);
		angles.put("leftRange3", 14);

		lowSkills.put("null", 53);
	//	lowSkills.put("step_circ_left_small", 50);
	//	lowSkills.put("step_circ_left", 51);
	//	lowSkills.put("step_circ_right_small", 52);
	//	lowSkills.put("step_circ_right", 53);
		lowSkills.put("step_left", 54);
		lowSkills.put("step_right", 55);
		lowSkills.put("turn_left_36", 56);
		lowSkills.put("turn_left_45", 57);
		lowSkills.put("turn_left_60", 58);
		lowSkills.put("turn_left_90", 59);
		lowSkills.put("turn_right_36", 60);
		lowSkills.put("turn_right_45", 61);
		lowSkills.put("turn_right_60", 62);
		lowSkills.put("turn_right_90", 63);
		lowSkills.put("walk_back", 64);
		lowSkills.put("walk_forward", 65);
		lowSkills.put("walk_slow", 66);
		lowSkills.put("walk_turn_left", 67);
		lowSkills.put("walk_turn_right", 68);
		lowSkills.put("head_right_120", 69);
		lowSkills.put("head_left_120", 70);
		lowSkills.put("turn_right_cont_20", 71);
		lowSkills.put("turn_left_cont_20", 72);
		lowSkills.put("rollback", 73);
		/*lowSkills.put("turn_right_cont_10", 68);
		lowSkills.put("turn_right_cont_20", 69);
		lowSkills.put("turn_right_cont_4.5", 70);
		lowSkills.put("turn_right_cont_6.5", 71);
		lowSkills.put("turn_left_cont_10", 60);
		lowSkills.put("turn_left_cont_20", 61);
		lowSkills.put("turn_left_cont_4.5", 62);
		lowSkills.put("turn_left_cont_6.5", 63);*/
		
		pathOfDt = path;
		goalFitness = goalF;
		populationLength = popLength;
		Population = new ArrayList<String>();
		PopulationData = new ArrayList<List<Double>>();
		
		XmlToData();
	}

	
	private List<Double> XmlToData(String file) {
		List<Double> result = new ArrayList<Double>();
		Document doc = XmlLoader.Load(file + ".xml");
		
		NodeList desicion = doc.getElementsByTagName("desicionTree");
		Node desicionTree = desicion.item(0);
		if(desicionTree.getNodeType() == Node.ELEMENT_NODE && HasChildElements((Element)desicionTree)){
			if(desicionTree.getFirstChild() != null)
				result.add((double)treeDepth.get("sameLevel"));
			result.addAll(getDataFromTree(desicionTree.getFirstChild()));
		}
		result.add(TreeParameterSeparator); // separator
		
		NodeList para = doc.getElementsByTagName("parameters");
		Node parameters = para.item(0).getFirstChild();
		while(parameters != null){
			if(parameters.getNodeType() != Node.ELEMENT_NODE){
				parameters = parameters.getNextSibling();
				continue;
			}
			if(GetAttr(parameters, "startAngle") != "")
				result.add(Double.parseDouble(GetAttr(parameters, "startAngle")));
			result.add(Double.parseDouble(parameters.getTextContent()));
			parameters = parameters.getNextSibling();
		}
		return result;
	}
	
	private List<Double> getDataFromTree(Node child){
		List<Double> result = new ArrayList<Double>();
		boolean sibling = false;
		
		
		while(child != null){
			if(child.getNodeType() != Node.ELEMENT_NODE){
				child = child.getNextSibling();
				continue;
			}
			if(sibling && result.size() > 0 && result.get(result.size() -1) != (double)treeDepth.get("higherLevel")) result.add((double)treeDepth.get("sameLevel"));
			if(child.getNodeName().equals("desicion")){
				
				String condition = "";
				String function = "";
				if(!(condition = GetAttr(child, "condition")).equals("")){
					
					if(condition.equals("else")){
						result.add((double) 0);
						result.add((double)desicions.get("else"));
						result.add((double) 0);
						
					}
					else if(condition.equals("biggerThen") || condition.equals("lowerThen")){
						double sign  = 1;
						boolean constFlag = !(GetAttr(child, "typeb").equals(""));
						if(!GetAttr(child, "signb").equals(""))
							sign = -1;
						result.add((double) agentVariables.get(GetAttr(child, "valuea")));
						result.add((double)desicions.get(condition));
						if(constFlag)
							result.add(Double.parseDouble(GetAttr(child, "valueb")) + 1000);
						else
							result.add((double) parameters.get(GetAttr(child, "valueb")) * sign);
					}
				}
				else if(!(function = GetAttr(child, "function")).equals("")){
					
					String[] params = GetAttr(child, "params").split(",");
					if(!params[0].isEmpty()){
						result.add( (double)functions.get(function));
						int i = 0;
						for(i = 0; i < params.length; i++){
							result.add((double)angles.get(params[i]));
						}
						if(params.length == 1){
							result.add(0.0);
						}
					}
				}
					
				if(!HasChildElements((Element)child)){
					if(!child.getTextContent().replaceAll("\\s+","").equals("")){
						result.add((double)lowSkills.get(child.getTextContent().replaceAll("\\s+","")));
					}

				}
				else{
					result.add((double)treeDepth.get("lowerLevel")); //dalsia uroven
					result.addAll(getDataFromTree(child.getFirstChild()));
					result.add((double)treeDepth.get("higherLevel")); //vyssia uroven
				}
			}
			child = child.getNextSibling();
			sibling = true;
		}

		return result;
	}
	
	private static boolean HasChildElements(Element el) {
	    NodeList children = el.getChildNodes();
	    for (int i = 0;i < children.getLength();i++) {
	        if (children.item(i).getNodeType() == Node.ELEMENT_NODE){
	            return true;
	        }
	    }
	    return false;
	}
	
	/*
	 * 
	 *	Ulo�enie jedincov do Xml suborov
	 *
	 */
	private void DataToXml(String file, List<Double> list) {
		Document doc = XmlLoader.Load(file + ".xml");
		
		NodeList desicion = doc.getElementsByTagName("desicionTree");
		Node desicionT = desicion.item(0);
		
		DeleteAllChildNodes(desicionT);
		
		int j = 0;
		Element actElement = (Element)desicionT;
		int offset = 0;
		while(list.get(j) != TreeParameterSeparator){
			boolean lowSkillAdded = false;
			Element addedElement = null;
			if(actElement == null)
				actElement = (Element)desicionT;
			if(GetKeyByValue(agentVariables, list.get(j)) != null){
				offset = 3;	
				boolean signb = false;
				boolean constant = false;
				double parameter = list.get(j+2);
				if(parameter < 0 && GetKeyByValue(parameters, -parameter) != null){
					parameter = -parameter;
					signb = true;
				}
				if(parameter >= 950){
					parameter -=1000;
					constant = true;
				}
					
				String agentVar = GetKeyByValue(agentVariables, list.get(j));
				String condition = GetKeyByValue(desicions, list.get(j+1));
				
				String param = "";
				if(!constant){
					param = GetKeyByValue(parameters, parameter);
				}
				else param = String.valueOf(parameter);
				
				if(GetKeyByValue(lowSkills, list.get(j + 3)) != null && list.get(j + 3) != (double)treeDepth.get("lowerLevel") ){
					offset++;
					lowSkillAdded = true;
					addedElement = AddConditionElement(doc, actElement, agentVar, condition, param, GetKeyByValue(lowSkills, list.get(j + 3)), signb, constant);
				}
				else{
					addedElement = AddConditionElement(doc, actElement, agentVar, condition, param, null, signb, constant);
				}
				j += offset;
				
			}
			else if(GetKeyByValue(desicions, list.get(j+1)) != null && GetKeyByValue(desicions, list.get(j+1)).equals("else")){
				offset = 3;
				if(GetKeyByValue(lowSkills, list.get(j + 3)) != null){
					offset++;
					lowSkillAdded= true;
					addedElement = AddElseElement(doc, actElement, GetKeyByValue(lowSkills, list.get(j + 3)));
				}
				else{
					addedElement = AddElseElement(doc, actElement, null);
				}
				j += offset;
			}
			else if(GetKeyByValue(functions, list.get(j)) != null){
				offset = 3;
				String func = GetKeyByValue(functions, list.get(j));
				String param1 = GetKeyByValue(angles, list.get(j+1));
				String lowSkill = GetKeyByValue(lowSkills, list.get(j + 3));
				if(lowSkill != null){
					lowSkillAdded = true;
					offset++;
				}
				if(GetKeyByValue(angles, list.get(j+2)) != null){
					String param2 = GetKeyByValue(angles, list.get(j+2));
					addedElement = AddFunctionElement(doc, actElement, func, param1, param2, lowSkill);
				}
				else
					addedElement = AddFunctionElement(doc, actElement, func, param1, null, lowSkill);
				j += offset;
			}
			if(list.get(j) == (double)treeDepth.get("lowerLevel") && !lowSkillAdded){
				if(addedElement != null)
				actElement = addedElement;
			}
			else if(list.get(j) == (double)treeDepth.get("higherLevel")){
				if(actElement.getNodeName() == "desicionTree"){
					list.set(j, (double)treeDepth.get("sameLevel"));	
				}
				else{ 
					Element pomElement = (Element)actElement.getParentNode();
					if(pomElement != null && pomElement.getNodeName() != "HighSkill"){
						actElement = pomElement;
					}
				}
			}
			if(list.get(j) == TreeParameterSeparator)
				break;
			j++;
		}
		
		NodeList para = doc.getElementsByTagName("parameters");
		Node parameters = para.item(0).getFirstChild();
		int i = j+1;
		while(parameters != null){
			if(parameters.getNodeType() != Node.ELEMENT_NODE){
				parameters = parameters.getNextSibling();
				continue;
			}
			if(GetAttr(parameters, "startAngle") != ""){
				((Element)parameters).getAttributeNode("startAngle").setValue(list.get(i).toString());
				i++;
			}
			parameters.setTextContent(list.get(i).toString());
			parameters = parameters.getNextSibling();
			i++;
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(PATH + file + ".xml", false));
			transformer.transform(source, result);
		} catch (TransformerException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void DeleteAllChildNodes(Node desicionT){
		 while (desicionT.hasChildNodes())
		        desicionT.removeChild(desicionT.getFirstChild());
	}
	
	private Element AddConditionElement(Document doc, Element actElement, String agentVar, String condition, String param, String lowSkill, boolean signb, boolean constant){
		Element newElement = doc.createElement("desicion");
		actElement.appendChild(newElement);
		AddAttr(doc, newElement,"condition", condition);
		AddAttr(doc, newElement,"valuea", agentVar);
		AddAttr(doc, newElement,"valueb", param);
		
		if(constant){
			AddAttr(doc, newElement,"typeb", "const");
		}
			
		if(signb){
			AddAttr(doc, newElement,"signb", "negative");
		}
		
		if(lowSkill != null){
			newElement.setTextContent(lowSkill);
		}
	
		return newElement;
	}
	
	private Element AddElseElement(Document doc, Element actElement, String lowSkill){
		Element newElement = doc.createElement("desicion");
		actElement.appendChild(newElement);
		AddAttr(doc, newElement,"condition", "else");
		if(lowSkill != null){
			newElement.setTextContent(lowSkill);
		}
	
		return newElement;
	}
	
	private Element AddFunctionElement(Document doc, Element actElement, String func, String param1, String param2, String lowSkill){
		Element newElement = doc.createElement("desicion");
		actElement.appendChild(newElement);
		AddAttr(doc, newElement,"function", func);
		AddAttr(doc, newElement,"type", "sk.fiit.robocup.library.geometry.Angles");
		if(param2 != null && param1 != null){
			param1 = param1.concat("," + param2);
		}
		if(param1 == null && param2!= null){
			param1 = param2;
		}
			
		AddAttr(doc, newElement,"params", param1);
		if(lowSkill != null){
			newElement.setTextContent(lowSkill);
		}
	
		return newElement;
	}
	
	private void AddAttr(Document doc, Element e, String AttrName, String AttrValue){
		Attr attr = doc.createAttribute(AttrName);
		attr.setValue(AttrValue);
		e.setAttributeNode(attr);
	}
	
	private String GetAttr(Node dt, String Attr){
		return ((Element)dt).getAttribute(Attr);
	}
	
	private String GetKeyByValue(Map<String, Integer> map, double value){
		Iterator<Entry<String, Integer>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,Integer> pairs = (Map.Entry<String,Integer>)iterator.next();
			if(value == pairs.getValue()){
				return pairs.getKey();
			}
		}
		return null;
	}
	
	private List<Double> mutate(List<Double> list){
		
		Map<String, Integer> c = null;
		int i = 0;
		boolean isEmpty = list.get(0) == TreeParameterSeparator;
		while(list.get(i) != TreeParameterSeparator || isEmpty){
			c = classify(list.get(i));
			/*if(c != null && (c.equals(treeDepth) || c.equals(functions))){
				mutationRate += 0.01;
			}*/
			
			 n = rand.nextInt(100) + 1;
			 if(isEmpty){
					//System.out.println("insert");
					boolean flagAdd = false;
					if(list.get(i+4) != (double)treeDepth.get("lowerLevel")){
						list.add(i, (double)rand.nextInt(lowSkills.size()) + Collections.min(lowSkills.values()));
						flagAdd = true;
					}
					list.add(i,  1000.0);
					list.add(i, (double)rand.nextInt(desicions.size()) + Collections.min(desicions.values()));
					list.add(i, (double)rand.nextInt(agentVariables.size()) + Collections.min(agentVariables.values()));
					list.add(i, (double)treeDepth.get("sameLevel"));
					i += 3;
					if(flagAdd)
						i++;
					isEmpty = false;
					continue;
			 }
			 
			if(((double)n/100) < mutationRate){
				
				if(list.get(i) == 0){
					i++;
					continue;
				}
				
				boolean changeToRandom = true;
				if(c != null) random = rand.nextInt(c.size()) + Collections.min(c.values());
				
				if(c != null && c.equals(desicions)){
					//System.out.println("desicions");
					if(random == c.get("else")){
						list.set(i-1, 0.0);
						list.set(i+1, 0.0);
					}
					else if((double)list.get(i) == c.get("else")){
						list.set(i-1, (double)rand.nextInt(agentVariables.size()) + Collections.min(agentVariables.values()));
						list.add(i+1,  (double)rand.nextInt( parameters.size()) + Collections.min(parameters.values()));
					}
				}
				
				if(c != null && c.equals(treeDepth) && list.get(i+1) != TreeParameterSeparator){
					//System.out.println("treeDepth");
					 n = rand.nextInt(100) + 1;
					if(((double)n/100) < insertConditionRate && list.size() < maxSizeList){
						//System.out.println("insert");
						boolean flagAdd = false;
						if(list.get(i+4) != (double)treeDepth.get("lowerLevel")){
							list.add(i, (double)rand.nextInt(lowSkills.size()) + Collections.min(lowSkills.values()));
							flagAdd = true;
						}
						changeToRandom = false;
						n = rand.nextInt(100) + 1;
						if(n < 40){
							list.add(i,  (double)rand.nextInt(parameters.size()) + Collections.min(parameters.values()));
							list.add(i, (double)rand.nextInt(desicions.size()) + Collections.min(desicions.values()));
							list.add(i, (double)rand.nextInt(agentVariables.size()) + Collections.min(agentVariables.values()));
						}
						else if(n < 70)
						{
							list.add(i,  1000.0);
							list.add(i, (double)rand.nextInt(desicions.size()) + Collections.min(desicions.values()));
							list.add(i, (double)rand.nextInt(agentVariables.size()) + Collections.min(agentVariables.values()));
						}
						else {
							double func = (double)rand.nextInt(functions.size()) + Collections.min(functions.values());
							if(func == functions.get("straight")){
								list.add(i,  (double)angles.get("straightRange1"));
								list.add(i,  (double)angles.get("straightRange2"));
							}
							else{
								list.add(i, 0.0);
								list.add(i, func - 12);
							}
							list.add(i, func);
						}
						n = rand.nextInt(100) + 1;
						double treed = RandomTreeDepth(n);
						if(treed == treeDepth.get("lowerLevel") && i > 0 && GetKeyByValue(lowSkills, list.get(i-1)) != null){
							list.remove(i-1);
							list.add(i-1, treed );
							i--;
						}
						else
							list.add(i, treed );
						i += 3;
						if(flagAdd)
							i++;
					}
					else if (((double)n/100) < deleteConditionRate && list.get(i) != (double)treeDepth.get("higherLevel")){
						//System.out.println("delete");
						for(int j = 0; j < 4; j++){
							if(list.get(i) == TreeParameterSeparator)
								break;
							list.remove(i);
						}
						if(GetKeyByValue(lowSkills, (double)list.get(i)) != null){
							list.remove(i);
						}
						i--;
						changeToRandom = false;
					}
				}
				if(c == null && list.get(i) >= 950){
					//System.out.println("const");
					list.set(i, list.get(i) + rand.nextDouble()/5  - rand.nextDouble()/5);
					changeToRandom = false;
				}
				if(changeToRandom){
					list.set(i, (double)random);
				}
			}
			if(i >= 0 && list.get(i) == TreeParameterSeparator){
				break;
			}
			i++;
		}
		i++;
		for(;i < list.size(); i++){
			n = rand.nextInt(100) + 1;
			if(((double)n/100) < mutationRate){
				n = rand.nextInt(200) - 99;
				double changeValue = (n/100) + list.get(i);
				list.set(i, changeValue);
			}
		}
		
		return list;
	}
	
	
	private double RandomTreeDepth(int n){
		if(n < 35)
			return treeDepth.get("lowerLevel");
		else if(n < 50)
			return  treeDepth.get("higherLevel");
		else
			return treeDepth.get("sameLevel");
	}
	
	private Map<String,Integer> classify(double x){
		if(x < 30){
			if(x < 7){
				if(x < -5){
					return treeDepth;
				}
				else{
					return parameters;
				}
			}else{
				if(x < 14){
					return angles;
				}
				else{
					return functions;
				}
					
			}
		}
		else{
			if(x > 103){
				if(x < 113)
					return desicions;
				else
					return null;
			}
			else if(x < 100){
				return lowSkills;
			}
			else if(x > 100)
				return agentVariables;
		}
		return null;
	}
	
	/*
	 * Mutacia generacie
	 */
	public void Mutate(){
		for(int i = 0; i < populationLength; i++){
			PopulationData.set(i, mutate(PopulationData.get(i)));
		}
	}
	
	/*
	 * Vytvorit XML subor jedinca i
	 */
	public void DataToXml(int i){
		DataToXml(Population.get(i), PopulationData.get(i));
	}
	
	/*
	 * Ulozit jedinca s fitness vacsou ako LOG_FITNESS
	 */
	public void SaveBestFitnessFile(List<TestCaseResult> testResults){
		for(int i = 0; i < populationLength; i++){
			if (maxFitness < testResults.get(i).getFitness()){
				maxFitness = testResults.get(i).getFitness();
				maxFitnessFile = Population.get(i);
				if(testResults.get(i).getFitness() > LOG_FITNESS){
					try {
						Files.copy( new File(PATH + Population.get(i) + ".xml").toPath(), new File(PATH + "over1000/" + Population.get(i) + dateFormatFile.format(new Date()) + ".xml").toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		PrintWriter out;
		try {
			out =  new PrintWriter(new BufferedWriter(new FileWriter("bestFiles.txt", true)));
		
		out.println("bestfile:" + maxFitnessFile + ", fitness:" + maxFitness + ", " + dateFormat.format(new Date()) );
		out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		maxFitness = 0;
	}
	
	/*
	 * Kontrola generacie ak sa vyskytuje jedinec s cielovou fintess hodnotou alebo vyssou
	 */
	public boolean ConditionSatisfied(){
		return maxFitness >= goalFitness;
	}
	
	/*
	 * Vyber
	 */
	public void Selection(List<TestCaseResult> testResults){
		if(testResults != null){
			Random rng = new Random();
			double[] cumulativeFit = new double[Population.size()];
			cumulativeFit[0] = testResults.get(0).getFitness();
			
			for (int i = 1; i < Population.size(); i++)
			{
				double fitness = testResults.get(i).getFitness();
				cumulativeFit[i] = cumulativeFit[i - 1] + fitness;
			}
			List<List<Double>> selection = new ArrayList<List<Double>>(Population.size());
			for (int i = 0; i < Population.size(); i++)
			{
				double randomFitness = rng.nextDouble() * cumulativeFit[cumulativeFit.length - 1];
				int index = Arrays.binarySearch(cumulativeFit, randomFitness);
				if (index < 0)
				{
					// Convert negative insertion point to array index.
					index = Math.abs(index + 1);
				}
				selection.add(PopulationData.get(index));
			}
			PopulationData = selection;
		}
	}
	
	/*
	 * Krizenie
	 */
	public void Crossover(){
		int length = PopulationData.get(0).size();
		Random rng = new Random();
		for(int i = 0; i < PopulationData.size()/2; i++){
			int point = rng.nextInt(length);
			List<Double> tmp = new ArrayList<Double>();
			for(int k = 0; k < point; k++){
				tmp.add(PopulationData.get(i).get(k));
				PopulationData.get(i).set(k, PopulationData.get(i + PopulationData.size()/2).get(k));
				PopulationData.get(i + PopulationData.size()/2).set(k, tmp.get(k));
			}
		}
	}
	
	/*
	 * Nacitanie XML suborov do pol� ktore reprezentuju jedincov
	 */
	public void XmlToData(){
		for(int i = 1; i <= populationLength; i++){
			Population.add(pathOfDt + i);
			PopulationData.add(XmlToData(Population.get(i-1)));
		}
	}


	public void ReplaceXmlInData() {
		rand = new Random(System.currentTimeMillis() );
		for(int i = 0; i < populationLength; i++){
			PopulationData.set(i, XmlToData(Population.get(i)));
		}
	}
}
