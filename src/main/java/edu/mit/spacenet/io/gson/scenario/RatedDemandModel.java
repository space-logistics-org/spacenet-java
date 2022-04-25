package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

public class RatedDemandModel extends DemandModel {
	public List<Resource> demandRates = new ArrayList<Resource>();

	public static RatedDemandModel createFrom(edu.mit.spacenet.domain.model.RatedDemandModel demandModel, Context context) {
		RatedDemandModel m = new RatedDemandModel();
		m.id = context.getUUID(demandModel);
		m.name = demandModel.getName();
		m.description = demandModel.getDescription();
		m.demandRates = Resource.createFrom(demandModel.getDemandRates(), context);
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.RatedDemandModel toSpaceNet(Context context) {
		edu.mit.spacenet.domain.model.RatedDemandModel m = new edu.mit.spacenet.domain.model.RatedDemandModel();
		m.setTid(context.getId(id));
		m.setName(name);
		m.setDescription(description);
		m.setDemandRates(Resource.toSpaceNetSet(demandRates, context));
		return m;
	}
}
