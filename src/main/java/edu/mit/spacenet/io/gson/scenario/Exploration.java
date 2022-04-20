package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class Exploration extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.EXPLORATION);

	public static Exploration createFrom(edu.mit.spacenet.simulator.event.ExplorationProcess event, Context context) {
		Exploration e = new Exploration();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ExplorationProcess toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ExplorationProcess e = new edu.mit.spacenet.simulator.event.ExplorationProcess();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
