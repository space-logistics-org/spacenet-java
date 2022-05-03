package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RatedDemandModel extends DemandModel {
	protected List<Resource> demandRates = new ArrayList<Resource>();

	public static RatedDemandModel createFrom(edu.mit.spacenet.domain.model.RatedDemandModel model, Context context) {
		RatedDemandModel m = new RatedDemandModel();
		m.templateId = context.getModelTemplate(model.getTid());
		RatedDemandModel template = (RatedDemandModel) context.getJsonObject(m.templateId);
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
		if(id != null) {
			context.putModelTemplate(m, id, this);
		}
		m.setTid(context.getJavaId(templateId == null ? id : templateId));
		RatedDemandModel template = (RatedDemandModel) context.getJsonObject(templateId);
		m.setName(name == null ? template.name : name);
		m.setDescription(description == null ? template.description : description);
		m.setDemandRates(Resource.toSpaceNetSet(demandRates == null ? template.demandRates : demandRates, context));
		return m;
	}
	
	@Override
	public RatedDemandModel clone() {
		RatedDemandModel m = new RatedDemandModel();
		if(id != null) {
			m.id = UUID.randomUUID();
		}
		m.templateId = templateId;
		m.name = name;
		m.description = description;
		m.demandRates = Resource.clone(demandRates);
		return m;
	}
}
