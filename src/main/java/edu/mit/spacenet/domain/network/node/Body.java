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
package edu.mit.spacenet.domain.network.node;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Enumeration of the celestial bodies.
 * 
 * @author Paul Grogan
 */
public enum Body implements Serializable { 
	
	/** The sun */
	SUN("Sun", Color.YELLOW, "/icons/sun.png", null, 332946., 109., 0, "icons/sun_icon.png"),
	
	/** Venus */
	VENUS("Venus", Color.WHITE, null, SUN, 0.815, 0.950, 0.723, "icons/venus_icon.png"),
	
	/** Earth */
	EARTH("Earth", Color.BLUE, "/icons/earth.png", SUN, 1., 1., 1., "icons/earth_icon.png"), 
	
	/** The moon */
	MOON("Moon", Color.LIGHT_GRAY, "/icons/moon.png", EARTH, 0.0123, 0.273, 0.00257, "icons/moon_icon.png"), 
	
	/** Mars */
	MARS("Mars", Color.RED, "/icons/mars.png", SUN, 0.11, 0.532, 1.52, "icons/mars_icon.png"), 
	
	/** Phobos */
	PHOBOS("Phobos", Color.DARK_GRAY, null, MARS, 1.8*Math.pow(10,-9), 11.1/6378., 9377.2/(150*Math.pow(10,6)), "icons/phobos_icon.png"),
	
	/** Deimos */
	DEIMOS("Deimos", Color.DARK_GRAY, null, MARS, 0.25*Math.pow(10,-9), 6.2/6378., 23460/(150*Math.pow(10,6)), "icons/deimos_icon.png"),
	
	/** Jupiter */
	JUPITER("Jupiter", Color.RED, null, SUN, 317.8, 11.209, 5.20, "icons/jupiter_icon.png");

	/** 1 AU (astronomical unit) in kilometers. */
	public static final long AU = Math.round(150*Math.pow(10,6));
	
	/** Earth's radius (kilometers). */
	public static final long EARTH_RADIUS = 6378;
	
	/** Earth's mass (kilograms). */
	public static final long EARTH_MASS = Math.round(5.9736*Math.pow(10,27));
	
	/**
	 * Gets the nicely-formatted name of the body.
	 * 
	 * @return the body's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the body's color representation.
	 * 
	 * @return the body's color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Gets the body's Lambert-Azimuthal projection.
	 * 
	 * @return the body's image
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Gets the parent body about which this body is orbiting.
	 * 
	 * @return the parent
	 */
	public Body getParent() {
		return parent;
	}
	
	/**
	 * Gets the mass.
	 * 
	 * @return the body's mass (units of Earth mass)
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Gets the mean planetary radius.
	 * 
	 * @return the body's mean radius (units of Earth radius)
	 */
	public double getPlanetaryRadius() {
		return planetaryRadius;
	}
	
	/**
	 * Gets the mean orbital radius.
	 * 
	 * @return the body's mean orbital radius (units of AU)
	 */
	public double getOrbitalRadius() {
		return orbitalRadius;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Method to get a particular instance of a body based on a
	 * case-insensitive string.
	 * 
	 * @param name 	case-insensitive string of name
	 * 
	 * @return the body, returns null if unknown name
	 */
	public static Body getInstance(String name) {
		for(Body body : Body.values()) {
			if(body.getName().toLowerCase().equals(name.toLowerCase())) return body;
		}
		return null;
    }
	
	/**
	 * Gets the icon of the body type.
	 * 
	 * @return the body type icon
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	
	// PRIVATE
	
	private String name;
	private Color color;
	private BufferedImage image;
	private Body parent;
	private double mass;
	private double planetaryRadius;
	private double orbitalRadius;
	private ImageIcon icon;
	
	/**
	 * The default constructor.
	 *
	 * @param name the name to display
	 * @param color the color
	 * @param imageUrl the image url
	 * @param parent the parent
	 * @param mass the mass (earth mass)
	 * @param planetaryRadius the planetary radius (earth radii)
	 * @param orbitalRadius the orbital radius (astronomical units)
	 * @param iconUrl the icon url
	 */
	private Body(String name, Color color, String imageUrl, Body parent, double mass, double planetaryRadius, double orbitalRadius, String iconUrl) { 
		this.name = name;
		this.color = color;
		if(imageUrl!=null) {
			try {
				image = ImageIO.read(getClass().getResource(imageUrl));
			} catch (IOException e) {
				System.out.println("error opening image");
			}
		}
		this.parent = parent;
		this.mass = mass;
		this.planetaryRadius = planetaryRadius;
		this.orbitalRadius = orbitalRadius;
		this.icon = new ImageIcon(getClass().getClassLoader().getResource(iconUrl));
	}
}