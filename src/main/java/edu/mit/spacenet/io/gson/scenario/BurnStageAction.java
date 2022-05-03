package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.simulator.event.BurnStageItem;

public class BurnStageAction {
  public static final BiMap<String, String> TYPE_MAP = new ImmutableBiMap.Builder<String, String>()
      .put("Burn", BurnStageItem.BURN).put("Stage", BurnStageItem.STAGE).build();

  protected String type;
  protected UUID element;

  public static BurnStageAction createFrom(edu.mit.spacenet.simulator.event.BurnStageItem action,
      Context context) {
    BurnStageAction a = new BurnStageAction();
    a.type = TYPE_MAP.inverse().get(action.getBurnStage());
    a.element = context.getJsonIdFromJavaObject(action.getElement());
    return a;
  }

  public static List<BurnStageAction> createFrom(
      Collection<edu.mit.spacenet.simulator.event.BurnStageItem> actions, Context context) {
    List<BurnStageAction> as = new ArrayList<BurnStageAction>();
    for (edu.mit.spacenet.simulator.event.BurnStageItem action : actions) {
      as.add(BurnStageAction.createFrom(action, context));
    }
    return as;
  }

  public edu.mit.spacenet.simulator.event.BurnStageItem toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.BurnStageItem a =
        new edu.mit.spacenet.simulator.event.BurnStageItem();
    a.setBurnStage(TYPE_MAP.get(type));
    a.setElement((I_Element) context.getJavaObjectFromJsonId(element));
    return a;
  }

  public static List<edu.mit.spacenet.simulator.event.BurnStageItem> actionsToSpaceNet(
      Collection<BurnStageAction> actions, Context context) {
    List<edu.mit.spacenet.simulator.event.BurnStageItem> as =
        new ArrayList<edu.mit.spacenet.simulator.event.BurnStageItem>();
    for (BurnStageAction a : actions) {
      as.add(a.toSpaceNet(context));
    }
    return as;
  }
}
