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
  private BiMap<Integer, Object> javaId_javaObject = HashBiMap.create();
  private BiMap<UUID, Object> jsonId_jsonObject = HashBiMap.create();
  private BiMap<Integer, UUID> javaId_jsonId = HashBiMap.create();

  public int getJavaId(Object javaObject) {
    return javaId_javaObject.inverse().get(javaObject);
  }

  public int getJavaId(UUID jsonId) {
    return javaId_jsonId.inverse().get(jsonId);
  }

  public Object getJsonObject(UUID jsonId) {
    return jsonId_jsonObject.get(jsonId);
  }

  public Object getJsonObjectFromJavaObject(Object javaObject) {
    if (javaObject == null) {
      return null;
    }
    return jsonId_jsonObject.get(getJsonIdFromJavaObject(javaObject));
  }

  public UUID getJsonIdFromJavaObject(Object javaObject) {
    if (javaObject == null) {
      return null;
    }
    return javaId_jsonId.get(getJavaId(javaObject));
  }

  public List<UUID> getJsonIdsFromJavaObjects(Collection<? extends Object> javaObjects) {
    List<UUID> jsonIds = new ArrayList<UUID>();
    for (Object o : javaObjects) {
      jsonIds.add(getJsonIdFromJavaObject(o));
    }
    return jsonIds;
  }

  public Object getJavaObjectFromJsonId(UUID jsonId) {
    if (jsonId == null) {
      return null;
    }
    return javaId_javaObject.get(getJavaId(jsonId));
  }

  private int nextJavaId = 0;

  public void put(Object javaObject, UUID jsonId, Object jsonObject) {
    if (javaId_javaObject.inverse().get(javaObject) == null) {
      javaId_javaObject.put(++nextJavaId, javaObject);
    }
    jsonId_jsonObject.put(jsonId, jsonObject);
    javaId_jsonId.put(getJavaId(javaObject), jsonId);
  }

  private Map<Integer, UUID> modelTemplates = new HashMap<Integer, UUID>();

  public void putModelTemplate(I_DemandModel template, UUID jsonId, Object jsonObject) {
    modelTemplates.put(template.getTid(), jsonId);
    put(template, jsonId, jsonObject);
  }

  public UUID getModelTemplate(int templateId) {
    return modelTemplates.get(templateId);
  }

  private Map<Integer, UUID> elementTemplates = new HashMap<Integer, UUID>();

  public void putElementTemplate(I_Element template, UUID jsonId, Object jsonObject) {
    elementTemplates.put(template.getTid(), jsonId);
    put(template, jsonId, jsonObject);
  }

  public UUID getElementTemplate(int templateId) {
    return elementTemplates.get(templateId);
  }
}
