/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.mit.spacenet.gui.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Table that creates a listing of objects with the first column reserved for
 * a boolean marker, graphically represented by a check box.
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
		if(!objects.contains(t)) {
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
		if(!objects.contains(t)) {
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
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 2;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return objects.size();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		if(col==0) {
			return checked.get(objects.get(row));
		} else {
			return objects.get(row);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
    	if(col==0) return true;
    	else return false;
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		if(col==0) return Boolean.class;
		else return Object.class;
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int col) {
    	if(col==0) {
    		checked.put(objects.get(row), (Boolean)value);
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
		for(T t : objects) {
			if(checked.get(t).equals(Boolean.FALSE)) {
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
		for(T t : objects) {
			if(checked.get(t).equals(Boolean.TRUE)) {
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
		if(checked.containsKey(t)) {
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
		if(checked.containsKey(t)) {
			checked.put(t, false);
			fireTableCellUpdated(objects.indexOf(t), 0);
		}
	}
	
	/**
	 * Checks all objects in the list.
	 */
	public void selectAll() {
		for(T t : checked.keySet()) {
			checked.put(t, Boolean.TRUE);
		}
		fireTableDataChanged();
	}
	
	/**
	 * Unchecks all objects in the list.
	 */
	public void deselectAll() {
		for(T t : checked.keySet()) {
			checked.put(t, Boolean.FALSE);
		}
		fireTableDataChanged();
	}
}
