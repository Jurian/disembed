package org.uu.nl.embedding;

import org.uu.nl.embedding.glove.opt.Optimum;
import org.uu.nl.embedding.pca.PCA;

/**
 * @author Jurian Baas
 */
public abstract class Embedding {
	
	protected int dimension;
	private final CRecMatrix coMatrix;
	private Optimum optimum;
	
	protected Embedding(int dim, CRecMatrix coMatrix) {
		this.coMatrix = coMatrix;
		this.dimension = dim;
	}

	public int getDimension() {
		return dimension;
	}

	public CRecMatrix getCoMatrix() {
		return coMatrix;
	}

	public Optimum getOptimum() { return optimum; }

	public void setOptimum(Optimum optimum) {
		this.optimum = optimum;
	}

	public abstract void setDimension(int dimension);

	public void updateOptimum(PCA.Projection projection) {
		this.optimum.setResult(projection.getProjection());
		this.dimension = projection.getnCols();
	}
}
