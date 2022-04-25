package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.simulator.event.EventType;

public class EvaEvent extends Event {
	public UUID vehicle;
	public PeriodDuration evaDuration;
	public List<UUID> elements;
	public List<UUID> states;
	public List<Resource> demands;

	public static EvaEvent createFrom(edu.mit.spacenet.simulator.event.EvaEvent event, Context context) {
		EvaEvent e = new EvaEvent();
		e.type = TYPE_MAP.inverse().get(EventType.EVA);
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(Duration.ofSeconds((long) event.getTime()*24*60*60));
		e.priority = event.getPriority();
		e.vehicle = context.getUUID(event.getVehicle());
		e.evaDuration = PeriodDuration.of(Duration.ofSeconds((long) event.getEvaDuration()*24*60*60));
		e.elements = context.getUUIDs(event.getStateMap().keySet());
		e.states = context.getUUIDs(event.getStateMap().values());
		e.demands = Resource.createFrom(event.getDemands(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.EvaEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.EvaEvent e = new edu.mit.spacenet.simulator.event.EvaEvent();
		e.setName(name);
		e.setTime(mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setVehicle((I_Carrier) context.getObject(vehicle));
		SortedMap<I_Element, I_State> stateMap = new TreeMap<I_Element, I_State>();
		for(int i = 0; i < elements.size(); i++) {
			stateMap.put(
				(I_Element) context.getObject(elements.get(i)), 
				(I_State) context.getObject(states.get(i))
			);
		}
		e.setStateMap(stateMap);
		e.setEvaDuration(evaDuration.getDuration().getSeconds() / (24*60*60d));
		return e;
	}

}
