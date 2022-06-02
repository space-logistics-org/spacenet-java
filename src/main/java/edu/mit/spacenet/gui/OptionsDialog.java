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
package edu.mit.spacenet.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.scenario.ItemDiscretization;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Dialog that displays options related to a specific scenario.
 * 
 * @author Paul Grogan
 */
public class OptionsDialog extends JDialog {
  private static final long serialVersionUID = -802497885876127342L;

  private ScenarioPanel scenarioPanel;

  private JPanel precisionPanel, constraintsPanel, demandsPanel, packingPanel, containersPanel, simulationPanel;
  private SpinnerNumberModel timeModel, demandModel, massModel, volumeModel;
  private JSpinner timeSpinner, demandSpinner, massSpinner, volumeSpinner;
  private JCheckBox volumeConstrained, environmentConstrained;

  private JComboBox<ItemDiscretization> discretizationCombo;
  private JSlider aggregationSlider;
  private JCheckBox scavengeSparesCheck;

  private SpinnerNumberModel gasModel, liquidModel, pressurizedModel, unpressurizedModel;
  private JSpinner gasSpinner, liquidSpinner, pressurizedSpinner, unpressurizedSpinner;
  
  private SpinnerNumberModel smallGasMassModel, smallGasVolumeModel, smallGasMaxMassModel, smallGasMaxVolumeModel;
  private SpinnerNumberModel largeGasMassModel, largeGasVolumeModel, largeGasMaxMassModel, largeGasMaxVolumeModel;
  private SpinnerNumberModel smallLiquidMassModel, smallLiquidVolumeModel, smallLiquidMaxMassModel, smallLiquidMaxVolumeModel;
  private SpinnerNumberModel largeLiquidMassModel, largeLiquidVolumeModel, largeLiquidMaxMassModel, largeLiquidMaxVolumeModel;
  private SpinnerNumberModel cargoMassModel, cargoVolumeModel, cargoMaxMassModel, cargoMaxVolumeModel;
  
  private JSpinner smallGasMassSpinner, smallGasVolumeSpinner, smallGasMaxMassSpinner, smallGasMaxVolumeSpinner;
  private JSpinner largeGasMassSpinner, largeGasVolumeSpinner, largeGasMaxMassSpinner, largeGasMaxVolumeSpinner;
  private JSpinner smallLiquidMassSpinner, smallLiquidVolumeSpinner, smallLiquidMaxMassSpinner, smallLiquidMaxVolumeSpinner;
  private JSpinner largeLiquidMassSpinner, largeLiquidVolumeSpinner, largeLiquidMaxMassSpinner, largeLiquidMaxVolumeSpinner;
  private JSpinner cargoMassSpinner, cargoVolumeSpinner, cargoMaxMassSpinner, cargoMaxVolumeSpinner;
  
  private JCheckBox explorationCheck, evaCheck;

  /**
   * Instantiates a new options dialog.
   *
   * @param scenarioPanel the scenario panel
   */
  public OptionsDialog(ScenarioPanel scenarioPanel) {
    super(scenarioPanel.getSpaceNetFrame(), "Scenario Options", true);
    this.scenarioPanel = scenarioPanel;

    buildDialog();
    setResizable(false);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  /**
   * Builds the dialog.
   */
  private void buildDialog() {
    buildPrecisionPanel();
    buildConstraintsPanel();
    buildDemandsPanel();
    buildContainersPanel();
    buildPackingPanel();
    buildSimulationPanel();

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
    wrapper.add(precisionPanel);
    wrapper.add(constraintsPanel);
    wrapper.add(packingPanel);
    wrapper.add(containersPanel);
    wrapper.add(demandsPanel);
    wrapper.add(simulationPanel);
    contentPanel.add(wrapper, c);
    c.gridy++;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LAST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveOptions();
        scenarioPanel.updateView();
        dispose();
      }
    });
    buttonPanel.add(okButton, c);
    getRootPane().setDefaultButton(okButton);
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    buttonPanel.add(cancelButton, c);
    contentPanel.add(buttonPanel, c);

    setModal(true);
    setContentPane(contentPanel);
    setMinimumSize(new Dimension(300, 250));
  }

  /**
   * Builds the precision panel.
   */
  private void buildPrecisionPanel() {
    precisionPanel = new JPanel();
    precisionPanel.setBorder(BorderFactory.createTitledBorder("Numerical Precision"));
    precisionPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    precisionPanel.add(new JLabel("Sets enforced precision of numerical values."), c);
    c.gridy++;
    c.gridwidth = 1;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    precisionPanel.add(new JLabel("Time Precision: "), c);
    c.gridy++;
    precisionPanel.add(new JLabel("Demand Precision: "), c);
    c.gridy--;
    c.gridx += 2;
    precisionPanel.add(new JLabel("Mass Precision: "), c);
    c.gridy++;
    precisionPanel.add(new JLabel("Volume Precision: "), c);
    c.gridx--;
    c.gridy--;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    timeModel = new SpinnerNumberModel(0.001, 0.001, 1.000, 0.001);
    timeSpinner = new JSpinner(timeModel);
    precisionPanel.add(new UnitsWrapper(timeSpinner, "days"), c);
    c.gridy++;
    demandModel = new SpinnerNumberModel(0.001, 0.001, 1.000, 0.001);
    demandSpinner = new JSpinner(demandModel);
    precisionPanel.add(new UnitsWrapper(demandSpinner, "units"), c);
    c.gridy--;
    c.gridx += 2;
    massModel = new SpinnerNumberModel(0.001, 0.001, 1.000, 0.001);
    massModel.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        smallGasMassModel.setStepSize(massModel.getNumber());
        smallGasMaxMassModel.setStepSize(massModel.getNumber());
        largeGasMassModel.setStepSize(massModel.getNumber());
        largeGasMaxMassModel.setStepSize(massModel.getNumber());
        smallLiquidMassModel.setStepSize(massModel.getNumber());
        smallLiquidMaxMassModel.setStepSize(massModel.getNumber());
        largeLiquidMassModel.setStepSize(massModel.getNumber());
        largeLiquidMaxMassModel.setStepSize(massModel.getNumber());
        cargoMassModel.setStepSize(massModel.getNumber());
        cargoMaxMassModel.setStepSize(massModel.getNumber());
      }
    });
    massSpinner = new JSpinner(massModel);
    precisionPanel.add(new UnitsWrapper(massSpinner, "kg"), c);
    c.gridy++;
    volumeModel = new SpinnerNumberModel(0.1, 0.1, 100., 0.1);
    volumeModel.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        smallGasVolumeModel.setStepSize(massModel.getNumber());
        smallGasMaxVolumeModel.setStepSize(massModel.getNumber());
        largeGasVolumeModel.setStepSize(massModel.getNumber());
        largeGasMaxVolumeModel.setStepSize(massModel.getNumber());
        smallLiquidVolumeModel.setStepSize(massModel.getNumber());
        smallLiquidMaxVolumeModel.setStepSize(massModel.getNumber());
        largeLiquidVolumeModel.setStepSize(massModel.getNumber());
        largeLiquidMaxVolumeModel.setStepSize(massModel.getNumber());
        cargoVolumeModel.setStepSize(massModel.getNumber());
        cargoMaxVolumeModel.setStepSize(massModel.getNumber());
      }
    });
    volumeSpinner = new JSpinner(volumeModel);
    precisionPanel.add(new UnitsWrapper(volumeSpinner, "cm^3"), c);
  }

  /**
   * Builds the constraints panel.
   */
  private void buildConstraintsPanel() {
    constraintsPanel = new JPanel();
    constraintsPanel.setBorder(BorderFactory.createTitledBorder("Cargo Constraints"));
    constraintsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    constraintsPanel.add(
        new JLabel("Sets constraints enforced for packing cargo in containers or carriers."), c);
    c.gridy++;
    volumeConstrained = new JCheckBox("Volume Enforced");
    volumeConstrained.setOpaque(false);
    constraintsPanel.add(volumeConstrained, c);
    c.gridy++;
    environmentConstrained = new JCheckBox("Environment Enforced");
    environmentConstrained.setOpaque(false);
    constraintsPanel.add(environmentConstrained, c);
  }

  /**
   * Builds the demands panel.
   */
  private void buildDemandsPanel() {
    demandsPanel = new JPanel();
    demandsPanel.setBorder(BorderFactory.createTitledBorder("Discrete Demands"));
    demandsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    c.gridwidth = 2;
    demandsPanel.add(new JLabel("Configures demands for discrete items."), c);
    c.gridy++;
    c.gridwidth = 1;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    demandsPanel.add(new JLabel("Item Discretization: "), c);
    c.gridy++;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    demandsPanel.add(new JLabel("Item Aggregation: "), c);
    c.weightx = 1;
    c.gridx = 1;
    c.gridy = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    discretizationCombo = new JComboBox<ItemDiscretization>();
    discretizationCombo
        .setToolTipText("Discretize demands for items to integer values at the selected level");
    for (ItemDiscretization t : ItemDiscretization.values()) {
      discretizationCombo.addItem(t);
    }
    discretizationCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          aggregationSlider
              .setEnabled(discretizationCombo.getSelectedItem() != ItemDiscretization.NONE);
        }
      }
    });
    demandsPanel.add(discretizationCombo, c);
    c.gridy++;
    aggregationSlider = new JSlider(JSlider.VERTICAL, 0, 4, 0);
    aggregationSlider.setOpaque(false);
    aggregationSlider.setFocusable(false);
    aggregationSlider.setToolTipText("Aggregate discretized item demands ahead or behind demands.");
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    labelTable.put(new Integer(0), new JLabel("First Demand", JLabel.LEFT));
    labelTable.put(new Integer(2), new JLabel("Half Demand", JLabel.LEFT));
    labelTable.put(new Integer(4), new JLabel("Unit Demand", JLabel.RIGHT));
    aggregationSlider.setLabelTable(labelTable);
    aggregationSlider.setPaintLabels(true);
    aggregationSlider.setMajorTickSpacing(1);
    aggregationSlider.setSnapToTicks(true);
    aggregationSlider.setPaintTicks(true);
    aggregationSlider.setPreferredSize(new Dimension(100, 100));
    demandsPanel.add(aggregationSlider, c);
    c.gridy++;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = 2;
    c.gridx = 0;
    scavengeSparesCheck = new JCheckBox("Scavenge Spares");
    scavengeSparesCheck.setOpaque(false);
    demandsPanel.add(scavengeSparesCheck, c);
  }

  /**
   * Builds the packing panel.
   */
  private void buildPackingPanel() {
    packingPanel = new JPanel();
    packingPanel.setBorder(BorderFactory.createTitledBorder("Generic Packing Factors"));
    packingPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    c.gridwidth = 4;
    packingPanel.add(new JLabel("Sets the estimated packing factors for generic resources."), c);
    c.gridy++;
    c.weightx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    packingPanel.add(new JLabel("Gas: "), c);
    c.gridy++;
    packingPanel.add(new JLabel("Liquid: "), c);
    c.gridx += 2;
    c.gridy--;
    packingPanel.add(new JLabel("Pressurized: "), c);
    c.gridy++;
    packingPanel.add(new JLabel("Unpressurized: "), c);
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    c.gridy--;
    c.gridx--;
    gasModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1);
    gasSpinner = new JSpinner(gasModel);
    packingPanel.add(new UnitsWrapper(gasSpinner, "% (Default: 100%)"), c);
    c.gridy++;
    liquidModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1);
    liquidSpinner = new JSpinner(liquidModel);
    packingPanel.add(new UnitsWrapper(liquidSpinner, "% (Default: 50%)"), c);
    c.gridx += 2;
    c.gridy--;
    pressurizedModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1);
    pressurizedSpinner = new JSpinner(pressurizedModel);
    packingPanel.add(new UnitsWrapper(pressurizedSpinner, "% (Default: 20%)"), c);
    c.gridy++;
    unpressurizedModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1);
    unpressurizedSpinner = new JSpinner(unpressurizedModel);
    packingPanel.add(new UnitsWrapper(unpressurizedSpinner, "% (Default: 60%)"), c);
  }


  /**
   * Builds the containers panel.
   */
  private void buildContainersPanel() {
    containersPanel = new JPanel();
    containersPanel.setBorder(BorderFactory.createTitledBorder("Resource Containers"));
    containersPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    c.gridwidth = 5;
    containersPanel.add(
        new JLabel("Customizes resource containers used in manifesting."), c);
    c.gridy++;
    c.weightx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.CENTER;
    containersPanel.add(new JLabel("Container"), c);
    c.gridx++;
    containersPanel.add(new JLabel("Mass (kg)"), c);
    c.gridx++;
    containersPanel.add(new JLabel("Volume (cm^3)"), c);
    c.gridx++;
    containersPanel.add(new JLabel("Max Mass (kg)"), c);
    c.gridx++;
    containersPanel.add(new JLabel("Max Volume (cm^3)"), c);
    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    containersPanel.add(new JLabel("Gas Tank (Small): "), c);
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 1;
    c.gridx++;
    smallGasMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    smallGasMassSpinner = new JSpinner(smallGasMassModel);
    smallGasMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallGasMassSpinner, c);
    c.gridx++;
    smallGasVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    smallGasVolumeSpinner = new JSpinner(smallGasVolumeModel);
    smallGasVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallGasVolumeSpinner, c);
    c.gridx++;
    smallGasMaxMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    smallGasMaxMassSpinner = new JSpinner(smallGasMaxMassModel);
    smallGasMaxMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallGasMaxMassSpinner, c);
    c.gridx++;
    smallGasMaxVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    smallGasMaxVolumeSpinner = new JSpinner(smallGasMaxVolumeModel);
    smallGasMaxVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallGasMaxVolumeSpinner, c);
    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    containersPanel.add(new JLabel("Gas Tank (Large): "), c);
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 1;
    c.gridx++;
    largeGasMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    largeGasMassSpinner = new JSpinner(largeGasMassModel);
    largeGasMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeGasMassSpinner, c);
    c.gridx++;
    largeGasVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    largeGasVolumeSpinner = new JSpinner(largeGasVolumeModel);
    largeGasVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeGasVolumeSpinner, c);
    c.gridx++;
    largeGasMaxMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    largeGasMaxMassSpinner = new JSpinner(largeGasMaxMassModel);
    largeGasMaxMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeGasMaxMassSpinner, c);
    c.gridx++;
    largeGasMaxVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    largeGasMaxVolumeSpinner = new JSpinner(largeGasMaxVolumeModel);
    largeGasMaxVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeGasMaxVolumeSpinner, c);
    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    containersPanel.add(new JLabel("Liquid Tank (Small): "), c);
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 1;
    c.gridx++;
    smallLiquidMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    smallLiquidMassSpinner = new JSpinner(smallLiquidMassModel);
    smallLiquidMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallLiquidMassSpinner, c);
    c.gridx++;
    smallLiquidVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    smallLiquidVolumeSpinner = new JSpinner(smallLiquidVolumeModel);
    smallLiquidVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallLiquidVolumeSpinner, c);
    c.gridx++;
    smallLiquidMaxMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    smallLiquidMaxMassSpinner = new JSpinner(smallLiquidMaxMassModel);
    smallLiquidMaxMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallLiquidMaxMassSpinner, c);
    c.gridx++;
    smallLiquidMaxVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    smallLiquidMaxVolumeSpinner = new JSpinner(smallLiquidMaxVolumeModel);
    smallLiquidMaxVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(smallLiquidMaxVolumeSpinner, c);
    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    containersPanel.add(new JLabel("Liquid Tank (Large): "), c);
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 1;
    c.gridx++;
    largeLiquidMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    largeLiquidMassSpinner = new JSpinner(largeLiquidMassModel);
    largeLiquidMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeLiquidMassSpinner, c);
    c.gridx++;
    largeLiquidVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    largeLiquidVolumeSpinner = new JSpinner(largeLiquidVolumeModel);
    largeLiquidVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeLiquidVolumeSpinner, c);
    c.gridx++;
    largeLiquidMaxMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    largeLiquidMaxMassSpinner = new JSpinner(largeLiquidMaxMassModel);
    largeLiquidMaxMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeLiquidMaxMassSpinner, c);
    c.gridx++;
    largeLiquidMaxVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    largeLiquidMaxVolumeSpinner = new JSpinner(largeLiquidMaxVolumeModel);
    largeLiquidMaxVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(largeLiquidMaxVolumeSpinner, c);
    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    containersPanel.add(new JLabel("Cargo Transfer Bag: "), c);
    c.anchor = GridBagConstraints.CENTER;
    c.weightx = 1;
    c.gridx++;
    cargoMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    cargoMassSpinner = new JSpinner(cargoMassModel);
    cargoMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(cargoMassSpinner, c);
    c.gridx++;
    cargoVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    cargoVolumeSpinner = new JSpinner(cargoVolumeModel);
    cargoVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(cargoVolumeSpinner, c);
    c.gridx++;
    cargoMaxMassModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getMassPrecision());
    cargoMaxMassSpinner = new JSpinner(cargoMaxMassModel);
    cargoMaxMassSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(cargoMaxMassSpinner, c);
    c.gridx++;
    cargoMaxVolumeModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, GlobalParameters.getSingleton().getVolumePrecision() * 1000000);
    cargoMaxVolumeSpinner = new JSpinner(cargoMaxVolumeModel);
    cargoMaxVolumeSpinner.setPreferredSize(new Dimension(75, 20));
    containersPanel.add(cargoMaxVolumeSpinner, c);
  }

  /**
   * Builds the simulation panel.
   */
  private void buildSimulationPanel() {
    simulationPanel = new JPanel();
    simulationPanel.setBorder(BorderFactory.createTitledBorder("Simulation Detail"));
    simulationPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1;
    simulationPanel.add(new JLabel("Sets the level of fidelity for simulation execution."), c);
    c.gridy++;
    explorationCheck = new JCheckBox("Detailed Explorations (generate EVA events)");
    explorationCheck.setOpaque(false);
    simulationPanel.add(explorationCheck, c);
    c.gridy++;
    evaCheck = new JCheckBox("Detailed EVAs (reconfigure and move elements)");
    evaCheck.setOpaque(false);
    simulationPanel.add(evaCheck, c);
  }

  /**
   * Saves the options.
   */
  private void saveOptions() {
    scenarioPanel.updateView();
    scenarioPanel.getScenario().setTimePrecision(timeModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setDemandPrecision(demandModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setMassPrecision(massModel.getNumber().doubleValue());
    scenarioPanel.getScenario()
        .setVolumePrecision(volumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setVolumeConstrained(volumeConstrained.isSelected());
    scenarioPanel.getScenario().setEnvironmentConstrained(environmentConstrained.isSelected());
    scenarioPanel.getScenario()
        .setItemDiscretization((ItemDiscretization) discretizationCombo.getSelectedItem());
    scenarioPanel.getScenario().setItemAggregation(aggregationSlider.getValue() / 4D);
    scenarioPanel.getScenario().setScavengeSpares(scavengeSparesCheck.isSelected());
    scenarioPanel.getScenario().setDetailedEva(evaCheck.isSelected());
    scenarioPanel.getScenario().setDetailedExploration(explorationCheck.isSelected());
    scenarioPanel.getScenario()
        .setGenericPackingFactorGas(gasModel.getNumber().doubleValue() / 100D);
    scenarioPanel.getScenario()
        .setGenericPackingFactorLiquid(liquidModel.getNumber().doubleValue() / 100D);
    scenarioPanel.getScenario()
        .setGenericPackingFactorPressurized(pressurizedModel.getNumber().doubleValue() / 100D);
    scenarioPanel.getScenario()
        .setGenericPackingFactorUnpressurized(unpressurizedModel.getNumber().doubleValue() / 100D);
    scenarioPanel.getScenario().setSmallGasTankMass(smallGasMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setSmallGasTankVolume(smallGasVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setSmallGasTankMaxMass(smallGasMaxMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setSmallGasTankMaxVolume(smallGasMaxVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setLargeGasTankMass(largeGasMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setLargeGasTankVolume(largeGasVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setLargeGasTankMaxMass(largeGasMaxMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setLargeGasTankMaxVolume(largeGasMaxVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setSmallLiquidTankMass(smallLiquidMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setSmallLiquidTankVolume(smallLiquidVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setSmallLiquidTankMaxMass(smallLiquidMaxMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setSmallLiquidTankMaxVolume(smallLiquidMaxVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setLargeLiquidTankMass(largeLiquidMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setLargeLiquidTankVolume(largeLiquidVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setLargeLiquidTankMaxMass(largeLiquidMaxMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setLargeLiquidTankMaxVolume(largeLiquidMaxVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setCargoTransferBagMass(cargoMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setCargoTransferBagVolume(cargoVolumeModel.getNumber().doubleValue() / 1000000D);
    scenarioPanel.getScenario().setCargoTransferBagMaxMass(cargoMaxMassModel.getNumber().doubleValue());
    scenarioPanel.getScenario().setCargoTransferBagMaxVolume(cargoMaxVolumeModel.getNumber().doubleValue() / 1000000D);
  }

  /**
   * Initializes the dialog with the options data.
   */
  private void initialize() {
    timeModel.setValue(scenarioPanel.getScenario().getTimePrecision());
    demandModel.setValue(scenarioPanel.getScenario().getDemandPrecision());
    massModel.setValue(scenarioPanel.getScenario().getMassPrecision());
    volumeModel.setValue(scenarioPanel.getScenario().getVolumePrecision() * 1000000);

    volumeConstrained.setSelected(scenarioPanel.getScenario().isVolumeConstrained());
    environmentConstrained.setSelected(scenarioPanel.getScenario().isEnvironmentConstrained());

    discretizationCombo.setSelectedItem(scenarioPanel.getScenario().getItemDiscretization());
    aggregationSlider.setEnabled(discretizationCombo.getSelectedItem() != ItemDiscretization.NONE);
    aggregationSlider.setValue((int) (4 * scenarioPanel.getScenario().getItemAggregation()));
    scavengeSparesCheck.setSelected(scenarioPanel.getScenario().isScavengeSpares());

    explorationCheck.setSelected(scenarioPanel.getScenario().isDetailedExploration());
    evaCheck.setSelected(scenarioPanel.getScenario().isDetailedEva());

    gasModel.setValue(scenarioPanel.getScenario().getGenericPackingFactorGas() * 100);
    liquidModel.setValue(scenarioPanel.getScenario().getGenericPackingFactorLiquid() * 100);
    pressurizedModel
        .setValue(scenarioPanel.getScenario().getGenericPackingFactorPressurized() * 100);
    unpressurizedModel
        .setValue(scenarioPanel.getScenario().getGenericPackingFactorUnpressurized() * 100);
    
    smallGasMassModel.setValue(scenarioPanel.getScenario().getSmallGasTankMass());
    smallGasVolumeModel.setValue(scenarioPanel.getScenario().getSmallGasTankVolume() * 1000000);
    smallGasMaxMassModel.setValue(scenarioPanel.getScenario().getSmallGasTankMaxMass());
    smallGasMaxVolumeModel.setValue(scenarioPanel.getScenario().getSmallGasTankMaxVolume() * 1000000);
    largeGasMassModel.setValue(scenarioPanel.getScenario().getLargeGasTankMass());
    largeGasVolumeModel.setValue(scenarioPanel.getScenario().getLargeGasTankVolume() * 1000000);
    largeGasMaxMassModel.setValue(scenarioPanel.getScenario().getLargeGasTankMaxMass());
    largeGasMaxVolumeModel.setValue(scenarioPanel.getScenario().getLargeGasTankMaxVolume() * 1000000);
    smallLiquidMassModel.setValue(scenarioPanel.getScenario().getSmallLiquidTankMass());
    smallLiquidVolumeModel.setValue(scenarioPanel.getScenario().getSmallLiquidTankVolume() * 1000000);
    smallLiquidMaxMassModel.setValue(scenarioPanel.getScenario().getSmallLiquidTankMaxMass());
    smallLiquidMaxVolumeModel.setValue(scenarioPanel.getScenario().getSmallLiquidTankMaxVolume() * 1000000);
    largeLiquidMassModel.setValue(scenarioPanel.getScenario().getLargeLiquidTankMass());
    largeLiquidVolumeModel.setValue(scenarioPanel.getScenario().getLargeLiquidTankVolume() * 1000000);
    largeLiquidMaxMassModel.setValue(scenarioPanel.getScenario().getLargeLiquidTankMaxMass());
    largeLiquidMaxVolumeModel.setValue(scenarioPanel.getScenario().getLargeLiquidTankMaxVolume() * 1000000);
    cargoMassModel.setValue(scenarioPanel.getScenario().getCargoTransferBagMass());
    cargoVolumeModel.setValue(scenarioPanel.getScenario().getCargoTransferBagVolume() * 1000000);
    cargoMaxMassModel.setValue(scenarioPanel.getScenario().getCargoTransferBagMaxMass());
    cargoMaxVolumeModel.setValue(scenarioPanel.getScenario().getCargoTransferBagMaxVolume() * 1000000);
  }

  /**
   * Initializes the view components, re-positions the dialog to the center of the frame, and
   * enabled visibility.
   */
  public void showDialog() {
    initialize();
    pack();
    setLocationRelativeTo(getParent());
    setVisible(true);
  }
}
