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

public class CrewMember extends Element {
  protected Double availableTimeFraction;

  public static CrewMember createFrom(edu.mit.spacenet.domain.element.CrewMember element,
      Context context) {
    CrewMember e = new CrewMember();
    e.id = UUID.randomUUID();
    context.put(element, e.id, e);
    e.templateId = context.getElementTemplate(element.getTid());
    CrewMember template = (CrewMember) context.getJsonObject(e.templateId);
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

      e.availableTimeFraction = element.getAvailableTimeFraction();
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
      if (element.getCurrentState() != null) {
        List<I_State> states = new ArrayList<I_State>(element.getStates());
        if (!template.currentStateIndex.equals(states.indexOf(element.getCurrentState()))) {
          e.currentStateIndex = states.indexOf(element.getCurrentState());
        }
      }
      if (!template.availableTimeFraction.equals(element.getAvailableTimeFraction())) {
        e.availableTimeFraction = element.getAvailableTimeFraction();
      }
    }
    return e;
  }

  @Override
  public edu.mit.spacenet.domain.element.CrewMember toSpaceNet(Context context) {
    edu.mit.spacenet.domain.element.CrewMember e = new edu.mit.spacenet.domain.element.CrewMember();
    context.put(e, id, this);
    e.setUid(context.getJavaId(id));
    e.setTid(templateId == null ? context.getJavaId(id) : context.getJavaId(templateId));
    CrewMember template = (CrewMember) context.getJsonObject(templateId);
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
    e.setStates(
        State.toSpaceNet(e, states == null ? State.clone(template.states) : states, context));
    if (currentStateIndex != null || (template != null && template.currentStateIndex != null)) {
      e.setCurrentState(new ArrayList<I_State>(e.getStates())
          .get(currentStateIndex == null ? template.currentStateIndex : currentStateIndex));
    }
    e.setParts(Part.toSpaceNet(parts == null ? template.parts : parts, context));

    e.setAvailableTimeFraction(
        availableTimeFraction == null ? template.availableTimeFraction : availableTimeFraction);
    return e;
  }

  @Override
  public ElementPreview getPreview(Context context) {
    return new ElementPreview(context.getJavaId(id), name, ElementType.CREW_MEMBER,
        ElementIcon.getInstance(icon));
  }

  @Override
  public CrewMember clone() {
    CrewMember e = new CrewMember();
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
    e.availableTimeFraction = availableTimeFraction;
    return e;
  }
}
