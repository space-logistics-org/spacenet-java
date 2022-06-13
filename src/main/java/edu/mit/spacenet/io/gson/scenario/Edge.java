package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.network.edge.EdgeType;

public abstract class Edge extends Location {
  public static final BiMap<String, EdgeType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, EdgeType>().put("Surface Edge", EdgeType.SURFACE)
          .put("Space Edge", EdgeType.SPACE).put("Flight Edge", EdgeType.FLIGHT).build();

  protected UUID origin_id;
  protected UUID destination_id;

  public static Edge createFrom(edu.mit.spacenet.domain.network.edge.Edge edge, Context context) {
    if (edge.getEdgeType() == EdgeType.SURFACE) {
      return SurfaceEdge.createFrom((edu.mit.spacenet.domain.network.edge.SurfaceEdge) edge,
          context);
    } else if (edge.getEdgeType() == EdgeType.FLIGHT) {
      return FlightEdge.createFrom((edu.mit.spacenet.domain.network.edge.FlightEdge) edge, context);
    } else if (edge.getEdgeType() == EdgeType.SPACE) {
      return SpaceEdge.createFrom((edu.mit.spacenet.domain.network.edge.SpaceEdge) edge, context);
    } else {
      throw new UnsupportedOperationException("unknown edge type: " + edge.getEdgeType());
    }
  }

  public static List<Edge> createFrom(
      Collection<? extends edu.mit.spacenet.domain.network.edge.Edge> edges, Context context) {
    List<Edge> es = new ArrayList<Edge>();
    for (edu.mit.spacenet.domain.network.edge.Edge e : edges) {
      es.add(Edge.createFrom(e, context));
    }
    return es;
  }

  @Override
  public abstract edu.mit.spacenet.domain.network.edge.Edge toSpaceNet(Context context);

  public static List<edu.mit.spacenet.domain.network.edge.Edge> toSpaceNet(Collection<Edge> edges,
      Context context) {
    List<edu.mit.spacenet.domain.network.edge.Edge> es =
        new ArrayList<edu.mit.spacenet.domain.network.edge.Edge>();
    if (edges != null) {
      for (Edge e : edges) {
        es.add(e.toSpaceNet(context));
      }
    }
    return es;
  }
}
