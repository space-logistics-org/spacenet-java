package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class ConsumeResources extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.DEMAND);

	public static ConsumeResources createFrom(edu.mit.spacenet.simulator.event.DemandEvent event, Context context) {
		ConsumeResources e = new ConsumeResources();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.DemandEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.DemandEvent e = new edu.mit.spacenet.simulator.event.DemandEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
