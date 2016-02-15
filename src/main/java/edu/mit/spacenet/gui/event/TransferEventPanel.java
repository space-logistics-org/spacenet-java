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
package edu.mit.spacenet.gui.event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.ResourceTank;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.gui.component.CapacityPanel;
import edu.mit.spacenet.simulator.event.TransferEvent;

/**
 * The Class TransferEventPanel.
 * 
 * @author Paul Grogan
 */
public class TransferEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private TransferEvent event;
	
	private JComboBox ddlDestinationContainer, ddlOriginContainer;
	private CapacityPanel capacityPanel;
	private TransferDemandsTable demandsTable;
	
	/**
	 * Instantiates a new transfer event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public TransferEventPanel(EventDialog eventDialog, TransferEvent event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Transfer from: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlOriginContainer = new JComboBox();
		add(ddlOriginContainer, c);
		c.gridy++;
		c.gridwidth = 1;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Transfer to: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlDestinationContainer = new JComboBox();
		add(ddlDestinationContainer, c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		capacityPanel = new CapacityPanel();
		add(capacityPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("Contents: "), c);
		c.gridy++;
		c.weighty = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		demandsTable = new TransferDemandsTable();
		JScrollPane demandsScroll = new JScrollPane(demandsTable);
		demandsScroll.setPreferredSize(new Dimension(250,50));
		demandsScroll.setBackground(Color.WHITE);
		demandsScroll.getViewport().setBackground(Color.WHITE);
		add(demandsScroll, c);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		ddlOriginContainer.addItem(event.getOriginContainer());
		ddlOriginContainer.setSelectedItem(event.getOriginContainer());
		ddlOriginContainer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					demandsTable.setData((I_ResourceContainer)ddlOriginContainer.getSelectedItem(), new DemandSet());
					updateView();
				}
			}
		});
		ddlDestinationContainer.addItem(event.getDestinationContainer());
		ddlDestinationContainer.setSelectedItem(event.getDestinationContainer());
		ddlDestinationContainer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateCapacities();
			}
		});
		if(event.getOriginContainer()!=null 
				&& event.getOriginContainer() instanceof ResourceTank 
				&& event.getOriginContainer().getContainer() instanceof PropulsiveVehicle) {
			demandsTable.setData(((PropulsiveVehicle)getEventDialog().getSimElement(((PropulsiveVehicle)event.getOriginContainer().getContainer()).getUid())).getOmsFuelTank(), event.getTransferDemands());
		} else if(event.getOriginContainer()!=null 
				&& event.getOriginContainer() instanceof ResourceTank 
				&& event.getOriginContainer().getContainer() instanceof SurfaceVehicle) {
			demandsTable.setData(((SurfaceVehicle)getEventDialog().getSimElement(((SurfaceVehicle)event.getOriginContainer().getContainer()).getUid())).getFuelTank(), event.getTransferDemands());
		} else if(event.getOriginContainer()!=null) {
			demandsTable.setData((I_ResourceContainer)getEventDialog().getSimElement(event.getOriginContainer().getUid()), event.getTransferDemands());
		}
		demandsTable.repaint();
		
		demandsTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				updateCapacities();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public TransferEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		if(demandsTable.isEditing()) demandsTable.getCellEditor().stopCellEditing();
		if(ddlDestinationContainer.getSelectedItem() instanceof ResourceTank 
				&& ((ResourceTank)ddlDestinationContainer.getSelectedItem()).getContainer() instanceof PropulsiveVehicle) {
			PropulsiveVehicle vehicle = (PropulsiveVehicle)((ResourceTank)ddlDestinationContainer.getSelectedItem()).getContainer();
			event.setDestinationContainer(((PropulsiveVehicle)getEventDialog().getElement(vehicle.getUid())).getOmsFuelTank());
		} else if(ddlDestinationContainer.getSelectedItem() instanceof ResourceTank &&
				((ResourceTank)ddlDestinationContainer.getSelectedItem()).getContainer() instanceof SurfaceVehicle) {
			SurfaceVehicle vehicle = (SurfaceVehicle)((ResourceTank)ddlDestinationContainer.getSelectedItem()).getContainer();
			event.setDestinationContainer(((SurfaceVehicle)getEventDialog().getElement(vehicle.getUid())).getFuelTank());
		} else if(ddlDestinationContainer.getSelectedItem() instanceof I_ResourceContainer) {
			I_ResourceContainer container = (I_ResourceContainer)ddlDestinationContainer.getSelectedItem();
			event.setDestinationContainer((I_ResourceContainer)getEventDialog().getElement(container.getUid()));
		}
		if(ddlOriginContainer.getSelectedItem() instanceof ResourceTank 
				&& ((ResourceTank)ddlOriginContainer.getSelectedItem()).getContainer() instanceof PropulsiveVehicle) {
			PropulsiveVehicle vehicle = (PropulsiveVehicle)((ResourceTank)ddlOriginContainer.getSelectedItem()).getContainer();
			event.setOriginContainer(((PropulsiveVehicle)getEventDialog().getElement(vehicle.getUid())).getOmsFuelTank());
			event.setTransferDemands(demandsTable.getTransferDemands());
		} else if(ddlOriginContainer.getSelectedItem() instanceof ResourceTank 
				&& ((ResourceTank)ddlOriginContainer.getSelectedItem()).getContainer() instanceof SurfaceVehicle) {
			SurfaceVehicle vehicle = (SurfaceVehicle)((ResourceTank)ddlOriginContainer.getSelectedItem()).getContainer();
			event.setOriginContainer(((SurfaceVehicle)getEventDialog().getElement(vehicle.getUid())).getFuelTank());
			event.setTransferDemands(demandsTable.getTransferDemands());
		} else if(ddlOriginContainer.getSelectedItem() instanceof I_ResourceContainer) {
			I_ResourceContainer container = (I_ResourceContainer)ddlOriginContainer.getSelectedItem();
			event.setOriginContainer((I_ResourceContainer)getEventDialog().getElement(container.getUid()));
			event.setTransferDemands(demandsTable.getTransferDemands());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		I_ResourceContainer destinationContainer = (I_ResourceContainer)ddlDestinationContainer.getSelectedItem();
		ItemListener destinationListener = ddlDestinationContainer.getItemListeners()[0];
		ddlDestinationContainer.removeItemListener(destinationListener);
		ddlDestinationContainer.removeAllItems();

		I_ResourceContainer originContainer = (I_ResourceContainer)ddlOriginContainer.getSelectedItem();
		ItemListener originListener = ddlOriginContainer.getItemListeners()[0];
		ddlOriginContainer.removeItemListener(originListener);
		ddlOriginContainer.removeAllItems();
		
		DemandSet demands = demandsTable.getTransferDemands();
		
		for(I_Element i : getEventDialog().getSimNode().getCompleteContents()) {
			if(i instanceof I_ResourceContainer) {
				ddlDestinationContainer.addItem(i);
				ddlOriginContainer.addItem(i);
			} else if(i instanceof PropulsiveVehicle) {
				PropulsiveVehicle v = (PropulsiveVehicle)i;
				if(v.getOmsFuelTank()!=null) {
					ddlDestinationContainer.addItem(v.getOmsFuelTank());
					ddlOriginContainer.addItem(v.getOmsFuelTank());
				}
			} else if(i instanceof SurfaceVehicle) {
				SurfaceVehicle v = (SurfaceVehicle)i;
				if(v.getFuelTank()!=null) {
					ddlDestinationContainer.addItem(v.getFuelTank());
					ddlOriginContainer.addItem(v.getFuelTank());
				}
			}
		}
		if(destinationContainer!=null 
				&& destinationContainer instanceof ResourceTank
				&& destinationContainer.getContainer() instanceof PropulsiveVehicle) {
			ddlDestinationContainer.setSelectedItem(((PropulsiveVehicle)getEventDialog().getSimElement(((PropulsiveVehicle)destinationContainer.getContainer()).getUid())).getOmsFuelTank());
		} else if(destinationContainer!=null 
				&& destinationContainer instanceof ResourceTank
				&& destinationContainer.getContainer() instanceof SurfaceVehicle) {
			ddlDestinationContainer.setSelectedItem(((SurfaceVehicle)getEventDialog().getSimElement(((SurfaceVehicle)destinationContainer.getContainer()).getUid())).getFuelTank());
		} else if(destinationContainer!=null 
				&& getEventDialog().getSimElement(destinationContainer.getUid()) != null) {
			ddlDestinationContainer.setSelectedItem(getEventDialog().getSimElement(destinationContainer.getUid()));
		}
		ddlDestinationContainer.addItemListener(destinationListener);
		if(originContainer!=null 
				&& originContainer instanceof ResourceTank
				&& originContainer.getContainer() instanceof PropulsiveVehicle) {
			ddlOriginContainer.setSelectedItem(((PropulsiveVehicle)getEventDialog().getSimElement(((PropulsiveVehicle)originContainer.getContainer()).getUid())).getOmsFuelTank());
			demandsTable.setData(((PropulsiveVehicle)getEventDialog().getSimElement(((PropulsiveVehicle)originContainer.getContainer()).getUid())).getOmsFuelTank(), demands);
		} else if(originContainer!=null 
				&& originContainer instanceof ResourceTank
				&& originContainer.getContainer() instanceof SurfaceVehicle) {
			ddlDestinationContainer.setSelectedItem(((SurfaceVehicle)getEventDialog().getSimElement(((SurfaceVehicle)originContainer.getContainer()).getUid())).getFuelTank());
			demandsTable.setData(((SurfaceVehicle)getEventDialog().getSimElement(((SurfaceVehicle)originContainer.getContainer()).getUid())).getFuelTank(), demands);
		} else if(originContainer!=null 
				&& getEventDialog().getSimElement(originContainer.getUid())!=null) {
			ddlOriginContainer.setSelectedItem(getEventDialog().getSimElement(originContainer.getUid()));
			demandsTable.setData((I_ResourceContainer)getEventDialog().getSimElement(originContainer.getUid()), demands);
		}
		ddlOriginContainer.addItemListener(originListener);
				
		updateCapacities();
	}
	
	/**
	 * Update capacities.
	 */
	private void updateCapacities() {
		double mass = 0;
		double volume = 0;
		boolean hasErrors = false;
		if(ddlDestinationContainer.getSelectedItem() instanceof I_ResourceContainer) {
			I_ResourceContainer v = (I_ResourceContainer)ddlDestinationContainer.getSelectedItem();
			mass += v.getCargoMass();
			volume += v.getCargoVolume();
			if(!ddlDestinationContainer.getSelectedItem().equals(ddlOriginContainer.getSelectedItem())) {
				for(Demand demand : demandsTable.getTransferDemands()) {
					if(v.canAdd(demand.getResource(), demand.getAmount())) {
						mass += demand.getMass();
						volume += demand.getVolume();
					} else {
						mass += demand.getMass();
						volume += demand.getVolume();
						hasErrors = true;
					}
				}
			}
			hasErrors = capacityPanel.updateCapacities(v, mass, volume) || hasErrors;
		}
	}
}
