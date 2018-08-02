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
