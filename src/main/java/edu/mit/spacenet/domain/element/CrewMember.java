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
package edu.mit.spacenet.domain.element;

import edu.mit.spacenet.domain.ClassOfSupply;

/**
 * An element used to represent crew members.
 * 
 * @author Paul Grogan
 */
public class CrewMember extends Element implements I_Agent {
	private double availableTimeFraction;
	
	/**
	 * The default constructor that sets the class of supply to COS0.
	 */
	public CrewMember() {
		super();
		setClassOfSupply(ClassOfSupply.COS0);
		availableTimeFraction = 0.667;
	}
	
	/**
	 * Gets the available time fraction (percent of time available for
	 * exploration, maintenance, etc.)
	 * 
	 * @return the available time fraction
	 */
	public double getAvailableTimeFraction() {
		return availableTimeFraction;
	}
	
	/**
	 * Sets the available time fraction (percent of time available for
	 * exploration, maintenance, etc.)
	 * 
	 * @param availableTimeFraction the available time fraction
	 */
	public void setAvailableTimeFraction(double availableTimeFraction) {
		this.availableTimeFraction = availableTimeFraction;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Element#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.CREW_MEMBER;
	}
}