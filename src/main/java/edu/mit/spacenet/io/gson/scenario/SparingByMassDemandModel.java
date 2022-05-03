package edu.mit.spacenet.io.gson.scenario;

import edu.mit.spacenet.domain.element.I_Element;

public class SparingByMassDemandModel extends DemandModel {
	public Double unpressurizedSparesRate;
	public Double pressurizedSparesRate;
	public Boolean partsListEnabled;

	public static SparingByMassDemandModel createFrom(edu.mit.spacenet.domain.model.SparingByMassDemandModel model, Context context) {
		SparingByMassDemandModel m = new SparingByMassDemandModel();
		context.getUUID(model); // register object
		m.templateId = context.getModelTemplateUUID(model);
		SparingByMassDemandModel template = (SparingByMassDemandModel) context.getObject(m.templateId);
		if(template == null) {
			m.name = model.getName();
			m.description = model.getDescription();
			m.unpressurizedSparesRate = model.getUnpressurizedSparesRate();
			m.pressurizedSparesRate = model.getPressurizedSparesRate();
			m.partsListEnabled = model.isPartsListEnabled();
		} else {
			if(!template.name.equals(model.getName())) {
				m.name = model.getName();
			}
			if(!template.description.equals(model.getDescription())) {
				m.description = model.getDescription();
			}
			if(!template.unpressurizedSparesRate.equals(model.getUnpressurizedSparesRate())) {
				m.unpressurizedSparesRate = model.getUnpressurizedSparesRate();
			}
			if(!template.pressurizedSparesRate.equals(model.getPressurizedSparesRate())) {
				m.pressurizedSparesRate = model.getPressurizedSparesRate();
			}
			if(!template.partsListEnabled.equals(model.isPartsListEnabled())) {
				m.partsListEnabled = model.isPartsListEnabled();
			}
		}
		return m;
	}
	
	@Override
	public edu.mit.spacenet.domain.model.SparingByMassDemandModel toSpaceNet(Object source, Context context) {
		edu.mit.spacenet.domain.model.SparingByMassDemandModel m = new edu.mit.spacenet.domain.model.SparingByMassDemandModel((I_Element) source);
		m.setTid(templateId == null ? context.getId(id, m) : context.getId(templateId, context.getObject(templateId)));
		edu.mit.spacenet.domain.model.SparingByMassDemandModel template = (edu.mit.spacenet.domain.model.SparingByMassDemandModel) context.getObject(templateId);
		m.setName(name == null ? template.getName() : name);
		m.setDescription(description == null ? template.getDescription() : description);
		m.setUnpressurizedSparesRate(unpressurizedSparesRate == null ? template.getUnpressurizedSparesRate() : unpressurizedSparesRate);
		m.setPressurizedSparesRate(pressurizedSparesRate == null ? template.getPressurizedSparesRate() : pressurizedSparesRate);
		m.setPartsListEnabled(partsListEnabled == null ? template.isPartsListEnabled() : partsListEnabled);
		return m;
	}
	
	@Override
	public SparingByMassDemandModel clone() {
		SparingByMassDemandModel m = new SparingByMassDemandModel();
		m.id = UUID.randomUUID();
		m.name = name;
		m.description = description;
		m.unpressurizedSparesRate = unpressurizedSparesRate;
		m.pressurizedSparesRate = pressurizedSparesRate;
		m.partsListEnabled = partsListEnabled;
		return m;
	}
}
