/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.mit.spacenet.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

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
public abstract class XStreamEngine {

  private static XStream getXStream() {
    XStream xs = new XStream();
    xs.addPermission(NoTypePermission.NONE);
    xs.addPermission(NullPermission.NULL);
    xs.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xs.allowTypesByWildcard(new String[] {"edu.mit.spacenet.**", "java.util.*"});
    // skip loading manifest (breaking changes in data structure)
    xs.omitField(Scenario.class, "manifest");

    return xs;
  }

  private static XStream getXStreamRead() {
    XStream xs = new XStream();
    xs.addPermission(NoTypePermission.NONE);
    xs.addPermission(NullPermission.NULL);
    xs.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xs.allowTypesByWildcard(new String[] {"edu.mit.spacenet.**", "java.util.*"});
    // TODO: correct references to node/edge libraries
    // xs.omitField(DataSource.class, "nodeLibrary");
    // xs.omitField(DataSource.class, "edgeLibrary");
    // TODO: correct references to resource library
    // xs.omitField(DataSource.class, "resourceLibrary");
    // TODO: correct references to item preview library
    // xs.omitField(DataSource.class, "itemPreviewLibrary");
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
    xs.alias("edu.mit.spacenet.domain.demand.model.SparingByMassDemandModel",
        SparingByMassDemandModel.class);
    xs.alias("edu.mit.spacenet.domain.demand.model.RatedDemandModel", RatedDemandModel.class);
    xs.alias("edu.mit.spacenet.domain.demand.model.TimedImpulseDemandModel",
        TimedImpulseDemandModel.class);
    xs.alias("edu.mit.spacenet.domain.demand.State", State.class);
    xs.alias("edu.mit.spacenet.domain.demand.PartApplication", PartApplication.class);
    xs.alias("edu.mit.spacenet.scenario.data.Spreadsheet_2_5", Spreadsheet_2_5.class);
    xs.alias("edu.mit.spacenet.scenario.data.DataSourceType", DataSourceType.class);
    xs.alias("edu.mit.spacenet.scenario.data.ElementPreview", ElementPreview.class);

    // skip loading manifest (breaking changes in data structure)
    xs.omitField(Scenario.class, "manifest");

    return xs;
  }

  /**
   * Serializes and saves a scenario.
   * 
   * @param scenario the scenario to save
   * 
   * @throws FileNotFoundException the file not found exception
   * @throws IOException the i/o exception
   */
  public static void saveScenario(Scenario scenario) throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(scenario.getFilePath());
    getXStream().toXML(scenario, fos);
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
  public static Scenario openScenario(String filePath) throws FileNotFoundException, IOException {
    Scenario scenario = new Scenario();

    FileInputStream fis = new FileInputStream(filePath);
    getXStreamRead().fromXML(fis, scenario);
    // TODO: re-load libraries when opening scenario
    // scenario.getDataSource().loadLibraries();
    fis.close();
    return scenario;
  }
}
