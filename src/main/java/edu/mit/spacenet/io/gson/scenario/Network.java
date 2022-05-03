package edu.mit.spacenet.io.gson.scenario;

import java.util.List;

public class Network {
  protected List<Node> nodes;
  protected List<Edge> edges;

  public static Network createFrom(edu.mit.spacenet.domain.network.Network network,
      Context context) {
    Network n = new Network();
    n.nodes = Node.createFrom(network.getNodes(), context);
    n.edges = Edge.createFrom(network.getEdges(), context);
    return n;
  }

  public void toSpaceNet(edu.mit.spacenet.scenario.Scenario scenario, Context context) {
    for (Location node : nodes) {
      scenario.getNetwork().add(
          (edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(node.id));
    }
    for (Location edge : edges) {
      scenario.getNetwork().add(
          (edu.mit.spacenet.domain.network.edge.Edge) context.getJavaObjectFromJsonId(edge.id));
    }
  }
}
