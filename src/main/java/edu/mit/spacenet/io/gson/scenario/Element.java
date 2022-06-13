package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

public class Element implements Cloneable {
  public static final BiMap<String, ElementType> TYPE_MAP =
      new ImmutableBiMap.Builder<String, ElementType>().put("Element", ElementType.ELEMENT)
          .put("Crew Member", ElementType.CREW_MEMBER)
          .put("Resource Container", ElementType.RESOURCE_CONTAINER)
          .put("Resource Tank", ElementType.RESOURCE_TANK).put("Element Carrier", ElementType.CARRIER)
          .put("Propulsive Vehicle", ElementType.PROPULSIVE_VEHICLE)
          .put("Surface Vehicle", ElementType.SURFACE_VEHICLE).build();

  protected UUID id;
  protected UUID templateId;
  protected String name;
  protected String description;
  protected Double accommodatationMass;
  protected Double mass;
  protected Double volume;
  protected Integer classOfSupply;
  protected String environment;
  protected List<State> states;
  protected Integer currentStateIndex;
  protected List<Part> parts;
  protected String icon;

  public static Element createFrom(I_Element element, Context context) {
    if (element.getElementType() == ElementType.ELEMENT) {
      Element e = new Element();
      e.id = UUID.randomUUID();
      context.put(element, e.id, e);
      e.templateId = context.getElementTemplate(element.getTid());
      Element template = (Element) context.getJsonObject(e.templateId);
      if (template == null) {
        e.name = element.getName();
        e.description = element.getDescription();
        e.accommodatationMass = element.getAccommodationMass();
        e.mass = element.getMass();
        e.volume = element.getVolume();
        e.classOfSupply = element.getClassOfSupply().getId();
        e.environment = element.getEnvironment().getName();
        if (element.getIconType() != element.getElementType().getIconType()) {
          e.icon = element.getIconType().getName();
        }
        e.states = State.createFrom(element.getStates(), context);
        if (element.getCurrentState() != null) {
          e.currentStateIndex =
              new ArrayList<I_State>(element.getStates()).indexOf(element.getCurrentState());
        }
        e.parts = Part.createFrom(element.getParts(), context);
      } else {
        if (!template.name.equals(element.getName())) {
          e.name = element.getName();
        }
        if (!template.description.equals(element.getDescription())) {
          e.description = element.getDescription();
        }
        if (!template.accommodatationMass.equals(element.getAccommodationMass())) {
          e.accommodatationMass = element.getAccommodationMass();
        }
        if (!template.mass.equals(element.getMass())) {
          e.mass = element.getMass();
        }
        if (!template.volume.equals(element.getVolume())) {
          e.volume = element.getVolume();
        }
        if (!template.classOfSupply.equals(element.getClassOfSupply().getId())) {
          e.classOfSupply = element.getClassOfSupply().getId();
        }
        if (!template.environment.equals(element.getEnvironment().getName())) {
          e.environment = element.getEnvironment().getName();
        }
        if ((template.icon == null
            && element.getIconType() != element.getElementType().getIconType())
            || (template.icon != null && !template.icon.equals(element.getIconType().getName()))) {
          e.icon = element.getIconType().getName();
        }
        List<State> states = State.createFrom(element.getStates(), context);
        if (!template.states.equals(states)) {
          e.states = states;
        }
        if (e.states != null || element.getCurrentState() != null) {
          List<I_State> eStates = new ArrayList<I_State>(element.getStates());
          if (!template.currentStateIndex.equals(eStates.indexOf(element.getCurrentState()))) {
            e.currentStateIndex = eStates.indexOf(element.getCurrentState());
          }
        }
        List<Part> parts = Part.createFrom(element.getParts(), context);
        if (!template.parts.equals(parts)) {
          e.parts = parts;
        }
      }
      return e;
    } else if (element.getElementType() == ElementType.RESOURCE_CONTAINER) {
      return ResourceContainer
          .createFrom((edu.mit.spacenet.domain.element.ResourceContainer) element, context);
    } else if (element.getElementType() == ElementType.RESOURCE_TANK) {
      return ResourceTank.createFrom((edu.mit.spacenet.domain.element.ResourceTank) element,
          context);
    } else if (element.getElementType() == ElementType.CARRIER) {
      return Carrier.createFrom((edu.mit.spacenet.domain.element.Carrier) element, context);
    } else if (element.getElementType() == ElementType.PROPULSIVE_VEHICLE) {
      return PropulsiveVehicle
          .createFrom((edu.mit.spacenet.domain.element.PropulsiveVehicle) element, context);
    } else if (element.getElementType() == ElementType.SURFACE_VEHICLE) {
      return SurfaceVehicle.createFrom((edu.mit.spacenet.domain.element.SurfaceVehicle) element,
          context);
    } else if (element.getElementType() == ElementType.CREW_MEMBER) {
      return CrewMember.createFrom((edu.mit.spacenet.domain.element.CrewMember) element, context);
    } else {
      throw new UnsupportedOperationException("unknown element type: " + element.getElementType());
    }
  }

  public static List<Element> createFrom(Collection<? extends I_Element> elements,
      Context context) {
    List<Element> es = new ArrayList<Element>();
    for (I_Element e : elements) {
      es.add(Element.createFrom(e, context));
    }
    return es;
  }

  public edu.mit.spacenet.domain.element.Element toSpaceNet(Context context) {
    edu.mit.spacenet.domain.element.Element e = new edu.mit.spacenet.domain.element.Element();
    context.put(e, id, this);
    e.setUid(context.getJavaId(id));
    e.setTid(templateId == null ? context.getJavaId(id) : context.getJavaId(templateId));
    Element template = (Element) context.getJsonObject(templateId);
    e.setName(name == null ? template.name : name);
    e.setDescription(description == null ? template.description : description);
    e.setAccommodationMass(
        accommodatationMass == null ? template.accommodatationMass : accommodatationMass);
    e.setMass(mass == null ? template.mass : mass);
    e.setVolume(volume == null ? template.volume : volume);
    e.setClassOfSupply(
        ClassOfSupply.getInstance(classOfSupply == null ? template.classOfSupply : classOfSupply));
    e.setEnvironment(
        Environment.getInstance(environment == null ? template.environment : environment));
    e.setIconType(ElementIcon.getInstance(icon == null && template != null ? template.icon : icon));
    List<State> _states = states;
    if (states == null) {
      _states = State.clone(template.states);
    }
    e.setStates(State.toSpaceNet(e, states == null ? _states : states, context));
    if (currentStateIndex == null) {
      if (template != null && template.currentStateIndex != null) {
        e.setCurrentState(
            (I_State) context.getJavaObjectFromJsonId(_states.get(template.currentStateIndex).id));
      }
    } else {
      e.setCurrentState(
          (I_State) context.getJavaObjectFromJsonId(_states.get(currentStateIndex).id));
    }
    e.setParts(Part.toSpaceNet(parts == null ? template.parts : parts, context));
    return e;
  }

  public ElementPreview getPreview(Context context) {
    return new ElementPreview(context.getJavaId(id), name, ElementType.ELEMENT,
        ElementIcon.getInstance(icon));
  }

  public static SortedSet<I_Element> toSpaceNet(Collection<Element> elements, Context context) {
    SortedSet<I_Element> es = new TreeSet<I_Element>();
    if (elements != null) {
      for (Element e : elements) {
        es.add(e.toSpaceNet(context));
      }
    }
    return es;
  }

  public static SortedSet<I_Element> toSpaceNetViaId(Collection<UUID> elements, Context context) {
    SortedSet<I_Element> es = new TreeSet<I_Element>();
    if (elements != null) {
      for (UUID uuid : elements) {
        es.add(((I_Element) context.getJavaObjectFromJsonId(uuid)));
      }
    }
    return es;
  }

  @Override
  public Element clone() {
    Element e = new Element();
    e.id = UUID.randomUUID();
    e.templateId = templateId;
    e.name = name;
    e.description = description;
    e.accommodatationMass = accommodatationMass;
    e.mass = mass;
    e.volume = volume;
    e.classOfSupply = classOfSupply;
    e.environment = environment;
    e.states = State.clone(states);
    e.currentStateIndex = currentStateIndex;
    e.parts = Part.clone(parts);
    e.icon = icon;
    return e;
  }

  public static List<Element> clone(Collection<? extends Element> elements) {
    List<Element> es = new ArrayList<Element>();
    for (Element e : elements) {
      es.add(e.clone());
    }
    return es;
  }
}
