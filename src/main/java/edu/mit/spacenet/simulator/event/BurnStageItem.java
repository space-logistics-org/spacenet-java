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
package edu.mit.spacenet.simulator.event;

import javax.swing.ImageIcon;

import edu.mit.spacenet.domain.element.I_Element;

/**
 * This class represents an entry into a burn/stage list, either a burn of a
 * propulsive vehicle, or a staging of an expended element.
 * 
 * @author Paul Grogan
 */
public class BurnStageItem {
	
	/** The Constant BURN. */
	public static final String BURN = "Burn";
	
	/** The Constant STAGE. */
	public static final String STAGE = "Stage";
	
	/** The Constant BURN_ICON. */
	public static final ImageIcon BURN_ICON = new ImageIcon(BurnStageItem.class.getClassLoader().getResource("icons/bullet_red.png"));
	
	/** The Constant STAGE_ICON. */
	public static final ImageIcon STAGE_ICON = new ImageIcon(BurnStageItem.class.getClassLoader().getResource("icons/bullet_black.png"));
	private String burnStage;
	private I_Element element;
	
	/**
	 * The default constructor.
	 */
	public BurnStageItem() {}
	
	/**
	 * The constructor which sets the burn/stage type, and target element.
	 * 
	 * @param burnStage the burn stage type, accessible by static variables
	 * @param element the target element
	 */
	public BurnStageItem(String burnStage, I_Element element) {
		this.burnStage = burnStage;
		this.element = element;
	}
	
	/**
	 * Gets the burn/stage type.
	 * 
	 * @return the burn/stage type
	 */
	public String getBurnStage() {
		return burnStage;
	}
	
	/**
	 * Sets the burn/stage type.
	 * 
	 * @param burnStage the burn/stage type, accessible by static variables
	 */
	public void setBurnStage(String burnStage) {
		this.burnStage = burnStage;
	}
	
	/**
	 * Gets the target element.
	 * 
	 * @return the target element
	 */
	public I_Element getElement() {
		return element;
	}
	
	/**
	 * Sets the target element.
	 * 
	 * @param element the target element
	 */
	public void setElement(I_Element element) {
		this.element = element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(burnStage.equals(BURN)) return "[B] " + element;
		else if(burnStage.equals(STAGE)) return "[S] " + element;
		else return "";
	}
}
