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

import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyPoint;

/**
 * A table for viewing the packed contents of a container.
 */
public class ContainerContentsTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;
  private ManifestTab manifestTab;
  private ContainerContentsTableModel model;

  /**
   * Instantiates a new container contents table.
   * 
   * @param manifestTab the manifest tab
   */
  public ContainerContentsTable(ManifestTab manifestTab) {
    this.manifestTab = manifestTab;
    model = new ContainerContentsTableModel();
    setModel(model);
    getColumnModel().getColumn(0).setHeaderValue("Supply Point");
    getColumnModel().getColumn(0).setMaxWidth(100);
    getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 6799027210269525751L;

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (getManifest().isEdgeDemand(getManifest().getDemand(getDemand(row))))
          setText(getText() + "*");
        return this;
      }
    });
    getColumnModel().getColumn(1).setHeaderValue("Resource");
    getColumnModel().getColumn(2).setHeaderValue("Amount");
    getColumnModel().getColumn(2).setMaxWidth(100);
    getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 6799027210269525751L;
      private DecimalFormat format = new DecimalFormat("0.00");

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(format.format(value) + " " + model.getDemand(row).getResource().getUnits());
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
   * Sets the container.
   * 
   * @param container the container
   * @param point the point
   */
  public void setContainer(I_ResourceContainer container, SupplyPoint point) {
    model.setContainer(container, point);
    model.fireTableDataChanged();
  }

  /**
   * Gets the demand.
   * 
   * @param row the row
   * 
   * @return the demand
   */
  public Demand getDemand(int row) {
    return model.getDemand(row);
  }

  /**
   * Gets the selected demand.
   * 
   * @return the selected demand
   */
  public Demand getSelectedDemand() {
    int row = getSelectedRow();
    if (row < 0)
      return null;
    else
      return getDemand(row);
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
   * The Class ContainerContentsTableModel.
   */
  class ContainerContentsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;

    private I_ResourceContainer container;
    private SupplyPoint point;

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
      if (container == null)
        return 0;
      else
        return getManifest().getPackedDemands(container, point).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0)
        return point;
      else if (col == 1)
        return getDemand(row).getResource();
      else
        return getDemand(row).getAmount();
    }

    private Demand getDemand(int row) {
      int lastRowSeen = -1;
      for (Demand demand : getManifest().getPackedDemands(container, point)) {
        lastRowSeen++;
        if (row == lastRowSeen)
          return demand;
      }
      return null;
    }

    private void setContainer(I_ResourceContainer container, SupplyPoint point) {
      this.container = container;
      this.point = point;
    }
  }
}
