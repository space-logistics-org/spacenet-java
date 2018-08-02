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
package edu.mit.spacenet.gui.element;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.ResourceContainer;
import edu.mit.spacenet.domain.element.ResourceTank;
import edu.mit.spacenet.domain.element.SurfaceVehicle;

/**
 * An abstract class that contains a factory method to build the appropriate
 * combination of element panels.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class ElementPanelFactory {
	
	/**
	 * A factory method that returns a list of element panels appropriate for
	 * viewing and editing the element.
	 * 
	 * @param elementDialog the parent element dialog component
	 * @param element the element
	 * 
	 * @return a list of element panels
	 */
	public static List<AbstractElementPanel> createElementPanel(ElementDialog elementDialog, I_Element element) {
		List<AbstractElementPanel> panels = new ArrayList<AbstractElementPanel>();
		
		switch(element.getElementType()) {
		case ELEMENT:
			break;
		case RESOURCE_CONTAINER:
			panels.add(new ResourceContainerPanel(elementDialog, (ResourceContainer)element));
			break;
		case RESOURCE_TANK:
			panels.add(new ResourceTankPanel(elementDialog, (ResourceTank)element));
			break;
		case CREW_MEMBER:
			panels.add(new CrewMemberPanel(elementDialog, (CrewMember)element));
			break;
		case CARRIER:
			panels.add(new CarrierPanel(elementDialog, (Carrier)element));
			break;
		case PROPULSIVE_VEHICLE:
			panels.add(new CarrierPanel(elementDialog, (PropulsiveVehicle)element));
			panels.add(new PropulsiveVehiclePanel(elementDialog, (PropulsiveVehicle)element));
			break;
		case SURFACE_VEHICLE:
			panels.add(new CarrierPanel(elementDialog, (SurfaceVehicle)element));
			panels.add(new SurfaceVehiclePanel(elementDialog, (SurfaceVehicle)element));
			break;
		default: throw new RuntimeException("Unsupported Element");
		}
		
		return panels;
	}
}
