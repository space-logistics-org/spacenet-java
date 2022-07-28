package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import org.threeten.extra.PeriodDuration;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class EvaEvent extends Event {
  protected UUID vehicle;
  protected PeriodDuration evaDuration;
  protected List<UUID> elements;
  protected List<ElementState> elementStates;
  protected List<Resource> additionalDemands;

  public static EvaEvent createFrom(edu.mit.spacenet.simulator.event.EvaEvent event,
      Context context) {
    EvaEvent e = new EvaEvent();
    e.name = event.getName();
    e.missionTime = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.vehicle = context.getJsonIdFromJavaObject(event.getVehicle());
    e.evaDuration = PeriodDuration.of(Period.ofDays((int) (event.getEvaDuration() / 24d)),
        Duration
            .ofSeconds((long) ((event.getEvaDuration() / 24d - (int) (event.getEvaDuration() / 24d))
                * 24 * 60 * 60)));
    e.elementStates = new ArrayList<ElementState>();
    for (I_Element element : event.getStateMap().keySet()) {
      e.elementStates
          .add(ElementState.createFrom(element, event.getStateMap().get(element), context));
    }
    e.additionalDemands = Resource.createFrom(event.getDemands(), context);
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.EvaEvent toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.EvaEvent e = new edu.mit.spacenet.simulator.event.EvaEvent();
    e.setName(name);
    e.setTime(missionTime.getPeriod().getDays()
        + missionTime.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    e.setVehicle((I_Carrier) context.getJavaObjectFromJsonId(vehicle));
    SortedMap<I_Element, I_State> stateMap = new TreeMap<I_Element, I_State>();
    for (ElementState elementState : elementStates) {
      if (elementState.stateIndex == -1) {
        stateMap.put((I_Element) context.getJavaObjectFromJsonId(elementState.element), null);
      } else {
        stateMap.put((I_Element) context.getJavaObjectFromJsonId(elementState.element),
            (I_State) context.getJavaObjectFromJsonId(
                ((Element) context.getJsonObject(elementState.element)).states
                    .get(elementState.stateIndex).id));
      }
    }
    e.setStateMap(stateMap);
    e.setEvaDuration(evaDuration.getPeriod().getDays() * 24
        + evaDuration.getDuration().getSeconds() / (60 * 60d));
    return e;
  }

}
