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
package edu.mit.spacenet.simulator.event;

import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * An event that moves a set of elements to a new container.
 * 
 * @author Paul Grogan
 */
public class MoveEvent extends AbstractEvent {
	private SortedSet<I_Element> elements;
	private I_Container container;
	
	/**
	 * Instantiates a new move event.
	 */
	public MoveEvent() {
		super();
		this.elements = new TreeSet<I_Element>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(container == null) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
					"No container defined, defaulting to " + getLocation() + "."));
			container = getLocation();
		}
		if(container instanceof I_Carrier 
				&& ((I_Carrier)container).getLocation()==null) {
			throw new SimSpatialError(simulator.getTime(), this, 
					container + " was not found.");
		}
		
		if(elements.size() == 0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
				"No elements defined."));
		} else {
			System.out.printf("%.3f: %s\n", 
					getTime(), "Transferring " + elements + " to " + container);
			for(I_Element element : elements) {
				if(element.getLocation()==null) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " was not found.");
				} else if(!getLocation().equals(element.getLocation())) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " is located at " + element.getLocation() + " instead of " + getLocation() + ".");
				} else if(container.add(element)) {
					// successful for element 'element'
				} else {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " could not be added to " + container + ".");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		System.out.println("Move Event for " + elements.toString() + " -> " + container.getName());
	}
	
	/**
	 * Gets the elements.
	 * 
	 * @return the elements
	 */
	public SortedSet<I_Element> getElements() {
		return elements;
	}
	
	/**
	 * Sets the elements.
	 * 
	 * @param elements the new elements
	 */
	public void setElements(SortedSet<I_Element> elements) {
		this.elements = elements;
	}
	
	/**
	 * Gets the container.
	 * 
	 * @return the container
	 */
	public I_Container getContainer() {
		return container;
	}
	
	/**
	 * Sets the container.
	 * 
	 * @param container the new container
	 */
	public void setContainer(I_Container container) {
		this.container = container;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.MOVE;
	}
}
