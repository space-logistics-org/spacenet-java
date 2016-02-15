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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Element panel for viewing and editing carrier-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class CarrierPanel extends AbstractElementPanel {
	private static final long serialVersionUID = 6335483106289763369L;
	
	private Carrier carrier;
	
	private JSpinner maxCrewSpinner, maxCargoMassSpinner, maxCargoVolumeSpinner;
	private SpinnerNumberModel maxCargoMassModel, maxCargoVolumeModel, maxCrewModel;
	private JComboBox cargoEnvironmentCombo;
	
	/**
	 * Instantiates a new carrier panel.
	 * 
	 * @param elementDialog the element dialog
	 * @param carrier the carrier
	 */
	public CarrierPanel(ElementDialog elementDialog, Carrier carrier) {
		super(elementDialog, carrier);
		this.carrier = carrier;
		buildPanel();
		initialize();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Cargo Environment: "), c);
		c.gridx+=2;
		add(new JLabel("Max Crew Size: "), c);
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Max Cargo Mass: "), c);
		c.gridx+=2;
		add(new JLabel("Max Cargo Volume: "), c);
		
		c.gridy = 0;
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		cargoEnvironmentCombo = new JComboBox();
		for(Environment e : Environment.values()) {
			cargoEnvironmentCombo.addItem(e);
		}
		add(cargoEnvironmentCombo, c);
		cargoEnvironmentCombo.setToolTipText("Storage environment available for nested elements");
		
		c.gridx+=2;
		maxCrewModel = new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1);
		maxCrewSpinner = new JSpinner(maxCrewModel);
		maxCrewSpinner.setPreferredSize(new Dimension(150,20));
		add(maxCrewSpinner, c);
		maxCrewSpinner.setToolTipText("Maximum number of nested crew");
		
		c.gridx = 1;
		c.gridy++;
		maxCargoMassModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		maxCargoMassSpinner = new JSpinner(maxCargoMassModel);
		maxCargoMassSpinner.setPreferredSize(new Dimension(150,20));
		add(new UnitsWrapper(maxCargoMassSpinner, "kg"),c);
		maxCargoMassSpinner.setToolTipText("Maximum mass of nested elements [kilograms]");
		
		c.gridx+=2;
		maxCargoVolumeModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getVolumePrecision());
		maxCargoVolumeSpinner = new JSpinner(maxCargoVolumeModel);
		maxCargoVolumeSpinner.setPreferredSize(new Dimension(150,20));
		add(new UnitsWrapper(maxCargoVolumeSpinner, "m^3"),c);
		maxCargoVolumeSpinner.setToolTipText("Maximum volume of nested elements [cubic meters]");
	}
	
	/**
	 * Initializes the panel components for a carrier.
	 */
	private void initialize() {
		cargoEnvironmentCombo.setSelectedItem(carrier.getCargoEnvironment());
		maxCargoMassModel.setValue(carrier.getMaxCargoMass());
		maxCargoVolumeModel.setValue(carrier.getMaxCargoVolume());
		maxCrewModel.setValue(carrier.getMaxCrewSize());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public Carrier getElement() {
		return carrier;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		carrier.setCargoEnvironment((Environment)cargoEnvironmentCombo.getSelectedItem());
		carrier.setMaxCargoMass(maxCargoMassModel.getNumber().doubleValue());
		carrier.setMaxCargoVolume(maxCargoVolumeModel.getNumber().doubleValue());
		carrier.setMaxCrewSize(maxCrewModel.getNumber().intValue());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
	 */
	@Override
	public boolean isElementValid() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isVerticallyExpandable()
	 */
	public boolean isVerticallyExpandable() {
		return false;
	}
}
