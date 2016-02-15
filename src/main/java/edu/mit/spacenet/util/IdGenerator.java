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
package edu.mit.spacenet.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Keeps track of the sequence of unique identifiers that are assigned to
 * newly instantiated items.
 * 
 * @author Paul Grogan
 */
public class IdGenerator {
	private static AtomicInteger uid = new AtomicInteger(1);
	
	/**
	 * Gets the next unique identifier in the sequence.
	 * 
	 * @return the next unique identifier
	 */
	public static int getUid() {
		return uid.getAndIncrement();
	}
	
	/**
	 * Sets the sequence of unique identifies to restart after the last uid.
	 * 
	 * @param lastUid the last uid
	 */
	public static void setLastUid(int lastUid) {
		uid.set(lastUid+1);
	}
}