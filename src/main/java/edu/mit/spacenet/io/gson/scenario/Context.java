package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Context {
	public BiMap<UUID, Object> uuids = HashBiMap.create();
	public Map<UUID, Integer> ids = new HashMap<UUID, Integer>();
	public BiMap<Integer, Object> objects = HashBiMap.create();

	public BiMap<UUID, Integer> templateIds = HashBiMap.create();
	
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
	
	public UUID getTemplateUUID(int templateId) {
		if(!templateIds.containsValue(templateId)) {
			templateIds.put(UUID.randomUUID(), templateId);
		}
		return templateIds.inverse().get(templateId);
	}
	
	public boolean isTemplateUUID(int templateId) {
		return templateIds.containsValue(templateId);
	}
	
	private int nextTemplateId = 0;
	
	public int getTemplateId(UUID uuid) {
		if(!templateIds.containsKey(uuid)) {
			templateIds.put(uuid, ++nextTemplateId);
		}
		return templateIds.get(uuid);
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
}
