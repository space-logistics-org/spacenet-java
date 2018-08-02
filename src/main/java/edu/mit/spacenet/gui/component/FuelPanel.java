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
package edu.mit.spacenet.gui.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.SurfaceVehicle;

/**
 * The FuelPanel class displays fuel progress bars for oms/rcs/fuel tanks.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class FuelPanel extends JPanel {
	private static final long serialVersionUID = 8033464124593156155L;
	private JProgressBar omsCapacity, rcsCapacity, fuelCapacity;
	private JLabel omsLabel, rcsLabel, fuelLabel;
	private DecimalFormat fuelFormat = new DecimalFormat("0.0");
	
	/**
	 * Instantiates a new capacity panel.
	 */
	public FuelPanel() {
		initializePanel();
	}
	
	/**
	 * Initialize panel.
	 */
	private void initializePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		omsCapacity = new JProgressBar(0, 100);
		omsCapacity.setForeground(new Color(0, 153, 0));
		omsCapacity.setStringPainted(true);
		omsCapacity.setVisible(false);
		add(omsCapacity, c);
		c.gridy++;
		rcsCapacity = new JProgressBar(0, 100);
		rcsCapacity.setStringPainted(true);
		rcsCapacity.setForeground(new Color(0, 153, 0));
		rcsCapacity.setVisible(false);
		add(rcsCapacity, c);
		c.gridy++;
		fuelCapacity = new JProgressBar(0, 100);
		fuelCapacity.setStringPainted(true);
		fuelCapacity.setForeground(new Color(0, 153, 0));
		fuelCapacity.setVisible(false);
		add(fuelCapacity, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx++;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		omsLabel = new JLabel(" OMS Fuel");
		omsLabel.setVisible(false);
		add(omsLabel, c);
		c.gridy++;
		rcsLabel = new JLabel(" RCS Fuel");
		rcsLabel.setVisible(false);
		add(rcsLabel, c);
		c.gridy++;
		fuelLabel = new JLabel(" Fuel");
		fuelLabel.setVisible(false);
		add(fuelLabel, c);
	}
	
	/**
	 * Update capacities.
	 *
	 * @param vehicle the vehicle
	 * @return true, if successful
	 */
	public void updateFuel(PropulsiveVehicle vehicle) {
		if(vehicle.getOmsFuelTank()!=null) {
			omsLabel.setVisible(true);
			omsCapacity.setVisible(true);
			omsCapacity.setValue((int)(100*vehicle.getOmsFuelTank().getAmount()/vehicle.getOmsFuelTank().getMaxAmount()));
			omsCapacity.setString(fuelFormat.format(vehicle.getOmsFuelTank().getAmount()) 
					+ " / " + fuelFormat.format(vehicle.getOmsFuelTank().getMaxAmount())
					+ " " + vehicle.getOmsFuelTank().getResource().getUnits());
		} else {
			omsLabel.setVisible(false);
			omsCapacity.setVisible(false);
		}
		if(vehicle.getRcsFuelTank() != null 
				&& vehicle.getRcsFuelTank()!=vehicle.getOmsFuelTank()) {
			rcsLabel.setVisible(true);
			rcsCapacity.setVisible(true);
			rcsCapacity.setValue((int)(100*vehicle.getRcsFuelTank().getAmount()/vehicle.getRcsFuelTank().getMaxAmount()));
			rcsCapacity.setString(fuelFormat.format(vehicle.getRcsFuelTank().getAmount()) 
					+ " / " + fuelFormat.format(vehicle.getRcsFuelTank().getMaxAmount())
					+ " " + vehicle.getRcsFuelTank().getResource().getUnits());
		} else {
			rcsLabel.setVisible(false);
			rcsCapacity.setVisible(false);
		}
		fuelLabel.setVisible(false);
		fuelCapacity.setVisible(false);
	}
	
	/**
	 * Update capacities.
	 *
	 * @param vehicle the vehicle
	 * @return true, if successful
	 */
	public void updateFuel(SurfaceVehicle vehicle) {
		fuelLabel.setVisible(true);
		fuelCapacity.setVisible(true);
		fuelCapacity.setValue((int)(100*vehicle.getFuelTank().getAmount()/vehicle.getFuelTank().getMaxAmount()));
		fuelCapacity.setString(fuelFormat.format(vehicle.getFuelTank().getAmount()) 
				+ " / " + fuelFormat.format(vehicle.getFuelTank().getMaxAmount())
				+ " " + vehicle.getFuelTank().getResource().getUnits());
		omsLabel.setVisible(false);
		omsCapacity.setVisible(false);
		rcsLabel.setVisible(false);
		rcsCapacity.setVisible(false);
	}
}
