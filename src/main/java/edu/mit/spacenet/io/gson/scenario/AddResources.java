package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class AddResources extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.ADD);

	public static AddResources createFrom(edu.mit.spacenet.simulator.event.AddEvent event, Context context) {
		AddResources e = new AddResources();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.AddEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.AddEvent e = new edu.mit.spacenet.simulator.event.AddEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
