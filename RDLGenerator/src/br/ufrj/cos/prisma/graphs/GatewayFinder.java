package br.ufrj.cos.prisma.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import br.ufrj.cos.prisma.model.ModelNode;

public class GatewayFinder implements DfsVisitor {

	Stack<ModelNode> path = new Stack<ModelNode>();

	List<Stack<ModelNode>> cycles = new ArrayList<Stack<ModelNode>>();

	public void preorder(ModelNode v) {
		if (!v.isGateway()) {
//			v.setVisited(true);
		}
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
		path.push(v);
	}

	private void findBackEdge(ModelNode v) {
		int i = path.indexOf(v);
		if (i != -1) {
			Stack<ModelNode> cycle = new Stack<ModelNode>();
			for (int j = i; j < path.size(); j++) {
				cycle.add(path.get(j));
				path.get(j).setInsideLoop(true);

				if (j == path.size() - 1) {
					path.get(j).setBeginLoop(true);
				}
			}
			cycles.add(cycle);
		}
	}

	public List<Stack<ModelNode>> cycles() {
		return cycles;
	}
	
	public Stack<ModelNode> getPath() {
		return path;
	}

}
