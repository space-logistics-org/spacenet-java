package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.domain.model.I_DemandModel;

public class State {
	public static final BiMap<String, StateType> TYPE_MAP = new ImmutableBiMap.Builder<String, StateType>()
			.put("Active", StateType.ACTIVE)
			.put("Quiescent", StateType.QUIESCENT)
			.put("Special", StateType.SPECIAL)
			.put("Dormant", StateType.DORMANT)
			.put("Decommissioned", StateType.DECOMMISSIONED)
			.build();
	
	public UUID id;
	public String name;
	public String description;
	public String type;
	public List<DemandModel> demandModels = new ArrayList<DemandModel>();

	public static State createFrom(I_State state, Context context) {
		State s = new State();
		s.id = context.getUUID(state);
		s.name = state.getName();
		s.description = state.getDescription();
		s.type = TYPE_MAP.inverse().get(state.getStateType());
		for(I_DemandModel m : state.getDemandModels()) {
			s.demandModels.add(DemandModel.createFrom(m, context));
		}
		return s;
	}
	
	public I_State toSpaceNet(Context context) {
		edu.mit.spacenet.domain.element.State s = new edu.mit.spacenet.domain.element.State();
		s.setTid(context.getId(id));
		s.setName(name);
		s.setDescription(description);
		s.setStateType(TYPE_MAP.get(type));
		SortedSet<I_DemandModel> ms = new TreeSet<I_DemandModel>();
		for(DemandModel m : demandModels) {
			ms.add(m.toSpaceNet(context));
		}
		s.setDemandModels(ms);
		return s;
	}
}
