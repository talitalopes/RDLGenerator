package br.ufrj.cos.prisma.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.prisma.graphs.CycleFinder;
import br.ufrj.cos.prisma.graphs.DFS;
import br.ufrj.cos.prisma.graphs.GatewayFinder;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDLGraph extends BaseGraph {

	public XPDLGraph() {
		super();
	}

	public XPDLGraph(String model) {
		super(model);
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
		NodeList transitions = Util.getNodesWithType(doc,
				Constants.TRANSITION_TAG);
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

		mapCycles(cycleFinder);
	}

	public Stack<ModelNode> findNextGateway(ModelNode startNode) {
		GatewayFinder gtwFinder = new GatewayFinder();
		DFS dfs = new DFS(this.graph);
		dfs.dfsVisit(startNode, "GATEWAY", gtwFinder);

		return gtwFinder.getPath();
	}

	private void findStartAndEndVertexs() {
		for (ModelNode n : this.graph.vertexSet()) {
			if (this.graph.inDegreeOf(n) == 0) {
				setStartNode(n);
			}

			if (this.graph.outDegreeOf(n) == 0) {
				setEndNode(n);
			}
		}
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
				path += String
						.format(format, ((Element) cycle.get(j).getNode())
								.getAttribute("Name"));
			}
			Util.log(String.format("Cycle: %d -- Path: %s", i, path));
		}
	}

}
