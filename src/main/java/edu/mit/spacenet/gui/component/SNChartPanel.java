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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import org.jfree.chart.ChartPanel;

/**
 * A subclass of a chart panel that sets some SpaceNet defaults.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SNChartPanel extends ChartPanel {
  private static final long serialVersionUID = 117468040756000184L;
  private static final Cursor zoomCursor = Toolkit.getDefaultToolkit().createCustomCursor(
      new ImageIcon(ClassLoader.getSystemResource("icons/zoom_icon.gif")).getImage(),
      new Point(15, 15), "Zoom");

  /**
   * Instantiates a new SpaceNet chart panel.
   */
  public SNChartPanel() {
    super(null);
    addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        setCursor(zoomCursor);
      }

      public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
      }
    });
  }
}
