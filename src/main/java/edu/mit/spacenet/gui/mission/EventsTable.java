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
package edu.mit.spacenet.gui.mission;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.simulator.MiniSimulator;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * A table used to display and edit the events for a mission.
 * 
 * @author Paul Grogan
 */
public class EventsTable extends JTable {
  private static final long serialVersionUID = -5960985748957372743L;

  private CustomTableModel model;
  private MissionPanel eventsPanel;
  private String[] columnToolTips =
      {"", "Days after mission start", "Relative priority of concurrent events", null};

  /**
   * Instantiates a new event table.
   * 
   * @param tab the missions tab
   */
  public EventsTable(MissionPanel eventsPanel) {
    super();
    this.eventsPanel = eventsPanel;

    model = new CustomTableModel();
    setModel(model);

    getColumnModel().getColumn(0).setHeaderValue("");
    getColumnModel().getColumn(0).setMaxWidth(20);
    getColumnModel().getColumn(1).setHeaderRenderer(new DefaultTableCellRenderer() {
      // custom renderer to show a clock header icon
      private static final long serialVersionUID = 1L;

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/time.png")));
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setBackground(UIManager.getColor("TableHeader.cellBackground"));
        setHorizontalAlignment(CENTER);
        return this;
      }
    });
    getColumnModel().getColumn(1).setMaxWidth(40);
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
    getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
      // custom renderer to show "0.00" format for time
      private static final long serialVersionUID = 7793722593255866883L;

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Double) {
          DecimalFormat format = new DecimalFormat("0.00");
          setText(format.format(value));
          setHorizontalAlignment(CENTER);
        }
        return this;
      }
    });
    getColumnModel().getColumn(2).setHeaderRenderer(new DefaultTableCellRenderer() {
      // custom renderer to show priority header icon
      private static final long serialVersionUID = 1L;

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/priority.png")));
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setBackground(UIManager.getColor("TableHeader.cellBackground"));
        setHorizontalAlignment(CENTER);
        return this;
      }
    });
    getColumnModel().getColumn(2).setMaxWidth(20);
    getColumnModel().getColumn(2).setCellRenderer(renderer);
    getColumnModel().getColumn(3).setHeaderValue("Event Name");
    getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
      // custom renderer to show error icons and messages (if necessary)
      private static final long serialVersionUID = 7793722593255866883L;

      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setIcon(null);
        setIconTextGap(0);
        setToolTipText(null);
        for (SimSpatialError error : getSimulator().getSpatialErrors()) {
          if (error.getEvent().getRoot().equals(getEvent(row))) {
            setIcon(
                new ImageIcon(getClass().getClassLoader().getResource("icons/bullet_error.png")));
            setToolTipText("Error: " + error.getMessage());
            return this;
          }
        }
        return this;
      }
    });

    getTableHeader().setReorderingAllowed(false);
  }

  /**
   * Initializes the table for a new mission.
   */
  public void initialize() {
    model.setMission(eventsPanel.getMissionSplitPane().getMission());
  }

  /**
   * Gets the simulator.
   * 
   * @return the simulator
   */
  private MiniSimulator getSimulator() {
    return eventsPanel.getMissionSplitPane().getMissionsTab().getSimulator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JTable#createDefaultTableHeader()
   */
  protected JTableHeader createDefaultTableHeader() {
    return new JTableHeader(columnModel) {
      private static final long serialVersionUID = 5017703878414116560L;

      public String getToolTipText(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        int index = columnModel.getColumnIndexAtX(p.x);
        int realIndex = columnModel.getColumn(index).getModelIndex();
        return columnToolTips[realIndex];
      }
    };
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JTable#getCellEditor(int, int)
   */
  public TableCellEditor getCellEditor(int row, int col) {
    if (col == 2) {
      JComboBox<String> comboBox = new JComboBox<String>();
      for (int i = 1; i <= 5; i++) {
        comboBox.addItem(new Integer(i).toString());
      }
      comboBox.setSelectedIndex((Integer) getValueAt(row, col) - 1);
      return new DefaultCellEditor(comboBox);
    } else {
      return super.getCellEditor(row, col);
    }
  }

  /**
   * A custom table model to handle event data for missions.
   */
  class CustomTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -678300273079091701L;
    private List<I_Event> data;

    /**
     * Sets the mission.
     * 
     * @param mission the new mission
     */
    public void setMission(Mission mission) {
      if (mission == null)
        data = null;
      else
        data = mission.getEventList();
      fireTableDataChanged();
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
      return data == null ? 0 : data.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
      if (col == 0)
        return false;
      else
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int col) {
      if (col == 0) {
        return ImageIcon.class;
      } else if (col == 1) {
        return Double.class;
      } else if (col == 2) {
        return Integer.class;
      } else {
        return String.class;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0) {
        return data.get(row).getEventType().getIcon();
      } else if (col == 1) {
        return data.get(row).getTime();
      } else if (col == 2) {
        return data.get(row).getPriority();
      } else {
        return data.get(row).getName();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {
      if (col == 1) {
        data.get(row).setTime((Double) value);
      } else if (col == 2) {
        data.get(row).setPriority(Integer.parseInt((String) value));
      } else if (col == 3) {
        data.get(row).setName((String) value);
      }
      Collections.sort(data);
      fireTableDataChanged();
      eventsPanel.getMissionSplitPane().getMissionsTab().updateView();
    }

    /**
     * Gets the event.
     * 
     * @param row the row
     * 
     * @return the event
     */
    public I_Event getEvent(int row) {
      return data.get(row);
    }

    /**
     * Removes the event.
     * 
     * @param event the event
     */
    public void removeEvent(I_Event event) {
      data.remove(event);
      fireTableDataChanged();
    }

    /**
     * Adds the event.
     * 
     * @param event the event
     */
    public void add(I_Event event) {
      data.add(event);
      fireTableDataChanged();
    }
  }

  /**
   * Updates the view.
   */
  public void updateView() {
    int selectedRow = getSelectedRow();
    model.fireTableDataChanged();
    if (getSelectedRow() < getRowCount())
      getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
  }

  /**
   * Gets the event displayed in a particular row.
   * 
   * @param row the row
   * 
   * @return the event
   */
  public I_Event getEvent(int row) {
    return model.getEvent(row);
  }

  /**
   * Gets the selected event.
   * 
   * @return the selected event
   */
  public I_Event getSelectedEvent() {
    if (getSelectedRow() < 0)
      return null;
    else
      return getEvent(getSelectedRow());
  }
}
