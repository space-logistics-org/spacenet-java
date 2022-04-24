package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.I_Resource;
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
	
	public String name;
	public String description;
	public Date startDate;
	public String scenarioType;
	public Network network;
	public List<Mission> missions = new ArrayList<Mission>();
	public List<ResourceType> resources = new ArrayList<ResourceType>();
	public List<Element> elements = new ArrayList<Element>();
	
	public static Scenario createFrom(edu.mit.spacenet.scenario.Scenario scenario) {
		Scenario s = new Scenario();
		s.name = scenario.getName();
		s.description = scenario.getDescription();
		s.startDate = scenario.getStartDate();
		s.scenarioType = TYPE_MAP.inverse().get(scenario.getScenarioType());
		Context context = new Context();
		s.network = Network.createFrom(scenario.getNetwork(), context);
		for(edu.mit.spacenet.scenario.Mission mission : scenario.getMissionList()) {
			s.missions.add(Mission.createFrom(mission, context));
		}
		for(I_Resource resource : scenario.getDataSource().getResourceLibrary()) {
			s.resources.add(ResourceType.createFrom(resource, context));
		}
		for(I_Element element : scenario.getElements()) {
			s.elements.add(Element.createFrom(element, context));
		}
		return s;
	}
	
	public edu.mit.spacenet.scenario.Scenario toSpaceNet() {
		edu.mit.spacenet.scenario.Scenario s = new edu.mit.spacenet.scenario.Scenario();
		s.setName(name);
		s.setDescription(description);
		s.setStartDate(startDate);
		s.setScenarioType(TYPE_MAP.get(scenarioType));
		Context context = new Context();
		for(Node n : network.nodes) {
			s.getNetwork().add(n.toSpaceNet(context));
		}
		for(Edge e : network.edges) {
			s.getNetwork().add(e.toSpaceNet(context));
		}
		// load resources
		for(ResourceType r : resources) {
			context.getUUID(r);
		}
		// load elements
		for(Element e : elements) {
			context.getUUID(e);
		}
		for(Mission m : missions) {
			s.getMissionList().add(m.toSpaceNet(s, context));
		}
		return s;
	}
}
