package br.ufrj.cos.prisma.Parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Node;

public class Parser {

	public static void main(String[] args) {
		DirectedGraph<Node, DefaultEdge> g = XPDLGraph.getGraph("input/test.xpdl.xml");
	}
}
