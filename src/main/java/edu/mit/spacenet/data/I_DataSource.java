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
package edu.mit.spacenet.data;

import java.util.Date;
import java.util.List;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Resource;

/**
 * The data source interface allows for the abstracted loading of libraries and
 * elements from a general data source. Nodes, edges, and resources are handled
 * by loading and maintaining <em>in memory</em> a library of objects for usage
 * in the scenario. Elements are handled differently, where only previews are 
 * loaded and maintained in memory to save on space -- when an element is used 
 * in a scenario (during a Create Elements event), the data source is queried to 
 * build a new element object.
 * 
 * @author Paul Grogan
 */
public interface I_DataSource {
	
	/**
	 * Deletes an edge from the data source. Also deletes any associated objects
	 * (e.g. burns for space edges).
	 * 
	 * @param tid the primary key (type ID) of the edge to delete
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public boolean deleteEdge(int tid) throws Exception;
	
	/**
	 * Deletes an element from the data source. Also deletes all associated
	 * part applications and states.
	 * 
	 * @param tid the primary key (type ID) of the element to delete
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public boolean deleteElement(int tid) throws Exception;
	
	/**
	 * Deletes a node from the data source. If node is used in any edges in the
	 * edge library, this method will fail (return false).
	 * 
	 * @param tid the primary key (type ID) of the node to delete
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public boolean deleteNode(int tid) throws Exception;
	
	/**
	 * Deletes a resource from the data source. If resource is used in any
	 * elements, demand models, or demands, this method will fail (return 
	 * false).
	 * 
	 * @param tid the primary key (type ID) of the resource to delete
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public boolean deleteResource(int tid) throws Exception;
	
	/**
	 * Gets the list of resources from the resource library, filtered to only
	 * include the continuous resources.
	 * 
	 * @return the list of continuous resources
	 */
	public List<Resource> getContinuousResourceLibrary();
	
	/**
	 * Gets the data source type.
	 * 
	 * @return the data source type
	 */
	public DataSourceType getDataSourceType();
	
	/**
	 * Gets a list of edges loaded from the data source.
	 * 
	 * @return the list of edges
	 */
	public List<Edge> getEdgeLibrary();
	
	/**
	 * Gets a list of element previews loaded from the data source.
	 * 
	 * @return the list of element previews
	 */
	public List<ElementPreview> getElementPreviewLibrary();
		
	/**
	 * Gets the list of resources from the resource library, filtered to only
	 * include the discrete resources.
	 * 
	 * @return the list of discrete resources
	 */
	public List<I_Item> getItemLibrary();
	
	/**
	 * Gets the date that the data source was last loaded.
	 * 
	 * @return the last load date
	 */
	public Date getLastLoadDate();
	
	/**
	 * Gets the name of the data source.
	 * 
	 * @return the data source name
	 */
	public String getName();
	
	/**
	 * Gets a list of nodes loaded from the data source.
	 * 
	 * @return the list of nodes
	 */
	public List<Node> getNodeLibrary();
	
	/**
	 * Gets a list of resources loaded from the data source.
	 * 
	 * @return the list of resources
	 */
	public List<I_Resource> getResourceLibrary();
	
	/**
	 * Load edge from the edge library by its primary key (type ID).
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @return the edge
	 */
	public Edge loadEdge(int tid);
	
	/**
	 * Loads (reloads) the edge library.
	 */
	public void loadEdgeLibrary() throws Exception;
	
	/**
	 * Loads an element from the data source by its primary key (type ID).
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @return the element
	 */
	public I_Element loadElement(int tid) throws Exception;
	
	/**
	 * Loads (reloads) the element library.
	 */
	public void loadElementLibrary() throws Exception;
	
	/**
	 * Loads an element preview by its primary key (type ID).
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @return the element preview
	 */
	public ElementPreview loadElementPreview(int tid);
	
	/**
	 * Loads (reloads) the libraries (nodes, edges, resources, elements).
	 */
	public void loadLibraries() throws Exception;
	
	/**
	 * Loads (reloads) the libraries with an option to only load certain tables.
	 * 
	 * @param loadNodes update the nodes table
	 * @param loadEdges update the edges table
	 * @param loadResources update the resources table
	 */
	public void loadLibraries(boolean updateNodes, boolean updateEdges, 
			boolean updateResources) throws Exception;
	
	/**
	 * Loads a node from the node library by its primary key (type ID).
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @return the node
	 */
	public Node loadNode(int tid);
	
	/**
	 * Loads (reloads) the node library.
	 */
	public void loadNodeLibrary() throws Exception;
	
	/**
	 * Loads a resource from the resource library by its primary key (type ID)
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @return the resource
	 */
	public I_Resource loadResource(int tid);
	
	/**
	 * Load resource library.
	 */
	public void loadResourceLibrary() throws Exception;
	
	/**
	 * Prints the data source to console.
	 */
	public void print();
	
	/**
	 * Save an edge to the data source and updates the edge library accordingly. 
	 * Updates existing entry if modified or inserts a new entry (assigning
	 * a new primary key) if created. Also saves any associated objects (e.g. 
	 * burns for a space edge).
	 * 
	 * @param edge the edge
	 */
	public void saveEdge(Edge edge) throws Exception;
	
	/**
	 * Saves an element to the data source and updates the element preview 
	 * library accordingly. Updates existing entry if modified or inserts a new
	 * entry (assigning a new primary key) if created. Also saves all associated 
	 * part applications and states.
	 * 
	 * @param element the element
	 */
	public void saveElement(I_Element element) throws Exception;
	
	/**
	 * Saves a node to the data source and updates the node library accordingly.
	 * Updates existing entry if modified or inserts a new entry (assigning
	 * a new primary key) if created. 
	 * 
	 * @param node the node
	 */
	public void saveNode(Node node) throws Exception;
	
	/**
	 * Saves a resource to the data source and updates the resource library
	 * accordingly. Updates existing entry if modified or inserts a new entry
	 * (assigning a new primary key) if created.
	 * 
	 * @param resource the resource
	 * 
	 * @throws Exception the exception
	 */
	public void saveResource(I_Resource resource) throws Exception;
	
	/**
	 * Runs a validation script on the data source, checking for any parse 
	 * errors.
	 * 
	 * @return a list of error messages
	 */
	public List<String> validateData() throws Exception;
	
	/**
	 * Formats the data source to accept data.
	 * 
	 * @throws Exception the exception
	 */
	public void format() throws Exception;
}
