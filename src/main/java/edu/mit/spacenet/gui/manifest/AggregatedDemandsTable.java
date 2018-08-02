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

import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyEdge.SupplyPoint;

/**
 * A table for visualizing aggregated demands, and the portion of each demand
 * that has been packed.
 * 
 * @author Paul Grogan
 */
public class AggregatedDemandsTable extends JTable {
	private static final long serialVersionUID = -5960985748957372743L;
	private ManifestTab manifestTab;
	private AggregatedDemandsTableModel model;
	
	/**
	 * Instantiates a new aggregated demands table.
	 * 
	 * @param m the manifest
	 */
	public AggregatedDemandsTable(ManifestTab manifestTab) {
		this.manifestTab = manifestTab;
		model = new AggregatedDemandsTableModel();
		setModel(model);
		getColumnModel().getColumn(0).setHeaderValue("Supply Point");
		getColumnModel().getColumn(0).setMaxWidth(100);
		getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 6799027210269525751L;
			// custom renderer to highlight edge demands with an asterisk
			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column)  {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(getManifest().isEdgeDemand(getDemand(row))) setText(getText() + "*");
				return this;
			}
		});		
		getColumnModel().getColumn(1).setHeaderValue("Resource");
		getColumnModel().getColumn(2).setHeaderValue("Amount");
		getColumnModel().getColumn(2).setMaxWidth(100);
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the decimals
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " " + ((AggregatedDemandsTableModel)getModel()).getDemand(row).getResource().getUnits());
				return this;
			}
		});
		getColumnModel().getColumn(3).setHeaderValue("Packed");
		getColumnModel().getColumn(3).setMaxWidth(100);
		getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 6799027210269525751L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the decimals
			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column)  {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setText(format.format(value) + " " + ((AggregatedDemandsTableModel)getModel()).getDemand(row).getResource().getUnits());
				setHorizontalAlignment(JLabel.RIGHT);
				if(getValueAt(row, column).equals(getValueAt(row, column-1))&&!isSelected) {
					setBackground(new Color(0xcc,0xff,0xcc));
				} else if(!isSelected) {
					setBackground(new Color(0xff,0xcc,0xcc));
				}
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
	 * Initialize.
	 */
	public void initialize() {
		model.fireTableDataChanged();
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
		if(row<0) return null;
		else return getDemand(row);
	}
	
	/**
	 * Gets the supply point.
	 * 
	 * @param row the row
	 * 
	 * @return the supply point
	 */
	public SupplyPoint getSupplyPoint(int row) {
		return model.getSupplyPoint(row);
	}
	
	/**
	 * Gets the selected supply point.
	 * 
	 * @return the selected supply point
	 */
	public SupplyPoint getSelectedSupplyPoint() {
		int row = getSelectedRow();
		if(row<0) return null;
		else return getSupplyPoint(row);
	}
	
	/**
	 * Updates the view.
	 */
	public void updateView() {
		model.fireTableDataChanged();
	}
	
	/**
	 * The Class AggregatedDemandsTableModel.
	 */
	private class AggregatedDemandsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -678300273079091701L;

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 4;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			int i = 0;
			for(SupplyPoint point : getManifest().getAggregatedNodeDemands().keySet()) {
				i+=getManifest().getAggregatedNodeDemands().get(point).size();
				i+=getManifest().getAggregatedEdgeDemands().get(point.getEdge()).size();
			}
			return i;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) return getSupplyPoint(row);
			else if(col==1) return getDemand(row).getResource();
			else if(col==2) return getDemand(row).getAmount();
			else return getManifest().getPackedAmount(getDemand(row));
		}
		
		/**
		 * Gets the demand.
		 * 
		 * @param row the row
		 * 
		 * @return the demand
		 */
		private Demand getDemand(int row) {
			int lastRowSeen = -1;
			for(SupplyPoint point : getManifest().getAggregatedNodeDemands().keySet()) {
				if(lastRowSeen + 
						getManifest().getAggregatedNodeDemands().get(point).size() + 
						getManifest().getAggregatedEdgeDemands().get(point.getEdge()).size() >= row) {
					for(Demand d : getManifest().getAggregatedNodeDemands().get(point)) {
						lastRowSeen++;
						if(lastRowSeen==row) return d;
					}
					for(Demand d : getManifest().getAggregatedEdgeDemands().get(point.getEdge())) {
						lastRowSeen++;
						if(lastRowSeen==row) return d;
					}
					return null;
				}
				lastRowSeen+=getManifest().getAggregatedNodeDemands().get(point).size()+ 
					getManifest().getAggregatedEdgeDemands().get(point.getEdge()).size();
			}
			return null;
		}
		
		/**
		 * Gets the supply point.
		 * 
		 * @param row the row
		 * 
		 * @return the supply point
		 */
		private SupplyPoint getSupplyPoint(int row) {
			return getManifest().getSupplyPoint(getDemand(row));
		}
	}
}
