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
package edu.mit.spacenet.gui.mission;

import javax.swing.JSplitPane;

import edu.mit.spacenet.scenario.Mission;

/**
 * A split panel used to display mission details and a bat chart visualization.
 */
public class MissionSplitPane extends JSplitPane {
	private static final long serialVersionUID = -1544896538049800449L;

	private MissionsTab missionsTab;
	private Mission mission;
	
	private MissionPanel missionPanel;
	private MissionBatChart missionBatChart;
	
	/**
	 * Instantiates a new mission split pane.
	 * 
	 * @param missionsTab the missions tab
	 */
	public MissionSplitPane(MissionsTab missionsTab) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.missionsTab = missionsTab;
		buildPanel();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		missionPanel = new MissionPanel(this);
		setLeftComponent(missionPanel);
		missionBatChart = new MissionBatChart(this);
		setRightComponent(missionBatChart);
	}
	
	/**
	 * Initializes the panel for a new mission.
	 */
	public void initialize() {
		missionPanel.initialize();
		missionBatChart.initialize();
	}
	
	/**
	 * Updates the view.
	 */
	public void updateView() {
		missionPanel.updateView();
		missionBatChart.updateView();
	}
	
	/**
	 * Gets the missions tab.
	 * 
	 * @return the missions tab
	 */
	public MissionsTab getMissionsTab() {
		return missionsTab;
	}
	
	/**
	 * Gets the mission.
	 * 
	 * @return the mission
	 */
	public Mission getMission() {
		return mission;
	}
	
	/**
	 * Sets the mission and initializes the components.
	 * 
	 * @param mission the new mission
	 */
	public void setMission(Mission mission) {
		this.mission = mission;
		if(mission!=null) initialize();
	}
}
