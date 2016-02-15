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

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * This class extends a combo box to provide icons for objects that implement
 * the Container interface.
 * 
 * @author Paul Grogan
 */
public class ContainerComboBox extends JComboBox {
	private static final long serialVersionUID = -4738778912721458792L;
	private ImageIcon transparentIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/transparent_icon.png"));
	
	/**
	 * Instantiates a new container combo box.
	 */
	public ContainerComboBox() {
		setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2255885956722142642L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof I_Element) {
					setIcon(((I_Element)value).getIcon());
				} else if(value instanceof Node) {
					setIcon(((Node)value).getNodeType().getIcon());
				} else if(value instanceof Edge) {
					setIcon(((Edge)value).getEdgeType().getIcon());
				} else if(value == null) {
					setIcon(transparentIcon);
				}
				return this;
			}
		});
	}
}