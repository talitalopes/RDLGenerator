package br.ufrj.cos.prisma.model;

import java.util.Map;
import java.util.Stack;

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
