package sk.fiit.testframework.parsing.models.nodes;

import java.util.*;

public abstract class NonLeafNode extends Node {

	ArrayList<Node> children = new ArrayList<Node>();
	
	public NonLeafNode(NodeType nodeType, Node parent)
	{
		super(nodeType,parent);
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
}
