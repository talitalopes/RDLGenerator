package br.ufrj.cos.prisma.Parser;

import java.util.Iterator;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import br.ufrj.cos.prisma.util.Util;

public class Parser {
	private static DirectedGraph<Node, DefaultEdge> g;
	private static Node startNode;
	private static Node endNode;
	
	public static void main(String[] args) {
		g = XPDLGraph.getGraph("input/test.xpdl.xml");
		
		findStartAndEndVertexs();
		
		Util.log("Start node: " + ((Element)startNode).getAttribute("Name"));
		Util.log("End node: " + ((Element)endNode).getAttribute("Name"));
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
	
}
