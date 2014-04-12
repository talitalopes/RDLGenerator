package br.ufrj.cos.prisma.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Document;

import br.ufrj.cos.prisma.util.Util;

public abstract class BaseGraph {

	protected DirectedGraph<ModelNode, DefaultEdge> graph;
	protected Map<ModelNode, Stack<ModelNode>> cyclesMap;
	protected ModelNode startNode;
	protected ModelNode endNode;
	protected Document doc;
	
	public BaseGraph() {
		this.graph = new DefaultDirectedGraph<ModelNode, DefaultEdge>(
				DefaultEdge.class);
		this.cyclesMap = new HashMap<ModelNode, Stack<ModelNode>>();
	}

	public BaseGraph(String model) {
		if (model == null) {
			Util.log("You must provide a valid url for a XPDL model");
			return;
		}
		
		this.graph = new DefaultDirectedGraph<ModelNode, DefaultEdge>(
				DefaultEdge.class);
		this.cyclesMap = new HashMap<ModelNode, Stack<ModelNode>>();
		this.doc = Util.getDomObject(model);
		
		createGraph();
	}
	
	protected abstract void createGraph();
	
	public void addNode(ModelNode node) {
		this.graph.addVertex(node);
	}

	public void addEdge(ModelNode source, ModelNode target) {
		this.graph.addEdge(source, target);
	}

	public void setStartNode(ModelNode node) {
		this.startNode = node;
	}

	public ModelNode getStartNode() {
		return this.startNode;
	}

	public void setEndNode(ModelNode node) {
		this.endNode = node;
	}

	public ModelNode getEndNode() {
		return this.endNode;
	}

	public Stack<ModelNode> getCycleForNode(ModelNode node) {
		return this.cyclesMap.get(node);
	}

	public DirectedGraph<ModelNode, DefaultEdge> getGraph() {
		return graph;
	}

}
