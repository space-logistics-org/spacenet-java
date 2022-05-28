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
package edu.mit.spacenet.gui.edge;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.renderer.NodeListCellRenderer;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A panel used to edit flight edges.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class FlightEdgeEditorPanel extends AbstractEdgeEditorPanel {
  private static final long serialVersionUID = 6489837201474688317L;

  private FlightEdge edge;

  private JComboBox<Node> originCombo;
  private JComboBox<Node> destinationCombo;
  private JTextField nameText;
  private SpinnerNumberModel durationModel, maxCargoModel, maxCrewModel;
  private JSpinner durationSpinner, maxCargoSpinner, maxCrewSpinner;
  private JTextArea descriptionText;

  /**
   * Instantiates a new flight edge panel editor.
   * 
   * @param edge the edge
   */
  public FlightEdgeEditorPanel(EdgeEditorDialog dialog, FlightEdge edge) {
    super(dialog);
    this.edge = edge;
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
    c.weightx = 0;
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
    add(new JLabel("Origin: "), c);

    c.gridx++;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    originCombo = new JComboBox<Node>();
    originCombo.setRenderer(new NodeListCellRenderer());
    for (Node t : getDialog().getDialog().getDataSource().getNodeLibrary())
      originCombo.addItem(t);
    originCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          edge.setOrigin((Node) e.getItem());
        }
      }
    });
    add(originCombo, c);
    originCombo.setToolTipText("Origin node of the abstracted flight");

    c.gridx++;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Destination: "), c);

    c.gridx++;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    destinationCombo = new JComboBox<Node>();
    destinationCombo.setRenderer(new NodeListCellRenderer());
    for (Node t : getDialog().getDialog().getDataSource().getNodeLibrary())
      destinationCombo.addItem(t);
    destinationCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          edge.setDestination((Node) e.getItem());
        }
      }
    });
    add(destinationCombo, c);
    destinationCombo.setToolTipText("Destination node of the abstracted flight");

    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Time Duration:"), c);

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    durationModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE,
        GlobalParameters.getSingleton().getTimePrecision());
    durationSpinner = new JSpinner(durationModel);
    durationSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(durationSpinner, "days"), c);
    durationSpinner.setToolTipText(
        "Total duration of the abstracted flight between origin and destination [days]");

    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Maximum Crew: "), c);

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    maxCrewModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
    maxCrewSpinner = new JSpinner(maxCrewModel);
    maxCrewSpinner.setPreferredSize(new Dimension(75, 20));
    add(maxCrewSpinner, c);
    maxCrewSpinner.setToolTipText("Maximum number of human crew members allowed");

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Maximum Mass: "), c);

    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    maxCargoModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE,
        GlobalParameters.getSingleton().getMassPrecision());
    maxCargoSpinner = new JSpinner(maxCargoModel);
    maxCargoSpinner.setPreferredSize(new Dimension(75, 20));
    add(new UnitsWrapper(maxCargoSpinner, "kg"), c);
    maxCargoSpinner.setToolTipText("Maximum cargo mass allowed [kilograms]");

    c.gridy++;
    c.gridx = 0;
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
    if (edge != null) {
      originCombo.setSelectedItem(edge.getOrigin());
      destinationCombo.setSelectedItem(edge.getDestination());
      nameText.setText(edge.getName());
      durationModel.setValue(edge.getDuration());
      maxCrewModel.setValue(edge.getMaxCrewSize());
      maxCargoModel.setValue(edge.getMaxCargoMass());
      descriptionText.setText(edge.getDescription());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.gui.edge.AbstractEdgePanelEditor#getEdge()
   */
  public FlightEdge getEdge() {
    return edge;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.gui.edge.AbstractEdgePanelEditor#isEdgeValid()
   */
  public boolean isEdgeValid() {
    if (nameText.getText().length() == 0) {
      JOptionPane.showMessageDialog(getDialog(), "Please enter an edge name.", "SpaceNet Warning",
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (originCombo.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(getDialog(), "Please select an origin node.",
          "SpaceNet Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (destinationCombo.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(getDialog(), "Please select a destination node.",
          "SpaceNet Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    if (originCombo.getSelectedItem() == destinationCombo.getSelectedItem()) {
      JOptionPane.showMessageDialog(getDialog(), "Origin node cannot also be the destination node.",
          "SpaceNet Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.editor.gui.edge.AbstractEdgePanelEditor#saveEdge()
   */
  public void saveEdge() {
    edge.setName(nameText.getText());
    edge.setOrigin((Node) originCombo.getSelectedItem());
    edge.setDestination((Node) destinationCombo.getSelectedItem());
    edge.setDuration(durationModel.getNumber().doubleValue());
    edge.setMaxCargoMass(maxCargoModel.getNumber().doubleValue());
    edge.setMaxCrewSize(maxCrewModel.getNumber().intValue());
    edge.setDescription(descriptionText.getText());
  }
}
