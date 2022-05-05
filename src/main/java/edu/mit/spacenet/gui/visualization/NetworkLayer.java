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
package edu.mit.spacenet.gui.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;

/**
 * The layer that represents nodes and edges in the network visualization.
 * 
 * @author Nii Armar, Paul Grogan
 * 
 */
public class NetworkLayer extends JPanel {
  private static final long serialVersionUID = 4415070504799243431L;
  private NetworkPanel networkPanel;

  private double altitudeScale = 0.01;
  private double altitudeBasis = 150;

  private int edgeWidth = 1;
  private int nodeSize = 16;

  private Map<Integer, Point> nodeLocationMap;
  private Set<Integer> selectedEdges, selectedNodes;

  /**
   * Instantiates a new network layer.
   * 
   * @param networkPanel the network panel
   */
  public NetworkLayer(NetworkPanel networkPanel) {
    this.networkPanel = networkPanel;
    nodeLocationMap = new HashMap<Integer, Point>();
    selectedEdges = new HashSet<Integer>();
    selectedNodes = new HashSet<Integer>();
    setOpaque(false);
    setLayout(null);
    /*
     * for(Node node : getScenario().getNetwork().getNodes()) { NodeLabel n = new NodeLabel(node,
     * this); add(n); }
     */
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    nodeLocationMap.clear();
    for (Node node : getNetwork().getNodes()) {
      nodeLocationMap.put(node.getTid(), getLocation(node));
    }
    for (Edge edge : getNetwork().getEdges()) {
      Point origin = nodeLocationMap.get(edge.getOrigin().getTid());
      Point destination = nodeLocationMap.get(edge.getDestination().getTid());
      g.setColor(edge.getEdgeType().getColor());
      Graphics2D g2 = (Graphics2D) g;
      if (selectedEdges.contains(edge.getTid())) {
        g2.setStroke(new BasicStroke(3 * edgeWidth));
      } else {
        g2.setStroke(new BasicStroke(edgeWidth));
      }
      if (edge instanceof SurfaceEdge || origin.x == destination.x || origin.y == destination.y) {
        g.drawLine(origin.x, origin.y, destination.x, destination.y);
      } else {
        int w = Math.abs(origin.x - destination.x);
        int h = Math.abs(origin.y - destination.y);
        if (origin.x <= destination.x && origin.y <= destination.y) {
          g.drawArc(origin.x, origin.y - h, 2 * w, 2 * h, 180, 90);
        } else if (origin.x <= destination.x && origin.y > destination.y) {
          g.drawArc(origin.x, origin.y - h, 2 * w, 2 * h, 90, 90);
        } else if (origin.x > destination.x && origin.y <= destination.y) {
          g.drawArc(origin.x - 2 * w, origin.y - h, 2 * w, 2 * h, 270, 90);
        } else if (origin.x > destination.x && origin.y > destination.y) {
          g.drawArc(origin.x - 2 * w, origin.y - h, 2 * w, 2 * h, 0, 90);
        }
      }
      if (selectedEdges.contains(edge.getTid())) {
        if (networkPanel.isBlackBackground())
          g.setColor(Color.WHITE);
        else
          g.setColor(Color.BLACK);
        g.setFont(getFont().deriveFont(Font.BOLD).deriveFont(10));
        g.drawString(edge.getName(), (origin.x + destination.x) / 2,
            (origin.y + destination.y) / 2);
      }
    }
    for (Node node : getNetwork().getNodes()) {
      ImageIcon icon = node.getNodeType().getIcon();
      Point point = nodeLocationMap.get(node.getTid());
      g.drawImage(icon.getImage(), point.x - nodeSize / 2, point.y - nodeSize / 2, nodeSize,
          nodeSize, null);
      if (selectedNodes.contains(node.getTid())) {
        if (networkPanel.isBlackBackground())
          g.setColor(Color.WHITE);
        else
          g.setColor(Color.BLACK);
        g.setFont(getFont().deriveFont(Font.BOLD).deriveFont(10));
        g.drawString(node.getName(), point.x + icon.getIconWidth() / 2,
            point.y + icon.getIconHeight() / 2);
      }
    }
  }

  /**
   * Gets the location.
   * 
   * @param node the node
   * 
   * @return the location
   */
  public Point getLocation(Node node) {
    if (node instanceof SurfaceNode)
      return getLocation((SurfaceNode) node);
    else if (node instanceof OrbitalNode)
      return getLocation((OrbitalNode) node);
    else if (node instanceof LagrangeNode)
      return getLocation((LagrangeNode) node);
    else
      return new Point();
  }

  private Point getLocation(SurfaceNode node) {
    /*
     * Note: this function converts the latitude and longitude coordinates to a Lambert-Azimuthal
     * projected area, formula was taken from
     * http://mathworld.wolfram.com/LambertAzimuthalEqual-AreaProjection.html
     */
    double d_pi = Math.PI / 180D;

    Point bodyPoint = networkPanel.getBodyLayer().getBodyLocation(node.getBody());
    double bodyRadius = networkPanel.getBodyLayer().getBodySize(node.getBody()) / 2;

    double d_lat = node.getLatitude() * d_pi;
    double d_lon = node.getLongitude() == 180 ? 179 : node.getLongitude() * d_pi;

    double d_phi = 0 * d_pi;
    double d_lam = 0 * d_pi;

    double kk0 = Math.sqrt(2 / (1 + Math.sin(d_phi) * Math.sin(d_lat)
        + Math.cos(d_phi) * Math.cos(d_lat) * Math.cos(d_lon - d_lam)));

    double xx0 = kk0 * Math.cos(d_lat) * Math.sin(d_lon - d_lam);
    int d_xx1 = (int) Math.round(bodyPoint.x + 0.5 * bodyRadius * xx0);

    double yy0 = kk0 * (Math.cos(d_phi) * Math.sin(d_lat)
        - Math.sin(d_phi) * Math.cos(d_lat) * Math.cos(d_lon - d_lam));
    int d_yy1 = (int) Math.round(bodyPoint.y + 0.5 * bodyRadius * (-yy0));

    return new Point(d_xx1, d_yy1);
  }

  private Point getLocation(OrbitalNode node) {
    Point bodyPoint = networkPanel.getBodyLayer().getBodyLocation(node.getBody());
    double bodyRadius = networkPanel.getBodyLayer().getBodySize(node.getBody()) / 2d;

    double altitude = bodyRadius
        + bodyRadius * .25 * getAltitude(0.5 * (node.getApoapsis() + node.getPeriapsis()));

    int d_xx1;
    if (node.getBody() == Body.EARTH || node.getBody() == Body.SUN) {
      d_xx1 = (int) Math
          .round(bodyPoint.x + altitude * Math.cos(node.getInclination() / 180d * Math.PI));
    } else {
      d_xx1 = (int) Math
          .round(bodyPoint.x - altitude * Math.cos(node.getInclination() / 180d * Math.PI));
    }
    int d_yy1 =
        (int) Math.round(bodyPoint.y + altitude * Math.sin(node.getInclination() / 180d * Math.PI));

    return new Point(d_xx1, d_yy1);
  }

  private Point getLocation(LagrangeNode node) {
    Point minorBodyPoint = networkPanel.getBodyLayer().getBodyLocation(node.getMinorBody());
    double minorBodyRadius = networkPanel.getBodyLayer().getBodySize(node.getMinorBody()) / 2d;
    Point majorBodyPoint = networkPanel.getBodyLayer().getBodyLocation(node.getBody());
    double majorBodyRadius = networkPanel.getBodyLayer().getBodySize(node.getBody()) / 2d;

    double R = Math
        .sqrt(Math.pow(majorBodyPoint.x + majorBodyRadius - minorBodyPoint.x + minorBodyRadius, 2)
            + Math.pow(majorBodyPoint.y + majorBodyRadius - minorBodyPoint.y + minorBodyRadius, 2));

    switch (node.getNumber()) {
      case 1:
        return new Point((int) Math.round(minorBodyPoint.x - 1.5 * minorBodyRadius),
            (int) Math.round(minorBodyPoint.y));
      case 2:
        return new Point((int) Math.round(minorBodyPoint.x + 1.5 * minorBodyRadius),
            (int) Math.round(minorBodyPoint.y));
      case 3:
        return new Point((int) Math.round(majorBodyPoint.x - R),
            (int) Math.round(majorBodyPoint.y));
      case 4:
        return new Point((int) Math.round(majorBodyPoint.x + R * Math.cos(60 / 180d * Math.PI)),
            (int) Math.round(majorBodyPoint.y + R * Math.sin(60 / 180d * (Math.PI / 2d))));
      case 5:
        return new Point((int) Math.round(majorBodyPoint.x + R * Math.cos(60 / 180d * Math.PI)),
            (int) Math.round(majorBodyPoint.y - R * Math.sin(60 / 180d * Math.PI)));
    }
    return new Point();
  }

  private Network getNetwork() {
    return networkPanel.getNetwork();
  }

  private double getAltitude(double altitude) {
    return 1 / (Math.PI) * Math.atan(altitudeScale * (altitude - altitudeBasis)) + 0.5;
  }

  /**
   * Gets the altitude scale.
   * 
   * @return the altitude scale
   */
  public double getAltitudeScale() {
    return altitudeScale;
  }

  /**
   * Sets the altitude scale.
   * 
   * @param altitudeScale the new altitude scale
   */
  public void setAltitudeScale(double altitudeScale) {
    this.altitudeScale = altitudeScale;
  }

  /**
   * Gets the altitude basis.
   * 
   * @return the altitude basis
   */
  public double getAltitudeBasis() {
    return altitudeBasis;
  }

  /**
   * Sets the altitude basis.
   * 
   * @param altitudeBasis the new altitude basis
   */
  public void setAltitudeBasis(double altitudeBasis) {
    this.altitudeBasis = altitudeBasis;
  }

  /*
   * private class NodeLabel extends JLabel { private static final long serialVersionUID =
   * -5320521626370983008L; private Node node; private NetworkLayer networkLayer;
   * 
   * public NodeLabel(Node node, NetworkLayer networkLayer) {
   * super(NodeType.getInstance(node.getClass()).getIcon(), JLabel.LEADING); this.node = node;
   * this.networkLayer = networkLayer; setOpaque(false); setIconTextGap(0); setSize(new
   * Dimension(getIcon().getIconWidth(),getIcon().getIconHeight())); setToolTipText(node.getName() +
   * " (" + NodeType.getInstance(node.getClass()).getName() + ")" +
   * (node.getDescription()==null?"":": "+node.getDescription())); } public void
   * paintComponent(Graphics g) { setLocation(networkLayer.getLocation(node).x-getWidth()/2,
   * networkLayer.getLocation(node).y-getHeight()/2); super.paintComponent(g); } public Node
   * getNode() { return node; } }
   */
  /**
   * Gets the selected edges.
   * 
   * @return the selected edges
   */
  public Set<Integer> getSelectedEdges() {
    return selectedEdges;
  }

  /**
   * Gets the selected nodes.
   * 
   * @return the selected nodes
   */
  public Set<Integer> getSelectedNodes() {
    return selectedNodes;
  }

  /**
   * Gets the edge width.
   * 
   * @return the edge width
   */
  public int getEdgeWidth() {
    return edgeWidth;
  }

  /**
   * Sets the edge width.
   * 
   * @param edgeWidth the new edge width
   */
  public void setEdgeWidth(int edgeWidth) {
    this.edgeWidth = edgeWidth;
  }

  /**
   * Gets the node size.
   * 
   * @return the node size
   */
  public int getNodeSize() {
    return nodeSize;
  }

  /**
   * Sets the node size.
   * 
   * @param nodeSize the new node size
   */
  public void setNodeSize(int nodeSize) {
    this.nodeSize = nodeSize;
  }
}
