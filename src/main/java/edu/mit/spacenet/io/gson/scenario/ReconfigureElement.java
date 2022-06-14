package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class ReconfigureElement extends Event {

  protected UUID element;
  protected Integer stateIndex;

  public static ReconfigureElement createFrom(
      edu.mit.spacenet.simulator.event.ReconfigureEvent event, Context context) {
    ReconfigureElement e = new ReconfigureElement();
    e.name = event.getName();
    e.missionTime = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.element = context.getJsonIdFromJavaObject(event.getElement());
    e.stateIndex = new ArrayList<I_State>(event.getElement().getStates()).indexOf(event.getState());
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.ReconfigureEvent toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.ReconfigureEvent e =
        new edu.mit.spacenet.simulator.event.ReconfigureEvent();
    e.setName(name);
    e.setTime(missionTime.getPeriod().getDays()
        + missionTime.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    I_Element el = (I_Element) context.getJavaObjectFromJsonId(element);
    e.setElement(el);
    e.setState((I_State) new ArrayList<I_State>(el.getStates()).get(stateIndex));
    return e;
  }

}
