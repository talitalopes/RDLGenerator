package br.ufrj.cos.prisma.graphs;

import org.w3c.dom.Node;

/**
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * **/
public interface DfsVisitor {

	public void preorder(Node v);

	public void postorder(Node v);

	public void beforeChild(Node v);

	public void afterChild(Node v);

	public void skipChild(Node v);
}
