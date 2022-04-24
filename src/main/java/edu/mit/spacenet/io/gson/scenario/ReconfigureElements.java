package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import edu.mit.spacenet.simulator.event.EventType;

public class ReconfigureElements extends Event {
	public String type = TYPE_MAP.inverse().get(EventType.RECONFIGURE_GROUP);
	
	public List<UUID> elements = new ArrayList<UUID>();
	private String stateType;

	public static ReconfigureElements createFrom(edu.mit.spacenet.simulator.event.ReconfigureGroupEvent event, Context context) {
		ReconfigureElements e = new ReconfigureElements();
		e.name = event.getName();
		e.mission_time = Duration.ofSeconds((long) event.getTime()*24*60*60);
		e.priority = event.getPriority();
		for(edu.mit.spacenet.domain.element.I_Element element : event.getElements()) {
			e.elements.add(context.getUUID(element));
		}
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ReconfigureGroupEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ReconfigureGroupEvent e = new edu.mit.spacenet.simulator.event.ReconfigureGroupEvent();
		e.setName(name);
		e.setTime(mission_time.getSeconds() / (24*60*60));
		e.setPriority(priority);
		SortedSet<edu.mit.spacenet.domain.element.I_Element> es = new TreeSet<edu.mit.spacenet.domain.element.I_Element>();
		for(UUID uuid : elements) {
			es.add(((edu.mit.spacenet.domain.element.I_Element) context.getObject(uuid)));
		}
		e.setElements(es);
		return e;
	}

}
