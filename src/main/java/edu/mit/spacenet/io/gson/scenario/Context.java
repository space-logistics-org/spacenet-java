package edu.mit.spacenet.io.gson.scenario;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Context {
	public BiMap<UUID, Object> uuids = HashBiMap.create();
	public Map<UUID, Integer> ids = new HashMap<UUID, Integer>();
	
	public Object getObject(UUID uuid) {
		return uuids.get(uuid);
	}
	
	private int nextId = 0;
	
	public int getId(UUID uuid) {
		if(!ids.containsKey(uuid)) {
			ids.put(uuid, ++nextId);
		}
		return ids.get(uuid);
	}
	
	public UUID getUUID(Object object) {
		if(!uuids.containsValue(object)) {
			uuids.put(UUID.randomUUID(), object);
		}
		return uuids.inverse().get(object);
	}
}
