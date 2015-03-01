package sk.fiit.testframework.parsing.models.nodes;

public class PredefinedMesh extends Node {

	private String type;
	private String[] params;

	private double scaleX;
	private double scaleY;
	private double scaleZ;

	public PredefinedMesh(Node parent) {
		super(NodeType.PredefinedMesh,parent);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String[] getParams() {
		return params;
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
