package br.ufrj.cos.prisma.Parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;

import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class XPDLParser {

	private static XPDLGraph graph;
	private static DirectedGraph<ModelNode, DefaultEdge> modelGraph;

	public static void main(String[] args) {
		graph = new XPDLGraph(Constants.XPDLFile);
		modelGraph = graph.getGraph();

		traverseGraph();
	}

	private static void traverseGraph() {
		RDLWriter rdlWriter = new RDLWriter();
		rdlWriter.prepareRDLFile();

		Set<String> visitedNodes = new HashSet<String>();

		// Root node = start Node
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		nodesToVisit.add(graph.getStartNode());
		
		boolean insideConditional = false;
		
		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			Element visitingElement = (Element) visitingNode.getNode();
			visitedNodes.add(visitingElement.getAttribute("Id"));

			if (visitingNode.isVisited()) {
				Util.log("Visited nodes wont be writen again.");
				continue;
			}

			Set<DefaultEdge> edges = modelGraph.outgoingEdgesOf(visitingNode);
			if (edges.size() == 0) {
				continue;
			}

			if (visitingNode.isBeginLoop()) {
				Util.log("Begin loop");
				rdlWriter.addBeginLoop();
				for (ModelNode n : graph.getCycleForNode(visitingNode)) {
					n.setVisited(true);
					Util.log(((Element)n.getNode()).getAttribute("Name"));
					
					if (n != visitingNode) {
						addActivityOrGateway(rdlWriter, n);
					}
				}
				Util.log("End loop");
				rdlWriter.addEndLoop();
				
			} else if (!visitingElement.getAttribute("Name")
					.contains("GATEWAY")) {
				rdlWriter.addClassExtensionOrMethodExtension(visitingNode);

			} else if (!visitingNode.isInsideLoop()) {
				if (!insideConditional) {
					rdlWriter.addBeginIf();
					insideConditional = true;
				} else {
					rdlWriter.addEndIf();
					insideConditional = false;
				}
				
			}

			DefaultEdge edge = (DefaultEdge) edges.iterator().next();
			DefaultEdge oldEdge = null;
			while (edge != null && oldEdge != edge) {
				nodesToVisit.add(modelGraph.getEdgeTarget(edge));
				oldEdge = edge;
				edge = (DefaultEdge) edges.iterator().next();
			}
		}

		rdlWriter.generateRDLFile();
		Util.log("Finished");
	}
	
	public static void addActivityOrGateway(RDLWriter rdlWriter, ModelNode visitingNode) {
		Element visitingElement = (Element)visitingNode.getNode();
		
		if (!visitingElement.getAttribute("Name").contains("GATEWAY")) {
			rdlWriter.addClassExtensionOrMethodExtension(visitingNode);
		} else {
			if (modelGraph.outgoingEdgesOf(visitingNode).size() > 1) {
				rdlWriter.addFork();
			} else if (modelGraph.incomingEdgesOf(visitingNode).size() > 1) {
				rdlWriter.addJoin();
			}
		}
	}
}
