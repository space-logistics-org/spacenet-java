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
package edu.mit.spacenet.gui.demand;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.scenario.ItemDiscretization;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.RepairItem;
import edu.mit.spacenet.scenario.Scenario;

/**
 * Component for performing repairability analysis.
 * 
 * @author Paul Grogan
 */
public class RepairabilityTab extends JSplitPane {
	private static final long serialVersionUID = -6405124067606236705L;
	private DemandsTab demandsTab;
	
	private ListSelectionListener summaryListener;
	private RepairSummaryTable summaryTable;
	private ChartPanel chartPanel;

	private SpinnerNumberModel autoRepairModel;
	private JSpinner autoRepairSpinner;
	private JButton autoRepairButton;
	private RepairableDemandsTable repairTable;
	
	/**
	 * Instantiates a new repairability tab.
	 * 
	 * @param tab the demands tab
	 */
	public RepairabilityTab(DemandsTab tab) {
		super(JSplitPane.VERTICAL_SPLIT);
		this.demandsTab = tab;
		
		JSplitPane topPanel = new JSplitPane();
		summaryTable = new RepairSummaryTable(this);
		summaryListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting() && e.getFirstIndex()>=0) {
					autoRepairModel.setValue(summaryTable.getValueAt(summaryTable.getSelectedRow(), 2));
					updateView();
				}
				autoRepairButton.setEnabled(summaryTable.getSelectedRow() >=0 );
			}
		};
		summaryTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		summaryTable.getSelectionModel().addListSelectionListener(summaryListener);
		JScrollPane summaryScroll = new JScrollPane(summaryTable);
		topPanel.setLeftComponent(summaryScroll);
		
		chartPanel = new ChartPanel();
		topPanel.setRightComponent(chartPanel);
		topPanel.setBorder(BorderFactory.createEmptyBorder());
		topPanel.setDividerLocation(300);
		topPanel.setResizeWeight(0.5);
		setTopComponent(topPanel);
		
		JPanel repairPanel = new JPanel();
		repairPanel.setLayout(new GridBagLayout());
		repairPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx=0;
		c.gridy=0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		repairPanel.add(new JLabel("Auto-Repair: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		autoRepairModel = new SpinnerNumberModel(0,0,10000,0.25);
		autoRepairSpinner = new JSpinner(autoRepairModel);
		repairPanel.add(new UnitsWrapper(autoRepairSpinner, "hours"), c);
		c.gridx++;
		c.weightx = 1;
		autoRepairButton = new JButton ("Auto-Repair", new ImageIcon(getClass().getClassLoader().getResource("icons/lightning.png")));
		autoRepairButton.setEnabled(false);
		autoRepairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autoRepair();
				updateView();
			}
		});
		repairPanel.add(autoRepairButton, c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 3;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		repairTable = new RepairableDemandsTable(this);
		repairTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				summaryTable.updateView();
				chartPanel.updateView();
			}
		});
		JScrollPane repairScroll = new JScrollPane(repairTable);
		repairPanel.add(repairScroll, c);
		setBottomComponent(repairPanel);
		
		setBorder(BorderFactory.createEmptyBorder());
		setDividerLocation(300);
		setResizeWeight(.25);
		setName("Repairability Analysis");
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		summaryTable.initialize();
		repairTable.initialize();
		chartPanel.initialize();
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return demandsTab.getScenario();
	}
	
	/**
	 * Gets the repair summary table.
	 * 
	 * @return the repair summary table
	 */
	public RepairSummaryTable getRepairSummaryTable() {
		return summaryTable;
	}
	
	/**
	 * Update view.
	 */
	public void updateView() {
		summaryTable.updateView();
		repairTable.updateView();
		chartPanel.updateView();
	}
	
	/**
	 * Automatically selects the best set of items to repair for a single 
	 * mission.
	 */
	private void autoRepair() {
		Mission mission = summaryTable.getSelectedMission();
		int index = getScenario().getMissionList().indexOf(mission);
		if(getScenario().getRepairedItems().get(mission)==null) {
			getScenario().getRepairedItems().put(mission, new HashSet<RepairItem>());
		} else {
			getScenario().getRepairedItems().get(mission).clear();
		}
		
		double repairTime = autoRepairModel.getNumber().doubleValue();
		ArrayList<RepairItem> repairItems = new ArrayList<RepairItem>();
		if(demandsTab.getSimulator().getSortedRepairItems().get(index)!=null) {
			for(RepairItem i : demandsTab.getSimulator().getSortedRepairItems().get(index)) {
				repairItems.add(i);
			}
		}
		for(RepairItem item : repairItems) {
			if(repairTime >= item.getMeanRepairTime()) {
				// can repair entire item
				repairTime -= item.getMeanRepairTime();
				logRepairItem(index, 
						item.getDemand().getResource(), 
						item.getDemand().getAmount(), 
						item.getElement());
			} else if(getScenario().getItemDiscretization().equals(ItemDiscretization.NONE)
					&& repairTime > 0) {
				// else if there is no discretization of items, can repair
				// a fraction of the item
				double amount = item.getDemand().getAmount()*repairTime/item.getMeanRepairTime();
				repairTime = 0;
				logRepairItem(index, 
						item.getDemand().getResource(), 
						amount, 
						item.getElement());
			} else if(repairTime >= item.getUnitMeanRepairTime()) {
				// otherwise can repair a whole portion of the demand
				int quantity = (int)Math.floor(Math.min(item.getDemand().getAmount(), repairTime/item.getUnitMeanRepairTime()));
				repairTime -= quantity*item.getUnitMeanRepairTime();
				logRepairItem(index, 
						item.getDemand().getResource(), 
						quantity, 
						item.getElement());
			}
		}
	}
	
	/**
	 * Log repair item.
	 * 
	 * @param missionIndex the mission index
	 * @param resource the resource
	 * @param amountRepaired the amount repaired
	 * @param element the element
	 */
	private void logRepairItem(int missionIndex, I_Resource resource, 
			double amountRepaired, I_Element element) {
		Mission mission = getScenario().getMissionList().get(missionIndex);
		for(RepairItem item : getScenario().getRepairedItems().get(mission)) {
			if(item.getDemand().getResource().equals(resource)
					&& item.getElement().equals(element)) {
				// if equivalent repair item already exists, add the
				// corresponding new amount repaired
				item.getDemand().setAmount(item.getDemand().getAmount() + amountRepaired);
				return;
			}
		}
		// otherwise create new repair item and log amount repaired
		getScenario().getRepairedItems().get(mission).add(new RepairItem(
				new Demand(resource, amountRepaired), element));
	}
	
	/**
	 * Gets the demands tab.
	 * 
	 * @return the demands tab
	 */
	public DemandsTab getDemandsTab() {
		return demandsTab;
	}
	
	/**
	 * The Class ChartPanel.
	 */
	private class ChartPanel extends SNChartPanel {
		private static final long serialVersionUID = -3883762777518801995L;

		/**
		 * Instantiates a new chart panel.
		 */
		public ChartPanel() {
			setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			setPreferredSize(new Dimension(600,400));
		}
		
		/**
		 * Initialize.
		 */
		public void initialize() {
			updateView();
		}
		
		/**
		 * Update view.
		 */
		public void updateView() {
			if(summaryTable.getSelectedRowCount()==0) {
				setChart(null);
			} else {
				Mission mission = summaryTable.getSelectedMission();
				int index = getScenario().getMissionList().indexOf(mission);
	
				XYSeriesCollection dataset = new XYSeriesCollection();
				JFreeChart chart = ChartFactory.createXYLineChart(
						null, 						// title
						"Repair Time (hr)", 		// x-axis label
						"Spares Mass Savings (kg)", // y-axis label	
						dataset, 					// data
						PlotOrientation.VERTICAL,	// orientation
						true, 						// legend
						true, 
						false);
				
				XYPlot plot = (XYPlot) chart.getPlot();
				plot.setBackgroundPaint(Color.WHITE);
				plot.setDomainGridlinePaint(Color.GRAY);
				plot.setRangeGridlinePaint(Color.GRAY);
				plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
				plot.setRangeGridlinesVisible(true);
				plot.setDomainGridlinesVisible(true);
	
				XYItemRenderer r = chart.getXYPlot().getRenderer();
	
				if (r instanceof XYLineAndShapeRenderer) {
					XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
					renderer.setBaseShapesVisible(true);
					renderer.setBaseShapesFilled(true);
				}
	
				XYSeries s = new XYSeries("Unsorted");
				double mass = 0;
				double time = 0;
				s.addOrUpdate(mass, time);
				if(demandsTab.getSimulator().getUnsortedRepairItems().get(index)!=null) {
					for(RepairItem i : demandsTab.getSimulator().getUnsortedRepairItems().get(index)) {
						mass += i.getDemand().getMass()-i.getMassToRepair();
						time += i.getMeanRepairTime();
						s.addOrUpdate(time, mass);
					}
				}
				dataset.addSeries(s);
				r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
				r.setSeriesShape(dataset.getSeriesCount() - 1, 
						new Ellipse2D.Double(0, 0, 0, 0));
	
				mass = 0;
				time = 0;
				XYSeries s2 = new XYSeries("Optimal Order");
				s2.addOrUpdate(0, 0);
				if(demandsTab.getSimulator().getSortedRepairItems().get(index)!=null) {
					for(RepairItem i :demandsTab.getSimulator().getSortedRepairItems().get(index)) {
						mass += i.getDemand().getMass()-i.getMassToRepair();
						time += i.getMeanRepairTime();
						s2.addOrUpdate(time, mass);
					}
				}
				dataset.addSeries(s2);
				r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLUE);
				r.setSeriesShape(dataset.getSeriesCount() - 1, 
						new Ellipse2D.Double(0, 0, 0, 0));
				
				XYSeries s3 = new XYSeries("Repair Selection");
				mass = 0;
				time = 0;
				if(getScenario().getRepairedItems().get(mission) != null) {
					for(RepairItem i : getScenario().getRepairedItems().get(mission)) {
						mass += (i.getDemand().getMass()-i.getMassToRepair());
						time += i.getMeanRepairTime();
					}
				}
				s3.addOrUpdate(time, mass);
				dataset.addSeries(s3);
				r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLACK);
				r.setSeriesShape(dataset.getSeriesCount() - 1, 
						new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
				
				setChart(chart);
			}
		}
	}
}