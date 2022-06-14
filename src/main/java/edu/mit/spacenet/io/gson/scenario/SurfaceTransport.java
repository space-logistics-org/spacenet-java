package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import edu.mit.spacenet.domain.element.I_State;

public class SurfaceTransport extends Event {
  protected Double dutyCycle;
  protected UUID vehicle;
  protected UUID transportState;
  protected Double speed;
  protected UUID edge;

  public static SurfaceTransport createFrom(edu.mit.spacenet.simulator.event.SurfaceTransport event,
      Context context) {
    SurfaceTransport e = new SurfaceTransport();
    e.name = event.getName();
    e.missionTime = PeriodDuration.of(Period.ofDays((int) event.getTime()),
        Duration.ofSeconds((long) ((event.getTime() - (int) event.getTime()) * 24 * 60 * 60)));
    e.priority = event.getPriority();
    e.location = context.getJsonIdFromJavaObject(event.getLocation());
    e.dutyCycle = event.getDutyCycle();
    e.vehicle = context.getJsonIdFromJavaObject(event.getVehicle());
    e.transportState = context.getJsonIdFromJavaObject(event.getTransportState());
    e.speed = event.getSpeed();
    e.edge = context.getJsonIdFromJavaObject(event.getEdge());
    return e;
  }

  @Override
  public edu.mit.spacenet.simulator.event.SurfaceTransport toSpaceNet(Context context) {
    edu.mit.spacenet.simulator.event.SurfaceTransport e =
        new edu.mit.spacenet.simulator.event.SurfaceTransport();
    e.setName(name);
    e.setTime(missionTime.getPeriod().getDays()
        + missionTime.getDuration().getSeconds() / (24 * 60 * 60d));
    e.setPriority(priority);
    e.setLocation(
        (edu.mit.spacenet.domain.network.Location) context.getJavaObjectFromJsonId(location));
    e.setDutyCycle(dutyCycle);
    e.setVehicle(
        (edu.mit.spacenet.domain.element.SurfaceVehicle) context.getJavaObjectFromJsonId(vehicle));
    e.setTransportState((I_State) context.getJavaObjectFromJsonId(transportState));
    e.setSpeed(speed);
    e.setEdge(
        (edu.mit.spacenet.domain.network.edge.SurfaceEdge) context.getJavaObjectFromJsonId(edge));
    return e;
  }

}
