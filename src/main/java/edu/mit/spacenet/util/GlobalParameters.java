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

  /**
   * Gets the singleton.
   *
   * @return the singleton
   */
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

  private double smallGasTankMass = 10.8;
  private double smallGasTankVolume = 0.275;
  private double smallGasTankMaxMass = 10;
  private double smallGasTankMaxVolume = 0.275;

  private double largeGasTankMass = 108;
  private double largeGasTankVolume = 2.75;
  private double largeGasTankMaxMass = 100;
  private double largeGasTankMaxVolume = 2.75;

  private double smallLiquidTankMass = 11.4567;
  private double smallLiquidTankVolume = 0.0249;
  private double smallLiquidTankMaxMass = 24.9333;
  private double smallLiquidTankMaxVolume = 0.0249;

  private double largeLiquidTankMass = 34.37;
  private double largeLiquidTankVolume = 0.0748;
  private double largeLiquidTankMaxMass = 74.8;
  private double largeLiquidTankMaxVolume = 0.0748;

  private double cargoTransferBagMass = 0.83;
  private double cargoTransferBagVolume = 0.053;
  private double cargoTransferBagMaxMass = 26.8;
  private double cargoTransferBagMaxVolume = 0.049;

  /**
   * Instantiates a new global parameters.
   */
  private GlobalParameters() {}

  /**
   * Sets the parameters from.
   *
   * @param scenario the new parameters from
   */
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
    setSmallGasTankMass(scenario.getSmallGasTankMass());
    setSmallGasTankVolume(scenario.getSmallGasTankVolume());
    setSmallGasTankMaxMass(scenario.getSmallGasTankMaxMass());
    setSmallGasTankMaxVolume(scenario.getSmallGasTankMaxVolume());
    setLargeGasTankMass(scenario.getLargeGasTankMass());
    setLargeGasTankVolume(scenario.getLargeGasTankVolume());
    setLargeGasTankMaxMass(scenario.getLargeGasTankMaxMass());
    setLargeLiquidTankMaxVolume(scenario.getLargeLiquidTankMaxVolume());
    setSmallLiquidTankMass(scenario.getSmallLiquidTankMass());
    setSmallLiquidTankVolume(scenario.getSmallLiquidTankVolume());
    setSmallLiquidTankMaxMass(scenario.getSmallLiquidTankMaxMass());
    setSmallLiquidTankMaxVolume(scenario.getSmallLiquidTankMaxVolume());
    setLargeLiquidTankMass(scenario.getLargeLiquidTankMass());
    setLargeLiquidTankVolume(scenario.getLargeLiquidTankVolume());
    setLargeLiquidTankMaxMass(scenario.getLargeLiquidTankMaxMass());
    setLargeLiquidTankMaxVolume(scenario.getLargeLiquidTankMaxVolume());
    setCargoTransferBagMass(scenario.getCargoTransferBagMass());
    setCargoTransferBagVolume(scenario.getCargoTransferBagVolume());
    setCargoTransferBagMaxMass(scenario.getCargoTransferBagMaxMass());
    setCargoTransferBagMaxVolume(scenario.getCargoTransferBagMaxVolume());
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

  /**
   * Gets the small gas tank mass.
   *
   * @return the small gas tank mass
   */
  public double getSmallGasTankMass() {
    return smallGasTankMass;
  }

  /**
   * Sets the small gas tank mass.
   *
   * @param smallGasTankMass the new small gas tank mass
   */
  public void setSmallGasTankMass(double smallGasTankMass) {
    this.smallGasTankMass = smallGasTankMass;
  }

  /**
   * Gets the small gas tank volume.
   *
   * @return the small gas tank volume
   */
  public double getSmallGasTankVolume() {
    return smallGasTankVolume;
  }

  /**
   * Sets the small gas tank volume.
   *
   * @param smallGasTankVolume the new small gas tank volume
   */
  public void setSmallGasTankVolume(double smallGasTankVolume) {
    this.smallGasTankVolume = smallGasTankVolume;
  }

  /**
   * Gets the small gas tank max mass.
   *
   * @return the small gas tank max mass
   */
  public double getSmallGasTankMaxMass() {
    return smallGasTankMaxMass;
  }

  /**
   * Sets the small gas tank max mass.
   *
   * @param smallGasTankMaxMass the new small gas tank max mass
   */
  public void setSmallGasTankMaxMass(double smallGasTankMaxMass) {
    this.smallGasTankMaxMass = smallGasTankMaxMass;
  }

  /**
   * Gets the small gas tank max volume.
   *
   * @return the small gas tank max volume
   */
  public double getSmallGasTankMaxVolume() {
    return smallGasTankMaxVolume;
  }

  /**
   * Sets the small gas tank max volume.
   *
   * @param smallGasTankMaxVolume the new small gas tank max volume
   */
  public void setSmallGasTankMaxVolume(double smallGasTankMaxVolume) {
    this.smallGasTankMaxVolume = smallGasTankMaxVolume;
  }

  /**
   * Gets the large gas tank mass.
   *
   * @return the large gas tank mass
   */
  public double getLargeGasTankMass() {
    return largeGasTankMass;
  }

  /**
   * Sets the large gas tank mass.
   *
   * @param largeGasTankMass the new large gas tank mass
   */
  public void setLargeGasTankMass(double largeGasTankMass) {
    this.largeGasTankMass = largeGasTankMass;
  }

  /**
   * Gets the large gas tank volume.
   *
   * @return the large gas tank volume
   */
  public double getLargeGasTankVolume() {
    return largeGasTankVolume;
  }

  /**
   * Sets the large gas tank volume.
   *
   * @param largeGasTankVolume the new large gas tank volume
   */
  public void setLargeGasTankVolume(double largeGasTankVolume) {
    this.largeGasTankVolume = largeGasTankVolume;
  }

  /**
   * Gets the large gas tank max mass.
   *
   * @return the large gas tank max mass
   */
  public double getLargeGasTankMaxMass() {
    return largeGasTankMaxMass;
  }

  /**
   * Sets the large gas tank max mass.
   *
   * @param largeGasTankMaxMass the new large gas tank max mass
   */
  public void setLargeGasTankMaxMass(double largeGasTankMaxMass) {
    this.largeGasTankMaxMass = largeGasTankMaxMass;
  }

  /**
   * Gets the large gas tank max volume.
   *
   * @return the large gas tank max volume
   */
  public double getLargeGasTankMaxVolume() {
    return largeGasTankMaxVolume;
  }

  /**
   * Sets the large gas tank max volume.
   *
   * @param largeGasTankMaxVolume the new large gas tank max volume
   */
  public void setLargeGasTankMaxVolume(double largeGasTankMaxVolume) {
    this.largeGasTankMaxVolume = largeGasTankMaxVolume;
  }

  /**
   * Gets the small liquid tank mass.
   *
   * @return the small liquid tank mass
   */
  public double getSmallLiquidTankMass() {
    return smallLiquidTankMass;
  }

  /**
   * Sets the small liquid tank mass.
   *
   * @param smallLiquidTankMass the new small liquid tank mass
   */
  public void setSmallLiquidTankMass(double smallLiquidTankMass) {
    this.smallLiquidTankMass = smallLiquidTankMass;
  }

  /**
   * Gets the small liquid tank volume.
   *
   * @return the small liquid tank volume
   */
  public double getSmallLiquidTankVolume() {
    return smallLiquidTankVolume;
  }

  /**
   * Sets the small liquid tank volume.
   *
   * @param smallLiquidTankVolume the new small liquid tank volume
   */
  public void setSmallLiquidTankVolume(double smallLiquidTankVolume) {
    this.smallLiquidTankVolume = smallLiquidTankVolume;
  }

  /**
   * Gets the small liquid tank max mass.
   *
   * @return the small liquid tank max mass
   */
  public double getSmallLiquidTankMaxMass() {
    return smallLiquidTankMaxMass;
  }

  /**
   * Sets the small liquid tank max mass.
   *
   * @param smallLiquidTankMaxMass the new small liquid tank max mass
   */
  public void setSmallLiquidTankMaxMass(double smallLiquidTankMaxMass) {
    this.smallLiquidTankMaxMass = smallLiquidTankMaxMass;
  }

  /**
   * Gets the small liquid tank max volume.
   *
   * @return the small liquid tank max volume
   */
  public double getSmallLiquidTankMaxVolume() {
    return smallLiquidTankMaxVolume;
  }

  /**
   * Sets the small liquid tank max volume.
   *
   * @param smallLiquidTankMaxVolume the new small liquid tank max volume
   */
  public void setSmallLiquidTankMaxVolume(double smallLiquidTankMaxVolume) {
    this.smallLiquidTankMaxVolume = smallLiquidTankMaxVolume;
  }

  /**
   * Gets the large liquid tank mass.
   *
   * @return the large liquid tank mass
   */
  public double getLargeLiquidTankMass() {
    return largeLiquidTankMass;
  }

  /**
   * Sets the large liquid tank mass.
   *
   * @param largeLiquidTankMass the new large liquid tank mass
   */
  public void setLargeLiquidTankMass(double largeLiquidTankMass) {
    this.largeLiquidTankMass = largeLiquidTankMass;
  }

  /**
   * Gets the large liquid tank volume.
   *
   * @return the large liquid tank volume
   */
  public double getLargeLiquidTankVolume() {
    return largeLiquidTankVolume;
  }

  /**
   * Sets the large liquid tank volume.
   *
   * @param largeLiquidTankVolume the new large liquid tank volume
   */
  public void setLargeLiquidTankVolume(double largeLiquidTankVolume) {
    this.largeLiquidTankVolume = largeLiquidTankVolume;
  }

  /**
   * Gets the large liquid tank max mass.
   *
   * @return the large liquid tank max mass
   */
  public double getLargeLiquidTankMaxMass() {
    return largeLiquidTankMaxMass;
  }

  /**
   * Sets the large liquid tank max mass.
   *
   * @param largeLiquidTankMaxMass the new large liquid tank max mass
   */
  public void setLargeLiquidTankMaxMass(double largeLiquidTankMaxMass) {
    this.largeLiquidTankMaxMass = largeLiquidTankMaxMass;
  }

  /**
   * Gets the large liquid tank max volume.
   *
   * @return the large liquid tank max volume
   */
  public double getLargeLiquidTankMaxVolume() {
    return largeLiquidTankMaxVolume;
  }

  /**
   * Sets the large liquid tank max volume.
   *
   * @param largeLiquidTankMaxVolume the new large liquid tank max volume
   */
  public void setLargeLiquidTankMaxVolume(double largeLiquidTankMaxVolume) {
    this.largeLiquidTankMaxVolume = largeLiquidTankMaxVolume;
  }

  /**
   * Gets the cargo transfer bag mass.
   *
   * @return the cargo transfer bag mass
   */
  public double getCargoTransferBagMass() {
    return cargoTransferBagMass;
  }

  /**
   * Sets the cargo transfer bag mass.
   *
   * @param cargoTransferBagMass the new cargo transfer bag mass
   */
  public void setCargoTransferBagMass(double cargoTransferBagMass) {
    this.cargoTransferBagMass = cargoTransferBagMass;
  }

  /**
   * Gets the cargo transfer bag volume.
   *
   * @return the cargo transfer bag volume
   */
  public double getCargoTransferBagVolume() {
    return cargoTransferBagVolume;
  }

  /**
   * Sets the cargo transfer bag volume.
   *
   * @param cargoTransferBagVolume the new cargo transfer bag volume
   */
  public void setCargoTransferBagVolume(double cargoTransferBagVolume) {
    this.cargoTransferBagVolume = cargoTransferBagVolume;
  }

  /**
   * Gets the cargo transfer bag max mass.
   *
   * @return the cargo transfer bag max mass
   */
  public double getCargoTransferBagMaxMass() {
    return cargoTransferBagMaxMass;
  }

  /**
   * Sets the cargo transfer bag max mass.
   *
   * @param cargoTransferBagMaxMass the new cargo transfer bag max mass
   */
  public void setCargoTransferBagMaxMass(double cargoTransferBagMaxMass) {
    this.cargoTransferBagMaxMass = cargoTransferBagMaxMass;
  }

  /**
   * Gets the cargo transfer bag max volume.
   *
   * @return the cargo transfer bag max volume
   */
  public double getCargoTransferBagMaxVolume() {
    return cargoTransferBagMaxVolume;
  }

  /**
   * Sets the cargo transfer bag max volume.
   *
   * @param cargoTransferBagMaxVolume the new cargo transfer bag max volume
   */
  public void setCargoTransferBagMaxVolume(double cargoTransferBagMaxVolume) {
    this.cargoTransferBagMaxVolume = cargoTransferBagMaxVolume;
  }
}
