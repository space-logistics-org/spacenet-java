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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Table that creates a listing of objects with the first column reserved for a boolean marker,
 * graphically represented by a check box.
 * 
 * @param <T> the object type to list
 * 
 * @author Paul Grogan
 */
public class CheckBoxTableModel<T> extends AbstractTableModel {
  private static final long serialVersionUID = 4160394859308981241L;
  private List<T> objects;
  private Map<T, Boolean> checked;

  /**
   * The constructor.
   */
  public CheckBoxTableModel() {
    objects = new ArrayList<T>();
    checked = new HashMap<T, Boolean>();
  }

  /**
   * Adds a new object to the listing.
   * 
   * @param t the object to add
   */
  public void addObject(T t) {
    if (!objects.contains(t)) {
      objects.add(t);
      checked.put(t, true);
      fireTableRowsInserted(objects.indexOf(t), objects.indexOf(t));
    }
  }

  /**
   * Adds a new object to the listing with passed checked state.
   * 
   * @param t the object to add
   * @param isChecked whether the object should be checked
   */
  public void addObject(T t, boolean isChecked) {
    if (!objects.contains(t)) {
      objects.add(t);
      checked.put(t, isChecked);
      fireTableRowsInserted(objects.indexOf(t), objects.indexOf(t));
    }
  }

  /**
   * Clears the list of objects.
   */
  public void clear() {
    objects.clear();
    checked.clear();
    fireTableDataChanged();
  }

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
    return objects.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt(int row, int col) {
    if (col == 0) {
      return checked.get(objects.get(row));
    } else {
      return objects.get(row);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  public boolean isCellEditable(int row, int col) {
    if (col == 0)
      return true;
    else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  public Class<?> getColumnClass(int col) {
    if (col == 0)
      return Boolean.class;
    else
      return Object.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  public void setValueAt(Object value, int row, int col) {
    if (col == 0) {
      checked.put(objects.get(row), (Boolean) value);
    } else {
      throw new RuntimeException("Cannot modify objects");
    }
    fireTableCellUpdated(row, col);
  }

  /**
   * Gets a list of the unchecked objects.
   * 
   * @return the list of unchecked objects
   */
  public List<T> getDeselectedObjects() {
    ArrayList<T> deselected = new ArrayList<T>();
    for (T t : objects) {
      if (checked.get(t).equals(Boolean.FALSE)) {
        deselected.add(t);
      }
    }
    return deselected;
  }

  /**
   * Gets a list of the checked objects.
   * 
   * @return the list of checked objects
   */
  public List<T> getSelectedObjects() {
    ArrayList<T> selected = new ArrayList<T>();
    for (T t : objects) {
      if (checked.get(t).equals(Boolean.TRUE)) {
        selected.add(t);
      }
    }
    return selected;
  }

  /**
   * Gets all the objects.
   * 
   * @return the objects
   */
  public List<T> getAllObjects() {
    return objects;
  }

  /**
   * Checks an object.
   * 
   * @param t the object
   */
  public void selectObject(T t) {
    if (checked.containsKey(t)) {
      checked.put(t, true);
      fireTableCellUpdated(objects.indexOf(t), 0);
    }
  }

  /**
   * Unchecks an object.
   * 
   * @param t the object to uncheck
   */
  public void deselectObject(T t) {
    if (checked.containsKey(t)) {
      checked.put(t, false);
      fireTableCellUpdated(objects.indexOf(t), 0);
    }
  }

  /**
   * Checks all objects in the list.
   */
  public void selectAll() {
    for (T t : checked.keySet()) {
      checked.put(t, Boolean.TRUE);
    }
    fireTableDataChanged();
  }

  /**
   * Unchecks all objects in the list.
   */
  public void deselectAll() {
    for (T t : checked.keySet()) {
      checked.put(t, Boolean.FALSE);
    }
    fireTableDataChanged();
  }
}
