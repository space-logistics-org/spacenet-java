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
package edu.mit.spacenet.domain.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;

/**
 * Represents the network of nodes and edges for the simulation to act over.
 * 
 * @author Paul Grogan
 */
public class Network {
	private SortedSet<Node> nodes;
	private SortedSet<Edge> edges;
	private SortedMap<Integer, I_Element> registrar;
	private SortedMap<Integer, I_Element> removedRegistrar;
	
	/**
	 * Default constructor that initializes the structures for the nodes and
	 * edges.
	 */
	public Network() {
		nodes = new TreeSet<Node>();
		edges = new TreeSet<Edge>();
		registrar = new TreeMap<Integer, I_Element>();
		removedRegistrar = new TreeMap<Integer, I_Element>();
	}
	
	/**
	 * Gets the set of nodes.
	 * 
	 * @return the set of nodes
	 */
	public SortedSet<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets a node by its type identifier.
	 * 
	 * @param tid the node's type identifier (primary key)
	 * 
	 * @return the node, or null if not found
	 */
	public Node getNodeByTid(int tid) {
		Node node = null;
		for(Node n : nodes) {
			if(n.getTid() == tid) {
				node = n;
				break;
			}
		}
		return node;
	}
	
	/**
	 * Gets the set of edges.
	 * 
	 * @return the set of edges
	 */
	public SortedSet<Edge> getEdges() {
		return edges;
	}
	
	/**
	 * Gets an edge by its type identifier.
	 * 
	 * @param tid the edge's type identifier (primary key)
	 * 
	 * @return the edge, or null if not found
	 */
	public Edge getEdgeByTid(int tid) {
		Edge edge = null;
		for(Edge e : edges) { 
			if(e.getTid() == tid) { 
				edge = e; 
				break;
			}
		}
		return edge;
	}
	
	/**
	 * Adds a network component (node or edge) to the network. In the case of an
	 * edge, the origin and destination node must exist in the network to be
	 * successful.
	 * 
	 * @param c the network component
	 * 
	 * @return true, if successful, false otherwise
	 */
	public boolean add(Location c) {
		if(c instanceof Node) return nodes.add((Node)c);
		else {
			Edge e = (Edge)c;
			if(nodes.contains(e.getOrigin()) && nodes.contains(e.getDestination()))
				return edges.add(e);	
			else return false;
		}
	}
	
	/**
	 * Removes a network component (node or edge) from the network. In the case
	 * of a node, it will also remove all associated edges from the network.
	 * 
	 * @param n the network component
	 * 
	 * @return true, if successful, false otherwise
	 */
	public boolean remove(Location n) {
		if(n instanceof Node) {
			Set<Edge> edgesToRemove = new HashSet<Edge>();
			for(Edge e : edges) {
				if(e.getOrigin().equals(n) || e.getDestination().equals(n))
					edgesToRemove.add(e);
			}
			for(Edge e : edgesToRemove) {
				edges.remove(e);
			}
			return nodes.remove((Node)n);
		}
		else return edges.remove((Edge)n);
	}
	
	/**
	 * Gets the locations.
	 * 
	 * @return the locations
	 */
	public List<Location> getLocations() {
		ArrayList<Location> locations = new ArrayList<Location>();
		for(Node node : getNodes()) {
			locations.add(node);
		}
		for(Edge edge : getEdges()) {
			locations.add(edge);
		}
		Collections.sort(locations);
		return locations;
	}
	
	/**
	 * Gets the registry of elements.
	 * 
	 * @return the element registry
	 */
	public SortedMap<Integer, I_Element> getRegistrar() {
		return registrar;
	}
	
	/**
	 * Gets the registry of removed elements.
	 *
	 * @return the removed element registry
	 */
	public SortedMap<Integer, I_Element> getRemovedRegistrar() {
		if(removedRegistrar==null) removedRegistrar = new TreeMap<Integer, I_Element>();
		return removedRegistrar;
	}
	
	/**
	 * Gets the contents of.
	 * 
	 * @param container the container
	 * 
	 * @return the contents of
	 */
	public SortedSet<I_Element> getContentsOf(I_Container container) {
		TreeSet<I_Element> elements = new TreeSet<I_Element>();
		for(I_Element element : getRegistrar().values()) {
			if(element.getContainer().equals(container)) elements.add(element);
		}
		return elements;
	}
	
	/**
	 * Gets the complete contents of.
	 * 
	 * @param location the location
	 * 
	 * @return the complete contents of
	 */
	public SortedSet<I_Element> getCompleteContentsOf(Location location) {
		TreeSet<I_Element> elements = new TreeSet<I_Element>();
		for(I_Element element : getRegistrar().values()) {
			if(element.getLocation().equals(location)) elements.add(element);
		}
		return elements;
	}
	
	/**
	 * Prints a network representation to console.
	 */
	public void print() {
		System.out.println("Network Nodes");
		for(Node n : nodes) {
			n.print(1);
		}
		System.out.println("Network Edges");
		for(Edge e : edges) {
			e.print(1);
		}
	}
}
