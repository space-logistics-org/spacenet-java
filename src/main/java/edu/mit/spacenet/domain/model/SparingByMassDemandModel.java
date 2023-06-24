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
package edu.mit.spacenet.domain.model;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.simulator.I_Simulator;

/**
 * Crude sparing demand model that demands generic class of supply 4 resources proportional to the
 * element's mass, a annual sparing rate, and the duration. An option allows an element's parts list
 * to be utilized to generate specific resource demands if desired.
 * 
 * @author Paul Grogan
 */
public class SparingByMassDemandModel extends AbstractDemandModel {
  private I_Element element;
  private double unpressurizedSparesRate;
  private double pressurizedSparesRate;
  private boolean partsListEnabled;

  /**
   * The default constructor sets the default packaging factors.
   * 
   * @param element the element
   */
  public SparingByMassDemandModel(I_Element element) {
    this.element = element;
    this.partsListEnabled = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#generateDemands(double,
   * edu.mit.spacenet.simulator.I_Simulator)
   */
  public DemandSet generateDemands(double duration, I_Simulator simulator) {
    return generateDemands(duration, pressurizedSparesRate, unpressurizedSparesRate,
        partsListEnabled);
  }

  /**
   * Generate demands.
   *
   * @param duration the duration
   * @param pressurizedSparesRate the pressurized spares rate
   * @param unpressurizedSparesRate the unpressurized spares rate
   * @param partsListEnabled the parts list enabled
   * @return the demand set
   */
  public DemandSet generateDemands(double duration, double pressurizedSparesRate,
      double unpressurizedSparesRate, boolean partsListEnabled) {
    DemandSet demands = new DemandSet();

    double pressPartMass = 0;
    double unpressPartMass = 0;
    double genericPartMass = 0;

    if (partsListEnabled) {
      for (PartApplication p : element.getParts()) {
        if (p.getQuantity() > 0) {
          if (p.getPart().getEnvironment().equals(Environment.PRESSURIZED)) {
            pressPartMass += p.getQuantity() * p.getPart().getUnitMass();
          } else if (p.getPart().getEnvironment().equals(Environment.UNPRESSURIZED)) {
            unpressPartMass += p.getQuantity() * p.getPart().getUnitMass();
          }
        }
      }
      // TODO: should check for error conditions:
      // pressPartMass > element.getMass()
      // unpressPartMass > element.getMass()
      // pressPartMass + unpressPartMass > element.getMass()
      genericPartMass = Math.max(0, element.getMass() - pressPartMass - unpressPartMass);
      for (PartApplication p : element.getParts()) {
        if (p.getQuantity() > 0) {
          Demand demand = new Demand();
          demand.setResource(p.getPart());
          if (p.getPart().getEnvironment().equals(Environment.PRESSURIZED)) {
            demand.setAmount(duration * pressurizedSparesRate / 365 * element.getMass()
                * p.getQuantity() / (genericPartMass + pressPartMass));
          } else if (p.getPart().getEnvironment().equals(Environment.UNPRESSURIZED)) {
            demand.setAmount(duration * unpressurizedSparesRate / 365 * element.getMass()
                * p.getQuantity() / (genericPartMass + unpressPartMass));
          }
          demands.add(demand);
        }
      }
    } else {
      genericPartMass = element.getMass();
    }

    Demand unpressSpares = new Demand();
    unpressSpares.setResource(new GenericResource(ClassOfSupply.COS4));
    unpressSpares.getResource().setEnvironment(Environment.UNPRESSURIZED);
    unpressSpares.setAmount(duration * unpressurizedSparesRate / 365 * element.getMass()
        * genericPartMass / (genericPartMass + unpressPartMass));
    demands.add(unpressSpares);

    Demand pressSpares = new Demand();
    pressSpares.setResource(new GenericResource(ClassOfSupply.COS4));
    pressSpares.getResource().setEnvironment(Environment.PRESSURIZED);
    pressSpares.setAmount(duration * pressurizedSparesRate / 365 * element.getMass()
        * genericPartMass / (genericPartMass + pressPartMass));
    demands.add(pressSpares);

    return demands;
  }

  /**
   * Gets the element.
   * 
   * @return the element
   */
  public I_Element getElement() {
    return element;
  }

  /**
   * Sets the element.
   * 
   * @param element the element
   */
  public void setElement(I_Element element) {
    this.element = element;
  }

  /**
   * Gets the unpressurized sparing rate.
   * 
   * @return the sparing rate (percent element mass per year)
   */
  public double getUnpressurizedSparesRate() {
    return unpressurizedSparesRate;
  }

  /**
   * Sets the unpressurized sparing rate.
   * 
   * @param sparesRate the sparing rate (percent element mass per year)
   */
  public void setUnpressurizedSparesRate(double sparesRate) {
    this.unpressurizedSparesRate = sparesRate;
  }

  /**
   * Gets the pressurized sparing rate.
   * 
   * @return the sparing rate (percent element mass per year)
   */
  public double getPressurizedSparesRate() {
    return pressurizedSparesRate;
  }

  /**
   * Sets the pressurized sparing rate.
   * 
   * @param sparesRate the spares rate
   */
  public void setPressurizedSparesRate(double sparesRate) {
    this.pressurizedSparesRate = sparesRate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.model.I_DemandModel#getDemandModelType()
   */
  public DemandModelType getDemandModelType() {
    return DemandModelType.SPARING_BY_MASS;
  }

  /**
   * Checks if is parts list enabled, allowing demands for generic and specific resources.
   * 
   * @return true, if is parts list enabled
   */
  public boolean isPartsListEnabled() {
    return partsListEnabled;
  }

  /**
   * Sets the parts list enabled, allowing demands for generic and specific resources.
   * 
   * @param partsListEnabled the new parts list enabled
   */
  public void setPartsListEnabled(boolean partsListEnabled) {
    this.partsListEnabled = partsListEnabled;
  }

  @Override
  public SparingByMassDemandModel clone() throws CloneNotSupportedException {
    SparingByMassDemandModel m = new SparingByMassDemandModel(getElement());
    m.setTid(getTid());
    m.setName(getName());
    m.setDescription(getDescription());
    m.setUnpressurizedSparesRate(getUnpressurizedSparesRate());
    m.setPressurizedSparesRate(getPressurizedSparesRate());
    m.setPartsListEnabled(isPartsListEnabled());
    return m;
  }
}
