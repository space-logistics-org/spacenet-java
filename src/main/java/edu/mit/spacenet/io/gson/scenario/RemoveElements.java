package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class RemoveElements extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.REMOVE);

	public static RemoveElements createFrom(edu.mit.spacenet.simulator.event.RemoveEvent event, Context context) {
		RemoveElements e = new RemoveElements();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.RemoveEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.RemoveEvent e = new edu.mit.spacenet.simulator.event.RemoveEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
