package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class SpaceTransport extends Event {
  protected UUID edge;
  protected List<UUID> elements;
  protected List<BurnStageActions> burnStageSequence;

  public static SpaceTransport createFrom(edu.mit.spacenet.simulator.event.SpaceTransport event,
      Context context) {
    SpaceTransport e = new SpaceTransport();
    e.name = event.getName();
    e.mission_time = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.edge = context.getJsonIdFromJavaObject(event.getEdge());
    e.elements = context.getJsonIdsFromJavaObjects(event.getElements());
    e.burnStageSequence = BurnStageActions.createFrom(event.getEdge().getBurns(),
        event.getBurnStageSequence(), context);
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.SpaceTransport toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.SpaceTransport e =
        new edu.mit.spacenet.simulator.event.SpaceTransport();
    e.setName(name);
    e.setTime(mission_time.getPeriod().getDays()
        + mission_time.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    e.setEdge(
        (edu.mit.spacenet.domain.network.edge.SpaceEdge) context.getJavaObjectFromJsonId(edge));
    e.setElements(Element.toSpaceNetViaId(elements, context));
    e.getBurnStageSequence().clear();
    e.getBurnStageSequence().addAll(BurnStageActions
        .toSpaceNet((SpaceEdge) context.getJsonObject(edge), burnStageSequence, context));
    return e;
  }

}
