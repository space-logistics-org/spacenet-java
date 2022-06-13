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

import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.StateType;

public class State implements Cloneable {
  public static final BiMap<String, StateType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, StateType>().put("Active", StateType.ACTIVE)
          .put("Quiescent", StateType.QUIESCENT).put("Special", StateType.SPECIAL)
          .put("Dormant", StateType.DORMANT).put("Decommissioned", StateType.DECOMMISSIONED)
          .build();

  protected UUID id;
  protected String name;
  protected String description;
  protected String type;
  protected List<DemandModel> demandModels;

  public static State createFrom(I_State state, Context context) {
    State s = new State();
    s.id = UUID.randomUUID();
    context.put(state, s.id, s);
    s.name = state.getName();
    s.description = state.getDescription();
    s.type = TYPE_MAP.inverse().get(state.getStateType());
    s.demandModels = DemandModel.createFrom(state.getDemandModels(), context);
    return s;
  }

  public static List<State> createFrom(Collection<I_State> states, Context context) {
    List<State> ss = new ArrayList<State>();
    for (I_State s : states) {
      ss.add(State.createFrom(s, context));
    }
    return ss;
  }

  public I_State toSpaceNet(Object source, Context context) {
    edu.mit.spacenet.domain.element.State s = new edu.mit.spacenet.domain.element.State();
    context.put(s, id, this);
    s.setTid(context.getJavaId(id));
    s.setName(name);
    s.setDescription(description);
    s.setStateType(TYPE_MAP.get(type));
    s.setDemandModels(DemandModel.toSpaceNet(source, demandModels, context));
    return s;
  }

  public static SortedSet<I_State> toSpaceNet(Object source, Collection<State> states,
      Context context) {
    SortedSet<I_State> ss = new TreeSet<I_State>();
    if (states != null) {
      for (State state : states) {
        ss.add(state.toSpaceNet(source, context));
      }
    }
    return ss;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof State)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final State other = (State) obj;
    // nb: omit id to allow for instances to match with template
    return new EqualsBuilder().append(name, other.name).append(description, other.description)
        .append(type, other.type).append(demandModels, other.demandModels).isEquals();
  }

  @Override
  public int hashCode() {
    // nb: omit id to allow for instances to match with template
    return new HashCodeBuilder(17, 31).append(name).append(description).append(type)
        .append(demandModels).toHashCode();
  }

  @Override
  public State clone() {
    State s = new State();
    s.id = UUID.randomUUID();
    s.name = name;
    s.description = description;
    s.type = type;
    s.demandModels = DemandModel.clone(demandModels);
    return s;
  }


  public static List<State> clone(Collection<State> states) {
    List<State> ss = new ArrayList<State>();
    for (State s : states) {
      ss.add(s.clone());
    }
    return ss;
  }
}
