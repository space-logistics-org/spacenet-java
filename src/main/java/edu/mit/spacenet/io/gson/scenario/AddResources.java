package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.simulator.event.EventType;

public class AddResources extends Event {

	public List<Resource> resources;
	public UUID container;
	
	public static AddResources createFrom(edu.mit.spacenet.simulator.event.AddEvent event, Context context) {
		AddResources e = new AddResources();
		e.type = TYPE_MAP.inverse().get(EventType.ADD);
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(Duration.ofSeconds((long) event.getTime()*24*60*60));
		e.priority = event.getPriority();
		e.location = context.getUUID(event.getLocation());
		e.resources = Resource.createFrom(event.getDemands(), context);
		e.container = context.getUUID(event.getContainer());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.AddEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.AddEvent e = new edu.mit.spacenet.simulator.event.AddEvent();
		e.setName(name);
		e.setTime(mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getObject(location));
		e.setDemands(Resource.toSpaceNet(resources, context));
		e.setContainer((I_ResourceContainer) context.getObject(container));
		return e;
	}

}
