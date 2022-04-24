package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class BurnEvent extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.BURN);

	public static BurnEvent createFrom(edu.mit.spacenet.simulator.event.BurnEvent event, Context context) {
		BurnEvent e = new BurnEvent();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.BurnEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.BurnEvent e = new edu.mit.spacenet.simulator.event.BurnEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}