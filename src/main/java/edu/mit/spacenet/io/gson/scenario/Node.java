package edu.mit.spacenet.io.gson.scenario;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.network.node.NodeType;

public abstract class Node extends Location {
	public static final BiMap<String, NodeType> TYPE_MAP = new ImmutableBiMap.Builder<String, NodeType>()
			.put("SurfaceNode", NodeType.SURFACE)
			.put("OrbitalNode", NodeType.ORBITAL)
			.put("LagrangeNode", NodeType.LAGRANGE)
			.build();

	public String type;
	public String body_1;
	
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
	
	@Override
	public abstract edu.mit.spacenet.domain.network.node.Node toSpaceNet(Context context);
}
