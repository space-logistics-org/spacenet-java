package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.model.I_DemandModel;

public abstract class DemandModel {
	public static final BiMap<String, DemandModelType> TYPE_MAP = new ImmutableBiMap.Builder<String, DemandModelType>()
			.put("Crew Consumables Demand Model", DemandModelType.CREW_CONSUMABLES)
			.put("Timed Impulse Demand Model", DemandModelType.TIMED_IMPULSE)
			.put("Rated Demand Model", DemandModelType.RATED)
			.put("Sparing by Mass Demand Model", DemandModelType.SPARING_BY_MASS)
			.build();
	
	public UUID id;
	public String name;
	public String description;

	public static DemandModel createFrom(I_DemandModel demandModel, Context context) {
		if(demandModel.getDemandModelType() == DemandModelType.TIMED_IMPULSE) {
			return ImpulseDemandModel.createFrom((edu.mit.spacenet.domain.model.TimedImpulseDemandModel) demandModel, context);
		} else if(demandModel.getDemandModelType() == DemandModelType.RATED) {
			return RatedDemandModel.createFrom((edu.mit.spacenet.domain.model.RatedDemandModel) demandModel, context);
		} else if(demandModel.getDemandModelType() == DemandModelType.SPARING_BY_MASS) {
			return SparingByMassDemandModel.createFrom((edu.mit.spacenet.domain.model.SparingByMassDemandModel) demandModel, context);
		} else {
			throw new UnsupportedOperationException("unknown demand model type: " + demandModel.getDemandModelType());
		}
	}
	
	public abstract I_DemandModel toSpaceNet(Context context);
}
