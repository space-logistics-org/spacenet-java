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
package edu.mit.spacenet.scenario;

/**
 * Enumeration representing the different methods of item discretization.
 * 
 * @author Paul Grogan
 */
public enum ItemDiscretization {
	
	/** Items are not discretized and are treated as continuous resources. */
	NONE("None"),
	
	/** Items are discretized on a per-element basis. */
	BY_ELEMENT("Element"),
	
	/** Items are discretized on a per-location basis. */
	BY_LOCATION("Location"),
	
	/** Items are discretized on a global basis. */
	BY_SCENARIO("Scenario");
	
	private String name;
	
	private ItemDiscretization(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of the item discretization.
	 * 
	 * @return the item discretization name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return getName();
	}
}