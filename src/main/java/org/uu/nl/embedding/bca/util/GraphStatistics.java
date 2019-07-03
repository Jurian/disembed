package org.uu.nl.embedding.bca.util;

import grph.Grph;
import grph.properties.NumericalProperty;
import grph.properties.Property;
import org.uu.nl.embedding.convert.util.NodeInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * This class will collect all the necessary information about the graph
 * in an efficient manner by only traversing it once.
 * @author Jurian Baas
 */
public class GraphStatistics {

	/**
	 * The type (URI, blank, literal, predicate) for each node in the graph
	 */
	public final byte[] types;
	/**
	 * All the unique nodes in the graph from which 
	 * we can start the bookmark coloring algorithm
	 */
	public final int[] jobs;
	/**
	 * The string representations of all the nodes in the graph
	 */
	public final String[] dict;

	public final int[] weights;

	private int uriNodeCount;
	private int blankNodeCount;
	private int literalNodeCount;
	public final int nrOfVertices;
	public final int nrOfEdges;
	public final int totalNodeCount;
	public final int nrOfEdgeTypes;
	
	public GraphStatistics(Grph graph, Map<String, Integer> weightMap, boolean includePredicates) {
		
		final Property labelProperties = graph.getVertexLabelProperty();
		final NumericalProperty typeProperties = graph.getVertexColorProperty();
		final Property edgeLabels = graph.getEdgeLabelProperty();

		nrOfVertices = graph.getVertices().size();
		nrOfEdges = graph.getEdges().size();

		final Map<Integer, Integer> edgeTypes = new HashMap<>();
		for(int i = 0; i < nrOfEdges; i++) {
			int type = graph.getEdgeColorProperty().getValueAsInt(i);
			edgeTypes.putIfAbsent(type, i);
		}

		nrOfEdgeTypes = edgeTypes.size();

		int vocabSize = includePredicates ? nrOfVertices + nrOfEdgeTypes : nrOfVertices;

		/*
		 * A mapping between nodes and a unique index, used in the bookmark
		 * coloring algorithm to do look-ups in constant time.
		 */
		final int[] keys = graph.getVertices().toIntArray();
		this.types = new byte[vocabSize];
		this.dict = new String[vocabSize];
		this.jobs = new int[nrOfVertices];
		
		//int job_i = 0;
		for(int i = 0; i < nrOfVertices; i++) {
			int node = keys[i];
			types[i] = (byte) typeProperties.getValue(node);
			dict[i] = labelProperties.getValueAsString(node);
			jobs[i] = node;

			if(types[i] == NodeInfo.BLANK) blankNodeCount++;
			else if(types[i] == NodeInfo.URI) {
				uriNodeCount++;
				//jobs[job_i++] = node;
			}
			else if(types[i] == NodeInfo.LITERAL) literalNodeCount++;
		}

		weights = new int[nrOfEdgeTypes];
		for(Map.Entry<Integer, Integer> entry : edgeTypes.entrySet()) {

			int type = entry.getKey();
			int edge = entry.getValue();

			String label = edgeLabels.getValueAsString(edge);
			if(weightMap.containsKey(label))
				weights[type] = weightMap.get(label);

			if(includePredicates) {
				dict[type + nrOfVertices] = label;
				types[type + nrOfVertices] = NodeInfo.PREDICATE;
			}
		}

		this.totalNodeCount = uriNodeCount + blankNodeCount + literalNodeCount;
	}
}