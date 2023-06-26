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

import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A generic type of resource container where the element's mass and volume are functions of the
 * contents' mass and volume via mass and volume packing factors. Instead of specifying maximum
 * cargo masses and volumes, you instead specify maximum masses and volumes for the entire
 * container.
 * 
 * @author Paul Grogan
 */
public class GenericResourceContainer extends ResourceContainer {
  private double massPackingFactor, volumePackingFactor;
  private double maxMass, maxVolume;

  /**
   * Instantiates a new generic resource container.
   */
  public GenericResourceContainer() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getVolume()
   */
  @Override
  public double getVolume() {
    return GlobalParameters.getSingleton()
        .getRoundedVolume(super.getVolume() + volumePackingFactor * getCargoVolume());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.Element#getMass()
   */
  @Override
  public double getMass() {
    return GlobalParameters.getSingleton()
        .getRoundedMass(super.getMass() + massPackingFactor * getCargoMass());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.ResourceContainer#getMaxCargoMass()
   */
  @Override
  public double getMaxCargoMass() {
    return GlobalParameters.getSingleton()
        .getRoundedMass((maxMass - getMass()) / (massPackingFactor + 1));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.ResourceContainer#setMaxCargoMass(double)
   */
  @Override
  public void setMaxCargoMass(double maxCargoMass) {
    maxMass = maxCargoMass * (massPackingFactor + 1) + getMass();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.ResourceContainer#getMaxCargoVolume()
   */
  @Override
  public double getMaxCargoVolume() {
    return GlobalParameters.getSingleton()
        .getRoundedVolume((maxVolume - getVolume()) / (volumePackingFactor + 1));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.element.ResourceContainer#setMaxCargoVolume(double)
   */
  @Override
  public void setMaxCargoVolume(double maxCargoVolume) {
    maxVolume = maxCargoVolume * (volumePackingFactor + 1) + getVolume();
  }

  /**
   * Gets the mass packing factor.
   * 
   * @return the mass packing factor
   */
  public double getMassPackingFactor() {
    return massPackingFactor;
  }

  /**
   * Sets the mass packing factor.
   * 
   * @param massPackingFactor the mass packing factor
   */
  public void setMassPackingFactor(double massPackingFactor) {
    this.massPackingFactor = massPackingFactor;
  }

  /**
   * Gets the volume packing factor.
   * 
   * @return the volume packing factor
   */
  public double getVolumePackingFactor() {
    return volumePackingFactor;
  }

  /**
   * Sets the volume packing factor.
   * 
   * @param volumePackingFactor the volume packing factor
   */
  public void setVolumePackingFactor(double volumePackingFactor) {
    this.volumePackingFactor = volumePackingFactor;
  }

  /**
   * Gets the maximum container mass.
   * 
   * @return the maximum mass (kilograms)
   */
  public double getMaxMass() {
    return GlobalParameters.getSingleton().getRoundedMass(maxMass);
  }

  /**
   * Sets the maximum container mass.
   * 
   * @param maxMass the maximum mass (kilograms)
   */
  public void setMaxMass(double maxMass) {
    this.maxMass = maxMass;
  }

  /**
   * Gets the maximum container volume.
   * 
   * @return the maximum container volume (cubic meters)
   */
  public double getMaxVolume() {
    return GlobalParameters.getSingleton().getRoundedVolume(maxVolume);
  }

  /**
   * Sets the maximum container volume.
   * 
   * @param maxVolume the maximum container volume (cubic meters)
   */
  public void setMaxVolume(double maxVolume) {
    this.maxVolume = maxVolume;
  }

  @Override
  public GenericResourceContainer clone() throws CloneNotSupportedException {
    GenericResourceContainer e = new GenericResourceContainer();
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
    e.setMaxCargoMass(getMaxCargoMass());
    e.setMaxCargoVolume(getMaxCargoVolume());
    e.setCargoEnvironment(getCargoEnvironment());
    e.setMassPackingFactor(getMassPackingFactor());
    e.setVolumePackingFactor(getVolumePackingFactor());
    e.setMaxMass(getMaxMass());
    e.setMaxVolume(getMaxVolume());
    for (I_Resource resource : getContents().keySet()) {
      e.getContents().put(resource, getContents().get(resource));
    }
    return e;
  }
}
