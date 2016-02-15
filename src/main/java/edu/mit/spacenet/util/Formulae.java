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
package edu.mit.spacenet.util;

/**
 * Class to contain various useful mathematical formulae, including the rocket
 * equation.
 * 
 * @author Paul Grogan
 */
public final class Formulae {
	
	/** The gravitational constant (meters per second squared). */
	public static final double g0 = 9.81;
	
	/**
	 * Gets the required fuel mass to achieve a specified delta-v by a stack of
	 * vehicles with a specified specific impulse.
	 * 
	 * @param stackMass the mass of the vehicle stack (kilograms)
	 * @param deltaV the delta-v to achieve (meters per second)
	 * @param isp 	the specific impulse of the engine (seconds)
	 * 
	 * @return 		the mass of fuel required for the burn (kilograms)
	 */
	public static final double getRequiredFuelMass(double stackMass, 
			double deltaV, double isp) {
		return stackMass*(1-Math.exp(-deltaV/(isp*g0)));
	}
	
	/**
	 * Gets the achieved delta-v from a specified fuel mass by a stack of
	 * vehicles with a specified specific impulse.
	 * 
	 * @param stackMass the mass of the vehicle stack (kilograms)
	 * @param isp 	the specific impulse of the engine (seconds)
	 * @param fuelMass the mass of fuel to be burned (kilograms)
	 * 
	 * @return 		the delta-v achieved (meters per second)
	 */
	public static final double getAchievedDeltaV(double stackMass, 
			double isp, double fuelMass) {
		return isp*g0*Math.log(stackMass/(stackMass-fuelMass));
	}
}
