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
package edu.mit.spacenet.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.Element;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.ResourceContainer;
import edu.mit.spacenet.domain.element.State;
import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.model.RatedDemandModel;
import edu.mit.spacenet.domain.model.SparingByMassDemandModel;
import edu.mit.spacenet.domain.model.TimedImpulseDemandModel;
import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.BurnType;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.domain.resource.ResourceType;

/**
 * A spreadsheet format compatible with the features introduced in SpaceNet 2.5
 * 
 * @author Paul Grogan
 */
public class Database extends AbstractDataSource {
	private String host, user, password, database;
	private Integer port;
	private Connection connection;
	
	/**
	 * Instantiates a new database.
	 * 
	 * @param host the host
	 * @param port the port
	 * @param database the database
	 * @param user the user
	 * @param password the password
	 */
	public Database(String host, Integer port, String database, 
			String user, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Instantiates a new database and sets the port field to null.
	 * 
	 * @param host the host
	 * @param database the database
	 * @param user the user
	 * @param password the password
	 */
	public Database(String host, String database, String user, String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Instantiates a new database and sets all fields to default values.
	 */
	public Database() { 
		host = "";
		database = "";
		user = "";
		password = "";
		port = 3306;
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#deleteEdge(int)
	 */
	public boolean deleteEdge(int tid) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		try {
			if(edgeLibrary.remove(loadEdge(tid))) {
				stmt = connection.prepareStatement("DELETE FROM edges " +
						"WHERE id=? LIMIT 1");
				stmt.setInt(1, tid);
				int i = stmt.executeUpdate();
				if(i > 0) {
					stmt = connection.prepareStatement("DELETE FROM burns " +
							"WHERE edge_id=?");
					stmt.setInt(1, tid);
					stmt.executeUpdate();
					System.out.println("Edge #" + tid + " deleted");
			        return true;
				} else {
					System.out.println("Edge #" + tid + " not found in database");
					return false; // edge not found
				}
			} else {
				System.out.println("Edge #" + tid + " not found in library");
				return false; // edge does not exist
			}
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#deleteElement(int)
	 */
	public boolean deleteElement(int tid) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if(elementPreviewLibrary.remove(loadElementPreview(tid))) {
				stmt = connection.prepareStatement("DELETE FROM elements " +
						"WHERE id=? LIMIT 1");
				stmt.setInt(1, tid);
				int i = stmt.executeUpdate();
				if(i==1) {
					stmt = connection.prepareStatement("DELETE FROM demands " +
							"WHERE container_id=?");
					stmt.setInt(1, tid);
					stmt.executeUpdate();
					stmt = connection.prepareStatement("DELETE FROM parts " +
							"WHERE element_id=?");
					stmt.setInt(1, tid);
					stmt.executeUpdate();
					stmt = connection.prepareStatement("SELECT id FROM states " +
							"WHERE element_id=?");
					stmt.setInt(1, tid);
					rs = stmt.executeQuery();
					while(rs.next()) {
						deleteState(rs.getInt("id"));
					}
					System.out.println("Element #" + tid + " deleted");
					return true;
				} else {
					System.out.println("Element #" + tid + " not found in database");
					return false; // element not found
				}
			} else {
				System.out.println("Element #" + tid + " not found in library");
				return false; // element does not exist
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Deletes a state triggers the deletion of any associated demand models.
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void deleteState(int tid) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("DELETE FROM states " +
					"WHERE id=? LIMIT 1");
			stmt.setInt(1, tid);
			int i = stmt.executeUpdate();
			if(i==1) {
				stmt = connection.prepareStatement("SELECT id FROM models " +
						"WHERE state_id=?");
				stmt.setInt(1, tid);
				rs = stmt.executeQuery();
				while(rs.next()) {
					deleteModel(rs.getInt("id"));
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Deletes a demand model and triggers the deletion of any associated
	 * demands.
	 * 
	 * @param tid the primary key (type ID)
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void deleteModel(int tid) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("DELETE FROM models " +
					"WHERE id=? LIMIT 1");
			stmt.setInt(1, tid);
			stmt.executeUpdate();
			stmt = connection.prepareStatement("DELETE FROM demands " +
					"WHERE model_id=?");
			stmt.setInt(1, tid);
			stmt.executeUpdate();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#deleteNode(int)
	 */
	public boolean deleteNode(int tid) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT COUNT(*) AS conflicts " +
					"FROM edges " +
					"WHERE origin_id=? OR destination_id=?");
			stmt.setInt(1, tid);
			stmt.setInt(2, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt("conflicts")>0) 
					return false; // node is used in edges
			}
			if(nodeLibrary.remove(loadNode(tid))) {
				stmt = connection.prepareStatement("DELETE FROM nodes " +
						"WHERE id=? LIMIT 1");
				stmt.setInt(1, tid);
				int i = stmt.executeUpdate();
				if(i > 0) {
					System.out.println("Node #" + tid + " deleted");
			        return true;
				} else {
					System.out.println("Node #" + tid + " not found in database");
					return false; // node not found
				}
			} else {
				System.out.println("Node #" + tid + " not found in library");
				return false; // node does not exist
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#deleteResource(int)
	 */
	public boolean deleteResource(int tid) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT COUNT(*) AS conflicts " +
					"FROM elements " +
					"WHERE oms_id=? OR rcs_id=? OR fuel_id=?");
			stmt.setInt(1, tid);
			stmt.setInt(2, tid);
			stmt.setInt(3, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt("conflicts")>0) 
					return false; // resource is used in elements
			}
			stmt = connection.prepareStatement("SELECT COUNT(*) AS conflicts " +
					"FROM parts " +
					"WHERE resource_id=?");
			stmt.setInt(1, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt("conflicts")>0) 
					return false; // resource is used in parts
			}
			/* TODO: add back in when supporting ISRU or BOILOFF models
			stmt = connection.prepareStatement("SELECT COUNT(*) AS conflicts " +
					"FROM models " +
					"WHERE resource_id=?");
			stmt.setInt(1, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt("conflicts")>0) 
					return false; // resource is used in demand models
			}
			*/
			stmt = connection.prepareStatement("SELECT COUNT(*) AS conflicts " +
					"FROM demands " +
					"WHERE resource_id=?");
			stmt.setInt(1, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				if(rs.getInt("conflicts")>0) 
					return false; // resource is used in demands
			}
			if(resourceTypeLibrary.remove(loadResource(tid))) {
				stmt = connection.prepareStatement("DELETE FROM resources " +
						"WHERE id=? LIMIT 1");
				stmt.setInt(1, tid);
				int i = stmt.executeUpdate();
				if(i > 0) {
			        System.out.println("Resource #" + tid + " deleted");        
					return true;
				} else {
					System.out.println("Resource #" + tid + " not found in database");
					return false; // resource not found
				}
			} else {
				System.out.println("Resource #" + tid + " not found in library");
				return false; // resource does not exist
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#getDataSourceType()
	 */
	public DataSourceType getDataSourceType() {
		return DataSourceType.SQL_DB;
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#getName()
	 */
	public String getName() {
		if(host==null || host.length()==0) return "SpaceNet 2.5 (MySQL)";
		return database + " (" + host + ")";
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#loadEdgeLibrary()
	 */
	public void loadEdgeLibrary() throws SQLException {
		Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
		for(Edge edge : getEdgeLibrary()) {
			// mark all edges as unfound
			libraryContents.put(edge.getTid(), false);
		}
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM edges");
			rs = stmt.executeQuery();
			while(rs.next()) {
				try {
					Edge e = loadEdge(rs.getInt("id"));
					String edgeType = rs.getString("type").toLowerCase();
		    		if(e==null || !e.getEdgeType().getName().toLowerCase().contains(edgeType)) {
		    			edgeLibrary.remove(e);
						if(e != null) libraryContents.remove(e.getTid());
		    			// create new edge and add to library
		    			if(EdgeType.SURFACE.getName().toLowerCase().contains(edgeType)) {
			    			e = new SurfaceEdge();
			    		} else if(EdgeType.SPACE.getName().toLowerCase().contains(edgeType)) {
				    		e = new SpaceEdge();
			    		} else if(EdgeType.FLIGHT.getName().toLowerCase().contains(edgeType)) {
			    			e = new FlightEdge();
			    		} else {
			    			throw new Exception("Unknown Edge Type");
			    		}
		    			edgeLibrary.add(e);
		    		} else {
						// mark edge as found
						libraryContents.put(e.getTid(), true);
					}
		    		e.setTid(rs.getInt("id"));
					e.setName(rs.getString("name"));
					e.setOrigin(loadNode(rs.getInt("origin_id")));
					e.setDestination(loadNode(rs.getInt("destination_id")));
					e.setDescription(rs.getString("description"));
					if(EdgeType.SURFACE.getName().toLowerCase().contains(edgeType)) {
		    			e = new SurfaceEdge();
						((SurfaceEdge)e).setDistance(rs.getDouble("distance"));
		    		} else if(EdgeType.SPACE.getName().toLowerCase().contains(edgeType)) {
		    			e = new SpaceEdge();
						((SpaceEdge)e).setDuration(rs.getDouble("duration"));
		    			loadBurns((SpaceEdge)e);
		    		} else if(EdgeType.FLIGHT.getName().toLowerCase().contains(edgeType)) {
		    			e = new FlightEdge();
						((FlightEdge)e).setDuration(rs.getDouble("duration"));
						((FlightEdge)e).setMaxCrewSize(rs.getInt("max_crew"));
						((FlightEdge)e).setMaxCargoMass(rs.getDouble("max_cargo"));
		    		} else {
		    			throw new Exception("Unknown Edge Type");
		    		}
				} catch(Exception ex) {
					// error reading edge
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
		for(Integer i : libraryContents.keySet()) {
			if(!libraryContents.get(i)) {
				// remove all unfound edges
				getEdgeLibrary().remove(loadEdge(i));
			}
		}
	}
	
	/**
	 * Loads burns for a specific space edge.
	 * 
	 * @param edge the edge
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void loadBurns(SpaceEdge edge) throws SQLException {
		edge.getBurns().clear();
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM burns " +
					"WHERE burns.edge_id=?");
			stmt.setInt(1, edge.getTid());
			rs = stmt.executeQuery();
			while(rs.next()) {
				try {
					Burn b = new Burn();
					b.setTid(rs.getInt("id"));
					b.setTime(rs.getDouble("time"));
					b.setBurnType(BurnType.getInstance(rs.getString("type")));
					b.setDeltaV(rs.getDouble("delta_v"));
					edge.getBurns().add(rs.getInt("order"), b);
				} catch(Exception ex) {
					// error loading burn
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#loadElement(int)
	 */
	public I_Element loadElement(int tid) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM elements " +
					"WHERE id=?");
			stmt.setInt(1, tid);
			rs = stmt.executeQuery();
			if(rs.next()) {
				try {
					I_Element e = null;
					String elementType = rs.getString("type").toLowerCase();
					if(ElementType.ELEMENT.getName().toLowerCase().contains(elementType)) {
	    				e = new Element();
	    			} else if(ElementType.RESOURCE_CONTAINER.getName().toLowerCase().contains(elementType)) {
	    				e = new ResourceContainer();
	    				((ResourceContainer)e).setMaxCargoMass(rs.getDouble("cargo_mass"));
		    			((ResourceContainer)e).setMaxCargoVolume(rs.getDouble("cargo_volume"));
		    			((ResourceContainer)e).setCargoEnvironment(Environment.getInstance(rs.getString("cargo_environment")));
	    			} else if(ElementType.CREW_MEMBER.getName().toLowerCase().contains(elementType)) {
	    				e = new CrewMember();
	    				((CrewMember)e).setAvailableTimeFraction(rs.getDouble("active_fraction"));
	    			} else if(ElementType.CARRIER.getName().toLowerCase().contains(elementType)) {
	    				e = new Carrier();
	    				((Carrier)e).setMaxCrewSize(rs.getInt("max_crew"));
		    			((Carrier)e).setMaxCargoMass(rs.getDouble("cargo_mass"));
		    			((Carrier)e).setMaxCargoVolume(rs.getDouble("cargo_volume"));
		    			((Carrier)e).setCargoEnvironment(Environment.getInstance(rs.getString("cargo_environment")));
	    			} else if(ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(elementType)) {
	    				e = new SurfaceVehicle();
	    				((SurfaceVehicle)e).setMaxCrewSize(rs.getInt("max_crew"));
		    			((SurfaceVehicle)e).setMaxCargoMass(rs.getDouble("cargo_mass"));
		    			((SurfaceVehicle)e).setMaxCargoVolume(rs.getDouble("cargo_volume"));
		    			((SurfaceVehicle)e).setCargoEnvironment(Environment.getInstance(rs.getString("cargo_environment")));
		    			((SurfaceVehicle)e).setMaxSpeed(rs.getDouble("max_speed"));
		    			((SurfaceVehicle)e).getFuelTank().setMaxAmount(rs.getDouble("max_fuel"));
		    			((SurfaceVehicle)e).getFuelTank().setAmount(rs.getDouble("max_fuel"));
		    			((SurfaceVehicle)e).getFuelTank().setResource(loadResource(rs.getInt("fuel_id")));
	    			} else if(ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(elementType)) {
	    				e = new PropulsiveVehicle();
	    				((PropulsiveVehicle)e).setMaxCrewSize(rs.getInt("max_crew"));
		    			((PropulsiveVehicle)e).setMaxCargoMass(rs.getDouble("cargo_mass"));
		    			((PropulsiveVehicle)e).setMaxCargoVolume(rs.getDouble("cargo_volume"));
		    			((PropulsiveVehicle)e).setCargoEnvironment(Environment.getInstance(rs.getString("cargo_environment")));
		    			((PropulsiveVehicle)e).setOmsIsp(rs.getDouble("oms_isp"));
		    			((PropulsiveVehicle)e).setRcsIsp(rs.getDouble("rcs_isp"));
	    				if(((PropulsiveVehicle)e).getOmsIsp() > 0) {
	    					((PropulsiveVehicle)e).getOmsFuelTank().setMaxAmount(rs.getDouble("max_oms"));
	    					((PropulsiveVehicle)e).getOmsFuelTank().setAmount(rs.getDouble("max_oms"));
	    					((PropulsiveVehicle)e).getOmsFuelTank().setResource(loadResource(rs.getInt("oms_id")));
	    				} else {
	    					((PropulsiveVehicle)e).setOmsFuelTank(null);
	    				}
	    				if(((PropulsiveVehicle)e).getRcsIsp() > 0 && rs.getDouble("max_rcs") > 0) {
	    					((PropulsiveVehicle)e).getRcsFuelTank().setMaxAmount(rs.getDouble("max_rcs"));
	    					((PropulsiveVehicle)e).getRcsFuelTank().setAmount(rs.getDouble("max_rcs"));
	    					((PropulsiveVehicle)e).getRcsFuelTank().setResource(loadResource(rs.getInt("rcs_id")));
	    				} else if(((PropulsiveVehicle)e).getRcsIsp() > 0) {
	    					((PropulsiveVehicle)e).getContents().remove(((PropulsiveVehicle)e).getRcsFuelTank());
	    					((PropulsiveVehicle)e).setRcsFuelTank(((PropulsiveVehicle)e).getOmsFuelTank());
	    				} else {
	    					((PropulsiveVehicle)e).setRcsFuelTank(null);
	    				}
	    			} else {
		    			throw new Exception("Unknown Element Type");
		    		}
					e.setTid(rs.getInt("id"));
					e.setName(rs.getString("name"));
					e.setClassOfSupply(ClassOfSupply.getInstance(rs.getInt("cos")));
	    			e.setEnvironment(Environment.getInstance(rs.getString("environment")));
	    			e.setAccommodationMass(rs.getDouble("accommodation_mass"));
					e.setMass(rs.getDouble("mass"));
					e.setVolume(rs.getDouble("volume"));
					e.setDescription(rs.getString("description"));
					if(e.getElementType()==ElementType.RESOURCE_CONTAINER) {
						((I_ResourceContainer)e).getContents().clear();
						stmt = connection.prepareStatement("SELECT * FROM demands " +
								"WHERE container_id=?");
						stmt.setInt(1, e.getTid());
						rs = stmt.executeQuery();
						while(rs.next()) {
							((ResourceContainer)e).getContents().put(
									loadResource(rs.getInt("resource_id")), 
									rs.getDouble("amount"));
						}
					}
					e.getParts().clear();
					stmt = connection.prepareStatement("SELECT * FROM parts " +
							"WHERE element_id=?");
					stmt.setInt(1, e.getTid());
					rs = stmt.executeQuery();
					while(rs.next()) {
						PartApplication partApp = new PartApplication();
						partApp.setTid(rs.getInt("id"));
						partApp.setPart((Item)loadResource(rs.getInt("resource_id")));
						partApp.setQuantity(rs.getInt("quantity"));
						partApp.setDutyCycle(rs.getDouble("duty_cycle"));
						partApp.setMeanTimeToFailure(rs.getDouble("mttf"));
						partApp.setMeanTimeToRepair(rs.getDouble("mttr"));
						partApp.setMassToRepair(rs.getDouble("repair_mass"));
						e.getParts().add(partApp);
					}
					e.getStates().clear();
					stmt = connection.prepareStatement("SELECT * FROM states " +
							"WHERE element_id=?");
					stmt.setInt(1, e.getTid());
					rs = stmt.executeQuery();
					while(rs.next()) {
						State state = new State();
						state.setTid(rs.getInt("id"));
						state.setName(rs.getString("name"));
						state.setStateType(StateType.getInstance(rs.getString("type")));
						loadModels(e, state);
						e.getStates().add(state);
						if(rs.getInt("initial") == 1) 
							e.setCurrentState(state);
					}
					return e;
				} catch(Exception ex) {
					// error reading element
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
		return null;
	}
	
	/**
	 * Loads the demand models for a specific state.
	 * 
	 * @param element the element
	 * @param state the state
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void loadModels(I_Element element, I_State state) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM models " +
					"WHERE state_id=?");
			stmt.setInt(1, state.getTid());
			rs = stmt.executeQuery();
			while(rs.next()) {
				try {
					String modelType = rs.getString("type").toLowerCase();
					if(DemandModelType.TIMED_IMPULSE.getName().toLowerCase().contains(modelType)) {
						TimedImpulseDemandModel model = new TimedImpulseDemandModel();
						model.setTid(rs.getInt("id"));
						model.setName(rs.getString("name"));
						PreparedStatement s = connection.prepareStatement("SELECT * FROM demands " +
								"WHERE model_id=?");
						s.setInt(1, model.getTid());
						ResultSet r = s.executeQuery();
						while(r.next()) {
							Demand demand = new Demand();
							demand.setResource(loadResource(r.getInt("resource_id")));
							demand.setAmount(r.getDouble("amount"));
							model.getDemands().add(demand);
						}
						state.getDemandModels().add(model);
					} else if(DemandModelType.RATED.getName().toLowerCase().contains(modelType)) {
						RatedDemandModel model = new RatedDemandModel();
						model.setTid(rs.getInt("id"));
						model.setName(rs.getString("name"));
						PreparedStatement s = connection.prepareStatement("SELECT * FROM demands " +
								"WHERE model_id=?");
						s.setInt(1, model.getTid());
						ResultSet r = s.executeQuery();
						while(r.next()) {
							Demand demand = new Demand();
							demand.setResource(loadResource(r.getInt("resource_id")));
							demand.setAmount(r.getDouble("amount"));
							model.getDemandRates().add(demand);
						}
						state.getDemandModels().add(model);
					} else if(DemandModelType.SPARING_BY_MASS.getName().toLowerCase().contains(modelType)) {
						SparingByMassDemandModel model = new SparingByMassDemandModel(element);
						model.setTid(rs.getInt("id"));
						model.setName(rs.getString("name"));
						model.setElement(element);
						model.setPartsListEnabled(rs.getInt("parts_list")==1);
						model.setUnpressurizedSparesRate(rs.getDouble("unpress_rate"));
						model.setPressurizedSparesRate(rs.getDouble("press_rate"));
						state.getDemandModels().add(model);
					} else {
						throw new Exception("Unknown Demand Model Type");
					}
				} catch(Exception ex) {
					// error reading model
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Loads the element library.
	 * 
	 * @throws SQLException the SQL exception
	 */
	public void loadElementLibrary() throws SQLException {
		getElementPreviewLibrary().clear();
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM elements");
			rs = stmt.executeQuery();
			while(rs.next()) {
				try {
					ElementPreview e = null;
					String elementType = rs.getString("type").toLowerCase();
					String name = rs.getString("name");
					int tid = rs.getInt("id");
	    			// create new element preview and add to library
					// TODO: support element icon types
	    			if(ElementType.ELEMENT.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.ELEMENT, null);
	    			} else if(ElementType.RESOURCE_CONTAINER.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.RESOURCE_CONTAINER, null);
	    			} else if(ElementType.CREW_MEMBER.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.CREW_MEMBER, null);
	    			} else if(ElementType.CARRIER.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.CARRIER, null);
	    			} else if(ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.SURFACE_VEHICLE, null);
	    			} else if(ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(elementType)) {
	    				e = new ElementPreview(tid, name, ElementType.PROPULSIVE_VEHICLE, null);
	    			} else {
		    			throw new Exception("Unknown Element Type");
		    		}
	    			elementPreviewLibrary.add(e);
				} catch(Exception ex) {
					// error reading resource
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/**
	 * Loads the node library.
	 * 
	 * @throws SQLException the SQL exception
	 */
	public void loadNodeLibrary() throws SQLException {
		Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
		for(Node node : getNodeLibrary()) {
			// mark all nodes as unfound
			libraryContents.put(node.getTid(), false);
		}
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM nodes");;
			rs = stmt.executeQuery();;
			while(rs.next()) {
				try {
					Node n = loadNode(rs.getInt("id"));
					String nodeType = rs.getString("type").toLowerCase();
		    		if(n==null || !n.getNodeType().getName().toLowerCase().contains(nodeType)) {
		    			nodeLibrary.remove(n);
						if(n!=null) libraryContents.remove(n.getTid());
		    			// create new node and add to library
		    			if(NodeType.SURFACE.getName().toLowerCase().contains(nodeType)) {
			    			n = new SurfaceNode();
			    		} else if(NodeType.ORBITAL.getName().toLowerCase().contains(nodeType)) {
				    		n = new OrbitalNode();
			    		} else if(NodeType.LAGRANGE.getName().toLowerCase().contains(nodeType)) {
			    			n = new LagrangeNode();
			    		} else {
			    			throw new Exception("Unknown Node Type");
			    		}
		    			nodeLibrary.add(n);
		    		} else {
						// mark node as found
						libraryContents.put(n.getTid(), true);
					}
					n.setTid(rs.getInt("id"));
					n.setName(rs.getString("name"));
					n.setBody(Body.getInstance(rs.getString("body_1")));
					n.setDescription(rs.getString("description"));
		    		if(NodeType.SURFACE.getName().toLowerCase().contains(nodeType)) {
		    			n = new SurfaceNode();
						((SurfaceNode)n).setLatitude(rs.getDouble("latitude"));
						((SurfaceNode)n).setLongitude(rs.getDouble("longitude"));
		    		} else if(NodeType.ORBITAL.getName().toLowerCase().contains(nodeType)) {
		    			n = new OrbitalNode();
						((OrbitalNode)n).setApoapsis(rs.getDouble("apoapsis"));
						((OrbitalNode)n).setPeriapsis(rs.getDouble("periapsis"));
						((OrbitalNode)n).setInclination(rs.getDouble("inclination"));
		    		} else if(NodeType.LAGRANGE.getName().toLowerCase().contains(nodeType)) {
		    			n = new LagrangeNode();
						((LagrangeNode)n).setMinorBody(Body.getInstance(rs.getString("body_2")));
						((LagrangeNode)n).setNumber(rs.getInt("lp_number"));
		    		} else {
		    			throw new Exception("Unknown Node Type");
		    		}
				} catch(Exception ex) {
					// error reading node
					ex.printStackTrace();
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
		for(Integer i : libraryContents.keySet()) {
			if(!libraryContents.get(i)) {
				// remove all unfound nodes
				getNodeLibrary().remove(loadNode(i));
			}
		}
	}
	
	/**
	 * Loads the resource library.
	 * 
	 * @throws SQLException the SQL exception
	 */
	public void loadResourceLibrary() throws SQLException {
		Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
		for(I_Resource resource : getResourceLibrary()) {
			// mark all resources as not found
			libraryContents.put(resource.getTid(), false);
		}
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM resources");
			rs = stmt.executeQuery();
			while(rs.next()) {
				try {
					I_Resource r = loadResource(rs.getInt("id"));
		    		String resourceType = rs.getString("type").toLowerCase();
		    		if(r==null || !r.getResourceType().getName().toLowerCase().contains(resourceType)) {
		    			resourceTypeLibrary.remove(r);
						if(r!=null) libraryContents.remove(r.getTid());
		    			// create new resource and add to library
		    			if(ResourceType.RESOURCE.getName().toLowerCase().contains(resourceType)) {
			    			r = new Resource();
			    		} else if(ResourceType.ITEM.getName().toLowerCase().contains(resourceType)) {
				    		r = new Item();
			    		} else {
			    			throw new Exception("Unknown Resource Type");
			    		}
		    			resourceTypeLibrary.add(r);
		    		} else {
						// mark resource as found
						libraryContents.put(r.getTid(), true);
					}
		    		r.setTid(rs.getInt("id"));
		    		r.setName(rs.getString("name"));
		    		r.setClassOfSupply(ClassOfSupply.getInstance(rs.getInt("cos")));
		    		r.setUnits(rs.getString("units"));
		    		r.setUnitMass(rs.getDouble("unit_mass"));
		    		r.setUnitVolume(rs.getDouble("unit_volume"));
		    		r.setPackingFactor(rs.getDouble("packing_factor"));
	    			r.setEnvironment(Environment.getInstance(rs.getString("environment")));
		    		r.setDescription(rs.getString("description"));
				} catch(Exception ex) {
					// error reading resource
					ex.printStackTrace();
				}
			}
			for(Integer i : libraryContents.keySet()) {
				if(!libraryContents.get(i)) {
					// remove all unfound resources
					getResourceLibrary().remove(loadResource(i));
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#saveEdge(edu.mit.spacenet.domain.network.edge.Edge)
	 */
	public void saveEdge(Edge edge) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("UPDATE edges " +
				"SET type=?, name=?, origin_id=?, destination_id=?, " +
				"duration=?, distance=?, max_crew=?, max_cargo=?, " +
				"description=?" +
				"WHERE id=?");
			stmt.setString(1, edge.getEdgeType().getName());
			stmt.setString(2, edge.getName());
			stmt.setInt(3, edge.getOrigin().getTid());
			stmt.setInt(4, edge.getDestination().getTid());
			if(edge.getEdgeType()==EdgeType.SPACE) {
				stmt.setDouble(5, ((SpaceEdge)edge).getDuration());
			} else {
				if(edge.getEdgeType()!=EdgeType.FLIGHT) 
					stmt.setNull(5, Types.DOUBLE); // don't overwrite value
			}
			if(edge.getEdgeType()==EdgeType.SURFACE) {
				stmt.setDouble(6, ((SurfaceEdge)edge).getDistance());
			} else {
				stmt.setNull(6, Types.DOUBLE);
			}
			if(edge.getEdgeType()==EdgeType.FLIGHT) {
				stmt.setDouble(5, ((FlightEdge)edge).getDuration());
				stmt.setInt(7, ((FlightEdge)edge).getMaxCrewSize());
				stmt.setDouble(8, ((FlightEdge)edge).getMaxCargoMass());
			} else {
				if(edge.getEdgeType()!=EdgeType.SPACE) 
					stmt.setNull(5, Types.DOUBLE); // don't overwrite value
				stmt.setNull(7, Types.INTEGER);
				stmt.setNull(8, Types.DOUBLE);
			}
			stmt.setString(9, edge.getDescription());
			stmt.setInt(10, edge.getTid());
			int i = stmt.executeUpdate();
			if(i == 0) {
				// insert new edge
				stmt = connection.prepareStatement("INSERT INTO edges " +
						"(type, name, origin_id, destination_id, duration, " +
						"distance, max_crew, max_cargo, description) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, edge.getEdgeType().getName());
				stmt.setString(2, edge.getName());
				stmt.setInt(3, edge.getOrigin().getTid());
				stmt.setInt(4, edge.getDestination().getTid());
				if(edge.getEdgeType()==EdgeType.SPACE) {
					stmt.setDouble(5, ((SpaceEdge)edge).getDuration());
				} else {
					if(edge.getEdgeType()!=EdgeType.FLIGHT) 
						stmt.setNull(5, Types.DOUBLE); // don't overwrite value
				}
				if(edge.getEdgeType()==EdgeType.SURFACE) {
					stmt.setDouble(6, ((SurfaceEdge)edge).getDistance());
				} else {
					stmt.setNull(6, Types.DOUBLE);
				}
				if(edge.getEdgeType()==EdgeType.FLIGHT) {
					stmt.setDouble(5, ((FlightEdge)edge).getDuration());
					stmt.setInt(7, ((FlightEdge)edge).getMaxCrewSize());
					stmt.setDouble(8, ((FlightEdge)edge).getMaxCargoMass());
				} else {
					if(edge.getEdgeType()!=EdgeType.SPACE) 
						stmt.setNull(5, Types.DOUBLE); // don't overwrite value
					stmt.setNull(7, Types.INTEGER);
					stmt.setNull(8, Types.DOUBLE);
				}
				stmt.setString(9, edge.getDescription());
				stmt.executeUpdate();
				stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() AS id");
				rs = stmt.executeQuery();
				if(rs.next()) {
					edge.setTid(rs.getInt("id"));
					getEdgeLibrary().add(edge);
				}
			}
			if(edge.getEdgeType()==EdgeType.SPACE) saveBurns(((SpaceEdge)edge));
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves the burns associated with an edge. Removed burns will be deleted
	 * from the data source, changed burns will be updated, and added burns
	 * will be assigned a new TID and inserted.
	 * 
	 * @param edge the edge
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveBurns(SpaceEdge edge) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// delete unused burns
			if(edge.getBurns().size()>0) {
				String burnList = "";
				for(Burn burn : edge.getBurns()) burnList += burn.getTid() + ", ";
				burnList = burnList.substring(0, burnList.length()-2);
				stmt = connection.prepareStatement("DELETE FROM burns " +
						"WHERE edge_id = ? " +
						"AND id NOT IN (" + burnList + ")");
				stmt.setInt(1, edge.getTid());
				stmt.executeUpdate();
			}
			
			// update/insert burns
			for(Burn burn : edge.getBurns()) {
				stmt = connection.prepareStatement("UPDATE burns " +
						"SET `time`=?, `order`=?, type=?, delta_v=? " +
						"WHERE id=? AND edge_id=?");
				stmt.setDouble(1, burn.getTime());
				stmt.setInt(2, edge.getBurns().indexOf(burn));
				stmt.setString(3, burn.getBurnType().getName());
				stmt.setDouble(4, burn.getDeltaV());
				stmt.setInt(5, burn.getTid());
				stmt.setInt(6, edge.getTid());
				int i = stmt.executeUpdate();
				if(i == 0) {
					// insert new resource
					stmt = connection.prepareStatement("INSERT INTO burns " +
							"(edge_id, `time`, `order`, type, delta_v) " +
							"VALUES (?, ?, ?, ?, ?)");
					stmt.setInt(1, edge.getTid());
					stmt.setDouble(2, burn.getTime());
					stmt.setInt(3, edge.getBurns().indexOf(burn));
					stmt.setString(4, burn.getBurnType().getName());
					stmt.setDouble(5, burn.getDeltaV());
					stmt.executeUpdate();
					stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() AS id");
					rs = stmt.executeQuery();
					if(rs.next()) {
						burn.setTid(rs.getInt("id"));
					}
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#saveElement(edu.mit.spacenet.domain.element.I_Element)
	 */
	public void saveElement(I_Element element) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("UPDATE elements " +
					"SET type=?, name=?, cos=?, environment=?, " +
					"accommodation_mass=?, mass=?, volume=?, max_crew=?, " +
					"cargo_mass=?, cargo_volume=?, cargo_environment=?," +
					"active_fraction=?, oms_isp=?, max_oms=?, oms_id=?, " +
					"rcs_isp=?, max_rcs=?, rcs_id=?, max_speed=?, max_fuel=?," +
					"fuel_id=?, description=?" +
					"WHERE id=? ");
			stmt.setString(1, element.getElementType().getName());
			stmt.setString(2, element.getName());
			stmt.setInt(3, element.getClassOfSupply().getId());
			stmt.setString(4, element.getEnvironment().getName());
			stmt.setDouble(5, element.getAccommodationMass());
			stmt.setDouble(6, element.getMass());
			stmt.setDouble(7, element.getVolume());
			if(element.getElementType()==ElementType.ELEMENT) {
				stmt.setNull(8, Types.INTEGER);
				stmt.setNull(9, Types.DOUBLE);
				stmt.setNull(10, Types.DOUBLE);
				stmt.setNull(11, Types.VARCHAR);
				stmt.setNull(12, Types.DOUBLE);
				stmt.setNull(13, Types.DOUBLE);
				stmt.setNull(14, Types.DOUBLE);
				stmt.setNull(15, Types.INTEGER);
				stmt.setNull(16, Types.DOUBLE);
				stmt.setNull(17, Types.DOUBLE);
				stmt.setNull(18, Types.INTEGER);
				stmt.setNull(19, Types.DOUBLE);
				stmt.setNull(20, Types.DOUBLE);
				stmt.setNull(21, Types.INTEGER);
			} if(element.getElementType()==ElementType.RESOURCE_CONTAINER) {
				stmt.setNull(8, Types.INTEGER);
				stmt.setDouble(9, ((ResourceContainer)element).getMaxCargoMass());
				stmt.setDouble(10, ((ResourceContainer)element).getMaxCargoVolume());
				stmt.setString(11, ((ResourceContainer)element).getCargoEnvironment().getName());
				stmt.setNull(12, Types.DOUBLE);
				stmt.setNull(13, Types.DOUBLE);
				stmt.setNull(14, Types.DOUBLE);
				stmt.setNull(15, Types.INTEGER);
				stmt.setNull(16, Types.DOUBLE);
				stmt.setNull(17, Types.DOUBLE);
				stmt.setNull(18, Types.INTEGER);
				stmt.setNull(19, Types.DOUBLE);
				stmt.setNull(20, Types.DOUBLE);
				stmt.setNull(21, Types.INTEGER);
	    		saveContents((ResourceContainer)element);
			} else if(element.getElementType()==ElementType.CREW_MEMBER) {
				stmt.setNull(8, Types.INTEGER);
				stmt.setNull(9, Types.DOUBLE);
				stmt.setNull(10, Types.DOUBLE);
				stmt.setNull(11, Types.VARCHAR);
				stmt.setDouble(12, ((CrewMember)element).getAvailableTimeFraction());
				stmt.setNull(13, Types.DOUBLE);
				stmt.setNull(14, Types.DOUBLE);
				stmt.setNull(15, Types.INTEGER);
				stmt.setNull(16, Types.DOUBLE);
				stmt.setNull(17, Types.DOUBLE);
				stmt.setNull(18, Types.INTEGER);
				stmt.setNull(19, Types.DOUBLE);
				stmt.setNull(20, Types.DOUBLE);
				stmt.setNull(21, Types.INTEGER);
			} else if(element.getElementType()==ElementType.CARRIER) {
				stmt.setInt(8, ((Carrier)element).getMaxCrewSize());
				stmt.setDouble(9, ((Carrier)element).getMaxCargoMass());
				stmt.setDouble(10, ((Carrier)element).getMaxCargoVolume());
				stmt.setString(11, ((Carrier)element).getCargoEnvironment().getName());
				stmt.setNull(12, Types.DOUBLE);
				stmt.setNull(13, Types.DOUBLE);
				stmt.setNull(14, Types.DOUBLE);
				stmt.setNull(15, Types.INTEGER);
				stmt.setNull(16, Types.DOUBLE);
				stmt.setNull(17, Types.DOUBLE);
				stmt.setNull(18, Types.INTEGER);
				stmt.setNull(19, Types.DOUBLE);
				stmt.setNull(20, Types.DOUBLE);
				stmt.setNull(21, Types.INTEGER);
			} else if(element.getElementType()==ElementType.PROPULSIVE_VEHICLE) {
				stmt.setInt(8, ((PropulsiveVehicle)element).getMaxCrewSize());
				stmt.setDouble(9, ((PropulsiveVehicle)element).getMaxCargoMass());
				stmt.setDouble(10, ((PropulsiveVehicle)element).getMaxCargoVolume());
				stmt.setString(11, ((PropulsiveVehicle)element).getCargoEnvironment().getName());
				stmt.setNull(12, Types.DOUBLE);
				if(((PropulsiveVehicle)element).getOmsIsp() > 0) {
					stmt.setDouble(13, ((PropulsiveVehicle)element).getOmsIsp());
					stmt.setDouble(14, ((PropulsiveVehicle)element).getOmsFuelTank().getMaxAmount());
					stmt.setInt(15, ((PropulsiveVehicle)element).getOmsFuelTank().getResource().getTid());
				} else {
					stmt.setDouble(13, 0);
					stmt.setDouble(14, 0);
					stmt.setNull(15, Types.INTEGER);
				}
				if(((PropulsiveVehicle)element).getRcsIsp() > 0) {
					stmt.setDouble(16, ((PropulsiveVehicle)element).getRcsIsp());
					if(((PropulsiveVehicle)element).getRcsFuelTank()==null ||
							((PropulsiveVehicle)element).getRcsFuelTank()==((PropulsiveVehicle)element).getOmsFuelTank()) {
						// shared OMS / RCS tanks
						stmt.setDouble(17, 0);
						stmt.setNull(18, Types.INTEGER);
					} else {
						stmt.setDouble(17, ((PropulsiveVehicle)element).getRcsFuelTank().getMaxAmount());
						stmt.setInt(18, ((PropulsiveVehicle)element).getRcsFuelTank().getResource().getTid());
					}
				} else {
					stmt.setDouble(16, 0);
					stmt.setDouble(17, 0);
					stmt.setNull(18, Types.INTEGER);
				}
				stmt.setNull(19, Types.DOUBLE);
				stmt.setNull(20, Types.DOUBLE);
				stmt.setNull(21, Types.INTEGER);
			} else if(element.getElementType()==ElementType.SURFACE_VEHICLE) {
				stmt.setInt(8, ((SurfaceVehicle)element).getMaxCrewSize());
				stmt.setDouble(9, ((SurfaceVehicle)element).getMaxCargoMass());
				stmt.setDouble(10, ((SurfaceVehicle)element).getMaxCargoVolume());
				stmt.setString(11, ((SurfaceVehicle)element).getCargoEnvironment().getName());
				stmt.setNull(12, Types.DOUBLE);
				stmt.setNull(13, Types.DOUBLE);
				stmt.setNull(14, Types.DOUBLE);
				stmt.setNull(15, Types.INTEGER);
				stmt.setNull(16, Types.DOUBLE);
				stmt.setNull(17, Types.DOUBLE);
				stmt.setNull(18, Types.INTEGER);
				stmt.setDouble(19, ((SurfaceVehicle)element).getMaxSpeed());
				stmt.setDouble(20, ((SurfaceVehicle)element).getFuelTank().getMaxAmount());
				stmt.setInt(21, ((SurfaceVehicle)element).getFuelTank().getResource().getTid());
			}
			stmt.setString(22, element.getDescription());
			stmt.setInt(23, element.getTid());
			int i = stmt.executeUpdate();
			if(i==0) {
				// create new element
				stmt = connection.prepareStatement("INSERT INTO elements " +
						"(type, name, cos, environment, accommodation_mass, " +
						"mass, volume, max_crew, cargo_mass, cargo_volume, " +
						"cargo_environment, active_fraction, oms_isp, max_oms, " +
						"oms_id, rcs_isp, max_rcs, rcs_id, max_speed, max_fuel, " +
						"fuel_id, description) " +
						"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, element.getElementType().getName());
				stmt.setString(2, element.getName());
				stmt.setInt(3, element.getClassOfSupply().getId());
				stmt.setString(4, element.getEnvironment().getName());
				stmt.setDouble(5, element.getAccommodationMass());
				stmt.setDouble(6, element.getMass());
				stmt.setDouble(7, element.getVolume());
				// TODO: support element icon types
				if(element.getElementType()==ElementType.ELEMENT) {
					stmt.setNull(8, Types.INTEGER);
					stmt.setNull(9, Types.DOUBLE);
					stmt.setNull(10, Types.DOUBLE);
					stmt.setNull(11, Types.VARCHAR);
					stmt.setNull(12, Types.DOUBLE);
					stmt.setNull(13, Types.DOUBLE);
					stmt.setNull(14, Types.DOUBLE);
					stmt.setNull(15, Types.INTEGER);
					stmt.setNull(16, Types.DOUBLE);
					stmt.setNull(17, Types.DOUBLE);
					stmt.setNull(18, Types.INTEGER);
					stmt.setNull(19, Types.DOUBLE);
					stmt.setNull(20, Types.DOUBLE);
					stmt.setNull(21, Types.INTEGER);
				} else if(element.getElementType()==ElementType.RESOURCE_CONTAINER) {
					stmt.setNull(8, Types.INTEGER);
					stmt.setDouble(9, ((ResourceContainer)element).getMaxCargoMass());
					stmt.setDouble(10, ((ResourceContainer)element).getMaxCargoVolume());
					stmt.setString(11, ((ResourceContainer)element).getCargoEnvironment().getName());
					stmt.setNull(12, Types.DOUBLE);
					stmt.setNull(13, Types.DOUBLE);
					stmt.setNull(14, Types.DOUBLE);
					stmt.setNull(15, Types.INTEGER);
					stmt.setNull(16, Types.DOUBLE);
					stmt.setNull(17, Types.DOUBLE);
					stmt.setNull(18, Types.INTEGER);
					stmt.setNull(19, Types.DOUBLE);
					stmt.setNull(20, Types.DOUBLE);
					stmt.setNull(21, Types.INTEGER);
		    		saveContents((ResourceContainer)element);
				} else if(element.getElementType()==ElementType.CREW_MEMBER) {
					stmt.setNull(8, Types.INTEGER);
					stmt.setNull(9, Types.DOUBLE);
					stmt.setNull(10, Types.DOUBLE);
					stmt.setNull(11, Types.VARCHAR);
					stmt.setDouble(12, ((CrewMember)element).getAvailableTimeFraction());
					stmt.setNull(13, Types.DOUBLE);
					stmt.setNull(14, Types.DOUBLE);
					stmt.setNull(15, Types.INTEGER);
					stmt.setNull(16, Types.DOUBLE);
					stmt.setNull(17, Types.DOUBLE);
					stmt.setNull(18, Types.INTEGER);
					stmt.setNull(19, Types.DOUBLE);
					stmt.setNull(20, Types.DOUBLE);
					stmt.setNull(21, Types.INTEGER);
				} else if(element.getElementType()==ElementType.CARRIER) {
					stmt.setInt(8, ((Carrier)element).getMaxCrewSize());
					stmt.setDouble(9, ((Carrier)element).getMaxCargoMass());
					stmt.setDouble(10, ((Carrier)element).getMaxCargoVolume());
					stmt.setString(11, ((Carrier)element).getCargoEnvironment().getName());
					stmt.setNull(12, Types.DOUBLE);
					stmt.setNull(13, Types.DOUBLE);
					stmt.setNull(14, Types.DOUBLE);
					stmt.setNull(15, Types.INTEGER);
					stmt.setNull(16, Types.DOUBLE);
					stmt.setNull(17, Types.DOUBLE);
					stmt.setNull(18, Types.INTEGER);
					stmt.setNull(19, Types.DOUBLE);
					stmt.setNull(20, Types.DOUBLE);
					stmt.setNull(21, Types.INTEGER);
				} else if(element.getElementType()==ElementType.PROPULSIVE_VEHICLE) {
					stmt.setInt(8, ((PropulsiveVehicle)element).getMaxCrewSize());
					stmt.setDouble(9, ((PropulsiveVehicle)element).getMaxCargoMass());
					stmt.setDouble(10, ((PropulsiveVehicle)element).getMaxCargoVolume());
					stmt.setString(11, ((PropulsiveVehicle)element).getCargoEnvironment().getName());
					stmt.setNull(12, Types.DOUBLE);
					if(((PropulsiveVehicle)element).getOmsIsp() > 0) {
						stmt.setDouble(13, ((PropulsiveVehicle)element).getOmsIsp());
						stmt.setDouble(14, ((PropulsiveVehicle)element).getOmsFuelTank().getMaxAmount());
						stmt.setInt(15, ((PropulsiveVehicle)element).getOmsFuelTank().getResource().getTid());
					} else {
						stmt.setDouble(13, 0);
						stmt.setDouble(14, 0);
						stmt.setNull(15, Types.INTEGER);
					}
					if(((PropulsiveVehicle)element).getRcsIsp() > 0) {
						stmt.setDouble(16, ((PropulsiveVehicle)element).getRcsIsp());
						if(((PropulsiveVehicle)element).getRcsFuelTank()==null ||
								((PropulsiveVehicle)element).getRcsFuelTank()==((PropulsiveVehicle)element).getOmsFuelTank()) {
							// shared OMS / RCS tanks
							stmt.setDouble(17, 0);
							stmt.setNull(18, Types.INTEGER);
						} else {
							stmt.setDouble(17, ((PropulsiveVehicle)element).getRcsFuelTank().getMaxAmount());
							stmt.setInt(18, ((PropulsiveVehicle)element).getRcsFuelTank().getResource().getTid());
						}
					} else {
						stmt.setDouble(16, 0);
						stmt.setDouble(17, 0);
						stmt.setNull(18, Types.INTEGER);
					}
					stmt.setNull(19, Types.DOUBLE);
					stmt.setNull(20, Types.DOUBLE);
					stmt.setNull(21, Types.INTEGER);
				} else if(element.getElementType()==ElementType.SURFACE_VEHICLE) {
					stmt.setInt(8, ((SurfaceVehicle)element).getMaxCrewSize());
					stmt.setDouble(9, ((SurfaceVehicle)element).getMaxCargoMass());
					stmt.setDouble(10, ((SurfaceVehicle)element).getMaxCargoVolume());
					stmt.setString(11, ((SurfaceVehicle)element).getCargoEnvironment().getName());
					stmt.setNull(12, Types.DOUBLE);
					stmt.setNull(13, Types.DOUBLE);
					stmt.setNull(14, Types.DOUBLE);
					stmt.setNull(15, Types.INTEGER);
					stmt.setNull(16, Types.DOUBLE);
					stmt.setNull(17, Types.DOUBLE);
					stmt.setNull(18, Types.INTEGER);
					stmt.setDouble(19, ((SurfaceVehicle)element).getMaxSpeed());
					stmt.setDouble(20, ((SurfaceVehicle)element).getFuelTank().getMaxAmount());
					stmt.setInt(21, ((SurfaceVehicle)element).getFuelTank().getResource().getTid());
				}
				stmt.setString(22, element.getDescription());
				stmt.executeUpdate();
				stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() AS id");
				rs = stmt.executeQuery();
				if(rs.next()) {
					element.setTid(rs.getInt("id"));
				}
				elementPreviewLibrary.add(new ElementPreview(
	    				element.getTid(), 
	    				element.getName(), 
	    				element.getElementType(),
	    				element.getIconType()));
			} else {
				elementPreviewLibrary.remove(loadElementPreview(element.getTid()));
	    		elementPreviewLibrary.add(new ElementPreview(
	    				element.getTid(), 
	    				element.getName(), 
	    				element.getElementType(),
	    				element.getIconType()));
			}
			saveParts(element);
			saveStates(element);
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves the contents of a resource container. Removed resources will be
	 * deleted from the data source, changed resources will be updated, and
	 * added resources will be inserted.
	 * 
	 * @param container the container
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveContents(I_ResourceContainer container) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// delete unused contents
			if(container.getContents().keySet().size()>0) {
				String resourceList = "";
				for(I_Resource resource : container.getContents().keySet()) 
					resourceList += resource.getTid() + ", ";
				resourceList = resourceList.substring(0, resourceList.length()-2);
				stmt = connection.prepareStatement("DELETE FROM demands " +
						"WHERE container_id = ? " +
						"AND resource_id NOT IN (" + resourceList + ")");
				stmt.setInt(1, container.getTid());
				stmt.executeUpdate();
			}
			
			// update/insert contents
			for(I_Resource resource : container.getContents().keySet()) {
				stmt = connection.prepareStatement("UPDATE demands " +
						"SET amount=? " +
						"WHERE resource_id=? AND container_id=? ");
				stmt.setDouble(1, container.getContents().get(resource));
				stmt.setInt(2, resource.getTid());
				stmt.setInt(3, container.getTid());
				int i = stmt.executeUpdate();
				if(i==0) {
					// insert new contents
					stmt = connection.prepareStatement("INSERT INTO demands " +
							"(resource_id, container_id, amount) " +
							"VALUES (?, ?, ?) ");
					stmt.setInt(1, resource.getTid());
					stmt.setInt(2, container.getTid());
					stmt.setDouble(3, container.getContents().get(resource));
					stmt.executeUpdate();
				}
			}
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves the parts associated with an element. Removed parts will be
	 * deleted from the data source, changed parts will be updated, and
	 * added parts will be inserted.
	 * 
	 * @param element the element
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveParts(I_Element element) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// delete unused parts
			if(element.getParts().size()>0) {
				String partsList = "";
				for(PartApplication part : element.getParts()) 
					partsList += part.getPart().getTid() + ", ";
				partsList = partsList.substring(0, partsList.length()-2);
				stmt = connection.prepareStatement("DELETE FROM parts " +
						"WHERE element_id = ? " +
						"AND resource_id NOT IN (" + partsList + ")");
				stmt.setInt(1, element.getTid());
				stmt.executeUpdate();
			}
			
			// update/insert parts
			for(PartApplication part : element.getParts()) {
				stmt = connection.prepareStatement("UPDATE parts " +
						"SET quantity=?, duty_cycle=?, mttf=?, mttr=?, " +
						"repair_mass=? " +
						"WHERE resource_id=? AND element_id=? ");
				stmt.setDouble(1, part.getQuantity());
				stmt.setDouble(2, part.getDutyCycle());
				stmt.setDouble(3, part.getMeanTimeToFailure());
				stmt.setDouble(4, part.getMeanTimeToRepair());
				stmt.setDouble(5, part.getMassToRepair());
				stmt.setInt(6, part.getPart().getTid());
				stmt.setInt(7, element.getTid());
				int i = stmt.executeUpdate();
				if(i==0) {
					// insert new part
					stmt = connection.prepareStatement("INSERT INTO parts " +
							"(resource_id, element_id, quantity, duty_cycle, " +
							"mttf, mttr, repair_mass) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?) ");
					stmt.setInt(1, part.getPart().getTid());
					stmt.setInt(2, element.getTid());
					stmt.setDouble(3, part.getQuantity());
					stmt.setDouble(4, part.getDutyCycle());
					stmt.setDouble(5, part.getMeanTimeToFailure());
					stmt.setDouble(6, part.getMeanTimeToRepair());
					stmt.setDouble(7, part.getMassToRepair());
					stmt.executeUpdate();
					rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
					if(rs.next()) {
						part.setTid(rs.getInt("id"));
					}
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves the states associated with an element. Removed states will be
	 * deleted from the data source, changed states will be updated, and 
	 * added states will be assigned a new TID and inserted.
	 * 
	 * @param element the element
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveStates(I_Element element) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// delete unused states
			if(element.getStates().size()>0) {
				String statesList = "";
				for(I_State state : element.getStates()) 
					statesList += state.getTid() + ", ";
				statesList = statesList.substring(0, statesList.length()-2);
				stmt = connection.prepareStatement("DELETE FROM states " +
						"WHERE element_id = ? " +
						"AND id NOT IN (" + statesList + ")");
				stmt.setInt(1, element.getTid());
				stmt.executeUpdate();
			}
			
			// update/insert states
			for(I_State state : element.getStates()) {
				stmt = connection.prepareStatement("UPDATE states " +
						"SET name=?, type=?, initial=? " +
						"WHERE id=? ");
				stmt.setString(1, state.getName());
				stmt.setString(2, state.getStateType().getName());
				stmt.setInt(3, element.getCurrentState()==state?1:0);
				stmt.setInt(4, state.getTid());
				int i = stmt.executeUpdate();
				if(i==0) {
					// insert new state
					stmt = connection.prepareStatement("INSERT INTO states " +
							"(element_id, name, type, initial) " +
							"VALUES (?, ?, ?, ?) ");
					stmt.setInt(1, element.getTid());
					stmt.setString(2, state.getName());
					stmt.setString(3, state.getStateType().getName());
					stmt.setInt(4, element.getCurrentState()==state?1:0);
					stmt.executeUpdate();
					rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
					if(rs.next()) {
						state.setTid(rs.getInt("id"));
					}
				}
				saveModels(state);
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves the associated demand models contained within a state. Removed
	 * models will be deleted from the data source, changed models will be
	 * updated, and added models will assigned a new TID and be inserted.
	 * 
	 * @param state the state
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveModels(I_State state) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// delete unused models
			if(state.getDemandModels().size()>0) {
				String modelsList = "";
				for(I_DemandModel model : state.getDemandModels()) 
					modelsList += model.getTid() + ", ";
				modelsList = modelsList.substring(0, modelsList.length()-2);
				stmt = connection.prepareStatement("DELETE FROM models " +
						"WHERE state_id = ? " +
						"AND id NOT IN (" + modelsList + ")");
				stmt.setInt(1, state.getTid());
				stmt.executeUpdate();
			}
			
			// update/insert models
			for(I_DemandModel model : state.getDemandModels()) {
				stmt = connection.prepareStatement("UPDATE models " +
						"SET type=?, name=?, parts_list=?, unpress_rate=?, press_rate=? " +
						"WHERE id=? ");
				stmt.setString(1, model.getDemandModelType().getName());
				stmt.setString(2, model.getName());
				stmt.setInt(6, model.getTid());
				if(model.getDemandModelType()==DemandModelType.TIMED_IMPULSE 
						|| model.getDemandModelType()==DemandModelType.RATED) {
					stmt.setNull(3, Types.INTEGER);
					stmt.setNull(4, Types.DOUBLE);
					stmt.setNull(5, Types.DOUBLE);
					saveDemands(model);
				} else if(model.getDemandModelType()==DemandModelType.SPARING_BY_MASS) {
					stmt.setInt(3, ((SparingByMassDemandModel)model).isPartsListEnabled()?1:0);
					stmt.setDouble(4, ((SparingByMassDemandModel)model).getUnpressurizedSparesRate());
					stmt.setDouble(5, ((SparingByMassDemandModel)model).getPressurizedSparesRate());
				}
				int i = stmt.executeUpdate();
				if(i==0) {
					// insert new model
					stmt = connection.prepareStatement("INSERT INTO models " +
							"(state_id, type, name, parts_list, unpress_rate, press_rate) " +
							"VALUES (?, ?, ?, ?, ?, ?) ");
					stmt.setInt(1, state.getTid());
					stmt.setString(2, model.getDemandModelType().getName());
					stmt.setString(3, model.getName());
					if(model.getDemandModelType()==DemandModelType.TIMED_IMPULSE) {
						stmt.setNull(4, Types.INTEGER);
						stmt.setNull(5, Types.DOUBLE);
						stmt.setNull(6, Types.DOUBLE);
						saveDemands(model);
					} else if (model.getDemandModelType()==DemandModelType.RATED) {
						stmt.setNull(4, Types.INTEGER);
						stmt.setNull(5, Types.DOUBLE);
						stmt.setNull(6, Types.DOUBLE);
						saveDemands(model);
					} else if(model.getDemandModelType()==DemandModelType.SPARING_BY_MASS) {
						stmt.setInt(4, ((SparingByMassDemandModel)model).isPartsListEnabled()?1:0);
						stmt.setDouble(5, ((SparingByMassDemandModel)model).getUnpressurizedSparesRate());
						stmt.setDouble(6, ((SparingByMassDemandModel)model).getPressurizedSparesRate());
					}
					stmt.executeUpdate();
					rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
					if(rs.next()) {
						model.setTid(rs.getInt("id"));
					}
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/**
	 * Saves any associated demands from a demand model. Demands that have been
	 * removed will be deleted from the data source, demands that have been
	 * changed will be updated, and added demands will be inserted.
	 * 
	 * @param model the model
	 * 
	 * @throws SQLException the SQL exception
	 */
	private void saveDemands(I_DemandModel model) throws SQLException {
		PreparedStatement stmt = null;
		try {
			if(model.getDemandModelType()==DemandModelType.TIMED_IMPULSE) {
				// delete unused demands
				if(((TimedImpulseDemandModel)model).getDemands().size()>0) {
					String demandsList = "";
					for(Demand demand : ((TimedImpulseDemandModel)model).getDemands()) 
						demandsList += demand.getResource().getTid() + ", ";
					demandsList = demandsList.substring(0, demandsList.length()-2);
					stmt = connection.prepareStatement("DELETE FROM demands " +
							"WHERE model_id = ? " +
							"AND resource_id NOT IN (" + demandsList + ")");
					stmt.setInt(1, model.getTid());
					stmt.executeUpdate();
				}
				
				// update/insert demands
				for(Demand demand : ((TimedImpulseDemandModel)model).getDemands()) {
					stmt = connection.prepareStatement("UPDATE demands " +
							"SET amount=? " +
							"WHERE resource_id=? AND model_id=? ");
					stmt.setDouble(1, demand.getAmount());
					stmt.setInt(2, demand.getResource().getTid());
					stmt.setInt(3, model.getTid());
					int i = stmt.executeUpdate();
					if(i==0) {
						// insert new demand
						stmt = connection.prepareStatement("INSERT INTO demands " +
								"(resource_id, model_id, amount) " +
								"VALUES (?, ?, ?) ");
						stmt.setInt(1, demand.getResource().getTid());
						stmt.setInt(2, model.getTid());
						stmt.setDouble(3, demand.getAmount());
						stmt.executeUpdate();
					}
				}
			} else if(model.getDemandModelType()==DemandModelType.RATED) {
				// delete unused demands
				if(((RatedDemandModel)model).getDemandRates().size()>0) {
					String demandsList = "";
					for(Demand demand : ((RatedDemandModel)model).getDemandRates()) 
						demandsList += demand.getResource().getTid() + ", ";
					demandsList = demandsList.substring(0, demandsList.length()-2);
					stmt = connection.prepareStatement("DELETE FROM demands " +
							"WHERE model_id = ? " +
							"AND resource_id NOT IN (" + demandsList + ")");
					stmt.setInt(1, model.getTid());
					stmt.executeUpdate();
				}
				
				// update/insert demands
				for(Demand demand : ((RatedDemandModel)model).getDemandRates()) {
					stmt = connection.prepareStatement("UPDATE demands " +
							"SET amount=? " +
							"WHERE resource_id=? AND model_id=? ");
					stmt.setDouble(1, demand.getAmount());
					stmt.setInt(2, demand.getResource().getTid());
					stmt.setInt(3, model.getTid());
					int i = stmt.executeUpdate();
					if(i==0) {
						// insert new demand
						stmt = connection.prepareStatement("INSERT INTO demands " +
								"(resource_id, model_id, amount) " +
								"VALUES (?, ?, ?) ");
						stmt.setInt(1, demand.getResource().getTid());
						stmt.setInt(2, model.getTid());
						stmt.setDouble(3, demand.getAmount());
						stmt.executeUpdate();
					}
				}
			}
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#saveNode(edu.mit.spacenet.domain.network.node.Node)
	 */
	public void saveNode(Node node) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("UPDATE nodes " +
				"SET type=?, name=?, body_1=?, latitude=?, longitude=?, " +
				"apoapsis=?, periapsis=?, inclination=?, body_2=?, " +
				"lp_number=?, description=?" +
				"WHERE id=?");
			stmt.setString(1, node.getNodeType().getName());
			stmt.setString(2, node.getName());
			stmt.setString(3, node.getBody().getName());
			if(node.getNodeType()==NodeType.SURFACE) {
				stmt.setDouble(4, ((SurfaceNode)node).getLatitude());
				stmt.setDouble(5, ((SurfaceNode)node).getLongitude());
			} else {
				stmt.setNull(4, Types.DOUBLE);
				stmt.setNull(5, Types.DOUBLE);
			}
			if(node.getNodeType()==NodeType.ORBITAL) {
				stmt.setDouble(6, ((OrbitalNode)node).getApoapsis());
				stmt.setDouble(7, ((OrbitalNode)node).getPeriapsis());
				stmt.setDouble(8, ((OrbitalNode)node).getInclination());
			} else {
				stmt.setNull(6, Types.DOUBLE);
				stmt.setNull(7, Types.DOUBLE);
				stmt.setNull(8, Types.DOUBLE);
			}
			if(node.getNodeType()==NodeType.LAGRANGE) {
				stmt.setString(9, ((LagrangeNode)node).getMinorBody().getName());
				stmt.setInt(10, ((LagrangeNode)node).getNumber());
			} else {
				stmt.setNull(9, Types.VARCHAR);
				stmt.setNull(10, Types.INTEGER);
			}
			stmt.setString(11, node.getDescription());
			stmt.setInt(12, node.getTid());
			int i = stmt.executeUpdate();
			if(i == 0) {
				// insert new node
				stmt = connection.prepareStatement("INSERT INTO nodes " +
						"(type, name, body_1, latitude, longitude, " +
						"apoapsis, periapsis, inclination, body_2, " +
						"lp_number, description) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, node.getNodeType().getName());
				stmt.setString(2, node.getName());
				stmt.setString(3, node.getBody().getName());
				if(node.getNodeType()==NodeType.SURFACE) {
					stmt.setDouble(4, ((SurfaceNode)node).getLatitude());
					stmt.setDouble(5, ((SurfaceNode)node).getLongitude());
				} else {
					stmt.setNull(4, Types.DOUBLE);
					stmt.setNull(5, Types.DOUBLE);
				}
				if(node.getNodeType()==NodeType.ORBITAL) {
					stmt.setDouble(6, ((OrbitalNode)node).getApoapsis());
					stmt.setDouble(7, ((OrbitalNode)node).getPeriapsis());
					stmt.setDouble(8, ((OrbitalNode)node).getInclination());
				} else {
					stmt.setNull(6, Types.DOUBLE);
					stmt.setNull(7, Types.DOUBLE);
					stmt.setNull(8, Types.DOUBLE);
				}
				if(node.getNodeType()==NodeType.LAGRANGE) {
					stmt.setString(9, ((LagrangeNode)node).getMinorBody().getName());
					stmt.setInt(10, ((LagrangeNode)node).getNumber());
				} else {
					stmt.setNull(9, Types.VARCHAR);
					stmt.setNull(10, Types.INTEGER);
				}
				stmt.setString(11, node.getDescription());
				stmt.executeUpdate();
				stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() AS id");
				rs = stmt.executeQuery();
				if(rs.next()) {
					node.setTid(rs.getInt("id"));
					getNodeLibrary().add(node);
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) {}
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#saveResource(edu.mit.spacenet.domain.resource.I_Resource)
	 */
	public void saveResource(I_Resource resource) throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("UPDATE resources " +
					"SET type=?, name=?, cos=?, units=?, unit_mass=?, " +
					"unit_volume=?, packing_factor=?, environment=?, " +
					"description=? " +
					"WHERE id=?");
			stmt.setString(1, resource.getResourceType().getName());
			stmt.setString(2, resource.getName());
			stmt.setInt(3, resource.getClassOfSupply().getId());
			stmt.setString(4, resource.getUnits());
			stmt.setDouble(5, resource.getUnitMass());
			stmt.setDouble(6, resource.getUnitVolume());
			stmt.setDouble(7, resource.getPackingFactor());
			stmt.setString(8, resource.getEnvironment().getName());
			stmt.setString(9, resource.getDescription());
			stmt.setInt(10, resource.getTid());
			int i = stmt.executeUpdate();
			if(i == 0) {
				// insert new resource
				stmt = connection.prepareStatement("INSERT INTO resources " +
						"(type, name, cos, units, unit_mass, unit_volume, " +
						"packing_factor, environment, description) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				stmt.setString(1, resource.getResourceType().getName());
				stmt.setString(2, resource.getName());
				stmt.setInt(3, resource.getClassOfSupply().getId());
				stmt.setString(4, resource.getUnits());
				stmt.setDouble(5, resource.getUnitMass());
				stmt.setDouble(6, resource.getUnitVolume());
				stmt.setDouble(7, resource.getPackingFactor());
				stmt.setString(8, resource.getEnvironment().getName());
				stmt.setString(9, resource.getDescription());
				stmt.executeUpdate();
				stmt = connection.prepareStatement("SELECT LAST_INSERT_ID() AS id");
				rs = stmt.executeQuery();
				if(rs.next()) {
					resource.setTid(rs.getInt("id"));
					getResourceLibrary().add(resource);
				}
			}
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch(SQLException ex) { }
			}
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#validateData()
	 */
	public List<String> validateData() {
		// this data source enforces valid data via table structure
		return new ArrayList<String>();
	}
	
	/**
	 * Creates the connection.
	 * 
	 * @throws SQLException the SQL exception
	 */
	public void createConnection() throws SQLException {
		if(connection==null) {
			if(port==null) {
				connection = DriverManager.getConnection("jdbc:mysql://"+host+
						"/"+database+"?user="+user+"&password="+password);
			} else {
				connection = DriverManager.getConnection("jdbc:mysql://"+host+
						":"+port+"/"+database+"?user="+user+"&password="+password);
			}
		}
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 * 
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the database.
	 * 
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Sets the database.
	 * 
	 * @param database the new database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 * 
	 * @param port the new port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.data.I_DataSource#format()
	 */
	public void format() throws SQLException {
		createConnection();
		PreparedStatement stmt = null;
		try {
			String query = new String();
			query += "CREATE TABLE IF NOT EXISTS `nodes` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`type` enum('Surface','Orbital','Lagrange') NOT NULL," +
				"`name` varchar(255) NOT NULL," +
				"`body_1` enum('Sun','Earth','Moon','Mars','Phobos','Venus','Jupiter') NOT NULL," +
				"`latitude` double default NULL," +
				"`longitude` double default NULL," +
				"`apoapsis` double default NULL," +
				"`periapsis` double default NULL," +
				"`inclination` double default NULL," +
				"`body_2` enum('Sun','Earth','Moon','Mars','Phobos','Venus','Jupiter') default NULL," +
				"`lp_number` int(11) default NULL," +
				"`description` varchar(4000) NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `edges` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`type` enum('Space','Surface','Flight') NOT NULL," +
				"`name` varchar(255) NOT NULL," +
				"`origin_id` int(11) NOT NULL," +
				"`destination_id` int(11) NOT NULL," +
				"`duration` double default NULL," +
				"`distance` double default NULL," +
				"`max_crew` int(11) default NULL," +
				"`max_cargo` double default NULL," +
				"`description` varchar(4000) NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `burns` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`edge_id` int(11) NOT NULL," +
				"`time` double NOT NULL," +
				"`order` int(11) NOT NULL," +
				"`type` enum('OMS','RCS') NOT NULL," +
				"`delta_v` double NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `resources` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`type` enum('Continuous','Discrete') NOT NULL," +
				"`name` varchar(255) NOT NULL," +
				"`cos` int(11) NOT NULL," +
				"`units` varchar(255) NOT NULL default 'kg'," +
				"`unit_mass` double NOT NULL default '1'," +
				"`unit_volume` double NOT NULL default '0.01'," +
				"`packing_factor` double NOT NULL default '0'," +
				"`environment` enum('Pressurized','Unpressurized') NOT NULL default 'Unpressurized'," +
				"`description` varchar(4000) NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `elements` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`type` enum('Element','Crew Member','Resource Container','Carrier','Propulsive Vehicle','Surface Vehicle') NOT NULL default 'Element'," +
				"`name` varchar(255) NOT NULL," +
				"`cos` int(11) NOT NULL," +
				"`environment` enum('Pressurized','Unpressurized') NOT NULL default 'Unpressurized'," +
				"`accommodation_mass` double NOT NULL default '0'," +
				"`mass` double NOT NULL default '0'," +
				"`volume` double NOT NULL default '0'," +
				"`max_crew` int(11) default NULL," +
				"`cargo_mass` double default NULL," +
				"`cargo_volume` double default NULL," +
				"`cargo_environment` enum('Pressurized','Unpressurized') default NULL," +
				"`active_fraction` double default NULL," +
				"`oms_isp` double default NULL," +
				"`max_oms` double default NULL," +
				"`oms_id` int(11) default NULL," +
				"`rcs_isp` double default NULL," +
				"`max_rcs` double default NULL," +
				"`rcs_id` int(11) default NULL," +
				"`max_speed` double default NULL," +
				"`max_fuel` double default NULL," +
				"`fuel_id` int(11) default NULL," +
				"`description` varchar(4000) NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `parts` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`resource_id` int(11) NOT NULL," +
				"`element_id` int(11) NOT NULL," +
				"`quantity` int(11) NOT NULL default '1'," +
				"`duty_cycle` double NOT NULL default '1'," +
				"`mttf` double NOT NULL default '0'," +
				"`mttr` double NOT NULL default '0'," +
				"`repair_mass` double NOT NULL default '0'," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `states` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`element_id` int(11) NOT NULL," +
				"`name` varchar(255) NOT NULL," +
				"`type` enum('Active','Quiescent','Dormant','Decommissioned') NOT NULL," +
				"`initial` tinyint(1) NOT NULL default '0'," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `models` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`type` enum('Rated Demand Model','Timed Impulse Demand Model','Sparing By Mass Demand Model') NOT NULL," +
				"`state_id` int(11) NOT NULL," +
				"`parts_list` tinyint(1) default NULL," +
				"`unpress_rate` double default NULL," +
				"`press_rate` double default NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			query += "CREATE TABLE IF NOT EXISTS `demands` (" +
				"`id` int(11) NOT NULL auto_increment," +
				"`model_id` int(11) default NULL," +
				"`container_id` int(11) default NULL," +
				"`resource_id` int(11) NOT NULL," +
				"`amount` double NOT NULL," +
				"PRIMARY KEY  (`id`)" +
				") ENGINE=MyISAM  DEFAULT CHARSET=latin1;";
			stmt = connection.prepareStatement("");
			stmt.executeUpdate();
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch(SQLException ex) {}
			}
		}
	}
}
