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
package edu.mit.spacenet.gui.mission;

import java.awt.Component;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.MiniSimulator;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * A table to display and edit missions.
 * 
 * @author Paul Grogan
 */
public class MissionsTable extends JTable {
	private static final long serialVersionUID = -5960985748957372743L;
	
	private CustomTableModel model;
	private MissionsPanel missionListPanel;
	
	/**
	 * Instantiates a new mission table.
	 * 
	 * @param missionListPanel the mission list panel
	 */
	public MissionsTable(MissionsPanel missionListPanel) {
		super();
		this.missionListPanel = missionListPanel;
				
		model = new CustomTableModel();
		setModel(model);
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		getColumnModel().getColumn(0).setHeaderValue("#");
		getColumnModel().getColumn(0).setCellRenderer(renderer);
		getColumnModel().getColumn(0).setMaxWidth(25);
		getColumnModel().getColumn(1).setHeaderValue("Start Date");
		getColumnModel().getColumn(1).setMaxWidth(100);
		getColumnModel().getColumn(2).setHeaderValue("Mission Name");
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			// a custom renderer to display simulation error icons and messages
			private static final long serialVersionUID = 7793722593255866883L;
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setIcon(null);
				setIconTextGap(0);
				setToolTipText(null);
				for(SimSpatialError error : getSimulator().getSpatialErrors()) {
					for(I_Event event : getMission(row).getEventList()) {
						if(error.getEvent().getRoot().equals(event)) {
							setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bullet_error.png")));
							setToolTipText("This mission has errors.");
							return this;
						}
					}
				}
				return this;
			}
		});
		
		getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Initializes the table for a new scenario.
	 */
	public void initialize() {
		model.setScenario(getScenario());
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return missionListPanel.getMissionsSplitPane().getMissionsTab().getScenarioPanel().getScenario();
	}
	
	/**
	 * Gets the simulator.
	 * 
	 * @return the simulator
	 */
	private MiniSimulator getSimulator() {
		return missionListPanel.getMissionsSplitPane().getMissionsTab().getSimulator();
	}
	
	/**
	 * A custom table model to manage the mission data in a campaign.
	 */
	class CustomTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -678300273079091701L;
		private List<Mission> data;
		
		/**
		 * Sets the scenario.
		 * 
		 * @param scenario the new scenario
		 */
		public void setScenario(Scenario scenario) {
			if(scenario==null) data = null;
			else data = scenario.getMissionList();
			fireTableDataChanged();
		}
		
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
			return data==null?0:data.size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
	    	if(col==0) return false;
	    	else return true;
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int col) {
			if(col==0) {
				return Integer.class;
			} else if(col==1) {
				return Date.class;
			} else {
				return String.class;
			}
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) {
				return new Integer(row+1);
			} else if(col==1) {
				return data.get(row).getStartDate();
			} else {
				return data.get(row).getName();
			}
		}
	    
    	/* (non-Javadoc)
    	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
    	 */
    	public void setValueAt(Object value, int row, int col) {
	    	if(col==1) {
	    		data.get(row).setStartDate((Date)value);
	    	} else if(col==2) {
	    		data.get(row).setName((String)value);
	    	}
	    	sortMissions();
	    	missionListPanel.getMissionsSplitPane().getMissionsTab().updateView();
	    }
    	
    	/**
	     * Gets the mission in a particular row.
	     * 
	     * @param row the row
	     * 
	     * @return the mission
	     */
	    public Mission getMission(int row) {
    		return data.get(row);
    	}
	    
	    /**
    	 * Sorts the missions.
    	 */
    	public void sortMissions() {
	    	Collections.sort(data);
			fireTableDataChanged();
	    }
	}
	
	/**
	 * Updates the view.
	 */
	public void updateView() {
		int selectedRow = getSelectedRow();
		model.sortMissions();
		if(selectedRow<getRowCount());
			getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
	}
	
	/**
	 * Gets the mission in a particular row.
	 * 
	 * @param row the row
	 * 
	 * @return the mission
	 */
	public Mission getMission(int row) {
		return model.getMission(row);
	}
	
	/**
	 * Gets the selected mission.
	 * 
	 * @return the selected mission
	 */
	public Mission getSelectedMission() {
		if(getSelectedRow()<0) return null;
		else return getMission(getSelectedRow());
	}
}