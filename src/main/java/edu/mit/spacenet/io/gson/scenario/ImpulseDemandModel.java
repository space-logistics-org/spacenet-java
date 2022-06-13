package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ImpulseDemandModel extends DemandModel {
  protected List<Resource> demands;

  public static ImpulseDemandModel createFrom(
      edu.mit.spacenet.domain.model.TimedImpulseDemandModel model, Context context) {
    ImpulseDemandModel m = new ImpulseDemandModel();
    m.templateId = context.getModelTemplate(model.getTid());
    ImpulseDemandModel template = (ImpulseDemandModel) context.getJsonObject(m.templateId);
    if (template == null) {
      m.name = model.getName();
      m.description = model.getDescription();
      m.demands = Resource.createFrom(model.getDemands(), context);
    } else {
      if (!template.name.equals(model.getName())) {
        m.name = model.getName();
      }
      if (!template.description.equals(model.getDescription())) {
        m.description = model.getDescription();
      }
      List<Resource> demands = Resource.createFrom(model.getDemands(), context);
      if (!template.demands.equals(demands)) {
        m.demands = demands;
      }
    }
    return m;
  }

  @Override
  public edu.mit.spacenet.domain.model.TimedImpulseDemandModel toSpaceNet(Object source,
      Context context) {
    edu.mit.spacenet.domain.model.TimedImpulseDemandModel m =
        new edu.mit.spacenet.domain.model.TimedImpulseDemandModel();
    if (id != null) {
      context.putModelTemplate(m, id, this);
    }
    m.setTid(context.getJavaId(templateId == null ? id : templateId));
    ImpulseDemandModel template = (ImpulseDemandModel) context.getJsonObject(templateId);
    m.setName(name == null ? template.name : name);
    m.setDescription(description == null ? template.description : description);
    m.setDemands(Resource.toSpaceNetSet(demands == null ? template.demands : demands, context));
    return m;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ImpulseDemandModel)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final ImpulseDemandModel other = (ImpulseDemandModel) obj;
    return new EqualsBuilder().appendSuper(super.equals(other)).append(demands, other.demands)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).appendSuper(super.hashCode()).append(demands).toHashCode();
  }

  @Override
  public ImpulseDemandModel clone() {
    ImpulseDemandModel m = new ImpulseDemandModel();
    if (id != null) {
      m.id = UUID.randomUUID();
    }
    m.templateId = templateId;
    m.name = name;
    m.description = description;
    m.demands = Resource.clone(demands);
    return m;
  }
}
