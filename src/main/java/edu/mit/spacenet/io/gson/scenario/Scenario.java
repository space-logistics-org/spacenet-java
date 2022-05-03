package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.model.I_DemandModel;
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
	public Network network;
	public List<Mission> missionList;
	public List<ResourceType> resourceList;
	public List<Element> elementTemplates;
	public List<Element> instantiatedElements;
	public List<DemandModel> demandModels;
	
	public static Scenario createFrom(edu.mit.spacenet.scenario.Scenario scenario) {
		Scenario s = new Scenario();
		s.createdBy = scenario.getCreatedBy();
		s.name = scenario.getName();
		s.description = scenario.getDescription();
		s.startDate = scenario.getStartDate();
		s.scenarioType = TYPE_MAP.inverse().get(scenario.getScenarioType());
		Context context = new Context();
		s.network = Network.createFrom(scenario.getNetwork(), context);
		s.demandModels = new ArrayList<DemandModel>();
		for(edu.mit.spacenet.scenario.Mission mission : scenario.getMissionList()) {
			for(I_DemandModel model : mission.getDemandModels()) {
				if(context.getModelTemplate(model.getTid()) == null) {
					DemandModel m = DemandModel.createFrom(model, context);
					m.id = UUID.randomUUID();
					m.templateId = null;
					context.putModelTemplate(model, m.id, m);
					s.demandModels.add(m);
				}
			}
		}
		for(I_Element element : scenario.getElements()) {
			for(I_State state : element.getStates()) {
				for(I_DemandModel model : state.getDemandModels()) {
					if(context.getModelTemplate(model.getTid()) == null) {
						DemandModel m = DemandModel.createFrom(model, context);
						m.id = UUID.randomUUID();
						m.templateId = null;
						context.putModelTemplate(model, m.id, m);
						s.demandModels.add(m);
					}
				}
			}
		}
		s.resourceList = ResourceType.createFrom(scenario.getDataSource().getResourceLibrary(), context);
		s.elementTemplates = new ArrayList<Element>();
		for(I_Element element : scenario.getElements()) {
			if(context.getElementTemplate(element.getTid()) == null) {
				Element e = Element.createFrom(element, context);
				context.putElementTemplate(element, e.id, e);
				for(ElementPreview p : scenario.getDataSource().getElementPreviewLibrary()) {
					if(context.getElementTemplate(p.ID) == e.id) {
						e.name = p.NAME;
					}
				}
				s.elementTemplates.add(e);
			}
		}
		s.instantiatedElements = Element.createFrom(scenario.getElements(), context);
		s.missionList = Mission.createFrom(scenario.getMissionList(), context);
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
		// load data source
		DataSource dataSource = new DataSource();
		dataSource.nodeLibrary = new ArrayList<Node>(network.nodes);
		dataSource.edgeLibrary = new ArrayList<Edge>(network.edges);
		dataSource.resourceTypeLibrary = new ArrayList<ResourceType>(resourceList);
		dataSource.elementTemplateLibrary = new ArrayList<Element>(elementTemplates);
		dataSource.demandModelLibrary = new ArrayList<DemandModel>(demandModels);
		s.setDataSource(dataSource.toSpaceNet(context));
		// load network
		network.toSpaceNet(s, context);
		// load elements
		Element.toSpaceNet(instantiatedElements, context);
		// load missions
		s.getMissionList().addAll(Mission.toSpaceNet(missionList, s, context));
		return s;
	}
}
