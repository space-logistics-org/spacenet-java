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
package edu.mit.spacenet.gui.resource;

import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.domain.resource.ResourceType;

/**
 * A factory for creating ResourcePanelEditor objects.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class ResourceEditorPanelFactory {
	public static ResourceEditorPanel createResourceEditorPanel(ResourceEditorDialog dialog, I_Resource resource) {
		if(resource.getResourceType()==ResourceType.RESOURCE)
			return new ResourceEditorPanel(dialog,(Resource)resource);
		else if (resource.getResourceType()==ResourceType.ITEM){
			return new ResourceEditorPanel(dialog,(Item)resource);
		}
		else return null;
	}
}
