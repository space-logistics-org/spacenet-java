/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
