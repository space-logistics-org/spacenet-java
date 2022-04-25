package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.simulator.event.EventType;

public class SpaceTransport extends Event {
	public UUID edge;
	public List<UUID> elements;
	public List<List<BurnStageAction>> burnStageSequence;

	public static SpaceTransport createFrom(edu.mit.spacenet.simulator.event.SpaceTransport event, Context context) {
		SpaceTransport e = new SpaceTransport();
		e.type = TYPE_MAP.inverse().get(EventType.SPACE_TRANSPORT);
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(Duration.ofSeconds((long) event.getTime()*24*60*60));
		e.priority = event.getPriority();
		e.edge = context.getUUID(event.getEdge());
		e.elements = context.getUUIDs(event.getElements());
		e.burnStageSequence = BurnStageAction.createFrom(event.getBurnStageSequence(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.SpaceTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.SpaceTransport e = new edu.mit.spacenet.simulator.event.SpaceTransport();
		e.setName(name);
		e.setTime(mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setEdge((edu.mit.spacenet.domain.network.edge.SpaceEdge) context.getObject(edge));
		e.setElements(Element.toSpaceNet(elements, context));
		e.getBurnStageSequence().addAll(BurnStageAction.toSpaceNet(burnStageSequence, context));
		return e;
	}

}
