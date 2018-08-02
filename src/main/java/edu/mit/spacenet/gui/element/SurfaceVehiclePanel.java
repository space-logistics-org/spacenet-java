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
package edu.mit.spacenet.gui.element;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.renderer.ResourceListCellRenderer;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element panel for viewing and editing surface vehicle-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class SurfaceVehiclePanel extends AbstractElementPanel {
	private static final long serialVersionUID = 6335483106289763369L;
	
	private SurfaceVehicle vehicle;
	
	private JSpinner maxSpeedSpinner, maxFuelSpinner, fuelSpinner;
	private SpinnerNumberModel maxSpeedModel, maxFuelModel, fuelModel;
	private UnitsWrapper maxFuelWrapper, fuelWrapper;
	private JComboBox resourceCombo;
	
	/**
	 * Instantiates a new surface vehicle panel.
	 * 
	 * @param elementDialog the element dialog
	 * @param vehicle the vehicle
	 */
	public SurfaceVehiclePanel(ElementDialog elementDialog, SurfaceVehicle vehicle) {
		super(elementDialog, vehicle);
		this.vehicle = vehicle;
		setLayout(new GridBagLayout());
		setOpaque(false);
		buildPanel();
		initialize();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Maximum Speed: "), c);
		c.gridy++;
		add(new JLabel("Fuel Type: "), c);
		c.gridy++;
		add(new JLabel("Max Fuel: "), c);
		c.gridx+=2;
		c.weightx = 1;
		add(new JLabel("Initial Fuel: "), c);
		
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 3;
		maxSpeedModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,0.1);
		maxSpeedSpinner = new JSpinner(maxSpeedModel);
		maxSpeedSpinner.setPreferredSize(new Dimension(50,20));
		add(new UnitsWrapper(maxSpeedSpinner, "km/hr"), c);
		maxSpeedSpinner.setToolTipText("Maximum surface transport speed [kilometers per hour]");
		
		c.gridy++;
		resourceCombo = new JComboBox();
		resourceCombo.addItem(null);
		for(I_Resource r : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary()) {					
			if(r.getClassOfSupply().equals(ClassOfSupply.COS1)
						|| r.getClassOfSupply().isSubclassOf(ClassOfSupply.COS1)) {
				resourceCombo.addItem(r);
			}
		}
		for(ClassOfSupply cos : ClassOfSupply.values()) {
			if(cos.equals(ClassOfSupply.COS1)
					|| cos.isSubclassOf(ClassOfSupply.COS1)) {
				resourceCombo.addItem(new GenericResource(cos));
			}
		}
		resourceCombo.setRenderer(new ResourceListCellRenderer());
		resourceCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& resourceCombo.getSelectedItem() != null) {
					String fuelUnits = ((I_Resource)resourceCombo.getSelectedItem()).getUnits();
					maxFuelWrapper.setUnits(fuelUnits);
					fuelWrapper.setUnits(fuelUnits);
				}
			}
		});
		add(resourceCombo, c);
		
		c.gridy++;
		c.gridwidth = 1;		
		maxFuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		maxFuelSpinner = new JSpinner(maxFuelModel);
		maxFuelSpinner.setPreferredSize(new Dimension(75,20));
		maxFuelWrapper = new UnitsWrapper(maxFuelSpinner, "");
		add(maxFuelWrapper, c);
		maxFuelSpinner.setToolTipText("Maximum amount of fuel");
		
		c.gridx+=2;
		fuelModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		fuelSpinner = new JSpinner(fuelModel);
		fuelSpinner.setPreferredSize(new Dimension(75,20));
		fuelWrapper = new UnitsWrapper(fuelSpinner, "");
		add(fuelWrapper, c);
		fuelSpinner.setToolTipText("Initial amount of fuel");
	}
	
	/**
	 * Initializes the panel components for a new surface vehicle.
	 */
	private void initialize() {
		maxSpeedModel.setValue(vehicle.getMaxSpeed());
		maxFuelModel.setValue(vehicle.getFuelTank().getMaxAmount());
		fuelModel.setValue(vehicle.getFuelTank().getAmount());
		resourceCombo.setSelectedItem(vehicle.getFuelTank().getResource());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public SurfaceVehicle getElement() {
		return vehicle;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		vehicle.setMaxSpeed(maxSpeedModel.getNumber().doubleValue());
		vehicle.getFuelTank().setMaxAmount(maxFuelModel.getNumber().doubleValue());
		vehicle.getFuelTank().setAmount(Math.min(maxFuelModel.getNumber().doubleValue(), fuelModel.getNumber().doubleValue()));
		vehicle.getFuelTank().setResource((I_Resource)resourceCombo.getSelectedItem());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
	 */
	@Override
	public boolean isElementValid() {
		if((maxFuelModel.getNumber().doubleValue() > 0 
				|| fuelModel.getNumber().doubleValue() > 0) 
				&& resourceCombo.getSelectedItem()==null)
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
