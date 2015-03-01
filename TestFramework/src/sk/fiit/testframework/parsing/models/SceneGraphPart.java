package sk.fiit.testframework.parsing.models;

import java.util.*;

import sk.fiit.testframework.parsing.models.nodes.*;

public class SceneGraphPart {
	private ArrayList<Node> nodes = new ArrayList<Node>();

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
}
