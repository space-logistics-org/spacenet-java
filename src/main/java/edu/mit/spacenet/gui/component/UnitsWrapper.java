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
