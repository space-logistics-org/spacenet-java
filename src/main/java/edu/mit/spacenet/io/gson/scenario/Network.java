package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

public class Network {
	public List<Node> nodes = new ArrayList<Node>();
	public List<Edge> edges = new ArrayList<Edge>();
	
	public static Network createFrom(edu.mit.spacenet.domain.network.Network network, Context context) {
		Network n = new Network();
		for(edu.mit.spacenet.domain.network.node.Node node : network.getNodes()) {
			n.nodes.add(Node.createFrom(node, context));
		}
		for(edu.mit.spacenet.domain.network.edge.Edge edge : network.getEdges()) {
			n.edges.add(Edge.createFrom(edge, context));
		}
		return n;
	}
}
