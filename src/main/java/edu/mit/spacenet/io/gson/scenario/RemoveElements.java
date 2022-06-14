package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class RemoveElements extends Event {

  protected List<UUID> elements = new ArrayList<UUID>();

  public static RemoveElements createFrom(edu.mit.spacenet.simulator.event.RemoveEvent event,
      Context context) {
    RemoveElements e = new RemoveElements();
    e.name = event.getName();
    e.missionTime = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.elements = context.getJsonIdsFromJavaObjects(event.getElements());
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.RemoveEvent toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.RemoveEvent e =
        new edu.mit.spacenet.simulator.event.RemoveEvent();
    e.setName(name);
    e.setTime(missionTime.getPeriod().getDays()
        + missionTime.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    e.setElements(Element.toSpaceNetViaId(elements, context));
    return e;
  }

}
