package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class SpaceEdge extends Edge {
	public PeriodDuration duration;
	public List<Burn> burns;

	public static SpaceEdge createFrom(edu.mit.spacenet.domain.network.edge.SpaceEdge edge, Context context) {
		SpaceEdge e = new SpaceEdge();
		e.id = UUID.randomUUID();
		context.put(edge, e.id, e);
		e.name = edge.getName();
		e.description = edge.getDescription();
		e.origin_id = context.getJsonIdFromJavaObject(edge.getOrigin());
		e.destination_id = context.getJsonIdFromJavaObject(edge.getDestination());
		e.duration = PeriodDuration.of(
				Period.ofDays((int) edge.getDuration()), 
				Duration.ofSeconds((long) (edge.getDuration() - (int) edge.getDuration())*24*60*60)
			);
		e.burns = Burn.createFrom(edge.getBurns(), context);
		e.contents = context.getJsonIdsFromJavaObjects(edge.getContents());
		return e;
	}
	
	public edu.mit.spacenet.domain.network.edge.SpaceEdge toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.SpaceEdge e = new edu.mit.spacenet.domain.network.edge.SpaceEdge();
		context.put(e, id, this);
		e.setTid(context.getJavaId(id));
		e.setName(name);
		e.setDescription(description);
		e.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(origin_id));
		e.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(destination_id));
		e.setDuration(duration.getPeriod().getDays() + duration.getDuration().getSeconds() / (24*60*60d));
		e.setBurns(Burn.toSpaceNet(burns, context));
		e.getContents().addAll(Element.toSpaceNetViaId(contents, context));
		return e;
	}
}
