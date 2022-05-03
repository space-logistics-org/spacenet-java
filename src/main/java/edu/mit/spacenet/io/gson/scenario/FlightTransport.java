package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class FlightTransport extends Event {
	public UUID edge;
	public List<UUID> elements;

	public static FlightTransport createFrom(edu.mit.spacenet.simulator.event.FlightTransport event, Context context) {
		FlightTransport e = new FlightTransport();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime())*24*60*60))
			);
		e.priority = event.getPriority();
		e.location = context.getJsonIdFromJavaObject(event.getLocation());
		e.edge = context.getJsonIdFromJavaObject(event.getEdge());
		e.elements = context.getJsonIdsFromJavaObjects(event.getElements());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.FlightTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.FlightTransport e = new edu.mit.spacenet.simulator.event.FlightTransport();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
		e.setEdge((edu.mit.spacenet.domain.network.edge.FlightEdge) context.getJavaObjectFromJsonId(edge));
		e.setElements(Element.toSpaceNetViaId(elements, context));
		return e;
	}

}
