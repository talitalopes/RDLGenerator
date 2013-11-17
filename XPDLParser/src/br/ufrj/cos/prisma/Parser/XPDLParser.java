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
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDLParser {
	
	private static XPDLGraph graph;
	private static DirectedGraph<ModelNode, DefaultEdge> modelGraph;
	private static ModelNode startNode;
	private static ModelNode endNode;

	private static BPMNCodeGenerator codegen = new BPMNCodeGenerator();

	public static void main(String[] args) {
		graph = new XPDLGraph(Constants.XPDLFile);
		modelGraph = graph.getGraph();
		
		Util.log("Start node: " + ((Element) graph.getStartNode().getNode()).getAttribute("Name"));
		Util.log("End node: " + ((Element) graph.getEndNode().getNode()).getAttribute("Name"));

		if (startNode == null || endNode == null) {
			Util.log("Couldn't find start or end node.");
			return;
		}

		prepareRDLFile();
		traverseGraph();
		generateRDLFile();
	}

	private static void traverseGraph() {
		Set<String> visitedNodes = new HashSet<String>();

		// Root node = start Node
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		nodesToVisit.add(startNode);

		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			Element visitingElement = (Element) visitingNode;
			visitedNodes.add(visitingElement.getAttribute("Id"));

			Set<DefaultEdge> edges = modelGraph.outgoingEdgesOf(visitingNode);

			// Deal with gateways
			if (edges.size() > 1) {
				boolean hasCycles = DFS(visitingNode);
				
				if (hasCycles) {
					Util.log(":" + nodesMap.get(startNode).predecessor);
					
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

	private static void addClassExtensionOrMethodExtension(ModelNode node) {
		Element el = (Element) node;

		if (el.getAttribute("Name").contains(Constants.CLASS_EXTENSION_PREFIX)) {
			String superName = el.getAttribute("Name").split("_")[1];
			// The user provides the name of the subclass
			codegen.addClassExtension(superName, Constants.PACKAGE_VAR_NAME,
					"?");
		}
	}

	private static void addGateway(int type, ModelNode node) {
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

	private static Map<ModelNode, SearchNode> nodesMap = new HashMap<ModelNode, SearchNode>();
	private static boolean DFS(ModelNode startNode) {
		Set<ModelNode> nodes = modelGraph.vertexSet();
		
		for (ModelNode n: nodes) {
			SearchNode node = new SearchNode();
			node.visited = false;
			node.predecessor = null;
			node.node = n;
			
			nodesMap.put(n, node);
		}
		
		return DFSVisit(nodesMap.get(startNode), nodesMap.get(startNode));
	}
	
	private static boolean DFSVisit(SearchNode targetNode, SearchNode node) {
		boolean hasCycles = false;
		node.visited = true;
		
		for (DefaultEdge edge: modelGraph.outgoingEdgesOf(node.node)) {
			SearchNode currentNode = nodesMap.get(modelGraph.getEdgeTarget(edge));
			
			if (currentNode == targetNode) {
				Util.log("Has cycles");
				hasCycles = true;
			}
			
			if (!currentNode.visited) {
				SearchNode sn = nodesMap.get(modelGraph.getEdgeTarget(edge));
				sn.predecessor = node.node.getNode();
				nodesMap.put(modelGraph.getEdgeTarget(edge), sn);
				
				hasCycles = DFSVisit(targetNode, nodesMap.get(modelGraph.getEdgeTarget(edge)));	
			}
		}
		
		return hasCycles;
	}
	
//	private static void smallestPath(ModelNode startNode, ModelNode endNode) {
//		List<SearchNode> path = new ArrayList<SearchNode>();
//		List<SearchNode> queue = new ArrayList<SearchNode>();
//		
//		SearchNode node = new SearchNode();
//		node.node = startNode;
//		node.predecessor = null;
//		path.add(node);
//		
//		for (DefaultEdge edge: modelGraph.outgoingEdgesOf(startNode)) {
//			SearchNode n = new SearchNode();
//			n.node = modelGraph.getEdgeTarget(edge);
//			n.predecessor = startNode.getNode();
//			queue.add(n);
//		}
//		
//		while (queue.size() > 0) {
//			SearchNode currentNode = queue.get(0);
//			
//			if (currentNode == endNode.getNode())
//				return;
//			
//		}
//	}
	
	static private class SearchNode {
		public ModelNode node;
		public Boolean visited;
		public Node predecessor;
	}

}
