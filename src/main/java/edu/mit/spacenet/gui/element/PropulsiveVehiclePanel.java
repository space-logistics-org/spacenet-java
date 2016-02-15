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
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.ResourceTank;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.renderer.ResourceListCellRenderer;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The element panel for viewing and editing propulsive vehicle-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class PropulsiveVehiclePanel extends AbstractElementPanel {
	private static final long serialVersionUID = 6335483106289763369L;
	
	private PropulsiveVehicle vehicle;
	
	private JCheckBox omsCheck, rcsCheck, sharedCheck;
	private SpinnerNumberModel omsIspModel, maxOmsFuelModel, omsFuelModel, 
		rcsIspModel, maxRcsFuelModel, rcsFuelModel;
	private JSpinner omsIspSpinner, maxOmsFuelSpinner, omsFuelSpinner,
		rcsIspSpinner, maxRcsFuelSpinner, rcsFuelSpinner;
	private UnitsWrapper maxOmsFuelWrapper, omsFuelWrapper, maxRcsFuelWrapper, rcsFuelWrapper;
	private JComboBox omsResourceCombo, rcsResourceCombo;
	
	/**
	 * Instantiates a new propulsive vehicle panel.
	 * 
	 * @param elementDialog the element dialog
	 * @param vehicle the vehicle
	 */
	public PropulsiveVehiclePanel(ElementDialog elementDialog, PropulsiveVehicle vehicle) {
		super(elementDialog, vehicle);
		this.vehicle = vehicle;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel omsPanel = new JPanel();
		omsPanel.setLayout(new GridBagLayout());
		omsPanel.setBorder(BorderFactory.createTitledBorder("OMS Engine"));
		add(omsPanel);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		omsPanel.add(new JLabel("Isp: "), c);
		c.gridy++;
		omsPanel.add(new JLabel("Fuel Type: "), c);
		c.gridy++;
		omsPanel.add(new JLabel("Max Fuel: "), c);
		c.gridy++;
		if(getElementDialog().getDataSourceDialog()==null)
			omsPanel.add(new JLabel("Initial Fuel: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		omsCheck = new JCheckBox("OMS Capabilities");
		omsPanel.add(omsCheck, c);
		
		c.gridy++;
		omsIspModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,0.1);
		omsIspSpinner = new JSpinner(omsIspModel);
		omsIspSpinner.setPreferredSize(new Dimension(50,20));
		omsPanel.add(new UnitsWrapper(omsIspSpinner, "s"), c);
		omsIspSpinner.setToolTipText("Specific impulse of orbital maneuvering system [seconds]");
		
		c.gridy++;
		omsResourceCombo = new JComboBox();
		omsResourceCombo.setRenderer(new ResourceListCellRenderer());
		omsResourceCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& omsResourceCombo.getSelectedItem() != null
						&& omsResourceCombo.getSelectedItem() instanceof I_Resource) {
					String fuelUnits = ((I_Resource)omsResourceCombo.getSelectedItem()).getUnits();
					maxOmsFuelWrapper.setUnits(fuelUnits);
					omsFuelWrapper.setUnits(fuelUnits);
				}
			}
		});
		omsPanel.add(omsResourceCombo, c);
		
		c.gridy++;
		maxOmsFuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		maxOmsFuelSpinner = new JSpinner(maxOmsFuelModel);
		maxOmsFuelSpinner.setPreferredSize(new Dimension(150,20));
		maxOmsFuelSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				omsFuelSpinner.setValue(maxOmsFuelSpinner.getValue());
			}
		});
		maxOmsFuelWrapper = new UnitsWrapper(maxOmsFuelSpinner, "");
		omsPanel.add(maxOmsFuelWrapper, c);
		maxOmsFuelSpinner.setToolTipText("Maximum amount of orbital maneuvering system fuel");
		
		c.gridy++;
		omsFuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		omsFuelSpinner = new JSpinner(omsFuelModel);
		omsFuelSpinner.setPreferredSize(new Dimension(150,20));
		omsFuelWrapper = new UnitsWrapper(omsFuelSpinner, "");
		if(getElementDialog().getDataSourceDialog()==null)
			omsPanel.add(omsFuelWrapper, c);
		omsFuelSpinner.setToolTipText("Initial amount of orbital maneuvering system fuel");
		
		
		JPanel rcsPanel = new JPanel();
		rcsPanel.setLayout(new GridBagLayout());
		rcsPanel.setBorder(BorderFactory.createTitledBorder("RCS Engine"));
		add(rcsPanel);
		
		c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		rcsPanel.add(new JLabel("Isp: "), c);
		c.gridy+=2;
		rcsPanel.add(new JLabel("Fuel Type: "), c);
		c.gridy++;
		rcsPanel.add(new JLabel("Max Fuel: "), c);
		c.gridy++;
		if(getElementDialog().getDataSourceDialog()==null)
		rcsPanel.add(new JLabel("Initial Fuel: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 1;
		rcsCheck = new JCheckBox("RCS Capabilities");
		rcsPanel.add(rcsCheck, c);
		c.gridy++;
		
		rcsIspModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,0.1);
		rcsIspSpinner = new JSpinner(rcsIspModel);
		rcsIspSpinner.setPreferredSize(new Dimension(50,20));
		rcsPanel.add(new UnitsWrapper(rcsIspSpinner, "s"), c);
		rcsIspSpinner.setToolTipText("Specific impulse of reaction control system [seconds]");
		
		c.gridy++;
		sharedCheck = new JCheckBox("Shared OMS Fuel Tank");
		sharedCheck.setOpaque(false);
		rcsPanel.add(sharedCheck, c);
		sharedCheck.setToolTipText("Share fuel between the orbital maneuvering and reaction control systems");
		
		c.gridy++;
		rcsResourceCombo = new JComboBox();
		rcsResourceCombo.setRenderer(new ResourceListCellRenderer());
		rcsResourceCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& rcsResourceCombo.getSelectedItem() != null
						&& rcsResourceCombo.getSelectedItem() instanceof I_Resource) {
					String fuelUnits = ((I_Resource)rcsResourceCombo.getSelectedItem()).getUnits();
					maxRcsFuelWrapper.setUnits(fuelUnits);
					rcsFuelWrapper.setUnits(fuelUnits);
				}
			}
		});
		rcsPanel.add(rcsResourceCombo, c);
		
		c.gridy++;
		maxRcsFuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		maxRcsFuelSpinner = new JSpinner(maxRcsFuelModel);
		maxRcsFuelSpinner.setPreferredSize(new Dimension(150,20));
		maxRcsFuelSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rcsFuelSpinner.setValue(maxRcsFuelSpinner.getValue());
			}
		});
		maxRcsFuelWrapper = new UnitsWrapper(maxRcsFuelSpinner, "");
		rcsPanel.add(maxRcsFuelWrapper, c);
		maxRcsFuelSpinner.setToolTipText("Maximum amount of reaction control system fuel");
		
		c.gridy++;
		rcsFuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		rcsFuelSpinner = new JSpinner(rcsFuelModel);
		rcsFuelSpinner.setPreferredSize(new Dimension(150,20));
		rcsFuelWrapper = new UnitsWrapper(rcsFuelSpinner, "");
		if(getElementDialog().getDataSourceDialog()==null)
			rcsPanel.add(rcsFuelWrapper, c);
		rcsFuelSpinner.setToolTipText("Initial amount of reaction control system fuel");
	}
	
	/**
	 * Initializes the panel components for a new propulsive vehicle.
	 */
	private void initialize() {
		omsResourceCombo.addItem(null);
		rcsResourceCombo.addItem(null);
		for(I_Resource r : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary()) {
			if(r.getClassOfSupply().equals(ClassOfSupply.COS1)
					|| r.getClassOfSupply().isSubclassOf(ClassOfSupply.COS1)) {
				omsResourceCombo.addItem(r);
				rcsResourceCombo.addItem(r);
			}
		}
		for(ClassOfSupply cos : ClassOfSupply.values()) {
			if(cos.equals(ClassOfSupply.COS1)
					|| cos.isSubclassOf(ClassOfSupply.COS1)) {
				omsResourceCombo.addItem(new GenericResource(cos));
				rcsResourceCombo.addItem(new GenericResource(cos));
			}
		}
		if(vehicle.getOmsIsp() > 0) omsCheck.setSelected(true);
		else omsCheck.setSelected(false);
		
		if(vehicle.getRcsIsp() > 0) rcsCheck.setSelected(true);
		else rcsCheck.setSelected(false);
		
		if(vehicle.getOmsFuelTank() == vehicle.getRcsFuelTank())
			sharedCheck.setSelected(true);
		else sharedCheck.setSelected(false);
		
		omsCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		rcsCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		sharedCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		
		if(omsCheck.isSelected()) {
			omsIspModel.setValue(vehicle.getOmsIsp());
			if(vehicle.getOmsFuelTank() != null) {
				maxOmsFuelModel.setValue(vehicle.getOmsFuelTank().getMaxAmount());
				omsFuelModel.setValue(vehicle.getOmsFuelTank().getAmount());
				omsResourceCombo.setSelectedItem(vehicle.getOmsFuelTank().getResource());
			}
		}
		if(rcsCheck.isSelected() && sharedCheck.isSelected()) {
			rcsIspModel.setValue(vehicle.getRcsIsp());
		} else if(rcsCheck.isSelected()) {
			rcsIspModel.setValue(vehicle.getRcsIsp());
			if(vehicle.getRcsFuelTank() != null) {
				maxRcsFuelModel.setValue(vehicle.getRcsFuelTank().getMaxAmount());
				rcsFuelModel.setValue(vehicle.getRcsFuelTank().getAmount());
				rcsResourceCombo.setSelectedItem(vehicle.getRcsFuelTank().getResource());
			}
		}
		repaint();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if(omsCheck.isSelected()) {
			omsIspSpinner.setEnabled(true);
			maxOmsFuelSpinner.setEnabled(true);
			omsFuelSpinner.setEnabled(true);
			omsResourceCombo.setEnabled(true);
		} else {
			omsIspModel.setValue(0);
			omsIspSpinner.setEnabled(false);
			maxOmsFuelModel.setValue(0);
			maxOmsFuelSpinner.setEnabled(false);
			omsFuelModel.setValue(0);
			omsFuelSpinner.setEnabled(false);
			omsResourceCombo.setSelectedItem(null);
			omsResourceCombo.setEnabled(false);
		}
		if(rcsCheck.isSelected() && sharedCheck.isSelected()) {
			rcsIspSpinner.setEnabled(true);
			sharedCheck.setEnabled(true);
			maxRcsFuelModel.setValue(0);
			maxRcsFuelSpinner.setEnabled(false);
			rcsFuelModel.setValue(0);
			rcsFuelSpinner.setEnabled(false);
			rcsResourceCombo.setSelectedItem(null);
			rcsResourceCombo.setEnabled(false);
		} else if(rcsCheck.isSelected()) {
			rcsIspSpinner.setEnabled(true);
			sharedCheck.setEnabled(true);
			maxRcsFuelSpinner.setEnabled(true);
			rcsFuelSpinner.setEnabled(true);
			rcsResourceCombo.setEnabled(true);
		} else {
			sharedCheck.setSelected(false);
			sharedCheck.setEnabled(false);
			rcsIspModel.setValue(0);
			rcsIspSpinner.setEnabled(false);
			maxRcsFuelModel.setValue(0);
			maxRcsFuelSpinner.setEnabled(false);
			rcsFuelModel.setValue(0);
			rcsFuelSpinner.setEnabled(false);
			rcsResourceCombo.setSelectedItem(null);
			rcsResourceCombo.setEnabled(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public PropulsiveVehicle getElement() {
		return vehicle;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		if(omsCheck.isSelected()) {
			vehicle.setOmsIsp(omsIspModel.getNumber().doubleValue());
			ResourceTank omsFuelTank;
			if(vehicle.getOmsFuelTank()!=null) omsFuelTank = vehicle.getOmsFuelTank();
			else {
				omsFuelTank = new ResourceTank();
				vehicle.setOmsFuelTank(omsFuelTank);
			}
			omsFuelTank.setMaxAmount(maxOmsFuelModel.getNumber().doubleValue());
			omsFuelTank.setAmount(Math.min(maxOmsFuelModel.getNumber().doubleValue(), 
					omsFuelModel.getNumber().doubleValue()));
			omsFuelTank.setResource((I_Resource)omsResourceCombo.getSelectedItem());
		} else {
			vehicle.setOmsIsp(0);
			vehicle.setOmsFuelTank(null);
		}
		if(rcsCheck.isSelected() && !sharedCheck.isSelected()) {
			vehicle.setRcsIsp(rcsIspModel.getNumber().doubleValue());
			ResourceTank rcsFuelTank;
			if(vehicle.getRcsFuelTank()!=null) rcsFuelTank = vehicle.getRcsFuelTank();
			else {
				rcsFuelTank = new ResourceTank();
				vehicle.setRcsFuelTank(rcsFuelTank);
			}
			rcsFuelTank.setMaxAmount(maxRcsFuelModel.getNumber().doubleValue());
			rcsFuelTank.setAmount(Math.min(maxRcsFuelModel.getNumber().doubleValue(), 
					rcsFuelModel.getNumber().doubleValue()));
			rcsFuelTank.setResource((I_Resource)rcsResourceCombo.getSelectedItem());
		} else if(rcsCheck.isSelected() && sharedCheck.isSelected()) {
			vehicle.setRcsIsp(rcsIspModel.getNumber().doubleValue());
			vehicle.setRcsFuelTank(vehicle.getOmsFuelTank());
		} else {
			vehicle.setRcsIsp(0);
			vehicle.setRcsFuelTank(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
	 */
	@Override
	public boolean isElementValid() {
		if((maxOmsFuelModel.getNumber().doubleValue() > 0 
				|| omsFuelModel.getNumber().doubleValue() > 0) 
				&& omsResourceCombo.getSelectedItem()==null)
			return false;
		if((maxRcsFuelModel.getNumber().doubleValue() > 0 
				|| rcsFuelModel.getNumber().doubleValue() > 0) 
				&& rcsResourceCombo.getSelectedItem()==null)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isVerticallyExpandable()
	 */
	public boolean isVerticallyExpandable() {
		return false;
	}
}
