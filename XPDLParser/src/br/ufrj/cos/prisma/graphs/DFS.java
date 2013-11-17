package br.ufrj.cos.prisma.graphs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.w3c.dom.Node;

/**
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * **/

public class DFS {
	
	/**
	 * Complete graph 
	 * **/
	DirectedGraph<Node, DefaultEdge> g;
	
	/**
     * Set V of vertices
     */
    public Set<Node> V;
     
    public Map<Node, Boolean> visited;
    
    public DFS(DirectedGraph<Node, DefaultEdge> g) {
    	this.g = g; 
        this.V   = g.vertexSet();

        this.visited = new HashMap<Node, Boolean>();
        for (Node v: V) {
        	this.visited.put(v, false);
        }
    }
 
    public void visit(DfsVisitor visitor) {
      for(Node u : V) {
            if(!visited.get(u)) {
                dfsVisit(u, visitor);
            }
        } 
    }
    
    private void dfsVisit(Node u, DfsVisitor visitor) {
        visited.put(u, true);
        
        visitor.preorder(u);
        
        for(DefaultEdge edge : g.outgoingEdgesOf(u)) {
        	Node v = g.getEdgeTarget(edge);
        	
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
}
