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

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.gui.renderer.ElementTableCellRenderer;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.scenario.SupplyEdge.SupplyPoint;

/**
 * The table used to view the available resource containers for demand packing.
 * 
 * @author Paul Grogan
 */
public class PackedDemandsTable extends JTable {
	private static final long serialVersionUID = -5960985748957372743L;
	private ManifestTab manifestTab;
	private PackedDemandsTableModel model;
	/**
	 * Instantiates a new packed demands table.
	 * 
	 * @param manifestTab the manifest tab
	 */
	public PackedDemandsTable(ManifestTab manifestTab) {
		this.manifestTab = manifestTab;
		model = new PackedDemandsTableModel();
		setModel(model);
		getColumnModel().getColumn(0).setHeaderValue("Supply Point");
		getColumnModel().getColumn(0).setMaxWidth(100);
		getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 6799027210269525751L;
			// custom renderer to show edge demands with an asterisk
			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column)  {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(getManifest().isEdgeDemand(getContainer(row))) setText(getText() + "*");
				return this;
			}
		});		
		getColumnModel().getColumn(1).setHeaderValue("Container");
		getColumnModel().getColumn(1).setCellRenderer(new ElementTableCellRenderer());
		getColumnModel().getColumn(2).setHeaderValue("Supply Edge");
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 6799027210269525751L;
			// custom renderer to highlight selection
			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column)  {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value!=null&&!isSelected) {
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
	 * Gets the manifest.
	 * 
	 * @return the manifest
	 */
	private Manifest getManifest() {
		return manifestTab.getManifest();
	}
	
	/**
	 * Gets the container.
	 * 
	 * @param row the row
	 * 
	 * @return the container
	 */
	public I_ResourceContainer getContainer(int row) {
		return ((PackedDemandsTableModel)getModel()).getContainer(row);
	}
	
	/**
	 * Gets the selected container.
	 * 
	 * @return the selected container
	 */
	public I_ResourceContainer getSelectedContainer() {
		int row = getSelectedRow();
		if(row<0) return null;
		else return getContainer(row);
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
	 * Gets the supply edge.
	 * 
	 * @param row the row
	 * 
	 * @return the supply edge
	 */
	public SupplyEdge getSupplyEdge(int row) {
		return model.getSupplyEdge(row);
	}
	
	/**
	 * Gets the selected supply edge.
	 * 
	 * @return the selected supply edge
	 */
	public SupplyEdge getSelectedSupplyEdge() {
		int row = getSelectedRow();
		if(row<0) return null;
		else return getSupplyEdge(row);
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
	 * The Class PackedDemandsTableModel.
	 */
	class PackedDemandsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -678300273079091701L;
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 3;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			int rows = getManifest().getEmptyContainers().size();
			for(SupplyPoint point : getManifest().getSupplyPoints()) {
				rows+=getManifest().getCachedContainerDemands().get(point).size();
			}
			return rows;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) return getSupplyPoint(row);
			else if(col==1) return getContainer(row);
			else return getSupplyEdge(row);
		}
		
		/**
		 * Gets the container.
		 * 
		 * @param row the row
		 * 
		 * @return the container
		 */
		private I_ResourceContainer getContainer(int row) {
			int lastRowSeen = -1;
			for(I_ResourceContainer container : getManifest().getEmptyContainers()) {
				lastRowSeen++;
				if(lastRowSeen==row) return container;
			}
			for(SupplyPoint point : getManifest().getSupplyPoints()) {
				for(I_ResourceContainer container : getManifest().getCachedContainerDemands().get(point)) {
					lastRowSeen++;
					if(lastRowSeen==row) return container;
				}
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
			int lastRowSeen = getManifest().getEmptyContainers().size()-1;
			if(row<=lastRowSeen) return null;
			for(SupplyPoint point : getManifest().getSupplyPoints()) {
				lastRowSeen+=getManifest().getCachedContainerDemands().get(point).size();
				if(row<=lastRowSeen) return point;
			}
			return null;
		}
		
		/**
		 * Gets the supply edge.
		 * 
		 * @param row the row
		 * 
		 * @return the supply edge
		 */
		private SupplyEdge getSupplyEdge(int row) {
			SupplyPoint point = getSupplyPoint(row);
			I_ResourceContainer container = getContainer(row);
			
			if(point==null) return null;
			for(SupplyEdge edge : getManifest().getSupplyEdges()) {
				if(edge.getDestination().equals(point.getNode())
						&& edge.getEndTime()<=point.getTime()) {
					for(I_Carrier carrier : edge.getCarriers()) {
						if(getManifest().getManifestedContainers().get(edge).get(carrier).contains(container))
							return edge;
					}
				}
			}
			return null;
		}
	}
}
