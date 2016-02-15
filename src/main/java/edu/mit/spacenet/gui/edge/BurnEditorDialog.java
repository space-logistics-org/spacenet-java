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
package edu.mit.spacenet.gui.edge;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.BurnType;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class BurnEditorDialog extends JDialog {
	private static final long serialVersionUID = 4910581442994056108L;
	
	private Burn burn;
	private SpaceEdgeEditorPanel panel;
	
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox burnTypeCombo;
	private SpinnerNumberModel timeModel, deltaVModel;
	private JSpinner timeSpinner, deltaVSpinner;
	private JTextArea descriptionText;
	
	/**
	 * Instantiates a new burn editor dialog.
	 * 
	 * @param edgeDialog the edge dialog
	 * @param burn the burn
	 */
	private BurnEditorDialog(SpaceEdgeEditorPanel panel, Burn burn) {
		super(panel.getDialog(), "Edit Burn", true);
		this.burn = burn;
		this.panel = panel;
		buildDialog();
		initialize();
	}
	
	/**
	 * Builds the dialog.
	 */
	public void buildDialog(){
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Burn Type: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		burnTypeCombo = new JComboBox();
		for(BurnType t : BurnType.values()) burnTypeCombo.addItem(t);
		burnTypeCombo.setToolTipText("Type of burn: orbital maneuvering system (OMS) or reaction control system (RCS)");
		contentPanel.add(burnTypeCombo, c);
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Burn Time: "), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		timeModel = new SpinnerNumberModel(0, 0, panel.getEdgeDuration(), GlobalParameters.getTimePrecision());
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setPreferredSize(new Dimension(75,20));
		timeSpinner.setToolTipText("Time (after transport start) to perform burn [days]");
		contentPanel.add(new UnitsWrapper(timeSpinner, "days"), c);
		
		c.gridy++;
		c.gridx=0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Delta-V: "), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		deltaVModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE, 0.01);
		deltaVSpinner = new JSpinner(deltaVModel);
		deltaVSpinner.setPreferredSize(new Dimension(75,20));
		deltaVSpinner.setToolTipText("Required change in velocity to achieve [meters per second]");
		contentPanel.add(new UnitsWrapper(deltaVSpinner, "m/s"), c);
		
		c.gridy++;
		c.gridx=0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Description: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(5,10);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionText.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
		contentPanel.add(new JScrollPane(descriptionText), c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		okButton = new JButton(burn.getTid()<0?"Save":"Save", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveBurn();
			}
		});
		buttonPanel.add(okButton);
		
		cancelButton = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("icons/database.png")));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, c);
		
		setContentPane(contentPanel);
	}
	
	/**
	 * Initializes the components.
	 */
	private void initialize() {
		if(burn!= null){
			burnTypeCombo.setSelectedItem(burn.getBurnType());
			timeModel.setValue(burn.getTime());
			deltaVModel.setValue(burn.getDeltaV());
			descriptionText.setText(burn.getDescription());
		}
	}
	
	/**
	 * Gets the burn.
	 * 
	 * @return the burn
	 */
	public Burn getBurn(){
		return this.burn;
	}
	
	/**
	 * Saves the burn.
	 */
	public void saveBurn(){
		if(isBurnValid()) {
			try {
				burn.setBurnType((BurnType)burnTypeCombo.getSelectedItem());
				burn.setTime(timeModel.getNumber().doubleValue());
				burn.setDeltaV(deltaVModel.getNumber().doubleValue());
				burn.setDescription(descriptionText.getText());
				if(!panel.getBurnListModel().contains(burn)) {
					panel.getBurnListModel().addElement(burn);
				}
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(panel.getDialog(), 
						"SpaceNet Errror",
						"An error occurred while saving the edge.", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Checks if is burn valid.
	 * 
	 * @return true, if is burn valid
	 */
	public boolean isBurnValid(){
		if(deltaVModel.getNumber().doubleValue()==0) {
			JOptionPane.showMessageDialog(this, 
					"Please select a non-zero delta-v.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * Creates and shows the and GUI.
	 * 
	 * @param spaceEdgePanel the space edge panel
	 * @param burn the burn
	 * @param burnDuration the burn duration
	 */
	public static void createAndShowGUI(SpaceEdgeEditorPanel spaceEdgePanel, Burn burn) {
		BurnEditorDialog d = new BurnEditorDialog(spaceEdgePanel, burn);	
		d.setMinimumSize(new Dimension(300,150));
		d.pack();
		d.setLocationRelativeTo(d.getParent());	
		d.setVisible(true);
	}
}