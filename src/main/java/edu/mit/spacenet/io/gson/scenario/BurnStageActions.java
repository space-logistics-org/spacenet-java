package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BurnStageActions {
  protected UUID burn;
  protected List<BurnStageAction> actions;

  public static BurnStageActions createFrom(edu.mit.spacenet.domain.network.edge.Burn burn,
      Collection<edu.mit.spacenet.simulator.event.BurnStageItem> actions, Context context) {
    BurnStageActions a = new BurnStageActions();
    a.burn = context.getJsonIdFromJavaObject(burn);
    a.actions = BurnStageAction.createFrom(actions, context);
    return a;
  }

  public static List<BurnStageActions> createFrom(
      List<edu.mit.spacenet.domain.network.edge.Burn> burns,
      List<List<edu.mit.spacenet.simulator.event.BurnStageItem>> sequence, Context context) {
    List<BurnStageActions> as = new ArrayList<BurnStageActions>();
    for (int i = 0; i < burns.size(); i++) {
      as.add(BurnStageActions.createFrom(burns.get(i), sequence.get(i), context));
    }
    return as;
  }

  public List<edu.mit.spacenet.simulator.event.BurnStageItem> toSpaceNet(Context context) {
    List<edu.mit.spacenet.simulator.event.BurnStageItem> as =
        new ArrayList<edu.mit.spacenet.simulator.event.BurnStageItem>();
    for (BurnStageAction a : actions) {
      as.add(a.toSpaceNet(context));
    }
    return as;

  }

  public static List<List<edu.mit.spacenet.simulator.event.BurnStageItem>> toSpaceNet(
      SpaceEdge edge, Collection<BurnStageActions> sequence, Context context) {
    List<List<edu.mit.spacenet.simulator.event.BurnStageItem>> as =
        new ArrayList<List<edu.mit.spacenet.simulator.event.BurnStageItem>>();
    for (int i = 0; i < edge.burns.size(); i++) {
      BurnStageActions matchingActions = null;
      for (BurnStageActions actions : sequence) {
        if (actions.burn.equals(edge.burns.get(i).id)) {
          matchingActions = actions;
        }
      }
      if (matchingActions != null) {
        as.add(matchingActions.toSpaceNet(context));
      } else {
        as.add(new ArrayList<edu.mit.spacenet.simulator.event.BurnStageItem>());
      }
    }
    return as;
  }
}
