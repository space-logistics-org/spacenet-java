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
package edu.mit.spacenet.gui.element;

import javax.swing.JPanel;

import edu.mit.spacenet.domain.element.I_Element;

/**
 * Abstract component that serves as an interface to the various element panels.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class AbstractElementPanel extends JPanel {
	private static final long serialVersionUID = 4132394191670439311L;

	private ElementDialog elementDialog;
	
	/**
	 * The constructor.
	 * 
	 * @param elementDialog the element dialog component
	 * @param element the element
	 */
	public AbstractElementPanel(ElementDialog elementDialog, I_Element element) {
		this.elementDialog = elementDialog;
	}
	
	/**
	 * Requests that the dialog save all element-specific data.
	 */
	public abstract void saveElement();
	
	/**
	 * Gets the element dialog component.
	 * 
	 * @return the element dialog
	 */
	public ElementDialog getElementDialog() {
		return elementDialog;
	}
	
	/**
	 * Checks if is vertically expandable.
	 *
	 * @return true, if is vertically expandable
	 */
	public abstract boolean isVerticallyExpandable();
	
	/**
	 * Gets the element.
	 * 
	 * @return the element
	 */
	public abstract I_Element getElement();
	
	/**
	 * Checks if is element valid.
	 * 
	 * @return true, if is element valid
	 */
	public abstract boolean isElementValid();
	
}
