package br.ufrj.cos.prisma.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		addConditionalTasks();
	}

	private void addConditionalTasks() {
		Iterator<ModelNode> iter = this.getGraph().vertexSet().iterator();
		List<DefaultEdge> edgesToDelete = new ArrayList<DefaultEdge>();

		int id = 1;
		while (iter.hasNext()) {
			ModelNode node = iter.next();

			Set<DefaultEdge> edges = node.getEdges(getGraph());
			if (edges.size() <= 1) {
				continue;
			}

			for (DefaultEdge edge : edges) {
				edgesToDelete.add(edge);
			}

		}

		for (DefaultEdge edge : edgesToDelete) {
			ModelNode sourceNode = getGraph().getEdgeSource(edge);
			ModelNode targetNode = getGraph().getEdgeTarget(edge);

			// When the target node is a gateway with only one outgoing edge, we
			// change the target node for the target node of this edge
			if (targetNode.getName().toLowerCase().contains("gateway")
					&& targetNode.getEdges(getGraph()).size() == 1) {

				DefaultEdge targetEdge = targetNode.getEdges(getGraph())
						.iterator().next();
				targetNode = getGraph().getEdgeTarget(targetEdge);
			}

			String condition = String.format("IF %s?", targetNode.getName());
			ModelNode ifNode = new ModelNode(String.valueOf(id), condition);

			addNode(ifNode);
			addEdge(sourceNode, ifNode);
			addEdge(ifNode, targetNode);
			
			id += 1;
		}

	}

	@Override
	protected void createNodesForActivities(Map<String, ModelNode> nodesIds) {
		NodeList nodes = Util
				.getNodesWithType(doc, Constants.XPDL_ACTIVITY_TAG);

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

	@Override
	protected void createEdges(Map<String, ModelNode> nodesIds) {
		NodeList transitions = Util.getNodesWithType(doc,
				Constants.XPDL_TRANSITION_TAG);
		for (int temp = 0; temp < transitions.getLength(); temp++) {
			Node sequenceNode = transitions.item(temp);
			if (sequenceNode == null
					|| sequenceNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element sequenceElement = (Element) sequenceNode;
			String sourceId = sequenceElement
					.getAttribute(Constants.XPDL_TRANSITION_SOURCE_TAG);
			String targetId = sequenceElement
					.getAttribute(Constants.XPDL_TRANSITION_TARGET_TAG);

			if (nodesIds.get(sourceId) != null
					&& nodesIds.get(targetId) != null) {
				this.graph.addEdge(nodesIds.get(sourceId),
						nodesIds.get(targetId));
			}
		}
	}

	public Stack<ModelNode> findNextGateway(ModelNode startNode) {
		GatewayFinder gtwFinder = new GatewayFinder();
		DFS dfs = new DFS(this.graph);
		dfs.dfsVisit(startNode, "GATEWAY", gtwFinder);

		return gtwFinder.getPath();
	}

}
