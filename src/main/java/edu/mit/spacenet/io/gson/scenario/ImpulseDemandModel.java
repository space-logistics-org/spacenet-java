package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.model.DemandModelType;

public class ImpulseDemandModel extends DemandModel {
	public String type = TYPE_MAP.inverse().get(DemandModelType.TIMED_IMPULSE);
	public List<Resource> demands = new ArrayList<Resource>();

	public static ImpulseDemandModel createFrom(edu.mit.spacenet.domain.model.TimedImpulseDemandModel demandModel, Context context) {
		ImpulseDemandModel m = new ImpulseDemandModel();
		m.id = context.getUUID(demandModel);
		m.name = demandModel.getName();
		m.description = demandModel.getDescription();
		for(edu.mit.spacenet.domain.resource.Demand d : demandModel.getDemands()) {
			m.demands.add(Resource.createFrom(d, context));
		}
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.TimedImpulseDemandModel toSpaceNet(Context context) {
		edu.mit.spacenet.domain.model.TimedImpulseDemandModel m = new edu.mit.spacenet.domain.model.TimedImpulseDemandModel();
		m.setTid(context.getId(id));
		m.setName(name);
		m.setDescription(description);
		SortedSet<edu.mit.spacenet.domain.resource.Demand> ds = new TreeSet<edu.mit.spacenet.domain.resource.Demand>();
		for(Resource d : demands) {
			ds.add(d.toSpaceNet(context));
		}
		m.setDemands(ds);
		return m;
	}
}
