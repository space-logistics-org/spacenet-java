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
 * The environment enumeration is used to differentiate between pressurized and
 * unpressurized cargo.
 * 
 * @author Paul Grogan
 */
public enum Environment {
	
	/** Pressurized environments. */
	PRESSURIZED("Pressurized"),
	
	/** Unpressurized environments. */
	UNPRESSURIZED("Unpressurized");
	
	private String name;
	
	private Environment(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Gets the single instance of Environment.
	 * 
	 * @param name the name
	 * 
	 * @return single instance of Environment
	 */
	public static Environment getInstance(String name) {
		for(Environment environment : values()) {
			if(environment.getName().toLowerCase().equals(name.toLowerCase()))
				return environment;
		}
		return null;
	}
}
