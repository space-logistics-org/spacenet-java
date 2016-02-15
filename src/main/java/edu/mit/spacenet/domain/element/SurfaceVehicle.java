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
package edu.mit.spacenet.domain.element;


import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A vehicle with an assigned maximum speed and a resource container to
 * represent a fuel tank. Note that unlike a propulsive vehicle, consumption of
 * fuel should be handled by a demand model rather than through impulsive burns.
 * 
 * @author Paul Grogan
 */
public class SurfaceVehicle extends Carrier {
	private double maxSpeed;
	private ResourceTank fuelTank;
	
	/**
	 * The default constructor that initializes the fuel tank.
	 */
	public SurfaceVehicle() {
		super();
		fuelTank = new ResourceTank();
		fuelTank.setName(getName() + " Fuel Tank");
		fuelTank.setContainer(this);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		super.setName(name);
		if(fuelTank!=null)
			fuelTank.setName(getName() + " Fuel Tank");
	}
	
	/**
	 * Gets the maximum speed.
	 * 
	 * @return the maximum speed (kilometers per hour)
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
	 * Sets the maximum speed.
	 * 
	 * @param maxSpeed the maximum speed (kilometers per hour)
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * Gets the fuel tank resource container.
	 * 
	 * @return the fuel tank resource container
	 */
	public ResourceTank getFuelTank() {
		return fuelTank;
	}
	
	/**
	 * Sets the fuel tank resource container.
	 * 
	 * @param fuelTank the fuel tank resource container
	 */
	public void setFuelTank(ResourceTank fuelTank) {
		this.fuelTank = fuelTank;
		if(fuelTank!=null) {
			fuelTank.setName(getName() + " Fuel Tank");
			fuelTank.setContainer(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass()
	 */
	@Override
	public double getTotalMass() {
		return GlobalParameters.getRoundedMass(super.getTotalMass() + fuelTank.getTotalMass());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
	 */
	@Override
	public double getTotalMass(ClassOfSupply cos) {
		double amount = super.getTotalMass(cos);
		if(getFuelTank()!=null) amount += getFuelTank().getTotalMass(cos);
		return GlobalParameters.getRoundedMass(amount);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		fuelTank.print(tabOrder+1);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#satisfyDemands(edu.mit.spacenet.domain.resource.DemandSet, edu.mit.spacenet.simulator.I_Simulator)
	 */
	@Override
	public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
		fuelTank.satisfyDemands(demands, simulator);
		super.satisfyDemands(demands, simulator);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.SURFACE_VEHICLE;
	}
}
