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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ResourceContainer;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element panel for viewing and editing resource container-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class ResourceContainerPanel extends AbstractElementPanel {
	private static final long serialVersionUID = 6335483106289763369L;
	
	private ResourceContainer container;
	
	private SpinnerNumberModel maxCargoMassModel, maxCargoVolumeModel;
	private JSpinner maxCargoMassSpinner, maxCargoVolumeSpinner;
	private JComboBox<Environment> environmentCombo;
	private DemandTable resourcesList;
	private JButton removeResourceButton;
	
	/**
	 * Instantiates a new resource container panel.
	 * 
	 * @param elementDialog the element dialog
	 * @param container the container
	 */
	public ResourceContainerPanel(ElementDialog elementDialog, ResourceContainer container) {
		super(elementDialog, container);
		this.container = container;
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
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Max Cargo Mass: "), c);
		c.gridx+=2;
		add(new JLabel("Max Cargo Volume: "), c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Cargo Environment: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Resources: "), c);
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		maxCargoMassModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getMassPrecision());
		maxCargoMassSpinner = new JSpinner(maxCargoMassModel);
		maxCargoMassSpinner.setPreferredSize(new Dimension(75,20));
		add(new UnitsWrapper(maxCargoMassSpinner, "kg"), c);
		maxCargoMassSpinner.setToolTipText("Maximum mass of nested resources [kilograms]");
		
		c.gridx+=2;
		maxCargoVolumeModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE,GlobalParameters.getVolumePrecision());
		maxCargoVolumeSpinner = new JSpinner(maxCargoVolumeModel);
		maxCargoVolumeSpinner.setPreferredSize(new Dimension(75,20));
		add(new UnitsWrapper(maxCargoVolumeSpinner, "m^3"), c);
		maxCargoVolumeSpinner.setToolTipText("Maximum volume of nested resources [cubic meters]");
		
		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 3;
		environmentCombo = new JComboBox<Environment>();
		for(Environment e : Environment.values()) {
			environmentCombo.addItem(e);
		}
		add(environmentCombo, c);
		environmentCombo.setToolTipText("Storage environment available for nested resources");
		
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		resourcesList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		resourcesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		resourcesList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(resourcesList.getSelectedRows().length >= 1) {
					removeResourceButton.setEnabled(true);
				} else {
					removeResourceButton.setEnabled(false);
				}
			}
		});
		JScrollPane demandsScroll = new JScrollPane(resourcesList);
		demandsScroll.setPreferredSize(new Dimension(400,75));
		add(demandsScroll, c);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel demandButtonPanel = new JPanel();
		demandButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		add(demandButtonPanel, c);
		
		JButton addResourceButton = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
		addResourceButton.setToolTipText("Add a Resource");
		addResourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resourcesList.addDemand(new Demand());
			}
		});
		demandButtonPanel.add(addResourceButton);
		removeResourceButton = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
		removeResourceButton.setToolTipText("Remove Resource");
		removeResourceButton.setEnabled(false);
		removeResourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Demand> demands = new ArrayList<Demand>();
				for(int row : resourcesList.getSelectedRows()) {
					demands.add(resourcesList.getDemand(row));
				}
				for(Demand demand : demands) {
					resourcesList.remove(demand);
				}
			}
		});		
		demandButtonPanel.add(removeResourceButton);
	}
	
	/**
	 * Initializes the panel components for a new resource container.
	 */
	private void initialize() {
		maxCargoMassModel.setValue(container.getMaxCargoMass());
		maxCargoVolumeModel.setValue(container.getMaxCargoVolume());
		environmentCombo.setSelectedItem(container.getCargoEnvironment());
		for(I_Resource r : container.getContents().keySet()) {
			resourcesList.addDemand(new Demand(r, container.getContents().get(r)));
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public ResourceContainer getElement() {
		return container;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		if(resourcesList.isEditing()) resourcesList.getCellEditor().stopCellEditing();
		container.setMaxCargoMass(maxCargoMassModel.getNumber().doubleValue());
		container.setMaxCargoVolume(maxCargoVolumeModel.getNumber().doubleValue());
		container.setCargoEnvironment((Environment)environmentCombo.getSelectedItem());
		container.getContents().clear();
		for(Demand demand : resourcesList.getDemands()) {
			if(demand.getResource()!=null)
				container.add(demand.getResource(), demand.getAmount());
		}
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
