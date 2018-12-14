package org.uu.nl.analyze.bca.util;

import java.util.HashMap;

import org.apache.jena.graph.Node;

/**
 * Convenience class for keeping track of the wet paint for
 * the bookmark coloring algorithm
 * @author jbaas
 *
 */
public class PaintRegistry extends HashMap<Node, Double> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Add the value to a key, or create a new 
	 * record if the key was not present before
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key.
		(A null return can also indicate that the map previously associated null with key.)
	 */
	public Double add(Node key, Double value) {
		return super.put(key, getOrDefault(key, 0d) + value);
	}
}