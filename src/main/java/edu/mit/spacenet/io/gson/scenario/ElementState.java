package edu.mit.spacenet.io.gson.scenario;

import java.util.ArrayList;
import java.util.UUID;
import edu.mit.spacenet.domain.element.I_State;

public class ElementState {
  protected UUID element;
  protected Integer stateIndex;

  public static ElementState createFrom(edu.mit.spacenet.domain.element.I_Element element,
      edu.mit.spacenet.domain.element.I_State state, Context context) {
    ElementState e = new ElementState();
    e.element = context.getJsonIdFromJavaObject(element);
    if (state == null) {
      e.stateIndex = -1;
    } else {
      e.stateIndex = new ArrayList<I_State>(element.getStates()).indexOf(state);
    }
    return e;
  }
}
