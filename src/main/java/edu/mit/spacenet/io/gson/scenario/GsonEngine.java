package edu.mit.spacenet.io.gson.scenario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.threeten.extra.PeriodDuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.gson.typeadapters.UtcDateTypeAdapter;

import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.EventType;
import edu.mit.spacenet.util.GlobalParameters;

public abstract class GsonEngine {
  public static Scenario openScenario(String filePath) throws FileNotFoundException, IOException {
    BufferedReader in = new BufferedReader(new FileReader(filePath));
    Scenario scenario =
        getGson().fromJson(in, edu.mit.spacenet.io.gson.scenario.Scenario.class).toSpaceNet();
    in.close();
    GlobalParameters.getSingleton().setParametersFrom(scenario);
    return scenario;
  }

  public static void saveScenario(Scenario scenario) throws FileNotFoundException, IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(scenario.getFilePath()));
    getGson().toJson(edu.mit.spacenet.io.gson.scenario.Scenario.createFrom(scenario), out);
    out.close();
  }

  private static Gson getGson() {
    RuntimeTypeAdapterFactory<Location> locationAdapterFactory =
        RuntimeTypeAdapterFactory.of(Location.class, "type")
            .registerSubtype(SurfaceNode.class, Node.TYPE_MAP.inverse().get(NodeType.SURFACE))
            .registerSubtype(OrbitalNode.class, Node.TYPE_MAP.inverse().get(NodeType.ORBITAL))
            .registerSubtype(LagrangeNode.class, Node.TYPE_MAP.inverse().get(NodeType.LAGRANGE))
            .registerSubtype(SurfaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SURFACE))
            .registerSubtype(SpaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SPACE))
            .registerSubtype(FlightEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.FLIGHT));
    RuntimeTypeAdapterFactory<Node> nodeAdapterFactory =
        RuntimeTypeAdapterFactory.of(Node.class, "type")
            .registerSubtype(SurfaceNode.class, Node.TYPE_MAP.inverse().get(NodeType.SURFACE))
            .registerSubtype(OrbitalNode.class, Node.TYPE_MAP.inverse().get(NodeType.ORBITAL))
            .registerSubtype(LagrangeNode.class, Node.TYPE_MAP.inverse().get(NodeType.LAGRANGE));
    RuntimeTypeAdapterFactory<Edge> edgeAdapterFactory =
        RuntimeTypeAdapterFactory.of(Edge.class, "type")
            .registerSubtype(SurfaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SURFACE))
            .registerSubtype(SpaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SPACE))
            .registerSubtype(FlightEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.FLIGHT));
    RuntimeTypeAdapterFactory<Event> eventAdapterFactory = RuntimeTypeAdapterFactory
        .of(Event.class, "type")
        .registerSubtype(CreateElements.class, Event.TYPE_MAP.inverse().get(EventType.CREATE))
        .registerSubtype(MoveElements.class, Event.TYPE_MAP.inverse().get(EventType.MOVE))
        .registerSubtype(RemoveElements.class, Event.TYPE_MAP.inverse().get(EventType.REMOVE))
        .registerSubtype(AddResources.class, Event.TYPE_MAP.inverse().get(EventType.ADD))
        .registerSubtype(ConsumeResources.class, Event.TYPE_MAP.inverse().get(EventType.DEMAND))
        .registerSubtype(TransferResources.class, Event.TYPE_MAP.inverse().get(EventType.TRANSFER))
        .registerSubtype(BurnEvent.class, Event.TYPE_MAP.inverse().get(EventType.BURN))
        .registerSubtype(ReconfigureElement.class,
            Event.TYPE_MAP.inverse().get(EventType.RECONFIGURE))
        .registerSubtype(ReconfigureElements.class,
            Event.TYPE_MAP.inverse().get(EventType.RECONFIGURE_GROUP))
        .registerSubtype(EvaEvent.class, Event.TYPE_MAP.inverse().get(EventType.EVA))
        .registerSubtype(Exploration.class, Event.TYPE_MAP.inverse().get(EventType.EXPLORATION))
        .registerSubtype(SpaceTransport.class,
            Event.TYPE_MAP.inverse().get(EventType.SPACE_TRANSPORT))
        .registerSubtype(SurfaceTransport.class,
            Event.TYPE_MAP.inverse().get(EventType.SURFACE_TRANSPORT))
        .registerSubtype(FlightTransport.class,
            Event.TYPE_MAP.inverse().get(EventType.FLIGHT_TRANSPORT));
    RuntimeTypeAdapterFactory<DemandModel> demandModelAdapterFactory =
        RuntimeTypeAdapterFactory.of(DemandModel.class, "type")
            .registerSubtype(RatedDemandModel.class,
                DemandModel.TYPE_MAP.inverse().get(DemandModelType.RATED))
            .registerSubtype(ImpulseDemandModel.class,
                DemandModel.TYPE_MAP.inverse().get(DemandModelType.TIMED_IMPULSE))
            .registerSubtype(SparingByMassDemandModel.class,
                DemandModel.TYPE_MAP.inverse().get(DemandModelType.SPARING_BY_MASS))
            .registerSubtype(ConsumablesDemandModel.class,
                DemandModel.TYPE_MAP.inverse().get(DemandModelType.CREW_CONSUMABLES));
    RuntimeTypeAdapterFactory<Element> elementAdapterFactory = RuntimeTypeAdapterFactory
        .of(Element.class, "type")
        .registerSubtype(Element.class, Element.TYPE_MAP.inverse().get(ElementType.ELEMENT))
        .registerSubtype(CrewMember.class, Element.TYPE_MAP.inverse().get(ElementType.CREW_MEMBER))
        .registerSubtype(ResourceContainer.class,
            Element.TYPE_MAP.inverse().get(ElementType.RESOURCE_CONTAINER))
        .registerSubtype(ResourceTank.class,
            Element.TYPE_MAP.inverse().get(ElementType.RESOURCE_TANK))
        .registerSubtype(Carrier.class, Element.TYPE_MAP.inverse().get(ElementType.CARRIER))
        .registerSubtype(PropulsiveVehicle.class,
            Element.TYPE_MAP.inverse().get(ElementType.PROPULSIVE_VEHICLE))
        .registerSubtype(SurfaceVehicle.class,
            Element.TYPE_MAP.inverse().get(ElementType.SURFACE_VEHICLE));

    return new GsonBuilder().registerTypeAdapter(Date.class, new UtcDateTypeAdapter())
        .registerTypeAdapter(PeriodDuration.class, new PeriodDurationTypeAdpater())
        .registerTypeAdapterFactory(locationAdapterFactory)
        .registerTypeAdapterFactory(nodeAdapterFactory)
        .registerTypeAdapterFactory(edgeAdapterFactory)
        .registerTypeAdapterFactory(eventAdapterFactory)
        .registerTypeAdapterFactory(demandModelAdapterFactory)
        .registerTypeAdapterFactory(elementAdapterFactory).setPrettyPrinting().create();
  }
}
