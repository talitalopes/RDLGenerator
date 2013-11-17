package br.ufrj.cos.prisma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.prisma.graphs.CycleFinder;
import br.ufrj.cos.prisma.graphs.DFS;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDLGraph {

	DirectedGraph<ModelNode, DefaultEdge> graph;
	ModelNode startNode;
	ModelNode endNode;
	Document doc;
	
	public XPDLGraph() {
		this.graph = new DefaultDirectedGraph<ModelNode, DefaultEdge>(
				DefaultEdge.class);
	}
	
	public XPDLGraph(String model) {
		if (model == null) {
			Util.log("You must provide a valid url for a XPDL model");
			return;
		}
		
		this.graph = new DefaultDirectedGraph<ModelNode, DefaultEdge>(
				DefaultEdge.class);
		this.doc = Util.getDomObject(model);
		
		createGraph();
	}
	
	private void createGraph() {
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
	
	private void createNodesForActivities(Map<String, ModelNode> nodesIds) {
		NodeList nodes = Util.getNodesWithType(doc, Constants.ACTIVITY_TAG);

		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;
			String id = element.getAttribute("Id");

			ModelNode mNode = new ModelNode(node);
			addNode(mNode);

			nodesIds.put(id, mNode);
		}
	}

	private void createEdges(Map<String, ModelNode> nodesIds) {
		NodeList transitions = Util.getNodesWithType(doc, Constants.TRANSITION_TAG);
		for (int temp = 0; temp < transitions.getLength(); temp++) {
			Node sequenceNode = transitions.item(temp);
			if (sequenceNode == null
					|| sequenceNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element sequenceElement = (Element) sequenceNode;
			String sourceId = sequenceElement.getAttribute("From");
			String targetId = sequenceElement.getAttribute("To");

			if (nodesIds.get(sourceId) != null
					&& nodesIds.get(targetId) != null) {
				this.graph.addEdge(nodesIds.get(sourceId),
						nodesIds.get(targetId));
			}
		}
	}

	private void findCycles() {
		CycleFinder cycleFinder = new CycleFinder();
		DFS dfs = new DFS(this.graph);
		dfs.visit(cycleFinder);

		printCycles(cycleFinder);
	}
	
	private void findStartAndEndVertexs() {
		for (ModelNode n: this.graph.vertexSet()) {
			if (this.graph.inDegreeOf(n) == 0)
				setStartNode(n);
			if (this.graph.outDegreeOf(n) == 0)
				setEndNode(n);
		}
	}
	
	private void printCycles(CycleFinder cycleFinder) {
		List<Stack<ModelNode>> cycles = cycleFinder.cycles();
		Util.log("Detected Cycles: " + cycles.size());

		for (int i = 0; i < cycles.size(); i++) {
			Stack<ModelNode> cycle = cycleFinder.cycles().get(i);

			String path = "";
			for (int j = 0; j < cycle.size(); j++) {
				String format = (j == cycle.size() - 1) ? "%s" : "%s --> ";
				path += String
						.format(format, ((Element) cycle.get(j).getNode())
								.getAttribute("Name"));
			}
			Util.log(String.format("Cycle: %d -- Path: %s", i, path));
		}
	}
	
	public void addNode (ModelNode node) {
		this.graph.addVertex(node);
	}

	public void addEdge (ModelNode source, ModelNode target) {
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

	public DirectedGraph<ModelNode, DefaultEdge> getGraph() {
		return graph;
	}
}
