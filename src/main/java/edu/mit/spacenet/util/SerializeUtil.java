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
