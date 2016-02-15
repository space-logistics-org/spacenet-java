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
package edu.mit.spacenet.gui.network;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.renderer.EdgeTableCellRenderer;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;

/**
 * A table to view and toggle the inclusion of edges in the scenario network.
 * 
 * @author Paul Grogan
 */
public class EdgeTable extends JTable {
	private static final long serialVersionUID = -5960985748957372743L;
	
	private CustomTableModel model;
	private NetworkTab networkTab;
	
	/**
	 * Instantiates a new edge table.
	 * 
	 * @param networkTab the network tab
	 */
	public EdgeTable(NetworkTab networkTab) {
		super();
		this.networkTab = networkTab;
		
		model = new CustomTableModel();
		setModel(model);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
		getColumnModel().getColumn(0).setMaxWidth(25);
		getColumnModel().getColumn(1).setHeaderValue("Name");
		getColumnModel().getColumn(1).setCellRenderer(new EdgeTableCellRenderer());
		getColumnModel().getColumn(2).setHeaderValue("Orig.");
		getColumnModel().getColumn(2).setCellRenderer(renderer);
		getColumnModel().getColumn(2).setMaxWidth(45);
		getColumnModel().getColumn(3).setHeaderValue("Dest.");
		getColumnModel().getColumn(3).setCellRenderer(renderer);
		getColumnModel().getColumn(3).setMaxWidth(45);
		
		getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Initializes the edge table to a new scenario.
	 */
	public void initialize() {
		((CustomTableModel)getModel()).setScenario(networkTab.getScenario());
	}
	
	/**
	 * Update view.
	 */
	public void updateView() {
		model.fireTableDataChanged();
	}
	
	/**
	 * Check all edges.
	 */
	public void checkAll() {
		for(int i=0; i<getRowCount(); i++) {
			model.setValueAt(Boolean.TRUE, i, 0);
		}
	}
	
	/**
	 * Uncheck all edges.
	 */
	public void uncheckAll() {
		for(int i=0; i<getRowCount(); i++) {
			model.setValueAt(Boolean.FALSE, i, 0);
		}
	}
	private class CustomTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -678300273079091701L;
		private Scenario scenario;
		
		public void setScenario(Scenario scenario) {
			this.scenario = scenario;
			fireTableDataChanged();
		}
		public int getColumnCount() {
			return 4;
		}
		public int getRowCount() {
			if(scenario != null && scenario.getDataSource() != null) {
				int count = 0;
				for(Edge edge : scenario.getDataSource().getEdgeLibrary()) {
					if((networkTab.getEdgeTypeFilter()==null||edge.getEdgeType()==networkTab.getEdgeTypeFilter())
							&& edge.getName().toLowerCase().contains(networkTab.getEdgeSearchText().toLowerCase())) {
						count++;
					}
				}
				return count;
			} else return 0;
		}
		public boolean isCellEditable(int row, int col) {
	    	if(col==0) return true;
	    	else return false;
	    }
		public Class<?> getColumnClass(int col) {
			if(col==0) return Boolean.class;
			else if(col==2||col==3) return Node.class;
			else return Edge.class;
	    }
		public Object getValueAt(int row, int col) {
			if(col==0) { 
				Edge edge = (Edge)getValueAt(row, 1);
				for(Edge e : scenario.getNetwork().getEdges()) {
					if(e.equals(edge)) return true;
				}
				return false;
			} 
			else if(col==2) return ((Edge)getValueAt(row, 1)).getOrigin();
			else if(col==3) return ((Edge)getValueAt(row, 1)).getDestination();
			else {
				int count = 0;
				for(Edge edge : scenario.getDataSource().getEdgeLibrary()) {
					if((networkTab.getEdgeTypeFilter()==null||edge.getEdgeType()==networkTab.getEdgeTypeFilter())
							&& edge.getName().toLowerCase().contains(networkTab.getEdgeSearchText().toLowerCase())) {
						if(count==row) return edge;
						count++;
					}
				}
				return null;
			}
		}
	    public void setValueAt(Object value, int row, int col) {
	    	if(col==0) {
    			Edge edge = (Edge)getValueAt(row,1);
	    		if(value.equals(Boolean.TRUE)) {
					scenario.getNetwork().getEdges().add(edge);
					for(Node node : scenario.getDataSource().getNodeLibrary()) {
						if(!scenario.getNetwork().getNodes().contains(node)
								&& (edge.getOrigin().equals(node)
								|| edge.getDestination().equals(node))) {
							scenario.getNetwork().getNodes().add(node);
						}
					}
	    		} else {
	    			List<Object> uses = networkTab.getScenario().getEdgeUses(edge);
	    			if(uses.size() > 0) {
	    				JPanel warningPanel = new JPanel();
	    				warningPanel.setLayout(new GridBagLayout());
	    				GridBagConstraints c = new GridBagConstraints();
	    				c.insets = new Insets(2,2,2,2);
	    				c.gridx = 0;
	    				c.gridy = 0;
	    				c.weightx = 0;
	    				c.weighty = 0;
	    				c.anchor = GridBagConstraints.LINE_START;
	    				c.fill = GridBagConstraints.BOTH;
	    				warningPanel.add(new JLabel("The following use " + edge + " and must be changed before removal:"), c);
	    				c.gridy++;
	    				c.weighty = 1;
	    				DefaultListModel usagesList = new DefaultListModel();
	    				for(Object o : uses) {
	    					usagesList.addElement(o);
	    				}
	    				warningPanel.add(new JScrollPane(new JList(usagesList)), c);
	    				JOptionPane.showMessageDialog(networkTab.getScenarioPanel(), warningPanel, "SpaceNet Warning", JOptionPane.WARNING_MESSAGE);
	    			} else {
	    				scenario.getNetwork().remove(edge);
	    			}
	    		}
	    	}
	    	fireTableRowsUpdated(row, row);
	    	networkTab.getNetworkPanel().repaint();
	    }
	}
}
