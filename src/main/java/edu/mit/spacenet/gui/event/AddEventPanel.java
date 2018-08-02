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
package edu.mit.spacenet.gui.event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.simulator.event.AddEvent;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A panel for viewing and editing an add event.
 * 
 * @author Paul Grogan
 */
public class AddEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private AddEvent event;
	
	private ElementTree elementTree;
	private JLabel containerEnvironmentLabel;
	private JProgressBar containerMassCapacity, containerVolumeCapacity;
	private DemandTable demandsList;
	private JScrollPane demandsScroll;
	private JButton addDemandButton, removeDemandButton;
	
	/**
	 * Instantiates a new adds the event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public AddEventPanel(EventDialog eventDialog, AddEvent event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Element: "), c);
		c.gridy++;
		add(new JLabel("Environment: "), c);
		c.gridy++;
		add(new JLabel("Cargo Mass: "), c);
		c.gridy++;
		add(new JLabel("Cargo Volume: "), c);
		c.gridy++;
		add(new JLabel("Resources: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = .5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		elementTree = new ElementTree();
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,100));
		add(elementScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		containerEnvironmentLabel = new JLabel();
		add(containerEnvironmentLabel, c);
		c.gridy++;
		containerMassCapacity = new JProgressBar(0,100);
		containerMassCapacity.setStringPainted(true);
		containerMassCapacity.setString("");
		add(containerMassCapacity, c);
		c.gridy++;
		containerVolumeCapacity = new JProgressBar(0,100);
		containerVolumeCapacity.setStringPainted(true);
		containerVolumeCapacity.setString("");
		add(containerVolumeCapacity, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		demandsList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		demandsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		demandsScroll = new JScrollPane(demandsList);
		demandsScroll.setPreferredSize(new Dimension(400,50));
		demandsScroll.setBackground(Color.WHITE);
		demandsScroll.getViewport().setBackground(Color.WHITE);
		add(demandsScroll, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		JPanel demandButtonPanel = new JPanel();
		demandButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		addDemandButton = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
		addDemandButton.setMargin(new Insets(3,3,3,3));
		addDemandButton.setToolTipText("Add Resource");
		demandButtonPanel.add(addDemandButton);
		removeDemandButton = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
		removeDemandButton.setMargin(new Insets(3,3,3,3));
		removeDemandButton.setToolTipText("Remove Resource");
		removeDemandButton.setEnabled(false);
		demandButtonPanel.add(removeDemandButton);
		add(demandButtonPanel, c);
	}
	private void initialize() {
		elementTree.setRoot(getEventDialog().getSimNode());
		if(event.getContainer()!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(event.getContainer().getUid()));
		}
		elementTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				updateCapacities();
			}
		});
		for(Demand demand : event.getDemands()) {
			demandsList.addDemand(demand);
		}
		demandsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(demandsList.getSelectedRows().length >= 1) {
					removeDemandButton.setEnabled(true);
				} else {
					removeDemandButton.setEnabled(false);
				}
			}
		});
		demandsList.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				updateCapacities();
			}
		});
		addDemandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demandsList.addDemand(new Demand());
			}
		});
		removeDemandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Demand> demands = new ArrayList<Demand>();
				for(int row : demandsList.getSelectedRows()) {
					demands.add(demandsList.getDemand(row));
				}
				for(Demand demand : demands) {
					demandsList.remove(demand);
				}
				updateView();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public AddEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setContainer((I_ResourceContainer)getEventDialog().getElement((elementTree.getElementSelection().getUid())));
		event.getDemands().clear();
		for(Demand demand: demandsList.getDemands()) {
			if(demand.getResource()!=null)
				event.getDemands().add(demand);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		I_Element selectedItem = elementTree.getElementSelection();
		elementTree.setRoot(getEventDialog().getSimNode());
		if(selectedItem!=null)
			elementTree.setSelection(getEventDialog().getSimElement(selectedItem.getUid()));
		updateCapacities();
	}
	private void updateCapacities() {
		if(elementTree.getSelection() instanceof I_ResourceContainer) {
			I_ResourceContainer container = (I_ResourceContainer)elementTree.getSelection();
			double cargoMass = container.getCargoMass();
			double cargoVolume = container.getCargoVolume();
			
			for(Demand demand : demandsList.getDemands()) {
				cargoMass += demand.getMass();
				cargoVolume += demand.getVolume();
			}
			
			DecimalFormat massFormat = new DecimalFormat("0.00");
			DecimalFormat volumeFormat = new DecimalFormat("0.000");
			containerEnvironmentLabel.setText(container.getCargoEnvironment().toString());
			
			boolean hasErrors = false;
			
			if(cargoMass > container.getMaxCargoMass()) {
				containerMassCapacity.setForeground(new Color(153, 0, 0));
				hasErrors = true;
			}
			else {
				containerMassCapacity.setForeground(new Color(0, 153, 0));
			}
			if(container.getMaxCargoMass()==0) containerMassCapacity.setValue(100);
			else containerMassCapacity.setValue((int)(100*cargoMass/container.getMaxCargoMass()));
			containerMassCapacity.setString(massFormat.format(cargoMass) + " / " + massFormat.format(container.getMaxCargoMass()) + " kg");
			
			if(cargoVolume > container.getMaxCargoVolume()) {
				containerVolumeCapacity.setForeground(new Color(153, 0, 0));
				hasErrors = hasErrors||GlobalParameters.isVolumeConstrained();
			}
			else {
				containerVolumeCapacity.setForeground(new Color(0, 153, 0));
			}
			if(container.getMaxCargoVolume()==0) containerVolumeCapacity.setValue(100);
			else containerVolumeCapacity.setValue((int)(100*cargoVolume/container.getMaxCargoVolume()));
			containerVolumeCapacity.setString(volumeFormat.format(cargoVolume) + " / " + volumeFormat.format(container.getMaxCargoVolume()) + " m^3");
			
			getEventDialog().setOkButtonEnabled(!hasErrors);
		} else {
			containerEnvironmentLabel.setText(null);
			containerMassCapacity.setValue(0);
			containerMassCapacity.setString("");
			containerVolumeCapacity.setValue(0);
			containerVolumeCapacity.setString("");
			getEventDialog().setOkButtonEnabled(false);
		}
	}
}
