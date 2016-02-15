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

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.SimNetwork;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Chart for displaying the history of elements' contents broken down by class
 * of supply.
 * 
 * @author Paul Grogan
 */
public class ElementCosHistoryChart extends JSplitPane {
	private static final long serialVersionUID = -6405124067606236705L;
	private SimulationTab tab;
	private JComboBox elementCombo;
	private CheckBoxTableModel<ClassOfSupply> cosModel;
	private ChartPanel chartPanel;
	private JCheckBox linearizeDataCheck;
	
	/**
	 * Instantiates a new element cos history chart.
	 * 
	 * @param tab the simulation tab
	 */
	public ElementCosHistoryChart(SimulationTab tab) {
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
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		controlPanel.add(new JLabel("Element: "), c);
		c.gridy = 0;
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		elementCombo = new ContainerComboBox();
		elementCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					updateView();
				}
			}
		});
		controlPanel.add(elementCombo, c);
		c.gridx--;
		c.gridy++;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = 1;
		cosModel = new CheckBoxTableModel<ClassOfSupply>();
		for(int i=1; i<=10; i++) {
			cosModel.addObject(ClassOfSupply.getInstance(i));
		}
		cosModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				updateChart();
			}
		});
		JTable cosTable = new JTable(cosModel);
		cosTable.getTableHeader().setReorderingAllowed(false);
		cosTable.getColumnModel().getColumn(0).setHeaderValue("");
		cosTable.getColumnModel().getColumn(0).setMaxWidth(25);
		cosTable.getColumnModel().getColumn(1).setHeaderValue("Filter Classes of Supply");
		cosTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
		cosTable.setShowGrid(false);
		JScrollPane cosScroll = new JScrollPane(cosTable);
		cosScroll.setPreferredSize(new Dimension(150,200));
		controlPanel.add(cosScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllButton = new JButton("Select All");
		selectAllButton.setToolTipText("Select All COS");
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cosModel.selectAll();
			}
		});
		buttonPanel.add(selectAllButton);
		JButton deselectAllButton = new JButton("Deselect All");
		deselectAllButton.setToolTipText("Deselect All COS");
		deselectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cosModel.deselectAll();
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

		setName("Element History");
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
		elementCombo.removeAllItems();
		updateChart();
	}
	
	
	/**
	 * Update view.
	 */
	public void updateView() {
		tab.getScenarioPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		I_Element selectedElement = (I_Element)elementCombo.getSelectedItem();
		ItemListener elementListener = elementCombo.getItemListeners()[0];
		elementCombo.removeItemListener(elementListener);
		elementCombo.removeAllItems();
		for(I_Element element : getScenario().getElements()) {
			elementCombo.addItem(element);
		}
		if(selectedElement!=null) elementCombo.setSelectedItem(selectedElement);
		elementCombo.addItemListener(elementListener);
		updateChart();
		tab.getScenarioPanel().setCursor(Cursor.getDefaultCursor());
	}
	
	/**
	 * Update chart.
	 */
	private void updateChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, 
				"Date", 
				"Mass (kg)", 
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
		if(elementCombo.getSelectedItem()!=null) {
			for(ClassOfSupply cos : cosModel.getSelectedObjects()) {
				TimeSeries s = new TimeSeries("COS " + cos.getId(), Hour.class);
				double amount = 0;
				boolean firstPoint = true;
				for(SimNetwork network : tab.getSimulator().getNetworkHistory()) {
					I_Element element = network.getNetwork().getRegistrar().get(((I_Element)elementCombo.getSelectedItem()).getUid());
					if(element == null) 
						element = network.getNetwork().getRemovedRegistrar().get(((I_Element)elementCombo.getSelectedItem()).getUid());
					if(element != null) {
						Hour hour = new Hour(DateFunctions.getDate(getScenario().getStartDate(), network.getTime()));
						if(!firstPoint 
								&& !linearizeDataCheck.isSelected()) 
							s.addOrUpdate(hour.previous(), amount);
						amount = element.getTotalMass(cos);
						s.addOrUpdate(hour, amount);
						firstPoint = false;
					}
				}
				
				dataset.addSeries(s);
				r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1,-1,2,2));
				r.setSeriesPaint(dataset.getSeriesCount() - 1, cos.getColor());
			}
		}
		chartPanel.setChart(chart);
	}
}
