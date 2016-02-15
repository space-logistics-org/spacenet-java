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
 * A vehicle that is characterized by having one, either, or both OMS impulsive
 * burns and RCS impulsive burns capabilities.
 * 
 * @author Paul Grogan
 */
public class PropulsiveVehicle extends Carrier {
	private double omsIsp, rcsIsp;
	private ResourceTank omsFuelTank, rcsFuelTank;
	
	/**
	 * The default constructor that initializes the OMS and RCS resource
	 * containers.
	 */
	public PropulsiveVehicle() {
		super();
		omsFuelTank = new ResourceTank();
		omsFuelTank.setName(getName() + " OMS Fuel Tank");
		omsFuelTank.setContainer(this);
		rcsFuelTank = new ResourceTank();
		rcsFuelTank.setName(getName() + " RCS Fuel Tank");
		rcsFuelTank.setContainer(this);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		super.setName(name);
		if(omsFuelTank!=null) 
			omsFuelTank.setName(getName() + " OMS Fuel Tank");
		if(rcsFuelTank!=null && rcsFuelTank!=omsFuelTank)
			rcsFuelTank.setName(getName() + " RCS Fuel Tank");
	}
	
	/**
	 * Gets the Isp (specific impulse) of the OMS engine.
	 * 
	 * @return the OMS Isp (seconds)
	 */
	public double getOmsIsp() {
		return omsIsp;
	}
	
	/**
	 * Sets the Isp (specific impulse) of the OMS engine.
	 * 
	 * @param omsIsp the OMS Isp (seconds)
	 */
	public void setOmsIsp(double omsIsp) {
		this.omsIsp = omsIsp;
	}
	
	/**
	 * Gets the resource container representing the OMS fuel tank.
	 * 
	 * @return the OMS fuel tank resource container
	 */
	public ResourceTank getOmsFuelTank() {
		return omsFuelTank;
	}
	
	/**
	 * Sets the resource container representing the OMS fuel tank.
	 * 
	 * @param omsFuelTank the OMS fuel tank resource container
	 */
	public void setOmsFuelTank(ResourceTank omsFuelTank) {
		this.omsFuelTank = omsFuelTank;
		if(omsFuelTank!=null) {
			omsFuelTank.setName(getName() + " OMS Fuel Tank");
			omsFuelTank.setContainer(this);
		}
	}
	
	/**
	 * Gets the Isp (specific impulse) of the RCS engine.
	 * 
	 * @return the RCS Isp (seconds)
	 */
	public double getRcsIsp() {
		return rcsIsp;
	}
	
	/**
	 * Sets the Isp (specific impulse) of the RCS engine.
	 * 
	 * @param rcsIsp the RCS Isp (seconds)
	 */
	public void setRcsIsp(double rcsIsp) {
		this.rcsIsp = rcsIsp;
	}
	
	/**
	 * Gets the resource container representing the RCS fuel tank.
	 * 
	 * @return the RCS fuel tank resource container
	 */
	public ResourceTank getRcsFuelTank() {
		return rcsFuelTank;
	}
	
	/**
	 * Sets the resource container representing the RCS fuel tank.
	 * 
	 * @param rcsFuelTank the RCS fuel tank resource container
	 */
	public void setRcsFuelTank(ResourceTank rcsFuelTank) {
		this.rcsFuelTank = rcsFuelTank;
		if(rcsFuelTank!=null && rcsFuelTank!=omsFuelTank) {
			rcsFuelTank.setName(getName() + " RCS Fuel Tank");
			rcsFuelTank.setContainer(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass()
	 */
	@Override
	public double getTotalMass() {
		double mass = super.getTotalMass();
		if(omsFuelTank != null) 
			mass += omsFuelTank.getTotalMass();
		if(rcsFuelTank != null && rcsFuelTank != omsFuelTank) 
			mass += rcsFuelTank.getTotalMass();
		return GlobalParameters.getRoundedMass(mass);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
	 */
	@Override
	public double getTotalMass(ClassOfSupply cos) {
		double amount = super.getTotalMass(cos);
		if(getOmsFuelTank()!=null) 
			amount += getOmsFuelTank().getTotalMass(cos);
		if(getRcsFuelTank() != null && getRcsFuelTank()!=getOmsFuelTank()) 
			amount += getRcsFuelTank().getTotalMass(cos);
		return GlobalParameters.getRoundedMass(amount);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		if(omsFuelTank != null) 
			omsFuelTank.print(tabOrder+1);
		if(rcsFuelTank != null && rcsFuelTank != omsFuelTank) 
			rcsFuelTank.print(tabOrder+1);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#satisfyDemands(edu.mit.spacenet.domain.resource.DemandSet, edu.mit.spacenet.simulator.I_Simulator)
	 */
	@Override
	public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
		if(omsFuelTank != null) 
			omsFuelTank.satisfyDemands(demands, simulator);
		if(rcsFuelTank != null && rcsFuelTank != omsFuelTank) 
			rcsFuelTank.satisfyDemands(demands, simulator);
		super.satisfyDemands(demands, simulator);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Carrier#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.PROPULSIVE_VEHICLE;
	}
}
