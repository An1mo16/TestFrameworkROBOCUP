package sk.fiit.testframework.parsing;


import sk.fiit.robocup.library.math.TransformationMatrix;
import sk.fiit.testframework.parsing.models.*;
import sk.fiit.testframework.parsing.models.nodes.*;

public class SceneGraphPartParser extends SExpressionParser {

	public SceneGraphPart parse(String sceneGraphPart) {
		SceneGraphPart result = new SceneGraphPart();

		int currentIndex = 1; // skip first bracket
		int length = sceneGraphPart.length() - 1;

		while (currentIndex < length) {
			String part = getWholeSection(sceneGraphPart, currentIndex);
			processPart(part, result, null, 0);
			currentIndex += part.length();
		}

		return result;
	}

	private NodeType getNodeType(String part, int currentIndex) {
		// part starts with (nd

		if (part.charAt(currentIndex + 3) != ' ') {
			return NodeType.Empty;
		} else if (part.startsWith("TRF ", currentIndex + 4) || part.startsWith("(SLT", currentIndex + 4)) {
			return NodeType.Transform;
		} else if (part.startsWith("Light ", currentIndex + 4)) {
			return NodeType.Light;
		} else if (part.startsWith("StaticMesh ",currentIndex + 4)) {
			return NodeType.StaticMesh;
		} else if (part.startsWith("SMN ",currentIndex + 4)) {
			return NodeType.PredefinedMesh;
		}
		return NodeType.Empty;
	}

	private void processPart(String part, SceneGraphPart sceneGraphPart,
			NonLeafNode parentNode, int currentIndex) {
		NodeType nodeType = getNodeType(part, currentIndex);
		switch (nodeType) {
		case Empty:
			processEmptyType(part, sceneGraphPart, parentNode, currentIndex);
			break;
//			if (parentNode!=null) {
//				parentNode.getChildren().add(new EmptyNode(parentNode));
//			} else {
//				sceneGraphPart.getNodes().add(new EmptyNode(parentNode));
//			}
//			break;
		case Transform:
			processTransformType(part, sceneGraphPart, parentNode, currentIndex);
			break;
		case Light:
			//ignore
			break;
		case StaticMesh:
			processStaticMeshType(part, sceneGraphPart, parentNode, currentIndex);
			break;
		case PredefinedMesh:
			processPredefinedMeshType(part, sceneGraphPart, parentNode, currentIndex);
			break;
		}
		
	}

	private void processEmptyType(String part,
			SceneGraphPart sceneGraphPart, NonLeafNode parentNode,
			int currentIndex) {
		currentIndex++;
		
		currentIndex = moveToNextSection(part,currentIndex);
		EmptyNode node = new EmptyNode(parentNode);
		
		if (parentNode != null) {
			parentNode.getChildren().add(node);
		} else {
			sceneGraphPart.getNodes().add(node);
		}
		if (currentIndex==-1) return;
		
		while (currentIndex < part.length() - 1) {
			String section = getWholeSection(part, currentIndex);
			processPart(section, sceneGraphPart, node, 0);

			currentIndex += section.length();
		}

	}
	
	private void processTransformType(String part,
			SceneGraphPart sceneGraphPart, NonLeafNode parentNode,
			int currentIndex) {
		currentIndex++;
		
		currentIndex = moveToNextSection(part,currentIndex);
		TransformNode node = new TransformNode(parentNode);
		
		if (parentNode != null) {
			parentNode.getChildren().add(node);
		} else {
			sceneGraphPart.getNodes().add(node);
		}
		while (currentIndex < part.length() - 1) {
			String section = getWholeSection(part, currentIndex);
			if (section.startsWith("(SLT")) {
				String[] data = processSExpression(section);
				double[] values = new double[]
                    {
						Double.parseDouble(data[1]),
						Double.parseDouble(data[2]),
						Double.parseDouble(data[3]),
						Double.parseDouble(data[4]),
						Double.parseDouble(data[5]),
						Double.parseDouble(data[6]),
						Double.parseDouble(data[7]),
						Double.parseDouble(data[8]),
						Double.parseDouble(data[9]),
						Double.parseDouble(data[10]),
						Double.parseDouble(data[11]),
						Double.parseDouble(data[12]),
						Double.parseDouble(data[13]),
						Double.parseDouble(data[14]),
						Double.parseDouble(data[15]),
						Double.parseDouble(data[16])
                    };
				node.setTransformMatrix(new TransformationMatrix(values));
			} else {
				processPart(section, sceneGraphPart, node, 0);
			}
			currentIndex += section.length();
		}

	}
	
	private void processStaticMeshType(String part,
			SceneGraphPart sceneGraphPart, NonLeafNode parentNode,
			int currentIndex) {
		currentIndex++;
		
		currentIndex = moveToNextSection(part,currentIndex);
		StaticMeshNode node = new StaticMeshNode(parentNode);
		
		if (parentNode != null) {
			parentNode.getChildren().add(node);
		} else {
			sceneGraphPart.getNodes().add(node);
		}
		
		String[] data;
		
		while (currentIndex < part.length() - 1) {
			if (part.charAt(currentIndex) != '(') {
				currentIndex=moveToNextSection(part, currentIndex);
			}
			String section = getWholeSection(part, currentIndex);
			data = processSExpression(section);
			if (data[0].equals("load")) {
				node.setModel(data[1]);	
			} else if (data[0].equals("sSc")) {
				node.setScaleX(Double.parseDouble(data[1]));
				node.setScaleY(Double.parseDouble(data[2]));
				node.setScaleZ(Double.parseDouble(data[3]));	
			}
			currentIndex+=section.length();	
		}
	}
	
	private void processPredefinedMeshType(String part,
			SceneGraphPart sceneGraphPart, NonLeafNode parentNode,
			int currentIndex) {
		currentIndex++;
		
		currentIndex = moveToNextSection(part,currentIndex);
		PredefinedMesh node = new PredefinedMesh(parentNode);
		
		if (parentNode != null) {
			parentNode.getChildren().add(node);
		} else {
			sceneGraphPart.getNodes().add(node);
		}
		
		String[] data;
		
		while (currentIndex < part.length() - 1) {
			if (part.charAt(currentIndex) != '(') {
				currentIndex=moveToNextSection(part, currentIndex);
			}
			String section = getWholeSection(part, currentIndex);
			data = processSExpression(section);
			if (data[0].equals("load")) {
				node.setType(data[1]);
				if (data.length>2) {
					String[] params = new String[data.length-2];
					for (int i=2;i<data.length;i++) {
						params[i-2]=data[i];
					}
					node.setParams(params);
				}	
			} else if (data[0].equals("sSc")) {
				node.setScaleX(Double.parseDouble(data[1]));
				node.setScaleY(Double.parseDouble(data[2]));
				node.setScaleZ(Double.parseDouble(data[3]));	
			}
			currentIndex+=section.length();	
		}
		
	}
}
