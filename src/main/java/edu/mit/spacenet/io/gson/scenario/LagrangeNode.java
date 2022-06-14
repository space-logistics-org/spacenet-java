package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.network.node.Body;

public class LagrangeNode extends Node {
  protected String body2;
  protected Integer lpNumber;

  public static LagrangeNode createFrom(edu.mit.spacenet.domain.network.node.LagrangeNode node,
      Context context) {
    LagrangeNode n = new LagrangeNode();
    n.id = UUID.randomUUID();
    context.put(node, n.id, n);
    n.name = node.getName();
    n.description = node.getDescription();
    n.body1 = node.getBody().getName();
    n.body2 = node.getMinorBody().getName();
    n.lpNumber = node.getNumber();
    n.contents = context.getJsonIdsFromJavaObjects(node.getContents());
    return n;
  }

  public edu.mit.spacenet.domain.network.node.LagrangeNode toSpaceNet(Context context) {
    edu.mit.spacenet.domain.network.node.LagrangeNode n =
        new edu.mit.spacenet.domain.network.node.LagrangeNode();
    context.put(n, id, this);
    n.setTid(context.getJavaId(id));
    n.setName(name);
    n.setDescription(description);
    n.setBody(Body.getInstance(body1));
    n.setMinorBody(Body.getInstance(body2));
    n.setNumber(lpNumber);
    n.getContents().addAll(Element.toSpaceNetViaId(contents, context));
    return n;
  }
}
