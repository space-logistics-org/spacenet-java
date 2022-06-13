package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.I_Resource;

public class PropulsiveVehicle extends Carrier {
  protected Double isp;
  protected UUID fuelType;
  protected Double fuelMaxAmount;
  protected Double fuelAmount;

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

      e.maxCargoMass = element.getMaxCargoMass();
      e.maxCargoVolume = element.getMaxCargoVolume();
      e.cargoEnvironment = element.getCargoEnvironment().getName();
      e.maxCrewSize = element.getMaxCrewSize();
      e.contents = Element.createFrom(element.getContents(), context);

      if (element.getOmsFuelTank() != null) {
        e.isp = element.getOmsIsp();
        e.fuelType = context.getJsonIdFromJavaObject(element.getOmsFuelTank().getResource());
        e.fuelMaxAmount = element.getOmsFuelTank().getMaxAmount();
        e.fuelAmount = element.getOmsFuelTank().getAmount();
      } else if (element.getRcsFuelTank() != null) {
        e.isp = element.getRcsIsp();
        e.fuelType = context.getJsonIdFromJavaObject(element.getRcsFuelTank().getResource());
        e.fuelMaxAmount = element.getRcsFuelTank().getMaxAmount();
        e.fuelAmount = element.getRcsFuelTank().getAmount();
      }
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
      if (!template.maxCrewSize.equals(element.getMaxCrewSize())) {
        e.maxCrewSize = element.getMaxCrewSize();
      }
      // TODO cannot override template contents; fails silently
      if (!template.isp
          .equals(element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp())) {
        e.isp = element.getOmsFuelTank() != null ? element.getOmsIsp() : element.getRcsIsp();
      }
      if (!template.fuelType.equals(element.getOmsFuelTank() != null
          ? context.getJsonIdFromJavaObject(element.getOmsFuelTank().getResource())
          : context.getJsonIdFromJavaObject(element.getRcsFuelTank().getResource()))) {
        e.fuelType = element.getOmsFuelTank() != null
            ? context.getJsonIdFromJavaObject(element.getOmsFuelTank().getResource())
            : context.getJsonIdFromJavaObject(element.getRcsFuelTank().getResource());
      }
      if (!template.fuelMaxAmount
          .equals(element.getOmsFuelTank() != null ? element.getOmsFuelTank().getMaxAmount()
              : element.getRcsFuelTank().getMaxAmount())) {
        e.fuelMaxAmount = element.getOmsFuelTank() != null ? element.getOmsFuelTank().getMaxAmount()
            : element.getRcsFuelTank().getMaxAmount();
      }
      if (!template.fuelAmount
          .equals(element.getOmsFuelTank() != null ? element.getOmsFuelTank().getAmount()
              : element.getRcsFuelTank().getAmount())) {
        e.fuelAmount = element.getOmsFuelTank() != null ? element.getOmsFuelTank().getAmount()
            : element.getRcsFuelTank().getAmount();
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

    e.setMaxCargoMass(maxCargoMass == null ? template.maxCargoMass : maxCargoMass);
    e.setMaxCargoVolume(maxCargoVolume == null ? template.maxCargoVolume : maxCargoVolume);
    e.setCargoEnvironment(Environment
        .getInstance(cargoEnvironment == null ? template.cargoEnvironment : cargoEnvironment));
    e.setMaxCrewSize(maxCrewSize == null ? template.maxCrewSize : maxCrewSize);
    e.getContents().addAll(Element
        .toSpaceNet(contents == null ? Element.clone(template.contents) : contents, context));

    e.setOmsIsp(isp == null ? template.isp : isp);
    edu.mit.spacenet.domain.element.ResourceTank t =
        new edu.mit.spacenet.domain.element.ResourceTank();
    t.setResource((I_Resource) context
        .getJavaObjectFromJsonId(fuelType == null ? template.fuelType : fuelType));
    t.setMaxAmount(fuelMaxAmount == null ? template.fuelMaxAmount : fuelMaxAmount);
    t.setAmount(fuelAmount == null ? template.fuelAmount : fuelAmount);
    e.setOmsFuelTank(t);
    return e;
  }

  @Override
  public ElementPreview getPreview(Context context) {
    return new ElementPreview(context.getJavaId(id), name, ElementType.PROPULSIVE_VEHICLE,
        ElementIcon.getInstance(icon));
  }

  @Override
  public PropulsiveVehicle clone() {
    PropulsiveVehicle e = new PropulsiveVehicle();
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
    e.maxCargoMass = maxCargoMass;
    e.maxCargoVolume = maxCargoVolume;
    e.cargoEnvironment = cargoEnvironment;
    e.maxCrewSize = maxCrewSize;
    e.contents = Element.clone(contents);
    e.isp = isp;
    e.fuelType = fuelType;
    e.fuelMaxAmount = fuelMaxAmount;
    e.fuelAmount = fuelAmount;
    return e;
  }
}
