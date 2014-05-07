package br.ufrj.cos.prisma.model;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class CNetGraph extends BaseGraph {

	public CNetGraph(String model) {
		super(model);
	}

	@Override
	protected void createNodesForActivities(Map<String, ModelNode> nodesIds) {
		NodeList nodes = Util
				.getNodesWithType(doc, Constants.CNET_ACTIVITY_TAG);

		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Node parentNode = node.getParentNode();
			if (!parentNode.getNodeName().equals("node")) {
				continue;
			}
			
			String name = node.getTextContent();
			String id = ((Element)parentNode).getAttribute("id");
			ModelNode mNode = new ModelNode(id, name);
			addNode(mNode);

			nodesIds.put(id, mNode);
		}

	}

	@Override
	protected void createEdges(Map<String, ModelNode> nodesIds) {
		NodeList transitions = Util.getNodesWithType(doc,
				Constants.CNET_TRANSITION_TAG);
		for (int temp = 0; temp < transitions.getLength(); temp++) {
			Node sequenceNode = transitions.item(temp);
			if (sequenceNode == null
					|| sequenceNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element sequenceElement = (Element) sequenceNode;
			String sourceId = sequenceElement
					.getAttribute(Constants.CNET_TRANSITION_SOURCE_TAG);
			String targetId = sequenceElement
					.getAttribute(Constants.CNET_TRANSITION_TARGET_TAG);

			if (nodesIds.get(sourceId) != null
					&& nodesIds.get(targetId) != null) {
				this.graph.addEdge(nodesIds.get(sourceId),
						nodesIds.get(targetId));
			}
		}

	}

}
