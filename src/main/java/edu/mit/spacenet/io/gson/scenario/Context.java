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
	
	public Object getObject(UUID uuid) {
		return uuids.get(uuid);
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
