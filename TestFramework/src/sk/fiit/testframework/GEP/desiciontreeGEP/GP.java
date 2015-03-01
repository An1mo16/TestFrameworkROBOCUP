package sk.fiit.testframework.GEP.desiciontreeGEP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import sk.fiit.testframework.init.Implementation;
import sk.fiit.testframework.init.ImplementationFactory;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.trainer.testsuite.testcases.tournament.DtWalkToBallTest;

public class GP {
	public int populationLength = 50;
	public List<String> Population = null; 
	public List<List<Double>> PopulationData = null;
	public double maxFitness;
	public double goalFitness;
	public String maxFitnessFile;
	public Random rand = new Random(System.currentTimeMillis() );
	
	Map<String, Integer> agentVariables = new HashMap<String, Integer>();
	Map<String, Integer> desicions = new HashMap<String, Integer>();
	Map<String, Integer> functions = new HashMap<String, Integer>();
	Map<String, Integer> parameters = new HashMap<String, Integer>();
	Map<String, Integer> angles = new HashMap<String, Integer>();
	Map<String, Integer> treeDepth = new HashMap<String, Integer>();
	Map<String, Integer> lowSkills = new HashMap<String, Integer>();
	
	int agentVar1;
	int agentVar2;
	int desicion1;
	int desicion2;
	int treeDepth1;
	int treeDepth2;
	int func1;
	int func2;
	int angle1;
	int angle2;
	int param1;
	int param2;
	int lowSkill1;
	int lowSkill2;
	
	private String pathOfDt;
	
	public GP(String path, double goalF){
		agentVariables.put("r", 101);
		agentVariables.put("x", 102);
		agentVariables.put("y", 103);
		agentVar1 = 101;
		agentVar2 = 103;
				
		desicions.put("lowerThen", 110);
		desicions.put("biggerThen", 111);
		desicions.put("else", 112);
		desicion1 = 110;
		desicion2 = 112;
		treeDepth.put("lowerLevel", -50);
		treeDepth.put("sameLevel", -51);
		treeDepth.put("higherLevel", -52);
		treeDepth1 = -50;
		treeDepth2 = -52;
		
		functions.put("straight", 20);
		functions.put("right_and_distant", 21);
		functions.put("right_and_distant_less", 22);
		functions.put("right_and_distant_bit", 23);
		functions.put("left_and_distant", 24);
		functions.put("left_and_distant_less", 25);
		functions.put("left_and_distant_bit", 26);
		func1 = 20;
		func2 = 26;

		parameters.put("closeDistance", 1);
		parameters.put("x1", 2);
		parameters.put("x2", 3);
		parameters.put("y1", 4);
		parameters.put("y2", 5);
		param1 = 1;
		param2 = 5;
		
		angles.put("straightRange1", 6);
		angles.put("straightRange2", 7);
		angles.put("rightRange", 8);
		angles.put("rightRange2", 9);
		angles.put("rightRange3", 10);
		angles.put("leftRange", 11);
		angles.put("leftRange2", 12);
		angles.put("leftRange3", 13);
		angle1 = 6;
		angle2 = 13;

	//	lowSkills.put("null", 49);
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
		//lowSkills.put("rollback", 73);
		lowSkill1 = 54;
		lowSkill2 = 72;
		/*lowSkills.put("turn_right_cont_10", 68);
		lowSkills.put("turn_right_cont_20", 69);
		lowSkills.put("turn_right_cont_4.5", 70);
		lowSkills.put("turn_right_cont_6.5", 71);
		lowSkills.put("turn_left_cont_10", 60);
		lowSkills.put("turn_left_cont_20", 61);
		lowSkills.put("turn_left_cont_4.5", 62);
		lowSkills.put("turn_left_cont_6.5", 63);*/
		
		pathOfDt = path;
		this.goalFitness = goalF;
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
				result.add(-51.0);
			result.addAll(getDataFromTree(desicionTree.getFirstChild()));
		}
		result.add(16.0); // separator
		
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
			if(sibling && result.get(result.size() -1) != -52) result.add(-51.0);
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
					result.add( (double)functions.get(function));
					String[] params = GetAttr(child, "params").split(",");
					int i = 0;
					for(i = 0; i < params.length; i++){
						result.add((double)angles.get(params[i]));
					}
					if(i == 1){
						result.add(0.0);
					}
				}
					
				if(!HasChildElements((Element)child)){
					if(!child.getTextContent().replaceAll("\\s+","").equals("")){
						result.add((double)lowSkills.get(child.getTextContent().replaceAll("\\s+","")));
					}

				}
				else{
					result.add(-50.0); //dalsia uroven
					result.addAll(getDataFromTree(child.getFirstChild()));
					result.add(-52.0); //vyssia uroven
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
	
	private void DataToXml(String file, List<Double> list) {
		Document doc = XmlLoader.Load(file + ".xml");
		
		NodeList desicion = doc.getElementsByTagName("desicionTree");
		Node desicionT = desicion.item(0);
		
		DeleteAllChildNodes(desicionT);
		
		int j = 0;
		Element actElement = (Element)desicionT;
		int offset = 0;
		while(list.get(j) != 16){
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
				if(!constant)param = GetKeyByValue(parameters, parameter);
				else param = String.valueOf(parameter);
				if(param.equals(""))
					 param="";
					
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
			if(list.get(j) == -50 && !lowSkillAdded){
				if(addedElement != null)
				actElement = addedElement;
			}
			else if(list.get(j) == -52){
				if(actElement.getNodeName() == "desicionTree"){
					list.set(j, -51.0);	
				}
				else{ 
					Element pomElement = (Element)actElement.getParentNode();
					if(pomElement != null && pomElement.getNodeName() != "HighSkill"){
						actElement = pomElement;
					}
				}
			}
			if(list.get(j) == 16)
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
			StreamResult result = new StreamResult(new FileOutputStream("C:/Users/Julius/Documents/workspace/Jim/dtHighskills/" + file + ".xml", false));
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
		if(param2 != null){
			param1 = param1.concat("," + param2);
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
		double mutationRate = 0.05;
		double insertConditionRate = 0.55;
		double deleteConditionRate = 0.45;
		double maxChangeValue = 0.1;
		int maxSizeList = 150;
		int n = 0;
		int random = 0;
		Map<String, Integer> c = null;
		int i = 0;
		boolean isEmpty = list.get(0) == 16;
		while(list.get(i) != 16 || isEmpty){
			c = classify(list.get(i));
			if(c != null && (c.equals(treeDepth) || c.equals(functions))){
				mutationRate =  0.10;//(100 - list.size()) * 0.05 + mutationRate ;
			}
			else{
				mutationRate = 0.05;
			}
			//System.out.println(i + ":" + list.get(i));
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
						list.set(i+1, 1000.0);
					}
				}
				
				if(c != null && c.equals(treeDepth) && list.get(i+1) != 16){
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
						if(n < 50){
							list.add(i,  1000.0);
							list.add(i, (double)rand.nextInt(desicions.size()) + Collections.min(desicions.values()));
							list.add(i, (double)rand.nextInt(agentVariables.size()) + Collections.min(agentVariables.values()));
						}
						else{
							double func = (double)rand.nextInt(functions.size()) + Collections.min(functions.values());
							if(func == functions.get("straight")){
								list.add(i,  (double)angles.get("straightRange1"));
								list.add(i,  (double)angles.get("straightRange2"));
							}
							else{
								list.add(i, 0.0);
								list.add(i, func - 13);
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
					else if (((double)n/100) > (1 -deleteConditionRate) && list.get(i) != -52){
						//System.out.println("delete");
						for(int j = 0; j < 4; j++){
							if(list.get(i) == 16)
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
			if(i >= 0 && list.get(i) == 16){
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
			if(x < 6){
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
	
	public void Mutate(){
		for(int i = 0; i < populationLength; i++){
			PopulationData.set(i, mutate(PopulationData.get(i)));
		}
	}
	
	public void DataToXml(int i){
		DataToXml(Population.get(i), PopulationData.get(i));
	}
	
	public void SaveBestFitnessFile(List<TestCaseResult> testResults){
		for(int i = 0; i < populationLength; i++){
			if (maxFitness < testResults.get(i).getFitness()){
				maxFitness = testResults.get(i).getFitness();
				maxFitnessFile = Population.get(i);
			}
		}
	}
	
	public boolean ConditionSatisfied(){
		return maxFitness >= goalFitness;
	}
	
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
	
	public void XmlToData(){
		for(int i = 1; i <= populationLength; i++){
			Population.add(pathOfDt + i);
			PopulationData.add(XmlToData(Population.get(i-1)));
		}
	}
}
