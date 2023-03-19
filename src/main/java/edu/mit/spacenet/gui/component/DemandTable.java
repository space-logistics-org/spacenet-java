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
package edu.mit.spacenet.gui.component;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.GenericResource;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.domain.resource.ResourceType;
import edu.mit.spacenet.gui.renderer.ResourceTypeTableCellRenderer;

/**
 * A table used to display a set of demands with editable fields.
 * 
 * @author Paul Grogan
 */
public class DemandTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;

  private List<I_Resource> resourceTypeList;
  private CustomTableModel model;

  /**
   * The constructor.
   * 
   * @param list the list of available resources
   */
  public DemandTable(List<I_Resource> list) {
    super();
    this.resourceTypeList = list;

    model = new CustomTableModel();
    setModel(model);
    getColumnModel().getColumn(0).setHeaderValue("Type");
    getColumnModel().getColumn(0).setCellRenderer(new ResourceTypeTableCellRenderer());
    getColumnModel().getColumn(0).setMaxWidth(100);
    getColumnModel().getColumn(1).setHeaderValue("Resource");
    getColumnModel().getColumn(2).setHeaderValue("Environment");
    getColumnModel().getColumn(2).setMaxWidth(75);
    getColumnModel().getColumn(3).setHeaderValue("Amount");
    getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 2092491034324672219L;
      private DecimalFormat format = new DecimalFormat("0.00");

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, format.format(value), isSelected, hasFocus, row,
            column);
        return this;
      }
    });
    getColumnModel().getColumn(3).setMaxWidth(75);
    getColumnModel().getColumn(4).setHeaderValue("Units");
    getColumnModel().getColumn(4).setMaxWidth(75);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(JLabel.CENTER);
    getColumnModel().getColumn(4).setCellRenderer(renderer);
    getTableHeader().setReorderingAllowed(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JTable#getCellEditor(int, int)
   */
  public TableCellEditor getCellEditor(int row, int col) {
    if (col == 0) {
      JComboBox<ResourceType> comboBox = new JComboBox<ResourceType>();
      comboBox.setRenderer(new DefaultListCellRenderer() {
        private static final long serialVersionUID = -8350081466318995133L;

        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
          JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
              cellHasFocus);
          label.setIcon(((ResourceType) value).getIcon());
          return label;
        }
      });
      for (ResourceType t : ResourceType.values()) {
        comboBox.addItem(t);
      }
      comboBox.setSelectedItem(getValueAt(row, col));
      return new DefaultCellEditor(comboBox);
    } else if (col == 1) {
      JComboBox<I_Resource> comboBox = new JComboBox<I_Resource>();
      for (I_Resource r : resourceTypeList) {
        if (this.getModel().getValueAt(row, 0).equals(ResourceType.RESOURCE)) {
          if (r.getClass().equals(Resource.class))
            comboBox.addItem(r);
        } else if (this.getModel().getValueAt(row, 0).equals(ResourceType.ITEM)) {
          if (r.getClass().equals(Item.class))
            comboBox.addItem(r);
        }
      }
      if (this.getModel().getValueAt(row, 0).equals(ResourceType.GENERIC)) {
        for (ClassOfSupply cos : ClassOfSupply.values()) {
          comboBox.addItem(new GenericResource(cos));
        }
      }
      return new DefaultCellEditor(comboBox);
    } else if (col == 2) {
      JComboBox<Environment> comboBox = new JComboBox<Environment>();
      comboBox.setRenderer(new DefaultListCellRenderer() {
        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
          JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
              cellHasFocus);
          return label;
        }
      });
      for (Environment e : Environment.values()) {
        comboBox.addItem(e);
      }
      comboBox.setSelectedItem(getValueAt(row, col));
      return new DefaultCellEditor(comboBox);
    } else {
      return super.getCellEditor(row, col);
    }
  }

  private class CustomTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;
    private List<Demand> data;
    private Map<Demand, ResourceType> dataType;

    public CustomTableModel() {
      super();
      data = new ArrayList<Demand>();
      dataType = new HashMap<Demand, ResourceType>();
    }

    public int getColumnCount() {
      return 5;
    }

    public int getRowCount() {
      return data.size();
    }

    public boolean isCellEditable(int row, int col) {
      if (col == 2) {
        return data.get(row).getResource() != null && data.get(row).getResource().getResourceType() == ResourceType.GENERIC;
      } else if (col == 4) {
        return false;
      } else {
        return true;
      }
    }

    public Class<?> getColumnClass(int col) {
      if (col == 0) {
        return ResourceType.class;
      } else if (col == 1) {
        return Demand.class;
      } else if (col == 2) {
        return Environment.class;
      } else if (col == 3) {
        return Double.class;
      } else
        return String.class;
    }

    public Object getValueAt(int row, int col) {
      if (col == 0) {
        return dataType.get(data.get(row));
      } else if (col == 1) {
        return data.get(row).getResource();
      } else if (col == 2) {
        if (data.get(row).getResource() != null) {
          return data.get(row).getResource().getEnvironment();
        } else {
          return Environment.UNPRESSURIZED;
        }
      } else if (col == 3) {
        return data.get(row).getAmount();
      } else {
        if (data.get(row).getResource() != null)
          return data.get(row).getResource().getUnits();
        else
          return "";
      }
    }

    public void setValueAt(Object value, int row, int col) {
      if (col == 0) {
        dataType.put(data.get(row), (ResourceType) value);
      } else if (col == 1) {
        data.get(row).setResource((I_Resource) value);
        fireTableCellUpdated(row, 2);
        fireTableCellUpdated(row, 4);
      } else if (col == 2) {
        if(data.get(row).getResource() != null) {
          data.get(row).getResource().setEnvironment((Environment) value);
        }
      } else {
        data.get(row).setAmount((Double) value);
      }
      fireTableCellUpdated(row, col);
    }

    public List<Demand> getData() {
      return data;
    }

    public void addRow(Demand demand) {
      data.add(demand);
      if (demand.getResource() == null) {
        dataType.put(demand, ResourceType.GENERIC);
      } else if (demand.getResource().getResourceType() == ResourceType.GENERIC) {
        dataType.put(demand, ResourceType.GENERIC);
      } else if (demand.getResource().getResourceType() == ResourceType.RESOURCE) {
        dataType.put(demand, ResourceType.RESOURCE);
      } else {
        dataType.put(demand, ResourceType.ITEM);
      }
      fireTableRowsInserted(data.indexOf(demand), data.indexOf(demand));
    }

    public void remove(Demand demand) {
      int index = data.indexOf(demand);
      data.remove(demand);
      fireTableRowsDeleted(index, index);
    }
  }

  /**
   * Adds the demand.
   * 
   * @param demand the demand
   */
  public void addDemand(Demand demand) {
    model.addRow(demand);
  }

  /**
   * Gets the demands.
   * 
   * @return the demands
   */
  public List<Demand> getDemands() {
    return model.getData();
  }

  /**
   * Gets the demand.
   * 
   * @param row the row
   * 
   * @return the demand
   */
  public Demand getDemand(int row) {
    return model.getData().get(row);
  }

  /**
   * Removes the.
   * 
   * @param demand the demand
   */
  public void remove(Demand demand) {
    model.remove(demand);
  }

  /**
   * Removes the all demands.
   */
  public void removeAllDemands() {
    while (model.getRowCount() > 0) {
      model.remove(getDemand(0));
    }
  }
}
