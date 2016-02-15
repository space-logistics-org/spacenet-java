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
package edu.mit.spacenet.domain.network.edge;

import edu.mit.spacenet.domain.DomainType;

/**
 * Class that represents an impulsive burn (either OMS or RCS) to achieve a
 * specified delta-v (m/s), offset from the propulsive maneuver by a time.
 * 
 * @author Paul Grogan
 */
public class Burn extends DomainType implements Comparable<Burn> {
	private double time;
	private BurnType burnType;
	private double deltaV;
	
	/**
	 * The default constructor sets the burn type to OMS.
	 */
	public Burn() {
		super();
		setBurnType(BurnType.OMS);
	}
	
	/**
	 * The inline constructor.
	 * 
	 * @param time the burn time (days)
	 * @param burnType the burn type
	 * @param deltaV the delta-v (meters per second)
	 */
	public Burn(double time, BurnType burnType, double deltaV) {
		super();
		setTime(time);
		setBurnType(burnType);
		setDeltaV(deltaV);
	}
	
	/**
	 * Gets the time offset from the initial propulsive maneuver.
	 * 
	 * @return the time offset (days)
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * Sets the time offset from the initial propulsive maneuver.
	 * 
	 * @param time the time offset (days)
	 */
	public void setTime(double time) {
		this.time = time;
	}
	
	/**
	 * Gets the burn type.
	 * 
	 * @return the burn type
	 */
	public BurnType getBurnType() {
		return burnType;
	}
	
	/**
	 * Sets the burn type.
	 * 
	 * @param burnType the burn type
	 */
	public void setBurnType(BurnType burnType) {
		this.burnType = burnType;
	}
	
	/**
	 * Gets the delta-v.
	 * 
	 * @return the delta-v (meters per second)
	 */
	public double getDeltaV() {
		return deltaV;
	}
	
	/**
	 * Sets the delta-v.
	 * 
	 * @param deltaV the delta-v (meters per second)
	 */
	public void setDeltaV(double deltaV) {
		this.deltaV = deltaV;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#toString()
	 */
	@Override
	public String toString() {
		return burnType + " (" + deltaV + " m/s)";
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Burn o) {
		if(o==null) return -1;
		return new Double(getTime()).compareTo(o.getTime());
	}
}