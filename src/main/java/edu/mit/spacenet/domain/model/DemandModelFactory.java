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
