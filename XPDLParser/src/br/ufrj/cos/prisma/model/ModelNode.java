package br.ufrj.cos.prisma.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ModelNode {

	Node node;
	boolean beginLoop;
	boolean endLoop;
	boolean beginConditional;
	boolean endConditional;
	boolean visited;
	boolean insideLoop;
	String uniqueId;
	
	public ModelNode() {
	}
	
	public ModelNode(Node n) {
		this.beginLoop = false;
		this.endLoop = false;
		this.visited = false;
		this.beginConditional = false;
		this.endConditional = false;
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
	
	public void setBeginConditional(boolean beginConditional) {
		this.beginConditional = beginConditional;
	}

	public boolean beginConditional() {
		return beginConditional;
	}

	public void setEndConditional(boolean endConditional) {
		this.endConditional = endConditional;
	}

	public boolean endConditional() {
		return endConditional;
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
	
	public static Set<DefaultEdge> getNonVisitedEdges(DirectedGraph<ModelNode, DefaultEdge> modelGraph, ModelNode n) {
		Set<DefaultEdge> edges = new HashSet<DefaultEdge>();
		if (n == null || modelGraph == null) {
			return edges;
		}
		
		edges = modelGraph.outgoingEdgesOf(n);
		Iterator<DefaultEdge> iter = edges.iterator();
		while (iter.hasNext()) {
			DefaultEdge edge = iter.next();
			if (!modelGraph.getEdgeTarget(edge).isVisited()) {
				edges.add(edge);
			}
		}
		
		return edges;
	}
}