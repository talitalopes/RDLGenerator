package br.ufrj.cos.prisma.graphs;

import br.ufrj.cos.prisma.model.ModelNode;

/**
 * Adapted from cycle-finder.groovy 
 * {@link} https://gist.github.com/jarek-przygodzki/5390434
 * **/
public interface DfsVisitor {

	public void preorder(ModelNode v);

	public void postorder(ModelNode v);

	public void beforeChild(ModelNode v);

	public void afterChild(ModelNode v);

	public void skipChild(ModelNode v);
}
