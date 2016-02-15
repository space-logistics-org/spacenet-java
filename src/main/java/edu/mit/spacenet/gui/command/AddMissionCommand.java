/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
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
