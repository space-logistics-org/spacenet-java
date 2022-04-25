package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class ReconfigureElement extends Event {
	
	public UUID element;
	public UUID state;

	public static ReconfigureElement createFrom(edu.mit.spacenet.simulator.event.ReconfigureEvent event, Context context) {
		ReconfigureElement e = new ReconfigureElement();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) (event.getTime() - (int) event.getTime())*24*60*60)
			);
		e.priority = event.getPriority();
		e.location = context.getUUID(event.getLocation());
		e.element = context.getUUID(event.getElement());
		e.state = context.getUUID(event.getState());
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ReconfigureEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ReconfigureEvent e = new edu.mit.spacenet.simulator.event.ReconfigureEvent();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getObject(location));
		e.setElement((I_Element) context.getObject(element));
		e.setState((I_State) context.getObject(state));
		return e;
	}

}
