package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class TransferResources extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.TRANSFER);

	public static TransferResources createFrom(edu.mit.spacenet.simulator.event.TransferEvent event, Context context) {
		TransferResources e = new TransferResources();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.TransferEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.TransferEvent e = new edu.mit.spacenet.simulator.event.TransferEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}
}
