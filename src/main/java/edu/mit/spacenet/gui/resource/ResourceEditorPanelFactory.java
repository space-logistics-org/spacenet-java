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
