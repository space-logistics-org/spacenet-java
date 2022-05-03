package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.network.node.NodeType;

public abstract class Node extends Location {
	public static final BiMap<String, NodeType> TYPE_MAP = new ImmutableBiMap.Builder<String, NodeType>()
			.put("SurfaceNode", NodeType.SURFACE)
			.put("OrbitalNode", NodeType.ORBITAL)
			.put("LagrangeNode", NodeType.LAGRANGE)
			.build();

	protected String body_1;
	
	public static Node createFrom(edu.mit.spacenet.domain.network.node.Node node, Context context) {
		if(node.getNodeType() == NodeType.SURFACE) {
			return SurfaceNode.createFrom((edu.mit.spacenet.domain.network.node.SurfaceNode) node, context);
		} else if(node.getNodeType() == NodeType.ORBITAL) {
			return OrbitalNode.createFrom((edu.mit.spacenet.domain.network.node.OrbitalNode) node, context);
		} else if(node.getNodeType() == NodeType.LAGRANGE) {
			return LagrangeNode.createFrom((edu.mit.spacenet.domain.network.node.LagrangeNode) node, context);
		} else {
			throw new UnsupportedOperationException("unknown node type: " + node.getNodeType());
		}
	}
	
	public static List<Node> createFrom(Collection<? extends edu.mit.spacenet.domain.network.node.Node> nodes, Context context) {
		List<Node> ns = new ArrayList<Node>();
		for(edu.mit.spacenet.domain.network.node.Node n : nodes) {
			ns.add(Node.createFrom(n, context));
		}
		return ns;
	}
	
	@Override
	public abstract edu.mit.spacenet.domain.network.node.Node toSpaceNet(Context context);
	
	public static List<edu.mit.spacenet.domain.network.node.Node> toSpaceNet(Collection<Node> nodes, Context context) {
		List<edu.mit.spacenet.domain.network.node.Node> ns = new ArrayList<edu.mit.spacenet.domain.network.node.Node>();
		if(nodes != null) {
			for(Node n : nodes) {
				ns.add(n.toSpaceNet(context));
			}
		}
		return ns;
	}
}
