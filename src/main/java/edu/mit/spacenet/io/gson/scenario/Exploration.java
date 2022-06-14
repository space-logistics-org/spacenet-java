package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import org.threeten.extra.PeriodDuration;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class Exploration extends Event {
  protected UUID vehicle;
  protected PeriodDuration duration;
  protected Double evaPerWeek;
  protected PeriodDuration evaDuration;
  protected Map<UUID, Integer> elementStates;
  protected List<Resource> additionalDemands;

  public static Exploration createFrom(edu.mit.spacenet.simulator.event.ExplorationProcess event,
      Context context) {
    Exploration e = new Exploration();
    e.name = event.getName();
    e.missionTime = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.duration = PeriodDuration.of(Period.ofDays((int) event.getDuration()), Duration
        .ofSeconds((long) ((event.getDuration() - (int) event.getDuration()) * 24 * 60 * 60)));
    e.evaPerWeek = event.getEvaPerWeek();
    e.vehicle = context.getJsonIdFromJavaObject(event.getVehicle());
    e.evaDuration =
        PeriodDuration.of(Period.ofDays((int) event.getEvaDuration()), Duration.ofSeconds(
            (long) ((event.getEvaDuration() - (int) event.getEvaDuration()) * 24 * 60 * 60)));
    e.elementStates = new HashMap<UUID, Integer>();
    for (I_Element element : event.getStateMap().keySet()) {
      if (event.getStateMap().get(element) == null) {
        e.elementStates.put(context.getJsonIdFromJavaObject(element), -1);
      } else {
        e.elementStates.put(context.getJsonIdFromJavaObject(element),
            new ArrayList<I_State>(element.getStates()).indexOf(event.getStateMap().get(element)));
      }
    }
    e.additionalDemands = Resource.createFrom(event.getDemands(), context);
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.ExplorationProcess toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.ExplorationProcess e =
        new edu.mit.spacenet.simulator.event.ExplorationProcess();
    e.setName(name);
    e.setTime(missionTime.getPeriod().getDays()
        + missionTime.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    e.setVehicle((I_Carrier) context.getJavaObjectFromJsonId(vehicle));
    SortedMap<I_Element, I_State> stateMap = new TreeMap<I_Element, I_State>();
    for (UUID element : elementStates.keySet()) {
      if (elementStates.get(element).equals(-1)) {
        stateMap.put((I_Element) context.getJavaObjectFromJsonId(element), null);
      } else {
        stateMap.put((I_Element) context.getJavaObjectFromJsonId(element),
            (I_State) context
                .getJavaObjectFromJsonId(((Element) context.getJsonObject(element)).states
                    .get(elementStates.get(element)).id));
      }
    }
    e.setStateMap(stateMap);
    e.setDuration(
        duration.getPeriod().getDays() + duration.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setEvaPerWeek(evaPerWeek);
    e.setEvaDuration(evaDuration.getPeriod().getDays()
        + evaDuration.getDuration().getSeconds() / (24 * 60 * 60d));
    return e;
  }

}
