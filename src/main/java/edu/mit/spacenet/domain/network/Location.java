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
package edu.mit.spacenet.domain.network;

import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.DomainType;
import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Base class for any network component (e.g. node or edge).
 * 
 * @author Paul Grogan
 */
public abstract class Location extends DomainType implements I_Container, Comparable<Location> {
	private SortedSet<I_Element> contents;
	
	/**
	 * The default constructor initializes the contents structure.
	 */
	public Location() {
		contents = new TreeSet<I_Element>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getContents()
	 */
	public SortedSet<I_Element> getContents() {
		return contents;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getCompleteContents()
	 */
	public SortedSet<I_Element> getCompleteContents() {
		SortedSet<I_Element> elements = new TreeSet<I_Element>();
		for(I_Element element : contents) recursiveAdd(elements, element);
		return elements;
	}
	private void recursiveAdd(SortedSet<I_Element> elements, I_Element element) {
		elements.add(element);
		if(element instanceof I_Container) {
			for(I_Element child : ((I_Container)element).getContents()) {
				recursiveAdd(elements, child);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#canAdd(edu.mit.spacenet.domain.element.I_Element)
	 */
	public boolean canAdd(I_Element element) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#canAdd(double)
	 */
	public boolean canAdd(double addedMass) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#add(edu.mit.spacenet.domain.element.I_Element)
	 */
	public boolean add(I_Element element) {
		if(element.getContainer()!=null) element.getContainer().remove(element);
		element.setContainer(this);
		return contents.add(element);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#remove(edu.mit.spacenet.domain.element.I_Element)
	 */
	public boolean remove(I_Element element) {
		element.setContainer(null);
		return contents.remove(element);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getCargoMass()
	 */
	public double getCargoMass() {
		double mass = 0;
		for(I_Element i : contents) {
			if(!(i instanceof CrewMember)) {
				mass += i.getTotalMass();
			}
		}
		return mass;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getCargoVolume()
	 */
	public double getCargoVolume() {
		double volume = 0;
		for(I_Element e : contents) {
			if(!(e instanceof CrewMember)) {
				volume += e.getVolume();
			}
		}
		return volume;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getCrewSize()
	 */
	public int getCrewSize() {
		int crew = 0;
		for(I_Element e : contents) {
			if(e instanceof CrewMember) {
				crew++;
			}
		}
		return crew;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getTotalCrewSize()
	 */
	public int getTotalCrewSize() {
		int crew = 0;
		for(I_Element e : contents) {
			if(e instanceof CrewMember) {
				crew++;
			} else if(e instanceof I_Carrier) {
				crew+=((I_Carrier)e).getTotalCrewSize();
			}
		}
		return crew;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_Container#getTotalMass()
	 */
	public double getTotalMass() {
		return getCargoMass();
	}
	
	/**
	 * Gets the total volume.
	 * 
	 * @return the total volume
	 */
	public double getTotalVolume() {
		return getCargoVolume();
	}
	
	/**
	 * Gets the total mass.
	 * 
	 * @param cos the cos
	 * @param simulator the simulator
	 * 
	 * @return the total mass
	 */
	public double getTotalMass(ClassOfSupply cos, I_Simulator simulator) {
		double amount = 0;
		for(I_Element element : getContents()) {
			amount+=element.getTotalMass(cos);
		}
		return amount;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#toString()
	 */
	public String toString() {
		return getName();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#print(int)
	 */
	public void print(int tabOrder) {
		super.print(tabOrder);
		for(I_Element i : ((I_Container)this).getContents()) {
			i.print(tabOrder+1);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Location location) {
		//return new Integer(getTid()).compareTo(new Integer(location.getTid()));
		if(getTid()==location.getTid()) return 0;
		else if(getName()!=null && !getName().equals(location.getName())) 
			return getName().compareTo(location.getName());
		else return new Integer(getTid()).compareTo(new Integer(location.getTid()));
	}
}