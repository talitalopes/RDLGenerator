package br.ufrj.cos.prisma.graphs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import br.ufrj.cos.prisma.model.ModelNode;

/**
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * **/

public class DFS {
	
	/**
	 * Complete graph 
	 * **/
	DirectedGraph<ModelNode, DefaultEdge> g;
	
	/**
     * Set V of vertices
     */
    public Set<ModelNode> V;
     
    public Map<ModelNode, Boolean> visited;
    
    public DFS(DirectedGraph<ModelNode, DefaultEdge> g) {
    	this.g = g; 
        this.V   = g.vertexSet();

        this.visited = new HashMap<ModelNode, Boolean>();
        for (ModelNode v: V) {
        	this.visited.put(v, false);
        }
    }
 
    public void visit(DfsVisitor visitor) {
      for(ModelNode u : V) {
            if(!visited.get(u)) {
                dfsVisit(u, visitor);
            }
        } 
    }

    private void dfsVisit(ModelNode u, DfsVisitor visitor) {
        visited.put(u, true);
        
        visitor.preorder(u);
        
        for(DefaultEdge edge : g.outgoingEdgesOf(u)) {
        	ModelNode v = g.getEdgeTarget(edge);
        	
            if(!visited.get(v)) {
                visitor.beforeChild(v);
                dfsVisit(v, visitor);
                visitor.afterChild(v);
            } else {
                visitor.skipChild(v);
            }
        }
        visitor.postorder(u);
    }
    
    public void dfsVisit(ModelNode startNode, String nodeName, DfsVisitor visitor) {
    	visited.put(startNode, true);
        
        visitor.preorder(startNode);
        
        for(DefaultEdge edge : g.outgoingEdgesOf(startNode)) {
        	ModelNode v = g.getEdgeTarget(edge);
        	
        	if (v != startNode && v.getName().contains(nodeName)) {
        		visitor.skipChild(v);
        		v.setEndIf(true);
        		v.setVisited(false);
        		return;
        	}
        	
            if(!visited.get(v)) {
                visitor.beforeChild(v);
                dfsVisit(v, nodeName, visitor);
                visitor.afterChild(v);
            } else {
                visitor.skipChild(v);
            }
        }
        visitor.postorder(startNode);
    }
}
