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

import edu.mit.spacenet.scenario.ItemDiscretization;

/**
 * A global access point to scenario options.
 * 
 * @author Paul Grogan
 */
public class GlobalParameters {
	private static final int DECIMAL_PRECISION = 1000;
	
	private static double timePrecision = 0.05;
	
	/**
	 * Gets the time precision.
	 * 
	 * @return the time precision (days)
	 */
	public static double getTimePrecision() {
		return timePrecision;
	}
	
	/**
	 * Sets the time precision.
	 * 
	 * @param timePrecision the time precision (days)
	 */
	public static void setTimePrecision(double timePrecision) {
		GlobalParameters.timePrecision = timePrecision;
	}
	
	/**
	 * Gets the rounded time according to the time precision.
	 * 
	 * @param time the raw time (days)
	 * 
	 * @return the rounded time (days)
	 */
	public static double getRoundedTime(double time) {
		return Math.round(Math.round(time/timePrecision)*timePrecision*DECIMAL_PRECISION)/((double)DECIMAL_PRECISION);
	}
	
	private static double demandPrecision = 0.01;
	
	/**
	 * Gets the demand precision.
	 * 
	 * @return the demand precision (units)
	 */
	public static double getDemandPrecision() {
		return demandPrecision;
	}
	
	/**
	 * Sets the demand precision.
	 * 
	 * @param demandPrecision the demand precision (units)
	 */
	public static void setDemandPrecision(double demandPrecision) {
		GlobalParameters.demandPrecision = demandPrecision;
	}
	
	/**
	 * Gets the rounded demand amount according to the demand precision.
	 * 
	 * @param demand the raw demand amount
	 * 
	 * @return the rounded demand amount
	 */
	public static double getRoundedDemand(double demand) {
		return Math.round(demand/demandPrecision)*demandPrecision;
	}
	
	private static double massPrecision = 0.01;
	
	/**
	 * Gets the mass precision.
	 * 
	 * @return the mass precision (kilograms)
	 */
	public static double getMassPrecision() {
		return massPrecision;
	}
	
	/**
	 * Sets the mass precision.
	 * 
	 * @param massPrecision the mass precision (kilograms)
	 */
	public static void setMassPrecision(double massPrecision) {
		GlobalParameters.massPrecision = massPrecision;
	}
	
	/**
	 * Gets the rounded mass according to the mass precision.
	 * 
	 * @param mass the raw mass (kilograms)
	 * 
	 * @return the rounded mass (kilograms)
	 */
	public static double getRoundedMass(double mass) {
		return Math.round(mass/massPrecision)*massPrecision;
	}
	
	private static double volumePrecision = 0.000001;
	
	/**
	 * Gets the volume precision.
	 * 
	 * @return the volume precision (cubic meters)
	 */
	public static double getVolumePrecision() {
		return volumePrecision;
	}
	
	/**
	 * Sets the volume precision.
	 * 
	 * @param volumePrecision the volume precision (cubic meters)
	 */
	public static void setVolumePrecision(double volumePrecision) {
		GlobalParameters.volumePrecision = volumePrecision;
	}
	
	/**
	 * Gets the rounded volume according to the volume precision.
	 * 
	 * @param volume the raw volume	(cubic meters)
	 * 
	 * @return the rounded volume (cubic meters)
	 */
	public static double getRoundedVolume(double volume) {
		return Math.round(volume/volumePrecision)*volumePrecision;
	}
	
	private static boolean volumeConstrained = false;
	
	/**
	 * Sets whether volume constraints should be active.
	 * 
	 * @param volumeConstrained true if volume constraints are active, false otherwise
	 */
	public static void setVolumeConstrained(boolean volumeConstrained) {
		GlobalParameters.volumeConstrained = volumeConstrained;
	}
	
	/**
	 * Gets whether volume constraints are active.
	 * 
	 * @return true if volume constraints are active, false otherwise
	 */
	public static boolean isVolumeConstrained() {
		return volumeConstrained;
	}
	
	private static boolean environmentConstrained = true;
	
	/**
	 * Sets whether environment constraints (pressurized items can only go
	 * inside pressurized containers) should be active.
	 * 
	 * @param environmentConstrained true if environment constraints are active, false otherwise
	 */
	public static void setEnvironmentConstrained(boolean environmentConstrained) {
		GlobalParameters.environmentConstrained = environmentConstrained;
	}
	
	/**
	 * Gets whether environment constraints (pressurized items can only go
	 * inside pressurized containers) are active.
	 * 
	 * @return true if environment constraints are active, false otherwise
	 */
	public static boolean isEnvironmentConstrained() {
		return environmentConstrained;
	}
	
	private static ItemDiscretization itemDiscretization = ItemDiscretization.NONE;
	
	/**
	 * Gets the item discretization option.
	 * 
	 * @return how items are discretized
	 */
	public static ItemDiscretization getItemDiscretization() {
		return itemDiscretization;
	}
	
	/**
	 * Sets the item discretization option.
	 * 
	 * @param itemDiscretization how items are discretized
	 */
	public static void setItemDiscretization(ItemDiscretization itemDiscretization) {
		GlobalParameters.itemDiscretization = itemDiscretization;
	}
	
	private static double itemAggregation = 0;
	
	/**
	 * Gets the item aggregation point, which ranges between 0 and 1. 0
	 * aggregates whole items at the first partial demand, 1 aggregates whole
	 * items after demands totaling one unit.
	 * 
	 * @return the item aggregation point (between 0 and 1)
	 */
	public static double getItemAggregation() {
		return itemAggregation;
	}
	
	/**
	 * Sets the item aggregation point, which ranges between 0 and 1. 0
	 * aggregates whole items at the first partial demand, 1 aggregates whole
	 * items after demands totaling one unit.
	 * 
	 * @param itemAggregation the item aggregation point (between 0 and 1)
	 */
	public static void setItemAggregation(double itemAggregation) {
		if(itemAggregation < 0) itemAggregation = 0;
		else if(itemAggregation > 1) itemAggregation = 1;
		GlobalParameters.itemAggregation = itemAggregation;
	}
	
	private static boolean scavengeSpares = false;
	
	/**
	 * Gets whether spares can be scavenged from decommissioned elements.
	 * 
	 * @return true if spares can be scavenged, false otherwise
	 */
	public static boolean isScavengeSpares() {
		return scavengeSpares;
	}
	
	/**
	 * Sets whether spares can be scavenged from decommissioned elements.
	 * 
	 * @param scavengeSpares true if spares can be scavenged, false otherwise
	 */
	public static void setScavengeSpares(boolean scavengeSpares) {
		GlobalParameters.scavengeSpares = scavengeSpares;
	}
}
