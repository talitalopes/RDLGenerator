package br.ufrj.cos.prisma.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.w3c.dom.Node;

/**
 * Detecting cycles in a directed graph
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * 
 */
public class CycleFinder implements DfsVisitor {

	Stack<Node> path = new Stack<Node>();

	List<Stack<Node>> cycles = new ArrayList<Stack<Node>>();

	public void preorder(Node v) {
		System.out.println("preorder");
		path.push(v);
	}

	public void postorder(Node v) {
		System.out.println("postorder");
		path.pop();
	}

	public void beforeChild(Node v) {
		System.out.println("beforeChild");
		findBackEdge(v);
	}

	public void afterChild(Node v) {
		System.out.println("afterChild");
	}

	public void skipChild(Node v) {
		findBackEdge(v);
	}

	private void findBackEdge(Node v) {
	        int i = path.indexOf(v);
	        if(i != -1) {
	        	Stack<Node> cycle = new Stack<Node>();
	        	for (int j = i; j < path.size(); j++) {
	        		cycle.add(path.get(j)); // new ArrayList(path[i..-1])
	        	}
	            cycles.add(cycle);
	        }       
	    }

	public List<Stack<Node>> cycles() {
		return cycles;
	}

}
