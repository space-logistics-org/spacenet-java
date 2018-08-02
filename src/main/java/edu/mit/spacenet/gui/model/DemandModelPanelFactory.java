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
package edu.mit.spacenet.gui.model;

import edu.mit.spacenet.domain.model.CrewConsumablesDemandModel;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.model.RatedDemandModel;
import edu.mit.spacenet.domain.model.SparingByMassDemandModel;
import edu.mit.spacenet.domain.model.TimedImpulseDemandModel;

/**
 * A factory for creating demand model panels.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class DemandModelPanelFactory {
	
	/**
	 * Creates a new DemandModelPanel object.
	 * 
	 * @param demandModelDialog the demand model dialog
	 * @param demandModel the demand model
	 * 
	 * @return the abstract demand model panel
	 */
	public static AbstractDemandModelPanel createDemandModelPanel(DemandModelDialog demandModelDialog, I_DemandModel demandModel) {
		switch(demandModel.getDemandModelType()) {
		case CREW_CONSUMABLES:
			return new CrewConsumablesDemandModelPanel(demandModelDialog, (CrewConsumablesDemandModel)demandModel);
		case TIMED_IMPULSE:
			return new TimedImpulseDemandModelPanel(demandModelDialog, (TimedImpulseDemandModel)demandModel);
		case RATED:
			return new RatedDemandModelPanel(demandModelDialog, (RatedDemandModel)demandModel);
		case SPARING_BY_MASS:
			return new SparingByMassDemandModelPanel(demandModelDialog, (SparingByMassDemandModel)demandModel);
		default: throw new RuntimeException("Unsupported Demand Model");
		}
	}

}
