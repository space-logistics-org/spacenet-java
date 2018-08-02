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