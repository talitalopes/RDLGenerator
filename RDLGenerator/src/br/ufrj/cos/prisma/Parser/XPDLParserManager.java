package br.ufrj.cos.prisma.Parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDLParserManager {
	XPDLGraph graph;
	DirectedGraph<ModelNode, DefaultEdge> modelGraph;
	RDLWriter rdlWriter = new RDLWriter();
	List<ModelNode> nodesToVisit;
	Set<String> visited;
	List<DefaultEdge> edgesToVisit;
	Set<DefaultEdge> visitedEdges;
	int vertexCount = 0;
	int edgesCount = 0;
	boolean visitEdges = true;

	private static XPDLParserManager instance = new XPDLParserManager();

	public static XPDLParserManager getInstance() {
		return instance;
	}

	private XPDLParserManager() {
		graph = new XPDLGraph(Constants.XPDLFile);
		modelGraph = graph.getGraph();

		Util.log(String.format("Initial vertex count: %d", modelGraph
				.vertexSet().size()));
		Util.log(String.format("Initial edges count: %d", modelGraph.edgeSet()
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

		Util.log(String.format("Final vertex count: %d", visited.size()));
		Util.log(String.format("Final edges count: %d", visitedEdges.size()));
	}

	private void traverseGraphMarkingEdges() {
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
					Iterator<ModelNode> iterator = graph.getCycleForNode(
							visitingNode).iterator();
					iterateOverLoopOrIf(iterator);
					rdlWriter.addEndLoop();

					// writeLoop(visitingNode);
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

	private void iterateOverLoopOrIf(Iterator<ModelNode> iter) {
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
					while (edgesToVisit.contains(currEdge)) {
						edgesToVisit.remove(currEdge);
					}
					visitedEdges.add(currEdge);
				}
			}
			lastNode = n;
		}
	}

	private void addEdgesToVisit(Set<DefaultEdge> edges) {
		Iterator<DefaultEdge> iter = edges.iterator();
		while (iter.hasNext()) {
			DefaultEdge edge = iter.next();
			edgesToVisit.add(edge);
		}
	}

	private void traverseGraph() {
		ModelNode rootNode = graph.getStartNode();
		nodesToVisit.add(rootNode);

		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			Util.log("Current node: " + visitingNode.getId());
			Set<DefaultEdge> edges = isNodeVisited(visitingNode);
			if (edges == null || edges.size() == 0) {
				continue;
			}
			vertexCount++;
			visitingNode.setVisited(true);
			visited.add(visitingNode.getUniqueId());

			// Set<DefaultEdge> edges =
			// modelGraph.outgoingEdgesOf(visitingNode);
			// if (edges.size() == 0) {
			// continue;
			// }
			addNodesToVisit(edges);

			if (!visitingNode.isGateway() && !visitingNode.isInsideLoop()) {
				// rdlWriter.addClassExtensionOrMethodExtension(visitingNode);
			} else {
				if (visitingNode.isBeginLoop()) {
					Util.log("begin loop: "
							+ graph.getCycleForNode(visitingNode).size());
					for (ModelNode n : graph.getCycleForNode(visitingNode)) {
						Util.log(" >> Current node: " + n.getId());
					}
					Util.log("end loop");

					// writeLoop(visitingNode);
				} else {
					Stack<ModelNode> path = graph.findNextGateway(visitingNode);
					Iterator<ModelNode> iter = path.iterator();
					Util.log("begin if: " + path.size());
					while (iter.hasNext()) {
						ModelNode n = iter.next();
						Util.log(" >> Current node: " + n.getId());
					}
					Util.log("end if");
					// ModelNode lastNode = writeIf(visitingNode);
					// if (isNodeVisited(visitingNode).size() > 0) {
					// nodesToVisit.add(visitingNode);
					// }
				}
			}

		}

		rdlWriter.closeScript();
		rdlWriter.generateRDLFile();
		Util.log("Finished");
	}

	private void addNodesToVisit(Set<DefaultEdge> edges) {
		DefaultEdge edge = (DefaultEdge) edges.iterator().next();
		Iterator<DefaultEdge> iter = edges.iterator();
		edgesCount += edges.size();
		while (iter.hasNext()) {
			edge = iter.next();
			nodesToVisit.add(modelGraph.getEdgeTarget(edge));
		}
	}

	/**
	 * Helper method to identify nodes visited previously. For each edge of the
	 * target node, it is verified if the edge's target node was already visited
	 * 
	 * @param ModelNode
	 *            n -> the target node
	 * **/
	private Set<DefaultEdge> isNodeVisited(ModelNode n) {
		Set<DefaultEdge> nonVisitedEdges = new HashSet<DefaultEdge>();
		if (n == null) {
			return nonVisitedEdges;
		}

		Set<DefaultEdge> edges = modelGraph.outgoingEdgesOf(n);
		// Util.log("Edges: " + edges.size());
		Iterator<DefaultEdge> iter = edges.iterator();

		while (iter.hasNext()) {
			DefaultEdge edge = iter.next();
			if (!visited.contains(modelGraph.getEdgeTarget(edge).getUniqueId())) {
				nonVisitedEdges.add(edge);
			}
			// visited = visited && modelGraph.getEdgeTarget(edge).isVisited();
		}
		
		Util.log("nonVisitedEdges Edges: " + nonVisitedEdges.size());
		return nonVisitedEdges;
	}

	public void writeLoop(ModelNode visitingNode) {
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

	private void writeIfInsideLoop(ModelNode visitingNode) {
		if (modelGraph.outDegreeOf(visitingNode) <= 1) {
			return;
		}
		writeIf(visitingNode);
	}

	private ModelNode writeIf(ModelNode visitingNode) {
		rdlWriter.addBeginIf();
		ModelNode n = writeIfActivities(visitingNode);
		rdlWriter.addEndIf();
		return n;
	}

	private ModelNode writeIfActivities(ModelNode visitingNode) {
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
}
