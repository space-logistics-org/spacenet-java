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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A wrapper for components that adds units labels.
 * 
 * @author Paul Grogan
 */
public class UnitsWrapper extends JPanel {
  private static final long serialVersionUID = -7924295829431793990L;
  private JLabel label;

  /**
   * The constructor.
   * 
   * @param input the input component
   * @param units the units label
   */
  public UnitsWrapper(Component input, String units) {
    super();
    setOpaque(false);
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.weightx = 1;
    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(input, c);
    label = new JLabel(units);
    label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    c.weightx = 0;
    c.gridx = 1;
    add(label, c);
  }

  /**
   * Sets the units label.
   * 
   * @param units the units label
   */
  public void setUnits(String units) {
    label.setText(units);
  }
}
