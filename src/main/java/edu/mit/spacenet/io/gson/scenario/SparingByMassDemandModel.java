package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import edu.mit.spacenet.domain.element.I_Element;

public class SparingByMassDemandModel extends DemandModel {
  protected Double unpressurizedSparesRate;
  protected Double pressurizedSparesRate;
  protected Boolean partsListEnabled;

  public static SparingByMassDemandModel createFrom(
      edu.mit.spacenet.domain.model.SparingByMassDemandModel model, Context context) {
    SparingByMassDemandModel m = new SparingByMassDemandModel();
    m.templateId = context.getModelTemplate(model.getTid());
    SparingByMassDemandModel template =
        (SparingByMassDemandModel) context.getJsonObject(m.templateId);
    if (template == null) {
      m.name = model.getName();
      m.description = model.getDescription();
      m.unpressurizedSparesRate = model.getUnpressurizedSparesRate();
      m.pressurizedSparesRate = model.getPressurizedSparesRate();
      m.partsListEnabled = model.isPartsListEnabled();
    } else {
      if (!template.name.equals(model.getName())) {
        m.name = model.getName();
      }
      if (!template.description.equals(model.getDescription())) {
        m.description = model.getDescription();
      }
      if (!template.unpressurizedSparesRate.equals(model.getUnpressurizedSparesRate())) {
        m.unpressurizedSparesRate = model.getUnpressurizedSparesRate();
      }
      if (!template.pressurizedSparesRate.equals(model.getPressurizedSparesRate())) {
        m.pressurizedSparesRate = model.getPressurizedSparesRate();
      }
      if (!template.partsListEnabled.equals(model.isPartsListEnabled())) {
        m.partsListEnabled = model.isPartsListEnabled();
      }
    }
    return m;
  }

  @Override
  public edu.mit.spacenet.domain.model.SparingByMassDemandModel toSpaceNet(Object source,
      Context context) {
    edu.mit.spacenet.domain.model.SparingByMassDemandModel m =
        new edu.mit.spacenet.domain.model.SparingByMassDemandModel((I_Element) source);
    if (id != null) {
      context.putModelTemplate(m, id, this);
    }
    m.setTid(context.getJavaId(templateId == null ? id : templateId));
    SparingByMassDemandModel template =
        (SparingByMassDemandModel) context.getJsonObject(templateId);
    m.setName(name == null ? template.name : name);
    m.setDescription(description == null ? template.description : description);
    m.setUnpressurizedSparesRate(unpressurizedSparesRate == null ? template.unpressurizedSparesRate
        : unpressurizedSparesRate);
    m.setPressurizedSparesRate(
        pressurizedSparesRate == null ? template.pressurizedSparesRate : pressurizedSparesRate);
    m.setPartsListEnabled(partsListEnabled == null ? template.partsListEnabled : partsListEnabled);
    return m;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SparingByMassDemandModel)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final SparingByMassDemandModel other = (SparingByMassDemandModel) obj;
    return new EqualsBuilder().appendSuper(super.equals(other))
        .append(unpressurizedSparesRate, other.unpressurizedSparesRate)
        .append(pressurizedSparesRate, other.pressurizedSparesRate)
        .append(partsListEnabled, other.partsListEnabled).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).appendSuper(super.hashCode()).append(unpressurizedSparesRate)
        .append(pressurizedSparesRate).append(partsListEnabled).toHashCode();
  }

  @Override
  public SparingByMassDemandModel clone() {
    SparingByMassDemandModel m = new SparingByMassDemandModel();
    if (id != null) {
      m.id = UUID.randomUUID();
    }
    m.templateId = templateId;
    m.name = name;
    m.description = description;
    m.unpressurizedSparesRate = unpressurizedSparesRate;
    m.pressurizedSparesRate = pressurizedSparesRate;
    m.partsListEnabled = partsListEnabled;
    return m;
  }
}
