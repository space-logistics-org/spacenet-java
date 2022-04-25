package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Mission {
	public UUID id;
	public String name;
	public Date start_date;
	public List<Event> events;
	public List<DemandModel> demand_models = new ArrayList<DemandModel>();
	public UUID origin;
	public UUID destination;
	public UUID return_origin;
	public UUID return_destination;

	public static Mission createFrom(edu.mit.spacenet.scenario.Mission mission, Context context) {
		Mission m = new Mission();
		m.id = context.getUUID(mission);
		m.name = mission.getName();
		m.start_date = mission.getStartDate();
		m.origin = context.getUUID(mission.getOrigin());
		m.destination = context.getUUID(mission.getDestination());
		m.return_origin = context.getUUID(mission.getReturnOrigin());
		m.return_destination = context.getUUID(mission.getReturnDestination());
		m.events = Event.createFrom(mission.getEventList(), context);
		m.demand_models = DemandModel.createFrom(mission.getDemandModels(), context);
		return m;
	}
	
	public static List<Mission> createFrom(Collection<edu.mit.spacenet.scenario.Mission> missions, Context context) {
		List<Mission> ms = new ArrayList<Mission>();
		for(edu.mit.spacenet.scenario.Mission mission : missions) {
			ms.add(Mission.createFrom(mission, context));
		}
		return ms;
	}
	
	public edu.mit.spacenet.scenario.Mission toSpaceNet(edu.mit.spacenet.scenario.Scenario scenario, Context context) {
		edu.mit.spacenet.scenario.Mission m = new edu.mit.spacenet.scenario.Mission(scenario);
		context.getId(id, m); // register id for consumables model
		m.setName(name);
		m.setStartDate(start_date);
		m.setOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObjectViaId(origin));
		m.setDestination((edu.mit.spacenet.domain.network.node.Node) context.getObjectViaId(destination));
		m.setReturnOrigin((edu.mit.spacenet.domain.network.node.Node) context.getObjectViaId(return_origin));
		m.setReturnDestination((edu.mit.spacenet.domain.network.node.Node) context.getObjectViaId(return_destination));
		m.getEventList().addAll(Event.toSpaceNet(events, context));
		m.getDemandModels().addAll(DemandModel.toSpaceNet(demand_models, context));
		return m;
	}
	
	public static List<edu.mit.spacenet.scenario.Mission> toSpaceNet(Collection<Mission> missions, edu.mit.spacenet.scenario.Scenario scenario, Context context) {
		List<edu.mit.spacenet.scenario.Mission> ms = new ArrayList<edu.mit.spacenet.scenario.Mission>();
		for(Mission m : missions) {
			ms.add(m.toSpaceNet(scenario, context));
		}
		return ms;
	}
}
