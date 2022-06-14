package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.network.node.Body;

public class OrbitalNode extends Node {
  protected double inclination;
  protected double periapsis;
  protected double apoapsis;

  public static OrbitalNode createFrom(edu.mit.spacenet.domain.network.node.OrbitalNode node,
      Context context) {
    OrbitalNode n = new OrbitalNode();
    n.id = UUID.randomUUID();
    context.put(node, n.id, n);
    n.name = node.getName();
    n.description = node.getDescription();
    n.body1 = node.getBody().getName();
    n.inclination = node.getInclination();
    n.periapsis = node.getPeriapsis();
    n.apoapsis = node.getApoapsis();
    n.contents = context.getJsonIdsFromJavaObjects(node.getContents());
    return n;
  }

  public edu.mit.spacenet.domain.network.node.OrbitalNode toSpaceNet(Context context) {
    edu.mit.spacenet.domain.network.node.OrbitalNode n =
        new edu.mit.spacenet.domain.network.node.OrbitalNode();
    context.put(n, id, this);
    n.setTid(context.getJavaId(id));
    n.setName(name);
    n.setDescription(description);
    n.setBody(Body.getInstance(body1));
    n.setInclination(inclination);
    n.setPeriapsis(periapsis);
    n.setApoapsis(apoapsis);
    n.getContents().addAll(Element.toSpaceNetViaId(contents, context));
    return n;
  }
}
