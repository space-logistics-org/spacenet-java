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
package edu.mit.spacenet.gui.event;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;

/**
 * A table for managing EVAs and explorations by selecting crew and EVA states.
 * 
 * @author Paul Grogan
 */
public class EvaTable extends JTable {
  private static final long serialVersionUID = 764356022370707951L;
  private EvaTableModel model;

  /**
   * Instantiates a new eva table.
   */
  public EvaTable() {
    model = new EvaTableModel();
    setModel(model);

    getColumnModel().getColumn(0).setHeaderValue("EVA?");
    getColumnModel().getColumn(0).setMaxWidth(50);
    getColumnModel().getColumn(1).setHeaderValue("Crew Member");
    getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof CrewMember)
          setIcon(ElementType.CREW_MEMBER.getIconType().getIcon());
        return this;
      }
    });
    getColumnModel().getColumn(2).setHeaderValue("EVA State");
    getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
      private static final long serialVersionUID = 1L;

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null && value instanceof I_State)
          setIcon(((I_State) value).getStateType().getIcon());
        else
          setIcon(null);
        return this;
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JTable#getModel()
   */
  public EvaTableModel getModel() {
    return model;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.JTable#getCellEditor(int, int)
   */
  public TableCellEditor getCellEditor(int row, int column) {
    if (column == 2) {
      JComboBox<I_State> stateCombo = new JComboBox<I_State>();
      stateCombo.setRenderer(new DefaultListCellRenderer() {
        private static final long serialVersionUID = -2255885956722142642L;

        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
          super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
          if (value instanceof I_State)
            setIcon(((I_State) value).getStateType().getIcon());
          return this;
        }
      });
      for (I_State state : ((I_Element) getValueAt(row, 1)).getStates()) {
        stateCombo.addItem(state);
      }
      return new DefaultCellEditor(stateCombo);
    } else
      return super.getCellEditor(row, column);
  }

  /**
   * The Class EvaTableModel.
   */
  public class EvaTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -6033117922087345936L;
    private SortedMap<I_Element, I_State> data;
    private Map<I_Element, Boolean> visibility;

    /**
     * Instantiates a new eva table model.
     */
    public EvaTableModel() {
      data = new TreeMap<I_Element, I_State>();
      visibility = new HashMap<I_Element, Boolean>();
    }

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
      return data.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
      if (col == 0)
        return visibility.get(getValueAt(row, 1));
      else if (col == 1)
        return data.keySet().toArray()[row];
      else {
        if (visibility.get(getValueAt(row, 1)))
          return data.get(getValueAt(row, 1));
        else
          return null;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int col) {
      if (col == 0)
        return Boolean.class;
      else if (col == 1)
        return Object.class;
      else
        return Object.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
      if (col == 0)
        return true;
      else if (col == 1)
        return false;
      else
        return (Boolean) getValueAt(row, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {
      if (col == 0) {
        if ((Boolean) value) {
          I_State evaState = ((I_Element) getValueAt(row, 1)).getCurrentState();
          for (I_State s : ((I_Element) getValueAt(row, 1)).getStates()) {
            if (s.getName().toLowerCase().contains("eva")) {
              evaState = s;
              break;
            }
          }
          data.put((I_Element) getValueAt(row, 1), evaState);
        }
        visibility.put((I_Element) getValueAt(row, 1), (Boolean) value);
      } else
        data.put((I_Element) getValueAt(row, 1), (I_State) value);
      fireTableRowsUpdated(row, row);
    }

    /**
     * Put.
     * 
     * @param c the c
     * @param s the s
     * @param isVisible the is visible
     */
    public void put(I_Element c, I_State s, boolean isVisible) {
      data.put(c, s);
      visibility.put(c, isVisible);
      fireTableDataChanged();
    }

    /**
     * Clear.
     */
    public void clear() {
      data.clear();
      visibility.clear();
      fireTableDataChanged();
    }

    /**
     * Gets the data.
     * 
     * @return the data
     */
    public SortedMap<I_Element, I_State> getData() {
      TreeMap<I_Element, I_State> retData = new TreeMap<I_Element, I_State>();
      for (I_Element c : data.keySet()) {
        if (visibility.get(c)) {
          retData.put(c, data.get(c));
        }
      }
      return retData;
    }
  }
}
