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
package edu.mit.spacenet.gui.element;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Element panel for viewing and editing carrier-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class CarrierPanel extends AbstractElementPanel {
  private static final long serialVersionUID = 6335483106289763369L;

  private Carrier carrier;

  private JSpinner maxCrewSpinner, maxCargoMassSpinner, maxCargoVolumeSpinner;
  private SpinnerNumberModel maxCargoMassModel, maxCargoVolumeModel, maxCrewModel;
  private JComboBox<Environment> cargoEnvironmentCombo;

  /**
   * Instantiates a new carrier panel.
   * 
   * @param elementDialog the element dialog
   * @param carrier the carrier
   */
  public CarrierPanel(ElementDialog elementDialog, Carrier carrier) {
    super(elementDialog, carrier);
    this.carrier = carrier;
    buildPanel();
    initialize();
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridy = 0;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Cargo Environment: "), c);
    c.gridx += 2;
    add(new JLabel("Max Crew Size: "), c);
    c.gridx = 0;
    c.gridy++;
    add(new JLabel("Max Cargo Mass: "), c);
    c.gridx += 2;
    add(new JLabel("Max Cargo Volume: "), c);

    c.gridy = 0;
    c.gridx = 1;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    cargoEnvironmentCombo = new JComboBox<Environment>();
    for (Environment e : Environment.values()) {
      cargoEnvironmentCombo.addItem(e);
    }
    add(cargoEnvironmentCombo, c);
    cargoEnvironmentCombo.setToolTipText("Storage environment available for nested elements");

    c.gridx += 2;
    maxCrewModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    maxCrewSpinner = new JSpinner(maxCrewModel);
    maxCrewSpinner.setPreferredSize(new Dimension(150, 20));
    add(maxCrewSpinner, c);
    maxCrewSpinner.setToolTipText("Maximum number of nested crew");

    c.gridx = 1;
    c.gridy++;
    maxCargoMassModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE,
        GlobalParameters.getSingleton().getMassPrecision());
    maxCargoMassSpinner = new JSpinner(maxCargoMassModel);
    maxCargoMassSpinner.setPreferredSize(new Dimension(150, 20));
    add(new UnitsWrapper(maxCargoMassSpinner, "kg"), c);
    maxCargoMassSpinner.setToolTipText("Maximum mass of nested elements [kilograms]");

    c.gridx += 2;
    maxCargoVolumeModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE,
        GlobalParameters.getSingleton().getVolumePrecision());
    maxCargoVolumeSpinner = new JSpinner(maxCargoVolumeModel);
    maxCargoVolumeSpinner.setPreferredSize(new Dimension(150, 20));
    add(new UnitsWrapper(maxCargoVolumeSpinner, "m^3"), c);
    maxCargoVolumeSpinner.setToolTipText("Maximum volume of nested elements [cubic meters]");
  }

  /**
   * Initializes the panel components for a carrier.
   */
  private void initialize() {
    cargoEnvironmentCombo.setSelectedItem(carrier.getCargoEnvironment());
    maxCargoMassModel.setValue(carrier.getMaxCargoMass());
    maxCargoVolumeModel.setValue(carrier.getMaxCargoVolume());
    maxCrewModel.setValue(carrier.getMaxCrewSize());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
   */
  @Override
  public Carrier getElement() {
    return carrier;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
   */
  @Override
  public void saveElement() {
    carrier.setCargoEnvironment((Environment) cargoEnvironmentCombo.getSelectedItem());
    carrier.setMaxCargoMass(maxCargoMassModel.getNumber().doubleValue());
    carrier.setMaxCargoVolume(maxCargoVolumeModel.getNumber().doubleValue());
    carrier.setMaxCrewSize(maxCrewModel.getNumber().intValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
   */
  @Override
  public boolean isElementValid() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isVerticallyExpandable()
   */
  public boolean isVerticallyExpandable() {
    return false;
  }
}
