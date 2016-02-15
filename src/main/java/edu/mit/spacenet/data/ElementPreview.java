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
package edu.mit.spacenet.data;

import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;

/**
 * A class that displays element properties (name, and type) before the entire
 * element is loaded from the data source.
 * 
 * @author Paul Grogan
 */
public class ElementPreview implements Comparable<ElementPreview> {
	
	/** The element's type identifier (TID). */
	public final int ID;
	
	/** The element's name. */
	public final String NAME;
	
	/** The element's type. */
	public final ElementType TYPE;
	
	private ElementIcon icon;
	
	/** The element's class. */
	@Deprecated public final Class<?> CLAZZ;
	
	/**
	 * The default constructor sets the preview parameters.
	 * 
	 * @param id the element id
	 * @param name the element name
	 * @param type the element type
	 */
	public ElementPreview(int id, String name, ElementType type, ElementIcon icon) {
		this.ID = id;
		this.NAME = name;
		this.TYPE = type;
		this.icon = icon;
		this.CLAZZ = null;
	}
	
	/**
	 * Gets the icon type.
	 *
	 * @return the icon
	 */
	public ElementIcon getIconType() {
		if(icon==null) this.icon = TYPE.getIconType();
		return this.icon;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return NAME + " (" + TYPE.getName() + ")";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ElementPreview o) {
		return new Integer(ID).compareTo(new Integer(o.ID));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof ElementPreview) {
			return ID == ((ElementPreview)object).ID;
		}
		else return false;
	}
}