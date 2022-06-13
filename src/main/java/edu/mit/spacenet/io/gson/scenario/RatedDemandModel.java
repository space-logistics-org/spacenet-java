package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;

public class RatedDemandModel extends DemandModel {
  protected List<ResourceRate> demands;

  public static RatedDemandModel createFrom(edu.mit.spacenet.domain.model.RatedDemandModel model,
      Context context) {
    RatedDemandModel m = new RatedDemandModel();
    m.templateId = context.getModelTemplate(model.getTid());
    RatedDemandModel template = (RatedDemandModel) context.getJsonObject(m.templateId);
    if (template == null) {
      m.name = model.getName();
      m.description = model.getDescription();
      m.demands = ResourceRate.createFrom(model.getDemandRates(), context);
    } else {
      if (!template.name.equals(model.getName())) {
        m.name = model.getName();
      }
      if (!template.description.equals(model.getDescription())) {
        m.description = model.getDescription();
      }
      // TODO cannot override template demands; fails silently
    }
    return m;
  }

  @Override
  public edu.mit.spacenet.domain.model.RatedDemandModel toSpaceNet(Object source, Context context) {
    edu.mit.spacenet.domain.model.RatedDemandModel m =
        new edu.mit.spacenet.domain.model.RatedDemandModel();
    if (id != null) {
      context.putModelTemplate(m, id, this);
    }
    m.setTid(context.getJavaId(templateId == null ? id : templateId));
    RatedDemandModel template = (RatedDemandModel) context.getJsonObject(templateId);
    m.setName(name == null ? template.name : name);
    m.setDescription(description == null ? template.description : description);
    m.setDemandRates(
        ResourceRate.toSpaceNetSet(demands == null ? template.demands : demands, context));
    return m;
  }

  @Override
  public RatedDemandModel clone() {
    RatedDemandModel m = new RatedDemandModel();
    if (id != null) {
      m.id = UUID.randomUUID();
    }
    m.templateId = templateId;
    m.name = name;
    m.description = description;
    m.demands = ResourceRate.clone(demands);
    return m;
  }
}
