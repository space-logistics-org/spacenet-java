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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.ResourceTank;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.renderer.ResourceListCellRenderer;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element panel used for viewing and editing resource tank-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class ResourceTankPanel extends AbstractElementPanel {
	private static final long serialVersionUID = 6335483106289763369L;
	
	private ResourceTank tank;
	
	private JComboBox resourceCombo;
	private SpinnerNumberModel amountModel, maxAmountModel;
	private JSpinner amountSpinner, maxAmountSpinner;
	private UnitsWrapper amountWrapper, maxAmountWrapper;
	
	/**
	 * Instantiates a new resource tank panel.
	 * 
	 * @param elementDialog the element dialog
	 * @param tank the tank
	 */
	public ResourceTankPanel(ElementDialog elementDialog, ResourceTank tank) {
		super(elementDialog, tank);
		this.tank = tank;
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
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Resource: "), c);
		c.gridy++;
		add(new JLabel("Max Amount: "), c);
		c.gridx+=2;
		add(new JLabel("Amount: "), c);
		
		c.gridy = 0;
		c.gridx = 1;
		c.gridwidth = 3;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_START;
		resourceCombo = new JComboBox();
		for(I_Resource resource : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary()) {
			resourceCombo.addItem(resource);
		}
		for(ClassOfSupply cos : ClassOfSupply.values()) {
			resourceCombo.addItem(new GenericResource(cos));
		}
		resourceCombo.setRenderer(new ResourceListCellRenderer());
		resourceCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& resourceCombo.getSelectedItem() != null) {
					String resourceUnits = ((I_Resource)resourceCombo.getSelectedItem()).getUnits();
					maxAmountWrapper.setUnits(resourceUnits);
					amountWrapper.setUnits(resourceUnits);
				}
			}
		});
		add(resourceCombo, c);
		
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		maxAmountModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
		maxAmountSpinner = new JSpinner(maxAmountModel);
		maxAmountSpinner.setPreferredSize(new Dimension(150,20));
		maxAmountWrapper = new UnitsWrapper(maxAmountSpinner, "units");
		add(maxAmountWrapper, c);
		maxAmountSpinner.setToolTipText("Maximum amount of resource to nest");
		
		c.gridx+=2;
		amountModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
		amountSpinner = new JSpinner(amountModel);
		amountSpinner.setPreferredSize(new Dimension(150,20));
		amountWrapper = new UnitsWrapper(amountSpinner, "units");
		add(amountWrapper, c);
		amountSpinner.setToolTipText("Initial amount of resource to nest");
	}
	
	/**
	 * Initializes the panel components for a new resource tank.
	 */
	private void initialize() {
		resourceCombo.setSelectedItem(tank.getResource());
		maxAmountModel.setValue(tank.getMaxAmount());
		amountModel.setValue(tank.getAmount());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public ResourceTank getElement() {
		return tank;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		tank.setResource((I_Resource)resourceCombo.getSelectedItem());
		tank.setMaxAmount(maxAmountModel.getNumber().doubleValue());
		tank.setAmount(amountModel.getNumber().doubleValue());
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
		return true;
	}
}
