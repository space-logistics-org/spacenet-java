package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class SpaceTransport extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.SPACE_TRANSPORT);

	public static SpaceTransport createFrom(edu.mit.spacenet.simulator.event.SpaceTransport event, Context context) {
		SpaceTransport e = new SpaceTransport();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.SpaceTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.SpaceTransport e = new edu.mit.spacenet.simulator.event.SpaceTransport();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
