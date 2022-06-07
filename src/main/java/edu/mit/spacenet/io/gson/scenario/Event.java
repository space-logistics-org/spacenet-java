package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.simulator.event.EventType;
import edu.mit.spacenet.simulator.event.I_Event;

public abstract class Event {
  public static final BiMap<String, EventType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, EventType>().put("CreateElements", EventType.CREATE)
          .put("AddResources", EventType.ADD).put("MoveElements", EventType.MOVE)
          .put("TransferResources", EventType.TRANSFER).put("RemoveElements", EventType.REMOVE)
          .put("ReconfigureElement", EventType.RECONFIGURE)
          .put("ReconfigureElements", EventType.RECONFIGURE_GROUP)
          .put("ConsumeResources", EventType.DEMAND).put("PropulsiveBurn", EventType.BURN)
          .put("CrewedEVA", EventType.EVA).put("CrewedExploration", EventType.EXPLORATION)
          .put("SpaceTransport", EventType.SPACE_TRANSPORT)
          .put("SurfaceTransport", EventType.SURFACE_TRANSPORT)
          .put("FlightTransport", EventType.FLIGHT_TRANSPORT).build();

  protected String name;
  protected Integer priority;
  protected PeriodDuration mission_time;
  protected UUID location;

  public static Event createFrom(edu.mit.spacenet.simulator.event.I_Event event, Context context) {
    if (event.getEventType() == EventType.CREATE) {
      return CreateElements.createFrom((edu.mit.spacenet.simulator.event.CreateEvent) event,
          context);
    } else if (event.getEventType() == EventType.ADD) {
      return AddResources.createFrom((edu.mit.spacenet.simulator.event.AddEvent) event, context);
    } else if (event.getEventType() == EventType.MOVE) {
      return MoveElements.createFrom((edu.mit.spacenet.simulator.event.MoveEvent) event, context);
    } else if (event.getEventType() == EventType.TRANSFER) {
      return TransferResources.createFrom((edu.mit.spacenet.simulator.event.TransferEvent) event,
          context);
    } else if (event.getEventType() == EventType.REMOVE) {
      return RemoveElements.createFrom((edu.mit.spacenet.simulator.event.RemoveEvent) event,
          context);
    } else if (event.getEventType() == EventType.RECONFIGURE) {
      return ReconfigureElement
          .createFrom((edu.mit.spacenet.simulator.event.ReconfigureEvent) event, context);
    } else if (event.getEventType() == EventType.RECONFIGURE_GROUP) {
      return ReconfigureElements
          .createFrom((edu.mit.spacenet.simulator.event.ReconfigureGroupEvent) event, context);
    } else if (event.getEventType() == EventType.DEMAND) {
      return ConsumeResources.createFrom((edu.mit.spacenet.simulator.event.DemandEvent) event,
          context);
    } else if (event.getEventType() == EventType.BURN) {
      return BurnEvent.createFrom((edu.mit.spacenet.simulator.event.BurnEvent) event, context);
    } else if (event.getEventType() == EventType.EVA) {
      return EvaEvent.createFrom((edu.mit.spacenet.simulator.event.EvaEvent) event, context);
    } else if (event.getEventType() == EventType.EXPLORATION) {
      return Exploration.createFrom((edu.mit.spacenet.simulator.event.ExplorationProcess) event,
          context);
    } else if (event.getEventType() == EventType.SPACE_TRANSPORT) {
      return SpaceTransport.createFrom((edu.mit.spacenet.simulator.event.SpaceTransport) event,
          context);
    } else if (event.getEventType() == EventType.SURFACE_TRANSPORT) {
      return SurfaceTransport.createFrom((edu.mit.spacenet.simulator.event.SurfaceTransport) event,
          context);
    } else if (event.getEventType() == EventType.FLIGHT_TRANSPORT) {
      return FlightTransport.createFrom((edu.mit.spacenet.simulator.event.FlightTransport) event,
          context);
    } else {
      throw new UnsupportedOperationException("unknown event type: " + event.getEventType());
    }
  }

  public static List<Event> createFrom(Collection<edu.mit.spacenet.simulator.event.I_Event> events,
      Context context) {
    List<Event> es = new ArrayList<Event>();
    if (events != null) {
      for (I_Event e : events) {
        es.add(Event.createFrom(e, context));
      }
    }
    return es;
  }

  public abstract edu.mit.spacenet.simulator.event.I_Event toSpaceNet(Context context);

  public static List<edu.mit.spacenet.simulator.event.I_Event> toSpaceNet(Collection<Event> events,
      Context context) {
    List<edu.mit.spacenet.simulator.event.I_Event> es =
        new ArrayList<edu.mit.spacenet.simulator.event.I_Event>();
    for (Event e : events) {
      es.add(e.toSpaceNet(context));
    }
    return es;
  }
}
