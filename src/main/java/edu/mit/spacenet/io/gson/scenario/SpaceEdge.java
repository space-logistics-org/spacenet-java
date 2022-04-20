package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.network.edge.EdgeType;

public class SpaceEdge extends Edge {
	public String type = TYPE_MAP.inverse().get(EdgeType.SPACE);
	public double duration;
	public List<Burn> burns = new ArrayList<Burn>();

	public static SpaceEdge createFrom(edu.mit.spacenet.domain.network.edge.SpaceEdge edge, Context context) {
		SpaceEdge e = new SpaceEdge();
		e.id = context.getUUID(edge);
		e.name = edge.getName();
		e.description = edge.getDescription();
		e.origin_id = context.getUUID(edge.getOrigin());
		e.destination_id = context.getUUID(edge.getDestination());
		e.duration = edge.getDuration();
		for(edu.mit.spacenet.domain.network.edge.Burn burn : edge.getBurns()) {
			e.burns.add(Burn.createFrom(burn));
		}
		return e;
	}
	
	public edu.mit.spacenet.domain.network.edge.SpaceEdge toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.SpaceEdge e = new edu.mit.spacenet.domain.network.edge.SpaceEdge();
		e.setTid(context.getId(id));
		e.setName(name);
		e.setDescription(description);
		e.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObject(origin_id));
		e.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getObject(destination_id));
		e.setDuration(duration);
		List<edu.mit.spacenet.domain.network.edge.Burn> bs = new ArrayList<edu.mit.spacenet.domain.network.edge.Burn>();
		for(Burn b : burns) {
			bs.add(b.toSpaceNet());
		}
		e.setBurns(bs);
		return e;
	}
}
