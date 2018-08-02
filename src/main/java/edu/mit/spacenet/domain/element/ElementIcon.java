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
package edu.mit.spacenet.domain.element;

import javax.swing.ImageIcon;

/**
 * The element icon enumeration serves as a library of built-in icons for
 * elements.
 * 
 * @author Paul Grogan
 */
public enum ElementIcon {
	
	/** The brick. */
	BRICK("Brick", "icons/brick.png"),
	
	/** The blue person. */
	USER("Blue Person", "icons/user.png"),
	
	/** The pink person. */
	USER_FEMALE("Pink Person", "icons/user_female.png"),
	
	/** The the green person. */
	USER_GREEN("Green Person", "icons/user_green.png"),
	
	/** The orange person. */
	USER_ORANGE("Orange Person", "icons/user_orange.png"),
	
	/** The red person. */
	USER_RED("Red Person", "icons/user_red.png"),
	
	/** The package. */
	PACKAGE("Package", "icons/package.png"),
	
	/** The delivery truck. */
	LORRY("Delivery Truck", "icons/lorry.png"),
	
	/** The car. */
	CAR("Car", "icons/car.png"),
	
	/** The crew module. */
	CREW_MODULE("Crew Module", "icons/crew_module.png"), 
	
	/** The service module. */
	SERVICE_MODULE("Service Module", "icons/service_module.png"),
	
	/** The ascent stage. */
	ASCENT_STAGE("Ascent Stage", "icons/ascent_stage.png"),
	
	/** The descent stage. */
	DESCENT_STAGE("Descent Stage", "icons/descent_stage.png"),
	
	/** The lunar lander. */
	LUNAR_LANDER("Lunar Lander", "icons/lunar_lander.png"),
	
	/** The crew capsule. */
	CREW_CAPSULE("Crew Capsule", "icons/crew_capsule.png"),
	
	/** The launch abort system. */
	LAUNCH_ABORT("Launch Abort", "icons/launch_abort.png"),
	
	/** The Ares I first stage. */
	ARES_I_FS("Ares I First Stage", "icons/ares_i_fs.png"),
	
	/** The Ares I upper stage. */
	ARES_I_US("Ares I Upper Stage", "icons/ares_i_us.png"),
	
	/** The Ares V SRBs. */
	ARES_V_SRBS("Ares V SRBs", "icons/ares_v_srbs.png"),
	
	/** The Ares V Core. */
	ARES_V_CORE("Ares V Core", "icons/ares_v_core.png"),
	
	/** The Ares V EDS. */
	ARES_V_EDS("Ares V EDS", "icons/ares_v_eds.png"),
	
	/** The Ares V Interstage. */
	ARES_V_IS("Ares V Interstage", "icons/ares_v_is.png"),
	
	/** The Ares V Payload Fairing. */
	ARES_V_PLF("Ares V PLF", "icons/ares_v_plf.png"),
	
	/** The EVA Astronaut. */
	ASTRONAUT_WHITE("EVA Suit Astronaut", "icons/astronaut_white.png"),
	
	/** The Flight Suit Astronaut. */
	ASTRONAUT_ORANGE("Flight Suit Astronaut", "icons/astronaut_orange.png"),
	
	/** The Shuttle. */
	SHUTTLE("Shuttle", "icons/shuttle.png"),
	
	/** The Orbiter. */
	ORBITER("Orbiter", "icons/orbiter.png"),
	
	/** The Habitat. */
	HABITAT("Habitat", "icons/habitat.png"),
	
	/** The Power Supply. */
	POWER_SUPPLY("Power Supply", "icons/power_supply.png"),
	
	/** The SOYUZ. */
	SOYUZ("Soyuz", "icons/soyuz.png"),
	
	/** The PROGRESS. */
	PROGRESS("Progress", "icons/progress.png"),
	
	/** The ATV. */
	ATV("ATV", "icons/atv.png"),
	
	/** The HTV. */
	HTV("HTV", "icons/htv.png"),
	
	/** The CYGNUS. */
	CYGNUS("Cygnus", "icons/cygnus.png"),
	
	/** The DRAGON. */
	DRAGON("Dragon", "icons/dragon.png"),
	
	/** The ISS. */
	ISS("ISS", "icons/iss.png"),
	
	AMS("AMS", "icons/ams.png"),
	
	PMM("PMM", "icons/pmm.png"),
	
	ELC("ELC", "icons/elc.png"),
	
	MLM("MLM", "icons/mlm.png"),
	
	MARS_TRANSFER_HABITAT("Mars Transfer Habitat", "icons/mth.png"),
	
	LH2_DROP_TANK("LH2 Drop Tank", "icons/lh2_drop_tank.png"),
	
	AEROSHELL("Aeroshell", "icons/aeroshell.png"),
	
	INLINE_LH2_TANK("Inline LH2 Tank", "icons/lh2_tank.png"),
	
	NUCLEAR_THERMAL_ROCKET("Nuclear Thermal Rocket", "icons/ntr.png"),
	
	HABITAT_LANDER("Habitat Lander", "icons/hab_lander.png")
	;
	
	private String name;
	private ImageIcon icon;
	
	private ElementIcon(String name, String iconUrl) {
		this.name = name;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
	
	/**
	 * Gets the name of the icon.
	 * 
	 * @return the icon name
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	/**
	 * Gets the element icon instance based on a given icon.
	 * 
	 * @param icon the icon to match
	 * 
	 * @return the matching element icon, null if no match was found
	 */
	public static ElementIcon getInstance(ImageIcon icon) {
		for(ElementIcon elementIcon : values()) {
			if(elementIcon.getIcon().equals(icon)) return elementIcon;
		}
		return null;
	}
	
	/**
	 * Gets the single instance of ElementIcon.
	 * 
	 * @param string the string
	 * 
	 * @return single instance of ElementIcon
	 */
	public static ElementIcon getInstance(String string) {
		for(ElementIcon icon : values()) {
			if(icon.getName().toLowerCase().equals(string.toLowerCase())) return icon;
		}
		return null;
	}
}
