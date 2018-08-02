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
package edu.mit.spacenet.gui.mission;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import edu.mit.spacenet.gui.component.DropDownButton;
import edu.mit.spacenet.gui.event.EventDialog;
import edu.mit.spacenet.simulator.event.EventFactory;
import edu.mit.spacenet.simulator.event.EventType;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.I_Process;
import edu.mit.spacenet.simulator.event.I_Transport;

/**
 * A custom drop down button that contains the menu items for adding all of the
 * different kinds of events to a mission.
 */
public class AddEventButton extends DropDownButton {
	private static final long serialVersionUID = 8336153141415104568L;
	private MissionPanel missionPanel;
	
	/**
	 * Instantiates a new adds the event button.
	 * 
	 * @param missionPanel the mission panel
	 */
	public AddEventButton(MissionPanel missionPanel) {
		super("Add Event", new ImageIcon(missionPanel.getClass().getClassLoader().getResource("icons/cog_add.png")));
		this.missionPanel = missionPanel;
		getButton().setMargin(new Insets(3,3,3,3));
		setToolTipText("Add Event");
		addMenuItems();
	}
	
	/**
	 * Adds the event menu items.
	 */
	private void addMenuItems() {
		JMenuItem addCreateEvent = new JMenuItem(EventType.CREATE.getName(), EventType.CREATE.getIcon());
		addCreateEvent.setToolTipText("Instantiate elements at a node or nested within a carrier element");
		addCreateEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.CREATE);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addCreateEvent);
		JMenuItem addAddEvent = new JMenuItem(EventType.ADD.getName(), EventType.ADD.getIcon());
		addAddEvent.setToolTipText("Add resources to a container element");
		addAddEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.ADD);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addAddEvent);
		JMenuItem addMoveEvent = new JMenuItem(EventType.MOVE.getName(), EventType.MOVE.getIcon());
		addMoveEvent.setToolTipText("Move elements to a node or to a carrier element");
		addMoveEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.MOVE);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addMoveEvent);
		JMenuItem addTransferEvent = new JMenuItem(EventType.TRANSFER.getName(), EventType.TRANSFER.getIcon());
		addTransferEvent.setToolTipText("Transfer resources from one container to another");
		addTransferEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.TRANSFER);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addTransferEvent);
		JMenuItem addRemoveEvent = new JMenuItem(EventType.REMOVE.getName(), EventType.REMOVE.getIcon());
		addRemoveEvent.setToolTipText("Remove elements from the simulation.");
		addRemoveEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.REMOVE);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addRemoveEvent);
		JMenuItem addReconfigureEvent = new JMenuItem(EventType.RECONFIGURE.getName(), EventType.RECONFIGURE.getIcon());
		addReconfigureEvent.setToolTipText("Reconfigure an element to a new state");
		addReconfigureEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.RECONFIGURE);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addReconfigureEvent);
		JMenuItem addReconfigureGroupEvent = new JMenuItem(EventType.RECONFIGURE_GROUP.getName(), EventType.RECONFIGURE_GROUP.getIcon());
		addReconfigureGroupEvent.setToolTipText("Reconfigure a set of elements to a new state type");
		addReconfigureGroupEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.RECONFIGURE_GROUP);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addReconfigureGroupEvent);
		JMenuItem addDemandEvent = new JMenuItem(EventType.DEMAND.getName(), EventType.DEMAND.getIcon());
		addDemandEvent.setToolTipText("Demand for resources by an element");
		addDemandEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.DEMAND);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addDemandEvent);
		JMenuItem addBurnEvent = new JMenuItem(EventType.BURN.getName(), EventType.BURN.getIcon());
		addBurnEvent.setToolTipText("Impulsive OMS or RCS burn");
		addBurnEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.BURN);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addBurnEvent);
		JMenuItem addEvaEvent = new JMenuItem(EventType.EVA.getName(), EventType.EVA.getIcon());
		addEvaEvent.setToolTipText("Extra-vehicular activity");
		addEvaEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.EVA);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addEvaEvent);
		JMenuItem addSpaceTransport = new JMenuItem(EventType.SPACE_TRANSPORT.getName(), EventType.SPACE_TRANSPORT.getIcon());
		addSpaceTransport.setToolTipText("Transports a stack from node to node along a trajectory of impulsive burns");
		addSpaceTransport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.SPACE_TRANSPORT);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addSpaceTransport);
		JMenuItem addSurfaceTransport = new JMenuItem(EventType.SURFACE_TRANSPORT.getName(), EventType.SURFACE_TRANSPORT.getIcon());
		addSurfaceTransport.setToolTipText("Transports a surface vehicle from surface node to surface node");
		addSurfaceTransport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.SURFACE_TRANSPORT);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addSurfaceTransport);
		JMenuItem addFlightProcess = new JMenuItem(EventType.FLIGHT_TRANSPORT.getName(), EventType.FLIGHT_TRANSPORT.getIcon());
		addFlightProcess.setToolTipText("Transports a stack from node to node under pre-specified flight constraints");
		addFlightProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.FLIGHT_TRANSPORT);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addFlightProcess);
		JMenuItem addExplorationProcess = new JMenuItem(EventType.EXPLORATION.getName(), EventType.EXPLORATION.getIcon());
		addExplorationProcess.setToolTipText("Schedules EVAs at a specified frequency");
		addExplorationProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				I_Event event = EventFactory.createEvent(EventType.EXPLORATION);
				prefill(event);
				EventDialog.createAndShowGUI(missionPanel, event);
			}
		});
		getMenu().add(addExplorationProcess);
	}
	
	/**
	 * Prefills the events based on the currently-selected event. If another 
	 * event is selected, the new event is assigned the same time, one higher
	 * priority, and same location. If another process is selected, the new
	 * event is assigned the time after duration, priority 1, and same location.
	 * If a transport is selected, the new event is assigned the time after
	 * duration, priority 1, and destination location. The new event is also
	 * prefixed with up to 5 characters of the name of the mission.
	 * 
	 * @param event the event
	 */
	private void prefill(I_Event event) {
		if(missionPanel.getEventsTable().getRowCount() > 0) {
			I_Event prevEvent;
			if(missionPanel.getEventsTable().getSelectedRowCount()>0
					&& missionPanel.getEventsTable().getSelectedRow()<missionPanel.getEventsTable().getRowCount()) {
				prevEvent = missionPanel.getEventsTable().getEvent(missionPanel.getEventsTable().getSelectedRow());
			} else {
				prevEvent = missionPanel.getEventsTable().getEvent(missionPanel.getEventsTable().getRowCount()-1);
			}
			if(prevEvent instanceof I_Transport) {
				event.setTime(prevEvent.getTime() + ((I_Transport)prevEvent).getDuration());
				event.setPriority(1);
				event.setLocation(((I_Transport)prevEvent).getDestination());
			} else if(prevEvent instanceof I_Process) {
				event.setTime(prevEvent.getTime() + ((I_Process)prevEvent).getDuration());
				event.setPriority(1);
				event.setLocation(prevEvent.getLocation());
			} else {
				event.setTime(prevEvent.getTime());
				event.setPriority(Math.min(prevEvent.getPriority() + 1, 5));
				event.setLocation(prevEvent.getLocation());
			}
			event.setName(missionPanel.getMissionSplitPane().getMission().getName().substring(0,
					Math.min(missionPanel.getMissionSplitPane().getMission().getName().length(), 5)) + 
					" | " + event.getName());
		} else {
			event.setTime(0);
			event.setPriority(1);
			event.setName(missionPanel.getMissionSplitPane().getMission().getName().substring(0,
					Math.min(missionPanel.getMissionSplitPane().getMission().getName().length(), 5)) + 
					" | " + event.getName());
		}
	}
}
