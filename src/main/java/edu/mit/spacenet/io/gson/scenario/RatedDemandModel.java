package edu.mit.spacenet.io.gson.scenario;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
      List<ResourceRate> demands = ResourceRate.createFrom(model.getDemandRates(), context);
      if (!template.demands.equals(demands)) {
        m.demands = demands;
      }
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
  public boolean equals(Object obj) {
    if (!(obj instanceof RatedDemandModel)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final RatedDemandModel other = (RatedDemandModel) obj;
    return new EqualsBuilder().appendSuper(super.equals(other)).append(demands, other.demands)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).appendSuper(super.hashCode()).append(demands).toHashCode();
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
