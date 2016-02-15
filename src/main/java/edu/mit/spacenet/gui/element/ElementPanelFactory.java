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
