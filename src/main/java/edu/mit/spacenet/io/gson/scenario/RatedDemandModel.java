package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.model.I_DemandModel;

public class RatedDemandModel extends DemandModel {
	public String type = TYPE_MAP.inverse().get(DemandModelType.RATED);
	public List<Resource> demandRates = new ArrayList<Resource>();

	public static RatedDemandModel createFrom(edu.mit.spacenet.domain.model.RatedDemandModel demandModel, Context context) {
		RatedDemandModel m = new RatedDemandModel();
		m.id = context.getUUID(demandModel);
		m.name = demandModel.getName();
		m.description = demandModel.getDescription();
		for(edu.mit.spacenet.domain.resource.Demand d : demandModel.getDemandRates()) {
			m.demandRates.add(Resource.createFrom(d, context));
		}
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.RatedDemandModel toSpaceNet(Context context) {
		edu.mit.spacenet.domain.model.RatedDemandModel m = new edu.mit.spacenet.domain.model.RatedDemandModel();
		m.setTid(context.getId(id));
		m.setName(name);
		m.setDescription(description);
		SortedSet<edu.mit.spacenet.domain.resource.Demand> ds = new TreeSet<edu.mit.spacenet.domain.resource.Demand>();
		for(Resource d : demandRates) {
			ds.add(d.toSpaceNet(context));
		}
		m.setDemandRates(ds);
		return m;
	}
}
