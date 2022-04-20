package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class SurfaceTransport extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.SURFACE_TRANSPORT);

	public static SurfaceTransport createFrom(edu.mit.spacenet.simulator.event.SurfaceTransport event, Context context) {
		SurfaceTransport e = new SurfaceTransport();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.SurfaceTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.SurfaceTransport e = new edu.mit.spacenet.simulator.event.SurfaceTransport();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
