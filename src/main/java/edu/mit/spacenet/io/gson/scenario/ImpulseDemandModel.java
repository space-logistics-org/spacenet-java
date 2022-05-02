package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;

public class ImpulseDemandModel extends DemandModel {
	public List<Resource> demands;

	public static ImpulseDemandModel createFrom(edu.mit.spacenet.domain.model.TimedImpulseDemandModel demandModel, Context context) {
		ImpulseDemandModel m = new ImpulseDemandModel();
		m.id = context.getUUID(demandModel);
		m.name = demandModel.getName();
		m.description = demandModel.getDescription();
		m.demands = Resource.createFrom(demandModel.getDemands(), context);
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.TimedImpulseDemandModel toSpaceNet(UUID source, Context context) {
		edu.mit.spacenet.domain.model.TimedImpulseDemandModel m = new edu.mit.spacenet.domain.model.TimedImpulseDemandModel();
		m.setTid(context.getId(id, m));
		m.setName(name);
		m.setDescription(description);
		m.setDemands(Resource.toSpaceNetSet(demands, context));
		return m;
	}
	
	@Override
	public ImpulseDemandModel clone() {
		ImpulseDemandModel m = new ImpulseDemandModel();
		m.id = UUID.randomUUID();
		m.name = name;
		m.description = description;
		m.demands = Resource.clone(demands);
		return m;
	}
}
