package edu.mit.spacenet.io.gson.scenario;

import java.util.UUID;

import edu.mit.spacenet.domain.network.node.Body;

public class LagrangeNode extends Node {
  protected String body_2;
  protected Integer lp_number;

  public static LagrangeNode createFrom(edu.mit.spacenet.domain.network.node.LagrangeNode node,
      Context context) {
    LagrangeNode n = new LagrangeNode();
    n.id = UUID.randomUUID();
    context.put(node, n.id, n);
    n.name = node.getName();
    n.description = node.getDescription();
    n.body_1 = node.getBody().getName();
    n.body_2 = node.getMinorBody().getName();
    n.lp_number = node.getNumber();
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
    n.setBody(Body.getInstance(body_1));
    n.setMinorBody(Body.getInstance(body_2));
    n.setNumber(lp_number);
    n.getContents().addAll(Element.toSpaceNetViaId(contents, context));
    return n;
  }
}
