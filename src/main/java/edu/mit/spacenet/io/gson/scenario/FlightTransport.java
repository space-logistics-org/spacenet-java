package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class FlightTransport extends Event {

	public static FlightTransport createFrom(edu.mit.spacenet.simulator.event.FlightTransport event, Context context) {
		FlightTransport e = new FlightTransport();
		e.type = TYPE_MAP.inverse().get(EventType.FLIGHT_TRANSPORT);
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.FlightTransport toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.FlightTransport e = new edu.mit.spacenet.simulator.event.FlightTransport();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
