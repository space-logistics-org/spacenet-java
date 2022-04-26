package edu.mit.spacenet.io.gson.scenario;

import java.util.Date;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.scenario.ScenarioType;

public class Scenario {
	public static final BiMap<String, ScenarioType> TYPE_MAP = new ImmutableBiMap.Builder<String, ScenarioType>()
			.put("ISS", ScenarioType.ISS)
			.put("Lunar", ScenarioType.LUNAR)
			.put("MoonOnly", ScenarioType.MOON_ONLY)
			.put("Martian", ScenarioType.MARTIAN)
			.put("MarsOnly", ScenarioType.MARS_ONLY)
			.put("SolarSystem", ScenarioType.SOLAR_SYSTEM)
			.build();
	
	public String createdBy;
	public String name;
	public String description;
	public Date startDate;
	public String scenarioType;
	public List<Location> nodes;
	public List<Location> edges;
	public List<Mission> missions;
	public List<ResourceType> resources;
	public List<Element> elements;
	
	public static Scenario createFrom(edu.mit.spacenet.scenario.Scenario scenario) {
		Scenario s = new Scenario();
		s.createdBy = scenario.getCreatedBy();
		s.name = scenario.getName();
		s.description = scenario.getDescription();
		s.startDate = scenario.getStartDate();
		s.scenarioType = TYPE_MAP.inverse().get(scenario.getScenarioType());
		Context context = new Context();
		s.nodes = Location.createFrom(scenario.getNetwork().getNodes(), context);
		s.edges = Location.createFrom(scenario.getNetwork().getEdges(), context);
		s.missions = Mission.createFrom(scenario.getMissionList(), context);
		s.resources = ResourceType.createFrom(scenario.getDataSource().getResourceLibrary(), context);
		s.elements = Element.createFrom(scenario.getElements(), context);
		return s;
	}
	
	public edu.mit.spacenet.scenario.Scenario toSpaceNet() {
		edu.mit.spacenet.scenario.Scenario s = new edu.mit.spacenet.scenario.Scenario();
		s.setCreatedBy(createdBy);
		s.setName(name);
		s.setDescription(description);
		s.setStartDate(startDate);
		s.setScenarioType(TYPE_MAP.get(scenarioType));
		Context context = new Context();
		// load nodes
		for(Location node : nodes) {
			s.getNetwork().add(node.toSpaceNet(context));
		}
		// load edges
		for(Location edge : edges) {
			s.getNetwork().add(edge.toSpaceNet(context));
		}
		// load resources
		for(ResourceType r : resources) {
			context.getId(r.id, r.toSpaceNet(context));
		}
		// load elements
		for(Element e : elements) {
			context.getId(e.id, e.toSpaceNet(context));
		}
		s.getMissionList().addAll(Mission.toSpaceNet(missions, s, context));
		return s;
	}
}
