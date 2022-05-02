package edu.mit.spacenet.io.gson.scenario;

import java.util.List;

public class ImpulseDemandModel extends DemandModel {
	public List<Resource> demands;

	public static ImpulseDemandModel createFrom(edu.mit.spacenet.domain.model.TimedImpulseDemandModel model, Context context) {
		ImpulseDemandModel m = new ImpulseDemandModel();
		context.getUUID(model); // register object
		m.templateId = context.getModelTemplateUUID(model);
		ImpulseDemandModel template = (ImpulseDemandModel) context.getObject(m.templateId);
		if(template == null) {
			m.name = model.getName();
			m.description = model.getDescription();
			m.demands = Resource.createFrom(model.getDemands(), context);
		} else {
			if(!template.name.equals(model.getName())) {
				m.name = model.getName();
			}
			if(!template.description.equals(model.getDescription())) {
				m.description = model.getDescription();
			}
		}
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.TimedImpulseDemandModel toSpaceNet(Object source, Context context) {
		edu.mit.spacenet.domain.model.TimedImpulseDemandModel m = new edu.mit.spacenet.domain.model.TimedImpulseDemandModel();
		m.setTid(templateId == null ? context.getId(id, m) : context.getId(templateId, context.getObject(templateId)));
		edu.mit.spacenet.domain.model.TimedImpulseDemandModel template = (edu.mit.spacenet.domain.model.TimedImpulseDemandModel) context.getObject(templateId);
		m.setName(name == null ? template.getName() : name);
		m.setDescription(description == null ? template.getDescription() : description);
		m.setDemands(demands == null ? template.getDemands() : Resource.toSpaceNetSet(demands, context));
		return m;
	}
}
