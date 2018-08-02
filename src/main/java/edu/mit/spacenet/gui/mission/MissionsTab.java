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

import java.awt.CardLayout;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.simulator.MiniSimulator;

/**
 * The tab used to define the campaign missions.
 */
public class MissionsTab extends JPanel {
	private static final long serialVersionUID = -1914358227873959747L;
	private static String MISSIONS = "Missions Panel";
	private static String MISSION = "Mission Panel";

	private ScenarioPanel scenarioPanel;
	
	private MissionsSplitPane missionsSplitPane;
	private MissionSplitPane missionSplitPane;
	private MiniSimulator simulator;
	private SimWorker simWorker;
	
	/**
	 * Instantiates a new missions tab.
	 * 
	 * @param scenarioPanel the scenario panel
	 */
	public MissionsTab(ScenarioPanel scenarioPanel) {
		this.scenarioPanel = scenarioPanel;
		setLayout(new CardLayout());
		missionsSplitPane = new MissionsSplitPane(this);
		add(missionsSplitPane, MISSIONS);
		missionSplitPane = new MissionSplitPane(this);
		add(missionSplitPane, MISSION);
	}
	
	/**
	 * Initializes the missions tab.
	 */
	public void initialize() {
		simulator = new MiniSimulator(scenarioPanel.getScenario());
		missionsSplitPane.initialize();
		editMissions();
	}
		
	/**
	 * Updates the view. Uses a swing worker to run the spatial simulation in a 
	 * separate thread so the GUI doesn't freeze up.
	 */
	public void updateView() {
		while(simWorker != null && !simWorker.isDone()) {
			// lock UI while previous simulation is running
		}
		simWorker = new SimWorker();
		simWorker.execute();
	}
	
	/**
	 * Gets the mission split pane.
	 * 
	 * @return the mission split pane
	 */
	public MissionSplitPane getMissionSplitPane() {
		return missionSplitPane;
	}
	
	/**
	 * Gets the simulator.
	 * 
	 * @return the simulator
	 */
	public MiniSimulator getSimulator() {
		return simulator;
	}
	
	/**
	 * Gives control to the mission split pane to edit the mission.
	 * 
	 * @param mission the mission
	 */
	public void editMission(Mission mission) {
		missionSplitPane.setMission(mission);
		((CardLayout)getLayout()).show(this, MISSION);
	}
	
	/**
	 * Gives control to the missions split pane to edit the missions.
	 */
	public void editMissions() {
		if(missionSplitPane.getMission()!=null) missionSplitPane.setMission(null);
		((CardLayout)getLayout()).show(this, MISSIONS);
		updateView();
	}
	
	/**
	 * Gets the scenario panel.
	 * 
	 * @return the scenario panel
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}	
	
	/**
	 * A SwingWorker subclass that manages the time-intensive simulation in a
	 * separate thread.
	 */
	private class SimWorker extends SwingWorker<Void, Void> {
		private boolean isInitialization;
		
		/**
		 * Instantiates a new sim worker.
		 * 
		 * @param isInitialization the is initialization
		 */
		public SimWorker(boolean isInitialization) {
			this.isInitialization = isInitialization;
		}
		
		/**
		 * Instantiates a new sim worker.
		 */
		public SimWorker() {
			this(false);
		}
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
		 */
		public Void doInBackground() {
			try {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Simulating Missions...");
				scenarioPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				simulator.simulate();
				scenarioPanel.setCursor(Cursor.getDefaultCursor());
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		public void done() {
			if(missionSplitPane.getMission()==null) {
				if(isInitialization) {
					missionsSplitPane.initialize();
				} else {
					missionsSplitPane.updateView();
				}
			} else {
				if(isInitialization) {
					missionSplitPane.initialize();
				} else {
					missionSplitPane.updateView();
				}
			}
		}
	}
	
}