package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.simulator.event.I_Event;

public class Mission {
	public String name;
	public Date start_date;
	public List<Object> events = new ArrayList<Object>();
	public List<Object> demand_models = new ArrayList<Object>();
	public UUID origin;
	public UUID destination;
	public UUID return_origin;
	public UUID return_destination;

	public static Mission createFrom(edu.mit.spacenet.scenario.Mission mission, Context context) {
		Mission m = new Mission();
		m.name = mission.getName();
		m.start_date = mission.getStartDate();
		m.origin = context.getUUID(mission.getOrigin());
		m.destination = context.getUUID(mission.getDestination());
		m.return_origin = context.getUUID(mission.getReturnOrigin());
		m.return_destination = context.getUUID(mission.getReturnDestination());
		for(I_Event event : mission.getEventList()) {
			m.events.add(Event.createFrom(event, context));
		}
		return m;
	}
	
	public edu.mit.spacenet.scenario.Mission toSpaceNet(edu.mit.spacenet.scenario.Scenario scenario, Context context) {
		edu.mit.spacenet.scenario.Mission m = new edu.mit.spacenet.scenario.Mission(scenario);
		m.setName(name);
		m.setStartDate(start_date);
		m.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObject(origin));
		m.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getObject(destination));
		m.setReturnOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObject(return_origin));
		m.setReturnDestination((edu.mit.spacenet.domain.network.node.Node) context.getObject(return_destination));
		return m;
	}
}
