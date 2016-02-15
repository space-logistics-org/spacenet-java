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
package edu.mit.spacenet.domain;

/**
 * The base interface for any domain-level objects.
 * 
 * @author Paul Grogan
 */
public interface I_DomainObject extends I_DomainType {
	
	/**
	 * Gets the Unique IDentifier.
	 * 
	 * @return 	the unique identifier
	 */
	public int getUid();
	
	/**
	 * Resets the Unique IDentifier after instantiation. Used during copy
	 * processes where already-instantiated domain objects must be reassigned
	 * a new identifier.
	 */
	public void resetUid();
	
	/**
	 * Gets the Type IDentifier.
	 * 
	 * @return 	the type identifier
	 */
	public int getTid();
	
	/**
	 * Sets the Type IDentifier.
	 * 
	 * @param tid the type identifier
	 */
	public void setTid(int tid);
	
	/**
	 * Prints to console without any indentation.
	 */
	public void print();
	
	/**
	 * Prints to console at a specific tab indentation level.
	 * 
	 * @param tabOrder the indentation level
	 */
	public void print(int tabOrder);
}