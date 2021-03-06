package br.ufrj.cos.prisma.Parser;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.BaseGraph;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.StringUtils;
import br.ufrj.cos.prisma.util.Util;

public class ModelParserManager {
	protected BaseGraph modelGraph;
	protected int vertexCount = 0;
	protected int edgesCount = 0;

	public void visitGraph() {
		ModelNode rootNode = modelGraph.getStartNode();
		DefaultEdge nextEdge = visitNode(rootNode);
		
		while (nextEdge != null) {
			ModelNode nextNode = modelGraph.getGraph().getEdgeTarget(nextEdge);
			nextEdge = visitNode(nextNode);
		}
	}
	
	public DefaultEdge visitNode(ModelNode node) {
		Set<DefaultEdge> edges = node.getEdges(modelGraph.getGraph());

		if (edges.size() == 1) {
			print(node.getName());
			return (edges.size() > 0) ? edges.iterator().next() : null;
		}
		
		if (edges.size() == 0) {
			return null;
		}
		
		DefaultEdge edge = askForUserDecision(edges);
		if (edge == null) {
			print("Couldn't get next node.");
			return null;
		}
		
		return edge;
	}

	public void traverseGraph() {
		ModelNode rootNode = modelGraph.getStartNode();
		traverseFromNode(rootNode, 0);
	}

	public void traverseFromNode(ModelNode rootNode, int level) {
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();
		nodesToVisit.add(rootNode);

		while (nodesToVisit.size() > 0) {
			ModelNode visitingNode = nodesToVisit.remove(0);
			Util.log("Current node", visitingNode.getName() + " "
					+ visitingNode.getId());

			if (visitingNode.beginConditional() && !visitingNode.isVisited()) {
				// mark as visited
				visitingNode.setVisited(true);
				int condLevel = level + 1;
				printBeginConditional(visitingNode, condLevel);
				traverseFromNode(visitingNode, condLevel);
				printEndConditional(condLevel);
				continue;
			}

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
			}
		}
	}
	
	protected DefaultEdge askForUserDecision(Set<DefaultEdge> edges) {
		String options = "";

		Iterator<DefaultEdge> iter = edges.iterator();
		int index = 0;
		while (iter.hasNext()) {
			index++;
			DefaultEdge edge = iter.next();
			ModelNode targetNode = modelGraph.getGraph().getEdgeTarget(edge);
			String reuseActionDescription = targetNode.getName().replace(
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

	protected boolean isValidInput(String str, int limit) {
		String format = String.format("[1-%d]", limit);
		Pattern p = Pattern.compile(format);
		if (p.matcher(str).find()) {
			return true;
		}
		return false;
	}
	
	protected void print(String message) {
		Console console = System.console();
		if (console == null) {
			System.out.println("Unable to fetch console");
			return;
		}
		console.printf(message + "\n");
	}
	
	protected void printBeginConditional(ModelNode n, int level) {
		String conditionalStr = String.format("IF (%s)? {", n.getName());
		printCodeLine(conditionalStr, level - 1);
		printNode(n, level);
	}

	protected void printEndConditional(int level) {
		printCodeLine("}\n", level - 1);
	}

	protected void printNode(ModelNode n, int level) {
		printCodeLine(n.getName(), level);
	}

	protected void printCodeLine(String string, int level) {
		String tab = "  ";
		String tabForLevel = StringUtils.repeat(tab, level);
		print(String.format("%s%s", tabForLevel, string));
	}

	protected List<ModelNode> addNodesToVisit(Set<DefaultEdge> edges) {
		List<ModelNode> nodesToVisit = new ArrayList<ModelNode>();

		boolean beginConditional = edges.size() > 1;
		DefaultEdge edge = (DefaultEdge) edges.iterator().next();
		Iterator<DefaultEdge> iter = edges.iterator();
		edgesCount += edges.size();

		while (iter.hasNext()) {
			edge = iter.next();
			ModelNode node2Visit = modelGraph.getGraph().getEdgeTarget(edge);
			node2Visit.setBeginConditional(beginConditional);
			nodesToVisit.add(node2Visit);
		}

		return nodesToVisit;
	}
}
