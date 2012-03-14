package com.thinkaurelius.titan.core;

/**
 * Holds statistical information about the stored graph.
 * 
 * This interface provides functionality to retrieve statistical information about the graph stored in
 * a graph database. Use {@link com.thinkaurelius.titan.core.GraphDatabase#getStatistics()} to get statistical information for a particular
 * graph database instance.
 * 
 * 
 * @author	Matthias Br&ouml;cheler (me@matthiasb.com);
 * @since	0.2.4
 * 
 */
public interface GraphStatistics {

	/**
	 * Returns the number of relationships stored in the graph database.
	 * 
	 * @return Number of relationships stored in the graph database.
	 */
	public long getNoRelationships();
	
	/**
	 * Returns the number of edges stored in the graph database (i.e. properties and relationships).
	 * 
	 * @return Number of properties stored in the graph database.
	 */
	public long getNoProperties();
	
	/**
	 * Returns the number of edges stored in the graph database (i.e. properties and relationships).
	 * 
	 * @return Number of edges stored in the graph database (i.e. properties and relationships).
	 */
	public long getNoEdges();
	
	/**
	 * Returns the number of nodes stored in the graph database.
	 * 
	 * @return Number of nodes stored in the graph database.
	 */
	public long getNoNodes();
	
	/**
	 * Returns the number of edge types stored in the graph database.
	 * 
	 * @return Number of edge types stored in the graph database.
	 */
	public long getNoEdgeTypes();
	
	/**
	 * Returns the number of edges of a given edge type.
	 * 
	 * @param edgeTypeName {@link com.thinkaurelius.titan.core.EdgeType} for which to retrieve the number of stored edges.
	 * @return Number of edges of a given edge type.
	 */
	public long getNoEdges(String edgeTypeName);
	
	/**
	 * Returns the number of edges in a given edge type group.
	 * 
	 * @param group {@link com.thinkaurelius.titan.core.EdgeTypeGroup} for which to retrieve the number of stored edges.
	 * @return Number of edges in a given edge type group.
	 */
	public long getNoEdges(EdgeTypeGroup group);
	
}
