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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.domain.resource.ResourceType;

/**
 * A custom JTable that does not display horizontal or vertical lines and
 * attempts to show an icons for associated object types.
 */
public class DataSourceObjectTable extends JTable {
	private static final long serialVersionUID = -5232329542188811358L;

	/**
	 * Instantiates a new data source object table.
	 * 
	 * @param tableModel the table model
	 */
	public DataSourceObjectTable(TableModel tableModel) {
		super(tableModel);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setFont(getTableHeader().getFont().deriveFont(Font.BOLD));
		if(getColumnModel().getColumnCount()>0)
			getColumnModel().getColumn(0).setMaxWidth(30);
		if(getColumnModel().getColumnCount()>1)
			getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
	                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                if(value instanceof ResourceType)
	                	setIcon(((ResourceType)value).getIcon());
	                else if(value instanceof EdgeType)
	                	setIcon(((EdgeType)value).getIcon());
	                else if(value instanceof NodeType)
	                	setIcon(((NodeType)value).getIcon());
	                else if(value instanceof ElementPreview) {
	                	setText(((ElementPreview)value).TYPE.getName());
	                	setIcon(((ElementPreview)value).getIconType().getIcon());
	                } return this;
				}
			});
		if(getColumnModel().getColumnCount()>3)
			getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
	                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                if(value instanceof Node)
	                	setIcon(((Node)value).getNodeType().getIcon());
	                return this;
				}
			});
		if(getColumnModel().getColumnCount()>4)
			getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
	                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                if(value instanceof Node)
	                	setIcon(((Node)value).getNodeType().getIcon());
	                return this;
				}
			});
	}
}
