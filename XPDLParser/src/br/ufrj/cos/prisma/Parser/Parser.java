package br.ufrj.cos.prisma.Parser;

import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.ufrj.cos.prisma.BPMNAPI.BPMNCodeGenerator;
import br.ufrj.cos.prisma.util.Constants;
import br.ufrj.cos.prisma.util.Util;

public class Parser {
	private static DirectedGraph<Node, DefaultEdge> g;
	private static Node startNode;
	private static Node endNode;
	
	private static BPMNCodeGenerator codegen = new BPMNCodeGenerator();
	
	public static void main(String[] args) {
		g = XPDLGraph.getGraph(Constants.XPDLFile);
		
		findStartAndEndVertexs();
		Util.log("Start node: " + ((Element)startNode).getAttribute("Name"));
		Util.log("End node: " + ((Element)endNode).getAttribute("Name"));

		if (startNode == null || endNode == null) {
			Util.log("Couldn't find start or end node.");
			return;
		}
		
		generateRDLFile();
	}
	
	private static void findStartAndEndVertexs() {
		Iterator<Node> nodes = g.vertexSet().iterator();
		
		while (nodes.hasNext()) {
			Node n = nodes.next();
			if (g.inDegreeOf(n) == 0)
				startNode = n;
			if (g.outDegreeOf(n) == 0)
				endNode = n;
		}
		
	}
	
	private void traverseGraph() {
		Set<DefaultEdge> edges = g.edgesOf(startNode);
		
		if (edges.size() > 1) {
			// Deal with gateways
		}
		
		if (edges.size() == 1) {
			// Sequencial Tasks
		}
		
	}
	
	private static void prepareRDLFile() {
		codegen.addImportArtifact(Constants.MODEL_URL);
		codegen.addExportArtifact(Constants.MODEL_OUTPUT_URL);
		
		codegen.addVarDeclaration("void", Constants.PACKAGE_VAR_NAME);
		codegen.addNewPackage("appmodel", Constants.PACKAGE_NAME); 	// TODO: confirm container value.
		codegen.addAssignment(Constants.PACKAGE_VAR_NAME, Constants.TEMP_VAR_ASSIGNMENT);
	}
	
	private static void generateRDLFile() {
		prepareRDLFile();
		codegen.generateToFile(Constants.RDL_OUTPUT);
	}
	
}
