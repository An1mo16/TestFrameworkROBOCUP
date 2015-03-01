package sk.fiit.testframework.parsing.models.nodes;

public class EmptyNode extends NonLeafNode {

	public EmptyNode(Node parent) {
		super(NodeType.Empty,parent);
	}
	
}
