package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class Exploration extends Event {
	protected UUID vehicle;
	protected PeriodDuration duration;
	protected Double evaPerWeek;
	protected PeriodDuration evaDuration;
	protected List<UUID> elements;
	protected List<UUID> states;
	protected List<Resource> demands;

	public static Exploration createFrom(edu.mit.spacenet.simulator.event.ExplorationProcess event, Context context) {
		Exploration e = new Exploration();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime())*24*60*60))
			);
		e.priority = event.getPriority();
		e.location = context.getJsonIdFromJavaObject(event.getLocation());
		e.duration = PeriodDuration.of(
				Period.ofDays((int) event.getDuration()), 
				Duration.ofSeconds((long) ((event.getDuration() - (int) event.getDuration())*24*60*60))
			);
		e.evaPerWeek = event.getEvaPerWeek();
		e.vehicle = context.getJsonIdFromJavaObject(event.getVehicle());
		e.evaDuration = PeriodDuration.of(
				Period.ofDays((int) event.getEvaDuration()), 
				Duration.ofSeconds((long) ((event.getEvaDuration() - (int) event.getEvaDuration())*24*60*60))
			);
		e.elements = context.getJsonIdsFromJavaObjects(event.getStateMap().keySet());
		e.states = context.getJsonIdsFromJavaObjects(event.getStateMap().values());
		e.demands = Resource.createFrom(event.getDemands(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.ExplorationProcess toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.ExplorationProcess e = new edu.mit.spacenet.simulator.event.ExplorationProcess();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setLocation((edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
		e.setVehicle((I_Carrier) context.getJavaObjectFromJsonId(vehicle));
		SortedMap<I_Element, I_State> stateMap = new TreeMap<I_Element, I_State>();
		for(int i = 0; i < elements.size(); i++) {
			stateMap.put(
				(I_Element) context.getJavaObjectFromJsonId(elements.get(i)), 
				(I_State) context.getJavaObjectFromJsonId(states.get(i))
			);
		}
		e.setStateMap(stateMap);
		e.setDuration(duration.getPeriod().getDays() + duration.getDuration().getSeconds() / (24*60*60d));
		e.setEvaPerWeek(evaPerWeek);
		e.setEvaDuration(evaDuration.getPeriod().getDays() + evaDuration.getDuration().getSeconds() / (24*60*60d));
		return e;
	}

}
