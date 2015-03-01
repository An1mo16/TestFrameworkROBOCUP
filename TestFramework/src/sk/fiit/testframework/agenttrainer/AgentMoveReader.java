/**
 * Name:    MoveEditor.java
 * Created: 22.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import sk.fiit.testframework.agenttrainer.models.*;

import static org.w3c.dom.Node.ELEMENT_NODE;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Represents a class responsible for reading agent moves specified in XML
 * format.
 * 
 * @author Roman Kovac
 * 
 */
public class AgentMoveReader {

	public AgentMove read(String fileName) throws Exception {
		File file = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
	
		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		Document doc = builder.parse(file);

		return read(doc);
	}

	public AgentMove read(InputStream inputStream) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		Document doc = builder.parse(inputStream);

		return read(doc);
	}

	private AgentMove read(Document doc) throws Exception {
		AgentMove agentMove = new AgentMove();
		agentMove.setDoc(doc);

		Node phasesNode = findChildNode(doc.getFirstChild(), "phases");
		fillPhases(agentMove, phasesNode);

		return agentMove;
	}

	
	private void fillPhases(AgentMove agentMove, Node phasesNode)
			throws Exception {
		NodeList phases = phasesNode.getChildNodes();

		ArrayList<AgentMovePhase> agentMovePhases = new ArrayList<AgentMovePhase>();

		for (int i = 0; i < phases.getLength(); i++) {
			if (phases.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			AgentMovePhase agentMovePhase = new AgentMovePhase();

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
			agentMovePhase.setDuration(Integer.parseInt(durationNode
					.getTextContent()));
			agentMovePhases.add(agentMovePhase);
		}

		agentMove.setPhases(agentMovePhases);
	}

	private void fillEffectors(AgentMovePhase agentMovePhase, Node effectorsNode)
			throws Exception {
		NodeList effectors = effectorsNode.getChildNodes();

		ArrayList<AgentMoveEffector> agentMoveEffectors = new ArrayList<AgentMoveEffector>();

		for (int i = 0; i < effectors.getLength(); i++) {
			if (effectors.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			NamedNodeMap attributes = effectors.item(i).getAttributes();
			AgentMoveEffector effector = new AgentMoveEffector();
			
			effector.setName(effectors.item(i).getNodeName());
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attributes.item(j).getNodeName().equals("end")) {
					effector.setEnd(Integer.parseInt(attributes.item(j).getNodeValue()));
				}
			}
			
			agentMoveEffectors.add(effector);
		}
		
		agentMovePhase.setEffectors(agentMoveEffectors);
	}

	static Node findChildNode(Node node, String name) throws Exception {
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
