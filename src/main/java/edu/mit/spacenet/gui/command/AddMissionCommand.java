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

import edu.mit.spacenet.gui.mission.MissionsTab;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.util.DateFunctions;

/**
 * The command to add a mission to a scenario, pre-filling data as available and
 * triggering a secondary command to edit the newly-created mission.
 * 
 * @author Paul Grogan
 */
public class AddMissionCommand implements I_Command {
	private MissionsTab missionsTab;
	private Mission mission;
	
	/**
	 * The constructor.
	 * 
	 * @param missionsTab the missions tab component
	 * @param mission the mission to add
	 */
	public AddMissionCommand(MissionsTab missionsTab, Mission mission) {
		this.missionsTab = missionsTab;
		this.mission = mission;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		if(missionsTab.getScenarioPanel().getScenario().getMissionList().size() > 0) {
			Mission prevMission = missionsTab.getScenarioPanel().getScenario().getMissionList().get(missionsTab.getScenarioPanel().getScenario().getMissionList().size()-1);
			mission.setStartDate(DateFunctions.getDate(prevMission.getStartDate(), prevMission.getDuration()));
			mission.setOrigin(prevMission.getOrigin());
			mission.setDestination(prevMission.getDestination());
			mission.setReturnOrigin(prevMission.getReturnOrigin());
			mission.setReturnDestination(prevMission.getReturnDestination());
		} else {
			mission.setStartDate(missionsTab.getScenarioPanel().getScenario().getStartDate());
			if(missionsTab.getScenarioPanel().getScenario().getNetwork().getNodes().size() > 0) {
				mission.setOrigin(missionsTab.getScenarioPanel().getScenario().getNetwork().getNodes().first());
				mission.setDestination(missionsTab.getScenarioPanel().getScenario().getNetwork().getNodes().first());
			}
		}
		missionsTab.getScenarioPanel().getScenario().getMissionList().add(mission);
		missionsTab.editMission(mission);
	}

}
