package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.model.I_DemandModel;

public class Context {
	public BiMap<UUID, Object> uuids = HashBiMap.create();
	public Map<UUID, Integer> ids = new HashMap<UUID, Integer>();
	public BiMap<Integer, Object> objects = HashBiMap.create();

	public BiMap<UUID, Integer> elementTemplateIds = HashBiMap.create();
	public BiMap<UUID, Integer> modelTemplateIds = HashBiMap.create();
	
	public Object getObject(UUID uuid) {
		return objects.get(ids.get(uuid));
	}
	
	private int nextId = 0;
	
	public int getId(UUID uuid, Object object) {
		if(!ids.containsKey(uuid)) {
			int id = ++nextId;
			ids.put(uuid, id);
			objects.put(id, object);
		}
		return ids.get(uuid);
	}
	
	public int getId(UUID uuid) {
		return ids.get(uuid);
	}
	
	public UUID getUUID(Object object) {
		if(object == null) {
			return null;
		}
		if(!uuids.containsValue(object)) {
			uuids.put(UUID.randomUUID(), object);
		}
		return uuids.inverse().get(object);
	}
	
	public List<UUID> getUUIDs(Collection<? extends Object> objects) {
		List<UUID> uuids = new ArrayList<UUID>();
		for(Object o : objects) {
			uuids.add(getUUID(o));
		}
		return uuids;
	}
	
	public UUID getElementTemplateUUID(I_Element element) {
		if(!elementTemplateIds.containsValue(element.getTid())) {
			elementTemplateIds.put(UUID.randomUUID(), element.getTid());
		}
		return elementTemplateIds.inverse().get(element.getTid());
	}
	
	public boolean isElementTemplateUUID(int templateId) {
		return elementTemplateIds.containsValue(templateId);
	}
	
	private int nextElementTemplateId = 0;
	
	public int getElementTemplateId(UUID uuid) {
		if(!elementTemplateIds.containsKey(uuid)) {
			elementTemplateIds.put(uuid, ++nextElementTemplateId);
		}
		return elementTemplateIds.get(uuid);
	}
	
	public UUID getModelTemplateUUID(I_DemandModel model) {
		if(!modelTemplateIds.containsValue(model.getTid())) {
			modelTemplateIds.put(UUID.randomUUID(), model.getTid());
		}
		return modelTemplateIds.inverse().get(model.getTid());
	}
	
	public boolean isModelTemplateUUID(int templateId) {
		return modelTemplateIds.containsValue(templateId);
	}
	
	private int nextModelTemplateId = 0;
	
	public int getModelTemplateId(UUID uuid) {
		if(!modelTemplateIds.containsKey(uuid)) {
			modelTemplateIds.put(uuid, ++nextModelTemplateId);
		}
		return modelTemplateIds.get(uuid);
	}
}
