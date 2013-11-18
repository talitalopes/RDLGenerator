package br.ufrj.cos.prisma.model;

import org.w3c.dom.Node;

public class ModelNode {

	Node node;
	boolean beginLoop;
	boolean visited;
	boolean insideLoop;
	
	public ModelNode() {
	}
	
	public ModelNode(Node n) {
		this.beginLoop = false;
		this.visited = false;
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

	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isInsideLoop() {
		return insideLoop;
	}

	public void setInsideLoop(boolean insideLoop) {
		this.insideLoop = insideLoop;
	}

}