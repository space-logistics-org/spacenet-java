package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_ResourceContainer;

public class TransferResources extends Event {

	protected List<Resource> resources;
	protected UUID origin;
	protected UUID destination;

	public static TransferResources createFrom(edu.mit.spacenet.simulator.event.TransferEvent event, Context context) {
		TransferResources e = new TransferResources();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime())*24*60*60))
			);
		e.priority = event.getPriority();
		e.location = context.getJsonIdFromJavaObject(event.getLocation());
		e.resources = Resource.createFrom(event.getTransferDemands(), context);
		e.origin = context.getJsonIdFromJavaObject(event.getOriginContainer());
		e.destination = context.getJsonIdFromJavaObject(event.getDestinationContainer());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.TransferEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.TransferEvent e = new edu.mit.spacenet.simulator.event.TransferEvent();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
		e.setTransferDemands(Resource.toSpaceNet(resources, context));
		e.setOriginContainer((I_ResourceContainer) context.getJavaObjectFromJsonId(origin));
		e.setDestinationContainer((I_ResourceContainer) context.getJavaObjectFromJsonId(destination));
		return e;
	}
}
