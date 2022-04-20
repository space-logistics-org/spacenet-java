package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class MoveElements extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.MOVE);

	public static MoveElements createFrom(edu.mit.spacenet.simulator.event.MoveEvent event, Context context) {
		MoveElements e = new MoveElements();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.MoveEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.MoveEvent e = new edu.mit.spacenet.simulator.event.MoveEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
