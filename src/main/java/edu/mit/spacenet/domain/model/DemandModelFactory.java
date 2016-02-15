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

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.scenario.Mission;

/**
 * A factory for creating demand models.
 * 
 * @author Paul Grogan
 */
public abstract class DemandModelFactory {
	
	/**
	 * Creates a new DemandModel object for a mission.
	 * 
	 * @param mission the mission
	 * @param type the type of demand model
	 * 
	 * @return the demand model
	 */
	public static I_DemandModel createDemandModel(Mission mission, 
			DemandModelType type) {
		switch(type) {
		case CREW_CONSUMABLES: return new CrewConsumablesDemandModel(mission);
		case RATED: return new RatedDemandModel();
		case TIMED_IMPULSE: return new TimedImpulseDemandModel();
		default: throw new RuntimeException("Unsupported Demand Model");
		}
	}
	
	/**
	 * Creates a new DemandModel object for an element.
	 * 
	 * @param element the element
	 * @param type the type of demand model
	 * 
	 * @return the demand model
	 */
	public static I_DemandModel createDemandModel(I_Element element, 
			DemandModelType type) {
		switch(type) {
		case RATED: return new RatedDemandModel();
		case SPARING_BY_MASS: return new SparingByMassDemandModel(element);
		case TIMED_IMPULSE: return new TimedImpulseDemandModel();
		default: throw new RuntimeException("Unsupported Demand Model");
		}
	}
}
