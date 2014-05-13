package br.ufrj.cos.prisma.Parser;

import java.io.Console;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.ModelNode;
import br.ufrj.cos.prisma.model.XPDLGraph;
import br.ufrj.cos.prisma.util.Constants;

public class XPDLManagerv2 extends ModelParserManager {
	int vertexCount = 0;
	int edgesCount = 0;
	
	private static XPDLManagerv2 instance = new XPDLManagerv2();

	public static XPDLManagerv2 getInstance() {
		return instance;
	}
	
	private XPDLManagerv2() {
		modelGraph = new XPDLGraph(Constants.XPDLFile);
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
		
		DefaultEdge gatewayEdge = gateway.getEdges(modelGraph.getGraph()).iterator().next(); 
		ModelNode targetNode = modelGraph.getGraph().getEdgeTarget(gatewayEdge);
		return targetNode.getName();
	}

}
