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
package edu.mit.spacenet.domain.element;

import java.util.SortedMap;
import java.util.TreeMap;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * An element that can hold many types of continuous resources up to set
 * mass, volume, and environment constraints (volume and environment can be
 * disabled via global parameters).
 * 
 * @author Paul Grogan
 */
public class ResourceContainer extends Element implements I_ResourceContainer {
	private double maxCargoMass, maxCargoVolume;
	private Environment cargoEnvironment;
	protected SortedMap<I_Resource, Double> contents;
	
	/**
	 * Instantiates a new resource container.
	 */
	public ResourceContainer() {
		super();
		setCargoEnvironment(Environment.UNPRESSURIZED);
		setClassOfSupply(ClassOfSupply.COS501);
		contents = new TreeMap<I_Resource, Double>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#canAdd(edu.mit.spacenet.domain.resource.I_Resource, double)
	 */
	public boolean canAdd(I_Resource resource, double amount) {
		if(getCargoMass() + resource.getUnitMass()*amount - maxCargoMass > GlobalParameters.getMassPrecision()/2d) {
			return false; // mass constrained
		} else if(GlobalParameters.isVolumeConstrained()
					&& getCargoVolume() + resource.getUnitVolume()*amount - maxCargoVolume > GlobalParameters.getVolumePrecision()/2d) {
			return false; // volume constrained
		} else if(GlobalParameters.isEnvironmentConstrained()
					&& resource.getEnvironment().equals(Environment.PRESSURIZED)
					&& getCargoEnvironment().equals(Environment.UNPRESSURIZED)) {
			return false; // environment constrained
		} else return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#add(edu.mit.spacenet.domain.resource.I_Resource, double)
	 */
	public boolean add(I_Resource resource, double amount) {
		if(canAdd(resource, amount)) {
			if(contents.keySet().contains(resource)) {
				double q = contents.get(resource);
				contents.put(resource, q+amount);
			} else {
				contents.put(resource, amount);
			}
			return true;
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#remove(edu.mit.spacenet.domain.resource.I_Resource, double)
	 */
	public boolean remove(I_Resource resource, double amount) {
		if(contents.keySet().contains(resource)
				&& contents.get(resource) > amount) {
			double q = contents.get(resource);
			contents.put(resource, q-amount);
			return true;
		} else if(contents.keySet().contains(resource)
				&& contents.get(resource) == amount) {
			contents.remove(resource);
			return true;
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoEnvironment()
	 */
	public Environment getCargoEnvironment() {
		return cargoEnvironment;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#setCargoEnvironment(edu.mit.spacenet.domain.Environment)
	 */
	public void setCargoEnvironment(Environment cargoEnvironment) {
		this.cargoEnvironment = cargoEnvironment;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getMaxCargoMass()
	 */
	public double getMaxCargoMass() {
		return GlobalParameters.getRoundedMass(maxCargoMass);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#setMaxCargoMass(double)
	 */
	public void setMaxCargoMass(double maxCargoMass) {
		this.maxCargoMass = maxCargoMass;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getMaxCargoVolume()
	 */
	public double getMaxCargoVolume() {
		return GlobalParameters.getRoundedVolume(maxCargoVolume);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#setMaxCargoVolume(double)
	 */
	public void setMaxCargoVolume(double maxCargoVolume) {
		this.maxCargoVolume = maxCargoVolume;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoVolume()
	 */
	public double getCargoVolume() {
		double volume = 0;
		for(I_Resource r : contents.keySet()) {
			volume += r.getUnitVolume()*contents.get(r);
		}
		return GlobalParameters.getRoundedVolume(volume);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getCargoMass()
	 */
	public double getCargoMass() {
		double mass = 0;
		for(I_Resource r : contents.keySet()) {
			mass += r.getUnitMass()*contents.get(r);
		}
		return GlobalParameters.getRoundedMass(mass);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.I_ResourceContainer#getContents()
	 */
	public SortedMap<I_Resource, Double> getContents() {
		return contents;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Element#getTotalMass()
	 */
	@Override
	public double getTotalMass() {
		return GlobalParameters.getRoundedMass(super.getTotalMass() + getCargoMass());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Element#getTotalMass(edu.mit.spacenet.domain.ClassOfSupply)
	 */
	@Override
	public double getTotalMass(ClassOfSupply cos) {
		double amount = super.getTotalMass(cos);
		
		for(I_Resource resource : getContents().keySet()) {
			if(resource.getClassOfSupply().isInstanceOf(cos)) {
				amount += getContents().get(resource)*resource.getUnitMass();
			}
		}
		
		return GlobalParameters.getRoundedMass(amount);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.DomainType#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		super.print(tabOrder);
		for(I_Resource r : contents.keySet()) {
			r.print(tabOrder + 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Element#satisfyDemands(edu.mit.spacenet.domain.resource.DemandSet, edu.mit.spacenet.simulator.I_Simulator)
	 */
	@Override
	public void satisfyDemands(DemandSet demands, I_Simulator simulator) {
		super.satisfyDemands(demands, simulator);
		for(Demand demand : demands) {
			if(demand.getAmount() > 0) { // consumption
				for(I_Resource resource : contents.keySet()) {
					if(resource.isSubstitutableFor(demand.getResource())) {
						if(demand.getAmount() >= contents.get(resource)) {
							// don't consume science demands!
							demand.setAmount(demand.getAmount() - contents.get(resource));
							if(!demand.getResource().getClassOfSupply().isInstanceOf(ClassOfSupply.COS6))
								contents.put(resource,0d);
						} else {
							// don't consume science demands!
							if(!demand.getResource().getClassOfSupply().isInstanceOf(ClassOfSupply.COS6))
								contents.put(resource, contents.get(resource)-demand.getAmount());
							demand.setAmount(0);
							break;
						}
					}
				}
			} else { // production
				//TODO need to check for resource compatibility (e.g. water in water tanks)
				boolean containsResource = false;
				for(I_Resource resource : contents.keySet()) if(resource.equals(demand.getResource())) containsResource = true;
				if(!containsResource) contents.put(demand.getResource(), 0d);
				for(I_Resource resource : contents.keySet()) {
					if(resource.equals(demand.getResource())) {
						containsResource = true;
						if(-demand.getAmount() > getMaxCargoMass() - getCargoMass()) {
							demand.setAmount(demand.getAmount() + (getMaxCargoMass() - getCargoMass()));
							contents.put(resource, contents.get(resource) + getMaxCargoMass() - getCargoMass()); 
						} else {
							contents.put(resource, contents.get(resource) + -demand.getAmount()); 
							demand.setAmount(0);
						}
					}
				}
			}
		}
		demands.clean();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.element.Element#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.RESOURCE_CONTAINER;
	}
}