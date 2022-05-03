package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_ResourceContainer;

public class AddResources extends Event {

	protected List<Resource> resources;
	protected UUID container;
	
	public static AddResources createFrom(edu.mit.spacenet.simulator.event.AddEvent event, Context context) {
		AddResources e = new AddResources();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime())*24*60*60))
			);
		e.priority = event.getPriority();
		e.location = context.getJsonIdFromJavaObject(event.getLocation());
		e.resources = Resource.createFrom(event.getDemands(), context);
		e.container = context.getJsonIdFromJavaObject(event.getContainer());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.AddEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.AddEvent e = new edu.mit.spacenet.simulator.event.AddEvent();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
		e.setDemands(Resource.toSpaceNet(resources, context));
		e.setContainer((I_ResourceContainer) context.getJavaObjectFromJsonId(container));
		return e;
	}

}
