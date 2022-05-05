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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * The layer that represents elements in the network visualization.
 * 
 * @author Paul Grogan
 */
public class ElementLayer extends JPanel {
  private static final long serialVersionUID = -2339880151125938146L;
  private NetworkPanel networkPanel;
  private Map<Integer, Point> elementLocationMap;

  private Set<Integer> selectedElements;

  /**
   * Instantiates a new element layer.
   * 
   * @param networkPanel the network panel
   */
  public ElementLayer(NetworkPanel networkPanel) {
    this.networkPanel = networkPanel;
    elementLocationMap = new HashMap<Integer, Point>();
    selectedElements = new HashSet<Integer>();
    setOpaque(false);
    setLayout(null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (Location location : getNetwork().getLocations()) {
      Point center = new Point();
      if (location instanceof Node) {
        center = networkPanel.getNetworkLayer().getLocation((Node) location);
      } else if (location instanceof SurfaceEdge) {
        Point origin = networkPanel.getNetworkLayer().getLocation(((Edge) location).getOrigin());
        Point destination =
            networkPanel.getNetworkLayer().getLocation(((Edge) location).getDestination());
        center = new Point((origin.x + destination.x) / 2, (origin.y + destination.y) / 2);
      } else if (location instanceof Edge) {
        Point origin = networkPanel.getNetworkLayer().getLocation(((Edge) location).getOrigin());
        Point destination =
            networkPanel.getNetworkLayer().getLocation(((Edge) location).getDestination());
        int w = Math.abs(origin.x - destination.x);
        int h = Math.abs(origin.y - destination.y);
        double angle = 0;
        Point ovalCenter = new Point(destination.x, origin.y);
        if (origin.x <= destination.x && origin.y <= destination.y) {
          angle = 5 * Math.PI / 4;
        } else if (origin.x <= destination.x && origin.y > destination.y) {
          angle = 3 * Math.PI / 4;
        } else if (origin.x > destination.x && origin.y <= destination.y) {
          angle = 7 * Math.PI / 4;
        } else if (origin.x > destination.x && origin.y > destination.y) {
          angle = 1 * Math.PI / 4;
        }
        center = new Point(ovalCenter.x + (int) (Math.cos(angle) * w),
            ovalCenter.y - (int) (Math.sin(angle) * h));
      }

      int sideCount = (int) Math.ceil(Math.sqrt(location.getContents().size()));
      int col = 0;
      int row = 0;
      for (I_Element element : location.getContents()) {
        ImageIcon icon = element.getIcon();
        int x = (int) Math.round(
            center.x - sideCount * icon.getIconWidth() / 2d + (col + 0.5) * icon.getIconWidth());
        int y = (int) Math.round(
            center.y - sideCount * icon.getIconHeight() / 2d + (row + 0.5) * icon.getIconHeight());
        Point point = new Point(x, y);
        elementLocationMap.put(element.getUid(), point);
        g.drawImage(icon.getImage(), point.x - icon.getIconWidth() / 2,
            point.y - icon.getIconHeight() / 2, icon.getIconWidth(), icon.getIconHeight(), null);
        if (selectedElements.contains(element.getUid())) {
          g.setColor(Color.WHITE);
          g.setFont(getFont().deriveFont(Font.BOLD).deriveFont(10));
          g.drawString(element.getName(), point.x + icon.getIconWidth() / 2,
              point.y + icon.getIconHeight() / 2);
        }
        col++;
        if (col >= sideCount) {
          col = 0;
          row++;
        }
      }

    }
  }

  private Network getNetwork() {
    return networkPanel.getNetwork();
  }

  /**
   * Gets the location.
   * 
   * @param element the element
   * 
   * @return the location
   */
  public Point getLocation(I_Element element) {
    return new Point();
  }
}
