package edu.mit.spacenet.io.gson.scenario;

import java.util.List;

public class Network {
	public List<Node> nodes;
	public List<Edge> edges;
	
	public static Network createFrom(edu.mit.spacenet.domain.network.Network network, Context context) {
		Network n = new Network();
		n.nodes = Node.createFrom(network.getNodes(), context);
		n.edges = Edge.createFrom(network.getEdges(), context);
		return n;
	}
	
	public void toSpaceNet(edu.mit.spacenet.scenario.Scenario scenario, Context context) {
		for(Location node : nodes) {
			scenario.getNetwork().add(node.toSpaceNet(context));
		}
		for(Location edge : edges) {
			scenario.getNetwork().add(edge.toSpaceNet(context));
		}
	}
}
