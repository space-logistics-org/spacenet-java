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
package edu.mit.spacenet.gui.node;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * A panel used to edit orbital nodes.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class OrbitalNodeEditorPanel extends AbstractNodeEditorPanel {
  private static final long serialVersionUID = -7968181121519566546L;

  private OrbitalNode node;

  private JTextField nameText;
  private JComboBox<Body> bodyCombo;
  private SpinnerNumberModel inclinationModel, apoapsisModel, periapsisModel;
  private JSpinner inclinationSpinner, apoapsisSpinner, periapsisSpinner;
  private JTextArea descriptionText;

  /**
   * Instantiates a new orbital node panel editor.
   * 
   * @param node the node
   */
  public OrbitalNodeEditorPanel(NodeEditorDialog dialog, OrbitalNode node) {
    super(dialog);
    this.node = node;
    buildPanel();
    initialize();
  }

  /**
   * Builds the panel.
   */
  public void buildPanel() {
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Name: "), c);

    c.gridx++;
    c.gridwidth = 3;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    nameText = new JTextField();
    add(nameText, c);

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Body: "), c);

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 3;
    bodyCombo = new JComboBox<Body>();
    for (Body t : Body.values()) {
      bodyCombo.addItem(t);
    }
    bodyCombo.setRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = -1061418828847174455L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null)
          setIcon(((Body) value).getIcon());
        return this;
      }
    });
    add(bodyCombo, c);
    bodyCombo.setToolTipText("Body about which orbit circles");

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 1;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Apoapsis: "), c);

    c.gridx++;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    apoapsisModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 0.01);
    apoapsisSpinner = new JSpinner(apoapsisModel);
    apoapsisSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(apoapsisSpinner, "km"), c);
    apoapsisSpinner.setToolTipText("Farthest distance in the orbit [kilometers]");

    c.gridx++;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Periapsis: "), c);

    c.gridx++;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    periapsisModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 0.01);
    periapsisSpinner = new JSpinner(periapsisModel);
    periapsisSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(periapsisSpinner, "km"), c);
    periapsisSpinner.setToolTipText("Closest distance in the orbit [kilometers]");

    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Inclination: "), c);

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 3;
    inclinationModel = new SpinnerNumberModel(0, -90, 270, 0.01);
    inclinationSpinner = new JSpinner(inclinationModel);
    inclinationSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(inclinationSpinner, "\u00B0"), c);
    inclinationSpinner.setToolTipText("Inclination of orbit [degrees]");

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Description: "), c);

    c.gridx++;
    c.weighty = 1;
    c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    descriptionText = new JTextArea(5, 10);
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionText.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
    add(new JScrollPane(descriptionText), c);
  }

  /**
   * Initializes the components.
   */
  private void initialize() {
    if (node != null) {
      nameText.setText(node.getName());
      bodyCombo.setSelectedItem(node.getBody());
      inclinationModel.setValue(node.getInclination());
      apoapsisModel.setValue(node.getApoapsis());
      periapsisModel.setValue(node.getPeriapsis());
      descriptionText.setText(node.getDescription());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.node.gui.AbstractNodePanelEditor#getNode()
   */
  public OrbitalNode getNode() {
    return node;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.node.gui.AbstractNodePanelEditor#isNodeValid()
   */
  public boolean isNodeValid() {
    if (nameText.getText().length() == 0) {
      JOptionPane.showMessageDialog(getDialog(), "Please enter a node name.", "SpaceNet Warning",
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (apoapsisModel.getNumber().doubleValue() < periapsisModel.getNumber().doubleValue()) {
      JOptionPane.showMessageDialog(getDialog(),
          "Orbital apoapsis cannot be smaller than the periapsis.", "SpaceNet Warning",
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (bodyCombo.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(getDialog(), "Please select a celestial body.",
          "SpaceNet Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.gui.node.AbstractNodePanelEditor#saveNode()
   */
  public void saveNode() {
    node.setName(nameText.getText());
    node.setBody((Body) bodyCombo.getSelectedItem());
    node.setInclination(inclinationModel.getNumber().doubleValue());
    node.setApoapsis(apoapsisModel.getNumber().doubleValue());
    node.setPeriapsis(periapsisModel.getNumber().doubleValue());
    node.setDescription(descriptionText.getText());
  }
}
