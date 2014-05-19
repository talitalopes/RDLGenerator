package br.ufrj.cos.prisma.Parser;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;

public class XPDLManagerv2 extends ModelParserManager {
	int vertexCount = 0;
	int edgesCount = 0;
	BPMNCodeGenerator codeGen;

	private static XPDLManagerv2 instance = new XPDLManagerv2();

	public static XPDLManagerv2 getInstance() {
		return instance;
	}

	private XPDLManagerv2() {
		modelGraph = new XPDLGraph(Constants.XPDLFile);
		codeGen = new BPMNCodeGenerator();
	}

	protected DefaultEdge askForUserDecision(Set<DefaultEdge> edges) {
		String options = "";

		Iterator<DefaultEdge> iter = edges.iterator();
		int index = 0;
		while (iter.hasNext()) {
			index++;
			DefaultEdge edge = iter.next();
			ModelNode targetNode = modelGraph.getGraph().getEdgeTarget(edge);

			String reuseActionDescription = targetNode.getName();
			if (targetNode.getName().toLowerCase().contains("gateway")) {
				reuseActionDescription = dealWithGateway(targetNode);
			}

			reuseActionDescription = reuseActionDescription.replace(
					"CLASS_EXTENSION_", "ClassExtension(");
			options += String
					.format("[%d] %s\n", index, reuseActionDescription);
		}

		options += "Please, select one of the options above";

		print(options);
		Console console = System.console();
		if (console == null) {
			System.out.println("Unable to fetch console");
			return null;
		}

		String line = console.readLine();
		boolean valid = isValidInput(line, edges.size());
		if (!valid) {
			console.printf(
					"Your input should be in range [1-%d]. Please, enter your option again.\n",
					edges.size());
			return askForUserDecision(edges);
		}

		int optionIndex = Integer.parseInt(line) - 1;
		return (DefaultEdge) edges.toArray()[optionIndex];
	}

	private String dealWithGateway(ModelNode gateway) {
		if (gateway.getEdges(modelGraph.getGraph()).size() == 0) {
			return "";
		}

		if (gateway.getEdges(modelGraph.getGraph()).size() > 1) {
			return "Other options";
		}

		DefaultEdge gatewayEdge = gateway.getEdges(modelGraph.getGraph())
				.iterator().next();
		ModelNode targetNode = modelGraph.getGraph().getEdgeTarget(gatewayEdge);
		return targetNode.getName();
	}

	// DFS

	public void traverseGraph() {
		super.traverseGraph();
		codeGen.generateToFile("output/gef.bpmn");
	}
	
	public void traverseFromNode(ModelNode rootNode, int level) {
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		nodesToVisit.add(rootNode);

		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			// Util.log("Current node: %s", visitingNode.getName() + " "
			// + visitingNode.getId());

			if (visitingNode.isGateway() && !visitingNode.isVisited()) {
				visitingNode.setVisited(true);
				int condLevel = level + 0;
				// printBeginConditional(visitingNode, condLevel);
				traverseFromNode(visitingNode, condLevel);
				continue;
			}

			// if (visitingNode.beginConditional() && !visitingNode.isVisited())
			// {
			// // mark as visited
			// visitingNode.setVisited(true);
			// int condLevel = level + 1;
			// printBeginConditional(visitingNode, condLevel);
			// traverseFromNode(visitingNode, condLevel);
			// // printEndConditional(condLevel);
			// continue;
			// }

			// get edges that need to be visited
			Set<DefaultEdge> edges = ModelNode.getNonVisitedEdges(
					modelGraph.getGraph(), visitingNode);
			if (edges == null || edges.size() == 0) {
				// No edges to add
				continue;
			}
			nodesToVisit.addAll(addNodesToVisit(edges));

			if (!visitingNode.isVisited()) {
				printNode(visitingNode, level);
				visitingNode.setVisited(true);

				if (visitingNode.getName().toLowerCase().contains("if ")) {
					traverseFromNode(visitingNode, 0);
//					visitingNode.setVisited(false);
				}

			} /* else if (visitingNode.getNextNode(modelGraph.getGraph()) != null) {
				if (visitingNode.getIncomingEdges(modelGraph.getGraph()).size() == 1
						&& visitingNode.conditionalChild(modelGraph.getGraph())) {
					String source = visitingNode.getName();
					String target = visitingNode.getNextNode(modelGraph.getGraph()).getName();
					String sequenceFlow = String.format("%s_%s", source, target);
					System.out.println("sequence: " + sequenceFlow);
				}
			}*/
		}
	}

	protected void printNode(ModelNode n, int level) {
		if (n.getName().toLowerCase().contains("if ")) {
			codeGen.addBeginIfBlock(n.getName());
			return;
		}
		
		String className = n.getName().split("CLASS_EXTENSION_")[0];
		codeGen.addClassExtension(className, "test", "?");
	}
}
