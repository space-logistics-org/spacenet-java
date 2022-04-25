package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.simulator.event.EventType;

public class ReconfigureElements extends Event {
	
	public List<UUID> elements;
	public String stateType;

	public static ReconfigureElements createFrom(edu.mit.spacenet.simulator.event.ReconfigureGroupEvent event, Context context) {
		ReconfigureElements e = new ReconfigureElements();
		e.type = TYPE_MAP.inverse().get(EventType.RECONFIGURE_GROUP);
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) (event.getTime() - (int) event.getTime())*24*60*60)
			);
		e.priority = event.getPriority();
		e.elements = context.getUUIDs(event.getElements());
		e.stateType = event.getStateType().getName();
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ReconfigureGroupEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ReconfigureGroupEvent e = new edu.mit.spacenet.simulator.event.ReconfigureGroupEvent();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setElements(Element.toSpaceNet(elements, context));
		e.setStateType(StateType.getInstance(stateType));
		return e;
	}

}
