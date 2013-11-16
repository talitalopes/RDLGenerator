package br.ufrj.cos.prisma.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class Parser {
	private static DirectedGraph<Node, DefaultEdge> modelGraph;
	private static Node startNode;
	private static Node endNode;

	private static BPMNCodeGenerator codegen = new BPMNCodeGenerator();

	public static void main(String[] args) {
		modelGraph = XPDLGraph.getGraph(Constants.XPDLFile);

		findStartAndEndVertexs();
		Util.log("Start node: " + ((Element) startNode).getAttribute("Name"));
		Util.log("End node: " + ((Element) endNode).getAttribute("Name"));

		if (startNode == null || endNode == null) {
			Util.log("Couldn't find start or end node.");
			return;
		}

		prepareRDLFile();
		traverseGraph();
		generateRDLFile();
	}

	private static void findStartAndEndVertexs() {
		for (Node n: modelGraph.vertexSet()) {
			if (modelGraph.inDegreeOf(n) == 0)
				startNode = n;
			if (modelGraph.outDegreeOf(n) == 0)
				endNode = n;
		}

	}

	private static void traverseGraph() {
		Set<String> visitedNodes = new HashSet<String>();

		// Root node = start Node
		List<Node> nodesToVisit = new ArrayList<Node>();
		nodesToVisit.add(startNode);

		while (nodesToVisit.size() > 0) {
			Node visitingNode = nodesToVisit.remove(0);
			Element visitingElement = (Element) visitingNode;
			visitedNodes.add(visitingElement.getAttribute("Id"));

			Set<DefaultEdge> edges = modelGraph.outgoingEdgesOf(visitingNode);

			// Deal with gateways
			if (edges.size() > 1) {
				boolean hasCycles = DFS(visitingNode);
				
				if (hasCycles) {
					addLoop();
					addGateway(Constants.JOIN_GATEWAY, visitingNode);
				}
			}

			// Sequencial Tasks
			if (edges.size() == 1) {

				if (visitingElement.getAttribute("Name").contains("GATEWAY")) {
					addGateway(Constants.JOIN_GATEWAY, visitingNode);
				} else {
					addClassExtensionOrMethodExtension(visitingNode);
				}
			}

			if (edges.size() == 0) {
				continue;
			}
			
			DefaultEdge edge = (DefaultEdge) edges.iterator().next();
			DefaultEdge oldEdge = null;
			while (edge != null && oldEdge != edge) {
				nodesToVisit.add(modelGraph.getEdgeTarget(edge));
				oldEdge = edge;
				edge = (DefaultEdge) edges.iterator().next();
			}

//			Util.log(String.format("Name: %s, Visited Nodes: %d",
//					((Element) visitingNode).getAttribute("Name"),
//					nodesToVisit.size()));
		}

		Util.log("Finished");
	}

	private static void addClassExtensionOrMethodExtension(Node node) {
		Element el = (Element) node;

		if (el.getAttribute("Name").contains(Constants.CLASS_EXTENSION_PREFIX)) {
			String superName = el.getAttribute("Name").split("_")[1];
			// The user provides the name of the subclass
			codegen.addClassExtension(superName, Constants.PACKAGE_VAR_NAME,
					"?");
		}
	}

	private static void addGateway(int type, Node node) {
		Util.log("Add Gateway");
	}
	
	private static void addLoop() {
		Util.log("loop");
		codegen.addBeginLoopBlock("Loop?");
		//codegen.addEndLoopBlock();
	}

	private static void prepareRDLFile() {
		codegen.addImportArtifact(Constants.MODEL_URL);
		codegen.addExportArtifact(Constants.MODEL_OUTPUT_URL);

		codegen.addVarDeclaration("void", Constants.PACKAGE_VAR_NAME);
		codegen.addNewPackage("appmodel", Constants.PACKAGE_NAME); // TODO:
																	// confirm
																	// container
																	// value.
		codegen.addAssignment(Constants.PACKAGE_VAR_NAME,
				Constants.TEMP_VAR_ASSIGNMENT);
	}

	private static void generateRDLFile() {
		codegen.generateToFile(Constants.RDL_OUTPUT);
	}

	private static Map<Node, DFSNode> nodesMap = new HashMap<Node, DFSNode>();
	private static boolean DFS(Node startNode) {
		Set<Node> nodes = modelGraph.vertexSet();
		
		for (Node n: nodes) {
			DFSNode node = new DFSNode();
			node.visited = false;
			node.predecessor = null;
			node.node = n;
			
			nodesMap.put(n, node);
		}
		
		return DFSVisit(nodesMap.get(startNode), nodesMap.get(startNode));
	}
	
	private static boolean DFSVisit(DFSNode targetNode, DFSNode node) {
		boolean hasCycles = false;
		node.visited = true;
		
		for (DefaultEdge edge: modelGraph.outgoingEdgesOf(node.node)) {
			DFSNode currentNode = nodesMap.get(modelGraph.getEdgeTarget(edge));
			
			if (currentNode == targetNode) {
				Util.log("Has cycles");
				hasCycles = true;
			}
			
			if (!currentNode.visited) {
				nodesMap.get(modelGraph.getEdgeTarget(edge)).predecessor = node.node;
				hasCycles = DFSVisit(targetNode, nodesMap.get(modelGraph.getEdgeTarget(edge)));	
			}
		}
		
		return hasCycles;
	}
		
	static private class DFSNode {
		public Node node;
		public Boolean visited;
		public Node predecessor;
	}

}
