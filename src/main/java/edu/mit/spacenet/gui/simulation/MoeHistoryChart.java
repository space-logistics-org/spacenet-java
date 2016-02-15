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
package edu.mit.spacenet.gui.simulation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.FullSimulator;
import edu.mit.spacenet.simulator.moe.MoeCrewSurfaceDays;
import edu.mit.spacenet.simulator.moe.MoeCrewTime;
import edu.mit.spacenet.simulator.moe.MoeExplorationCapability;
import edu.mit.spacenet.simulator.moe.MoeExplorationMassDelivered;
import edu.mit.spacenet.simulator.moe.MoeLaunchMass;
import edu.mit.spacenet.simulator.moe.MoeMassCapacityUtilization;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Chart for visualizing the evolution of the various measures of effectiveness
 * over the course of the simulation.
 * 
 * @author Paul Grogan
 */
public class MoeHistoryChart extends JSplitPane {
	private static final long serialVersionUID = -6405124067606236705L;
	private SimulationTab tab;
	private JComboBox moeCombo;
	private CheckBoxTableModel<Location> locationsModel;
	private JLabel totalValueLabel, valueLabel;
	private ChartPanel chartPanel;
	private JCheckBox linearizeDataCheck;
	
	/**
	 * Instantiates a new MOE history chart.
	 * 
	 * @param tab the simulation tab
	 */
	public MoeHistoryChart(SimulationTab tab) {
		this.tab = tab;
		
		chartPanel = new SNChartPanel();
		chartPanel.setPreferredSize(new Dimension(600,400));
		setLeftComponent(chartPanel);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		controlPanel.setBorder(BorderFactory.createTitledBorder("Chart Options"));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		moeCombo = new JComboBox();
		for(MoeType t : MoeType.values()) {
			moeCombo.addItem(t);
		}
		moeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					updateView();
				}
			}
		});
		controlPanel.add(moeCombo, c);
		c.gridx--;
		c.gridy++;
		c.gridwidth = 1;
		c.weighty = 0;
		c.weightx = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		JLabel totalMetricLabel = new JLabel("Total: ");
		controlPanel.add(totalMetricLabel, c);
		c.gridy++;
		JLabel metricLabel = new JLabel("Filtered: ");
		controlPanel.add(metricLabel, c);
		c.gridx++;
		c.gridy--;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		totalValueLabel = new JLabel("");
		controlPanel.add(totalValueLabel, c);
		c.gridy++;
		valueLabel = new JLabel("");
		controlPanel.add(valueLabel, c);
		c.gridx--;
		c.gridy++;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 1;
		locationsModel = new CheckBoxTableModel<Location>();
		locationsModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				updateChart();
			}
		});
		JTable locationsTable = new JTable(locationsModel);
		locationsTable.getTableHeader().setReorderingAllowed(false);
		locationsTable.getColumnModel().getColumn(0).setHeaderValue("");
		locationsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		locationsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Locations");
		locationsTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
		locationsTable.setShowGrid(false);
		JScrollPane locationsScroll = new JScrollPane(locationsTable);
		locationsScroll.setPreferredSize(new Dimension(150,200));
		controlPanel.add(locationsScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllButton = new JButton("Select All");
		selectAllButton.setToolTipText("Select All Locations");
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationsModel.selectAll();
			}
		});
		buttonPanel.add(selectAllButton);
		JButton deselectAllButton = new JButton("Deselect All");
		deselectAllButton.setToolTipText("Deselect All Locations");
		deselectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationsModel.deselectAll();
			}
		});
		buttonPanel.add(deselectAllButton);
		controlPanel.add(buttonPanel, c);
		c.gridy++;
		JPanel prop = new JPanel();
		prop.setPreferredSize(new Dimension(1,15));
		controlPanel.add(prop, c);

		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		linearizeDataCheck = new JCheckBox("Linearize Data", true);
		linearizeDataCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateChart();
			}
		});
		controlPanel.add(linearizeDataCheck, c);
		
		controlPanel.setMinimumSize(new Dimension(150,50));
		setRightComponent(controlPanel);

		setName("Measures History");
		setOneTouchExpandable(true);
		setDividerSize(10);
		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(1);
		setDividerLocation(700);
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return tab.getScenario();
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		locationsModel.clear();
		totalValueLabel.setText("");
		valueLabel.setText("");
		updateChart();
	}
	
	/**
	 * Update view.
	 */
	public void updateView() {
		tab.getScenarioPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		List<Location> deselectedLocations = locationsModel.getDeselectedObjects();
		locationsModel.clear();
		DecimalFormat format;
		switch((MoeType)moeCombo.getSelectedItem()) {
		case CREW_SURFACE_DAYS:
			for(MoeCrewSurfaceDays moe : tab.getSimulator().getCrewSurfaceDaysHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.0");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalCrewSurfaceDays()));
			break;
		case CREW_TIME:
			for(MoeCrewTime moe : tab.getSimulator().getCrewTimeHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.0");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalCrewTime()) + " hours");
			break;
		case LAUNCH_MASS:
			for(MoeLaunchMass moe : tab.getSimulator().getLaunchMassHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.0");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalLaunchMass()/1000d) + " mT");
			break;
		case EXPLORATION_MASS_DELIVERED:
			for(MoeExplorationMassDelivered moe : tab.getSimulator().getExplorationMassDeliveredHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalExplorationMassDelivered()) + " kg");
			break;
		case UP_MASS_CAPACITY_UTILIZATION:
			for(MoeMassCapacityUtilization moe : tab.getSimulator().getUpMassCapacityUtilizationHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.000");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalUpMassCapacityUtilization()));
			break;
		case RETURN_MASS_CAPACITY_UTILIZATION:
			for(MoeMassCapacityUtilization moe : tab.getSimulator().getDownMassCapacityUtilizationHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.000");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalDownMassCapacityUtilization()));
			break;
		case EXPLORATION_CAPABILITY:
			for(MoeExplorationCapability moe : tab.getSimulator().getExplorationCapabilityHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalExplorationCapability()));
			break;
		case RELATIVE_EXPLORATION_CAPABILITY:
			for(MoeExplorationCapability moe : tab.getSimulator().getExplorationCapabilityHistory()) {
				locationsModel.addObject(moe.getLocation(), !deselectedLocations.contains(moe.getLocation()));
			}
			format = new DecimalFormat("0.00");
			totalValueLabel.setText(format.format(tab.getSimulator().getTotalRelativeExplorationCapability()));
			break;
		}
		updateChart();
		tab.getScenarioPanel().setCursor(Cursor.getDefaultCursor());
	}
	private void updateChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, 
				"Date", 
				"Value", 
				dataset, 
				true, 
				true, 
				false);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setRangeGridlinePaint(Color.GRAY);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		XYItemRenderer r = chart.getXYPlot().getRenderer();

		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}

		DecimalFormat format;
		if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.CREW_SURFACE_DAYS)) {
			TimeSeries s = new TimeSeries("Total", Hour.class);
			plot.getRangeAxis().setLabel("Crew Surface Days");
			double amount = 0;
			s.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeCrewSurfaceDays moe : tab.getSimulator().getCrewSurfaceDaysHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && amount>0) s.addOrUpdate(new Hour(date).previous(), amount);
					amount += moe.getAmount();
					s.addOrUpdate(new Hour(date), amount);
				}
			}
			s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.0");
			valueLabel.setText(format.format(amount));
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.CREW_TIME)) {
			TimeSeries s1 = new TimeSeries(MoeCrewTime.UNAVAILABLE, Hour.class);
			TimeSeries s2 = new TimeSeries(MoeCrewTime.EXPLORATION, Hour.class);
			TimeSeries s3 = new TimeSeries(MoeCrewTime.CORRECTIVE_MAINTENANCE, Hour.class);
			TimeSeries s4 = new TimeSeries(MoeCrewTime.PREVENTATIVE_MAINTENANCE, Hour.class);
			plot.getRangeAxis().setLabel("Hours");
			double amount1=0,amount2=0,amount3=0,amount4=0;
			s1.addOrUpdate(new Hour(getScenario().getStartDate()), amount1);
			s2.addOrUpdate(new Hour(getScenario().getStartDate()), amount2);
			s3.addOrUpdate(new Hour(getScenario().getStartDate()), amount3);
			s4.addOrUpdate(new Hour(getScenario().getStartDate()), amount4);
			for(MoeCrewTime moe : tab.getSimulator().getCrewTimeHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && amount1>0) s1.addOrUpdate(new Hour(date).previous(), amount1);
					if(!linearizeDataCheck.isSelected() && amount2>0) s2.addOrUpdate(new Hour(date).previous(), amount2);
					if(!linearizeDataCheck.isSelected() && amount3>0) s3.addOrUpdate(new Hour(date).previous(), amount3);
					if(!linearizeDataCheck.isSelected() && amount4>0) s4.addOrUpdate(new Hour(date).previous(), amount4);
					if(moe.getType().equals(MoeCrewTime.UNAVAILABLE)) {
						amount1 += moe.getAmount();
					} else if(moe.getType().equals(MoeCrewTime.EXPLORATION)) {
						amount2 += moe.getAmount();
					} else if(moe.getType().equals(MoeCrewTime.CORRECTIVE_MAINTENANCE)) {
						amount3 += moe.getAmount();
					} else if(moe.getType().equals(MoeCrewTime.PREVENTATIVE_MAINTENANCE)) {
						amount4 += moe.getAmount();
					}
					s1.addOrUpdate(new Hour(date), amount1);
					s2.addOrUpdate(new Hour(date), amount2);
					s3.addOrUpdate(new Hour(date), amount3);
					s4.addOrUpdate(new Hour(date), amount4);
				}
			}
			s1.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount1);
			s2.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount2);
			s3.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount3);
			s4.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount4);
			dataset.addSeries(s1);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			dataset.addSeries(s2);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			dataset.addSeries(s3);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			dataset.addSeries(s4);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.0");
			valueLabel.setText(format.format(amount1+amount2+amount3+amount4));
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.LAUNCH_MASS)) {
			TimeSeries s = new TimeSeries("Total", Hour.class);
			plot.getRangeAxis().setLabel("Launch Mass (mT)");
			double amount = 0;
			s.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeLaunchMass moe : tab.getSimulator().getLaunchMassHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && amount>0) s.addOrUpdate(new Hour(date).previous(), amount);
					amount += moe.getAmount()/1000d;
					s.addOrUpdate(new Hour(date), amount);
				}
			}
			s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.0");
			valueLabel.setText(format.format(amount) + " mT");
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.EXPLORATION_MASS_DELIVERED)) {
			TimeSeries s = new TimeSeries("Total", Hour.class);
			plot.getRangeAxis().setLabel("Exploration Mass Delivered (kg)");
			double amount = 0;
			s.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeExplorationMassDelivered moe : tab.getSimulator().getExplorationMassDeliveredHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && amount>0) s.addOrUpdate(new Hour(date).previous(), amount);
					amount += moe.getAmount();
					s.addOrUpdate(new Hour(date), amount);
				}
			}
			s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0");
			valueLabel.setText(format.format(amount) + " kg");
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.UP_MASS_CAPACITY_UTILIZATION)) {
			TimeSeries s1 = new TimeSeries("Capacity", Hour.class);
			TimeSeries s2 = new TimeSeries("Utilization", Hour.class);
			plot.getRangeAxis().setLabel("Up-Mass Utilization (kg)");
			double capacity = 0;
			double amount = 0;
			s1.addOrUpdate(new Hour(getScenario().getStartDate()), capacity);
			s2.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeMassCapacityUtilization moe : tab.getSimulator().getUpMassCapacityUtilizationHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && capacity>0) s1.addOrUpdate(new Hour(date).previous(), capacity);
					if(!linearizeDataCheck.isSelected() && amount>0) s2.addOrUpdate(new Hour(date).previous(), amount);
					capacity += moe.getCapacity();
					amount += moe.getAmount();
					s1.addOrUpdate(new Hour(date), capacity);
					s2.addOrUpdate(new Hour(date), amount);
				}
			}
			s1.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), capacity);
			s2.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s1);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			dataset.addSeries(s2);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.000");
			valueLabel.setText(capacity==0?"n/a":format.format(amount/capacity));
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.RETURN_MASS_CAPACITY_UTILIZATION)) {
			TimeSeries s1 = new TimeSeries("Capacity", Hour.class);
			TimeSeries s2 = new TimeSeries("Utilization", Hour.class);
			plot.getRangeAxis().setLabel("Return-Mass Utilization (kg)");
			double capacity = 0;
			double amount = 0;
			s1.addOrUpdate(new Hour(getScenario().getStartDate()), capacity);
			s2.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeMassCapacityUtilization moe : tab.getSimulator().getDownMassCapacityUtilizationHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && capacity>0) s1.addOrUpdate(new Hour(date).previous(), capacity);
					if(!linearizeDataCheck.isSelected() && amount>0) s2.addOrUpdate(new Hour(date).previous(), amount);
					capacity += moe.getCapacity();
					amount += moe.getAmount();
					s1.addOrUpdate(new Hour(date), capacity);
					s2.addOrUpdate(new Hour(date), amount);
				}
			}
			s1.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), capacity);
			s2.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s1);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			dataset.addSeries(s2);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.000");
			valueLabel.setText(capacity==0?"n/a":format.format(amount/capacity));
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.EXPLORATION_CAPABILITY)) {
			TimeSeries s = new TimeSeries("Total", Hour.class);
			plot.getRangeAxis().setLabel("Exploration Capability");
			double amount = 0;
			s.addOrUpdate(new Hour(getScenario().getStartDate()), amount);
			for(MoeExplorationCapability moe : tab.getSimulator().getExplorationCapabilityHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					if(!linearizeDataCheck.isSelected() && amount>0) s.addOrUpdate(new Hour(date).previous(), amount);
					amount += moe.getMass()*moe.getDuration();
					s.addOrUpdate(new Hour(date), amount);
				}
			}
			s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), amount);
			dataset.addSeries(s);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0");
			valueLabel.setText(format.format(amount));
		} else if(((MoeType)moeCombo.getSelectedItem()).equals(MoeType.RELATIVE_EXPLORATION_CAPABILITY)) {
			TimeSeries s = new TimeSeries("Total", Hour.class);
			plot.getRangeAxis().setLabel("Relative Exploration Capability");
			double amount = 0;
			double value = 0;
			s.addOrUpdate(new Hour(getScenario().getStartDate()), value);
			for(MoeExplorationCapability moe : tab.getSimulator().getExplorationCapabilityHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), moe.getTime());
				
				if(locationsModel.getSelectedObjects().contains(moe.getLocation())) {
					amount += moe.getMass()*moe.getDuration();
					
					double mass = 0;
					for(MoeLaunchMass moeMass : tab.getSimulator().getLaunchMassHistory()) {
						if(moeMass.getTime()<=moe.getTime())
							mass += moeMass.getAmount();
						else break;
					}
					if(!linearizeDataCheck.isSelected() && value>0) s.addOrUpdate(new Hour(date).previous(), value);
					value = (amount/FullSimulator.APOLLO_17_EXPLORATION_CAPABILITY)/(mass/FullSimulator.APOLLO_17_LAUNCH_MASS);
					s.addOrUpdate(new Hour(date), value);
				}
			}
			s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), 
					tab.getSimulator().getTime())), value);
			dataset.addSeries(s);
			r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
			format = new DecimalFormat("0.00");
			valueLabel.setText(format.format(value));
		}
		
		chartPanel.setChart(chart);
	}
	
	/**
	 * The Enum MoeType.
	 */
	enum MoeType {
		
		/** The crew surface days. */
		CREW_SURFACE_DAYS("Crew Surface Days"),
		
		/** The crew time. */
		CREW_TIME("Crew Time"),
		
		/** The launch mass. */
		LAUNCH_MASS("Launch Mass"),
		
		/** The exploration mass delivered. */
		EXPLORATION_MASS_DELIVERED("Exploration Mass Delivered"),
		
		/** The up mass capacity utilization. */
		UP_MASS_CAPACITY_UTILIZATION("Up-Mass Capacity Utilization"),
		
		/** The return mass capacity utilization. */
		RETURN_MASS_CAPACITY_UTILIZATION("Return-Mass Capacity Utilization"),
		
		/** The exploration capability. */
		EXPLORATION_CAPABILITY("Exploration Capability"),
		
		/** The relative exploration capability. */
		RELATIVE_EXPLORATION_CAPABILITY("Relative Exploration Capability");
		
		private String name;
		private MoeType(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return name;
		}
	}
}
