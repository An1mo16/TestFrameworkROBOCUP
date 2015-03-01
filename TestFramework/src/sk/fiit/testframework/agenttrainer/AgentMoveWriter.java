/**
 * Name:    AgentMoveWriter.java
 * Created: 25.4.2011
 * 
 * @author: Roman Kovac
 */
package sk.fiit.testframework.agenttrainer;

import static org.w3c.dom.Node.ELEMENT_NODE;

import java.io.*;
import java.util.ArrayList;
import sk.fiit.testframework.agenttrainer.models.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * Represents a class responsible for writing agent moves into XML.
 * 
 * @author Roman Kovac
 * 
 */
public class AgentMoveWriter {

	public void write(AgentMove agentMove, String fileName) throws Exception {
		write(agentMove);
		
		Document doc = agentMove.getDoc();

		Source source = new DOMSource(doc);
		
		File file = new File(fileName);
		Result result = new StreamResult(file);

		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}
	
	public void write(AgentMove agentMove, OutputStream outputStream) throws Exception {
		write(agentMove);
		
		Document doc = agentMove.getDoc();

		Source source = new DOMSource(doc);
		
		Result result = new StreamResult(outputStream);

		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}
	
	private void write(AgentMove agentMove) throws Exception {
		Document doc = agentMove.getDoc();

		Node phasesNode = AgentMoveReader.findChildNode(doc.getFirstChild(),
				"phases");
		fillPhases(agentMove, phasesNode);
	}

	private void fillPhases(AgentMove agentMove, Node phasesNode)
			throws Exception {
		NodeList phases = phasesNode.getChildNodes();

		ArrayList<AgentMovePhase> agentMovePhases = agentMove.getPhases();

		int counter = 0;
		for (int i = 0; i < phases.getLength(); i++) {
			if (phases.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			AgentMovePhase agentMovePhase = agentMovePhases.get(counter);

			// effectors
			Node effectorsNode = AgentMoveReader.findChildNode(phases.item(i),
					"effectors");
			fillEffectors(agentMovePhase, effectorsNode);

			// duration
			Node durationNode = AgentMoveReader.findChildNode(phases.item(i),
					"duration");
			durationNode.setTextContent(String.format("%s",
					agentMovePhase.getDuration()));

			counter++;
		}

		agentMove.setPhases(agentMovePhases);
	}

	private void fillEffectors(AgentMovePhase agentMovePhase, Node effectorsNode)
			throws Exception {
		NodeList effectors = effectorsNode.getChildNodes();

		ArrayList<AgentMoveEffector> agentMoveEffectors = agentMovePhase
				.getEffectors();

		int counter = 0;

		for (int i = 0; i < effectors.getLength(); i++) {
			if (effectors.item(i).getNodeType() != ELEMENT_NODE)
				continue;

			NamedNodeMap attributes = effectors.item(i).getAttributes();
			AgentMoveEffector effector = agentMoveEffectors.get(counter);

			effector.setName(effectors.item(i).getNodeName());
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attributes.item(j).getNodeName().equals("end")) {
					attributes.item(j).setNodeValue(
							String.format("%s", effector.getEnd()));
				}
			}

			counter++;
		}

	}
}
