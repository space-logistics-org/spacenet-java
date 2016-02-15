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

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Base class for all events.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractEvent implements I_Event {
	private long uid;
	private String name;
	private double time;
	private int priority;
	private Location location;
	private I_Event parent;
	
	/**
	 * The default constructor sets the priority to a default value in the
	 * middle of the priority levels and sets a generic name.
	 */
	public AbstractEvent() {
		uid = (long)(Long.MAX_VALUE*Math.random());
		priority = 0;
		name = getEventType()==null?"":getEventType().getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getLocation()
	 */
	public Location getLocation() {
		return location;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#setLocation(edu.mit.spacenet.domain.network.Location)
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getTime()
	 */
	public double getTime() {
		return  GlobalParameters.getRoundedTime(time);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#setTime(double)
	 */
	public void setTime(double time) {
		this.time = time;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getPriority()
	 */
	public int getPriority() {
		return priority;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#setPriority(int)
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getParent()
	 */
	public I_Event getParent() {
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getRoot()
	 */
	public I_Event getRoot() {
		if(getParent()==null) return this;
		else return getParent().getRoot();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#setParent(edu.mit.spacenet.domain.event.I_Event)
	 */
	public void setParent(I_Event parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#compareTo(edu.mit.spacenet.domain.event.I_Event)
	 */
	public int compareTo(I_Event e) {
		if(getTime() < e.getTime()) {
			return -1;
		} else if(getTime() > e.getTime()) {
			return 1;
		} else if(getPriority() > e.getPriority()) {
			return 1;
		} else if(getPriority() < e.getPriority()) {
			return -1;
		} else {
			return getName().compareTo(e.getName());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#print()
	 */
	public void print() {
		print(0);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#print(int)
	 */
	public void print(int tabOrder) {
		String s = "";
		for(int i=0; i<tabOrder; i++) s += "  ";
		System.out.printf("%s- %.2f: ", s, getTime());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getUid()
	 */
	public long getUid() {
		return uid;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#resetUid()
	 */
	public void resetUid() {
		uid = (long)(Long.MAX_VALUE*Math.random());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if(object instanceof I_Event) {
			return getUid()==((I_Event)object).getUid();
		} else return false;
	}
}
