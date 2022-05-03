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
package edu.mit.spacenet.gui.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The CapacityPanel class displays capacity progress bars for mass, volume, (if
 * volume-constrained), and crew (if carrier).
 */
public class CapacityPanel extends JPanel {
  private static final long serialVersionUID = 8033464124593156155L;
  private JProgressBar massCapacity, volumeCapacity, crewCapacity;
  private JLabel environmentLabel, massLabel, volumeLabel, crewLabel;
  private DecimalFormat massFormat = new DecimalFormat("0.0"),
      volumeFormat = new DecimalFormat("0.000");

  /**
   * Instantiates a new capacity panel.
   */
  public CapacityPanel() {
    initializePanel();
  }

  /**
   * Initialize panel.
   */
  private void initializePanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.HORIZONTAL;
    environmentLabel = new JLabel();
    environmentLabel.setHorizontalAlignment(JLabel.CENTER);
    add(environmentLabel, c);
    c.gridy++;
    c.gridwidth = 1;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    massCapacity = new JProgressBar(0, 100);
    massCapacity.setStringPainted(true);
    massCapacity.setVisible(false);
    add(massCapacity, c);
    c.gridy++;
    volumeCapacity = new JProgressBar(0, 100);
    volumeCapacity.setStringPainted(true);
    volumeCapacity.setVisible(false);
    add(volumeCapacity, c);
    c.gridy++;
    crewCapacity = new JProgressBar(0, 100);
    crewCapacity.setStringPainted(true);
    crewCapacity.setVisible(false);
    add(crewCapacity, c);
    c.fill = GridBagConstraints.NONE;
    c.gridx++;
    c.gridy = 1;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    massLabel = new JLabel(" Cargo Mass");
    massLabel.setVisible(false);
    add(massLabel, c);
    c.gridy++;
    volumeLabel = new JLabel(" Cargo Volume");
    volumeLabel.setVisible(false);
    add(volumeLabel, c);
    c.gridy++;
    crewLabel = new JLabel(" Crew");
    crewLabel.setVisible(false);
    add(crewLabel, c);
  }

  /**
   * Update capacities.
   *
   * @param element the element
   * @param mass the mass
   * @param volume the volume
   * @param crew the crew
   * @return true, if successful
   */
  public boolean updateCapacities(I_ResourceContainer container, double mass, double volume) {
    boolean hasErrors = false;
    if (GlobalParameters.isEnvironmentConstrained()) {
      environmentLabel.setVisible(true);
      environmentLabel.setText(container.getCargoEnvironment().getName());
    } else {
      environmentLabel.setVisible(false);
    }
    massCapacity.setVisible(true);
    massLabel.setVisible(true);
    if (mass > container.getMaxCargoMass()) {
      hasErrors = true;
      massCapacity.setForeground(new Color(153, 0, 0));
    } else {
      massCapacity.setForeground(new Color(0, 153, 0));
    }
    if (container.getMaxCargoMass() == 0) {
      massCapacity.setValue(100);
    } else {
      massCapacity.setValue((int) (100 * mass / container.getMaxCargoMass()));
    }
    massCapacity.setString(
        massFormat.format(mass) + " / " + massFormat.format(container.getMaxCargoMass()) + " kg");
    if (GlobalParameters.isVolumeConstrained()) {
      volumeCapacity.setVisible(true);
      volumeLabel.setVisible(true);
      if (volume > container.getMaxCargoVolume()) {
        hasErrors = hasErrors || GlobalParameters.isVolumeConstrained();
        volumeCapacity.setForeground(new Color(153, 0, 0));
      } else {
        volumeCapacity.setForeground(new Color(0, 153, 0));
      }
      if (container.getMaxCargoVolume() == 0) {
        volumeCapacity.setValue(100);
      } else {
        volumeCapacity.setValue((int) (100 * volume / container.getMaxCargoVolume()));
      }
      volumeCapacity.setString(volumeFormat.format(volume) + " / "
          + volumeFormat.format(container.getMaxCargoVolume()) + " m^3");
    } else {
      volumeCapacity.setVisible(false);
      volumeLabel.setVisible(false);
    }
    crewCapacity.setVisible(false);
    crewLabel.setVisible(false);
    return hasErrors;
  }

  /**
   * Update capacities.
   *
   * @param carrier the carrier
   * @param mass the mass
   * @param volume the volume
   * @param crew the crew
   * @return true, if successful
   */
  public boolean updateCapacities(I_Carrier carrier, double mass, double volume, int crew) {
    boolean hasErrors = false;
    if (GlobalParameters.isEnvironmentConstrained()) {
      environmentLabel.setVisible(true);
      environmentLabel.setText(carrier.getCargoEnvironment().getName());
    } else {
      environmentLabel.setVisible(false);
    }
    massCapacity.setVisible(true);
    massLabel.setVisible(true);
    if (mass > carrier.getMaxCargoMass()) {
      hasErrors = true;
      massCapacity.setForeground(new Color(153, 0, 0));
    } else {
      massCapacity.setForeground(new Color(0, 153, 0));
    }
    if (carrier.getMaxCargoMass() == 0) {
      massCapacity.setValue(100);
    } else {
      massCapacity.setValue((int) (100 * mass / carrier.getMaxCargoMass()));
    }
    massCapacity.setString(
        massFormat.format(mass) + " / " + massFormat.format(carrier.getMaxCargoMass()) + " kg");
    if (GlobalParameters.isVolumeConstrained()) {
      volumeCapacity.setVisible(true);
      volumeLabel.setVisible(true);
      if (volume > carrier.getMaxCargoVolume()) {
        hasErrors = hasErrors || GlobalParameters.isVolumeConstrained();
        volumeCapacity.setForeground(new Color(153, 0, 0));
      } else {
        volumeCapacity.setForeground(new Color(0, 153, 0));
      }
      if (carrier.getMaxCargoVolume() == 0) {
        volumeCapacity.setValue(100);
      } else {
        volumeCapacity.setValue((int) (100 * volume / carrier.getMaxCargoVolume()));
      }
      volumeCapacity.setString(volumeFormat.format(volume) + " / "
          + volumeFormat.format(carrier.getMaxCargoVolume()) + " m^3");
    } else {
      volumeCapacity.setVisible(false);
      volumeLabel.setVisible(false);
    }
    crewCapacity.setVisible(true);
    crewLabel.setVisible(true);
    if (crew > carrier.getMaxCrewSize()) {
      hasErrors = true;
      crewCapacity.setForeground(new Color(153, 0, 0));
    } else {
      crewCapacity.setForeground(new Color(0, 153, 0));
    }
    if (carrier.getMaxCrewSize() == 0) {
      crewCapacity.setValue(100);
    } else {
      crewCapacity.setValue((int) (100 * crew / carrier.getMaxCrewSize()));
    }
    crewCapacity.setString((int) crew + " / " + carrier.getMaxCrewSize() + " crew");
    return hasErrors;
  }

  /**
   * Update capacities.
   *
   * @param location the location
   * @param mass the mass
   * @param volume the volume
   * @param crew the crew
   * @return true, if successful
   */
  public boolean updateCapacities(Location location, double mass, double volume, int crew) {
    if (GlobalParameters.isEnvironmentConstrained()) {
      environmentLabel.setText(Environment.UNPRESSURIZED.getName());
    } else {
      environmentLabel.setVisible(false);
    }
    massCapacity.setVisible(true);
    massLabel.setVisible(true);
    massCapacity.setForeground(new Color(0, 153, 0));
    massCapacity.setValue(0);
    massCapacity.setString(massFormat.format(mass) + " kg");
    if (GlobalParameters.isVolumeConstrained()) {
      volumeCapacity.setVisible(true);
      volumeLabel.setVisible(true);
      volumeCapacity.setForeground(new Color(0, 153, 0));
      volumeCapacity.setValue(0);
      volumeCapacity.setString(volumeFormat.format(volume) + " m^3");
    } else {
      volumeCapacity.setVisible(false);
      volumeLabel.setVisible(false);
    }
    crewCapacity.setVisible(true);
    crewLabel.setVisible(true);
    crewCapacity.setForeground(new Color(0, 153, 0));
    crewCapacity.setValue(0);
    crewCapacity.setString((int) crew + " crew");
    return false;
  }
}
