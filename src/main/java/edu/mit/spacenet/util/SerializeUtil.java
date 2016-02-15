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

import com.thoughtworks.xstream.XStream;

/**
 * A utility class used to serialize (clone) objects.
 * 
 * @author Paul Grogan
 */
public class SerializeUtil {
	
	/**
	 * Creates a serialized version of an object.
	 * 
	 * @param object the object to serialize
	 * 
	 * @return the serialized object
	 */
	public static Object deepClone(Object object) {
		XStream xStream = new XStream();
		return xStream.fromXML((String)xStream.toXML(object));
	}
}
