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