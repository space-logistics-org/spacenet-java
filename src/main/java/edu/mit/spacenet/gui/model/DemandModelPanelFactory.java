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
