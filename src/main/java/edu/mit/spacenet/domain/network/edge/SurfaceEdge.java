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

/**
 * Edge that represents a surface transfer with a specified distance.
 * 
 * @author Paul Grogan
 */
public class SurfaceEdge extends Edge {
	
	/** The distance. */
	double distance;
	
	/**
	 * The default constructor.
	 */
	public SurfaceEdge() {
		super();
	}
	
	/**
	 * Gets the distance.
	 * 
	 * @return the distance (kilometers)
	 */
	public double getDistance() {
		return this.distance;
	}
	
	/**
	 * Sets the distance.
	 * 
	 * @param distance the distance (kilometers)
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.edge.Edge#getEdgeType()
	 */
	@Override
	public EdgeType getEdgeType() {
		return EdgeType.SURFACE;
	}
}