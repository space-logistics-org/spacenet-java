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
	
	private JComboBox<I_Resource> resourceCombo;
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
		resourceCombo = new JComboBox<I_Resource>();
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
