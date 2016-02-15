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
 * Node that represents Lagrangian points in space.
 * 
 * @author Paul Grogan
 */
public class LagrangeNode extends SpaceNode {
	private Body minorBody;
	private int number;
	
	/**
	 * The default constructor.
	 */
	public LagrangeNode() {
		super();
	}
	
	/**
	 * Gets the Lagrangian number.
	 * 
	 * @return the Lagrangian number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Sets the Lagrangian number.
	 * 
	 * @param number the Lagrangian number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Gets the minor body of the Lagrangian.
	 * 
	 * @return the minor body
	 */
	public Body getMinorBody() {
		return minorBody;
	}
	
	/**
	 * Sets the major body of the Lagrangian.
	 * 
	 * @param minorBody the minor body
	 */
	public void setMinorBody(Body minorBody) {
		this.minorBody = minorBody;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.network.node.Node#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.LAGRANGE;
	}
}
