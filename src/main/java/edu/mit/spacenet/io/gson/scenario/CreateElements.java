package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.simulator.event.EventType;

public class CreateElements extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.CREATE);
	
	public List<UUID> elements;
	public UUID container;

	public static CreateElements createFrom(edu.mit.spacenet.simulator.event.CreateEvent event, Context context) {
		CreateElements e = new CreateElements();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		e.location = context.getUUID(event.getLocation());
		e.elements = Element.createIdsFrom(event.getElements(), context);
		e.container = context.getUUID(event.getContainer());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.CreateEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.CreateEvent e = new edu.mit.spacenet.simulator.event.CreateEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getObject(location));
		e.setElements(Element.toSpaceNet(elements, context));
		e.setContainer((edu.mit.spacenet.domain.I_Container) context.getObject(container));
		return e;
	}

}
