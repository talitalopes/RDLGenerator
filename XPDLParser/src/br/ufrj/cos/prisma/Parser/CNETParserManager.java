package br.ufrj.cos.prisma.Parser;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.CNetGraph;
import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.StringUtils;
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

	public void visitGraph() {
		ModelNode rootNode = cnetGraph.getStartNode();
		visitNode(rootNode);
	}

	public void visitNode(ModelNode node) {
		Set<DefaultEdge> edges = node.getEdges(cnetGraph.getGraph());

		if (edges.size() == 1) {
			print(node.getName());
			return;
		}

		DefaultEdge edge = askForUserDecision(edges);
		if (edge == null) {
			print("Couldn't get next node.");
			return;
		}
		
		String target = cnetGraph.getGraph().getEdgeTarget(edge).getName();
		print(target);
	}

	private DefaultEdge askForUserDecision(Set<DefaultEdge> edges) {
		String options = "";

		Iterator<DefaultEdge> iter = edges.iterator();
		int index = 0;
		while (iter.hasNext()) {
			index++;
			DefaultEdge edge = iter.next();
			ModelNode targetNode = cnetGraph.getGraph().getEdgeTarget(edge);
			String reuseActionDescription = targetNode.getName().replace(
					"CLASS_EXTENSION_", "ClassExtension(");
			options += String
					.format("[%d] %s\n", index, reuseActionDescription);
		}

		options += "Please, select one of the options above";

		System.out.println(options);
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

	private boolean isValidInput(String str, int limit) {
		String format = String.format("[1-%d]", limit);
		Pattern p = Pattern.compile(format);
		if (p.matcher(str).find()) {
			return true;
		}
		return false;
	}
	
	private void print(String message) {
		Console console = System.console();
		if (console == null) {
			System.out.println("Unable to fetch console");
			return;
		}
		console.printf(message + "\n");
	}
	
	public void traverseGraph() {
		ModelNode rootNode = cnetGraph.getStartNode();
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
					cnetGraph.getGraph(), visitingNode);
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

	private void printBeginConditional(ModelNode n, int level) {
		String conditionalStr = String.format("IF (%s)? {", n.getName());
		printCodeLine(conditionalStr, level - 1);
		printNode(n, level);
	}

	private void printEndConditional(int level) {
		printCodeLine("}\n", level - 1);
	}

	private void printNode(ModelNode n, int level) {
		printCodeLine(n.getName(), level);
	}

	private void printCodeLine(String string, int level) {
		String tab = "  ";
		String tabForLevel = StringUtils.repeat(tab, level);
		print(String.format("%s%s", tabForLevel, string));
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
