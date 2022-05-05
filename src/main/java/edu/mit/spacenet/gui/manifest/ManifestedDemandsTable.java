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

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyEdge;

/**
 * A table for viewing the available carriers for manifesting.
 * 
 * @author Paul Grogan
 */
public class ManifestedDemandsTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;
  private ManifestTab manifestTab;
  private ManifestedDemandsTableModel model;

  /**
   * Instantiates a new manifested demands table.
   * 
   * @param manifest the manifest
   */
  public ManifestedDemandsTable(ManifestTab manifestTab) {
    this.manifestTab = manifestTab;
    model = new ManifestedDemandsTableModel();
    setModel(model);
    getColumnModel().getColumn(0).setHeaderValue("Supply Edge");
    getColumnModel().getColumn(0).setWidth(150);
    getColumnModel().getColumn(1).setHeaderValue("Carrier");

    setBackground(Color.WHITE);
    getTableHeader().setReorderingAllowed(false);
    getTableHeader().setBackground(Color.WHITE);
    getTableHeader().setFont(getTableHeader().getFont().deriveFont(Font.BOLD));
    getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    getTableHeader().setOpaque(false);
  }

  /**
   * Gets the supply edge.
   * 
   * @param row the row
   * 
   * @return the supply edge
   */
  public SupplyEdge getSupplyEdge(int row) {
    return model.getEdge(row);
  }

  /**
   * Gets the selected supply edge.
   * 
   * @return the selected supply edge
   */
  public SupplyEdge getSelectedSupplyEdge() {
    int row = getSelectedRow();
    if (row < 0)
      return null;
    else
      return getSupplyEdge(row);
  }

  /**
   * Gets the carrier.
   * 
   * @param row the row
   * 
   * @return the carrier
   */
  public I_Carrier getCarrier(int row) {
    return model.getCarrier(row);
  }

  /**
   * Gets the selected carrier.
   * 
   * @return the selected carrier
   */
  public I_Carrier getSelectedCarrier() {
    int row = getSelectedRow();
    if (row < 0)
      return null;
    else
      return getCarrier(row);
  }

  /**
   * Gets the manifest.
   * 
   * @return the manifest
   */
  private Manifest getManifest() {
    return manifestTab.getManifest();
  }

  /**
   * Initialize.
   */
  public void initialize() {
    model.fireTableDataChanged();
  }

  /**
   * Update view.
   */
  public void updateView() {
    model.fireTableDataChanged();
  }

  /**
   * The Class ManifestedDemandsTableModel.
   */
  class ManifestedDemandsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
      return 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
      int i = 0;
      for (SupplyEdge edge : getManifest().getSupplyEdges()) {
        i += edge.getCarriers().size();
      }
      return i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0)
        return getEdge(row);
      else
        return getCarrier(row);
    }

    private SupplyEdge getEdge(int row) {
      int lastRowSeen = -1;
      for (SupplyEdge edge : getManifest().getSupplyEdges()) {
        if (lastRowSeen + edge.getCarriers().size() >= row) {
          return edge;
        }
        lastRowSeen += edge.getCarriers().size();
      }
      return null;
    }

    private I_Carrier getCarrier(int row) {
      int lastRowSeen = -1;
      for (SupplyEdge edge : getManifest().getSupplyEdges()) {
        if (lastRowSeen + edge.getCarriers().size() >= row) {
          for (I_Carrier carrier : edge.getCarriers()) {
            lastRowSeen++;
            if (lastRowSeen == row)
              return carrier;
          }
          return null;
        }
        lastRowSeen += edge.getCarriers().size();
      }
      return null;
    }
  }
}
