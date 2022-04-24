package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.simulator.event.EventType;

public class TransferResources extends Event {

	public List<Resource> resources;
	public UUID origin;
	public UUID destination;

	public static TransferResources createFrom(edu.mit.spacenet.simulator.event.TransferEvent event, Context context) {
		TransferResources e = new TransferResources();
		e.type = TYPE_MAP.inverse().get(EventType.TRANSFER);
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		e.location = context.getUUID(event.getLocation());
		e.resources = Resource.createFrom(event.getTransferDemands(), context);
		e.origin = context.getUUID(event.getOriginContainer());
		e.destination = context.getUUID(event.getDestinationContainer());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.TransferEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.TransferEvent e = new edu.mit.spacenet.simulator.event.TransferEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getObject(location));
		// TODO
		return e;
	}
}
