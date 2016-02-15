/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
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
			new ImageIcon(ClassLoader.getSystemResource("icons/zoom_icon.gif")).getImage(), new Point(15,15), "Zoom");

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
