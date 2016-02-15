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
package edu.mit.spacenet.gui.element;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.renderer.ResourceListCellRenderer;

/**
 * A component for viewing and editing part applications for an element.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class PartApplicationDialog extends JDialog {
	private static final long serialVersionUID = -9173211009538452900L;
	
	private JComboBox partCombo;
	private JCheckBox failureCheck, repairCheck;
	private SpinnerNumberModel mttfModel, mttrModel, repairMassModel, quantityModel, dutyCycleModel;
	private JSpinner mttfSpinner, mttrSpinner, repairMassSpinner, quantitySpinner, dutyCycleSpinner;
	private JButton btnOk, btnCancel;
	
	private ElementDialog elementDialog;
	private PartApplication partApplication;

	/**
	 * Instantiates a new part application dialog.
	 * 
	 * @param elementDialog the element dialog
	 * @param partApplication the part application
	 */
	public PartApplicationDialog(ElementDialog elementDialog, PartApplication partApplication) {
		super(elementDialog, "Part Application");
		this.elementDialog = elementDialog;
		this.partApplication = partApplication;
		
		buildDialog();
		initialize();
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3,3,3,3);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		contentPanel.add(new JLabel("Resource: "), c);
		c.gridy++;
		contentPanel.add(new JLabel("Quantity: "), c);
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		partCombo = new JComboBox();
		partCombo.setRenderer(new ResourceListCellRenderer());
		contentPanel.add(partCombo, c);
		c.gridy++;
		quantityModel = new SpinnerNumberModel(1,0,100,1);
		quantitySpinner = new JSpinner(quantityModel);
		contentPanel.add(quantitySpinner, c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		contentPanel.add(buildFailurePanel(), c);
		
		c.gridy++;
		contentPanel.add(buildRepairPanel(), c);
		
		c.gridy++;
		c.gridx=0;
		c.gridwidth=4;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePart();
			}
		});
		getRootPane().setDefaultButton(btnOk);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		buttonPanel.add(btnOk);
		buttonPanel.add(btnCancel);
		contentPanel.add(buttonPanel, c);
		
		setContentPane(contentPanel);
		setModal(true);
	}
	
	/**
	 * Builds the failure panel.
	 *
	 * @return the j panel
	 */
	private JPanel buildFailurePanel() {
		JPanel failurePanel = new JPanel();
		failurePanel.setBorder(BorderFactory.createTitledBorder("Failure Analysis"));
		failurePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		
		failureCheck = new JCheckBox("Enable Failure Analysis");
		failureCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					dutyCycleSpinner.setEnabled(true);
					mttfSpinner.setEnabled(true);
				} else {
					dutyCycleSpinner.setEnabled(false);
					dutyCycleSpinner.setValue(0);
					mttfSpinner.setEnabled(false);
					mttfSpinner.setValue(0);
				}
			}
		});
		failurePanel.add(failureCheck, c);
		
		c.gridy++;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		failurePanel.add(new JLabel("Duty Cycle: "), c);
		c.gridy++;
		failurePanel.add(new JLabel("Mean Time to Failure: "), c);
		
		c.gridy--;
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		dutyCycleModel = new SpinnerNumberModel(1,0,1,.1);
		dutyCycleSpinner = new JSpinner(dutyCycleModel);
		dutyCycleSpinner.setPreferredSize(new Dimension(50,20));
		failurePanel.add(dutyCycleSpinner, c);
		dutyCycleSpinner.setToolTipText("Fraction of time this part is used [unitless]");
		
		c.gridy++;
		mttfModel = new SpinnerNumberModel(0,0,1000000,100.);
		mttfSpinner = new JSpinner(mttfModel);
		failurePanel.add(new UnitsWrapper(mttfSpinner, "hours"), c);
		mttfSpinner.setToolTipText("Mean time to failure of part [hours]");
		
		return failurePanel;
	}
	
	/**
	 * Builds the repair panel.
	 *
	 * @return the j panel
	 */
	private JPanel buildRepairPanel() {
		JPanel repairPanel = new JPanel();
		repairPanel.setBorder(BorderFactory.createTitledBorder("Repair Analysis"));
		repairPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		
		repairCheck = new JCheckBox("Enable Repair Analysis");
		repairCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					mttrSpinner.setEnabled(true);
					repairMassSpinner.setEnabled(true);
				} else {
					mttrSpinner.setEnabled(false);
					mttrSpinner.setValue(0);
					repairMassSpinner.setEnabled(false);
					repairMassSpinner.setValue(0);
				}
			}
		});
		repairPanel.add(repairCheck, c);
		
		c.gridy++;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		repairPanel.add(new JLabel("Mean Time to Repair: "), c);
		c.gridy++;
		repairPanel.add(new JLabel("Mean Repair Mass: "), c);
		
		c.gridy--;
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		mttrModel = new SpinnerNumberModel(0,0,1000, 1.);
		mttrSpinner = new JSpinner(mttrModel);
		repairPanel.add(new UnitsWrapper(mttrSpinner, "hours"), c);
		mttrSpinner.setToolTipText("Time to repair this part [hours]");
		
		c.gridy++;
		repairMassModel = new SpinnerNumberModel(0,0,1000, .5);
		repairMassSpinner = new JSpinner(repairMassModel);
		repairPanel.add(new UnitsWrapper(repairMassSpinner, "kg"), c);
		repairMassSpinner.setToolTipText("Mass required to repair this part [kilograms]");
		
		return repairPanel;
	}
	
	/**
	 * Initializes the dialog for a new part application.
	 */
	private void initialize() {
		for(I_Item item : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getItemLibrary()) {
			partCombo.addItem(item);
			if(item.equals(partApplication.getPart())) 
				partCombo.setSelectedItem(item);
		}
		quantityModel.setValue(partApplication.getQuantity());
		
		mttfModel.setValue(partApplication.getMeanTimeToFailure());
		dutyCycleModel.setValue(partApplication.getDutyCycle());
		
		failureCheck.setSelected(partApplication.getMeanTimeToFailure()>0);
		mttfSpinner.setEnabled(partApplication.getMeanTimeToFailure()>0);
		dutyCycleSpinner.setEnabled(partApplication.getMeanTimeToFailure()>0);
		
		mttrModel.setValue(partApplication.getMeanTimeToRepair());
		repairMassModel.setValue(partApplication.getMassToRepair());
		
		repairCheck.setSelected(partApplication.getMeanTimeToRepair()>0);
		mttrSpinner.setEnabled(partApplication.getMeanTimeToRepair()>0);
		repairMassSpinner.setEnabled(partApplication.getMeanTimeToRepair()>0);
	}
	
	/**
	 * Gets the element dialog.
	 * 
	 * @return the element dialog
	 */
	public ElementDialog getElementDialog() {
		return elementDialog;
	}
	
	/**
	 * Checks if is part valid.
	 * 
	 * @return true, if is part valid
	 */
	private boolean isPartValid() {
		if(partCombo.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(this, 
					"Please select a resource.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * Save part.
	 */
	private void savePart() {
		if(isPartValid()) {
			partApplication.setPart((Item)partCombo.getSelectedItem());
			partApplication.setMeanTimeToFailure(mttfModel.getNumber().doubleValue());
			partApplication.setMeanTimeToRepair(mttrModel.getNumber().doubleValue());
			partApplication.setMassToRepair(repairMassModel.getNumber().doubleValue());
			partApplication.setQuantity(quantityModel.getNumber().intValue());
			partApplication.setDutyCycle(dutyCycleModel.getNumber().doubleValue());
			if(!elementDialog.containsPart(partApplication)) {
				elementDialog.addPart(partApplication);
			}
			elementDialog.repaint();
			dispose();
		}
	}
	
	/**
	 * Creates the and shows the gui.
	 * 
	 * @param elementDialog the element dialog
	 * @param part the part
	 */
	public static void createAndShowGUI(ElementDialog elementDialog, PartApplication part) {
		PartApplicationDialog d = new PartApplicationDialog(elementDialog, part);
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
}
