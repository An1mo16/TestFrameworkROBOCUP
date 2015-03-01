/**
 * Name:    AgentMoveConfigReader.java
 * Created: 8.5.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer;

import static org.w3c.dom.Node.ELEMENT_NODE;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import sk.fiit.testframework.agenttrainer.models.AgentMoveConfigEffector;
import sk.fiit.testframework.agenttrainer.models.AgentMoveConfigPhase;
import sk.fiit.testframework.agenttrainer.models.AgentMoveConfiguration;
import sk.fiit.testframework.agenttrainer.models.Calculated;
import sk.fiit.testframework.agenttrainer.models.Range;

/**
 * TODO: Replace with a brief purpose of class / interface.
 * 
 * @author Roman Kovac
 * 
 */
public class AgentMoveConfigReader {

	public AgentMoveConfiguration read(String fileName) throws Exception {
		File file = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		Document doc = builder.parse(file);

		return read(doc);
	}

	private AgentMoveConfiguration read(Document doc) throws Exception {
		AgentMoveConfiguration config = new AgentMoveConfiguration();

		Node phasesNode = findChildNode(doc.getFirstChild(), "phases");
		fillPhases(config, phasesNode);

		return config;
	}

	private void fillPhases(AgentMoveConfiguration config, Node phasesNode)
			throws Exception {
		NodeList phases = phasesNode.getChildNodes();

		ArrayList<AgentMoveConfigPhase> agentMovePhases = new ArrayList<AgentMoveConfigPhase>();

		for (int i = 0; i < phases.getLength(); i++) {
			if (phases.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			AgentMoveConfigPhase agentMovePhase = new AgentMoveConfigPhase();
						
			NamedNodeMap attributes = phases.item(i).getAttributes();

			// phase name
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attributes.item(j).getNodeName().equals("name")) {
					agentMovePhase.setName(attributes.item(j).getNodeValue());
				}
			}

			// effectors
			Node effectorsNode = findChildNode(phases.item(i), "effectors");
			fillEffectors(agentMovePhase, effectorsNode);

			// duration
			Node durationNode = findChildNode(phases.item(i), "duration");
			if (!fillCalculatedValue(agentMovePhase.getDuration(), durationNode)) {
				fillRangeValue(agentMovePhase.getDuration().getValue(), durationNode);
			}
			
			agentMovePhases.add(agentMovePhase);
		}

		config.setPhases(agentMovePhases);
	}

	private void fillEffectors(AgentMoveConfigPhase agentMovePhase, Node effectorsNode)
			throws Exception {
		NodeList effectors = effectorsNode.getChildNodes();

		ArrayList<AgentMoveConfigEffector> agentMoveEffectors = new ArrayList<AgentMoveConfigEffector>();

		for (int i = 0; i < effectors.getLength(); i++) {
			if (effectors.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			AgentMoveConfigEffector effector = new AgentMoveConfigEffector();

			effector.setName(effectors.item(i).getNodeName());
			if (!fillCalculatedValue(effector, effectors.item(i))) {
				fillRangeValue(effector.getValue(), effectors.item(i));
			}

			agentMoveEffectors.add(effector);
		}

		agentMovePhase.setEffectors(agentMoveEffectors);
	}
	
	private boolean fillCalculatedValue(Calculated object, Node node) {
		NamedNodeMap attributes = node.getAttributes();
		
		Node synPhase = attributes.getNamedItem("synPhase");
		if (synPhase==null) {
			object.setIsCalculated(false);
			return false;
		}
		
		object.setSynPhase(synPhase.getNodeValue());
		
		Node synEffector = attributes.getNamedItem("synEffector");
		if (synEffector!=null) {
			object.setSynEffector(synEffector.getNodeValue());
		}
		
		Node constant = attributes.getNamedItem("const");
		if (constant!=null) {
			object.setConstant(Integer.parseInt(constant.getNodeValue()));
		}
		
		object.setIsCalculated(true);
		
		return true;
	}
	
	private void fillRangeValue(Range object, Node node) {
		NamedNodeMap attributes = node.getAttributes();
		
		Node from = attributes.getNamedItem("from");		
		object.setFrom(Integer.parseInt(from.getNodeValue()));
		
		Node to = attributes.getNamedItem("to");		
		object.setTo(Integer.parseInt(to.getNodeValue()));
		
		Node step = attributes.getNamedItem("step");		
		object.setStep(Integer.parseInt(step.getNodeValue()));
		
	}

	private Node findChildNode(Node node, String name) throws Exception {
		NodeList rootNodeChildren = node.getChildNodes();
		for (int i = 0; i < rootNodeChildren.getLength(); i++) {
			if (rootNodeChildren.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			if (rootNodeChildren.item(i).getNodeName().equals(name)) {
				return rootNodeChildren.item(i);
			}
		}

		throw new Exception("Could not find the " + name + " node.");
	}
}
