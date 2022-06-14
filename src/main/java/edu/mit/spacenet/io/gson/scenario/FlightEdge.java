package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class FlightEdge extends Edge {
  protected PeriodDuration duration;
  protected Integer maxCrew;
  protected Double maxCargo;

  public static FlightEdge createFrom(edu.mit.spacenet.domain.network.edge.FlightEdge edge,
      Context context) {
    FlightEdge e = new FlightEdge();
    e.id = UUID.randomUUID();
    context.put(edge, e.id, e);
    e.name = edge.getName();
    e.description = edge.getDescription();
    e.originId = context.getJsonIdFromJavaObject(edge.getOrigin());
    e.destinationId = context.getJsonIdFromJavaObject(edge.getDestination());
    e.duration = PeriodDuration.of(Period.ofDays((int) edge.getDuration()), Duration
        .ofSeconds((long) ((edge.getDuration() - (int) edge.getDuration()) * 24 * 60 * 60)));
    e.maxCrew = edge.getMaxCrewSize();
    e.maxCargo = edge.getMaxCargoMass();
    e.contents = context.getJsonIdsFromJavaObjects(edge.getContents());
    return e;
  }

  public edu.mit.spacenet.domain.network.edge.FlightEdge toSpaceNet(Context context) {
    edu.mit.spacenet.domain.network.edge.FlightEdge e =
        new edu.mit.spacenet.domain.network.edge.FlightEdge();
    context.put(e, id, this);
    e.setTid(context.getJavaId(id));
    e.setName(name);
    e.setDescription(description);
    e.setOrigin(
        (edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(originId));
    e.setDestination(
        (edu.mit.spacenet.domain.network.node.Node) context.getJavaObjectFromJsonId(destinationId));
    e.setDuration(
        duration.getPeriod().getDays() + duration.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setMaxCrewSize(maxCrew);
    e.setMaxCargoMass(maxCargo);
    e.getContents().addAll(Element.toSpaceNetViaId(contents, context));
    return e;
  }
}
