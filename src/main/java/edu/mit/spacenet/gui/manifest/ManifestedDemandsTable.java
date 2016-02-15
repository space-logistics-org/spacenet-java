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
package edu.mit.spacenet.gui.manifest;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.SupplyEdge;

/**
 * A table for viewing the available carriers for manifesting.
 * 
 * @author Paul Grogan
 */
public class ManifestedDemandsTable extends JTable {
	private static final long serialVersionUID = -5960985748957372743L;
	private ManifestTab manifestTab;
	private ManifestedDemandsTableModel model;
	/**
	 * Instantiates a new manifested demands table.
	 * 
	 * @param manifest the manifest
	 */
	public ManifestedDemandsTable(ManifestTab manifestTab) {
		this.manifestTab = manifestTab;
		model = new ManifestedDemandsTableModel();	
		setModel(model);
		getColumnModel().getColumn(0).setHeaderValue("Supply Edge");
		getColumnModel().getColumn(0).setWidth(150);
		getColumnModel().getColumn(1).setHeaderValue("Carrier");
		
		setBackground(Color.WHITE);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setBackground(Color.WHITE);
		getTableHeader().setFont(getTableHeader().getFont().deriveFont(Font.BOLD));
		getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		getTableHeader().setOpaque(false);
	}
	
	/**
	 * Gets the supply edge.
	 * 
	 * @param row the row
	 * 
	 * @return the supply edge
	 */
	public SupplyEdge getSupplyEdge(int row) {
		return model.getEdge(row);
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
	 * Gets the carrier.
	 * 
	 * @param row the row
	 * 
	 * @return the carrier
	 */
	public I_Carrier getCarrier(int row) {
		return model.getCarrier(row);
	}
	
	/**
	 * Gets the selected carrier.
	 * 
	 * @return the selected carrier
	 */
	public I_Carrier getSelectedCarrier() {
		int row = getSelectedRow();
		if(row<0) return null;
		else return getCarrier(row);
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
	 * The Class ManifestedDemandsTableModel.
	 */
	class ManifestedDemandsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -678300273079091701L;
		
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
			int i = 0;
			for(SupplyEdge edge : getManifest().getSupplyEdges()) {
				i+=edge.getCarriers().size();
			}
			return i;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) return getEdge(row);
			else return getCarrier(row);
		}
		private SupplyEdge getEdge(int row) {
			int lastRowSeen = -1;
			for(SupplyEdge edge : getManifest().getSupplyEdges()) {
				if(lastRowSeen + edge.getCarriers().size() >= row) {
					return edge;
				}
				lastRowSeen+=edge.getCarriers().size();
			}
			return null;
		}
		private I_Carrier getCarrier(int row) {
			int lastRowSeen = -1;
			for(SupplyEdge edge : getManifest().getSupplyEdges()) {
				if(lastRowSeen + edge.getCarriers().size() >= row) {
					for(I_Carrier carrier : edge.getCarriers()) {
						lastRowSeen++;
						if(lastRowSeen==row) return carrier;
					}
					return null;
				}
				lastRowSeen+=edge.getCarriers().size();
			}
			return null;
		}
	}
}
