package org.uu.nl.embedding.analyze.glove.opt;

import java.util.concurrent.Callable;

public abstract class GloveJob implements Callable<Double> {
	
	final int id;
	
	public GloveJob(int id) {
		this.id = id;
	}
}
