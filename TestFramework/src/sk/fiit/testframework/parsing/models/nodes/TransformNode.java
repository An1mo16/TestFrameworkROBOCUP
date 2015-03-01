package sk.fiit.testframework.parsing.models.nodes;

import sk.fiit.robocup.library.math.TransformationMatrix;

public class TransformNode extends NonLeafNode {

	TransformationMatrix transformMatrix;

	public TransformNode(Node parent) {
		super(NodeType.Transform, parent);
	}

	public TransformationMatrix getTransformMatrix() {
		return transformMatrix;
	}

	public void setTransformMatrix(TransformationMatrix transformMatrix) {
		this.transformMatrix = transformMatrix;
	}

}
