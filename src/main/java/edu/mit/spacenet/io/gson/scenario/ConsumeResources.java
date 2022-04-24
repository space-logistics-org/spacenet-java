package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.simulator.event.EventType;

public class ConsumeResources extends Event {

	public List<Resource> resources;
	public UUID source;

	public static ConsumeResources createFrom(edu.mit.spacenet.simulator.event.DemandEvent event, Context context) {
		ConsumeResources e = new ConsumeResources();
		e.type = TYPE_MAP.inverse().get(EventType.DEMAND);
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		e.location = context.getUUID(event.getLocation());
		e.resources = Resource.createFrom(event.getDemands(), context);
		e.source = context.getUUID(event.getElement());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.DemandEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.DemandEvent e = new edu.mit.spacenet.simulator.event.DemandEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getObject(location));
		// TODO
		return e;
	}

}
