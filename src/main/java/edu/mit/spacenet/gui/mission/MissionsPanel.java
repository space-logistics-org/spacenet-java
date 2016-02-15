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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.gui.command.AddMissionCommand;
import edu.mit.spacenet.gui.command.CopyMissionCommand;
import edu.mit.spacenet.gui.command.RemoveMissionCommand;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;

/**
 * A panel used to display and edit the campaign missions.
 */
public class MissionsPanel extends JPanel {
	private static final long serialVersionUID = -227481087996502271L;

	private MissionsSplitPane missionsSplitPane;
	private MissionsTable missionsTable;
	
	private JButton addMissionButton, editMissionButton, copyMissionButton,
		removeMissionButton;
	
	/**
	 * Instantiates a new missions panel.
	 * 
	 * @param missionsSplitPane the missions split pane
	 */
	public MissionsPanel(MissionsSplitPane missionsSplitPane) {
		this.missionsSplitPane = missionsSplitPane;
		buildPanel();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setLayout(new BorderLayout());
		missionsTable = new MissionsTable(this);
		missionsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2 && missionsTable.getSelectedRow() >=0) {
					if(missionsTable.isEditing()) missionsTable.getCellEditor().stopCellEditing();
					missionsSplitPane.getMissionsTab().editMission(missionsTable.getSelectedMission());
				}
			}
		});
		missionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateButtons();
			}
		});
		JScrollPane missionScroll = new JScrollPane(missionsTable);
		missionScroll.setPreferredSize(new Dimension(150,250));
		add(missionScroll, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		addMissionButton = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/application_add.png")));
		addMissionButton.setMargin(new Insets(3,3,3,3));
		addMissionButton.setToolTipText("Add New Mission");
		addMissionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(missionsTable.isEditing()) missionsTable.getCellEditor().stopCellEditing();
				AddMissionCommand command = new AddMissionCommand(getMissionsSplitPane().getMissionsTab(), new Mission(getScenario()));
				command.execute();
			}
		});
		buttonPanel.add(addMissionButton);
		editMissionButton = new JButton("Edit", new ImageIcon(getClass().getClassLoader().getResource("icons/application_edit.png")));
		editMissionButton.setMargin(new Insets(3,3,3,3));
		editMissionButton.setToolTipText("Edit Mission");
		editMissionButton.setEnabled(false);
		editMissionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(missionsTable.isEditing()) missionsTable.getCellEditor().stopCellEditing();
				missionsSplitPane.getMissionsTab().editMission(missionsTable.getSelectedMission());
			}
		});
		buttonPanel.add(editMissionButton);
		copyMissionButton = new JButton("Copy", new ImageIcon(getClass().getClassLoader().getResource("icons/application_double.png")));
		copyMissionButton.setMargin(new Insets(3,3,3,3));
		copyMissionButton.setToolTipText("Copy Mission");
		copyMissionButton.setEnabled(false);
		copyMissionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(missionsTable.isEditing()) missionsTable.getCellEditor().stopCellEditing();
				CopyMissionCommand command = new CopyMissionCommand(missionsSplitPane.getMissionsTab(), missionsTable.getSelectedMission());
				command.execute();
			}
		});
		buttonPanel.add(copyMissionButton);
		removeMissionButton = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/application_delete.png")));
		removeMissionButton.setMargin(new Insets(3,3,3,3));
		removeMissionButton.setToolTipText("Remove Mission");
		removeMissionButton.setEnabled(false);
		removeMissionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(missionsTable.isEditing()) missionsTable.getCellEditor().stopCellEditing();
				ArrayList<Mission> missions = new ArrayList<Mission>();
				for(int row : missionsTable.getSelectedRows()) {
					missions.add(missionsTable.getMission(row));
				}
				RemoveMissionCommand command = new RemoveMissionCommand(getMissionsSplitPane().getMissionsTab(), missions);
				command.execute();
			}
		});
		buttonPanel.add(removeMissionButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return missionsSplitPane.getMissionsTab().getScenarioPanel().getScenario();
	}
	
	/**
	 * Updates the copy/edit/remove buttons.
	 */
	private void updateButtons() {
		if(missionsTable.getSelectedRowCount() == 1) {
			copyMissionButton.setEnabled(true);
			editMissionButton.setEnabled(true);
			removeMissionButton.setEnabled(true);
		} else if(missionsTable.getSelectedRowCount() > 1) {
			copyMissionButton.setEnabled(false);
			editMissionButton.setEnabled(false);
			removeMissionButton.setEnabled(true);
		} else {
			copyMissionButton.setEnabled(false);
			editMissionButton.setEnabled(false);
			removeMissionButton.setEnabled(false);
		}
	}
	
	/**
	 * Initializes the components for a new campaign.
	 */
	public void initialize() {
		missionsTable.initialize();
		updateButtons();
	}
	
	/**
	 * Updates the view.
	 */
	public void updateView() {
		missionsTable.updateView();
		updateButtons();
	}
	
	/**
	 * Gets the missions split pane.
	 * 
	 * @return the missions split pane
	 */
	public MissionsSplitPane getMissionsSplitPane() {
		return missionsSplitPane;
	}
}
