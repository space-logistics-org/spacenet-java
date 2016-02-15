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
 * The domain type class is used to assign identification numbers to objects
 * pulled from the data source. A domain type is used to differentiate static
 * objects like locations, and also object-types like element types.
 * 
 * @author Paul Grogan
 */
public abstract class DomainType implements I_DomainType {
	private int tid;
	private String name;
	private String description;
	
	/**
	 * The default constructor automatically assigns the type identifier to
	 * negative random number and assigns a default name of "".
	 */
	public DomainType() {
		tid = (int)(-1*Integer.MAX_VALUE*Math.random());
		setName("");
		setDescription("");
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#getTid()
	 */
	public int getTid() {
		return tid;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#setTid(int)
	 */
	public void setTid(int tid) {
		this.tid = tid;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		/* Due to the serialization process used in SpaceNet, domain types
		 * (i.e. locations, states, etc.) must use type identification numbers
		 * instead of memory addresses to determine equality.
		 */
		if(object instanceof DomainType
				&& getClass().equals(object.getClass()))
			return getTid() == ((DomainType)object).getTid();
		else return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#print()
	 */
	public void print() { 
		print(0); 
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.I_DomainType#print(int)
	 */
	public void print(int tabOrder) {
		String s = "";
		for(int i=0; i<tabOrder; i++) s += "  ";
		s += "- " + toString();
		System.out.printf("%s\n", s);
	}
}