package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.model.DemandModelType;

public class RatedDemandModel extends DemandModel {
	public String type = TYPE_MAP.inverse().get(DemandModelType.RATED);
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
		m.setDemandRates(Resource.toSpaceNet(demandRates, context));
		return m;
	}
}
