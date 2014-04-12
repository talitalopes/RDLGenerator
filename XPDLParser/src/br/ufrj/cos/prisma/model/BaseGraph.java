package br.ufrj.cos.prisma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Document;

import br.ufrj.cos.prisma.graphs.CycleFinder;
import br.ufrj.cos.prisma.graphs.DFS;
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

	protected void createGraph() {
		Map<String, ModelNode> nodesIds = new HashMap<String, ModelNode>();

		// get activity elements
		createNodesForActivities(nodesIds);

		// create edges for transitions
		createEdges(nodesIds);

		// Find cycles
		findCycles();

		// Find start and end nodes
		findStartAndEndVertexs();
	}

	protected abstract void createNodesForActivities(
			Map<String, ModelNode> nodesIds);

	protected abstract void createEdges(Map<String, ModelNode> nodesIds);

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

	protected void findStartAndEndVertexs() {
		for (ModelNode n : this.graph.vertexSet()) {
			if (this.graph.inDegreeOf(n) == 0) {
				setStartNode(n);
			}

			if (this.graph.outDegreeOf(n) == 0) {
				setEndNode(n);
			}
		}
	}

	// ------------------------
	// Cycles finder methods |
	// ------------------------

	protected void findCycles() {
		CycleFinder cycleFinder = new CycleFinder();
		DFS dfs = new DFS(this.graph);
		dfs.visit(cycleFinder);

		mapCycles(cycleFinder);
	}

	private void mapCycles(CycleFinder cycleFinder) {
		List<Stack<ModelNode>> cycles = cycleFinder.cycles();

		for (int i = 0; i < cycles.size(); i++) {
			Stack<ModelNode> cycle = cycleFinder.cycles().get(i);
			ModelNode loopBegin = cycle.get(0); // cycle.size() - 1
			this.cyclesMap.put(loopBegin, cycle);

			String path = "";
			for (int j = 0; j < cycle.size(); j++) {
				String format = (j == cycle.size() - 1) ? "%s" : "%s --> ";
				path += String.format(format, (cycle.get(j).getName()));
			}
			Util.log(String.format("Cycle: %d -- Path: %s", i, path));
		}
	}

	public void printInfo() {
		System.out.println(String.format("Nodes: %d, edges: %d", this.graph
				.vertexSet().size(), this.graph.edgeSet().size()));
		System.out.println("Start Node: " + getStartNode().name);
		System.out.println("End Node: " + getEndNode().name);
	}
}
