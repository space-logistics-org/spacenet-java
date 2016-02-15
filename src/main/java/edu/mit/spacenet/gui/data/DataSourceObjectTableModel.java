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
package edu.mit.spacenet.gui.data;

import javax.swing.table.AbstractTableModel;

import edu.mit.spacenet.data.I_DataSource;

/**
 * An implementation of a table model used to display the contents of a data
 * source.
 */
public class DataSourceObjectTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2710558664475507700L;
	public static final int NODE = 1, EDGE = 2, RESOURCE = 3, ELEMENT = 4;
	
	private int objectType;
	private I_DataSource dataSource;
	
	/**
	 * Instantiates a new data source object table model.
	 * 
	 * @param objectType the object type
	 * @param dataSource the data source
	 */
	public DataSourceObjectTableModel(int objectType, I_DataSource dataSource) {
		if(objectType<NODE||objectType>ELEMENT) 
			throw new IllegalArgumentException("Illegal Object Type");
		this.objectType = objectType;
		this.dataSource = dataSource;
	}
	
	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(I_DataSource dataSource) {
		this.dataSource = dataSource;
		fireTableDataChanged();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		switch(objectType) {
		case NODE: return 3;
		case EDGE: return 5;
		case RESOURCE: return 4;
		case ELEMENT: return 3;
		default: return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		switch(objectType) {
		case NODE: 
			switch(columnIndex) {
			case 0: return "ID";
			case 1: return "Type";
			case 2: return "Name";
			default: return null;
			}
		case EDGE: 
			switch(columnIndex) {
			case 0: return "ID";
			case 1: return "Type";
			case 2: return "Name";
			case 3: return "Origin";
			case 4: return "Destination";
			default: return null;
			}
		case RESOURCE: 
			switch(columnIndex) {
			case 0: return "ID";
			case 1: return "Type";
			case 2: return "Name";
			case 3: return "Class of Supply";
			default: return null;
			}
		case ELEMENT: 
			switch(columnIndex) {
			case 0: return "ID";
			case 1: return "Icon";
			case 2: return "Name";
			default: return null;
			}
		default: return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if(dataSource==null) return 0;
		switch(objectType) {
		case NODE: return dataSource.getNodeLibrary().size();
		case EDGE: return dataSource.getEdgeLibrary().size();
		case RESOURCE: return dataSource.getResourceLibrary().size();
		case ELEMENT: return dataSource.getElementPreviewLibrary().size();
		default: return 0;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(objectType) {
		case NODE: 
			switch(columnIndex) {
			case 0: return dataSource.getNodeLibrary().get(rowIndex).getTid();
			case 1: return dataSource.getNodeLibrary().get(rowIndex).getNodeType();
			case 2: return dataSource.getNodeLibrary().get(rowIndex).getName();
			default: return null;
			}
		case EDGE: 
			switch(columnIndex) {
			case 0: return dataSource.getEdgeLibrary().get(rowIndex).getTid();
			case 1: return dataSource.getEdgeLibrary().get(rowIndex).getEdgeType();
			case 2: return dataSource.getEdgeLibrary().get(rowIndex).getName();
			case 3: return dataSource.getEdgeLibrary().get(rowIndex).getOrigin();
			case 4: return dataSource.getEdgeLibrary().get(rowIndex).getDestination();
			default: return null;
			}
		case RESOURCE: 
			switch(columnIndex) {
			case 0: return dataSource.getResourceLibrary().get(rowIndex).getTid();
			case 1: return dataSource.getResourceLibrary().get(rowIndex).getResourceType();
			case 2: return dataSource.getResourceLibrary().get(rowIndex).getName();
			case 3: return dataSource.getResourceLibrary().get(rowIndex).getClassOfSupply();
			default: return null;
			}
		case ELEMENT: 
			switch(columnIndex) {
			case 0: return dataSource.getElementPreviewLibrary().get(rowIndex).ID;
			case 1: return dataSource.getElementPreviewLibrary().get(rowIndex);
			case 2: return dataSource.getElementPreviewLibrary().get(rowIndex).NAME;
			default: return null;
			}
		default: return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		if(col==0) return Integer.class;
		else return Object.class;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
