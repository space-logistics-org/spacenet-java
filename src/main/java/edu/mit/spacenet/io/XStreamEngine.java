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
package edu.mit.spacenet.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import edu.mit.spacenet.data.DataSourceType;
import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.data.Spreadsheet_2_5;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.element.State;
import edu.mit.spacenet.domain.model.RatedDemandModel;
import edu.mit.spacenet.domain.model.SparingByMassDemandModel;
import edu.mit.spacenet.domain.model.TimedImpulseDemandModel;
import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.scenario.Scenario;

/**
 * A class to help manage serialization/deserialization of objects.
 * 
 * @author Paul Grogan
 */
public class XStreamEngine {
	
	/**
	 * Serializes and saves a scenario.
	 * 
	 * @param scenario the scenario to save
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException the i/o exception
	 */
	public static void saveScenario(Scenario scenario) 
		throws FileNotFoundException, IOException {
		XStream xs = new XStream();
		// TODO: correct references to node/edge libraries
		//xs.omitField(DataSource.class, "nodeLibrary");
		//xs.omitField(DataSource.class, "edgeLibrary");
		// TODO: correct references to resource library
		//xs.omitField(DataSource.class, "resourceLibrary");
		// TODO: correct references to item preview library
		//xs.omitField(DataSource.class, "itemPreviewLibrary");
		xs.alias("scenario", Scenario.class);
		xs.alias("excel", Spreadsheet_2_5.class);
		xs.alias("surfaceNode", SurfaceNode.class);
		xs.alias("orbitalNode", OrbitalNode.class);
		xs.alias("lagrangeNode", LagrangeNode.class);
		xs.alias("spaceEdge", SpaceEdge.class);
		xs.alias("surfaceEdge", SurfaceEdge.class);
		xs.alias("flightEdge", FlightEdge.class);
		xs.alias("burn", Burn.class);
		
		FileOutputStream fos = new FileOutputStream(scenario.getFilePath());
		xs.toXML(scenario, fos);
		fos.close();
	}
	
	/**
	 * Opens and deserializes a scenario based on a file path.
	 * 
	 * @param filePath the file path to open
	 * 
	 * @return the deserialized scenario
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException the i/o exception
	 */
	public static Scenario openScenario(String filePath) 
		throws FileNotFoundException, IOException {
		XStream xs = new XStream();
		xs.alias("scenario", Scenario.class);
		xs.alias("excel", Spreadsheet_2_5.class);
		xs.alias("surfaceNode", SurfaceNode.class);
		xs.alias("orbitalNode", OrbitalNode.class);
		xs.alias("lagrangeNode", LagrangeNode.class);
		xs.alias("spaceEdge", SpaceEdge.class);
		xs.alias("surfaceEdge", SurfaceEdge.class);
		xs.alias("flightEdge", FlightEdge.class);
		xs.alias("burn", Burn.class);
		xs.alias("edu.mit.spacenet.domain.demand.Demand", Demand.class);
		xs.alias("edu.mit.spacenet.domain.demand.DemandSet", DemandSet.class);
		xs.alias("edu.mit.spacenet.domain.demand.model.SparingByMassDemandModel", SparingByMassDemandModel.class);
		xs.alias("edu.mit.spacenet.domain.demand.model.RatedDemandModel", RatedDemandModel.class);
		xs.alias("edu.mit.spacenet.domain.demand.model.TimedImpulseDemandModel", TimedImpulseDemandModel.class);
		xs.alias("edu.mit.spacenet.domain.demand.State", State.class);
		xs.alias("edu.mit.spacenet.domain.demand.PartApplication", PartApplication.class);
		xs.alias("edu.mit.spacenet.scenario.data.Spreadsheet_2_5", Spreadsheet_2_5.class);
		xs.alias("edu.mit.spacenet.scenario.data.DataSourceType", DataSourceType.class);
		xs.alias("edu.mit.spacenet.scenario.data.ElementPreview", ElementPreview.class);
		Scenario scenario = new Scenario();
		
		FileInputStream fis = new FileInputStream(filePath);
		xs.fromXML(fis, scenario);
		// TODO: re-load libraries when opening scenario
		//scenario.getDataSource().loadLibraries();
		fis.close();
		return scenario;
	}
}