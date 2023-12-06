package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class PropulsiveVehicle extends Carrier {
  protected Double isp;
  protected Resource fuel;
  protected Double maxFuel;

  public static PropulsiveVehicle createFrom(
      edu.mit.spacenet.domain.element.PropulsiveVehicle element, Context context) {
    PropulsiveVehicle e = new PropulsiveVehicle();
    e.id = UUID.randomUUID();
    context.put(element, e.id, e);
    e.templateId = context.getElementTemplate(element.getTid());
    PropulsiveVehicle template = (PropulsiveVehicle) context.getJsonObject(e.templateId);
    if (template == null) {
      e.name = element.getName();
      e.description = element.getDescription();
      e.accommodationMass = element.getAccommodationMass();
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

      e.maxCargoMass = element.getMaxCargoMass();
      e.maxCargoVolume = element.getMaxCargoVolume();
      e.cargoEnvironment = element.getCargoEnvironment().getName();
      e.maxCrew = element.getMaxCrewSize();
      e.contents = Element.createFrom(element.getContents(), context);
      e.fuel = new Resource();
      if (element.getOmsFuelTank() != null) {
        e.isp = element.getOmsIsp();
        if (element.getOmsFuelTank().getResource()
            .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
          e.fuel.classOfSupply = element.getOmsFuelTank().getResource().getClassOfSupply().getId();
          e.fuel.environment = element.getOmsFuelTank().getResource().getEnvironment().getName();
        } else {
          e.fuel.resource = context.getJsonIdFromJavaObject(element.getOmsFuelTank().getResource());
        }
        e.fuel.amount = element.getOmsFuelTank().getAmount();
        e.maxFuel = element.getOmsFuelTank().getMaxAmount();
      } else if (element.getRcsFuelTank() != null) {
        e.isp = element.getRcsIsp();
        if (element.getRcsFuelTank().getResource()
            .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
          e.fuel.classOfSupply = element.getRcsFuelTank().getResource().getClassOfSupply().getId();
          e.fuel.environment = element.getRcsFuelTank().getResource().getEnvironment().getName();
        } else {
          e.fuel.resource = context.getJsonIdFromJavaObject(element.getRcsFuelTank().getResource());
        }
        e.fuel.amount = element.getRcsFuelTank().getAmount();
        e.maxFuel = element.getRcsFuelTank().getMaxAmount();
      }
    } else {
      if (!template.name.equals(element.getName())) {
        e.name = element.getName();
      }
      if (!template.description.equals(element.getDescription())) {
        e.description = element.getDescription();
      }
      if (!template.accommodationMass.equals(element.getAccommodationMass())) {
        e.accommodationMass = element.getAccommodationMass();
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
      if ((template.icon == null && element.getIconType() != element.getElementType().getIconType())
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
      if (!template.maxCargoMass.equals(element.getMaxCargoMass())) {
        e.maxCargoMass = element.getMaxCargoMass();
      }
      if (!template.maxCargoVolume.equals(element.getMaxCargoVolume())) {
        e.maxCargoVolume = element.getMaxCargoVolume();
      }
      if (!template.cargoEnvironment.equals(element.getCargoEnvironment().getName())) {
        e.cargoEnvironment = element.getCargoEnvironment().getName();
      }
      if (!template.maxCrew.equals(element.getMaxCrewSize())) {
        e.maxCrew = element.getMaxCrewSize();
      }
      List<Element> contents = Element.createFrom(element.getContents(), context);
      if (!template.contents.equals(contents)) {
        e.contents = contents;
      }
      if (!template.isp
          .equals(element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp())) {
        e.isp = element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp();
      }
      Resource fuel = new Resource();
      if (element.getOmsFuelTank() != null) {
        if (element.getOmsFuelTank().getResource()
            .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
          fuel.classOfSupply = element.getOmsFuelTank().getResource().getClassOfSupply().getId();
          fuel.environment = element.getOmsFuelTank().getResource().getEnvironment().getName();
        } else {
          fuel.resource = context.getJsonIdFromJavaObject(element.getOmsFuelTank().getResource());
        }
        fuel.amount = element.getOmsFuelTank().getAmount();
        if (!template.maxFuel.equals(element.getOmsFuelTank().getMaxAmount())) {
          e.maxFuel = element.getOmsFuelTank().getMaxAmount();
        }
      } else if (element.getRcsFuelTank() != null) {
        if (element.getRcsFuelTank().getResource()
            .getResourceType() == edu.mit.spacenet.domain.resource.ResourceType.GENERIC) {
          fuel.classOfSupply = element.getRcsFuelTank().getResource().getClassOfSupply().getId();
          fuel.environment = element.getRcsFuelTank().getResource().getEnvironment().getName();
        } else {
          fuel.resource = context.getJsonIdFromJavaObject(element.getRcsFuelTank().getResource());
        }
        fuel.amount = element.getRcsFuelTank().getAmount();
        if (!template.maxFuel.equals(element.getRcsFuelTank().getMaxAmount())) {
          e.maxFuel = element.getRcsFuelTank().getMaxAmount();
        }
      }
      if (!template.fuel.equals(fuel)) {
        e.fuel = fuel;
      }
    }
    return e;
  }

  @Override
  public edu.mit.spacenet.domain.element.PropulsiveVehicle toSpaceNet(Context context) {
    edu.mit.spacenet.domain.element.PropulsiveVehicle e =
        new edu.mit.spacenet.domain.element.PropulsiveVehicle();
    context.put(e, id, this);
    e.setUid(context.getJavaId(id));
    e.setTid(templateId == null ? context.getJavaId(id) : context.getJavaId(templateId));
    PropulsiveVehicle template = (PropulsiveVehicle) context.getJsonObject(templateId);
    e.setName(name == null ? template.name : name);
    e.setDescription(description == null ? template.description : description);
    e.setAccommodationMass(
        accommodationMass == null ? template.accommodationMass : accommodationMass);
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

    e.setMaxCargoMass(maxCargoMass == null ? template.maxCargoMass : maxCargoMass);
    e.setMaxCargoVolume(maxCargoVolume == null ? template.maxCargoVolume : maxCargoVolume);
    e.setCargoEnvironment(Environment
        .getInstance(cargoEnvironment == null ? template.cargoEnvironment : cargoEnvironment));
    e.setMaxCrewSize(maxCrew == null ? template.maxCrew : maxCrew);
    e.getContents().addAll(Element
        .toSpaceNet(contents == null ? Element.clone(template.contents) : contents, context));

    e.setOmsIsp(isp == null ? template.isp : isp);
    edu.mit.spacenet.domain.element.ResourceTank t =
        new edu.mit.spacenet.domain.element.ResourceTank();
    Resource _fuel = (fuel == null ? template.fuel : fuel);
    if (_fuel.resource == null) {
      t.setResource(new edu.mit.spacenet.domain.resource.GenericResource(
          ClassOfSupply.getInstance(_fuel.classOfSupply),
          Environment.getInstance(_fuel.environment)));
    } else {
      t.setResource((I_Resource) context.getJavaObjectFromJsonId(_fuel.resource));
    }
    t.setAmount(_fuel.amount);
    t.setMaxAmount(maxFuel == null ? template.maxFuel : maxFuel);
    e.setOmsFuelTank(t);
    edu.mit.spacenet.domain.element.ResourceTank t2 = new edu.mit.spacenet.domain.element.ResourceTank();
    t2.setResource(new edu.mit.spacenet.domain.resource.GenericResource(
          ClassOfSupply.COS0,
          Environment.UNPRESSURIZED));
    e.setRcsFuelTank(t2);
    return e;
  }

  @Override
  public ElementPreview getPreview(Context context) {
    return new ElementPreview(context.getJavaId(id), name, ElementType.PROPULSIVE_VEHICLE,
        ElementIcon.getInstance(icon));
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PropulsiveVehicle)) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    final PropulsiveVehicle other = (PropulsiveVehicle) obj;
    return new EqualsBuilder().appendSuper(super.equals(obj)).append(isp, other.isp)
        .append(fuel, other.fuel).append(maxFuel, other.maxFuel).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31).appendSuper(super.hashCode()).append(isp).append(fuel)
        .append(maxFuel).toHashCode();
  }

  @Override
  public PropulsiveVehicle clone() {
    PropulsiveVehicle e = new PropulsiveVehicle();
    e.id = UUID.randomUUID();
    e.templateId = templateId;
    e.name = name;
    e.description = description;
    e.accommodationMass = accommodationMass;
    e.mass = mass;
    e.volume = volume;
    e.classOfSupply = classOfSupply;
    e.environment = environment;
    e.states = State.clone(states);
    e.currentStateIndex = currentStateIndex;
    e.parts = Part.clone(parts);
    e.icon = icon;
    e.maxCargoMass = maxCargoMass;
    e.maxCargoVolume = maxCargoVolume;
    e.cargoEnvironment = cargoEnvironment;
    e.maxCrew = maxCrew;
    e.contents = Element.clone(contents);
    e.isp = isp;
    e.fuel = fuel.clone();
    e.maxFuel = maxFuel;
    return e;
  }
}
