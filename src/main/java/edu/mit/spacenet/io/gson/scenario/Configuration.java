package edu.mit.spacenet.io.gson.scenario;

public class Configuration {
  protected Double timePrecision;
  protected Double demandPrecision;
  protected Double massPrecision;
  protected Double volumePrecision;
  protected Boolean volumeConstrained;
  protected Boolean environmentConstrained;
  protected String itemDiscretization;
  protected Double itemAggregation;
  protected Boolean scavengeSpares;
  // TODO add repaired items
  protected Boolean detailedEva;
  protected Boolean detailedExploration;
  protected Double genericPackingFactorGas;
  protected Double genericPackingFactorLiquid;
  protected Double genericPackingFactorPressurized;
  protected Double genericPackingFactorUnpressurized;


  protected Double smallGasTankMass;
  protected Double smallGasTankVolume;
  protected Double smallGasTankMaxMass;
  protected Double smallGasTankMaxVolume;
  protected Double largeGasTankMass;
  protected Double largeGasTankVolume;
  protected Double largeGasTankMaxMass;
  protected Double largeGasTankMaxVolume;
  protected Double smallLiquidTankMass;
  protected Double smallLiquidTankVolume;
  protected Double smallLiquidTankMaxMass;
  protected Double smallLiquidTankMaxVolume;
  protected Double largeLiquidTankMass;
  protected Double largeLiquidTankVolume;
  protected Double largeLiquidTankMaxMass;
  protected Double largeLiquidTankMaxVolume;
  protected Double cargoTransferBagMass;
  protected Double cargoTransferBagVolume;
  protected Double cargoTransferBagMaxMass;
  protected Double cargoTransferBagMaxVolume;

  public static Configuration createFrom(edu.mit.spacenet.scenario.Scenario scenario,
      Context context) {
    Configuration c = new Configuration();
    c.timePrecision = scenario.getTimePrecision();
    c.demandPrecision = scenario.getDemandPrecision();
    c.massPrecision = scenario.getMassPrecision();
    c.volumePrecision = scenario.getVolumePrecision();
    c.volumeConstrained = scenario.isVolumeConstrained();
    c.environmentConstrained = scenario.isEnvironmentConstrained();
    c.itemDiscretization = scenario.getItemDiscretization().getName();
    c.itemAggregation = scenario.getItemAggregation();
    c.scavengeSpares = scenario.isScavengeSpares();
    // TODO add repaired items
    c.detailedEva = scenario.isDetailedEva();
    c.detailedExploration = scenario.isDetailedExploration();
    c.genericPackingFactorGas = scenario.getGenericPackingFactorGas();
    c.genericPackingFactorLiquid = scenario.getGenericPackingFactorLiquid();
    c.genericPackingFactorPressurized = scenario.getGenericPackingFactorPressurized();
    c.genericPackingFactorUnpressurized = scenario.getGenericPackingFactorUnpressurized();
    c.smallGasTankMass = scenario.getSmallGasTankMass();
    c.smallGasTankVolume = scenario.getSmallGasTankVolume();
    c.smallGasTankMaxMass = scenario.getSmallGasTankMaxMass();
    c.smallGasTankMaxVolume = scenario.getSmallGasTankMaxVolume();
    c.largeGasTankMass = scenario.getLargeGasTankMass();
    c.largeGasTankVolume = scenario.getLargeGasTankVolume();
    c.largeGasTankMaxMass = scenario.getLargeGasTankMaxMass();
    c.largeGasTankMaxVolume = scenario.getLargeGasTankMaxVolume();
    c.smallLiquidTankMass = scenario.getSmallLiquidTankMass();
    c.smallLiquidTankVolume = scenario.getSmallLiquidTankVolume();
    c.smallLiquidTankMaxMass = scenario.getSmallLiquidTankMaxMass();
    c.smallLiquidTankMaxVolume = scenario.getSmallLiquidTankMaxVolume();
    c.largeLiquidTankMass = scenario.getLargeLiquidTankMass();
    c.largeLiquidTankVolume = scenario.getLargeLiquidTankVolume();
    c.largeLiquidTankMaxMass = scenario.getLargeLiquidTankMaxMass();
    c.largeLiquidTankMaxVolume = scenario.getLargeLiquidTankMaxVolume();
    c.cargoTransferBagMass = scenario.getCargoTransferBagMass();
    c.cargoTransferBagVolume = scenario.getCargoTransferBagVolume();
    c.cargoTransferBagMaxMass = scenario.getCargoTransferBagMaxMass();
    c.cargoTransferBagMaxVolume = scenario.getCargoTransferBagMaxVolume();

    return c;
  }
}
