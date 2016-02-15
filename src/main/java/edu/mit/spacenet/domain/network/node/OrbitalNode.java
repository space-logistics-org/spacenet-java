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
package edu.mit.spacenet.domain.network.node;

/**
 * Node that represents stable orbits around planetary bodies.
 * 
 * @author Paul Grogan
 */
public class OrbitalNode extends SpaceNode {
	private double inclination;
	private double periapsis;
	private double apoapsis;

	/**
	 * The default constructor.
	 */
	public OrbitalNode() {
		super();
	}
	
	/**
	 * Gets the inclination of the orbit.
	 * 
	 * @return the inclination (degrees)
	 */
	public double getInclination() {
		return inclination;
	}
	
	/**
	 * Sets the inclination of the orbit.
	 * 
	 * @param inclination the inclination (degrees)
	 */
	public void setInclination(double inclination) {
		this.inclination = inclination;
	}
	
	/**
	 * Gets the periapsis, or distance from the center of the planetary body to
	 * the point on the orbit closest to the planetary body.
	 * 
	 * @return periapsis	the orbital periapsis (kilometers)
	 */
	public double getPeriapsis() {
		return periapsis;
	}
	
	/**
	 * Sets the periapsis, or distance from the center of the planetary body to
	 * the point on the orbit closest to the planetary body.
	 * 
	 * @param periapsis the orbital periapsis (kilometers)
	 */
	public void setPeriapsis(double periapsis) {
		this.periapsis = periapsis;
	}
	
	/**
	 * Gets the apoapsis, or distance from the center of the planetary body to
	 * the point on the orbit farthest away to the planetary body.
	 * 
	 * @return the orbital apoapsis (kilometers)
	 */
	public double getApoapsis() {
		return apoapsis;
	}
	
	/**
	 * Sets the apoapsis, or distance from the center of the planetary body to
	 * the point on the orbit farthest away to the planetary body.
	 * 
	 * @param apoapsis the apoapsis (kilometers)
	 */
	public void setApoapsis(double apoapsis) {
		this.apoapsis = apoapsis;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.node.Node#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.ORBITAL;
	}
}
