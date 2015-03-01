package sk.fiit.testframework.parsing.models.nodes;

import sk.fiit.robocup.library.math.TransformationMatrix;

public abstract class Node {

	private NodeType nodeType;
	private Node parent;

	public Node(NodeType nodeType, Node parent) {
		this.nodeType = nodeType;
		this.parent = parent;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}

	public TransformationMatrix getGlobalTransform() {
		if (parent != null && parent.getNodeType() == NodeType.Transform) {
			return ((TransformNode) parent).getTransformMatrix().multiply(
					parent.getGlobalTransform());
		} else {
			return TransformationMatrix.getIndetity();
		}

	}
}
