package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.simulator.event.EventType;

public class FlightTransport extends Event {
	public UUID edge;
	public List<UUID> elements;

	public static FlightTransport createFrom(edu.mit.spacenet.simulator.event.FlightTransport event, Context context) {
		FlightTransport e = new FlightTransport();
		e.type = TYPE_MAP.inverse().get(EventType.FLIGHT_TRANSPORT);
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(Duration.ofSeconds((long) event.getTime()*24*60*60));
		e.priority = event.getPriority();
		e.edge = context.getUUID(event.getEdge());
		e.elements = context.getUUIDs(event.getElements());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.FlightTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.FlightTransport e = new edu.mit.spacenet.simulator.event.FlightTransport();
		e.setName(name);
		e.setTime(mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setEdge((edu.mit.spacenet.domain.network.edge.FlightEdge) context.getObject(edge));
		e.setElements(Element.toSpaceNet(elements, context));
		return e;
	}

}
