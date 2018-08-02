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
