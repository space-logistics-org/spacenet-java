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
package edu.mit.spacenet.domain.model;

import edu.mit.spacenet.domain.DomainType;

/**
 * Base class for all demand models.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractDemandModel extends DomainType implements I_DemandModel {	
	
	/**
	 * The default constructor that sets a default name.
	 */
	public AbstractDemandModel() {
		setName(getDemandModelType().getName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(I_DemandModel demandModel) {
		if(getName().equals(demandModel.getName())) {
			return new Integer(getTid()).compareTo(new Integer(demandModel.getTid()));
		} else {
			return getName().compareTo(demandModel.getName());
		}
	}
}