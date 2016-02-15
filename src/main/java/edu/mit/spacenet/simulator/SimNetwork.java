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
package edu.mit.spacenet.simulator;

import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A log of the scenario network at a particular time.
 * 
 * @author Paul Grogan
 */
public class SimNetwork implements Comparable<SimNetwork> {
	private double time;
	private Network network;
	
	/**
	 * Instantiates a new sim network.
	 * 
	 * @param time the simulation time
	 * @param network the scenario network
	 */
	public SimNetwork(double time, Network network) {
		this.time = time;
		this.network = network;
	}
	
	/**
	 * Gets the simulation time of the log.
	 * 
	 * @return the simulation time
	 */
	public double getTime() {
		return GlobalParameters.getRoundedTime(time);
	}
	
	/**
	 * Gets the scenario network.
	 * 
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SimNetwork network) {
		return Double.compare(getTime(), network==null?0:network.getTime());
	}
}