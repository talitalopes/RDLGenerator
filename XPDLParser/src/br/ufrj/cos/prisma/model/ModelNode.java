package br.ufrj.cos.prisma.model;

import org.w3c.dom.Node;

public class ModelNode {

	Node node;
	boolean beginLoop;
	boolean endLoop;
	
	public ModelNode() {
	}
	
	public ModelNode(Node n) {
		this.beginLoop = false;
		this.endLoop = false;
		this.node = n;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
		
	public boolean isBeginLoop() {
		return beginLoop;
	}
	
	public void setBeginLoop(boolean beginLoop) {
		this.beginLoop = beginLoop;
	}

	public boolean isEndLoop() {
		return beginLoop;
	}
	
	public void setEndLoop(boolean endLoop) {
		this.endLoop = endLoop;
	}

}
