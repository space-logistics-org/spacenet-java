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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLayeredPane;

import edu.mit.spacenet.domain.network.Network;

/**
 * The component that is used for network visualization. There are three layers:
 * the body layer displays the celestial bodies, the element layer displays the
 * simulation elements, and the network layer displays the nodes and edges.
 * 
 * @author Nii Armar, Paul Grogan
 */
public class NetworkPanel extends JLayeredPane {
	private static final long serialVersionUID = 4872032814941089113L;
	private Network network;
	
	private BodyLayer bodyLayer;
	private NetworkLayer networkLayer;
	private ElementLayer elementLayer;
	
	private boolean isBlackBackground = true;
	
	/**
	 * Instantiates a new network panel.
	 * 
	 * @param network the network
	 */
	public NetworkPanel(Network network) {
		this();
		setNetwork(network);
	}
	public NetworkPanel() {
		bodyLayer = new BodyLayer(this);
		add(bodyLayer);
		networkLayer = new NetworkLayer(this);
		add(networkLayer);
		elementLayer = new ElementLayer(this);
		add(elementLayer);
		setPosition(bodyLayer, 2);
		setPosition(networkLayer, 1);
		setPosition(elementLayer, 0);
		
		if(isBlackBackground)
			setBackground(Color.BLACK);
		else
			setBackground(Color.WHITE);
		setOpaque(true);
		setPreferredSize(new Dimension(600,400));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// this function is used to synchronize the size of the layers
		super.setBounds(x,y,width,height);
		bodyLayer.setBounds(0,0,width,height);
		networkLayer.setBounds(0,0,width,height);
		elementLayer.setBounds(0,0,width,height);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JLayeredPane#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if(isBlackBackground)
			setBackground(Color.BLACK);
		else
			setBackground(Color.WHITE);
	}
	
	/**
	 * Checks if is black background.
	 * 
	 * @return true, if is black background
	 */
	public boolean isBlackBackground() {
		return isBlackBackground;
	}
	
	/**
	 * Sets the black background.
	 * 
	 * @param isBlackBackground the new black background
	 */
	public void setBlackBackground(boolean isBlackBackground) {
		this.isBlackBackground = isBlackBackground;
	}
	
	/**
	 * Gets the network.
	 * 
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}
	
	/**
	 * Sets the network.
	 * 
	 * @param network the new network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	/**
	 * Gets the body layer.
	 * 
	 * @return the body layer
	 */
	public BodyLayer getBodyLayer() {
		return bodyLayer;
	}
	
	/**
	 * Gets the network layer.
	 * 
	 * @return the network layer
	 */
	public NetworkLayer getNetworkLayer() {
		return networkLayer;
	}
	
	/**
	 * Gets the element layer.
	 * 
	 * @return the element layer
	 */
	public ElementLayer getElementLayer() {
		return elementLayer;
	}
}
