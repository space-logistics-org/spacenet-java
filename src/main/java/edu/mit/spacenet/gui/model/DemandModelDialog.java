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
package edu.mit.spacenet.gui.model;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.element.StateDialog;
import edu.mit.spacenet.gui.mission.MissionPanel;

/**
 * The dialog for viewing and editing demand models.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class DemandModelDialog extends JDialog {
	private static final long serialVersionUID = 5836025162293791120L;
	
	private StateDialog stateDialog;
	private MissionPanel missionPanel;
	private I_DemandModel demandModel;
	
	private JTextField nameText;
	private AbstractDemandModelPanel demandModelPanel;
	private JButton okButton;
	
	/**
	 * Instantiates a new demand model dialog.
	 * 
	 * @param stateDialog the state dialog
	 * @param demandModel the demand model
	 */
	public DemandModelDialog(StateDialog stateDialog, I_DemandModel demandModel) {
		super(stateDialog, "Edit Demand Model", true);
		this.stateDialog = stateDialog;
		this.demandModel = demandModel;
		buildDialog();
		initialize();
		setMinimumSize(new Dimension(300,150));
	}
	
	/**
	 * Instantiates a new demand model dialog.
	 * 
	 * @param missionPanel the missions panel
	 * @param demandModel the demand model
	 */
	public DemandModelDialog(MissionPanel missionPanel, I_DemandModel demandModel) {
		super(SpaceNetFrame.getInstance(), "Edit Demand Model", true);
		this.missionPanel = missionPanel;
		this.demandModel = demandModel;
		buildDialog();
		initialize();
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Name: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		nameText = new JTextField(20);
		contentPanel.add(nameText, c);
		
		c.gridy++;
		c.gridx = 0;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		demandModelPanel = DemandModelPanelFactory.createDemandModelPanel(this, demandModel);
		contentPanel.add(demandModelPanel, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		contentPanel.add(buttonPanel, c);

		okButton = new JButton("OK", new ImageIcon(getClass().getClassLoader().getResource("icons/comment_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDemandModel();
			}
		});
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("icons/comment_delete.png")));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton);

		getRootPane().setDefaultButton(okButton);
		setContentPane(contentPanel);
	}
	
	/**
	 * Initializes the dialog for a new demand model.
	 */
	private void initialize() {
		nameText.setText(demandModel.getName());
	}
	
	/**
	 * Checks if is demand model valid.
	 *
	 * @return true, if is demand model valid
	 */
	private boolean isDemandModelValid() {
		if(nameText.getText().length()==0) {
			JOptionPane.showMessageDialog(this, 
					"Please enter a demand model name.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if(!demandModelPanel.isDemandModelValid()) return false;
		return true;
	}
	
	/**
	 * Saves the demand model.
	 */
	private void saveDemandModel() {
		if(isDemandModelValid()) {
			demandModel.setName(nameText.getText());
			demandModelPanel.saveDemandModel();
			if(missionPanel != null) {
				if(!missionPanel.getMissionSplitPane().getMission().getDemandModels().contains(demandModel)) {
					missionPanel.getMissionSplitPane().getMission().getDemandModels().add(demandModel);
				}
			} else if(stateDialog != null) {
				if(!stateDialog.containsDemandModel(demandModel)) {
					stateDialog.addDemandModel(demandModel);
				}
			}
			if(missionPanel != null) {
				missionPanel.updateView();
			} else if(stateDialog != null) {
				stateDialog.repaint();
			}
			dispose();
		}
	}
	
	/**
	 * Gets the mission panel.
	 * 
	 * @return the mission panel
	 */
	public MissionPanel getMissionPanel() {
		return missionPanel;
	}
	
	/**
	 * Gets the state dialog.
	 * 
	 * @return the state dialog
	 */
	public StateDialog getStateDialog() {
		return stateDialog;
	}
	
	/**
	 * Creates and shows the dialog.
	 * 
	 * @param missionsTab the missions tab
	 * @param demandModel the demand model
	 */
	public static void createAndShowGUI(MissionPanel missionPanel, I_DemandModel demandModel) {
		DemandModelDialog d = new DemandModelDialog(missionPanel, demandModel);
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
	
	/**
	 * Creates and shows the dialog.
	 * 
	 * @param stateDialog the state dialog
	 * @param demandModel the demand model
	 */
	public static void createAndShowGUI(StateDialog stateDialog, I_DemandModel demandModel) {
		DemandModelDialog d = new DemandModelDialog(stateDialog, demandModel);
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
}
