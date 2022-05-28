/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
    return GlobalParameters.getSingleton().getRoundedTime(time);
  }

  /**
   * Gets the scenario network.
   * 
   * @return the network
   */
  public Network getNetwork() {
    return network;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(SimNetwork network) {
    return Double.compare(getTime(), network == null ? 0 : network.getTime());
  }
}
