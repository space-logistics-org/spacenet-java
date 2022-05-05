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
package edu.mit.spacenet.domain.network.edge;

import edu.mit.spacenet.domain.DomainType;

/**
 * Class that represents an impulsive burn (either OMS or RCS) to achieve a specified delta-v (m/s),
 * offset from the propulsive maneuver by a time.
 * 
 * @author Paul Grogan
 */
public class Burn extends DomainType implements Comparable<Burn> {
  private double time;
  private BurnType burnType;
  private double deltaV;

  /**
   * The default constructor sets the burn type to OMS.
   */
  public Burn() {
    super();
    setBurnType(BurnType.OMS);
  }

  /**
   * The inline constructor.
   * 
   * @param time the burn time (days)
   * @param burnType the burn type
   * @param deltaV the delta-v (meters per second)
   */
  public Burn(double time, BurnType burnType, double deltaV) {
    super();
    setTime(time);
    setBurnType(burnType);
    setDeltaV(deltaV);
  }

  /**
   * Gets the time offset from the initial propulsive maneuver.
   * 
   * @return the time offset (days)
   */
  public double getTime() {
    return time;
  }

  /**
   * Sets the time offset from the initial propulsive maneuver.
   * 
   * @param time the time offset (days)
   */
  public void setTime(double time) {
    this.time = time;
  }

  /**
   * Gets the burn type.
   * 
   * @return the burn type
   */
  public BurnType getBurnType() {
    return burnType;
  }

  /**
   * Sets the burn type.
   * 
   * @param burnType the burn type
   */
  public void setBurnType(BurnType burnType) {
    this.burnType = burnType;
  }

  /**
   * Gets the delta-v.
   * 
   * @return the delta-v (meters per second)
   */
  public double getDeltaV() {
    return deltaV;
  }

  /**
   * Sets the delta-v.
   * 
   * @param deltaV the delta-v (meters per second)
   */
  public void setDeltaV(double deltaV) {
    this.deltaV = deltaV;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.domain.DomainType#toString()
   */
  @Override
  public String toString() {
    return burnType + " (" + deltaV + " m/s)";
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Burn o) {
    if (o == null)
      return -1;
    return new Double(getTime()).compareTo(o.getTime());
  }
}
