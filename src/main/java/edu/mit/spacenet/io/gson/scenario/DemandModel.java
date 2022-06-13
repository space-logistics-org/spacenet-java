package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.model.I_DemandModel;

public abstract class DemandModel implements Cloneable {
  public static final BiMap<String, DemandModelType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, DemandModelType>()
          .put("Crew Consumables", DemandModelType.CREW_CONSUMABLES)
          .put("Timed Impulse", DemandModelType.TIMED_IMPULSE).put("Rated", DemandModelType.RATED)
          .put("Sparing By Mass", DemandModelType.SPARING_BY_MASS).build();

  protected UUID id; // used only for templates
  protected UUID templateId; // used only for instances
  protected String name;
  protected String description;

  public static DemandModel createFrom(I_DemandModel model, Context context) {
    if (model.getDemandModelType() == DemandModelType.TIMED_IMPULSE) {
      return ImpulseDemandModel
          .createFrom((edu.mit.spacenet.domain.model.TimedImpulseDemandModel) model, context);
    } else if (model.getDemandModelType() == DemandModelType.RATED) {
      return RatedDemandModel.createFrom((edu.mit.spacenet.domain.model.RatedDemandModel) model,
          context);
    } else if (model.getDemandModelType() == DemandModelType.SPARING_BY_MASS) {
      return SparingByMassDemandModel
          .createFrom((edu.mit.spacenet.domain.model.SparingByMassDemandModel) model, context);
    } else if (model.getDemandModelType() == DemandModelType.CREW_CONSUMABLES) {
      return ConsumablesDemandModel
          .createFrom((edu.mit.spacenet.domain.model.CrewConsumablesDemandModel) model, context);
    } else {
      throw new UnsupportedOperationException(
          "unknown demand model type: " + model.getDemandModelType());
    }
  }

  public static List<DemandModel> createFrom(Collection<I_DemandModel> models, Context context) {
    List<DemandModel> ds = new ArrayList<DemandModel>();
    for (I_DemandModel d : models) {
      ds.add(DemandModel.createFrom(d, context));
    }
    return ds;
  }

  public abstract I_DemandModel toSpaceNet(Object source, Context context);

  public static SortedSet<I_DemandModel> toSpaceNet(Object source, Collection<DemandModel> models,
      Context context) {
    SortedSet<I_DemandModel> ds = new TreeSet<I_DemandModel>();
    for (DemandModel d : models) {
      ds.add(d.toSpaceNet(source, context));
    }
    return ds;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DemandModel)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final DemandModel other = (DemandModel) obj;
    return new EqualsBuilder().append(id, other.id).append(templateId, other.templateId)
        .append(name, other.name).append(description, other.description).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).append(id).append(templateId).append(name)
        .append(description).toHashCode();
  }

  @Override
  public abstract DemandModel clone();

  public static List<DemandModel> clone(Collection<? extends DemandModel> models) {
    List<DemandModel> ms = new ArrayList<DemandModel>();
    for (DemandModel m : models) {
      ms.add(m.clone());
    }
    return ms;
  }
}
