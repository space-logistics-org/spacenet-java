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
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * A panel that is used for editing surface nodes.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class SurfaceNodeEditorPanel extends AbstractNodeEditorPanel {
  private static final long serialVersionUID = -7968181121519566546L;
  private static final String N = "N", S = "S", E = "E", W = "W";

  private SurfaceNode node;

  private JTextField nameText;
  private JComboBox<Body> bodyCombo;
  private JComboBox<String> latitudeCombo;
  private JComboBox<String> longitudeCombo;
  private SpinnerNumberModel latitudeModel, longitudeModel;
  private JSpinner latitudeSpinner, longitudeSpinner;
  private JTextArea descriptionText;

  /**
   * Instantiates a new surface node panel editor.
   * 
   * @param node the node
   */
  public SurfaceNodeEditorPanel(NodeEditorDialog dialog, SurfaceNode node) {
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
    c.gridwidth = 2;
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
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    bodyCombo = new JComboBox<Body>();
    for (Body t : Body.values())
      bodyCombo.addItem(t);
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
    bodyCombo.setToolTipText("Body on which location resides");

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Latitude: "), c);

    c.gridx++;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    latitudeModel = new SpinnerNumberModel(0, 0, 90, 0.01);
    latitudeSpinner = new JSpinner(latitudeModel);
    latitudeSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(latitudeSpinner, "\u00B0"), c);
    latitudeSpinner.setToolTipText("Latitude of location [degrees]");
    c.gridx++;
    c.weightx = 1;
    latitudeCombo = new JComboBox<String>(new String[] {N, S});
    add(latitudeCombo, c);
    latitudeCombo.setToolTipText("N: North; S: South");

    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Longitude: "), c);

    c.gridx++;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    longitudeModel = new SpinnerNumberModel(0, 0, 180, 0.01);
    longitudeSpinner = new JSpinner(longitudeModel);
    longitudeSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(longitudeSpinner, "\u00B0"), c);
    longitudeSpinner.setToolTipText("Longitude of location [degrees]");
    c.gridx++;
    c.weightx = 1;
    longitudeCombo = new JComboBox<String>(new String[] {E, W});
    add(longitudeCombo, c);
    longitudeCombo.setToolTipText("E: East; W: West");

    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Description: "), c);

    c.gridx++;
    c.weighty = 1;
    c.gridwidth = 2;
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
      latitudeModel.setValue(Math.abs(node.getLatitude()));
      if (node.getLatitude() < 0)
        latitudeCombo.setSelectedItem(S);
      else
        latitudeCombo.setSelectedItem(N);
      longitudeModel.setValue(Math.abs(node.getLongitude()));
      if (node.getLatitude() < 0)
        longitudeCombo.setSelectedItem(W);
      else
        longitudeCombo.setSelectedItem(E);
      descriptionText.setText(node.getDescription());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.node.gui.AbstractNodePanelEditor#getNode()
   */
  public SurfaceNode getNode() {
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
    node.setLatitude(
        (latitudeCombo.getSelectedItem() == S ? -1 : 1) * latitudeModel.getNumber().doubleValue());
    node.setLongitude((longitudeCombo.getSelectedItem() == W ? -1 : 1)
        * longitudeModel.getNumber().doubleValue());
    node.setDescription(descriptionText.getText());
  }
}
