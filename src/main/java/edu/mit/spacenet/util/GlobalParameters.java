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
package edu.mit.spacenet.util;

import edu.mit.spacenet.scenario.ItemDiscretization;
import edu.mit.spacenet.scenario.Scenario;

/**
 * A global access point to scenario options.
 * 
 * @author Paul Grogan
 */
public class GlobalParameters {
  private static final GlobalParameters singleton = new GlobalParameters();

  public static final GlobalParameters getSingleton() {
    return singleton;
  }

  private final int DECIMAL_PRECISION = 1000;
  private double timePrecision = 0.05;
  private double demandPrecision = 0.01;
  private double massPrecision = 0.01;
  private boolean volumeConstrained = false;
  private double volumePrecision = 0.000001;
  private boolean environmentConstrained = true;
  private double itemAggregation = 0;
  private ItemDiscretization itemDiscretization = ItemDiscretization.NONE;
  private boolean scavengeSpares = false;
  private double genericPackingFactorGas = 1.0;
  private double genericPackingFactorLiquid = 0.5;
  private double genericPackingFactorPressurizedExternal = 1.2;
  private double genericPackingFactorPressurizedInternal = 0.2;
  private double genericPackingFactorUnpressurized = 0.6;

  private GlobalParameters() {}

  public void setParametersFrom(Scenario scenario) {
    setTimePrecision(scenario.getTimePrecision());
    setDemandPrecision(scenario.getDemandPrecision());
    setMassPrecision(scenario.getMassPrecision());
    setVolumeConstrained(scenario.isVolumeConstrained());
    setVolumePrecision(scenario.getVolumePrecision());
    setEnvironmentConstrained(scenario.isEnvironmentConstrained());
    setItemAggregation(scenario.getItemAggregation());
    setItemDiscretization(scenario.getItemDiscretization());
    setScavengeSpares(scenario.isScavengeSpares());
    setGenericPackingFactorGas(scenario.getGenericPackingFactorGas());
    setGenericPackingFactorLiquid(scenario.getGenericPackingFactorLiquid());
    setGenericPackingFactorPressurizedInternal(scenario.getGenericPackingFactorPressurized());
    setGenericPackingFactorUnpressurized(scenario.getGenericPackingFactorUnpressurized());
  }

  /**
   * Gets the time precision.
   * 
   * @return the time precision (days)
   */
  public double getTimePrecision() {
    return timePrecision;
  }

  /**
   * Sets the time precision.
   * 
   * @param timePrecision the time precision (days)
   */
  public void setTimePrecision(double timePrecision) {
    this.timePrecision = timePrecision;
  }

  /**
   * Gets the rounded time according to the time precision.
   * 
   * @param time the raw time (days)
   * 
   * @return the rounded time (days)
   */
  public double getRoundedTime(double time) {
    return Math.round(Math.round(time / timePrecision) * timePrecision * DECIMAL_PRECISION)
        / ((double) DECIMAL_PRECISION);
  }


  /**
   * Gets the demand precision.
   * 
   * @return the demand precision (units)
   */
  public double getDemandPrecision() {
    return demandPrecision;
  }

  /**
   * Sets the demand precision.
   * 
   * @param demandPrecision the demand precision (units)
   */
  public void setDemandPrecision(double demandPrecision) {
    this.demandPrecision = demandPrecision;
  }

  /**
   * Gets the rounded demand amount according to the demand precision.
   * 
   * @param demand the raw demand amount
   * 
   * @return the rounded demand amount
   */
  public double getRoundedDemand(double demand) {
    return Math.round(demand / demandPrecision) * demandPrecision;
  }


  /**
   * Gets the mass precision.
   * 
   * @return the mass precision (kilograms)
   */
  public double getMassPrecision() {
    return massPrecision;
  }

  /**
   * Sets the mass precision.
   * 
   * @param massPrecision the mass precision (kilograms)
   */
  public void setMassPrecision(double massPrecision) {
    this.massPrecision = massPrecision;
  }

  /**
   * Gets the rounded mass according to the mass precision.
   * 
   * @param mass the raw mass (kilograms)
   * 
   * @return the rounded mass (kilograms)
   */
  public double getRoundedMass(double mass) {
    return Math.round(mass / massPrecision) * massPrecision;
  }

  /**
   * Gets the volume precision.
   * 
   * @return the volume precision (cubic meters)
   */
  public double getVolumePrecision() {
    return volumePrecision;
  }

  /**
   * Sets the volume precision.
   * 
   * @param volumePrecision the volume precision (cubic meters)
   */
  public void setVolumePrecision(double volumePrecision) {
    this.volumePrecision = volumePrecision;
  }

  /**
   * Gets the rounded volume according to the volume precision.
   * 
   * @param volume the raw volume (cubic meters)
   * 
   * @return the rounded volume (cubic meters)
   */
  public double getRoundedVolume(double volume) {
    return Math.round(volume / volumePrecision) * volumePrecision;
  }


  /**
   * Sets whether volume constraints should be active.
   * 
   * @param volumeConstrained true if volume constraints are active, false otherwise
   */
  public void setVolumeConstrained(boolean volumeConstrained) {
    this.volumeConstrained = volumeConstrained;
  }

  /**
   * Gets whether volume constraints are active.
   * 
   * @return true if volume constraints are active, false otherwise
   */
  public boolean isVolumeConstrained() {
    return volumeConstrained;
  }

  /**
   * Sets whether environment constraints (pressurized items can only go inside pressurized
   * containers) should be active.
   * 
   * @param environmentConstrained true if environment constraints are active, false otherwise
   */
  public void setEnvironmentConstrained(boolean environmentConstrained) {
    this.environmentConstrained = environmentConstrained;
  }

  /**
   * Gets whether environment constraints (pressurized items can only go inside pressurized
   * containers) are active.
   * 
   * @return true if environment constraints are active, false otherwise
   */
  public boolean isEnvironmentConstrained() {
    return environmentConstrained;
  }

  /**
   * Gets the item discretization option.
   * 
   * @return how items are discretized
   */
  public ItemDiscretization getItemDiscretization() {
    return itemDiscretization;
  }

  /**
   * Sets the item discretization option.
   * 
   * @param itemDiscretization how items are discretized
   */
  public void setItemDiscretization(ItemDiscretization itemDiscretization) {
    this.itemDiscretization = itemDiscretization;
  }


  /**
   * Gets the item aggregation point, which ranges between 0 and 1. 0 aggregates whole items at the
   * first partial demand, 1 aggregates whole items after demands totaling one unit.
   * 
   * @return the item aggregation point (between 0 and 1)
   */
  public double getItemAggregation() {
    return itemAggregation;
  }

  /**
   * Sets the item aggregation point, which ranges between 0 and 1. 0 aggregates whole items at the
   * first partial demand, 1 aggregates whole items after demands totaling one unit.
   * 
   * @param itemAggregation the item aggregation point (between 0 and 1)
   */
  public void setItemAggregation(double itemAggregation) {
    if (itemAggregation < 0)
      itemAggregation = 0;
    else if (itemAggregation > 1)
      itemAggregation = 1;
    this.itemAggregation = itemAggregation;
  }

  /**
   * Gets whether spares can be scavenged from decommissioned elements.
   * 
   * @return true if spares can be scavenged, false otherwise
   */
  public boolean isScavengeSpares() {
    return scavengeSpares;
  }

  /**
   * Sets whether spares can be scavenged from decommissioned elements.
   * 
   * @param scavengeSpares true if spares can be scavenged, false otherwise
   */
  public void setScavengeSpares(boolean scavengeSpares) {
    this.scavengeSpares = scavengeSpares;
  }

  /**
   * Gets the generic packing factor gas.
   *
   * @return the generic packing factor gas
   */
  public double getGenericPackingFactorGas() {
    return genericPackingFactorGas;
  }

  /**
   * Sets the generic packing factor gas.
   *
   * @param genericPackingFactorGas the new generic packing factor gas
   */
  public void setGenericPackingFactorGas(double genericPackingFactorGas) {
    this.genericPackingFactorGas = genericPackingFactorGas;
  }

  /**
   * Gets the generic packing factor liquid.
   *
   * @return the generic packing factor liquid
   */
  public double getGenericPackingFactorLiquid() {
    return genericPackingFactorLiquid;
  }

  /**
   * Sets the generic packing factor liquid.
   *
   * @param genericPackingFactorLiquid the new generic packing factor liquid
   */
  public void setGenericPackingFactorLiquid(double genericPackingFactorLiquid) {
    this.genericPackingFactorLiquid = genericPackingFactorLiquid;
  }

  /**
   * Gets the generic packing factor pressurized external.
   *
   * @return the generic packing factor pressurized external
   */
  public double getGenericPackingFactorPressurizedExternal() {
    return genericPackingFactorPressurizedExternal;
  }

  /**
   * Sets the generic packing factor pressurized external.
   *
   * @param genericPackingFactorPressurizedExternal the new generic packing factor pressurized
   *        external
   */
  public void setGenericPackingFactorPressurizedExternal(
      double genericPackingFactorPressurizedExternal) {
    this.genericPackingFactorPressurizedExternal = genericPackingFactorPressurizedExternal;
  }

  /**
   * Gets the generic packing factor pressurized internal.
   *
   * @return the generic packing factor pressurized internal
   */
  public double getGenericPackingFactorPressurizedInternal() {
    return genericPackingFactorPressurizedInternal;
  }

  /**
   * Sets the generic packing factor pressurized internal.
   *
   * @param genericPackingFactorPressurizedInternal the new generic packing factor pressurized
   *        internal
   */
  public void setGenericPackingFactorPressurizedInternal(
      double genericPackingFactorPressurizedInternal) {
    this.genericPackingFactorPressurizedInternal = genericPackingFactorPressurizedInternal;
  }

  /**
   * Gets the generic packing factor unpressurized.
   *
   * @return the generic packing factor unpressurized
   */
  public double getGenericPackingFactorUnpressurized() {
    return genericPackingFactorUnpressurized;
  }

  /**
   * Sets the generic packing factor unpressurized.
   *
   * @param genericPackingFactorUnpressurized the new generic packing factor unpressurized
   */
  public void setGenericPackingFactorUnpressurized(double genericPackingFactorUnpressurized) {
    this.genericPackingFactorUnpressurized = genericPackingFactorUnpressurized;
  }
}
