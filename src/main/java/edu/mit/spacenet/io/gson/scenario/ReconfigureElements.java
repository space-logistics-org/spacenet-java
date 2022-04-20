package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;

import edu.mit.spacenet.simulator.event.EventType;

public class ReconfigureElements extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.RECONFIGURE_GROUP);

	public static ReconfigureElements createFrom(edu.mit.spacenet.simulator.event.ReconfigureGroupEvent event, Context context) {
		ReconfigureElements e = new ReconfigureElements();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ReconfigureGroupEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ReconfigureGroupEvent e = new edu.mit.spacenet.simulator.event.ReconfigureGroupEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		return e;
	}

}
