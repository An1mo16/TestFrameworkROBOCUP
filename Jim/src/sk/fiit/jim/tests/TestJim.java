package sk.fiit.jim.tests;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import sk.fiit.jim.agent.Planner;
import sk.fiit.jim.agent.communication.Communication;
import sk.fiit.jim.init.ScriptBoot;
import sk.fiit.jim.init.SkillsFromXmlLoader;
//import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.robocup.library.init.Script;

/**
 *  TestJim.java
 *  
 *@Title	Jim
 *@author	Androids
 */
/*public class TestJim {

	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	public void init() {
		Planner.setPlanningScript(Script.createScript("TestInitPlan.instance.control"));
	}
	
	public void run() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		new SkillsFromXmlLoader(new File("./moves")).load();
		ScriptBoot.boot();
		//dependency injection is set in dependencies.rb => no need to do them manually
		System.runFinalization();
		System.gc();
		
//		 executorService.submit(new Runnable() {
//	            @Override
//	            public void run() {
//	                try {
//	                	Communication.getInstance().start();
//	                } catch (IOException ex) {
//	                    Logger.getLogger(TestCase.class.getName()).log(Level.SEVERE, null, ex);
//	                }
//	            }
//	        });
		 
		
	}
	
	public void setPlanningScript(Script script) {
		Planner.setPlanningScript(script);
		
	}
}*/
