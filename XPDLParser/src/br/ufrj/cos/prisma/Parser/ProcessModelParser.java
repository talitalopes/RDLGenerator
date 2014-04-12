package br.ufrj.cos.prisma.Parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.CNetGraph;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;

public class ProcessModelParser {

	static XPDLGraph graph;
	static CNetGraph cnetGraph;
	static DirectedGraph<ModelNode, DefaultEdge> modelGraph;
	static RDLWriter rdlWriter = new RDLWriter();
	static List<ModelNode> nodesToVisit;
	static List<DefaultEdge> edgesToVisit;
	static Set<String> visited;
	static Set<DefaultEdge> visitedEdges;
	static int vertexCount = 0;
	static int edgesCount = 0;
	static boolean visitEdges = false;

	public static void main(String[] args) {
//		cnetGraph = new CNetGraph(Constants.CNETFile);
//		cnetGraph.printInfo();
		CNETParserManager.getInstance().traverseGraphv1();
	}
	
	private static void oldMain() {
		graph = new XPDLGraph(Constants.XPDLFile);
		modelGraph = graph.getGraph();

		log(String.format("Initial vertex count: %d", modelGraph.vertexSet()
				.size()));
		log(String.format("Initial edges count: %d", modelGraph.edgeSet()
				.size()));

		rdlWriter = new RDLWriter();
		rdlWriter.prepareRDLFile();
		nodesToVisit = new ArrayList<ModelNode>();
		visited = new HashSet<String>();
		visitedEdges = new HashSet<DefaultEdge>();
		edgesToVisit = new ArrayList<DefaultEdge>();

		if (visitEdges) {
			traverseGraphMarkingEdges();
		} else {
			XPDLParserManager.getInstance().traverseGraph();			
		}
		
		log(String.format("Final vertex count: %d", visited.size()));
		log(String.format("Final edges count: %d", visitedEdges.size()));
	}
	
	private static void traverseGraphMarkingEdges() {
		ModelNode rootNode = graph.getStartNode();
		addEdgesToVisit(modelGraph.outgoingEdgesOf(rootNode));
		
		while (edgesToVisit.size() > 0) {
			DefaultEdge visitingEdge = edgesToVisit.remove(0);
			ModelNode visitingNode = modelGraph.getEdgeTarget(visitingEdge);
			
			if (visitedEdges.contains(visitingEdge)) {
				continue;
			}
			
			visited.add(visitingNode.getId());
			visitedEdges.add(visitingEdge);
			addEdgesToVisit(modelGraph.outgoingEdgesOf(visitingNode));

			if (!visitingNode.isGateway() && !visitingNode.isInsideLoop()) {
				rdlWriter.addClassExtensionOrMethodExtension(visitingNode);
			} else {
				if (visitingNode.isBeginLoop()) {
					rdlWriter.addBeginLoop();
					Iterator<ModelNode> iterator = graph.getCycleForNode(visitingNode).iterator();
					iterateOverLoopOrIf(iterator);
					rdlWriter.addEndLoop();
					
//					writeLoop(visitingNode);
				} else {
					Stack<ModelNode> path = graph.findNextGateway(visitingNode);
					Iterator<ModelNode> iterator = path.iterator();
					rdlWriter.addBeginIf();
					iterateOverLoopOrIf(iterator);
					rdlWriter.addEndIf();
				}
			}

		}

	}

	private static void iterateOverLoopOrIf(Iterator<ModelNode> iter) {
		ModelNode lastNode = null;
		while (iter.hasNext()) {
			ModelNode n = iter.next();
			rdlWriter.addClassExtensionOrMethodExtension(n);
			visited.add(n.getId());
			
			Set<DefaultEdge> allEdges = modelGraph.outgoingEdgesOf(n);
			addEdgesToVisit(allEdges);

			if (lastNode != null) {
				DefaultEdge currEdge = modelGraph.getEdge(lastNode, n);
				
				if (currEdge != null) {
					while(edgesToVisit.contains(currEdge)) {
						edgesToVisit.remove(currEdge);
					}
					visitedEdges.add(currEdge);
				}
			}
			lastNode = n;
		}
	}
	
	private static void addEdgesToVisit(Set<DefaultEdge> edges) {
		Iterator<DefaultEdge> iter = edges.iterator();
		while (iter.hasNext()) {
			DefaultEdge edge = iter.next();
			edgesToVisit.add(edge);
		}
	}
	
//	private static void traverseGraph() {
//		ModelNode rootNode = graph.getStartNode();
//		nodesToVisit.add(rootNode);
//
//		while (nodesToVisit.size() > 0) {
//			ModelNode visitingNode = nodesToVisit.remove(0);
//			System.out.println("Current node: " + visitingNode.getId());
//			Set<DefaultEdge> edges = isNodeVisited(visitingNode);
//			if (edges == null || edges.size() == 0) {
//				continue;
//			}
//			vertexCount++;
//			visitingNode.setVisited(true);
//			visited.add(visitingNode.getUniqueId());
//
//			// Set<DefaultEdge> edges =
//			// modelGraph.outgoingEdgesOf(visitingNode);
//			// if (edges.size() == 0) {
//			// continue;
//			// }
//			addNodesToVisit(edges);
//
//			if (!visitingNode.isGateway() && !visitingNode.isInsideLoop()) {
////				rdlWriter.addClassExtensionOrMethodExtension(visitingNode);
//			} else {
//				if (visitingNode.isBeginLoop()) {
//					System.out.println("begin loop: " + graph.getCycleForNode(visitingNode).size());
//					for (ModelNode n : graph.getCycleForNode(visitingNode)) {
//						System.out.println(" >> Current node: " + n.getId());
//					}
//					System.out.println("end loop");
//					
////					writeLoop(visitingNode);
//				} else {
//					Stack<ModelNode> path = graph.findNextGateway(visitingNode);
//					Iterator<ModelNode> iter = path.iterator();
//					System.out.println("begin if: " + path.size());
//					while (iter.hasNext()) {
//						ModelNode n = iter.next();
//						System.out.println(" >> Current node: " + n.getId());
//					}
//					System.out.println("end if");
////					ModelNode lastNode = writeIf(visitingNode);
//					// if (isNodeVisited(visitingNode).size() > 0) {
////					nodesToVisit.add(visitingNode);
//					// }
//				}
//			}
//
//		}
//
//		rdlWriter.closeScript();
//		rdlWriter.generateRDLFile();
//		Util.log("Finished");
//	}

//	private static void addNodesToVisit(Set<DefaultEdge> edges) {
//		DefaultEdge edge = (DefaultEdge) edges.iterator().next();
//		Iterator<DefaultEdge> iter = edges.iterator();
//		edgesCount += edges.size();
//		while (iter.hasNext()) {
//			edge = iter.next();
//			nodesToVisit.add(modelGraph.getEdgeTarget(edge));
//		}
//
//	}

	public static void writeLoop(ModelNode visitingNode) {
		rdlWriter.addBeginLoop();

		for (ModelNode n : graph.getCycleForNode(visitingNode)) {
			n.setVisited(true);
			visited.add(n.getUniqueId());
			if (n.isGateway()) {
				writeIfInsideLoop(n);
			} else {
				rdlWriter.addClassExtensionOrMethodExtension(n);
			}
		}

		rdlWriter.addEndLoop();
	}

	private static void writeIfInsideLoop(ModelNode visitingNode) {
		if (modelGraph.outDegreeOf(visitingNode) <= 1) {
			return;
		}
		writeIf(visitingNode);
	}

	private static ModelNode writeIf(ModelNode visitingNode) {
		rdlWriter.addBeginIf();
		ModelNode n = writeIfActivities(visitingNode);
		rdlWriter.addEndIf();
		return n;
	}

	private static ModelNode writeIfActivities(ModelNode visitingNode) {
		if (modelGraph.outDegreeOf(visitingNode) <= 1) {
			return null;
		}

		Stack<ModelNode> path = graph.findNextGateway(visitingNode);
		Iterator<ModelNode> iter = path.iterator();
		ModelNode lastNode = null;
		while (iter.hasNext()) {
			ModelNode n = iter.next();
			if (n.isGateway() && modelGraph.outDegreeOf(n) <= 1) {
				visited.add(n.getUniqueId());
				continue;
			}
			rdlWriter.addClassExtensionOrMethodExtension(n);
			n.setVisited(true);
			visited.add(n.getUniqueId());
		}

		// lastNode.setVisited(false);
		return lastNode;
	}

//	private static Set<DefaultEdge> isNodeVisited(ModelNode n) {
//		Set<DefaultEdge> nonVisitedEdges = new HashSet<DefaultEdge>();
//		if (n == null) {
//			return nonVisitedEdges;
//		}
//
//		Set<DefaultEdge> edges = modelGraph.outgoingEdgesOf(n);
////		System.out.println("Edges: " + edges.size());
//		Iterator<DefaultEdge> iter = edges.iterator();
//
//		while (iter.hasNext()) {
//			DefaultEdge edge = iter.next();
//			if (!visited.contains(modelGraph.getEdgeTarget(edge).getUniqueId())) {
//				nonVisitedEdges.add(edge);
//			}
//			// visited = visited && modelGraph.getEdgeTarget(edge).isVisited();
//		}
////		System.out.println("nonVisitedEdges Edges: " + nonVisitedEdges.size());
//		return nonVisitedEdges;
//	}

	private static void log(String message) {
		System.out.println(message);
	}

}