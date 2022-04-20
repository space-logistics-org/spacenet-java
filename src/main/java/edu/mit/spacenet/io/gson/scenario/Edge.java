package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.network.edge.EdgeType;

public abstract class Edge {
	public static final BiMap<String, EdgeType> TYPE_MAP = new ImmutableBiMap.Builder<String, EdgeType>()
			.put("SurfaceEdge", EdgeType.SURFACE)
			.put("SpaceEdge", EdgeType.SPACE)
			.put("FlightEdge", EdgeType.FLIGHT)
			.build();
	
	public UUID id;
	public String name;
	public String description;
	public UUID origin;
	public UUID destination;
	
	public static Edge createFrom(edu.mit.spacenet.domain.network.edge.Edge edge, Context context) {
		if(edge.getEdgeType() == EdgeType.SURFACE) {
			return SurfaceEdge.createFrom((edu.mit.spacenet.domain.network.edge.SurfaceEdge) edge, context);
		} else if(edge.getEdgeType() == EdgeType.FLIGHT) {
			return FlightEdge.createFrom((edu.mit.spacenet.domain.network.edge.FlightEdge) edge, context);
		} else if(edge.getEdgeType() == EdgeType.SPACE) {
			return SpaceEdge.createFrom((edu.mit.spacenet.domain.network.edge.SpaceEdge) edge, context);
		} else {
			throw new UnsupportedOperationException("unknown edge type: " + edge.getEdgeType());
		}
	}
	
	public abstract edu.mit.spacenet.domain.network.edge.Edge toSpaceNet(Context context);
}
