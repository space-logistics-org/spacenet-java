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

import javax.swing.JPanel;

import edu.mit.spacenet.simulator.event.I_Event;

/**
 * The Class AbstractEventPanel.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractEventPanel extends JPanel {
	private static final long serialVersionUID = 1383454658585330190L;
	
	private EventDialog eventDialog;
	
	/**
	 * Instantiates a new abstract event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public AbstractEventPanel(EventDialog eventDialog, I_Event event) {
		this.eventDialog = eventDialog;
	}
	
	/**
	 * Saves the event.
	 */
	public abstract void saveEvent();
	
	/**
	 * Updates the view.
	 */
	public abstract void updateView();
	
	/**
	 * Gets the event dialog.
	 * 
	 * @return the event dialog
	 */
	public EventDialog getEventDialog() {
		return eventDialog;
	}
	
	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public abstract I_Event getEvent();
}
