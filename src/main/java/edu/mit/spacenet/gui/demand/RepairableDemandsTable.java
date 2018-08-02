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
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.scenario.RepairItem;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.DemandSimulator;

/**
 * The Class RepairableDemandsTable.
 */
public class RepairableDemandsTable extends JTable {
	private static final long serialVersionUID = -8427772886041511648L;
	
	private RepairabilityTab repairabilityTab;
	
	private RepairableDemandsModel model;
	
	/**
	 * Instantiates a new repairable demands table.
	 */
	public RepairableDemandsTable(RepairabilityTab repairabilityTab) {
		this.repairabilityTab = repairabilityTab;
		
		model = new RepairableDemandsModel();
		setModel(model);
		
		getColumnModel().getColumn(0).setHeaderValue("Element");
		getColumnModel().getColumn(1).setHeaderValue("Item");
		getColumnModel().getColumn(2).setHeaderValue("Amount");
		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the amount and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " " + ((RepairableDemandsModel)getModel()).getDemand(row).getResource().getUnits());
				return this;
			}
		});
		getColumnModel().getColumn(3).setHeaderValue("Mass");
		getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the mass and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " kg");
				return this;
			}
		});
		getColumnModel().getColumn(4).setHeaderValue("Mass to Repair");
		getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the mass and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " kg");
				return this;
			}
		});
		getColumnModel().getColumn(5).setHeaderValue("Time to Repair");
		getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the time and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " hr");
				return this;
			}
		});
		getColumnModel().getColumn(6).setHeaderValue("Amount Repaired");
		getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private DecimalFormat format = new DecimalFormat("0.00");
			// custom renderer to format the amount and add units
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(JLabel.RIGHT);
				setText(format.format(value) + " " + ((RepairableDemandsModel)getModel()).getDemand(row).getResource().getUnits());
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
	 * Update view.
	 */
	public void updateView() {
		model.fireTableDataChanged();
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return  repairabilityTab.getScenario();
	}
	
	/**
	 * Gets the simulator.
	 * 
	 * @return the simulator
	 */
	private DemandSimulator getSimulator() {
		return repairabilityTab.getDemandsTab().getSimulator();
	}
	
	/**
	 * The Class RepairableDemandsModel.
	 */
	private class RepairableDemandsModel extends AbstractTableModel {
		private static final long serialVersionUID = -2630916184656214544L;
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 7;
		}
		
		/**
		 * Gets the mission index.
		 * 
		 * @return the mission index
		 */
		private int getMissionIndex() {
			return repairabilityTab.getRepairSummaryTable().getSelectedRow();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int column) {
			if(column!=0 && column!=1) return Double.class;
			else return Object.class;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			if(getScenario()==null||getSimulator()==null) return 0;
			else if(getSimulator().getUnsortedRepairItems().get(getMissionIndex())==null) return 0;
			else return getSimulator().getUnsortedRepairItems().get(getMissionIndex()).size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int column) {
			if(column==0) return getElement(row);
			else if(column==1) return getDemand(row).getResource();
			else if(column==2) return getDemand(row).getAmount();
			else if(column==3) return getDemand(row).getMass();
			else if(column==4) return getRepairItem(row).getMassToRepair();
			else if(column==5) return getRepairItem(row).getMeanRepairTime();
			else if(column==6) {
				HashSet<RepairItem> repairedItems = new HashSet<RepairItem>();
				if(getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex()))!=null) {
					for(RepairItem item : getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex()))) {
						repairedItems.add(new RepairItem(new Demand(item.getDemand().getResource(), item.getDemand().getAmount()), item.getElement()));
					}
				}
				for(RepairItem simItem : getSimulator().getUnsortedRepairItems().get(getMissionIndex())) {
					double amountRepaired = 0;
					for(RepairItem item : repairedItems) {
						if(simItem.getDemand().getResource().equals(item.getDemand().getResource())
								&& simItem.getElement().equals(item.getElement())) {
							double amountNewlyRepaired = Math.min(simItem.getDemand().getAmount() - amountRepaired, item.getDemand().getAmount());
							item.getDemand().setAmount(item.getDemand().getAmount()-amountNewlyRepaired);
							amountRepaired = amountRepaired + amountNewlyRepaired;
						}
					}
					if(getRepairItem(row).equals(simItem)) return amountRepaired;
				}
				return 0;
			}
			else return null;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt(Object value, int row, int column) {
			if(column==6) {
				double oldValue = (Double)getValueAt(row, column);
				double newValue = Math.min(getDemand(row).getAmount(), (Double)value);
				
				if(newValue > oldValue) {
					if(getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex()))==null) {
						getScenario().getRepairedItems().put(getScenario().getMissionList().get(getMissionIndex()), new HashSet<RepairItem>());
					}
					boolean isFound = false;
					for(RepairItem item : getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex()))) {
						if(item.getDemand().getResource().equals(getDemand(row).getResource())
								&& item.getElement().equals(getElement(row))) {
							isFound = true;
							item.getDemand().setAmount(item.getDemand().getAmount() + (newValue - oldValue));
						}
					}
					if(!isFound) {
						getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex())).add(
								new RepairItem(new Demand(getDemand(row).getResource(), newValue - oldValue), 
										getElement(row)));
					}
				} else if(newValue < oldValue) {
					for(RepairItem item : getScenario().getRepairedItems().get(getScenario().getMissionList().get(getMissionIndex()))) {
						if(item.getDemand().getResource().equals(getDemand(row).getResource())
								&& item.getElement().equals(getElement(row))) {
							item.getDemand().setAmount(item.getDemand().getAmount() + (newValue - oldValue));
						}
					}
				}
				fireTableRowsUpdated(row, row);
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int column) {
			if(column==6) return true;
			else return false;
		}
		
		/**
		 * Gets the repair item.
		 * 
		 * @param row the row
		 * 
		 * @return the repair item
		 */
		private RepairItem getRepairItem(int row) {
			return getSimulator().getUnsortedRepairItems().get(getMissionIndex()).get(row);
		}
		
		/**
		 * Gets the element.
		 * 
		 * @param row the row
		 * 
		 * @return the element
		 */
		private I_Element getElement(int row) {
			return getRepairItem(row).getElement();
		}
		
		/**
		 * Gets the demand.
		 * 
		 * @param row the row
		 * 
		 * @return the demand
		 */
		private Demand getDemand(int row) {
			return getRepairItem(row).getDemand();
		}
	}
}