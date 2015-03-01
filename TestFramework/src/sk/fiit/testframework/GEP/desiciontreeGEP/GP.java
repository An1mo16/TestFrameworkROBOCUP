package sk.fiit.testframework.GEP.desiciontreeGEP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GP {
	public int populationLength = 50;
	public List<String> Population = null; 
	public List<List<Double>> PopulationData = null;
	public double maxFittnes;
	public String maxFittnesFile;
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
}
