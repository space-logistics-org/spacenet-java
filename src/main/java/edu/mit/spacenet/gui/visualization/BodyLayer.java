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
package edu.mit.spacenet.gui.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * The layer that represents celestrial bodies in the network visualization.
 * 
 * @author Nii Armar, Paul Grogan
 */
public class BodyLayer extends JPanel {
	private static final long serialVersionUID = -7130437870888000639L;

	private NetworkPanel networkPanel;

	private double horizontalScale = 1;
	private double verticalScale = 1;
	
	private double sizeDisparity = 10.0;
	private double sizeScale = 2.0;
	private double sizeBasis = 1.0;
	private double locationDisparity = 30.0;
	private double locationScale = 1.5;
	private double locationBasis = 1.0;
	private boolean drawOrbitalLimits = false;
	private boolean simpleMode = true;
	private boolean displaySun = false;
	
	private Map<Body, Point> bodyLocationMap;
	private Map<Body, Integer> bodySizeMap;
	
	/**
	 * Instantiates a new body layer.
	 * 
	 * @param networkPanel the network panel
	 */
	public BodyLayer(NetworkPanel networkPanel) {
		this.networkPanel = networkPanel;
		setOpaque(false);
		setLayout(null);
		bodyLocationMap = new HashMap<Body, Point>();
		bodySizeMap = new HashMap<Body, Integer>();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int planetCount = 0;
		
		Set<Body> bodies = new HashSet<Body>();
		for(Node node : getNetwork().getNodes()) {
			Body body = null;
			if(node instanceof LagrangeNode && ((LagrangeNode)node).getNumber() <= 2) {
				body = ((LagrangeNode)node).getMinorBody();
			} else {
				body = node.getBody();
			}
			if(body.getParent()==Body.SUN && !bodies.contains(body)) planetCount++;
			bodies.add(body);
		}
		
		if(bodies.size()==0) {
			// display nothing
		} else if((bodies.size()==1 || planetCount==1) && !bodies.contains(Body.SUN) && simpleMode) {
			// lay out moons to the right of planet
			double width = 0;
			double height = 0;
			
			for(Body planet : Body.values()) {
				double planetSize = 0;
				if((planet.getParent()==Body.SUN||bodies.size()==1) && bodies.contains(planet)) {
					planetSize = getRawSize(planet.getPlanetaryRadius());
					height = planetSize*1.5;
					width = planetSize*1.5;
					
					for(Body moon : Body.values()) {
						double moonSize = 0;
						double moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getRawSize(moon.getPlanetaryRadius());
							moonLocation = getRawLocation(moon.getOrbitalRadius());
							
							height = Math.max(height, moonSize*1.5);
							width = Math.max(width, planetSize*3/4d + moonLocation + moonSize*3/4d);
						}
					}
				}
			}
			horizontalScale = (getBounds().width)/width;
			verticalScale = (getBounds().height)/height;
			
			bodyLocationMap.clear();
			bodySizeMap.clear();
			for(Body planet : Body.values()) {
				int planetSize = 0;
				if((planet.getParent()==Body.SUN||bodies.size()==1) && bodies.contains(planet)) {
					planetSize = getScaledNumber(getRawSize(planet.getPlanetaryRadius()));
					int h_space = getBounds().width - (int)Math.round(width*Math.min(horizontalScale, verticalScale));
					int v_space = getBounds().height - (int)Math.round(height*Math.min(horizontalScale, verticalScale));
					bodyLocationMap.put(planet, new Point(h_space/2 + planetSize*3/4, v_space/2 + planetSize*3/4));
					bodySizeMap.put(planet, planetSize);
					
					drawBody(planet, g);
					
					for(Body moon : Body.values()) {
						int moonSize = 0;
						int moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getScaledNumber(getRawSize(moon.getPlanetaryRadius()));
							moonLocation = getScaledNumber(getRawLocation(moon.getOrbitalRadius()));
						
							bodyLocationMap.put(moon, 
									new Point(bodyLocationMap.get(planet).x+moonLocation, 
									bodyLocationMap.get(planet).y));
							bodySizeMap.put(moon, moonSize);
							
							drawBody(moon, g);
						}
					}
				}
			}
		} else if(displaySun||bodies.contains(Body.SUN)) {
			// lay out planets to the right, moons below each planet
			double width = 0;
			double height = 0;
			
			double sunSize = getRawSize(Body.SUN.getPlanetaryRadius());
			width = sunSize/2d;
			height = sunSize;
			
			for(Body planet : Body.values()) {
				double planetSize = 0;
				double planetLocation = 0;
				
				if(planet.getParent()==Body.SUN && bodies.contains(planet)) {
					planetSize = getRawSize(planet.getPlanetaryRadius());
					planetLocation = getRawLocation(planet.getOrbitalRadius());
					
					height = Math.max(height, planetSize*3/2d);
					width = Math.max(width, planetLocation + planetSize*3/4d);
					
					for(Body moon : Body.values()) {
						double moonSize = 0;
						double moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getRawSize(moon.getPlanetaryRadius());
							moonLocation = getRawLocation(moon.getOrbitalRadius());
							
							height = Math.max(height, sunSize/2d + moonLocation + moonSize*3/4d);
						}
					}
				}
			}
			
			horizontalScale = (getBounds().width)/width;
			verticalScale = (getBounds().height)/height;
			
			bodyLocationMap.clear();
			bodySizeMap.clear();

			int v_space = getBounds().height - (int)Math.round(height*Math.min(horizontalScale, verticalScale));

			bodyLocationMap.put(Body.SUN, new Point(0, v_space/2+getScaledNumber(sunSize)/2));
			bodySizeMap.put(Body.SUN, getScaledNumber(sunSize));
			
			if(Body.SUN.getImage()==null) {
				g.setColor(Body.SUN.getColor());
				g.fillOval(-bodySizeMap.get(Body.SUN)/2, bodyLocationMap.get(Body.SUN).y-bodySizeMap.get(Body.SUN)/2,
						bodySizeMap.get(Body.SUN), bodySizeMap.get(Body.SUN));
			} else {
				g.drawImage(Body.SUN.getImage(), bodyLocationMap.get(Body.SUN).x, bodyLocationMap.get(Body.SUN).y-bodySizeMap.get(Body.SUN)/2,
						bodySizeMap.get(Body.SUN)/2, bodySizeMap.get(Body.SUN), null);
			}
				
			for(Body planet : Body.values()) {
				int planetSize = 0;
				int planetLocation = 0;
				if(planet.getParent()==Body.SUN && bodies.contains(planet)) {
					planetSize = getScaledNumber(getRawSize(planet.getPlanetaryRadius()));
					planetLocation = getScaledNumber(getRawLocation(planet.getOrbitalRadius()));
					bodyLocationMap.put(planet, new Point(planetLocation, bodyLocationMap.get(Body.SUN).y));
					bodySizeMap.put(planet, planetSize);
					
					drawBody(planet, g);
					
					for(Body moon : Body.values()) {
						int moonSize = 0;
						int moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getScaledNumber(getRawSize(moon.getPlanetaryRadius()));
							moonLocation = getScaledNumber(getRawLocation(moon.getOrbitalRadius()));
						
							bodyLocationMap.put(moon, new Point(bodyLocationMap.get(planet).x, 
									bodyLocationMap.get(planet).y + moonLocation));
							bodySizeMap.put(moon, moonSize);
							
							drawBody(moon, g);
						}
					}
				}
			}
		} else {
			// lay out planets to the right, moons below each planet
			double width = 0;
			double height = 0;

			Body firstPlanet = null;
			Body largestPlanet = null;
			{
				double firstPlanetLocation = Double.MAX_VALUE;
				double largestPlanetSize = 0;
				for(Body planet : Body.values()) {
					if(planet.getParent()==Body.SUN && bodies.contains(planet)) {
						if(getRawLocation(planet.getOrbitalRadius()) < firstPlanetLocation) {
							firstPlanetLocation = getRawLocation(planet.getOrbitalRadius());
							firstPlanet = planet;
						}
						if(getRawSize(planet.getPlanetaryRadius()) > largestPlanetSize) {
							largestPlanetSize = getRawSize(planet.getPlanetaryRadius());
							largestPlanet = planet;
						}
					}
				}
			}
			
			double rawFirstPlanetLocation = getRawSize(firstPlanet.getPlanetaryRadius())*3/4d;
			double rawLargestPlanetSize = getRawSize(largestPlanet.getPlanetaryRadius());
			
			for(Body planet : Body.values()) {
				double planetSize = 0;
				double planetLocation = 0;

				if(planet.getParent()==Body.SUN && bodies.contains(planet)) {
					planetSize = getRawSize(planet.getPlanetaryRadius());
					planetLocation = planet==firstPlanet?rawFirstPlanetLocation:
						rawFirstPlanetLocation + getRawLocation(planet.getOrbitalRadius()-firstPlanet.getOrbitalRadius());
					
					height = Math.max(height, planetSize*3/2d);
					width = Math.max(width, planetLocation + planetSize*3/4d);
					
					for(Body moon : Body.values()) {
						double moonSize = 0;
						double moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getRawSize(moon.getPlanetaryRadius());
							moonLocation = getRawLocation(moon.getOrbitalRadius());
							
							height = Math.max(height, rawLargestPlanetSize*3/4d + moonLocation + moonSize*3/4d);
						}
					}
				}
			}
			
			horizontalScale = (getBounds().width)/width;
			verticalScale = (getBounds().height)/height;
			
			bodyLocationMap.clear();
			bodySizeMap.clear();

			int v_space = getBounds().height - (int)Math.round(height*Math.min(horizontalScale, verticalScale));
			int h_space = getBounds().width - (int)Math.round(width*Math.min(horizontalScale, verticalScale));
			
			int firstPlanetLocation = getScaledNumber(getRawSize(firstPlanet.getPlanetaryRadius()))*3/4;
			int largestPlanetSize = getScaledNumber(getRawSize(largestPlanet.getPlanetaryRadius()));
			
			for(Body planet : Body.values()) {
				int planetSize = 0;
				int planetLocation = 0;
				if(planet.getParent()==Body.SUN && bodies.contains(planet)) {
					planetSize = getScaledNumber(getRawSize(planet.getPlanetaryRadius()));
					planetLocation = planet==firstPlanet?firstPlanetLocation:
						firstPlanetLocation + getScaledNumber(getRawLocation(planet.getOrbitalRadius()-firstPlanet.getOrbitalRadius()));
					bodyLocationMap.put(planet, new Point(h_space/2 + planetLocation, v_space/2 + largestPlanetSize*3/4));
					bodySizeMap.put(planet, planetSize);
					
					drawBody(planet, g);
					
					for(Body moon : Body.values()) {
						int moonSize = 0;
						int moonLocation = 0;
						if(moon.getParent()==planet && bodies.contains(moon)) {
							moonSize = getScaledNumber(getRawSize(moon.getPlanetaryRadius()));
							moonLocation = getScaledNumber(getRawLocation(moon.getOrbitalRadius()));
						
							bodyLocationMap.put(moon, new Point(bodyLocationMap.get(planet).x, 
									bodyLocationMap.get(planet).y + moonLocation));
							bodySizeMap.put(moon, moonSize);
							
							drawBody(moon, g);
						}
					}
				}
			}
		}
	}
	private void drawBody(Body body, Graphics g) {
		if(body.getImage()==null) {
			g.setColor(body.getColor());
			g.fillOval(bodyLocationMap.get(body).x - bodySizeMap.get(body)/2, 
					bodyLocationMap.get(body).y - bodySizeMap.get(body)/2,
					bodySizeMap.get(body), bodySizeMap.get(body));					
		} else {
			g.drawImage(body.getImage(), 
					bodyLocationMap.get(body).x - bodySizeMap.get(body)/2, 
					bodyLocationMap.get(body).y - bodySizeMap.get(body)/2,
					bodySizeMap.get(body), bodySizeMap.get(body), null);
		}
		if(drawOrbitalLimits) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f));
			g.setColor(Color.LIGHT_GRAY);
			g.drawOval(bodyLocationMap.get(body).x - bodySizeMap.get(body)*3/4, 
					bodyLocationMap.get(body).y - bodySizeMap.get(body)*3/4,
					bodySizeMap.get(body)*3/2, 
					bodySizeMap.get(body)*3/2);
		}
	}
	
	/**
	 * Gets the scaled number.
	 * 
	 * @param number the number
	 * 
	 * @return the scaled number
	 */
	public int getScaledNumber(double number) {
		return (int)Math.round(number*Math.min(horizontalScale, verticalScale));
	}
	private double getRawSize(double size) {
		return (sizeDisparity - 1)/(Math.PI)*Math.atan(sizeScale*(size-sizeBasis))+(sizeDisparity + 1)/2d;
	}
	private double getRawLocation(double location) {
		return (locationDisparity - 1)/(Math.PI)*Math.atan(locationScale*(location-locationBasis))+(locationDisparity + 1)/2d;
	}
	private Network getNetwork() {
		return networkPanel.getNetwork();
	}
	
	/**
	 * Gets the size scale.
	 * 
	 * @return the size scale
	 */
	public double getSizeScale() {
		return sizeScale;
	}
	
	/**
	 * Sets the size scale.
	 * 
	 * @param sizeScale the new size scale
	 */
	public void setSizeScale(double sizeScale) {
		this.sizeScale = sizeScale;
	}
	
	/**
	 * Gets the size basis.
	 * 
	 * @return the size basis
	 */
	public double getSizeBasis() {
		return sizeBasis;
	}
	
	/**
	 * Sets the size basis.
	 * 
	 * @param sizeBasis the new size basis
	 */
	public void setSizeBasis(double sizeBasis) {
		this.sizeBasis = sizeBasis;
	}
	
	/**
	 * Gets the location scale.
	 * 
	 * @return the location scale
	 */
	public double getLocationScale() {
		return locationScale;
	}
	
	/**
	 * Sets the location scale.
	 * 
	 * @param locationScale the new location scale
	 */
	public void setLocationScale(double locationScale) {
		this.locationScale = locationScale;
	}
	
	/**
	 * Gets the location basis.
	 * 
	 * @return the location basis
	 */
	public double getLocationBasis() {
		return locationBasis;
	}
	
	/**
	 * Sets the location basis.
	 * 
	 * @param locationBasis the new location basis
	 */
	public void setLocationBasis(double locationBasis) {
		this.locationBasis = locationBasis;
	}
	
	/**
	 * Gets the size disparity.
	 * 
	 * @return the size disparity
	 */
	public double getSizeDisparity() {
		return sizeDisparity;
	}
	
	/**
	 * Sets the size disparity.
	 * 
	 * @param sizeDisparity the new size disparity
	 */
	public void setSizeDisparity(double sizeDisparity) {
		this.sizeDisparity = sizeDisparity;
	}
	
	/**
	 * Gets the location disparity.
	 * 
	 * @return the location disparity
	 */
	public double getLocationDisparity() {
		return locationDisparity;
	}
	
	/**
	 * Sets the location disparity.
	 * 
	 * @param locationDisparity the new location disparity
	 */
	public void setLocationDisparity(double locationDisparity) {
		this.locationDisparity = locationDisparity;
	}
	
	/**
	 * Gets the body location.
	 * 
	 * @param body the body
	 * 
	 * @return the body location
	 */
	public Point getBodyLocation(Body body) {
		if(bodyLocationMap.keySet().contains(body)) return bodyLocationMap.get(body);
		else return new Point();
	}
	
	/**
	 * Gets the body size.
	 * 
	 * @param body the body
	 * 
	 * @return the body size
	 */
	public Integer getBodySize(Body body) {
		if(bodySizeMap.keySet().contains(body)) return bodySizeMap.get(body);
		else return new Integer(0);
	}
	
	/**
	 * Checks if is draw orbital limits.
	 * 
	 * @return true, if is draw orbital limits
	 */
	public boolean isDrawOrbitalLimits() {
		return drawOrbitalLimits;
	}
	
	/**
	 * Sets the draw orbital limits.
	 * 
	 * @param drawOrbitalLimits the new draw orbital limits
	 */
	public void setDrawOrbitalLimits(boolean drawOrbitalLimits) {
		this.drawOrbitalLimits = drawOrbitalLimits;
	}
	
	/**
	 * Checks if is simple mode.
	 * 
	 * @return true, if is simple mode
	 */
	public boolean isSimpleMode() {
		return simpleMode;
	}
	
	/**
	 * Sets the simple mode.
	 * 
	 * @param simpleMode the new simple mode
	 */
	public void setSimpleMode(boolean simpleMode) {
		this.simpleMode = simpleMode;
	}
	
	/**
	 * Checks if is display sun.
	 * 
	 * @return true, if is display sun
	 */
	public boolean isDisplaySun() {
		return displaySun;
	}
	
	/**
	 * Sets the display sun.
	 * 
	 * @param displaySun the new display sun
	 */
	public void setDisplaySun(boolean displaySun) {
		this.displaySun = displaySun;
	}
}
