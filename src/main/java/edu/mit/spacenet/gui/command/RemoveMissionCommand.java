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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.mit.spacenet.gui.mission.MissionsTab;
import edu.mit.spacenet.scenario.Mission;

/**
 * The command to remove one or more missions.
 * 
 * @author Paul Grogan
 */
public class RemoveMissionCommand implements I_Command {
	private MissionsTab missionsTab;
	private List<Mission> missions;
	
	/**
	 * The constructor.
	 * 
	 * @param missionsTab the missions tab component
	 * @param missions the missions to remove
	 */
	public RemoveMissionCommand(MissionsTab missionsTab, List<Mission> missions) {
		this.missionsTab = missionsTab;
		this.missions = missions;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		JPanel warningPanel = new JPanel();
		warningPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		warningPanel.add(new JLabel("Permanently delete the following missions?"), c);
		c.gridy++;
		c.weighty = 1;
		DefaultListModel missionsList = new DefaultListModel();
		for(Mission m : missions) {
			missionsList.addElement(m);
		}
		warningPanel.add(new JScrollPane(new JList(missionsList)), c);
		int answer = JOptionPane.showOptionDialog(missionsTab, 
				warningPanel, 
				"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	    if (answer == JOptionPane.YES_OPTION) {
	    	for(Mission mission : missions) {
	    		missionsTab.getScenarioPanel().getScenario().getMissionList().remove(mission);
			}
	    }
	    missionsTab.updateView();
	}

}
