package br.ufrj.cos.prisma.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.ufrj.cos.prisma.graphs.CycleFinder;
import br.ufrj.cos.prisma.graphs.DFS;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Util;

public class XPDLGraphParser {

	private static String ACTIVITY_TAG = "Activity";
	private static String TRANSITION_TAG = "Transition";

	private static Map<String, ModelNode> nodesIds = new HashMap<String, ModelNode>();

	private XPDLGraph modelGraph;
	private Document doc;

	public XPDLGraphParser(String xpdlModel) {
		if (xpdlModel == null) {
			Util.log("You must provide a valid url for a XPDL model");
			return;
		}

		this.modelGraph = new XPDLGraph();
		this.doc = Util.getDomObject(xpdlModel);

		createGraph();
	}

	public XPDLGraph getGraph() {
		return modelGraph;
	}

	private void createGraph() {
		// get activity elements
		createNodesForActivities();

		// create edges for transitions
		createEdges();

		// Find cycles
		findCycles();

		// Find start and end nodes
		findStartAndEndVertexs();
	}

	private void createNodesForActivities() {
		NodeList nodes = getNodesWithType(doc, ACTIVITY_TAG);

		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node node = nodes.item(temp);
			if (node == null || node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;
			String id = element.getAttribute("Id");

			ModelNode mNode = new ModelNode(node);
			modelGraph.addNode(mNode);

			nodesIds.put(id, mNode);
		}
	}

	private void createEdges() {
		NodeList transitions = getNodesWithType(doc, TRANSITION_TAG);
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
				modelGraph.addEdge(nodesIds.get(sourceId),
						nodesIds.get(targetId));
			}
		}
	}

	private void findCycles() {
		CycleFinder cycleFinder = new CycleFinder();
		DFS dfs = new DFS(modelGraph.getGraph());
		dfs.visit(cycleFinder);

		printCycles(cycleFinder);
	}

	private void findStartAndEndVertexs() {
		for (ModelNode n : modelGraph.getGraph().vertexSet()) {
			if (modelGraph.getGraph().inDegreeOf(n) == 0)
				modelGraph.setStartNode(n);
			if (modelGraph.getGraph().outDegreeOf(n) == 0)
				modelGraph.setEndNode(n);
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
				path += String.format(format, (cycle.get(j).getName()));
			}
			Util.log(String.format("Cycle: %d -- Path: %s", i, path));
		}
	}

	private NodeList getNodesWithType(Document doc, String type) {
		return doc.getElementsByTagName(type);
	}

}