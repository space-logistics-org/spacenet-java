/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.mit.spacenet.domain.element;

import edu.mit.spacenet.domain.ClassOfSupply;

/**
 * An element used to represent crew members.
 * 
 * @author Paul Grogan
 */
public class CrewMember extends Element implements I_Agent {
  private double availableTimeFraction;

  /**
   * The default constructor that sets the class of supply to COS0.
   */
  public CrewMember() {
    super();
    setClassOfSupply(ClassOfSupply.COS0);
    availableTimeFraction = 0.667;
  }

  /**
   * Gets the available time fraction (percent of time available for exploration, maintenance, etc.)
   * 
   * @return the available time fraction
   */
  public double getAvailableTimeFraction() {
    return availableTimeFraction;
  }

  /**
   * Sets the available time fraction (percent of time available for exploration, maintenance, etc.)
   * 
   * @param availableTimeFraction the available time fraction
   */
  public void setAvailableTimeFraction(double availableTimeFraction) {
    this.availableTimeFraction = availableTimeFraction;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getElementType()
   */
  @Override
  public ElementType getElementType() {
    return ElementType.CREW_MEMBER;
  }

  @Override
  public CrewMember clone() throws CloneNotSupportedException {
    CrewMember e = new CrewMember();
    e.setTid(getTid());
    e.setName(getName());
    e.setDescription(getDescription());
    e.setClassOfSupply(getClassOfSupply());
    e.setEnvironment(getEnvironment());
    e.setAccommodationMass(getAccommodationMass());
    e.setMass(getMass());
    e.setVolume(getVolume());
    for (PartApplication part : getParts()) {
      e.getParts().add(part.clone());
    }
    for (I_State state : getStates()) {
      I_State s = state.clone();
      e.getStates().add(s);
      if (state.equals(getCurrentState())) {
        e.setCurrentState(s);
      }
    }
    e.setContainer(getContainer());
    e.setIconType(getIconType());
    e.setAvailableTimeFraction(getAvailableTimeFraction());
    return e;
  }
}
