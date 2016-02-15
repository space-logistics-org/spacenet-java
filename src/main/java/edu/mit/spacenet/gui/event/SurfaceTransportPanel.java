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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A panel for viewing and editing a surface transport event.
 * 
 * @author Paul Grogan
 */
public class SurfaceTransportPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private SurfaceTransport event;
	
	private JLabel lblDestination, lblDistance, lblDuration;
	private JComboBox ddlEdge, ddlState;
	private SpinnerNumberModel speedModel, dutyCycleModel;
	private JSpinner speedSpinner, dutyCycleSpinner;
	
	private ElementTree elementTree;
	
	/**
	 * Instantiates a new surface transport panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public SurfaceTransportPanel(EventDialog eventDialog, SurfaceTransport event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Edge: "), c);
		c.gridy+=2;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Vehicle: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Transport State: "), c);
		c.gridy++;
		add(new JLabel("Speed: "), c);
		c.gridy++;
		add(new JLabel("Duty Cycle: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlEdge = new ContainerComboBox();
		add(ddlEdge, c);
		c.gridy++;
		JPanel edgePanel = new JPanel();
		edgePanel.setLayout(new BoxLayout(edgePanel, BoxLayout.PAGE_AXIS));
		lblDestination = new JLabel();
		edgePanel.add(lblDestination);
		lblDistance = new JLabel();
		edgePanel.add(lblDistance);
		lblDuration = new JLabel();
		edgePanel.add(lblDuration);
		add(edgePanel, c);
		c.gridy++;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		elementTree = new ElementTree();
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,50));
		add(elementScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlState = new JComboBox();
		ddlState.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1271331296677711150L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof I_State)
					label.setIcon((((I_State)value).getStateType()).getIcon());
				return label;
			}
		});
		ddlState.setEnabled(false);
		add(ddlState, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		speedModel = new SpinnerNumberModel(0.0,0.0,100.0,1.0);
		speedSpinner = new JSpinner(speedModel);
		speedSpinner.setPreferredSize(new Dimension(75,20));
		speedSpinner.setEnabled(false);
		add(new UnitsWrapper(speedSpinner, "km / hr"), c);
		c.gridy++;
		dutyCycleModel = new SpinnerNumberModel(1.0,0.0,1.0,0.05);
		dutyCycleSpinner = new JSpinner(dutyCycleModel);
		dutyCycleSpinner.setEnabled(false);
		dutyCycleSpinner.setPreferredSize(new Dimension(75,20));
		add(dutyCycleSpinner, c);
	}
	private void initialize() {
		if(event.getEdge()!=null) {
			ddlEdge.addItem(event.getEdge());
			ddlEdge.setSelectedItem(event.getEdge());
		}
		
		elementTree.setRoot(getEventDialog().getSimNode());
		if(event.getVehicle()!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(event.getVehicle().getUid()));
			for(I_State simState : getEventDialog().getSimElement(event.getVehicle().getUid()).getStates()) {
				if(simState.equals(event.getTransportState())) {
					ddlState.addItem(simState);
					ddlState.setSelectedItem(simState);
				}
			}
		}
		
		speedModel.setValue(event.getSpeed());
		dutyCycleModel.setValue(event.getDutyCycle());
		speedSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDuration();
			}
		});
		dutyCycleSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDuration();
			}
		});
		ddlEdge.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateView();
			}
		});
		elementTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if(elementTree.getSelectionCount() > 0
						&& elementTree.getSelection() instanceof SurfaceVehicle) {
					speedSpinner.setValue(((SurfaceVehicle)elementTree.getSelection()).getMaxSpeed());
				}
				updateView();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public SurfaceTransport getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setEdge((SurfaceEdge)ddlEdge.getSelectedItem());
		
		event.setVehicle((SurfaceVehicle)getEventDialog().getElement(elementTree.getElementSelection().getUid()));
		
		if(ddlState.getSelectedItem() == null) {
			event.setTransportState(null);
		} else {
			for(I_State s : getEventDialog().getElement(elementTree.getElementSelection().getUid()).getStates()) {
				if(s.equals((I_State)ddlState.getSelectedItem())) {
					event.setTransportState(s);
					break;
				}
			}
		}
		event.setSpeed(speedModel.getNumber().doubleValue());
		event.setDutyCycle(dutyCycleModel.getNumber().doubleValue());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		SurfaceEdge edge = (SurfaceEdge)ddlEdge.getSelectedItem();
		ItemListener edgeListener = ddlEdge.getItemListeners()[0];
		ddlEdge.removeItemListener(edgeListener);
		ddlEdge.removeAllItems();
		for(Edge e : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getNetwork().getEdges()) {
			if(e instanceof SurfaceEdge && 
					(e.getOrigin().equals(getEventDialog().getNode()) 
							|| e.getDestination().equals(getEventDialog().getNode()))) {
				ddlEdge.addItem(e);
				if(e.equals(edge)) {
					ddlEdge.setSelectedItem(e);
				}
			}
		}
		if(ddlEdge.getSelectedItem()!=null) {
			lblDestination.setText("Destination: " + 
					((SurfaceEdge)ddlEdge.getSelectedItem()).getDestination());
			lblDistance.setText("Distance: " + 
					((SurfaceEdge)ddlEdge.getSelectedItem()).getDistance() + " kilometers");
		}
		ddlEdge.addItemListener(edgeListener);
		
		I_Element selectedItem = elementTree.getElementSelection();
		TreeSelectionListener treeListener = elementTree.getTreeSelectionListeners()[0];
		elementTree.removeTreeSelectionListener(treeListener);
		elementTree.setRoot(getEventDialog().getSimNode());
		elementTree.setSelection(selectedItem);
		elementTree.addTreeSelectionListener(treeListener);
		
		I_State state = (I_State)ddlState.getSelectedItem();
		
		ddlState.removeAllItems();
		if(elementTree.getSelectionCount() > 0) {
			for(I_State s : elementTree.getElementSelection().getStates()) {
				ddlState.addItem(s);
				
				if(s.equals(state)) {
					ddlState.setSelectedItem(s);
				}
			}
		}
		if(elementTree.getSelection() instanceof SurfaceVehicle) {
			getEventDialog().setOkButtonEnabled(true);
			ddlState.setEnabled(true);
			
			speedModel.setMaximum(((SurfaceVehicle)elementTree.getSelection()).getMaxSpeed());
			if(speedModel.getNumber().doubleValue() > ((SurfaceVehicle)elementTree.getSelection()).getMaxSpeed()) { 
				// TODO: should probably have a warning message
				speedModel.setValue(speedModel.getMaximum());
			}
			speedSpinner.setEnabled(true);
			dutyCycleSpinner.setEnabled(true);
		} else {
			speedModel.setValue(0);
			speedSpinner.setEnabled(false);
			dutyCycleSpinner.setEnabled(false);
			getEventDialog().setOkButtonEnabled(false);
			ddlState.setEnabled(false);
		}
		
		updateDuration();
	}
	private void updateDuration() {
		if(speedModel.getNumber().doubleValue() == 0 || dutyCycleModel.getNumber().doubleValue() == 0) {
			lblDuration.setText("Duration: n/a");
		} else {
			double duration = ((SurfaceEdge)ddlEdge.getSelectedItem()).getDistance()/(speedModel.getNumber().doubleValue()*dutyCycleModel.getNumber().doubleValue()*24);
			DecimalFormat format = new DecimalFormat("0.00");
			lblDuration.setText("Duration: " + format.format(GlobalParameters.getRoundedTime(duration)) + " days");
		}
	}
}
