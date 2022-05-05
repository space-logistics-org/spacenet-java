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
package edu.mit.spacenet.gui.network;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * A custom JPanel that displays the details (both generic and class-specific) for a node.
 * 
 * @author Paul Grogan
 */
public class NodeDetailsPanel extends JPanel {
  private static final long serialVersionUID = -3620939764968978036L;
  private static String NO_NODE = "No Node Panel";
  private static String SURFACE_NODE = "Surface Node Panel";
  private static String ORBITAL_NODE = "Orbital Node Panel";
  private static String LAGRANGE_NODE = "Lagrange Node Panel";

  private JLabel titleLabel, descriptionLabel, surfaceBodyLabel, latitudeLabel, longitudeLabel,
      orbitalBodyLabel, periapsisLabel, apoapsisLabel, inclinationLabel, majorBodyLabel,
      minorBodyLabel, lagrangeNumberLabel;
  private JPanel nodeDetailsPanel, surfaceNodeDetailsPanel, orbitalNodeDetailsPanel,
      lagrangeNodeDetailsPanel;

  /**
   * Instantiates a new node details panel.
   */
  public NodeDetailsPanel() {
    setBorder(BorderFactory.createTitledBorder("Node Details"));
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;
    titleLabel = new JLabel();
    add(titleLabel, c);
    c.gridy++;
    descriptionLabel = new JLabel();
    add(descriptionLabel, c);
    c.gridy++;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    nodeDetailsPanel = new JPanel();
    nodeDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    nodeDetailsPanel.setLayout(new CardLayout());
    nodeDetailsPanel.add(new JPanel(), NO_NODE);
    surfaceNodeDetailsPanel = new JPanel();
    surfaceNodeDetailsPanel.setLayout(new GridBagLayout());
    {
      GridBagConstraints g = new GridBagConstraints();
      g.gridx = 0;
      g.gridy = 0;
      g.fill = GridBagConstraints.NONE;
      g.anchor = GridBagConstraints.LINE_END;
      surfaceNodeDetailsPanel.add(new JLabel("Body: "), g);
      g.gridy++;
      surfaceNodeDetailsPanel.add(new JLabel("Latitude: "), g);
      g.gridy++;
      surfaceNodeDetailsPanel.add(new JLabel("Longitude: "), g);
      g.gridy = 0;
      g.gridx++;
      g.weightx = 1;
      g.anchor = GridBagConstraints.LINE_START;
      surfaceBodyLabel = new JLabel();
      surfaceNodeDetailsPanel.add(surfaceBodyLabel, g);
      g.gridy++;
      latitudeLabel = new JLabel();
      surfaceNodeDetailsPanel.add(new UnitsWrapper(latitudeLabel, "deg"), g);
      g.gridy++;
      longitudeLabel = new JLabel();
      surfaceNodeDetailsPanel.add(new UnitsWrapper(longitudeLabel, "deg"), g);
      g.gridy++;
      g.weighty = 1;
      surfaceNodeDetailsPanel.add(new JPanel(), g);
    }
    nodeDetailsPanel.add(surfaceNodeDetailsPanel, SURFACE_NODE);
    orbitalNodeDetailsPanel = new JPanel();
    orbitalNodeDetailsPanel.setLayout(new GridBagLayout());
    {
      GridBagConstraints g = new GridBagConstraints();
      g.gridx = 0;
      g.gridy = 0;
      g.fill = GridBagConstraints.NONE;
      g.anchor = GridBagConstraints.LINE_END;
      orbitalNodeDetailsPanel.add(new JLabel("Body: "), g);
      g.gridy++;
      orbitalNodeDetailsPanel.add(new JLabel("Periapsis: "), g);
      g.gridy++;
      orbitalNodeDetailsPanel.add(new JLabel("Apoapsis: "), g);
      g.gridy++;
      orbitalNodeDetailsPanel.add(new JLabel("Inclination: "), g);
      g.gridy = 0;
      g.gridx++;
      g.weightx = 1;
      g.anchor = GridBagConstraints.LINE_START;
      orbitalBodyLabel = new JLabel();
      orbitalNodeDetailsPanel.add(orbitalBodyLabel, g);
      g.gridy++;
      apoapsisLabel = new JLabel();
      orbitalNodeDetailsPanel.add(new UnitsWrapper(apoapsisLabel, "km"), g);
      g.gridy++;
      periapsisLabel = new JLabel();
      orbitalNodeDetailsPanel.add(new UnitsWrapper(periapsisLabel, "km"), g);
      g.gridy++;
      inclinationLabel = new JLabel();
      orbitalNodeDetailsPanel.add(new UnitsWrapper(inclinationLabel, "deg"), g);
      g.gridy++;
      g.weighty = 1;
      orbitalNodeDetailsPanel.add(new JPanel(), g);
    }
    nodeDetailsPanel.add(orbitalNodeDetailsPanel, ORBITAL_NODE);
    lagrangeNodeDetailsPanel = new JPanel();
    lagrangeNodeDetailsPanel.setLayout(new GridBagLayout());
    {
      GridBagConstraints g = new GridBagConstraints();
      g.gridx = 0;
      g.gridy = 0;
      g.fill = GridBagConstraints.NONE;
      g.anchor = GridBagConstraints.LINE_END;
      lagrangeNodeDetailsPanel.add(new JLabel("Major Body: "), g);
      g.gridy++;
      lagrangeNodeDetailsPanel.add(new JLabel("Minor Body: "), g);
      g.gridy++;
      lagrangeNodeDetailsPanel.add(new JLabel("Lagrange Number: "), g);
      g.gridy = 0;
      g.gridx++;
      g.weightx = 1;
      g.anchor = GridBagConstraints.LINE_START;
      majorBodyLabel = new JLabel();
      lagrangeNodeDetailsPanel.add(majorBodyLabel, g);
      g.gridy++;
      minorBodyLabel = new JLabel();
      lagrangeNodeDetailsPanel.add(minorBodyLabel, g);
      g.gridy++;
      lagrangeNumberLabel = new JLabel();
      lagrangeNodeDetailsPanel.add(lagrangeNumberLabel, g);
      g.gridy++;
      g.weighty = 1;
      lagrangeNodeDetailsPanel.add(new JPanel(), g);
    }
    nodeDetailsPanel.add(lagrangeNodeDetailsPanel, LAGRANGE_NODE);
    add(nodeDetailsPanel, c);
  }

  /**
   * Updates the display for a new node, clears if null is passed.
   * 
   * @param node the new node
   */
  public void setNode(Node node) {
    if (node == null) {
      titleLabel.setText("");
      descriptionLabel.setText("");
      ((CardLayout) nodeDetailsPanel.getLayout()).show(nodeDetailsPanel, NO_NODE);
    } else {
      titleLabel.setText(node.getName() + " (" + node.getNodeType() + " Node)");
      descriptionLabel.setText(node.getDescription());
      switch (node.getNodeType()) {
        case SURFACE:
          ((CardLayout) nodeDetailsPanel.getLayout()).show(nodeDetailsPanel, SURFACE_NODE);
          surfaceBodyLabel.setText(((SurfaceNode) node).getBody().toString());
          latitudeLabel.setText("" + ((SurfaceNode) node).getLatitude());
          longitudeLabel.setText("" + ((SurfaceNode) node).getLongitude());
          break;
        case ORBITAL:
          ((CardLayout) nodeDetailsPanel.getLayout()).show(nodeDetailsPanel, ORBITAL_NODE);
          orbitalBodyLabel.setText(((OrbitalNode) node).getBody().toString());
          apoapsisLabel.setText("" + ((OrbitalNode) node).getApoapsis());
          periapsisLabel.setText("" + ((OrbitalNode) node).getPeriapsis());
          inclinationLabel.setText("" + ((OrbitalNode) node).getInclination());
          break;
        case LAGRANGE:
          ((CardLayout) nodeDetailsPanel.getLayout()).show(nodeDetailsPanel, LAGRANGE_NODE);
          majorBodyLabel.setText(((LagrangeNode) node).getBody().toString());
          minorBodyLabel.setText(((LagrangeNode) node).getMinorBody().toString());
          lagrangeNumberLabel.setText("" + ((LagrangeNode) node).getNumber());
          break;
        default:
          ((CardLayout) nodeDetailsPanel.getLayout()).show(nodeDetailsPanel, NO_NODE);
      }
    }
  }
}
