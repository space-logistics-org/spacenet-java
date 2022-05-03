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
package edu.mit.spacenet.gui.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.spacenet.domain.model.CrewConsumablesDemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The panel for viewing and editing crew consumables mission-level demand models.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class CrewConsumablesDemandModelPanel extends AbstractDemandModelPanel {
  private static final long serialVersionUID = -966209133130231178L;

  private CrewConsumablesDemandModel demandModel;

  private SpinnerNumberModel crewSizeModel, crewTimeModel, explorationDurationModel,
      transitDurationModel, reservesDurationModel, waterRecoveryModel, clothingLifetimeModel;
  private JSpinner crewSizeSpinner, crewTimeSpinner, explorationDurationSpinner,
      transitDurationSpinner, reservesDurationSpinner, waterRecoverySpinner,
      clothingLifetimeSpinner;
  private JCheckBox transitDemandsCheck;
  private SpinnerNumberModel waterRateModel, evaWaterRateModel, foodSupportRateModel,
      ambientFoodRateModel, rfFoodRateModel, oxygenRateModel, evaOxygenRateModel, nitrogenRateModel,
      hygieneRateModel, hygieneKitModel, clothingRateModel, personalItemsModel,
      officeEquipmentModel, evaSuitModel, evaLithiumHydroxideModel, healthEquipmentModel,
      healthConsumablesModel, safetyEquipmentModel, commEquipmentModel, computerEquipmentModel,
      trashBagRateModel, wasteContainmentRateModel;
  private JSpinner waterRateSpinner, evaWaterRateSpinner, foodSupportRateSpinner,
      ambientFoodRateSpinner, rfFoodRateSpinner, oxygenRateSpinner, evaOxygenRateSpinner,
      nitrogenRateSpinner, hygieneRateSpinner, hygieneKitSpinner, clothingRateSpinner,
      personalItemsSpinner, officeEquipmentSpinner, evaSuitSpinner, evaLithiumHydroxideSpinner,
      healthEquipmentSpinner, healthConsumablesSpinner, safetyEquipmentSpinner,
      commEquipmentSpinner, computerEquipmentSpinner, trashBagRateSpinner,
      wasteContainmentRateSpinner;
  private DemandTable demandTable;

  /**
   * Instantiates a new crew consumables demand model panel.
   * 
   * @param demandModelDialog the demand model dialog
   * @param demandModel the demand model
   */
  public CrewConsumablesDemandModelPanel(DemandModelDialog demandModelDialog,
      CrewConsumablesDemandModel demandModel) {
    super(demandModelDialog, demandModel);
    this.demandModel = demandModel;
    buildPanel();
    initialize();
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.add(buildInputsPanel(), "Inputs");
    tabbedPane.add(buildRatesPanel(), "Rates");
    tabbedPane.add(buildDemandsPanel(), "Demands");
    tabbedPane.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        updateDemands();
      }
    });
    setLayout(new BorderLayout());
    add(tabbedPane, BorderLayout.CENTER);
  }

  /**
   * Builds the inputs panel.
   * 
   * @return the inputs panel
   */
  private JPanel buildInputsPanel() {
    JPanel inputsPanel = new JPanel();
    inputsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    inputsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    inputsPanel.add(new JLabel("Crew Size: "), c);
    c.gridy++;
    inputsPanel.add(new JLabel("EVA Crew Time: "), c);
    c.gridy++;
    inputsPanel.add(new JLabel("Exploration Duration: "), c);
    c.gridy++;
    c.gridy++;
    inputsPanel.add(new JLabel("Transit Duration: "), c);
    c.gridy++;
    inputsPanel.add(new JLabel("Reserves Duration: "), c);
    c.gridy++;
    inputsPanel.add(new JLabel("Water Recovery Rate: "), c);
    c.gridy++;
    inputsPanel.add(new JLabel("Clothing Lifetime"), c);
    c.gridy = 0;
    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.weightx = 1;
    crewSizeModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    crewSizeSpinner = new JSpinner(crewSizeModel);
    crewSizeSpinner.setEnabled(false);
    crewSizeSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(crewSizeSpinner, "crew"), c);
    c.gridy++;
    crewTimeModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 0.25);
    crewTimeSpinner = new JSpinner(crewTimeModel);
    crewTimeSpinner.setEnabled(false);
    crewTimeSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(crewTimeSpinner, "crew-hours"), c);
    c.gridy++;
    explorationDurationModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
    explorationDurationSpinner = new JSpinner(explorationDurationModel);
    explorationDurationSpinner.setEnabled(false);
    explorationDurationSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(explorationDurationSpinner, "days"), c);
    c.gridy++;
    transitDemandsCheck = new JCheckBox("Omit Transit Demands");
    transitDemandsCheck.setOpaque(false);
    inputsPanel.add(transitDemandsCheck, c);
    c.gridy++;
    transitDurationModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
    transitDurationSpinner = new JSpinner(transitDurationModel);
    transitDurationSpinner.setEnabled(false);
    transitDurationSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(transitDurationSpinner, "days"), c);
    c.gridy++;
    reservesDurationModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
    reservesDurationSpinner = new JSpinner(reservesDurationModel);
    reservesDurationSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(reservesDurationSpinner, "days"), c);
    c.gridy++;
    waterRecoveryModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
    waterRecoverySpinner = new JSpinner(waterRecoveryModel);
    waterRecoverySpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(waterRecoverySpinner, "%"), c);
    c.gridy++;
    clothingLifetimeModel =
        new SpinnerNumberModel(1, 1, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
    clothingLifetimeSpinner = new JSpinner(clothingLifetimeModel);
    clothingLifetimeSpinner.setPreferredSize(new Dimension(50, 20));
    inputsPanel.add(new UnitsWrapper(clothingLifetimeSpinner, "days"), c);
    c.gridy++;
    c.weighty = 1;
    inputsPanel.add(new JLabel(), c);
    return inputsPanel;
  }

  /**
   * Builds the rates panel.
   * 
   * @return the rates panel
   */
  private JPanel buildRatesPanel() {
    JPanel ratesPanel = new JPanel();
    ratesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    ratesPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    ratesPanel.add(new JLabel("Water: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("EVA Water: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Food Support: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Ambient Food: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("R/F Food: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Oxygen: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("EVA Oxygen: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Nitrogen: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Hygiene Items: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Hygiene Kits: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Clothing: "), c);
    c.gridx += 2;
    c.gridy = 0;
    ratesPanel.add(new JLabel("Personal Items: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Office Equipment: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("EVA Suits: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("EVA LiOH: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Health Equipment: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Health Consumables: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Safety Equipment: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Comm Equipment: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Computer Equipment: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Trash Bags: "), c);
    c.gridy++;
    ratesPanel.add(new JLabel("Waste Containment: "), c);
    c.gridy = 0;
    c.gridx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    waterRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    waterRateSpinner = new JSpinner(waterRateModel);
    waterRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(waterRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    evaWaterRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    evaWaterRateSpinner = new JSpinner(evaWaterRateModel);
    evaWaterRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(evaWaterRateSpinner, "kg/crew/hour"), c);
    c.gridy++;
    foodSupportRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    foodSupportRateSpinner = new JSpinner(foodSupportRateModel);
    foodSupportRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(foodSupportRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    ambientFoodRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    ambientFoodRateSpinner = new JSpinner(ambientFoodRateModel);
    ambientFoodRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(ambientFoodRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    rfFoodRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    rfFoodRateSpinner = new JSpinner(rfFoodRateModel);
    rfFoodRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(rfFoodRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    oxygenRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    oxygenRateSpinner = new JSpinner(oxygenRateModel);
    oxygenRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(oxygenRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    evaOxygenRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    evaOxygenRateSpinner = new JSpinner(evaOxygenRateModel);
    evaOxygenRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(evaOxygenRateSpinner, "kg/crew/hour"), c);
    c.gridy++;
    nitrogenRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    nitrogenRateSpinner = new JSpinner(nitrogenRateModel);
    nitrogenRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(nitrogenRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    hygieneRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    hygieneRateSpinner = new JSpinner(hygieneRateModel);
    hygieneRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(hygieneRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    hygieneKitModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    hygieneKitSpinner = new JSpinner(hygieneKitModel);
    hygieneKitSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(hygieneKitSpinner, "kg/crew"), c);
    c.gridy++;
    clothingRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    clothingRateSpinner = new JSpinner(clothingRateModel);
    clothingRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(clothingRateSpinner, "kg/crew/change"), c);
    c.gridy = 0;
    c.gridx += 2;
    personalItemsModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    personalItemsSpinner = new JSpinner(personalItemsModel);
    personalItemsSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(personalItemsSpinner, "kg/crew"), c);
    c.gridy++;
    officeEquipmentModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    officeEquipmentSpinner = new JSpinner(officeEquipmentModel);
    officeEquipmentSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(officeEquipmentSpinner, "kg/crew"), c);
    c.gridy++;
    evaSuitModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    evaSuitSpinner = new JSpinner(evaSuitModel);
    evaSuitSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(evaSuitSpinner, "kg/crew"), c);
    c.gridy++;
    evaLithiumHydroxideModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    evaLithiumHydroxideSpinner = new JSpinner(evaLithiumHydroxideModel);
    evaLithiumHydroxideSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(evaLithiumHydroxideSpinner, "kg/crew/hour"), c);
    c.gridy++;
    healthEquipmentModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    healthEquipmentSpinner = new JSpinner(healthEquipmentModel);
    healthEquipmentSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(healthEquipmentSpinner, "kg"), c);
    c.gridy++;
    healthConsumablesModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    healthConsumablesSpinner = new JSpinner(healthConsumablesModel);
    healthConsumablesSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(healthConsumablesSpinner, "kg/crew/day"), c);
    c.gridy++;
    safetyEquipmentModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    safetyEquipmentSpinner = new JSpinner(safetyEquipmentModel);
    safetyEquipmentSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(safetyEquipmentSpinner, "kg"), c);
    c.gridy++;
    commEquipmentModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    commEquipmentSpinner = new JSpinner(commEquipmentModel);
    commEquipmentSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(commEquipmentSpinner, "kg"), c);
    c.gridy++;
    computerEquipmentModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    computerEquipmentSpinner = new JSpinner(computerEquipmentModel);
    computerEquipmentSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(computerEquipmentSpinner, "kg/crew"), c);
    c.gridy++;
    trashBagRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    trashBagRateSpinner = new JSpinner(trashBagRateModel);
    trashBagRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(trashBagRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    wasteContainmentRateModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getDemandPrecision());
    wasteContainmentRateSpinner = new JSpinner(wasteContainmentRateModel);
    wasteContainmentRateSpinner.setPreferredSize(new Dimension(50, 20));
    ratesPanel.add(new UnitsWrapper(wasteContainmentRateSpinner, "kg/crew/day"), c);
    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 4;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JButton resetButton = new JButton("Reset Default Rates");
    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        demandModel.resetDefaultRates();
        initialize();
      }
    });
    ratesPanel.add(resetButton, c);
    return ratesPanel;
  }

  /**
   * Initializes the panel for a new demand model.
   */
  private void initialize() {
    crewSizeModel.setValue(demandModel.getMissionCrewSize());
    crewTimeModel.setValue(demandModel.getMissionEvaCrewTime());
    explorationDurationModel.setValue(demandModel.getMissionExplorationDuration());
    transitDemandsCheck.setSelected(demandModel.isTransitDemandsOmitted());
    transitDurationModel.setValue(demandModel.getMissionTransitDuration());
    reservesDurationModel.setValue(demandModel.getReservesDuration());
    waterRecoveryModel.setValue(100 * demandModel.getWaterRecoveryRate());
    clothingLifetimeModel.setValue(demandModel.getClothingLifetime());

    waterRateModel.setValue(demandModel.getWaterRate());
    evaWaterRateModel.setValue(demandModel.getEvaWaterRate());
    foodSupportRateModel.setValue(demandModel.getFoodSupportRate());
    ambientFoodRateModel.setValue(demandModel.getAmbientFoodRate());
    rfFoodRateModel.setValue(demandModel.getRfFoodRate());
    oxygenRateModel.setValue(demandModel.getOxygenRate());
    evaOxygenRateModel.setValue(demandModel.getEvaOxygenRate());
    nitrogenRateModel.setValue(demandModel.getNitrogenRate());
    hygieneRateModel.setValue(demandModel.getHygieneRate());
    hygieneKitModel.setValue(demandModel.getHygieneKit());
    clothingRateModel.setValue(demandModel.getClothingRate());
    personalItemsModel.setValue(demandModel.getPersonalItems());
    officeEquipmentModel.setValue(demandModel.getOfficeEquipment());
    evaSuitModel.setValue(demandModel.getEvaSuit());
    evaLithiumHydroxideModel.setValue(demandModel.getEvaLithiumHydroxide());
    healthEquipmentModel.setValue(demandModel.getHealthEquipment());
    healthConsumablesModel.setValue(demandModel.getHealthConsumables());
    safetyEquipmentModel.setValue(demandModel.getSafetyEquipment());
    commEquipmentModel.setValue(demandModel.getCommEquipment());
    computerEquipmentModel.setValue(demandModel.getComputerEquipment());
    trashBagRateModel.setValue(demandModel.getTrashBagRate());
    wasteContainmentRateModel.setValue(demandModel.getWasteContainmentRate());
  }

  /**
   * Builds the demands panel.
   * 
   * @return the demands panel
   */
  private JPanel buildDemandsPanel() {
    JPanel demandsPanel = new JPanel();
    demandsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.CENTER;
    c.weighty = 1;
    c.weightx = 1;
    demandTable = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario()
        .getDataSource().getResourceLibrary());
    demandTable.setEnabled(false);
    JScrollPane demandScroll = new JScrollPane(demandTable);
    demandScroll.setPreferredSize(new Dimension(300, 200));
    demandsPanel.add(demandScroll, c);
    return demandsPanel;
  }

  /**
   * Updates the demands.
   */
  private void updateDemands() {
    saveDemandModel();
    demandTable.removeAllDemands();
    for (Demand demand : demandModel.generateDemands(0, null)) {
      demandTable.addDemand(demand);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#getDemandModel()
   */
  public CrewConsumablesDemandModel getDemandModel() {
    return demandModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#saveDemandModel()
   */
  public void saveDemandModel() {
    demandModel.setTransitDemandsOmitted(transitDemandsCheck.isSelected());
    demandModel.setReservesDuration(reservesDurationModel.getNumber().doubleValue());
    demandModel.setWaterRecoveryRate(waterRecoveryModel.getNumber().doubleValue() / 100d);
    demandModel.setClothingLifetime(clothingLifetimeModel.getNumber().doubleValue());

    demandModel.setWaterRate(waterRateModel.getNumber().doubleValue());
    demandModel.setEvaWaterRate(evaWaterRateModel.getNumber().doubleValue());
    demandModel.setFoodSupportRate(foodSupportRateModel.getNumber().doubleValue());
    demandModel.setAmbientFoodRate(ambientFoodRateModel.getNumber().doubleValue());
    demandModel.setRfFoodRate(rfFoodRateModel.getNumber().doubleValue());
    demandModel.setOxygenRate(oxygenRateModel.getNumber().doubleValue());
    demandModel.setEvaOxygenRate(evaOxygenRateModel.getNumber().doubleValue());
    demandModel.setNitrogenRate(nitrogenRateModel.getNumber().doubleValue());
    demandModel.setHygieneRate(hygieneRateModel.getNumber().doubleValue());
    demandModel.setHygieneKit(hygieneKitModel.getNumber().doubleValue());
    demandModel.setClothingRate(clothingRateModel.getNumber().doubleValue());
    demandModel.setPersonalItems(personalItemsModel.getNumber().doubleValue());
    demandModel.setOfficeEquipment(officeEquipmentModel.getNumber().doubleValue());
    demandModel.setEvaSuit(evaSuitModel.getNumber().doubleValue());
    demandModel.setEvaLithiumHydroxide(evaLithiumHydroxideModel.getNumber().doubleValue());
    demandModel.setHealthEquipment(healthEquipmentModel.getNumber().doubleValue());
    demandModel.setHealthConsumables(healthConsumablesModel.getNumber().doubleValue());
    demandModel.setSafetyEquipment(safetyEquipmentModel.getNumber().doubleValue());
    demandModel.setCommEquipment(commEquipmentModel.getNumber().doubleValue());
    demandModel.setComputerEquipment(computerEquipmentModel.getNumber().doubleValue());
    demandModel.setTrashBagRate(trashBagRateModel.getNumber().doubleValue());
    demandModel.setWasteContainmentRate(wasteContainmentRateModel.getNumber().doubleValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#isDemandModelValid()
   */
  @Override
  public boolean isDemandModelValid() {
    return true;
  }
}
