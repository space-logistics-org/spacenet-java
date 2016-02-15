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

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.SimWarning;

/**
 * An event that removes a set of elements from the simulation.
 * 
 * @author Paul Grogan
 */
public class RemoveEvent extends AbstractEvent {
	private SortedSet<I_Element> elements;
	
	/**
	 * Instantiates a new removes the event.
	 */
	public RemoveEvent() {
		super();
		this.elements = new TreeSet<I_Element>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {
		if(elements.size() == 0) {
			simulator.getWarnings().add(new SimWarning(simulator.getTime(), this, 
				"No elements defined."));
		} else {
			for(I_Element element : elements) {
				if(element.getLocation()==null) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " was not found.");
				} else if(!getLocation().equals(element.getLocation())) {
					throw new SimSpatialError(simulator.getTime(), this, 
							element + " is located at " + element.getLocation() + " instead of " + getLocation() + ".");
				} else {
					System.out.printf("%.3f: %s\n", getTime(), 
							"Removing " + elements.toString() + " from Simulation");
					element.getContainer().remove(element);
					recursiveRemove(simulator, element);
				}
			}
		}
	}
	private void recursiveRemove(I_Simulator simulator, I_Element element) {
		if(element instanceof I_Carrier) {
			for(I_Element e : ((I_Carrier)element).getContents()) {
				recursiveRemove(simulator, e);
			}
		}
		simulator.getScenario().getNetwork().getRegistrar().remove(element.getUid());
		simulator.getScenario().getNetwork().getRemovedRegistrar().put(element.getUid(),element);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		System.out.println("Remove Event for " + elements.toString());
	}
	
	/**
	 * Gets the set of elements to remove.
	 * 
	 * @return the set of elements to remove
	 */
	public SortedSet<I_Element> getElements() {
		return elements;
	}
	
	/**
	 * Sets the set of elements to remove.
	 * 
	 * @param element the element
	 */
	public void setElements(SortedSet<I_Element> element) {
		this.elements = element;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.REMOVE;
	}
}
