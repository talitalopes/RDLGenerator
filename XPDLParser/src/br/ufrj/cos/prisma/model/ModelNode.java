package br.ufrj.cos.prisma.model;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ModelNode {

	Node node;
	boolean beginLoop;
	boolean endLoop;
	boolean endIf;
	boolean visited;
	boolean insideLoop;
	String uniqueId;
	
	public ModelNode() {
	}
	
	public ModelNode(Node n) {
		this.beginLoop = false;
		this.endLoop = false;
		this.visited = false;
		this.node = n;
		this.uniqueId = this.getId()+""+ this.getName();
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
		return endLoop;
	}
	
	public void setEndIf(boolean endIf) {
		this.endIf = endIf;
	}

	public boolean isEndIf() {
		return endIf;
	}
	
	public void setEndLoop(boolean endLoop) {
		this.endLoop = endLoop;
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
	
	public boolean isGateway() {
		return getName().contains("GATEWAY");
	}

	public String getName() {
		Element cElement = (Element) this.getNode();
		return cElement.getAttribute("Name");
	}

	public String getId() {
		Element cElement = (Element) this.getNode();
		return cElement.getAttribute("Id");
	}

	public String getUniqueId() {
		return this.uniqueId;
	}
}