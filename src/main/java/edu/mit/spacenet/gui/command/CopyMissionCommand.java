/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mit.spacenet.gui.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.mission.MissionsTab;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.AddEvent;
import edu.mit.spacenet.simulator.event.BurnEvent;
import edu.mit.spacenet.simulator.event.BurnStageItem;
import edu.mit.spacenet.simulator.event.CreateEvent;
import edu.mit.spacenet.simulator.event.DemandEvent;
import edu.mit.spacenet.simulator.event.EvaEvent;
import edu.mit.spacenet.simulator.event.ExplorationProcess;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.MoveEvent;
import edu.mit.spacenet.simulator.event.ReconfigureEvent;
import edu.mit.spacenet.simulator.event.ReconfigureGroupEvent;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.simulator.event.TransferEvent;
import edu.mit.spacenet.util.SerializeUtil;

/**
 * The command to copy a mission, performing all housekeeping operations to
 * reset unique identifiers and redirect pointers after serialization.
 * 
 * @author Paul Grogan
 */
public class CopyMissionCommand implements I_Command {
	private MissionsTab missionsTab;
	private Mission mission;
	
	/**
	 * The constructor.
	 * 
	 * @param missionsTab the missionsTab component
	 * @param mission the mission to copy
	 */
	public CopyMissionCommand(MissionsTab missionsTab, Mission mission) {
		this.missionsTab = missionsTab;
		this.mission = mission;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		Mission mission = (Mission)SerializeUtil.deepClone(this.mission);
		missionsTab.getScenarioPanel().getScenario().getMissionList().add(mission);
		mission.setName(mission.getName() + " (Copy)");
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(mission.getStartDate());
		g.add(Calendar.MONTH, 1);
		mission.setStartDate(g.getTime());
		// note: must reset all links to data objects that were destroyed in serialization
		mission.setScenario(missionsTab.getScenarioPanel().getScenario());
		if(mission.getOrigin()!=null) {
			mission.setOrigin(getNode(mission.getOrigin().getTid()));
		}
		if(mission.getDestination()!=null) {
			mission.setDestination(getNode(mission.getDestination().getTid()));
		}
		if(mission.getReturnOrigin()!=null) {
			mission.setReturnOrigin(getNode(mission.getReturnOrigin().getTid()));
		}
		if(mission.getReturnDestination()!=null) {
			mission.setReturnDestination(getNode(mission.getReturnDestination().getTid()));
		}
		for(I_Event event : mission.getEventList()) {
			event.resetUid();
			if(event.getLocation() instanceof Edge) {
				event.setLocation(getEdge(event.getLocation().getTid()));
			} else if(event.getLocation() instanceof Node) {
				event.setLocation(getNode(event.getLocation().getTid()));
			}
			if(event instanceof CreateEvent) {
				for(I_Element o : ((CreateEvent)event).getElements()) {
					o.resetUid();
				}
				if(((CreateEvent)event).getContainer() instanceof Node) {
					((CreateEvent)event).setContainer(event.getLocation());
				} else if(((CreateEvent) event).getContainer() instanceof I_Carrier) {
					((CreateEvent)event).setContainer((I_Carrier)getElement(((I_Carrier)((CreateEvent)event).getContainer()).getUid()));
				}
			} else if(event instanceof MoveEvent) {
				if(((MoveEvent)event).getContainer() instanceof Node) {
					((MoveEvent)event).setContainer(event.getLocation());
				} else if(((MoveEvent) event).getContainer() instanceof I_Carrier) {
					((MoveEvent)event).setContainer((I_Carrier)getElement(((I_Carrier)((MoveEvent)event).getContainer()).getUid()));
				}
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((MoveEvent)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((MoveEvent)event).getElements().clear();
				((MoveEvent)event).getElements().addAll(elements);
			} else if(event instanceof TransferEvent) {
				((TransferEvent)event).setOriginContainer(
						(I_ResourceContainer)getElement(
								((I_ResourceContainer)((TransferEvent)event).getOriginContainer()).getUid()));
				((TransferEvent)event).setDestinationContainer(
						(I_ResourceContainer)getElement(
								((I_ResourceContainer)((TransferEvent)event).getDestinationContainer()).getUid()));
			} else if(event instanceof AddEvent) {
				((AddEvent)event).setContainer(
						(I_ResourceContainer)getElement(
								((I_ResourceContainer)((AddEvent)event).getContainer()).getUid()));
			} else if(event instanceof DemandEvent) {
				((DemandEvent)event).setElement(
						(I_ResourceContainer)getElement(
								((I_ResourceContainer)((DemandEvent)event).getElement()).getUid()));
			} else if(event instanceof ReconfigureEvent) {
				((ReconfigureEvent)event).setElement(
						(I_ResourceContainer)getElement(
								((I_ResourceContainer)((ReconfigureEvent)event).getElement()).getUid()));
				for(I_State state : ((ReconfigureEvent)event).getElement().getStates()) {
					if(state.equals(((ReconfigureEvent)event).getState())) {
						((ReconfigureEvent)event).setState(state);
						break;
					}
				}
			} else if(event instanceof ReconfigureGroupEvent) {
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((ReconfigureGroupEvent)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((ReconfigureGroupEvent)event).getElements().clear();
				((ReconfigureGroupEvent)event).getElements().addAll(elements);
			} else if(event instanceof BurnEvent) {
				for(BurnStageItem item : ((BurnEvent)event).getBurnStageSequence()) {
					item.setElement(getElement(item.getElement().getUid()));
				}
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((BurnEvent)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((BurnEvent)event).getElements().clear();
				((BurnEvent)event).getElements().addAll(elements);
			} else if(event instanceof EvaEvent) {
				Map<CrewMember, I_State> map = new HashMap<CrewMember, I_State>();
				for(CrewMember crew : ((EvaEvent)event).getStateMap().keySet()) {
					I_State s = ((EvaEvent)event).getStateMap().get(crew);
					if(s==null) {
						map.put((CrewMember)getElement(crew.getUid()), null);
					} else {
						for(I_State state : getElement(crew.getUid()).getStates()) {
							if(state.equals(s)) {
								map.put((CrewMember)getElement(crew.getUid()), state);
								break;
							}
						}
					}
				}
				((EvaEvent)event).getStateMap().clear();
				for(CrewMember crew : map.keySet()) {
					((EvaEvent)event).getStateMap().put(crew, map.get(crew));
				}
				((EvaEvent)event).setVehicle((I_Carrier)getElement(((EvaEvent)event).getVehicle().getUid()));
			} else if(event instanceof ExplorationProcess) {
				Map<CrewMember, I_State> map = new HashMap<CrewMember, I_State>();
				for(CrewMember crew : ((ExplorationProcess)event).getStateMap().keySet()) {
					I_State s = ((ExplorationProcess)event).getStateMap().get(crew);
					if(s==null) {
						map.put((CrewMember)getElement(crew.getUid()), null);
					} else {
						for(I_State state : getElement(crew.getUid()).getStates()) {
							if(state.equals(s)) {
								map.put((CrewMember)getElement(crew.getUid()), state);
								break;
							}
						}
					}
				}
				((ExplorationProcess)event).getStateMap().clear();
				for(CrewMember crew : map.keySet()) {
					((ExplorationProcess)event).getStateMap().put(crew, map.get(crew));
				}
				((ExplorationProcess)event).setVehicle((I_Carrier)getElement(((ExplorationProcess)event).getVehicle().getUid()));
			} else if(event instanceof SpaceTransport) {
				List<List<BurnStageItem>> sequenceList = new ArrayList<List<BurnStageItem>>();
				for(List<BurnStageItem> list : ((SpaceTransport)event).getBurnStageSequence()) {
					List<BurnStageItem> sequence = new ArrayList<BurnStageItem>();
					for(BurnStageItem item : list) {
						item.setElement(getElement(item.getElement().getUid()));
						sequence.add(item);
					}
					sequenceList.add(sequence);
				}
				((SpaceTransport)event).setEdge((SpaceEdge)getEdge(((SpaceTransport)event).getEdge().getTid()));
				((SpaceTransport)event).getBurnStageSequence().clear();
				for(List<BurnStageItem> list : sequenceList) {
					((SpaceTransport)event).getBurnStageSequence().add(list);
				}
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((SpaceTransport)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((SpaceTransport)event).getElements().clear();
				((SpaceTransport)event).getElements().addAll(elements);
			} else if(event instanceof SurfaceTransport) {
				((SurfaceTransport)event).setEdge((SurfaceEdge)getEdge(((SurfaceTransport)event).getEdge().getTid()));
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((SurfaceTransport)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((SurfaceTransport)event).getElements().clear();
				((SurfaceTransport)event).getElements().addAll(elements);
			} else if(event instanceof FlightTransport) {
				((FlightTransport)event).setEdge((FlightEdge)getEdge(((FlightTransport)event).getEdge().getTid()));
				List<I_Element> elements = new ArrayList<I_Element>();
				for(I_Element element : ((FlightTransport)event).getElements()) {
					elements.add(getElement(element.getUid()));
				}
				((FlightTransport)event).getElements().clear();
				((FlightTransport)event).getElements().addAll(elements);
			}
		}
		missionsTab.editMission(mission);
	}
	private Node getNode(int tid) {
		return getScenario().getNetwork().getNodeByTid(tid);
	}
	private Edge getEdge(int tid) {
		return getScenario().getNetwork().getEdgeByTid(tid);
	}
	private I_Element getElement(int uid) {
		return getScenario().getElementByUid(uid);
	}
	private Scenario getScenario() {
		return missionsTab.getScenarioPanel().getScenario();
	}
}
