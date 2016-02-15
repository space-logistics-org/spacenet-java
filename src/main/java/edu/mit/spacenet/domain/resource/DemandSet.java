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
package edu.mit.spacenet.domain.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.util.GlobalParameters;

/**
 * A wrapper for a set of demands that performs grouping operations.
 * 
 * @author Paul Grogan
 */
public class DemandSet implements Iterable<Demand> {
	private static final long serialVersionUID = 153270744123011256L;
	private SortedSet<Demand> demands;
	
	/**
	 * The constructor.
	 */
	public DemandSet() {
		demands = new TreeSet<Demand>();
	}
	
	/**
	 * Adds a demand.
	 * 
	 * @param demand the demand
	 * 
	 * @return whether the operation was successful
	 */
	public boolean add(Demand demand) {
		for(Demand d : demands) {
			if(d.getResource().equals((demand.getResource()))) {
				d.setAmount(d.getAmount() + demand.getAmount());
				return true;
			}
		}
		Demand d = new Demand();
		d.setResource(demand.getResource());
		d.setAmount(demand.getAmount());
		return demands.add(d);
	}
	
	/**
	 * Adds a set of demands.
	 * 
	 * @param demands the demands
	 */
	public void addAll(Iterable<Demand> demands) {
		for(Demand demand : demands) {
			add(demand);
		}
	}
	
	/**
	 * Removes a demand.
	 * 
	 * @param demand the demand
	 * 
	 * @return whether the operation was successful
	 */
	public boolean remove(Demand demand) {
		for(Demand d : demands) {
			if(d.getResource().equals(demand.getResource())) {
				if(d.getAmount() >= demand.getAmount()) {
					d.setAmount(d.getAmount() - demand.getAmount());
					demand.setAmount(0);
					return true;
				} else {
					demand.setAmount(demand.getAmount() - d.getAmount());
					d.setAmount(0);
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets the total mass of all demands.
	 * 
	 * @return the mass (kilograms)
	 */
	public double getTotalMass() {
		double mass = 0;
		for(Demand demand : demands) {
			mass += demand.getMass();
		}
		return GlobalParameters.getRoundedMass(mass);
	}
	
	/**
	 * Gets the total volume of all demands.
	 * 
	 * @return the volume (cubic meters)
	 */
	public double getTotalVolume() {
		double volume = 0;
		for(Demand demand : demands) {
			volume += demand.getVolume();
		}
		return GlobalParameters.getRoundedVolume(volume);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Demand> iterator() {
		return demands.iterator();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return demands.toString();
	}
	
	/**
	 * Gets the number of different demands.
	 * 
	 * @return the size of the set
	 */
	public int size() {
		return demands.size();
	}
	
	/**
	 * Clears all of the demands.
	 */
	public void clear() {
		demands.clear();
	}
	
	/**
	 * Removes any demands with zero amount.
	 */
	public void clean() {
		List<Demand> forRemoval = new ArrayList<Demand>();
		for(Demand demand : this) {
			if(demand==null||demand.getAmount()==0)
				forRemoval.add(demand);
		}
		for(Demand d : forRemoval) {
			demands.remove(d);
		}
	}
}
