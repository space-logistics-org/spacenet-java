/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mit.spacenet.gui.event;

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
import edu.mit.spacenet.domain.resource.DemandSet;
import edu.mit.spacenet.domain.resource.I_Resource;

/**
 * The Class TransferDemandsTable.
 * 
 * @author Paul Grogan
 */
public class TransferDemandsTable extends JTable {
	private static final long serialVersionUID = 2409986766964137963L;
	
	/**
	 * Instantiates a new transfer demands table.
	 */
	public TransferDemandsTable() {
		setModel(new TransferDemandsModel());
		getColumnModel().getColumn(0).setHeaderValue("Resource");
		getColumnModel().getColumn(1).setHeaderValue("Amount");
		getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.000");
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " " + ((TransferDemandsModel)getModel()).getContainerContents(row).getResource().getUnits());
				return this;
			}
		});
		getColumnModel().getColumn(2).setHeaderValue("Transferred");
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.000");
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " " + ((TransferDemandsModel)getModel()).getContainerContents(row).getResource().getUnits());
				return this;
			}
		});
		setBackground(Color.WHITE);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setBackground(Color.WHITE);
		getTableHeader().setFont(getTableHeader().getFont().deriveFont(Font.BOLD));
		getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		getTableHeader().setOpaque(false);
	}
	
	/**
	 * Gets the transfer demands.
	 * 
	 * @return the transfer demands
	 */
	public DemandSet getTransferDemands() {
		return ((TransferDemandsModel)getModel()).getTransferDemands();
	}
	
	/**
	 * Sets the data.
	 * 
	 * @param container the container
	 * @param demands the demands
	 */
	public void setData(I_ResourceContainer container, DemandSet demands) {
		((TransferDemandsModel)getModel()).setData(container, demands);
	}
	private class TransferDemandsModel extends AbstractTableModel {
		private static final long serialVersionUID = -8936343745298804353L;
		private I_ResourceContainer container;
		private DemandSet demands;
		
		public int getColumnCount() {
			return 3;
		}
		public Class<?> getColumnClass(int col) {
			if(col==0) return Object.class;
			else return Double.class;
		}
		public boolean isCellEditable(int row, int col) {
			if(col==2) return true;
			else return false;
		}
		public int getRowCount() {
			if(container==null) return 0;
			else return container.getContents().keySet().size();
		}
		public Object getValueAt(int row, int col) {
			if(col==0) return getContainerContents(row).getResource();
			else if(col==1) return getContainerContents(row).getAmount();
			else return getTransferDemand(row)==null?0:getTransferDemand(row).getAmount();
		}
		public void setValueAt(Object value, int row, int col) {
			if(col==2) {
				double amount = Math.min((Double)value, getContainerContents(row).getAmount());
				if(getTransferDemand(row)==null) {
					demands.add(new Demand(getContainerContents(row).getResource(), amount));
				} else {
					getTransferDemand(row).setAmount(amount);
				}
				fireTableRowsUpdated(row, row);
			}
		}
		public Demand getContainerContents(int row) {
			int lastRowSeen = -1;
			for(I_Resource resource : container.getContents().keySet()) {
				lastRowSeen++;
				if(lastRowSeen==row) return new Demand(resource, container.getContents().get(resource));
			}
			return null;
		}
		public Demand getTransferDemand(int row) {
			int lastRowSeen = -1;
			for(I_Resource resource : container.getContents().keySet()) {
				lastRowSeen++;
				if(lastRowSeen==row) {
					for(Demand demand : demands) {
						if(resource.equals(demand.getResource())) return demand;
					}
					return null;
				}
			}
			return null;
		}
		public void setData(I_ResourceContainer container, DemandSet demands) {
			this.container = container;
			this.demands = new DemandSet();
			for(Demand demand : demands) {
				if(container.getContents().keySet().contains(demand.getResource())) {
					this.demands.add(demand);
				}
			}
			fireTableDataChanged();
		}
		public DemandSet getTransferDemands() {
			if(this.demands==null) return new DemandSet();
			DemandSet demands = new DemandSet();
			for(Demand demand : this.demands) {
				if(container.getContents().keySet().contains(demand.getResource())
						&& demand.getAmount()>0) {
					demands.add(demand);
				}
			}
			return demands;
		}
	}
}