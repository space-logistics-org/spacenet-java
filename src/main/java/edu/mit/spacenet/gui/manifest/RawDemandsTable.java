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
package edu.mit.spacenet.gui.manifest;

import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.simulator.DemandSimulator;
import edu.mit.spacenet.simulator.SimDemand;

/**
 * A table to view the raw demands.
 * 
 * @author Paul Grogan
 */
public class RawDemandsTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;

  private DemandSimulator simulator;

  /**
   * Instantiates a new raw demands table.
   * 
   * @param simulator the simulator
   */
  public RawDemandsTable(DemandSimulator simulator, boolean test) {
    this.simulator = simulator;
    setModel(new RawDemandsTableModel(simulator));
    getColumnModel().getColumn(0).setHeaderValue("Time");
    getColumnModel().getColumn(0).setMaxWidth(50);
    DefaultTableCellRenderer tRenderer = new DefaultTableCellRenderer();
    tRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    getColumnModel().getColumn(0).setCellRenderer(tRenderer);
    getColumnModel().getColumn(1).setHeaderValue("Element");
    getColumnModel().getColumn(1).setWidth(250);
    getColumnModel().getColumn(2).setHeaderValue("Location");
    getColumnModel().getColumn(2).setWidth(150);
    getColumnModel().getColumn(3).setHeaderValue("Demand");
  }

  /**
   * Update view.
   */
  public void updateView() {
    for (SimDemand demand : simulator.getUnsatisfiedDemands()) {
      demand.getDemands().clean();
    }

    ((RawDemandsTableModel) getModel()).fireTableDataChanged();
  }

  /**
   * The Class RawDemandsTableModel.
   */
  class RawDemandsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;

    private DemandSimulator simulator;

    /**
     * Instantiates a new raw demands table model.
     * 
     * @param simulator the simulator
     */
    public RawDemandsTableModel(DemandSimulator simulator) {
      super();
      this.simulator = simulator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
      return 4;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
      int i = 0;
      for (SimDemand demand : simulator.getUnsatisfiedDemands()) {
        i += demand.getDemands().size();
      }
      return i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(getSimDemand(row).getTime());
      } else if (col == 1) {
        return getSimDemand(row).getElement();
      } else if (col == 2) {
        return getSimDemand(row).getLocation();
      } else {
        return getDemand(row);
      }
    }

    private SimDemand getSimDemand(int row) {
      int lastRowSeen = -1;
      for (SimDemand demand : simulator.getUnsatisfiedDemands()) {
        if (lastRowSeen + demand.getDemands().size() >= row) {
          return demand;
        }
        lastRowSeen += demand.getDemands().size();
      }
      return null;
    }

    private Demand getDemand(int row) {
      int lastRowSeen = -1;
      for (SimDemand demand : simulator.getUnsatisfiedDemands()) {
        if (lastRowSeen + demand.getDemands().size() >= row) {
          for (Demand d : demand.getDemands()) {
            lastRowSeen++;
            if (lastRowSeen == row)
              return d;
          }
          return null;
        }
        lastRowSeen += demand.getDemands().size();
      }
      return null;
    }
  }
}
