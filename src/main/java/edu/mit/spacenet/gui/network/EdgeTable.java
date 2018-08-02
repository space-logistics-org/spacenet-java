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
