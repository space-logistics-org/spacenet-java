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
 * Node that represents a point on a planetary body's surface.
 * 
 * @author Paul Grogan
 */
public class SurfaceNode extends Node {
	private double latitude;
	private double longitude;
	
	/**
	 * The default constructor.
	 */
	public SurfaceNode() {
		super();
	}
	
	/**
	 * Gets the latitude of the point.
	 * 
	 * @return the latitude (degrees)
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude of the point.
	 * 
	 * @param latitude the latitude (degrees)
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude of the point.
	 * 
	 * @return the longitude (degrees)
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude of the point.
	 * 
	 * @param longitude the longitude (degrees)
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.node.Node#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.SURFACE;
	}
}
