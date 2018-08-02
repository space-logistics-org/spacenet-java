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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import edu.mit.spacenet.domain.model.CrewConsumablesDemandModel;
import edu.mit.spacenet.domain.model.DemandModelFactory;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.gui.component.DropDownButton;
import edu.mit.spacenet.gui.model.DemandModelDialog;

/**
 * A custom drop down button containing the options to add the different kinds
 * of mission-level demand models to a mission.
 */
public class AddMissionDemandModelButton extends DropDownButton {
	private static final long serialVersionUID = -3212156860465231812L;
	
	private MissionPanel missionPanel;

	/**
	 * Instantiates a new adds the mission demand model button.
	 * 
	 * @param missionPanel the mission panel
	 */
	public AddMissionDemandModelButton(MissionPanel missionPanel) {
		super("Add Model", new ImageIcon(missionPanel.getClass().getClassLoader().getResource("icons/comment_add.png")));
		this.missionPanel = missionPanel;
		getButton().setMargin(new Insets(3,3,3,3));
		setToolTipText("Add Demand Model");
		addMenuItems();
	}
	
	/**
	 * Adds the menu items.
	 */
	private void addMenuItems() {
		JMenuItem addImpulseDemandModel = new JMenuItem(DemandModelType.TIMED_IMPULSE.getName(), DemandModelType.TIMED_IMPULSE.getIcon());
		addImpulseDemandModel.setToolTipText("Add a one-time demand for resources");
		addImpulseDemandModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DemandModelDialog.createAndShowGUI(missionPanel, DemandModelFactory.createDemandModel(missionPanel.getMissionSplitPane().getMission(), DemandModelType.TIMED_IMPULSE));
			}
		});
		getMenu().add(addImpulseDemandModel);
		JMenuItem addRatedDemandModel = new JMenuItem(DemandModelType.RATED.getName(), DemandModelType.RATED.getIcon());
		addRatedDemandModel.setToolTipText("Add a recurring demand for resources based on a demand rate");
		addRatedDemandModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DemandModelDialog.createAndShowGUI(missionPanel, DemandModelFactory.createDemandModel(missionPanel.getMissionSplitPane().getMission(), DemandModelType.RATED));
			}
		});
		getMenu().add(addRatedDemandModel);
		JMenuItem addCrewConsumablesDemandModel = new JMenuItem(DemandModelType.CREW_CONSUMABLES.getName(), DemandModelType.CREW_CONSUMABLES.getIcon());
		addCrewConsumablesDemandModel.setToolTipText("Use the crew consumables model");
		addCrewConsumablesDemandModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CrewConsumablesDemandModel model = new CrewConsumablesDemandModel(missionPanel.getMissionSplitPane().getMission());
				DemandModelDialog.createAndShowGUI(missionPanel, model);
			}
		});
		getMenu().add(addCrewConsumablesDemandModel);
	}
}