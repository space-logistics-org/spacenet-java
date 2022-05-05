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
package edu.mit.spacenet.util;

/**
 * Class to contain various useful mathematical formulae, including the rocket equation.
 * 
 * @author Paul Grogan
 */
public final class Formulae {

  /** The gravitational constant (meters per second squared). */
  public static final double g0 = 9.81;

  /**
   * Gets the required fuel mass to achieve a specified delta-v by a stack of vehicles with a
   * specified specific impulse.
   * 
   * @param stackMass the mass of the vehicle stack (kilograms)
   * @param deltaV the delta-v to achieve (meters per second)
   * @param isp the specific impulse of the engine (seconds)
   * 
   * @return the mass of fuel required for the burn (kilograms)
   */
  public static final double getRequiredFuelMass(double stackMass, double deltaV, double isp) {
    return stackMass * (1 - Math.exp(-deltaV / (isp * g0)));
  }

  /**
   * Gets the achieved delta-v from a specified fuel mass by a stack of vehicles with a specified
   * specific impulse.
   * 
   * @param stackMass the mass of the vehicle stack (kilograms)
   * @param isp the specific impulse of the engine (seconds)
   * @param fuelMass the mass of fuel to be burned (kilograms)
   * 
   * @return the delta-v achieved (meters per second)
   */
  public static final double getAchievedDeltaV(double stackMass, double isp, double fuelMass) {
    return isp * g0 * Math.log(stackMass / (stackMass - fuelMass));
  }
}
