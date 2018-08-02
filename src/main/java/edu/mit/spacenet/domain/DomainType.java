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