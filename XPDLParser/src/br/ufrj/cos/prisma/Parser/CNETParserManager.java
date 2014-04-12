package br.ufrj.cos.prisma.Parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.CNetGraph;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class CNETParserManager {
	CNetGraph cnetGraph;
	int vertexCount = 0;
	int edgesCount = 0;

	private static CNETParserManager instance = new CNETParserManager();

	public static CNETParserManager getInstance() {
		return instance;
	}

	private CNETParserManager() {
		cnetGraph = new CNetGraph(Constants.CNETFile);
		cnetGraph.printInfo();
	}

	public void traverseGraphv1() {
		ModelNode rootNode = cnetGraph.getStartNode();
		traverseFromNode(rootNode);
	}

	public void traverseFromNode(ModelNode rootNode) {
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		nodesToVisit.add(rootNode);

		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			Util.log("Current node", visitingNode.getName() + " "
					+ visitingNode.getId());

			if (visitingNode.beginConditional() && !visitingNode.isVisited()) {
				// mark as visited
				visitingNode.setVisited(true);

				printBeginConditional(visitingNode);
				traverseFromNode(visitingNode);
				printEndConditional();
				continue;
			}

			// get edges that need to be visited
			Set<DefaultEdge> edges = ModelNode.getNonVisitedEdges(
					cnetGraph.getGraph(), visitingNode);
			if (edges == null || edges.size() == 0) {
				// No edges to add
				continue;
			}
			nodesToVisit.addAll(addNodesToVisit(edges));

			if (!visitingNode.isVisited()) {
				printNode(visitingNode);
				visitingNode.setVisited(true);
			}
		}
	}

	private void printBeginConditional(ModelNode n) {
		System.out.println(String.format("IF( %s?)?", n.getName()));
		System.out.println(n.getName());
	}

	private void printEndConditional() {
		System.out.println("}");
	}

	private void printNode(ModelNode n) {
		System.out.println(n.getName());
	}

	private List<ModelNode> addNodesToVisit(Set<DefaultEdge> edges) {
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		
		boolean beginConditional = edges.size() > 1;
		DefaultEdge edge = (DefaultEdge) edges.iterator().next();
		Iterator<DefaultEdge> iter = edges.iterator();
		edgesCount += edges.size();
		
		while (iter.hasNext()) {
			edge = iter.next();
			ModelNode node2Visit = cnetGraph.getGraph().getEdgeTarget(edge);
			node2Visit.setBeginConditional(beginConditional);
			nodesToVisit.add(node2Visit);
		}

		return nodesToVisit;
	}

}
