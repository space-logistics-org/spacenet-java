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
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.gui.renderer.ElementTableCellRenderer;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyEdge;

/**
 * A table for viewing the manifested contents of a carrier.
 * 
 * @author Paul Grogan
 */
public class CarrierContentsTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;
  private ManifestTab manifestTab;
  private CarrierContentsTableModel model;

  /**
   * Instantiates a new carrier contents table.
   * 
   * @param m the manifest
   */
  public CarrierContentsTable(ManifestTab manifestTab) {
    this.manifestTab = manifestTab;
    model = new CarrierContentsTableModel();
    setModel(model);
    getColumnModel().getColumn(0).setHeaderValue("Container");
    getColumnModel().getColumn(0).setWidth(150);
    getColumnModel().getColumn(0).setCellRenderer(new ElementTableCellRenderer());
    getColumnModel().getColumn(1).setHeaderValue("Mass");
    getColumnModel().getColumn(1).setMaxWidth(100);
    getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 6799027210269525751L;

      // custom renderer to format decimals and add units
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        DecimalFormat format = new DecimalFormat("0.00");
        setText(format.format(value) + " kg");
        setHorizontalAlignment(JLabel.RIGHT);
        return this;
      }
    });
    getColumnModel().getColumn(2).setHeaderValue("Volume");
    getColumnModel().getColumn(2).setMaxWidth(100);
    getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 6799027210269525751L;

      // custom renderer to format decimals and add units
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        DecimalFormat format = new DecimalFormat("0.000");
        setText(format.format(value) + " m^3");
        setHorizontalAlignment(JLabel.RIGHT);
        return this;
      }
    });
    setBackground(Color.WHITE);
    getTableHeader().setReorderingAllowed(false);
    getTableHeader().setBackground(Color.WHITE);
    getTableHeader().setFont(getTableHeader().getFont().deriveFont(Font.BOLD));
    getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    getTableHeader().setOpaque(false);
  }

  /**
   * Sets the carrier.
   * 
   * @param edge the edge
   * @param carrier the carrier
   */
  public void setCarrier(SupplyEdge edge, I_Carrier carrier) {
    model.setCarrier(edge, carrier);
    model.fireTableDataChanged();
  }

  /**
   * Gets the container.
   * 
   * @param row the row
   * 
   * @return the container
   */
  public I_ResourceContainer getContainer(int row) {
    return ((CarrierContentsTableModel) getModel()).getContainer(row);
  }

  /**
   * Gets the selected container.
   * 
   * @return the selected container
   */
  public I_ResourceContainer getSelectedContainer() {
    int row = getSelectedRow();
    if (row < 0)
      return null;
    else
      return getContainer(row);
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
   * The Class CarrierContentsTableModel.
   */
  class CarrierContentsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;

    private SupplyEdge edge;
    private I_Carrier carrier;

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
      return 3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
      if (edge == null || carrier == null)
        return 0;
      else
        return getManifest().getManifestedContainers(edge, carrier).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0)
        return getContainer(row);
      else if (col == 1)
        return getContainer(row).getMass()
            + getManifest().getCargoMass(getContainer(row), edge.getPoint());
      else
        return getContainer(row).getVolume();
    }

    private I_ResourceContainer getContainer(int row) {
      int lastRowSeen = -1;
      for (I_ResourceContainer container : getManifest().getManifestedContainers(edge, carrier)) {
        lastRowSeen++;
        if (row == lastRowSeen)
          return container;
      }
      return null;
    }

    private void setCarrier(SupplyEdge edge, I_Carrier carrier) {
      this.edge = edge;
      this.carrier = carrier;
    }
  }
}
