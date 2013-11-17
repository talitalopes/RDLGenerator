package br.ufrj.cos.prisma.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.ufrj.cos.prisma.model.ModelNode;

/**
 * Detecting cycles in a directed graph
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * 
 */
public class CycleFinder implements DfsVisitor {

	Stack<ModelNode> path = new Stack<ModelNode>();

	List<Stack<ModelNode>> cycles = new ArrayList<Stack<ModelNode>>();

	public void preorder(ModelNode v) {
		path.push(v);
	}

	public void postorder(ModelNode v) {
		path.pop();
	}

	public void beforeChild(ModelNode v) {
		findBackEdge(v);
	}

	public void afterChild(ModelNode v) {
	}

	public void skipChild(ModelNode v) {
		findBackEdge(v);
	}

	private void findBackEdge(ModelNode v) {
	        int i = path.indexOf(v);
	        if(i != -1) {
	        	Stack<ModelNode> cycle = new Stack<ModelNode>();
	        	for (int j = i; j < path.size(); j++) {
	        		cycle.add(path.get(j));
	        		
	        		if (j == path.size() - 1) {
	        			path.get(j).setBeginLoop(true);
	        			path.get(j).setEndLoop(true);
	        		}
	        	}
	            cycles.add(cycle);
	        }       
	    }

	public List<Stack<ModelNode>> cycles() {
		return cycles;
	}

}
