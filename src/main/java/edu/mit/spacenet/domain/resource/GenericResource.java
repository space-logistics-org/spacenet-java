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
package edu.mit.spacenet.domain.resource;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;

/**
 * A specific type of resource that is forced to have type identifier -cos and
 * (unit) mass 1.
 * 
 * @author Paul Grogan
 */
public class GenericResource extends Resource {
	
	/**
	 * The constructor that assigns a TID of -COS and a (unit) mass of 1. It
	 * also sets the class of supply and creates a generic name based on it.
	 * 
	 * @param classOfSupply the resource class of supply
	 */
	public GenericResource(ClassOfSupply classOfSupply) {
		super();
		setTid(-classOfSupply.getId());
		setName("Generic COS " + classOfSupply.getId() + " (" + classOfSupply.getName() + ")");
		setUnitMass(1);
		setUnits("kg");
		setClassOfSupply(classOfSupply);
	}
	
	/**
	 * Constructor that sets the class of supply and environment. Also assigns
	 * a TID of -COS and a (unit) mass of 1.
	 * 
	 * @param classOfSupply the resource class of supply
	 * @param environment the resource environment
	 */
	public GenericResource(ClassOfSupply classOfSupply, Environment environment) {
		this(classOfSupply);
		setEnvironment(environment);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.Resource#getUnitVolume()
	 */
	@Override
	public double getUnitVolume() {
		if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS1)) return 1900/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS201)) return 1/1000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS2)) return 7000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS3)) return 5000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS4)) return 3000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS5)) return 7000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS6)) return 5000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS7)) return 5000/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS8)) return 3500/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS9)) return 3500/1000000d;
		else if(getClassOfSupply().isInstanceOf(ClassOfSupply.COS10)) return 7000/1000000d;
		else return super.getUnitVolume();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.Resource#getPackingFactor()
	 */
	@Override
	public double getPackingFactor() {
		if(getClassOfSupply().equals(ClassOfSupply.COS203)) return 1.0;
		else if(getClassOfSupply().equals(ClassOfSupply.COS201)) return 0.5;
		else if(getClassOfSupply().equals(ClassOfSupply.COS6)) return 0;
		else if(getEnvironment().equals(Environment.UNPRESSURIZED)) return 0.6;
		//else if(getEnvironment().equals(Environment.PRESSURIZED_INTERNAL)) return 0.2;
		//else if(getEnvironment().equals(Environment.PRESSURIZED_EXTERNAL)) return 1.2;
		else if(getEnvironment().equals(Environment.PRESSURIZED)) return 1.2;
		else return super.getPackingFactor();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.resource.Resource#getResourceType()
	 */
	@Override
	public ResourceType getResourceType() {
		return ResourceType.GENERIC;
	}
}