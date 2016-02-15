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
import java.awt.Component;
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
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.SimDemand;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Component for displaying the element-level demands.
 * 
 * @author Paul Grogan
 */
public class ElementDemandsTab extends JSplitPane {
	private static final long serialVersionUID = -4728295622221432580L;

	private DemandsTab demandsTab;
	
	private OptionsPanel optionsPanel;
	private ChartPanel chartPanel;
	
	public ElementDemandsTab(DemandsTab demandsTab) {
		this.demandsTab = demandsTab;
		
		chartPanel = new ChartPanel();
		setLeftComponent(chartPanel);
		
		optionsPanel = new OptionsPanel();
		setRightComponent(optionsPanel);
		
		setName("Demands by Element");
		setOneTouchExpandable(true);
		setDividerSize(10);
		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(1);
		setDividerLocation(490);
	}
	
	/**
	 * Initializes the tab for a new scenario.
	 */
	public void initialize() {
		optionsPanel.initialize();
		chartPanel.initialize();
	}
	
	/**
	 * Updates the tab.
	 */
	public void updateView() {
		optionsPanel.updateView();
		chartPanel.updateView();
	}
	
	/**
	 * Checks if auto refresh is enabled.
	 * 
	 * @return true, if auto refresh is enabled
	 */
	private boolean isAutoRefresh() {
		return SpaceNetSettings.getInstance().isAutoRefresh();
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
	 * The Class OptionsPanel.
	 */
	private class OptionsPanel extends JPanel {
		private static final long serialVersionUID = -4714468679105858517L;
		
		private CheckBoxTableModel<I_Element> elementsModel;
		private CheckBoxTableModel<ClassOfSupply> cosModel;
		private JCheckBox displayTotalCheck, smoothDemandsCheck;
		private JButton refreshButton;
		
		/**
		 * Instantiates a new options panel.
		 * 
		 * @param networkTab the network tab
		 */
		private OptionsPanel() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createTitledBorder("Chart Options"));
			
			buildPanel();
		}
		
		/**
		 * Builds the panel.
		 */
		private void buildPanel() {
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(2,2,2,2);
			c.gridx = 0;
			c.gridy = 0;
			c.gridy++;
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.LINE_END;
			elementsModel = new CheckBoxTableModel<I_Element>();
			elementsModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if(e.getType()==TableModelEvent.UPDATE) {
						chartPanel.updateView();
					}
					displayTotalCheck.setEnabled(elementsModel.getSelectedObjects().size()==1);
					smoothDemandsCheck.setEnabled(elementsModel.getSelectedObjects().size()==1);
				}
			});
			JTable elementsTable = new JTable(elementsModel);
			elementsTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
			elementsTable.getTableHeader().setReorderingAllowed(false);
			elementsTable.getColumnModel().getColumn(0).setMaxWidth(25);
			elementsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Elements");
			elementsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 3205543512740776042L;
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
	                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                if(value instanceof I_Element) {
	                	setIcon(((I_Element)value).getIcon());
	                	setText(((I_Element)value).getName() + " [" + ((I_Element)value).getUid() + "]");
	                }
	                return this;
				}
			});
			elementsTable.setShowGrid(false);
			JScrollPane elementsScroll = new JScrollPane(elementsTable);
			elementsScroll.setPreferredSize(new Dimension(150,200));
			add(elementsScroll, c);
			c.gridy++;
			c.weighty = 0;
			c.fill = GridBagConstraints.NONE;
			JPanel elementsButtonPanel = new JPanel();
			elementsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			JButton selectAllElementsButton = new JButton("Select All");
			selectAllElementsButton.setToolTipText("Select All Elements");
			selectAllElementsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					elementsModel.selectAll();
				}
			});
			elementsButtonPanel.add(selectAllElementsButton);
			JButton deselectAllElementsButton = new JButton("Deselect All");
			deselectAllElementsButton.setToolTipText("Deselect All Elements");
			deselectAllElementsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					elementsModel.deselectAll();
				}
			});
			elementsButtonPanel.add(deselectAllElementsButton);
			add(elementsButtonPanel, c);
			c.weightx = 0;
			c.fill = GridBagConstraints.BOTH;
			c.gridy++;
			JPanel prop2 = new JPanel();
			prop2.setPreferredSize(new Dimension(1,15));
			add(prop2, c);
			c.gridy++;
			c.weighty = 1;
			cosModel = new CheckBoxTableModel<ClassOfSupply>();
			for(int i=1; i<=10; i++) {
				cosModel.addObject(ClassOfSupply.getInstance(i));
			}
			cosModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if(e.getType()==TableModelEvent.UPDATE) 
						chartPanel.updateView();
				}
			});
			JTable cosTable = new JTable(cosModel);
			cosTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
			cosTable.getTableHeader().setReorderingAllowed(false);
			cosTable.getColumnModel().getColumn(0).setMaxWidth(25);
			cosTable.getColumnModel().getColumn(1).setHeaderValue("Filter Classes of Supply");
			cosTable.setShowGrid(false);
			JScrollPane cosScroll = new JScrollPane(cosTable);
			cosScroll.setPreferredSize(new Dimension(150,150));
			add(cosScroll, c);
			c.gridy++;
			c.fill = GridBagConstraints.NONE;
			JPanel dataButtonPanel = new JPanel();
			dataButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			JButton selectAllDataButton = new JButton("Select All");
			selectAllDataButton.setToolTipText("Select All COS");
			selectAllDataButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cosModel.selectAll();
				}
			});
			dataButtonPanel.add(selectAllDataButton);
			JButton deselectAllDataButton = new JButton("Deselect All");
			deselectAllDataButton.setToolTipText("Deselect All COS");
			deselectAllDataButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cosModel.deselectAll();
				}
			});
			dataButtonPanel.add(deselectAllDataButton);
			add(dataButtonPanel, c);
			c.gridy++;
			c.anchor = GridBagConstraints.LINE_START;
			displayTotalCheck = new JCheckBox("Display Total", true);
			displayTotalCheck.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					chartPanel.updateView();
				}
			});
			add(displayTotalCheck, c);
			c.gridy++;
			smoothDemandsCheck = new JCheckBox("Smooth Demands", true);
			smoothDemandsCheck.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					chartPanel.updateView();
				}
			});
			add(smoothDemandsCheck, c);
			c.gridy++;
			c.anchor = GridBagConstraints.LINE_END;
			refreshButton = new JButton("Refresh", new ImageIcon(getClass().getClassLoader().getResource("icons/arrow_refresh.png")));
			refreshButton.setVisible(false);
			refreshButton.setToolTipText("Refresh Chart");
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.updateChart();
				}
			});
			add(refreshButton, c);
		}
		
		/**
		 * Initializes the panel.
		 */
		public void initialize() {
			elementsModel.clear();
			updateView();
		}
		
		/**
		 * Updates the view.
		 */
		public void updateView() {
			// update elements
			List<I_Element> selectedElements = elementsModel.getSelectedObjects();
			TableModelListener[] modelListeners = elementsModel.getTableModelListeners();
			for(TableModelListener l : modelListeners) {
				elementsModel.removeTableModelListener(l);
			}
			elementsModel.clear();
			if(demandsTab.getSimulator()!=null) {
				for(I_Element element : demandsTab.getScenario().getElements()) {
					elementsModel.addObject(element, selectedElements.contains(element));
				}
			}
			for(TableModelListener l : modelListeners) {
				elementsModel.addTableModelListener(l);
			}
			elementsModel.fireTableDataChanged();
			
			// update auto refresh button visibility
			if(isAutoRefresh()) {
				refreshButton.setVisible(false);
			} else {
				refreshButton.setVisible(true);
			}
		}
	}
	
	/**
	 * The Class ChartPanel.
	 */
	private class ChartPanel extends SNChartPanel {
		private static final long serialVersionUID = 4671269489085267359L;
		
		private ChartUpdater chartUpdater;
		
		/**
		 * Initializes the chart.
		 */
		public void initialize() {
			updateChart();
		}
		
		/**
		 * Updates the view.
		 */
		public void updateView() {
			if(isAutoRefresh()) updateChart();
		}
		
		/**
		 * Updates the chart.
		 */
		public void updateChart() {
			if(optionsPanel.elementsModel.getSelectedObjects().size()==1) {
				setChart(buildTimeSeriesChart());
			} else {
				while(chartUpdater != null && !chartUpdater.isDone()) {
					// wait for previous update to finish
				}
				chartUpdater = new ChartUpdater();
				chartUpdater.execute();
			}
		}
		
		private JFreeChart buildTimeSeriesChart() {
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			
			TimeSeries[] s = new TimeSeries[11];
			s[0] = new TimeSeries("Total", Hour.class);
			for(int i = 1; i <= 10; i++) {
				s[i] = new TimeSeries("COS " + i, Hour.class);
			}
			
			double amount[] = new double[11];
			for(SimDemand demand : demandsTab.getSimulator().getDemandHistory(optionsPanel.elementsModel.getSelectedObjects().get(0))) {
				Date date = DateFunctions.getDate(demandsTab.getScenario().getStartDate(), demand.getTime());
				if(!optionsPanel.smoothDemandsCheck.isSelected() && amount[0]>0) {
					for(int i = 0; i <=10; i++) {
						// replicate step function
						s[i].addOrUpdate(new Hour(date).previous(), amount[i]);
					}
				}
				for(Demand d : demand.getDemands()) {
					if(d.getMass()<0) continue;
					amount[0] += d.getMass();
					int cos = d.getResource().getClassOfSupply().getBaseClass().getId();
					amount[cos] += d.getMass();
				}
				for(int i = 0; i <=10; i++) {
					s[i].addOrUpdate(new Hour(date), amount[i]);
				}
			}
			
			JFreeChart chart = ChartFactory.createTimeSeriesChart(
					null, 				// title
					null, 				// x-axis label
					"Mass (kg)", 		// y-axis label
					dataset, 			// data
					true, 				// legend
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
			
			for(int i = 0; i <=10; i++) {
				if(i==0&&optionsPanel.displayTotalCheck.isSelected()) {
					dataset.addSeries(s[i]);
					r.setSeriesShape(dataset.getSeriesCount() - 1, 
							new Ellipse2D.Double(-1,-1,2,2));
					r.setSeriesPaint(dataset.getSeriesCount() - 1, 
							Color.BLACK);
				} else if(optionsPanel.cosModel.getSelectedObjects().contains(ClassOfSupply.getInstance(i))) {
					dataset.addSeries(s[i]);
					r.setSeriesShape(dataset.getSeriesCount() - 1, 
							new Ellipse2D.Double(-1,-1,2,2));
					r.setSeriesPaint(dataset.getSeriesCount() - 1, 
							ClassOfSupply.getInstance(i).getColor());
				}
			}
			return chart;
		}
		
		private JFreeChart buildBarChart() {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			JFreeChart chart = ChartFactory.createStackedBarChart(
					null, 						// title
					"", 						// x-axis label
					"Mass (kg)", 				// y-axis label
					dataset, 					// data
					PlotOrientation.VERTICAL, 	// plot orientation
					true, 						// legend
					true, 
					false);

			CategoryPlot plot = (CategoryPlot) chart.getPlot();
			plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.GRAY);
			plot.setRangeGridlinePaint(Color.GRAY);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			plot.setRangeGridlinesVisible(true);
			plot.setDomainGridlinesVisible(true);
			
			CategoryItemRenderer r = chart.getCategoryPlot().getRenderer();
			
			for(I_Element element : optionsPanel.elementsModel.getSelectedObjects()) {
				for(int i=1; i<=10; i++) {
					if(optionsPanel.cosModel.getSelectedObjects().contains(ClassOfSupply.getInstance(i))) {
						dataset.addValue(demandsTab.getSimulator().getDemands(element, 
								ClassOfSupply.getInstance(i)).getTotalMass(),
								"COS " + i, element.getName() + " [" + element.getUid() + "]");
						r.setSeriesPaint(dataset.getRowCount() - 1, ClassOfSupply.getInstance(i).getColor());
					}
				}
			}
			return chart;
		}
		
		private class ChartUpdater extends SwingWorker<Void, Void> {
			protected Void doInBackground() {
				try {
					SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Refreshing Chart...");
					demandsTab.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					setChart(buildBarChart());
					demandsTab.setCursor(Cursor.getDefaultCursor());
					SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}
		}
	}
}