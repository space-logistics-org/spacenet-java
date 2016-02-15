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

import edu.mit.spacenet.util.IdGenerator;

/**
 * The base class for any classes utilizing a database-storage format that also
 * require a specialized method for comparisons of objects that may reside at
 * different memory locations, but correspond to one logical object.
 * 
 * @author Paul Grogan
 */
public abstract class DomainObject extends DomainType implements I_DomainObject {
	private int uid;
	
	/**
	 * The default constructor automatically assigns the unique identifier and
	 * defaults the type identifier to -1.
	 */
	public DomainObject() {
		super();
		uid = IdGenerator.getUid();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainObject#getUid()
	 */
	public int getUid() {
		return uid;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainObject#resetUid()
	 */
	public void resetUid() {
		uid = IdGenerator.getUid();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		/* Due to the serialization process used in SpaceNet, domain objects
		 * (i.e. elements) must use unique identification numbers instead of 
		 * memory addresses to determine equality.
		 */
		if(object instanceof DomainObject
				&& getClass().equals(object.getClass()))
			return getUid() == ((DomainObject)object).getUid();
		else return false;
	}
}