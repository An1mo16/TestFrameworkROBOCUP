package sk.fiit.testframework.parsing.models.nodes;

public class StaticMeshNode extends Node {

	private String model;
	private double scaleX;
	private double scaleY;
	private double scaleZ;

	public StaticMeshNode(Node parent) {
		super(NodeType.StaticMesh, parent);
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleZ(double scaleZ) {
		this.scaleZ = scaleZ;
	}

	public double getScaleZ() {
		return scaleZ;
	}

}
