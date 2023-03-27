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
package edu.mit.spacenet.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.Element;
import edu.mit.spacenet.domain.element.ElementIcon;
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
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.domain.resource.ResourceType;

/**
 * A spreadsheet format compatible with the features introduced in SpaceNet 2.5.
 * 
 * Assumes the filepath points to a spreadsheet in Excel (.xls) format with the following sheets:
 * Nodes, Edges, Burns, Resources, Elements, Parts, States, Models, Demands.
 * 
 * @author Paul Grogan
 */
public class Spreadsheet_2_5 extends AbstractDataSource {
  private static final int BURN_ID = 0, BURN_EDGE_ID = 1, BURN_TIME = 2, BURN_ORDER = 3,
      BURN_TYPE = 4, BURN_DELTA_V = 5;
  private static final int DEMAND_ID = 0, DEMAND_MODEL_ID = 1, DEMAND_RESOURCE_ID = 2,
      DEMAND_AMOUNT = 3, DEMAND_CONTAINER_ID = 4;
  private static final int EDGE_ID = 0, EDGE_TYPE = 1, EDGE_NAME = 2, EDGE_ORIGIN_ID = 3,
      EDGE_DESTINATION_ID = 4, EDGE_DURATION = 5, EDGE_DISTANCE = 6, EDGE_MAX_CREW = 7,
      EDGE_MAX_CARGO = 8, EDGE_DESCRIPTION = 9;
  private static final int ELEMENT_ID = 0, ELEMENT_TYPE = 1, ELEMENT_NAME = 2, ELEMENT_COS = 3,
      ELEMENT_ENVIRONMENT = 4, ELEMENT_ACCOMMODATION_MASS = 5, ELEMENT_MASS = 6, ELEMENT_VOLUME = 7,
      ELEMENT_MAX_CREW = 8, ELEMENT_CARGO_MASS = 9, ELEMENT_CARGO_VOLUME = 10,
      ELEMENT_CARGO_ENVIRONMENT = 11, ELEMENT_ACTIVE_FRACTION = 12, ELEMENT_OMS_ISP = 13,
      ELEMENT_MAX_OMS = 14, ELEMENT_OMS_ID = 15, ELEMENT_RCS_ISP = 16, ELEMENT_MAX_RCS = 17,
      ELEMENT_RCS_ID = 18, ELEMENT_MAX_SPEED = 19, ELEMENT_MAX_FUEL = 20, ELEMENT_FUEL_ID = 21,
      ELEMENT_DESCRIPTION = 22, ELEMENT_ICON = 23;
  private static final int MODEL_ID = 0, MODEL_TYPE = 1, MODEL_STATE_ID = 2, MODEL_NAME = 3,
      MODEL_PARTS_LIST = 4, MODEL_UNPRESS_RATE = 5, MODEL_PRESS_RATE = 6;
  private static final int NODE_ID = 0, NODE_TYPE = 1, NODE_NAME = 2, NODE_BODY_1 = 3,
      NODE_LATITUDE = 4, NODE_LONGITUDE = 5, NODE_APOAPSIS = 6, NODE_PERIAPSIS = 7,
      NODE_INCLINATION = 8, NODE_BODY_2 = 9, NODE_LP_NUMBER = 10, NODE_DESCRIPTION = 11;
  private static final int NODE_SHEET = 0, EDGE_SHEET = 1, BURN_SHEET = 2, RESOURCE_SHEET = 3,
      ELEMENT_SHEET = 4, PART_SHEET = 5, STATE_SHEET = 6, MODEL_SHEET = 7, DEMAND_SHEET = 8;
  private static final int PART_ID = 0, PART_RESOURCE_ID = 1, PART_ELEMENT_ID = 2,
      PART_QUANTITY = 3, PART_DUTY_CYCLE = 4, PART_MTTF = 5, PART_MTTR = 6, PART_REPAIR_MASS = 7;
  private static final int RESOURCE_ID = 0, RESOURCE_TYPE = 1, RESOURCE_NAME = 2, RESOURCE_COS = 3,
      RESOURCE_UNITS = 4, RESOURCE_UNIT_MASS = 5, RESOURCE_UNIT_VOLUME = 6,
      RESOURCE_PACKING_FACTOR = 7, RESOURCE_ENVIRONMENT = 8, RESOURCE_DESCRIPTION = 9;
  private static final int STATE_ID = 0, STATE_ELEMENT_ID = 1, STATE_NAME = 2, STATE_TYPE = 3,
      STATE_INITIAL = 4;

  private String filePath;

  /**
   * Instantiates a new data source with the passed file path.
   * 
   * @param filePath the file path
   */
  public Spreadsheet_2_5(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Instantiates a new data source. Does not initialize the file path.
   */
  public Spreadsheet_2_5() {}

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#deleteEdge(int)
   */
  public boolean deleteEdge(int tid) throws IOException {
    if (edgeLibrary.remove(loadEdge(tid))) {
      Workbook wb = readWorkbook();
      int rowNum = -1;
      for (Row row : wb.getSheetAt(EDGE_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(EDGE_ID).getNumericCellValue() == tid) {
          rowNum = row.getRowNum();
          break;
        }
      }

      if (rowNum > 0) {
        List<Integer> burnsToDelete = new ArrayList<Integer>();
        for (Row row : wb.getSheetAt(BURN_SHEET)) {
          if (row.getRowNum() == 0 || isRowEmpty(row))
            continue;
          if (row.getCell(BURN_EDGE_ID).getNumericCellValue() == tid) {
            burnsToDelete.add((int) row.getCell(BURN_ID).getNumericCellValue());
          }
        }
        for (Integer burnId : burnsToDelete)
          deleteBurn(wb, burnId);
        deleteRow(wb.getSheetAt(EDGE_SHEET), rowNum);
        System.out.println("Edge #" + tid + " deleted");
        writeWorkbook(wb);
        return true;
      } else {
        System.out.println("Edge #" + tid + " not found in database");
        return false; // edge not found
      }
    } else {
      System.out.println("Edge #" + tid + " not found in library");
      return false; // edge does not exist
    }
  }

  /**
   * Deletes a burn from a workbook.
   * 
   * @param wb the workbook
   * @param tid the type ID of the part to delete
   * 
   * @return true, if successful
   */
  private boolean deleteBurn(Workbook wb, int tid) {
    int rowNum = -1;
    for (Row row : wb.getSheetAt(BURN_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(BURN_ID).getNumericCellValue() == tid) {
        rowNum = row.getRowNum();
        break;
      }
    }
    if (rowNum > 0) {
      deleteRow(wb.getSheetAt(BURN_SHEET), rowNum);
      System.out.println("Burn #" + tid + " deleted");
      return true;
    } else {
      System.out.println("Burn #" + tid + " not found in database");
      return false; // burn not found
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#deleteElement(int)
   */
  public boolean deleteElement(int tid) throws IOException {
    if (elementPreviewLibrary.remove(loadElementPreview(tid))) {
      Workbook wb = readWorkbook();
      int rowNum = -1;
      for (Row row : wb.getSheetAt(ELEMENT_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(EDGE_ID).getNumericCellValue() == tid) {
          rowNum = row.getRowNum();
          break;
        }
      }
      if (rowNum > 0) {
        List<Integer> demandsToDelete = new ArrayList<Integer>();
        for (Row row : wb.getSheetAt(DEMAND_SHEET)) {
          if (row.getRowNum() == 0 || isRowEmpty(row))
            continue;
          if (row.getCell(DEMAND_CONTAINER_ID).getCellType() == CellType.NUMERIC
              && row.getCell(DEMAND_CONTAINER_ID).getNumericCellValue() == tid) {
            demandsToDelete.add((int) row.getCell(DEMAND_ID).getNumericCellValue());
          }
        }
        for (Integer demandId : demandsToDelete)
          deleteDemand(wb, demandId);

        List<Integer> partsToDelete = new ArrayList<Integer>();
        for (Row row : wb.getSheetAt(PART_SHEET)) {
          if (row.getRowNum() == 0 || isRowEmpty(row))
            continue;
          if (row.getCell(PART_ELEMENT_ID).getNumericCellValue() == tid) {
            partsToDelete.add((int) row.getCell(PART_ID).getNumericCellValue());
          }
        }
        for (Integer partId : partsToDelete)
          deletePart(wb, partId);

        List<Integer> statesToDelete = new ArrayList<Integer>();
        for (Row row : wb.getSheetAt(STATE_SHEET)) {
          if (row.getRowNum() == 0 || isRowEmpty(row))
            continue;
          if (row.getCell(STATE_ELEMENT_ID).getNumericCellValue() == tid) {
            statesToDelete.add((int) row.getCell(STATE_ID).getNumericCellValue());
          }
        }
        for (Integer stateId : statesToDelete)
          deleteState(wb, stateId);
        deleteRow(wb.getSheetAt(ELEMENT_SHEET), rowNum);
        System.out.println("Element #" + tid + " deleted");
        writeWorkbook(wb);
        return true;
      } else {
        System.out.println("Element #" + tid + " not found in database");
        return false; // element not found
      }
    } else {
      System.out.println("Element #" + tid + " not found in library");
      return false; // element does not exist
    }
  }

  /**
   * Deletes a part from a workbook. Note: part objects do not have a primary key so the type ID
   * parameter must be determined internally to the data source.
   * 
   * @param wb the workbook
   * @param tid the type ID of the part to delete
   * 
   * @return true, if successful
   */
  private boolean deletePart(Workbook wb, int tid) {
    int rowNum = -1;
    for (Row row : wb.getSheetAt(PART_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(PART_ID).getNumericCellValue() == tid) {
        rowNum = row.getRowNum();
        break;
      }
    }
    if (rowNum > 0) {
      deleteRow(wb.getSheetAt(PART_SHEET), rowNum);
      System.out.println("Part #" + tid + " deleted");
      return true;
    } else {
      System.out.println("Part #" + tid + " not found in database");
      return false; // part not found
    }
  }

  /**
   * Deletes a state from a workbook. Also deletes any associated demand models.
   * 
   * @param wb the workbook
   * @param tid the type ID of the state to delete
   * 
   * @return true, if successful
   */
  private boolean deleteState(Workbook wb, int tid) {
    int rowNum = -1;
    for (Row row : wb.getSheetAt(STATE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(STATE_ID).getNumericCellValue() == tid) {
        rowNum = row.getRowNum();
        break;
      }
    }
    if (rowNum > 0) {
      List<Integer> modelsToDelete = new ArrayList<Integer>();
      for (Row row : wb.getSheetAt(MODEL_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(MODEL_STATE_ID).getNumericCellValue() == tid) {
          modelsToDelete.add((int) row.getCell(MODEL_ID).getNumericCellValue());
        }
      }
      for (Integer modelId : modelsToDelete)
        deleteModel(wb, modelId);
      deleteRow(wb.getSheetAt(STATE_SHEET), rowNum);
      System.out.println("State #" + tid + " deleted");
      return true;
    } else {
      System.out.println("State #" + tid + " not found in database");
      return false; // state not found
    }
  }

  /**
   * Deletes a demand model from a workbook. Also deletes any associated demands. Note: demand
   * objects do not have a primary key so the type ID parameter must be determined internally to the
   * data source.
   * 
   * @param wb the workbook
   * @param tid the type ID of the demand model to delete
   * 
   * @return true, if successful
   */
  private boolean deleteModel(Workbook wb, int tid) {
    int rowNum = -1;
    for (Row row : wb.getSheetAt(MODEL_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(MODEL_ID).getNumericCellValue() == tid) {
        rowNum = row.getRowNum();
        break;
      }
    }
    if (rowNum > 0) {
      List<Integer> demandsToDelete = new ArrayList<Integer>();
      for (Row row : wb.getSheetAt(DEMAND_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(DEMAND_MODEL_ID).getCellType() == CellType.NUMERIC
            && row.getCell(DEMAND_MODEL_ID).getNumericCellValue() == tid) {
          demandsToDelete.add((int) row.getCell(DEMAND_ID).getNumericCellValue());
        }
      }
      for (Integer demandId : demandsToDelete)
        deleteDemand(wb, demandId);
      deleteRow(wb.getSheetAt(MODEL_SHEET), rowNum);
      System.out.println("Model #" + tid + " deleted");
      return true;
    } else {
      System.out.println("Model #" + tid + " not found in database");
      return false; // model not found
    }
  }

  /**
   * Deletes a demand.
   * 
   * @param wb the workbook
   * @param tid the type ID of the demand to delete
   * 
   * @return true, if successful
   */
  private boolean deleteDemand(Workbook wb, int tid) {
    int rowNum = -1;
    for (Row row : wb.getSheetAt(DEMAND_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(DEMAND_ID).getNumericCellValue() == tid) {
        rowNum = row.getRowNum();
        break;
      }
    }
    if (rowNum > 0) {
      deleteRow(wb.getSheetAt(DEMAND_SHEET), rowNum);
      System.out.println("Demand #" + tid + " deleted");
      return true;
    } else {
      System.out.println("Demand #" + tid + " not found in database");
      return false; // demand not found
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#deleteNode(int)
   */
  public boolean deleteNode(int tid) throws IOException {
    Workbook wb = readWorkbook();
    for (Row row : wb.getSheetAt(EDGE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(EDGE_ORIGIN_ID).getNumericCellValue() == tid
          || row.getCell(EDGE_DESTINATION_ID).getNumericCellValue() == tid) {
        System.out.println("Node #" + tid + " is used in edges");
        return false; // node is used as edge origin or destination
      }
    }
    if (nodeLibrary.remove(loadNode(tid))) {
      int rowNum = -1;
      for (Row row : wb.getSheetAt(NODE_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(NODE_ID).getNumericCellValue() == tid) {
          rowNum = row.getRowNum();
          break;
        }
      }
      if (rowNum > 0) {
        deleteRow(wb.getSheetAt(NODE_SHEET), rowNum);
        System.out.println("Node #" + tid + " deleted");
        writeWorkbook(wb);
        return true;
      } else {
        System.out.println("Node #" + tid + " not found in database");
        return false; // node not found
      }
    } else {
      System.out.println("Node #" + tid + " not found in library");
      return false; // node does not exist
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#deleteResource(int)
   */
  public boolean deleteResource(int tid) throws IOException {
    Workbook wb = readWorkbook();
    for (Row row : wb.getSheetAt(ELEMENT_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if ((row.getCell(ELEMENT_OMS_ID).getCellType() == CellType.NUMERIC
          && row.getCell(ELEMENT_OMS_ID).getNumericCellValue() == tid)
          || (row.getCell(ELEMENT_RCS_ID).getCellType() == CellType.NUMERIC
              && row.getCell(ELEMENT_RCS_ID).getNumericCellValue() == tid)
          || (row.getCell(ELEMENT_FUEL_ID).getCellType() != CellType.NUMERIC
              && row.getCell(ELEMENT_FUEL_ID).getNumericCellValue() == tid)) {
        System.out.println("Resource #" + tid + " is used in elements");
        return false; // resource is used in elements
      }
    }
    for (Row row : wb.getSheetAt(PART_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(PART_RESOURCE_ID).getCellType() == CellType.NUMERIC
          && row.getCell(PART_RESOURCE_ID).getNumericCellValue() == tid) {
        System.out.println("Resource #" + tid + " is used in applications");
        return false; // resource is used in part applications
      }
    }
    for (Row row : wb.getSheetAt(DEMAND_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(DEMAND_RESOURCE_ID).getCellType() == CellType.NUMERIC
          && row.getCell(DEMAND_RESOURCE_ID).getNumericCellValue() == tid) {
        System.out.println("Resource #" + tid + " is used in demands");
        return false; // resource is used in demands
      }
    }
    if (resourceTypeLibrary.remove(loadResource(tid))) {
      int rowNum = -1;
      for (Row row : wb.getSheetAt(RESOURCE_SHEET)) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(RESOURCE_ID).getNumericCellValue() == tid) {
          rowNum = row.getRowNum();
          break;
        }
      }
      if (rowNum > 0) {
        deleteRow(wb.getSheetAt(RESOURCE_SHEET), rowNum);
        System.out.println("Resource #" + tid + " deleted");
        writeWorkbook(wb);
        return true;
      } else {
        System.out.println("Resource #" + tid + " not found in database");
        return false; // resource not found
      }
    } else {
      System.out.println("Resource #" + tid + " not found in library");
      return false; // resource does not exist
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#getDataSourceType()
   */
  public DataSourceType getDataSourceType() {
    return DataSourceType.EXCEL_2_5;
  }

  /**
   * Gets the file path.
   * 
   * @return the file path
   */
  public String getFilePath() {
    return filePath;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#getName()
   */
  public String getName() {
    if (filePath == null || filePath.length() == 0)
      return getDataSourceType().getName();
    int lastIndex = Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\"));
    return filePath.substring(lastIndex + 1, filePath.length());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#loadEdgeLibrary()
   */
  public void loadEdgeLibrary() throws IOException {
    loadEdgeLibrary(readWorkbook());
  }

  /**
   * Load edge library from a workbook
   * 
   * @param wb the workbook
   */
  private void loadEdgeLibrary(Workbook wb) {
    Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
    for (Edge edge : getEdgeLibrary()) {
      // mark all edges as unfound
      libraryContents.put(edge.getTid(), false);
    }
    for (Row row : wb.getSheetAt(EDGE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        Edge e = loadEdge((int) row.getCell(EDGE_ID).getNumericCellValue());
        String edgeType = row.getCell(EDGE_TYPE).getStringCellValue().toLowerCase();
        if (e == null || !e.getEdgeType().getName().toLowerCase().contains(edgeType)) {
          edgeLibrary.remove(e);
          if (e != null)
            libraryContents.remove(e.getTid());
          // create new edge and add to library
          if (EdgeType.SURFACE.getName().toLowerCase().contains(edgeType)) {
            e = new SurfaceEdge();
          } else if (EdgeType.SPACE.getName().toLowerCase().contains(edgeType)) {
            e = new SpaceEdge();
          } else if (EdgeType.FLIGHT.getName().toLowerCase().contains(edgeType)) {
            e = new FlightEdge();
          } else {
            throw new Exception("Unknown Edge Type");
          }
          edgeLibrary.add(e);
        } else {
          // mark edge as found
          libraryContents.put(e.getTid(), true);
        }
        e.setTid((int) row.getCell(EDGE_ID).getNumericCellValue());
        e.setName(row.getCell(EDGE_NAME).getStringCellValue());
        e.setOrigin(loadNode((int) row.getCell(EDGE_ORIGIN_ID).getNumericCellValue()));
        e.setDestination(loadNode((int) row.getCell(EDGE_DESTINATION_ID).getNumericCellValue()));
        e.setDescription(row.getCell(EDGE_DESCRIPTION).toString());
        if (EdgeType.SURFACE.getName().toLowerCase().contains(edgeType)) {
          ((SurfaceEdge) e).setDistance(row.getCell(EDGE_DISTANCE).getNumericCellValue());
        } else if (EdgeType.SPACE.getName().toLowerCase().contains(edgeType)) {
          ((SpaceEdge) e).setDuration(row.getCell(EDGE_DURATION).getNumericCellValue());
          loadBurns(wb, (SpaceEdge) e);
        } else if (EdgeType.FLIGHT.getName().toLowerCase().contains(edgeType)) {
          ((FlightEdge) e).setDuration(row.getCell(EDGE_DURATION).getNumericCellValue());
          ((FlightEdge) e).setMaxCrewSize((int) row.getCell(EDGE_MAX_CREW).getNumericCellValue());
          ((FlightEdge) e).setMaxCargoMass(row.getCell(EDGE_MAX_CARGO).getNumericCellValue());
        } else {
          throw new Exception("Unknown Edge Type");
        }
      } catch (Exception ex) {
        // error loading edge
        ex.printStackTrace();
      }
    }
    for (Integer i : libraryContents.keySet()) {
      if (!libraryContents.get(i)) {
        // remove all unfound edges
        getEdgeLibrary().remove(loadEdge(i));
      }
    }
  }

  /**
   * Load burns for a specific space edge.
   * 
   * @param wb the workbook
   * @param edge the space edge
   */
  private void loadBurns(Workbook wb, SpaceEdge edge) {
    edge.getBurns().clear();
    for (Row row : wb.getSheetAt(BURN_SHEET)) {
      try {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(BURN_EDGE_ID).getNumericCellValue() == edge.getTid()) {
          Burn b = new Burn();
          b.setTid((int) row.getCell(BURN_ID).getNumericCellValue());
          b.setTime(row.getCell(BURN_TIME).getNumericCellValue());
          b.setBurnType(BurnType.getInstance(row.getCell(BURN_TYPE).getStringCellValue()));
          b.setDeltaV(row.getCell(BURN_DELTA_V).getNumericCellValue());
          // TODO problems if burns are "out of order" in database... make sure this gets
          // fixed for the online database
          while (edge.getBurns().size() <= (int) row.getCell(BURN_ORDER).getNumericCellValue())
            edge.getBurns().add(new Burn());
          edge.getBurns().set((int) row.getCell(BURN_ORDER).getNumericCellValue(), b);
        }
      } catch (Exception ex) {
        // error loading burn
        ex.printStackTrace();
      }
    }
  }

  /**
   * Loads a resource from a cell reference, allowing generic resource environment encoding.
   *
   * @param cell the cell
   * @return the i resource
   */
  private I_Resource loadResource(Cell cell) {
    if (cell.getCellType() == CellType.NUMERIC) {
      return loadResource((int) cell.getNumericCellValue());
    }
    int cos = Integer.parseInt(cell.getStringCellValue().substring(1));
    if (cell.getStringCellValue().substring(0, 1).toLowerCase().equals("p")) {
      return new GenericResource(ClassOfSupply.getInstance(cos), Environment.PRESSURIZED);
    } else {
      return new GenericResource(ClassOfSupply.getInstance(cos), Environment.UNPRESSURIZED);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#loadElement(int)
   */
  public I_Element loadElement(int tid) throws IOException {
    Workbook wb = readWorkbook();
    for (Row row : wb.getSheetAt(ELEMENT_SHEET)) {
      try {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(ELEMENT_ID).getNumericCellValue() == tid) {
          I_Element e = null;
          String elementType = row.getCell(ELEMENT_TYPE).getStringCellValue().toLowerCase();
          if (ElementType.ELEMENT.getName().toLowerCase().contains(elementType)) {
            e = new Element();
          } else if (ElementType.RESOURCE_CONTAINER.getName().toLowerCase().contains(elementType)) {
            e = new ResourceContainer();
            ((ResourceContainer) e)
                .setMaxCargoMass(row.getCell(ELEMENT_CARGO_MASS).getNumericCellValue());
            ((ResourceContainer) e)
                .setMaxCargoVolume(row.getCell(ELEMENT_CARGO_VOLUME).getNumericCellValue());
            ((ResourceContainer) e).setCargoEnvironment(Environment
                .getInstance(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue()));
          } else if (ElementType.CREW_MEMBER.getName().toLowerCase().contains(elementType)) {
            e = new CrewMember();
            ((CrewMember) e).setAvailableTimeFraction(
                row.getCell(ELEMENT_ACTIVE_FRACTION).getNumericCellValue());
          } else if (ElementType.CARRIER.getName().toLowerCase().contains(elementType)) {
            e = new Carrier();
            ((Carrier) e).setMaxCrewSize((int) row.getCell(ELEMENT_MAX_CREW).getNumericCellValue());
            ((Carrier) e).setMaxCargoMass(row.getCell(ELEMENT_CARGO_MASS).getNumericCellValue());
            ((Carrier) e)
                .setMaxCargoVolume(row.getCell(ELEMENT_CARGO_VOLUME).getNumericCellValue());
            ((Carrier) e).setCargoEnvironment(Environment
                .getInstance(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue()));
          } else if (ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(elementType)) {
            e = new SurfaceVehicle();
            ((SurfaceVehicle) e)
                .setMaxCrewSize((int) row.getCell(ELEMENT_MAX_CREW).getNumericCellValue());
            ((SurfaceVehicle) e)
                .setMaxCargoMass(row.getCell(ELEMENT_CARGO_MASS).getNumericCellValue());
            ((SurfaceVehicle) e)
                .setMaxCargoVolume(row.getCell(ELEMENT_CARGO_VOLUME).getNumericCellValue());
            ((SurfaceVehicle) e).setCargoEnvironment(Environment
                .getInstance(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue()));
            ((SurfaceVehicle) e).setMaxSpeed(row.getCell(ELEMENT_MAX_SPEED).getNumericCellValue());
            ((SurfaceVehicle) e).getFuelTank()
                .setMaxAmount(row.getCell(ELEMENT_MAX_FUEL).getNumericCellValue());
            ((SurfaceVehicle) e).getFuelTank()
                .setAmount(row.getCell(ELEMENT_MAX_FUEL).getNumericCellValue());
            ((SurfaceVehicle) e).getFuelTank()
                .setResource(loadResource(row.getCell(ELEMENT_FUEL_ID)));
          } else if (ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(elementType)) {
            e = new PropulsiveVehicle();
            ((PropulsiveVehicle) e)
                .setMaxCrewSize((int) row.getCell(ELEMENT_MAX_CREW).getNumericCellValue());
            ((PropulsiveVehicle) e)
                .setMaxCargoMass(row.getCell(ELEMENT_CARGO_MASS).getNumericCellValue());
            ((PropulsiveVehicle) e)
                .setMaxCargoVolume(row.getCell(ELEMENT_CARGO_VOLUME).getNumericCellValue());
            ((PropulsiveVehicle) e).setCargoEnvironment(Environment
                .getInstance(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue()));
            ((PropulsiveVehicle) e).setOmsIsp(row.getCell(ELEMENT_OMS_ISP).getNumericCellValue());
            ((PropulsiveVehicle) e).setRcsIsp(row.getCell(ELEMENT_RCS_ISP).getNumericCellValue());
            if (((PropulsiveVehicle) e).getOmsIsp() > 0) {
              ((PropulsiveVehicle) e).getOmsFuelTank()
                  .setMaxAmount(row.getCell(ELEMENT_MAX_OMS).getNumericCellValue());
              ((PropulsiveVehicle) e).getOmsFuelTank()
                  .setAmount(row.getCell(ELEMENT_MAX_OMS).getNumericCellValue());
              ((PropulsiveVehicle) e).getOmsFuelTank()
                  .setResource(loadResource(row.getCell(ELEMENT_OMS_ID)));
            } else {
              ((PropulsiveVehicle) e).setOmsFuelTank(null);
            }
            if (((PropulsiveVehicle) e).getRcsIsp() > 0
                && row.getCell(ELEMENT_MAX_RCS).getNumericCellValue() > 0) {
              ((PropulsiveVehicle) e).getRcsFuelTank()
                  .setMaxAmount(row.getCell(ELEMENT_MAX_RCS).getNumericCellValue());
              ((PropulsiveVehicle) e).getRcsFuelTank()
                  .setAmount(row.getCell(ELEMENT_MAX_RCS).getNumericCellValue());
              ((PropulsiveVehicle) e).getRcsFuelTank()
                  .setResource(loadResource(row.getCell(ELEMENT_RCS_ID)));
            } else if (((PropulsiveVehicle) e).getRcsIsp() > 0) {
              ((PropulsiveVehicle) e).getContents()
                  .remove(((PropulsiveVehicle) e).getRcsFuelTank());
              ((PropulsiveVehicle) e).setRcsFuelTank(((PropulsiveVehicle) e).getOmsFuelTank());
            } else {
              ((PropulsiveVehicle) e).setRcsFuelTank(null);
            }
          } else {
            throw new Exception("Unknown Element Type");
          }
          e.setTid((int) row.getCell(ELEMENT_ID).getNumericCellValue());
          e.setName(row.getCell(ELEMENT_NAME).getStringCellValue());
          e.setClassOfSupply(
              ClassOfSupply.getInstance((int) row.getCell(ELEMENT_COS).getNumericCellValue()));
          e.setEnvironment(
              Environment.getInstance(row.getCell(ELEMENT_ENVIRONMENT).getStringCellValue()));
          e.setAccommodationMass(row.getCell(ELEMENT_ACCOMMODATION_MASS).getNumericCellValue());
          e.setMass(row.getCell(ELEMENT_MASS).getNumericCellValue());
          e.setVolume(row.getCell(ELEMENT_VOLUME).getNumericCellValue());
          e.setDescription(row.getCell(ELEMENT_DESCRIPTION).toString());
          if (ElementIcon.getInstance(row.getCell(ELEMENT_ICON).toString()) != null) {
            e.setIconType(ElementIcon.getInstance(row.getCell(ELEMENT_ICON).toString()));
          }
          if (e.getElementType() == ElementType.RESOURCE_CONTAINER) {
            ((I_ResourceContainer) e).getContents().clear();
            for (Row r : wb.getSheetAt(DEMAND_SHEET)) {
              if (r.getRowNum() == 0 || isRowEmpty(r))
                continue;
              if (r.getCell(DEMAND_CONTAINER_ID).getNumericCellValue() == e.getTid()) {
                ((ResourceContainer) e).getContents().put(
                    loadResource(r.getCell(DEMAND_RESOURCE_ID)),
                    r.getCell(DEMAND_AMOUNT).getNumericCellValue());
              }
            }
          }
          loadParts(wb, e);
          loadStates(wb, e);
          return e;
        }
      } catch (Exception ex) {
        // error reading element
        ex.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Load the parts for a specific element.
   * 
   * @param wb the workbook
   * @param element the element
   */
  private void loadParts(Workbook wb, I_Element element) {
    element.getParts().clear();
    for (Row row : wb.getSheetAt(PART_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        if (row.getCell(PART_ELEMENT_ID).getNumericCellValue() == element.getTid()) {
          PartApplication partApp = new PartApplication();
          partApp.setTid((int) row.getCell(PART_ID).getNumericCellValue());
          partApp.setPart((Item) loadResource(row.getCell(PART_RESOURCE_ID)));
          partApp.setQuantity((int) row.getCell(PART_QUANTITY).getNumericCellValue());
          partApp.setDutyCycle(row.getCell(PART_DUTY_CYCLE).getNumericCellValue());
          partApp.setMeanTimeToFailure(row.getCell(PART_MTTF).getNumericCellValue());
          partApp.setMeanTimeToRepair(row.getCell(PART_MTTR).getNumericCellValue());
          partApp.setMassToRepair(row.getCell(PART_REPAIR_MASS).getNumericCellValue());
          element.getParts().add(partApp);
        }
      } catch (Exception ex) {
        // error reading part
        ex.printStackTrace();
      }
    }
  }

  private void loadStates(Workbook wb, I_Element element) {
    element.getStates().clear();
    for (Row row : wb.getSheetAt(STATE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        if (row.getCell(STATE_ELEMENT_ID).getNumericCellValue() == element.getTid()) {
          State state = new State();
          state.setTid((int) row.getCell(STATE_ID).getNumericCellValue());
          state.setName(row.getCell(STATE_NAME).getStringCellValue());
          state.setStateType(StateType.getInstance(row.getCell(STATE_TYPE).getStringCellValue()));
          loadModels(wb, element, state);
          element.getStates().add(state);
          if (row.getCell(STATE_INITIAL).getBooleanCellValue())
            element.setCurrentState(state);
        }
      } catch (Exception ex) {
        // error reading state
        ex.printStackTrace();
      }
    }
  }

  /**
   * Loads the demand models for a given element and state.
   * 
   * @param wb the workbook
   * @param element the element
   * @param state the state
   */
  private void loadModels(Workbook wb, I_Element element, I_State state) {
    for (Row row : wb.getSheetAt(MODEL_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        String modelType = row.getCell(MODEL_TYPE).getStringCellValue().toLowerCase();
        if (row.getCell(MODEL_STATE_ID).getNumericCellValue() == state.getTid()) {
          if (DemandModelType.TIMED_IMPULSE.getName().toLowerCase().contains(modelType)) {
            TimedImpulseDemandModel model = new TimedImpulseDemandModel();
            model.setTid((int) row.getCell(MODEL_ID).getNumericCellValue());
            model.setName(row.getCell(MODEL_NAME).getStringCellValue());
            for (Row r : wb.getSheetAt(DEMAND_SHEET)) {
              if (r.getRowNum() == 0 || isRowEmpty(r))
                continue;
              if (r.getCell(DEMAND_MODEL_ID).getNumericCellValue() == model.getTid()) {
                Demand demand = new Demand();
                demand.setResource(loadResource(r.getCell(DEMAND_RESOURCE_ID)));
                demand.setAmount(r.getCell(DEMAND_AMOUNT).getNumericCellValue());
                model.getDemands().add(demand);
              }
            }
            state.getDemandModels().add(model);
          } else if (DemandModelType.RATED.getName().toLowerCase().contains(modelType)) {
            RatedDemandModel model = new RatedDemandModel();
            model.setTid((int) row.getCell(MODEL_ID).getNumericCellValue());
            model.setName(row.getCell(MODEL_NAME).getStringCellValue());
            for (Row r : wb.getSheetAt(DEMAND_SHEET)) {
              if (r.getRowNum() == 0 || isRowEmpty(r))
                continue;
              if (r.getCell(DEMAND_MODEL_ID).getNumericCellValue() == model.getTid()) {
                Demand demand = new Demand();
                demand.setResource(loadResource(r.getCell(DEMAND_RESOURCE_ID)));
                demand.setAmount(r.getCell(DEMAND_AMOUNT).getNumericCellValue());
                model.getDemandRates().add(demand);
              }
            }
            state.getDemandModels().add(model);
          } else if (DemandModelType.SPARING_BY_MASS.getName().toLowerCase().contains(modelType)) {
            SparingByMassDemandModel model = new SparingByMassDemandModel(element);
            model.setTid((int) row.getCell(MODEL_ID).getNumericCellValue());
            model.setName(row.getCell(MODEL_NAME).getStringCellValue());
            model.setElement(element);
            model.setPartsListEnabled(row.getCell(MODEL_PARTS_LIST).getBooleanCellValue());
            model.setUnpressurizedSparesRate(row.getCell(MODEL_UNPRESS_RATE).getNumericCellValue());
            model.setPressurizedSparesRate(row.getCell(MODEL_PRESS_RATE).getNumericCellValue());
            state.getDemandModels().add(model);
          } else {
            throw new Exception("Unknown Demand Model Type");
          }
        }
      } catch (Exception ex) {
        // error reading element
        ex.printStackTrace();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#loadElementLibrary()
   */
  public void loadElementLibrary() throws IOException {
    loadElementLibrary(readWorkbook());
  }

  /**
   * Loads the element library from a workbook.
   * 
   * @param wb the workbook
   */
  private void loadElementLibrary(Workbook wb) {
    getElementPreviewLibrary().clear();
    for (Row row : wb.getSheetAt(ELEMENT_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        ElementPreview e = null;
        String elementType = row.getCell(ELEMENT_TYPE).getStringCellValue().toLowerCase();
        String name = row.getCell(ELEMENT_NAME).getStringCellValue();
        ElementIcon icon = ElementIcon.getInstance(row.getCell(ELEMENT_ICON).getStringCellValue());
        int tid = (int) row.getCell(ELEMENT_ID).getNumericCellValue();
        // create new element preview and add to library
        if (ElementType.ELEMENT.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.ELEMENT, icon);
        } else if (ElementType.RESOURCE_CONTAINER.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.RESOURCE_CONTAINER, icon);
        } else if (ElementType.CREW_MEMBER.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.CREW_MEMBER, icon);
        } else if (ElementType.CARRIER.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.CARRIER, icon);
        } else if (ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.SURFACE_VEHICLE, icon);
        } else if (ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(elementType)) {
          e = new ElementPreview(tid, name, ElementType.PROPULSIVE_VEHICLE, icon);
        } else {
          throw new Exception("Unknown Element Type");
        }
        elementPreviewLibrary.add(e);
      } catch (Exception ex) {
        // error loading edge
        ex.printStackTrace();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#loadNodeLibrary()
   */
  public void loadNodeLibrary() throws IOException {
    loadNodeLibrary(readWorkbook());
  }

  /**
   * Load the node library from a workbook.
   * 
   * @param wb the workbook
   */
  private void loadNodeLibrary(Workbook wb) {
    Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
    for (Node node : getNodeLibrary()) {
      // mark all nodes as unfound
      libraryContents.put(node.getTid(), false);
    }
    for (Row row : wb.getSheetAt(NODE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        Node n = loadNode((int) row.getCell(NODE_ID).getNumericCellValue());
        String nodeType = row.getCell(NODE_TYPE).getStringCellValue().toLowerCase();
        if (n == null || !n.getNodeType().getName().toLowerCase().contains(nodeType)) {
          nodeLibrary.remove(n);
          if (n != null)
            libraryContents.remove(n.getTid());
          // create new node and add to library
          if (NodeType.SURFACE.getName().toLowerCase().contains(nodeType)) {
            n = new SurfaceNode();
          } else if (NodeType.ORBITAL.getName().toLowerCase().contains(nodeType)) {
            n = new OrbitalNode();
          } else if (NodeType.LAGRANGE.getName().toLowerCase().contains(nodeType)) {
            n = new LagrangeNode();
          } else {
            throw new Exception("Unknown Node Type");
          }
          nodeLibrary.add(n);
        } else {
          // mark node as found
          libraryContents.put(n.getTid(), true);
        }
        n.setTid((int) row.getCell(NODE_ID).getNumericCellValue());
        n.setName(row.getCell(NODE_NAME).getStringCellValue());
        n.setBody(Body.getInstance(row.getCell(NODE_BODY_1).getStringCellValue()));
        n.setDescription(row.getCell(NODE_DESCRIPTION).toString());
        if (NodeType.SURFACE.getName().toLowerCase().contains(nodeType)) {
          ((SurfaceNode) n).setLatitude(row.getCell(NODE_LATITUDE).getNumericCellValue());
          ((SurfaceNode) n).setLongitude(row.getCell(NODE_LONGITUDE).getNumericCellValue());
        } else if (NodeType.ORBITAL.getName().toLowerCase().contains(nodeType)) {
          ((OrbitalNode) n).setApoapsis(row.getCell(NODE_APOAPSIS).getNumericCellValue());
          ((OrbitalNode) n).setPeriapsis(row.getCell(NODE_PERIAPSIS).getNumericCellValue());
          ((OrbitalNode) n).setInclination(row.getCell(NODE_INCLINATION).getNumericCellValue());
        } else if (NodeType.LAGRANGE.getName().toLowerCase().contains(nodeType)) {
          ((LagrangeNode) n)
              .setMinorBody(Body.getInstance(row.getCell(NODE_BODY_2).getStringCellValue()));
          ((LagrangeNode) n).setNumber((int) row.getCell(NODE_LP_NUMBER).getNumericCellValue());
        } else {
          throw new Exception("Unknown Node Type");
        }
      } catch (Exception ex) {
        // error loading node
        ex.printStackTrace();
      }
    }
    for (Integer i : libraryContents.keySet()) {
      if (!libraryContents.get(i)) {
        // remove all unfound nodes
        getNodeLibrary().remove(loadNode(i));
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#loadResourceLibrary()
   */
  public void loadResourceLibrary() throws IOException {
    loadResourceLibrary(readWorkbook());
  }

  /**
   * Loads the resource library from a workbook.
   * 
   * @param wb the workbook
   */
  private void loadResourceLibrary(Workbook wb) {
    Map<Integer, Boolean> libraryContents = new HashMap<Integer, Boolean>();
    for (I_Resource resource : getResourceLibrary()) {
      // mark all resources as not found
      libraryContents.put(resource.getTid(), false);
    }
    for (Row row : wb.getSheetAt(RESOURCE_SHEET)) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      try {
        I_Resource r = loadResource(row.getCell(RESOURCE_ID));
        String resourceType = row.getCell(RESOURCE_TYPE).getStringCellValue().toLowerCase();
        if (r == null || !r.getResourceType().getName().toLowerCase().contains(resourceType)) {
          resourceTypeLibrary.remove(r);
          if (r != null)
            libraryContents.remove(r.getTid());
          // create new resource and add to library
          if (ResourceType.RESOURCE.getName().toLowerCase().contains(resourceType)) {
            r = new Resource();
          } else if (ResourceType.ITEM.getName().toLowerCase().contains(resourceType)) {
            r = new Item();
          } else {
            throw new Exception("Unknown Resource Type");
          }
          resourceTypeLibrary.add(r);
        } else {
          // mark resource as found
          libraryContents.put(r.getTid(), true);
        }
        r.setTid((int) row.getCell(RESOURCE_ID).getNumericCellValue());
        r.setName(row.getCell(RESOURCE_NAME).getStringCellValue());
        r.setClassOfSupply(
            ClassOfSupply.getInstance((int) row.getCell(RESOURCE_COS).getNumericCellValue()));
        r.setUnits(row.getCell(RESOURCE_UNITS).getStringCellValue());
        r.setUnitMass(row.getCell(RESOURCE_UNIT_MASS).getNumericCellValue());
        r.setUnitVolume(row.getCell(RESOURCE_UNIT_VOLUME).getNumericCellValue());
        r.setPackingFactor(row.getCell(RESOURCE_PACKING_FACTOR).getNumericCellValue());
        r.setEnvironment(
            Environment.getInstance(row.getCell(RESOURCE_ENVIRONMENT).getStringCellValue()));
        r.setDescription(row.getCell(RESOURCE_DESCRIPTION).toString());
      } catch (Exception ex) {
        // error loading resource
        ex.printStackTrace();
      }
    }
    for (Integer i : libraryContents.keySet()) {
      if (!libraryContents.get(i)) {
        // remove all unfound resources
        getResourceLibrary().remove(loadResource(i));
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#saveEdge(edu.mit.spacenet.domain.network.edge.Edge)
   */
  public void saveEdge(Edge edge) throws IOException {
    Workbook wb = readWorkbook();
    Sheet sheet = wb.getSheetAt(EDGE_SHEET);
    int rowNum = -1, maxTid = 0;
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(EDGE_ID).getNumericCellValue() == edge.getTid()) {
        rowNum = row.getRowNum();
      }
      maxTid = (int) Math.max(maxTid, row.getCell(EDGE_ID).getNumericCellValue());
    }
    Row row = null;
    if (rowNum < 0) {
      // edge not found in database -- create new entry
      rowNum = getLastNonEmptyRow(sheet) + 1;
      edge.setTid(maxTid + 1);
      edgeLibrary.add(edge);
      row = sheet.getRow(rowNum);
      if (row == null) {
        row = sheet.createRow(rowNum);
      }
    } else {
      row = sheet.getRow(rowNum);
    }
    row.getCell(EDGE_ID).setCellValue(edge.getTid());
    row.getCell(EDGE_TYPE).setCellValue(edge.getEdgeType().getName());
    row.getCell(EDGE_NAME).setCellValue(edge.getName());
    row.getCell(EDGE_ORIGIN_ID).setCellValue(edge.getOrigin().getTid());
    row.getCell(EDGE_DESTINATION_ID).setCellValue(edge.getDestination().getTid());
    row.getCell(EDGE_DESCRIPTION).setCellValue(edge.getDescription());
    if (edge.getEdgeType() == EdgeType.SPACE) {
      row.getCell(EDGE_DURATION).setCellValue(((SpaceEdge) edge).getDuration());
      saveBurns(wb, (SpaceEdge) edge);
    } else if (edge.getEdgeType() == EdgeType.SURFACE) {
      row.getCell(EDGE_DISTANCE).setCellValue(((SurfaceEdge) edge).getDistance());
    } else if (edge.getEdgeType() == EdgeType.FLIGHT) {
      row.getCell(EDGE_DURATION).setCellValue(((FlightEdge) edge).getDuration());
      row.getCell(EDGE_MAX_CREW).setCellValue(((FlightEdge) edge).getMaxCrewSize());
      row.getCell(EDGE_MAX_CARGO).setCellValue(((FlightEdge) edge).getMaxCargoMass());
    }
    writeWorkbook(wb);
  }

  /**
   * Saves the burns associated with an edge. Removed burns will be deleted from the data source,
   * changed burns will be updated, and added burns will be assigned a new TID and inserted.
   * 
   * @param wb the workbook
   * @param edge the edge
   */
  private void saveBurns(Workbook wb, SpaceEdge edge) {
    Sheet sheet = wb.getSheetAt(BURN_SHEET);
    List<Integer> burnsToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(BURN_EDGE_ID).getNumericCellValue() == edge.getTid()) {
        boolean isBurnFound = false;
        for (Burn burn : edge.getBurns()) {
          if (burn.getTid() == row.getCell(BURN_ID).getNumericCellValue()) {
            isBurnFound = true;
            break;
          }
        }
        if (!isBurnFound)
          burnsToDelete.add((int) row.getCell(BURN_ID).getNumericCellValue());
      }
    }
    for (Integer i : burnsToDelete) {
      deleteBurn(wb, i);
    }
    for (Burn burn : edge.getBurns()) {
      int rowNum = -1, maxTid = 0;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(BURN_ID).getNumericCellValue() == burn.getTid()) {
          rowNum = row.getRowNum();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(BURN_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // burn not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        burn.setTid(maxTid + 1);
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(BURN_ID).setCellValue(burn.getTid());
      row.getCell(BURN_EDGE_ID).setCellValue(edge.getTid());
      row.getCell(BURN_TIME).setCellValue(burn.getTime());
      row.getCell(BURN_ORDER).setCellValue(edge.getBurns().indexOf(burn));
      row.getCell(BURN_TYPE).setCellValue(burn.getBurnType().getName());
      row.getCell(BURN_DELTA_V).setCellValue(burn.getDeltaV());
    }
  }

  /**
   * Gets the generic resource encoding.
   *
   * @param resource the resource
   * @return the generic resource encoding
   */
  private String getGenericResourceEncoding(I_Resource resource) {
    String env = resource.getEnvironment() == Environment.PRESSURIZED ? "P" : "U";
    return env + -resource.getTid();
  }

  /**
   * Save resource, allowing for generic resource environment encoding.
   *
   * @param cell the cell
   * @param resource the resource
   */
  private void saveResource(Cell cell, I_Resource resource) {
    if (resource.getTid() > 0) {
      cell.setCellValue(resource.getTid());
    } else {
      cell.setCellValue(getGenericResourceEncoding(resource));
    }
  }

  /**
   * Checks if is cell is a resource match.
   *
   * @param cell the cell
   * @param resource the resource
   * @return true, if is cell resource match
   */
  private boolean isCellResourceMatch(Cell cell, I_Resource resource) {
    return (cell.getCellType() == CellType.NUMERIC
        && resource.getTid() == cell.getNumericCellValue())
        || (cell.getCellType() == CellType.STRING
            && getGenericResourceEncoding(resource).equals(cell.getStringCellValue()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#saveElement(edu.mit.spacenet.domain.element.I_Element)
   */
  public void saveElement(I_Element element) throws IOException {
    Workbook wb = readWorkbook();
    Sheet sheet = wb.getSheetAt(ELEMENT_SHEET);
    int rowNum = -1, maxTid = 0;
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(ELEMENT_ID).getNumericCellValue() == element.getTid()) {
        rowNum = row.getRowNum();
      }
      maxTid = (int) Math.max(maxTid, row.getCell(ELEMENT_ID).getNumericCellValue());
    }
    Row row = null;
    if (rowNum < 0) {
      // element not found in database -- create new entry
      rowNum = getLastNonEmptyRow(sheet) + 1;
      element.setTid(maxTid + 1);
      elementPreviewLibrary.add(new ElementPreview(element.getTid(), element.getName(),
          element.getElementType(), element.getIconType()));
      row = sheet.getRow(rowNum);
      if (row == null) {
        row = sheet.createRow(rowNum);
      }
    } else {
      row = sheet.getRow(rowNum);
      elementPreviewLibrary.remove(loadElementPreview(element.getTid()));
      elementPreviewLibrary.add(new ElementPreview(element.getTid(), element.getName(),
          element.getElementType(), element.getIconType()));
    }
    row.getCell(ELEMENT_ID).setCellValue(element.getTid());
    row.getCell(ELEMENT_TYPE).setCellValue(element.getElementType().getName());
    row.getCell(ELEMENT_NAME).setCellValue(element.getName());
    row.getCell(ELEMENT_COS).setCellValue(element.getClassOfSupply().getId());
    row.getCell(ELEMENT_ENVIRONMENT).setCellValue(element.getEnvironment().getName());
    row.getCell(ELEMENT_ACCOMMODATION_MASS).setCellValue(element.getAccommodationMass());
    row.getCell(ELEMENT_MASS).setCellValue(element.getMass());
    row.getCell(ELEMENT_VOLUME).setCellValue(element.getVolume());
    row.getCell(ELEMENT_DESCRIPTION).setCellValue(element.getDescription());
    row.getCell(ELEMENT_ICON).setCellValue(element.getIconType().getName());
    if (element.getElementType() == ElementType.RESOURCE_CONTAINER) {
      row.getCell(ELEMENT_CARGO_MASS).setCellValue(((ResourceContainer) element).getMaxCargoMass());
      row.getCell(ELEMENT_CARGO_VOLUME)
          .setCellValue(((ResourceContainer) element).getMaxCargoVolume());
      row.getCell(ELEMENT_CARGO_ENVIRONMENT)
          .setCellValue(((ResourceContainer) element).getCargoEnvironment().getName());
      saveContents(wb, (ResourceContainer) element);
    } else if (element.getElementType() == ElementType.CREW_MEMBER) {
      row.getCell(ELEMENT_ACTIVE_FRACTION)
          .setCellValue(((CrewMember) element).getAvailableTimeFraction());
    } else if (element.getElementType() == ElementType.CARRIER) {
      row.getCell(ELEMENT_MAX_CREW).setCellValue(((Carrier) element).getMaxCrewSize());
      row.getCell(ELEMENT_CARGO_MASS).setCellValue(((Carrier) element).getMaxCargoMass());
      row.getCell(ELEMENT_CARGO_VOLUME).setCellValue(((Carrier) element).getMaxCargoVolume());
      row.getCell(ELEMENT_CARGO_ENVIRONMENT)
          .setCellValue(((Carrier) element).getCargoEnvironment().getName());
    } else if (element.getElementType() == ElementType.PROPULSIVE_VEHICLE) {
      row.getCell(ELEMENT_MAX_CREW).setCellValue(((PropulsiveVehicle) element).getMaxCrewSize());
      row.getCell(ELEMENT_CARGO_MASS).setCellValue(((PropulsiveVehicle) element).getMaxCargoMass());
      row.getCell(ELEMENT_CARGO_VOLUME)
          .setCellValue(((PropulsiveVehicle) element).getMaxCargoVolume());
      row.getCell(ELEMENT_CARGO_ENVIRONMENT)
          .setCellValue(((PropulsiveVehicle) element).getCargoEnvironment().getName());
      if (((PropulsiveVehicle) element).getOmsIsp() > 0) {
        row.getCell(ELEMENT_OMS_ISP).setCellValue(((PropulsiveVehicle) element).getOmsIsp());
        row.getCell(ELEMENT_MAX_OMS)
            .setCellValue(((PropulsiveVehicle) element).getOmsFuelTank().getMaxAmount());
        saveResource(row.getCell(ELEMENT_OMS_ID),
            ((PropulsiveVehicle) element).getOmsFuelTank().getResource());
      } else {
        row.getCell(ELEMENT_OMS_ISP).setCellValue(0);
        row.getCell(ELEMENT_MAX_OMS).setCellValue(0);
        row.getCell(ELEMENT_OMS_ID).setCellValue("");
      }
      if (((PropulsiveVehicle) element).getRcsIsp() > 0) {
        row.getCell(ELEMENT_RCS_ISP).setCellValue(((PropulsiveVehicle) element).getRcsIsp());
        if (((PropulsiveVehicle) element).getRcsFuelTank() == null || ((PropulsiveVehicle) element)
            .getRcsFuelTank() == ((PropulsiveVehicle) element).getOmsFuelTank()) {
          // shared OMS / RCS tanks
          row.getCell(ELEMENT_MAX_RCS).setCellValue(0);
          row.getCell(ELEMENT_RCS_ID).setCellValue("");
        } else {
          row.getCell(ELEMENT_MAX_RCS)
              .setCellValue(((PropulsiveVehicle) element).getRcsFuelTank().getMaxAmount());
          saveResource(row.getCell(ELEMENT_RCS_ID),
              ((PropulsiveVehicle) element).getRcsFuelTank().getResource());
        }
      } else {
        row.getCell(ELEMENT_RCS_ISP).setCellValue(0);
        row.getCell(ELEMENT_MAX_RCS).setCellValue(0);
        row.getCell(ELEMENT_RCS_ID).setCellValue("");
      }
    } else if (element.getElementType() == ElementType.SURFACE_VEHICLE) {
      row.getCell(ELEMENT_MAX_CREW).setCellValue(((SurfaceVehicle) element).getMaxCrewSize());
      row.getCell(ELEMENT_CARGO_MASS).setCellValue(((SurfaceVehicle) element).getMaxCargoMass());
      row.getCell(ELEMENT_CARGO_VOLUME)
          .setCellValue(((SurfaceVehicle) element).getMaxCargoVolume());
      row.getCell(ELEMENT_CARGO_ENVIRONMENT)
          .setCellValue(((SurfaceVehicle) element).getCargoEnvironment().getName());
      row.getCell(ELEMENT_MAX_SPEED).setCellValue(((SurfaceVehicle) element).getMaxSpeed());
      row.getCell(ELEMENT_MAX_FUEL)
          .setCellValue(((SurfaceVehicle) element).getFuelTank().getMaxAmount());
      saveResource(row.getCell(ELEMENT_FUEL_ID),
          ((SurfaceVehicle) element).getFuelTank().getResource());
    }
    saveParts(wb, element);
    saveStates(wb, element);
    writeWorkbook(wb);
  }

  /**
   * Saves the contents of a resource container. Removed resources will be deleted from the data
   * source, changed resources will be updated, and added resources will be inserted.
   * 
   * @param wb the workbook
   * @param container the container
   */
  private void saveContents(Workbook wb, I_ResourceContainer container) {
    Sheet sheet = wb.getSheetAt(DEMAND_SHEET);
    List<Integer> demandsToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(DEMAND_CONTAINER_ID).getCellType() == CellType.NUMERIC
          && row.getCell(DEMAND_CONTAINER_ID).getNumericCellValue() == container.getTid()) {
        boolean isDemandFound = false;
        for (I_Resource resource : container.getContents().keySet()) {
          if (isCellResourceMatch(row.getCell(DEMAND_RESOURCE_ID), resource)) {
            isDemandFound = true;
            break;
          }
        }
        if (!isDemandFound)
          demandsToDelete.add((int) row.getCell(DEMAND_ID).getNumericCellValue());
      }
    }
    for (Integer i : demandsToDelete) {
      deleteDemand(wb, i);
    }
    for (I_Resource resource : container.getContents().keySet()) {
      int rowNum = -1, maxTid = 0, tid = -1;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(DEMAND_CONTAINER_ID).getCellType() == CellType.NUMERIC
            && row.getCell(DEMAND_CONTAINER_ID).getNumericCellValue() == container.getTid()
            && isCellResourceMatch(row.getCell(DEMAND_RESOURCE_ID), resource)) {
          rowNum = row.getRowNum();
          tid = (int) row.getCell(DEMAND_ID).getNumericCellValue();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(DEMAND_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // demand not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        tid = maxTid + 1;
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(DEMAND_ID).setCellValue(tid);
      saveResource(row.getCell(DEMAND_RESOURCE_ID), resource);
      row.getCell(DEMAND_AMOUNT).setCellValue(container.getContents().get(resource));
      row.getCell(DEMAND_CONTAINER_ID).setCellValue(container.getTid());
    }
  }

  /**
   * Saves the parts associated with an element. Removed parts will be deleted from the data source,
   * changed parts will be updated, and added parts will be inserted.
   * 
   * @param wb the workbook
   * @param element the element
   */
  private void saveParts(Workbook wb, I_Element element) {
    Sheet sheet = wb.getSheetAt(PART_SHEET);
    List<Integer> partsToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(PART_ELEMENT_ID).getNumericCellValue() == element.getTid()) {
        boolean isPartFound = false;
        for (PartApplication part : element.getParts()) {
          if (isCellResourceMatch(row.getCell(PART_RESOURCE_ID), part.getPart())) {
            isPartFound = true;
            break;
          }
        }
        if (!isPartFound)
          partsToDelete.add((int) row.getCell(PART_ID).getNumericCellValue());
      }
    }
    for (Integer i : partsToDelete) {
      deletePart(wb, i);
    }
    for (PartApplication part : element.getParts()) {
      int rowNum = -1, maxTid = 0;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(PART_ELEMENT_ID).getNumericCellValue() == element.getTid()
            && isCellResourceMatch(row.getCell(PART_RESOURCE_ID), part.getPart())) {
          rowNum = row.getRowNum();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(PART_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // part not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        part.setTid(maxTid + 1);
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(PART_ID).setCellValue(part.getTid());
      saveResource(row.getCell(PART_RESOURCE_ID), part.getPart());
      row.getCell(PART_ELEMENT_ID).setCellValue(element.getTid());
      row.getCell(PART_QUANTITY).setCellValue(part.getQuantity());
      row.getCell(PART_DUTY_CYCLE).setCellValue(part.getDutyCycle());
      row.getCell(PART_MTTF).setCellValue(part.getMeanTimeToFailure());
      row.getCell(PART_MTTR).setCellValue(part.getMeanTimeToRepair());
      row.getCell(PART_REPAIR_MASS).setCellValue(part.getMassToRepair());
    }
  }

  /**
   * Saves the states associated with an element. Removed states will be deleted from the data
   * source, changed states will be updated, and added states will be assigned a new TID and
   * inserted.
   * 
   * @param wb the workbook
   * @param element the element
   */
  private void saveStates(Workbook wb, I_Element element) {
    Sheet sheet = wb.getSheetAt(STATE_SHEET);
    List<Integer> statesToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(STATE_ELEMENT_ID).getNumericCellValue() == element.getTid()) {
        boolean isStateFound = false;
        for (I_State state : element.getStates()) {
          if (state.getTid() == row.getCell(STATE_ID).getNumericCellValue()) {
            isStateFound = true;
            break;
          }
        }
        if (!isStateFound)
          statesToDelete.add((int) row.getCell(STATE_ID).getNumericCellValue());
      }
    }
    for (Integer i : statesToDelete) {
      deleteState(wb, i);
    }
    for (I_State state : element.getStates()) {
      int rowNum = -1, maxTid = 0;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(STATE_ID).getNumericCellValue() == state.getTid()) {
          rowNum = row.getRowNum();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(STATE_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // state not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        state.setTid(maxTid + 1);
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(STATE_ID).setCellValue(state.getTid());
      row.getCell(STATE_ELEMENT_ID).setCellValue(element.getTid());
      row.getCell(STATE_NAME).setCellValue(state.getName());
      row.getCell(STATE_TYPE).setCellValue(state.getStateType().getName());
      row.getCell(STATE_INITIAL).setCellValue(element.getCurrentState() == state);

      saveModels(wb, state);
    }
  }

  /**
   * Saves the associated demand models contained within a state. Removed models will be deleted
   * from the data source, changed models will be updated, and added models will assigned a new TID
   * and be inserted.
   * 
   * @param wb the workbook
   * @param state the state
   */
  private void saveModels(Workbook wb, I_State state) {
    Sheet sheet = wb.getSheetAt(MODEL_SHEET);
    List<Integer> modelsToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(MODEL_STATE_ID).getNumericCellValue() == state.getTid()) {
        boolean isModelFound = false;
        for (I_DemandModel model : state.getDemandModels()) {
          if (model.getTid() == row.getCell(MODEL_ID).getNumericCellValue()) {
            isModelFound = true;
            break;
          }
        }
        if (!isModelFound)
          modelsToDelete.add((int) row.getCell(MODEL_ID).getNumericCellValue());
      }
    }
    for (Integer i : modelsToDelete) {
      deleteModel(wb, i);
    }
    for (I_DemandModel model : state.getDemandModels()) {
      int rowNum = -1, maxTid = 0;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(MODEL_ID).getNumericCellValue() == model.getTid()) {
          rowNum = row.getRowNum();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(MODEL_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // model not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        model.setTid(maxTid + 1);
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(MODEL_ID).setCellValue(model.getTid());
      row.getCell(MODEL_TYPE).setCellValue(model.getDemandModelType().getName());
      row.getCell(MODEL_STATE_ID).setCellValue(state.getTid());
      row.getCell(MODEL_NAME).setCellValue(model.getName());

      if (model.getDemandModelType() == DemandModelType.TIMED_IMPULSE
          || model.getDemandModelType() == DemandModelType.RATED) {
        saveDemands(wb, model);
      } else if (model.getDemandModelType() == DemandModelType.SPARING_BY_MASS) {
        row.getCell(MODEL_PARTS_LIST)
            .setCellValue(((SparingByMassDemandModel) model).isPartsListEnabled());
        row.getCell(MODEL_UNPRESS_RATE)
            .setCellValue(((SparingByMassDemandModel) model).getUnpressurizedSparesRate());
        row.getCell(MODEL_PRESS_RATE)
            .setCellValue(((SparingByMassDemandModel) model).getPressurizedSparesRate());
      }
    }
  }

  /**
   * Saves any associated demands from a demand model. Demands that have been removed will be
   * deleted from the data source, demands that have been changed will be updated, and added demands
   * will be inserted.
   * 
   * @param wb the workbook
   * @param model the demand model
   */
  private void saveDemands(Workbook wb, I_DemandModel model) {
    SortedSet<Demand> demands;
    if (model.getDemandModelType() == DemandModelType.TIMED_IMPULSE) {
      demands = ((TimedImpulseDemandModel) model).getDemands();
    } else if (model.getDemandModelType() == DemandModelType.RATED) {
      demands = ((RatedDemandModel) model).getDemandRates();
    } else {
      return;
    }
    Sheet sheet = wb.getSheetAt(DEMAND_SHEET);
    List<Integer> demandsToDelete = new ArrayList<Integer>();
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(DEMAND_MODEL_ID).getCellType() == CellType.NUMERIC
          && row.getCell(DEMAND_MODEL_ID).getNumericCellValue() == model.getTid()) {
        boolean isDemandFound = false;
        for (Demand demand : demands) {
          if (isCellResourceMatch(row.getCell(DEMAND_RESOURCE_ID), demand.getResource())) {
            isDemandFound = true;
            break;
          }
        }
        if (!isDemandFound)
          demandsToDelete.add((int) row.getCell(DEMAND_ID).getNumericCellValue());
      }
    }
    for (Integer i : demandsToDelete) {
      deleteDemand(wb, i);
    }
    for (Demand demand : demands) {
      int rowNum = -1, maxTid = 0, tid = -1;
      for (Row row : sheet) {
        if (row.getRowNum() == 0 || isRowEmpty(row))
          continue;
        if (row.getCell(DEMAND_MODEL_ID).getCellType() == CellType.NUMERIC
            && row.getCell(DEMAND_MODEL_ID).getNumericCellValue() == model.getTid()
            && isCellResourceMatch(row.getCell(DEMAND_RESOURCE_ID), demand.getResource())) {
          rowNum = row.getRowNum();
          tid = (int) row.getCell(DEMAND_ID).getNumericCellValue();
        }
        maxTid = (int) Math.max(maxTid, row.getCell(DEMAND_ID).getNumericCellValue());
      }
      Row row = null;
      if (rowNum < 0) {
        // demand not found in database -- create new entry
        rowNum = getLastNonEmptyRow(sheet) + 1;
        tid = maxTid + 1;
        row = sheet.getRow(rowNum);
        if (row == null) {
          row = sheet.createRow(rowNum);
        }
      } else {
        row = sheet.getRow(rowNum);
      }
      row.getCell(DEMAND_ID).setCellValue(tid);
      row.getCell(DEMAND_MODEL_ID).setCellValue(model.getTid());
      saveResource(row.getCell(DEMAND_RESOURCE_ID), demand.getResource());
      row.getCell(DEMAND_AMOUNT).setCellValue(demand.getAmount());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#saveNode(edu.mit.spacenet.domain.network.node.Node)
   */
  public void saveNode(Node node) throws IOException {
    Workbook wb = readWorkbook();
    Sheet sheet = wb.getSheetAt(NODE_SHEET);
    int rowNum = -1, maxTid = 0;
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(NODE_ID).getNumericCellValue() == node.getTid()) {
        rowNum = row.getRowNum();
      }
      maxTid = (int) Math.max(maxTid, row.getCell(NODE_ID).getNumericCellValue());
    }
    Row row = null;
    if (rowNum < 0) {
      // node not found in database -- create new entry
      rowNum = getLastNonEmptyRow(sheet) + 1;
      node.setTid(maxTid + 1);
      nodeLibrary.add(node);
      row = sheet.getRow(rowNum);
      if (row == null) {
        row = sheet.createRow(rowNum);
      }
    } else {
      row = sheet.getRow(rowNum);
    }
    row.getCell(NODE_ID).setCellValue(node.getTid());
    row.getCell(NODE_TYPE).setCellValue(node.getNodeType().getName());
    row.getCell(NODE_NAME).setCellValue(node.getName());
    row.getCell(NODE_BODY_1).setCellValue(node.getBody().getName());
    row.getCell(NODE_DESCRIPTION).setCellValue(node.getDescription());
    if (node.getNodeType() == NodeType.SURFACE) {
      row.getCell(NODE_LATITUDE).setCellValue(((SurfaceNode) node).getLatitude());
      row.getCell(NODE_LONGITUDE).setCellValue(((SurfaceNode) node).getLongitude());
    } else if (node.getNodeType() == NodeType.ORBITAL) {
      row.getCell(NODE_APOAPSIS).setCellValue(((OrbitalNode) node).getApoapsis());
      row.getCell(NODE_PERIAPSIS).setCellValue(((OrbitalNode) node).getPeriapsis());
      row.getCell(NODE_INCLINATION).setCellValue(((OrbitalNode) node).getInclination());
    } else if (node.getNodeType() == NodeType.LAGRANGE) {
      row.getCell(NODE_BODY_2).setCellValue(((LagrangeNode) node).getMinorBody().getName());
      row.getCell(NODE_LP_NUMBER).setCellValue(((LagrangeNode) node).getNumber());
    }
    writeWorkbook(wb);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.mit.spacenet.data.I_DataSource#saveResource(edu.mit.spacenet.domain.resource.I_Resource)
   */
  public void saveResource(I_Resource resource) throws IOException {
    if (resource.getResourceType() == ResourceType.GENERIC)
      return;
    Workbook wb = readWorkbook();
    Sheet sheet = wb.getSheetAt(RESOURCE_SHEET);
    int rowNum = -1, maxTid = 0;
    for (Row row : sheet) {
      if (row.getRowNum() == 0 || isRowEmpty(row))
        continue;
      if (row.getCell(RESOURCE_ID).getNumericCellValue() == resource.getTid()) {
        rowNum = row.getRowNum();
      }
      maxTid = (int) Math.max(maxTid, row.getCell(RESOURCE_ID).getNumericCellValue());
    }
    Row row = null;
    if (rowNum < 0) {
      // resource not found in database -- create new entry
      rowNum = getLastNonEmptyRow(sheet) + 1;
      resource.setTid(maxTid + 1);
      resourceTypeLibrary.add(resource);
      row = sheet.getRow(rowNum);
      if (row == null) {
        row = sheet.createRow(rowNum);
      }
    } else {
      row = sheet.getRow(rowNum);
    }
    row.getCell(RESOURCE_ID).setCellValue(resource.getTid());
    row.getCell(RESOURCE_TYPE).setCellValue(resource.getResourceType().getName());
    row.getCell(RESOURCE_NAME).setCellValue(resource.getName());
    row.getCell(RESOURCE_COS).setCellValue(resource.getClassOfSupply().getId());
    row.getCell(RESOURCE_UNITS).setCellValue(resource.getUnits());
    row.getCell(RESOURCE_UNIT_MASS).setCellValue(resource.getUnitMass());
    row.getCell(RESOURCE_UNIT_VOLUME).setCellValue(resource.getUnitVolume());
    row.getCell(RESOURCE_PACKING_FACTOR).setCellValue(resource.getPackingFactor());
    row.getCell(RESOURCE_ENVIRONMENT).setCellValue(resource.getEnvironment().getName());
    row.getCell(RESOURCE_DESCRIPTION).setCellValue(resource.getDescription());
    writeWorkbook(wb);
  }

  /**
   * Sets the file path.
   * 
   * @param filePath the new file path
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#validateData()
   */
  public List<String> validateData() throws IOException {
    Workbook wb = readWorkbook();
    ArrayList<String> errors = new ArrayList<String>();
    errors.addAll(checkNodes(wb.getSheetAt(NODE_SHEET)));
    errors.addAll(checkEdges(wb.getSheetAt(EDGE_SHEET)));
    errors.addAll(checkBurns(wb.getSheetAt(BURN_SHEET)));
    errors.addAll(checkResources(wb.getSheetAt(RESOURCE_SHEET)));
    errors.addAll(checkElements(wb.getSheetAt(ELEMENT_SHEET)));
    errors.addAll(checkPartApplications(wb.getSheetAt(PART_SHEET)));
    errors.addAll(checkStates(wb.getSheetAt(STATE_SHEET)));
    errors.addAll(checkModels(wb.getSheetAt(MODEL_SHEET)));
    errors.addAll(checkDemands(wb.getSheetAt(DEMAND_SHEET)));
    return errors;
  }

  /**
   * Checks the nodes sheet to find any parse errors.
   * 
   * @param sheet the nodes sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkNodes(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      String type = "";
      if (row.getCell(NODE_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Node in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(NODE_TYPE).getCellType() != CellType.STRING) {
        errors.add("Node in row " + (i + 1) + " has missing or invalid type.");
      } else {
        type = row.getCell(NODE_TYPE).getStringCellValue().toLowerCase();
      }
      if (row.getCell(NODE_NAME).getCellType() != CellType.STRING) {
        errors.add("Node in row " + (i + 1) + " has missing or invalid name.");
      }
      if (row.getCell(NODE_BODY_1).getCellType() != CellType.STRING) {
        errors.add("Node in row " + (i + 1) + " has missing or invalid body.");
      }
      if (NodeType.SURFACE.getName().toLowerCase().contains(type)) {
        if (row.getCell(NODE_LATITUDE).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid latitude.");
        }
        if (row.getCell(NODE_LONGITUDE).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid longitude.");
        }
      } else if (NodeType.ORBITAL.getName().toLowerCase().contains(type)) {
        if (row.getCell(NODE_APOAPSIS).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid apoapsis.");
        }
        if (row.getCell(NODE_PERIAPSIS).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid periapsis.");
        }
        if (row.getCell(NODE_INCLINATION).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid inclination.");
        }
      } else if (NodeType.LAGRANGE.getName().toLowerCase().contains(type)) {
        if (row.getCell(NODE_BODY_2).getCellType() != CellType.STRING) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid minor body.");
        }
        if (row.getCell(NODE_LP_NUMBER).getCellType() != CellType.NUMERIC) {
          errors.add("Node in row " + (i + 1) + " has missing or invalid lagrange number.");
        }
      } else if (!type.equals("")) {
        errors.add("Node in row " + (i + 1) + " has missing or invalid type.");
      }
    }
    return errors;
  }

  /**
   * Checks the edges sheet to find any parse errors.
   * 
   * @param sheet the edges sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkEdges(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      String type = "";
      if (row.getCell(EDGE_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(EDGE_TYPE).getCellType() != CellType.STRING) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid type.");
      } else {
        type = row.getCell(EDGE_TYPE).getStringCellValue().toLowerCase();
      }
      if (row.getCell(EDGE_NAME).getCellType() != CellType.STRING) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid name.");
      }
      if (row.getCell(EDGE_ORIGIN_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid origin node.");
      }
      if (row.getCell(EDGE_DESTINATION_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid destination node.");
      }
      if (EdgeType.SPACE.getName().toLowerCase().contains(type)) {
        if (row.getCell(EDGE_DURATION).getCellType() != CellType.NUMERIC) {
          errors.add("Edge in row " + (i + 1) + " has missing or invalid duration.");
        }
      } else if (EdgeType.SURFACE.getName().toLowerCase().contains(type)) {
        if (row.getCell(EDGE_DISTANCE).getCellType() != CellType.NUMERIC) {
          errors.add("Edge in row " + (i + 1) + " has missing or invalid distance.");
        }
      } else if (EdgeType.FLIGHT.getName().toLowerCase().contains(type)) {
        if (row.getCell(EDGE_DURATION).getCellType() != CellType.NUMERIC) {
          errors.add("Edge in row " + (i + 1) + " has missing or invalid duration.");
        }
        if (row.getCell(EDGE_MAX_CREW).getCellType() != CellType.NUMERIC) {
          errors.add("Edge in row " + (i + 1) + " has missing or invalid max crew size.");
        }
        if (row.getCell(EDGE_MAX_CARGO).getCellType() != CellType.NUMERIC) {
          errors.add("Edge in row " + (i + 1) + " has missing or invalid max cargo mass.");
        }
      } else if (!type.equals("")) {
        errors.add("Edge in row " + (i + 1) + " has missing or invalid type.");
      }
    }
    return errors;
  }

  /**
   * Checks the burns sheet to find any parse errors.
   * 
   * @param sheet the burns sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkBurns(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      if (row.getCell(BURN_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Burn in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(BURN_TIME).getCellType() != CellType.NUMERIC) {
        errors.add("Burn in row " + (i + 1) + " has missing or invalid time.");
      }
      if (row.getCell(BURN_ORDER).getCellType() != CellType.NUMERIC) {
        errors.add("Burn in row " + (i + 1) + " has missing or invalid order.");
      }
      if (row.getCell(BURN_TYPE).getCellType() != CellType.STRING) {
        errors.add("Burn in row " + (i + 1) + " has missing or invalid type.");
      }
      if (row.getCell(BURN_DELTA_V).getCellType() != CellType.NUMERIC) {
        errors.add("Burn in row " + (i + 1) + " has missing or invalid delta-v.");
      }
    }
    return errors;
  }

  /**
   * Checks the resources sheet to find any parse errors.
   * 
   * @param sheet the resources sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkResources(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      String type = "";
      if (row.getCell(RESOURCE_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(RESOURCE_TYPE).getCellType() != CellType.STRING) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid type.");
      } else {
        type = row.getCell(RESOURCE_TYPE).getStringCellValue().toLowerCase();
      }
      if (row.getCell(RESOURCE_NAME).getCellType() != CellType.STRING) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid name.");
      }
      if (row.getCell(RESOURCE_COS).getCellType() != CellType.NUMERIC
          || ClassOfSupply.getInstance((int) row.getCell(3).getNumericCellValue()) == null) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid class of supply.");
      }
      if (row.getCell(RESOURCE_UNITS).getCellType() != CellType.STRING) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid units.");
      }
      if (row.getCell(RESOURCE_UNIT_MASS).getCellType() != CellType.NUMERIC) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid unit mass.");
      }
      if (row.getCell(RESOURCE_UNIT_VOLUME).getCellType() != CellType.NUMERIC) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid unit volume.");
      }
      if (row.getCell(RESOURCE_PACKING_FACTOR).getCellType() != CellType.NUMERIC) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid packing factor.");
      }
      if (row.getCell(RESOURCE_ENVIRONMENT).getCellType() != CellType.STRING
          || (!Environment.PRESSURIZED.getName().toLowerCase()
              .equals(row.getCell(RESOURCE_ENVIRONMENT).getStringCellValue().toLowerCase())
              && !Environment.UNPRESSURIZED.getName().toLowerCase()
                  .equals(row.getCell(RESOURCE_ENVIRONMENT).getStringCellValue().toLowerCase()))) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid environment.");
      }
      if (ResourceType.RESOURCE.getName().toLowerCase().contains(type)) {
      } else if (ResourceType.ITEM.getName().toLowerCase().contains(type)) {
      } else if (!type.equals("")) {
        errors.add("Resource in row " + (i + 1) + " has missing or invalid type.");
      }
    }
    return errors;
  }

  /**
   * Check if a cell has a valid resource id (numeric or numeric prefixed by "U" or "P").
   *
   * @param cell the cell
   * @return true, if successful
   */
  private boolean cellHasValidResourceId(Cell cell) {
    if (cell.getCellType() == CellType.NUMERIC) {
      return true;
    } else if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().length() > 1
        && (cell.getStringCellValue().substring(0, 1).toLowerCase().equals("u")
            || cell.getStringCellValue().substring(0, 1).toLowerCase().equals("p"))) {
      try {
        Integer.parseInt(cell.getStringCellValue().substring(1));
        return true;
      } catch (NumberFormatException ex) {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Checks the elements sheet to find any parse errors.
   * 
   * @param sheet the elements sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkElements(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      String type = "";
      if (row.getCell(ELEMENT_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(ELEMENT_TYPE).getCellType() != CellType.STRING) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid type.");
      } else {
        type = row.getCell(ELEMENT_TYPE).getStringCellValue().toLowerCase();
      }
      if (row.getCell(ELEMENT_NAME).getCellType() != CellType.STRING) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid name.");
      }
      if (row.getCell(ELEMENT_COS).getCellType() != CellType.NUMERIC
          || ClassOfSupply.getInstance((int) row.getCell(3).getNumericCellValue()) == null) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid class of supply.");
      }
      if (row.getCell(ELEMENT_ENVIRONMENT).getCellType() != CellType.STRING
          || (!Environment.PRESSURIZED.getName().toLowerCase()
              .equals(row.getCell(ELEMENT_ENVIRONMENT).getStringCellValue().toLowerCase())
              && !Environment.UNPRESSURIZED.getName().toLowerCase()
                  .equals(row.getCell(ELEMENT_ENVIRONMENT).getStringCellValue().toLowerCase()))) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid environment.");
      }
      if (row.getCell(ELEMENT_ACCOMMODATION_MASS).getCellType() != CellType.NUMERIC) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid accommodation mass.");
      }
      if (row.getCell(ELEMENT_MASS).getCellType() != CellType.NUMERIC) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid mass.");
      }
      if (row.getCell(ELEMENT_VOLUME).getCellType() != CellType.NUMERIC) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid volume.");
      }
      if (ElementType.ELEMENT.getName().toLowerCase().contains(type)) {
      } else if (ElementType.RESOURCE_CONTAINER.getName().toLowerCase().contains(type)) {
        if (row.getCell(ELEMENT_CARGO_MASS).getCellType() != CellType.NUMERIC) {
          errors.add("Container in row " + (i + 1) + " has missing or invalid max cargo mass.");
        }
        if (row.getCell(ELEMENT_CARGO_VOLUME).getCellType() != CellType.NUMERIC) {
          errors.add("Container in row " + (i + 1) + " has missing or invalid max cargo volume.");
        }
        if (row.getCell(ELEMENT_CARGO_ENVIRONMENT).getCellType() != CellType.STRING
            || (!Environment.PRESSURIZED.getName().toLowerCase()
                .equals(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue().toLowerCase())
                && !Environment.UNPRESSURIZED.getName().toLowerCase().equals(
                    row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue().toLowerCase()))) {
          errors.add("Container in row " + (i + 1) + " has missing or invalid cargo environment.");
        }
      } else if (ElementType.CREW_MEMBER.getName().toLowerCase().contains(type)) {
        if (row.getCell(ELEMENT_ACTIVE_FRACTION).getCellType() != CellType.NUMERIC) {
          errors.add(
              "Crew Member in row " + (i + 1) + " has missing or invalid active time fraction.");
        }
      } else if (ElementType.CARRIER.getName().toLowerCase().contains(type)
          || ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(type)
          || ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(type)) {
        if (row.getCell(ELEMENT_MAX_CREW).getCellType() != CellType.NUMERIC) {
          errors.add("Carrier in row " + (i + 1) + " has missing or invalid max crew.");
        }
        if (row.getCell(ELEMENT_CARGO_MASS).getCellType() != CellType.NUMERIC) {
          errors.add("Carrier in row " + (i + 1) + " has missing or invalid max cargo mass.");
        }
        if (row.getCell(ELEMENT_CARGO_VOLUME).getCellType() != CellType.NUMERIC) {
          errors.add("Container in row " + (i + 1) + " has missing or invalid max cargo volume.");
        }
        if (row.getCell(ELEMENT_CARGO_ENVIRONMENT).getCellType() != CellType.STRING
            || (!Environment.PRESSURIZED.getName().toLowerCase()
                .equals(row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue().toLowerCase())
                && !Environment.UNPRESSURIZED.getName().toLowerCase().equals(
                    row.getCell(ELEMENT_CARGO_ENVIRONMENT).getStringCellValue().toLowerCase()))) {
          errors.add("Container in row " + (i + 1) + " has missing or invalid cargo environment.");
        }
      } else if (ElementType.PROPULSIVE_VEHICLE.getName().toLowerCase().contains(type)) {
        if (row.getCell(ELEMENT_OMS_ISP).getCellType() != CellType.NUMERIC) {
          errors.add("Propulsive Vehicle in row " + (i + 1) + " has missing or invalid OMS Isp.");
        }
        if (!cellHasValidResourceId(row.getCell(ELEMENT_OMS_ID))) {
          errors.add(
              "Propulsive Vehicle in row " + (i + 1) + " has missing or invalid max OMS fuel.");
        }
        if (row.getCell(ELEMENT_RCS_ISP).getCellType() != CellType.NUMERIC) {
          errors.add("Propulsive Vehicle in row " + (i + 1) + " has missing or invalid RCS Isp.");
        }
        if (!cellHasValidResourceId(row.getCell(ELEMENT_RCS_ID))) {
          errors.add(
              "Propulsive Vehicle in row " + (i + 1) + " has missing or invalid max RCS fuel.");
        }
      } else if (ElementType.SURFACE_VEHICLE.getName().toLowerCase().contains(type)) {
        if (row.getCell(ELEMENT_MAX_SPEED).getCellType() != CellType.NUMERIC) {
          errors.add("Surface Vehicle in row " + (i + 1) + " has missing or invalid max speed.");
        }
        if (row.getCell(ELEMENT_MAX_FUEL).getCellType() != CellType.NUMERIC) {
          errors.add("Surface Vehicle in row " + (i + 1) + " has missing or invalid max fuel.");
        }
        if (!cellHasValidResourceId(row.getCell(ELEMENT_FUEL_ID))) {
          errors.add("Surface Vehicle in row " + (i + 1) + " has missing or invalid fuel id.");
        }
      } else if (!type.equals("")) {
        errors.add("Element in row " + (i + 1) + " has missing or invalid type.");
      }
    }
    return errors;
  }

  /**
   * Checks the part applications sheet to find any parse errors.
   * 
   * @param sheet the parts sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkPartApplications(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      if (row.getCell(PART_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Part Application in row " + (i + 1) + " has missing or invalid id.");
      }
      if (!cellHasValidResourceId(row.getCell(PART_RESOURCE_ID))) {
        errors.add("Part Application in row " + (i + 1) + " has missing or invalid resource id.");
      }
      if (row.getCell(PART_QUANTITY).getCellType() != CellType.NUMERIC) {
        errors.add("Part Application in row " + (i + 1) + " has missing or invalid quantity.");
      }
      if (row.getCell(PART_DUTY_CYCLE).getCellType() != CellType.NUMERIC) {
        errors.add("Part Application in row " + (i + 1) + " has missing or invalid duty cycle.");
      }
      if (row.getCell(PART_MTTF).getCellType() != CellType.NUMERIC) {
        errors.add(
            "Part Application in row " + (i + 1) + " has missing or invalid mean time to failure.");
      }
      if (row.getCell(PART_MTTR).getCellType() != CellType.NUMERIC) {
        errors.add(
            "Part Application in row " + (i + 1) + " has missing or invalid mean time to repair.");
      }
      if (row.getCell(PART_REPAIR_MASS).getCellType() != CellType.NUMERIC) {
        errors
            .add("Part Application in row " + (i + 1) + " has missing or invalid mass to repair.");
      }
    }
    return errors;
  }

  /**
   * Checks the states sheet to find any parsing errors.
   * 
   * @param sheet the states sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkStates(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      if (row.getCell(STATE_ID).getCellType() != CellType.NUMERIC) {
        errors.add("State in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(STATE_NAME).getCellType() != CellType.STRING) {
        errors.add("State in row " + (i + 1) + " has missing or invalid name.");
      }
      if (row.getCell(STATE_TYPE).getCellType() != CellType.STRING || !(StateType.ACTIVE.getName()
          .toLowerCase().contains(row.getCell(STATE_TYPE).getStringCellValue().toLowerCase())
          || StateType.SPECIAL.getName().toLowerCase()
              .contains(row.getCell(STATE_TYPE).getStringCellValue().toLowerCase())
          || StateType.QUIESCENT.getName().toLowerCase()
              .contains(row.getCell(STATE_TYPE).getStringCellValue().toLowerCase())
          || StateType.DORMANT.getName().toLowerCase()
              .contains(row.getCell(STATE_TYPE).getStringCellValue().toLowerCase())
          || StateType.DECOMMISSIONED.getName().toLowerCase()
              .contains(row.getCell(STATE_TYPE).getStringCellValue().toLowerCase()))) {
        errors.add("State in row " + (i + 1) + " has missing or invalid type.");
      }
      if (row.getCell(STATE_INITIAL).getCellType() != CellType.BOOLEAN) {
        errors.add("State in row " + (i + 1) + " has missing or invalid initial state boolean.");
      }
    }
    return errors;
  }

  /**
   * Checks the models sheet to find any parsing errors.
   * 
   * @param sheet the models sheet
   * 
   * @return the list of errors
   */
  private List<String> checkModels(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      String type = "";
      if (row.getCell(MODEL_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Model in row " + (i + 1) + " has missing or invalid id.");
      }
      if (row.getCell(MODEL_TYPE).getCellType() != CellType.STRING) {
        errors.add("Model in row " + (i + 1) + " has missing or invalid type.");
      } else {
        type = row.getCell(MODEL_TYPE).getStringCellValue().toLowerCase();
      }
      if (row.getCell(MODEL_NAME).getCellType() != CellType.STRING) {
        errors.add("Model in row " + (i + 1) + " has missing or invalid name.");
      }
      if (DemandModelType.TIMED_IMPULSE.getName().toLowerCase().contains(type)) {
      } else if (DemandModelType.RATED.getName().toLowerCase().contains(type)) {
      } else if (DemandModelType.SPARING_BY_MASS.getName().toLowerCase().contains(type)) {
        if (row.getCell(MODEL_PARTS_LIST).getCellType() != CellType.BOOLEAN) {
          errors.add("Sparing by Mass Model in row " + (i + 1)
              + " has missing or invalid parts list boolean.");
        }
        if (row.getCell(MODEL_UNPRESS_RATE).getCellType() != CellType.NUMERIC) {
          errors.add("Sparing by Mass Model in row " + (i + 1)
              + " has missing or invalid unpressurized spares rate.");
        }
        if (row.getCell(MODEL_PRESS_RATE).getCellType() != CellType.NUMERIC) {
          errors.add("Sparing by Mass Model in row " + (i + 1)
              + " has missing or invalid pressurized spares rate.");
        }
      } else if (!type.equals("")) {
        errors.add("Model in row " + (i + 1) + " has missing or invalid type.");
      }
    }
    return errors;
  }

  /**
   * Checks the demands sheet to find any parsing errors.
   * 
   * @param sheet the demands sheet
   * 
   * @return the list of error messages
   */
  private List<String> checkDemands(Sheet sheet) {
    ArrayList<String> errors = new ArrayList<String>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (isRowEmpty(row))
        continue;
      if (row.getCell(DEMAND_ID).getCellType() != CellType.NUMERIC) {
        errors.add("Demand in row " + (i + 1) + " has missing or invalid id.");
      }
      if (!cellHasValidResourceId(row.getCell(DEMAND_RESOURCE_ID))) {
        errors.add("Demand in row " + (i + 1) + " has missing or invalid resource id.");
      }
      if (row.getCell(DEMAND_AMOUNT).getCellType() != CellType.NUMERIC) {
        errors.add("Demand in row " + (i + 1) + " has missing or invalid amount.");
      }
    }
    return errors;
  }

  /**
   * Reads a workbook from file.
   * 
   * @return the workbook
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private Workbook readWorkbook() throws IOException {
    FileInputStream fis = new FileInputStream(filePath);
    Workbook wb = new HSSFWorkbook(fis);
    wb.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
    fis.close();
    return wb;
  }

  /**
   * Writes a workbook to file, saving and changes that had been made.
   * 
   * @param wb the workbook
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void writeWorkbook(Workbook wb) throws IOException {
    FileOutputStream fos = new FileOutputStream(filePath);
    wb.write(fos);
    fos.close();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.data.I_DataSource#format()
   */
  public void format() throws IOException {
    FileOutputStream fos = new FileOutputStream(filePath);
    Workbook wb = new HSSFWorkbook();
    wb.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
    if (wb.getNumberOfSheets() <= NODE_SHEET) {
      wb.createSheet("nodes");
      Row row = wb.getSheetAt(NODE_SHEET).createRow(0);
      row.getCell(NODE_ID).setCellValue("id");
      row.getCell(NODE_TYPE).setCellValue("type");
      row.getCell(NODE_NAME).setCellValue("name");
      row.getCell(NODE_BODY_1).setCellValue("body_1");
      row.getCell(NODE_LATITUDE).setCellValue("latitude");
      row.getCell(NODE_LONGITUDE).setCellValue("longitude");
      row.getCell(NODE_APOAPSIS).setCellValue("apoapsis");
      row.getCell(NODE_PERIAPSIS).setCellValue("periapsis");
      row.getCell(NODE_INCLINATION).setCellValue("inclination");
      row.getCell(NODE_BODY_2).setCellValue("body_2");
      row.getCell(NODE_LP_NUMBER).setCellValue("lp_number");
      row.getCell(NODE_DESCRIPTION).setCellValue("description");
    }
    if (wb.getNumberOfSheets() <= EDGE_SHEET) {
      wb.createSheet("edges");
      Row row = wb.getSheetAt(EDGE_SHEET).createRow(0);
      row.getCell(EDGE_ID).setCellValue("id");
      row.getCell(EDGE_TYPE).setCellValue("type");
      row.getCell(EDGE_NAME).setCellValue("name");
      row.getCell(EDGE_ORIGIN_ID).setCellValue("origin_id");
      row.getCell(EDGE_DESTINATION_ID).setCellValue("destination_id");
      row.getCell(EDGE_DURATION).setCellValue("duration");
      row.getCell(EDGE_DISTANCE).setCellValue("distance");
      row.getCell(EDGE_MAX_CREW).setCellValue("max_crew");
      row.getCell(EDGE_MAX_CARGO).setCellValue("max_cargo");
      row.getCell(EDGE_DESCRIPTION).setCellValue("description");
    }
    if (wb.getNumberOfSheets() <= BURN_SHEET) {
      wb.createSheet("burns");
      Row row = wb.getSheetAt(BURN_SHEET).createRow(0);
      row.getCell(BURN_ID).setCellValue("id");
      row.getCell(BURN_EDGE_ID).setCellValue("edge_id");
      row.getCell(BURN_TIME).setCellValue("time");
      row.getCell(BURN_ORDER).setCellValue("order");
      row.getCell(BURN_TYPE).setCellValue("type");
      row.getCell(BURN_DELTA_V).setCellValue("delta_v");
    }
    if (wb.getNumberOfSheets() <= RESOURCE_SHEET) {
      wb.createSheet("resources");
      Row row = wb.getSheetAt(RESOURCE_SHEET).createRow(0);
      row.getCell(RESOURCE_ID).setCellValue("id");
      row.getCell(RESOURCE_TYPE).setCellValue("type");
      row.getCell(RESOURCE_NAME).setCellValue("name");
      row.getCell(RESOURCE_COS).setCellValue("cos");
      row.getCell(RESOURCE_UNITS).setCellValue("units");
      row.getCell(RESOURCE_UNIT_MASS).setCellValue("unit_mass");
      row.getCell(RESOURCE_UNIT_VOLUME).setCellValue("unit_volume");
      row.getCell(RESOURCE_PACKING_FACTOR).setCellValue("packing_factor");
      row.getCell(RESOURCE_ENVIRONMENT).setCellValue("environment");
      row.getCell(RESOURCE_DESCRIPTION).setCellValue("description");
    }
    if (wb.getNumberOfSheets() <= ELEMENT_SHEET) {
      wb.createSheet("elements");
      Row row = wb.getSheetAt(ELEMENT_SHEET).createRow(0);
      row.getCell(ELEMENT_ID).setCellValue("id");
      row.getCell(ELEMENT_TYPE).setCellValue("type");
      row.getCell(ELEMENT_NAME).setCellValue("name");
      row.getCell(ELEMENT_COS).setCellValue("cos");
      row.getCell(ELEMENT_ENVIRONMENT).setCellValue("environment");
      row.getCell(ELEMENT_ACCOMMODATION_MASS).setCellValue("accommodation_mass");
      row.getCell(ELEMENT_MASS).setCellValue("mass");
      row.getCell(ELEMENT_VOLUME).setCellValue("volume");
      row.getCell(ELEMENT_MAX_CREW).setCellValue("max_crew");
      row.getCell(ELEMENT_CARGO_MASS).setCellValue("cargo_mass");
      row.getCell(ELEMENT_CARGO_VOLUME).setCellValue("cargo_volume");
      row.getCell(ELEMENT_CARGO_ENVIRONMENT).setCellValue("cargo_environment");
      row.getCell(ELEMENT_ACTIVE_FRACTION).setCellValue("active_fraction");
      row.getCell(ELEMENT_OMS_ISP).setCellValue("oms_isp");
      row.getCell(ELEMENT_MAX_OMS).setCellValue("max_oms");
      row.getCell(ELEMENT_OMS_ID).setCellValue("oms_id");
      row.getCell(ELEMENT_RCS_ISP).setCellValue("rcs_isp");
      row.getCell(ELEMENT_MAX_RCS).setCellValue("max_rcs");
      row.getCell(ELEMENT_RCS_ID).setCellValue("rcs_id");
      row.getCell(ELEMENT_MAX_SPEED).setCellValue("max_speed");
      row.getCell(ELEMENT_MAX_FUEL).setCellValue("max_fuel");
      row.getCell(ELEMENT_FUEL_ID).setCellValue("fuel_id");
      row.getCell(ELEMENT_DESCRIPTION).setCellValue("description");
    }
    if (wb.getNumberOfSheets() <= PART_SHEET) {
      wb.createSheet("parts");
      Row row = wb.getSheetAt(PART_SHEET).createRow(0);
      row.getCell(PART_ID).setCellValue("id");
      row.getCell(PART_RESOURCE_ID).setCellValue("resource_id");
      row.getCell(PART_ELEMENT_ID).setCellValue("element_id");
      row.getCell(PART_QUANTITY).setCellValue("quantity");
      row.getCell(PART_DUTY_CYCLE).setCellValue("duty_cycle");
      row.getCell(PART_MTTF).setCellValue("mttf");
      row.getCell(PART_MTTR).setCellValue("mttr");
      row.getCell(PART_REPAIR_MASS).setCellValue("repair_mass");
    }
    if (wb.getNumberOfSheets() <= STATE_SHEET) {
      wb.createSheet("states");
      Row row = wb.getSheetAt(STATE_SHEET).createRow(0);
      row.getCell(STATE_ID).setCellValue("id");
      row.getCell(STATE_ELEMENT_ID).setCellValue("element_id");
      row.getCell(STATE_NAME).setCellValue("name");
      row.getCell(STATE_TYPE).setCellValue("type");
      row.getCell(STATE_INITIAL).setCellValue("initial_state");
    }
    if (wb.getNumberOfSheets() <= MODEL_SHEET) {
      wb.createSheet("models");
      Row row = wb.getSheetAt(MODEL_SHEET).createRow(0);
      row.getCell(MODEL_ID).setCellValue("id");
      row.getCell(MODEL_TYPE).setCellValue("type");
      row.getCell(MODEL_STATE_ID).setCellValue("state_id");
      row.getCell(MODEL_NAME).setCellValue("name");
      row.getCell(MODEL_PARTS_LIST).setCellValue("parts_list");
      row.getCell(MODEL_UNPRESS_RATE).setCellValue("unpress_rate");
      row.getCell(MODEL_PRESS_RATE).setCellValue("press_rate");
    }
    if (wb.getNumberOfSheets() <= DEMAND_SHEET) {
      wb.createSheet("demands");
      Row row = wb.getSheetAt(DEMAND_SHEET).createRow(0);
      row.getCell(DEMAND_ID).setCellValue("id");
      row.getCell(DEMAND_MODEL_ID).setCellValue("model_id");
      row.getCell(DEMAND_RESOURCE_ID).setCellValue("resource_id");
      row.getCell(DEMAND_AMOUNT).setCellValue("amount");
      row.getCell(DEMAND_CONTAINER_ID).setCellValue("container_id");
    }
    wb.write(fos);
    wb.close();
    fos.close();
  }

  /**
   * Deletes a row from a sheet and shifts all subsequent rows up one row to fill the blank spot.
   * 
   * @param sheet the sheet
   * @param row the row
   */
  private static void deleteRow(Sheet sheet, int row) {
    sheet.removeRow(sheet.getRow(row));
    if (row + 1 <= sheet.getLastRowNum())
      sheet.shiftRows(row + 1, sheet.getLastRowNum(), -1);
  }

  /**
   * Gets the last non empty row. The last non-empty row is the last row before the first empty row
   * (missing or invalid primary key) is encountered
   * 
   * @param sheet the sheet on which to find the non-empty row
   * 
   * @return the last non empty row index
   */
  private static int getLastNonEmptyRow(Sheet sheet) {
    for (Row row : sheet) {
      if (row.getRowNum() > 0 && isRowEmpty(row))
        return row.getRowNum() - 1;
    }
    return sheet.getLastRowNum();
  }

  /**
   * Checks if a row is empty by checking the primary key column (assumed to always be column 0) to
   * see if it is not null, numeric, and greater than 0
   * 
   * @param row the row to check
   * 
   * @return true, if is row empty
   */
  private static boolean isRowEmpty(Row row) {
    if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() != CellType.NUMERIC
        || row.getCell(0).getNumericCellValue() <= 0)
      return true;
    else
      return false;
  }
}
