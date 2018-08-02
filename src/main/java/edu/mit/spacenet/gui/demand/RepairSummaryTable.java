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
package edu.mit.spacenet.gui.demand;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.RepairItem;
import edu.mit.spacenet.scenario.Scenario;

public class RepairSummaryTable extends JTable {
	private static final long serialVersionUID = -5981730193560436124L;
	
	private RepairabilityTab repairabilityTab;
	private RepairSummaryModel model;
	
	/**
	 * Instantiates a new repair summary table.
	 * 
	 * @param repairabilityTab the repairability tab
	 */
	public RepairSummaryTable(RepairabilityTab repairabilityTab) {
		this.repairabilityTab = repairabilityTab;
		
		model = new RepairSummaryModel();
		setModel(model);
		
		getColumnModel().getColumn(0).setHeaderValue(" ");
		getColumnModel().getColumn(0).setMaxWidth(20);
		getColumnModel().getColumn(1).setHeaderValue("Mission");
		getColumnModel().getColumn(2).setHeaderValue("Repair Time");
		getColumnModel().getColumn(2).setMaxWidth(75);
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format time and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " hr");
				return this;
			}
		});
		getColumnModel().getColumn(3).setHeaderValue("Mass");
		getColumnModel().getColumn(3).setMaxWidth(75);
		getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format mass and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " kg");
				return this;
			}
		});
		getTableHeader().setReorderingAllowed(false);
	}
	
	/**
	 * Initializes the table for a new scenario.
	 */
	public void initialize() {
		model.fireTableDataChanged();
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return repairabilityTab.getScenario();
	}
	
	/**
	 * Gets the selected mission.
	 * 
	 * @return the selected mission
	 */
	public Mission getSelectedMission() {
		if(getSelectedRow()>=0)
			return (Mission)getValueAt(getSelectedRow(), 1);
		else return null;
	}
	
	/**
	 * Updates the data within the currently selected row (assumes that only
	 * the current mission can be edited).
	 */
	public void updateView() {
		if(getSelectedRow() >= 0)
			model.fireTableRowsUpdated(getSelectedRow(), getSelectedRow());
	}
	
	/**
	 * The Class RepairSummaryModel.
	 */
	private class RepairSummaryModel extends AbstractTableModel {
		private static final long serialVersionUID = -1788294077902504936L;
		
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
			return getScenario()==null?0:getScenario().getMissionList().size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) return row;
			if(col==1) return getScenario().getMissionList().get(row);
			if(col==2) return getDeltaTime(row);
			else return getDeltaMass(row);
		}
		
		/**
		 * Gets the delta time.
		 * 
		 * @param row the row
		 * 
		 * @return the delta time
		 */
		private double getDeltaTime(int row) {
			double amount = 0;
			if(getScenario().getRepairedItems().get(getScenario().getMissionList().get(row))!=null) {
				for(RepairItem item : getScenario().getRepairedItems().get(getScenario().getMissionList().get(row))) {
					amount += item.getMeanRepairTime();
				}
			}
			return amount;
		}
		
		/**
		 * Gets the delta mass.
		 * 
		 * @param row the row
		 * 
		 * @return the delta mass
		 */
		private double getDeltaMass(int row) {
			double amount = 0;
			if(getScenario().getRepairedItems().get(getScenario().getMissionList().get(row))!=null) {
				for(RepairItem item : getScenario().getRepairedItems().get(getScenario().getMissionList().get(row))) {
					amount += item.getDemand().getMass() - item.getMassToRepair();
				}
			}
			return amount;
		}
	}
}
