/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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