package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;

public class RatedDemandModel extends DemandModel {
	public List<Resource> demandRates = new ArrayList<Resource>();

	public static RatedDemandModel createFrom(edu.mit.spacenet.domain.model.RatedDemandModel model, Context context) {
		RatedDemandModel m = new RatedDemandModel();
		context.getUUID(model); // register object
		m.templateId = context.getModelTemplateUUID(model);
		RatedDemandModel template = (RatedDemandModel) context.getObject(m.templateId);
		if(template == null) {
			m.name = model.getName();
			m.description = model.getDescription();
			m.demandRates = Resource.createFrom(model.getDemandRates(), context);
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
	public edu.mit.spacenet.domain.model.RatedDemandModel toSpaceNet(Object source, Context context) {
		edu.mit.spacenet.domain.model.RatedDemandModel m = new edu.mit.spacenet.domain.model.RatedDemandModel();
		m.setTid(templateId == null ? context.getId(id, m) : context.getId(templateId, context.getObject(templateId)));
		edu.mit.spacenet.domain.model.RatedDemandModel template = (edu.mit.spacenet.domain.model.RatedDemandModel) context.getObject(templateId);
		m.setName(name == null ? template.getName() : name);
		m.setDescription(description == null ? template.getDescription() : description);
		m.setDemandRates(demandRates == null ? template.getDemandRates() : Resource.toSpaceNetSet(demandRates, context));
		return m;
	}
}
