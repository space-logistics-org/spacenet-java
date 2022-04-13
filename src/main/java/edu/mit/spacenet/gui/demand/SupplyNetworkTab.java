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

import java.awt.BasicStroke;
import java.awt.Color;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.toedter.calendar.JDateChooser;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.scenario.SupplyPoint;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Component for visualizing the supply network of the scenario.
 * 
 * @author Paul Grogan
 */
public class SupplyNetworkTab extends JSplitPane {
	private static final long serialVersionUID = -4728295622221432580L;
	private static String DEMANDS = "Transport Demands";
	private static String RAW_CAPACITY = "Raw Capacity";
	private static String REMAINING_CAPACITY = "Remaining Capacity";
	private static String NET_CAPACITY = "Net Capacity";
	private static int MIN_THICKNESS = 1;
	private static int MAX_THICKNESS = 8;
	private static int MIN_SIZE = 4;
	private static int MAX_SIZE = 20;

	private DemandsTab demandsTab;
	
	private OptionsPanel optionsPanel;
	private ChartPanel chartPanel;
	
	public SupplyNetworkTab(DemandsTab demandsTab) {
		this.demandsTab = demandsTab;
		
		chartPanel = new ChartPanel();
		setLeftComponent(chartPanel);
		
		optionsPanel = new OptionsPanel();
		setRightComponent(optionsPanel);
		
		setName("Supply Network");
		setOneTouchExpandable(true);
		setDividerSize(10);
		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(1);
		setDividerLocation(525);
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
		
		private JDateChooser startDate, endDate;
		private JCheckBox demandsCheck, capacitiesCheck;
		private JComboBox transportDisplayCombo;
		private CheckBoxTableModel<SupplyEdge> supplyEdgesModel;
		
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
			c.anchor = GridBagConstraints.LINE_END;
			add(new JLabel("Start Date: "), c);
			c.gridy++;
			add(new JLabel("End Date: "), c);
			c.gridy++;
			add(new JLabel("Display: "), c);
			c.gridy = 0;
			c.gridx++;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			startDate = new JDateChooser();
			startDate.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if(e.getPropertyName().equals("date"))
						chartPanel.updateView();
				}
			});
			add(startDate, c);
			c.gridy++;
			endDate = new JDateChooser();
			endDate.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if(e.getPropertyName().equals("date"))
						chartPanel.updateView();
				}
			});
			add(endDate, c);
			c.gridy++;
			transportDisplayCombo = new JComboBox();
			transportDisplayCombo.addItem(DEMANDS);
			transportDisplayCombo.addItem(RAW_CAPACITY);
			transportDisplayCombo.addItem(REMAINING_CAPACITY);
			transportDisplayCombo.addItem(NET_CAPACITY);
			transportDisplayCombo.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					chartPanel.updateView();
				}
			});
			add(transportDisplayCombo, c);
			c.gridx--;
			c.gridy++;
			c.gridwidth = 2;
			c.weightx = 1;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			demandsCheck = new JCheckBox("Label Aggregated Demands", true);
			demandsCheck.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					chartPanel.updateView();
				}
			});
			add(demandsCheck, c);
			c.gridy++;
			capacitiesCheck = new JCheckBox("Label Transports", true);
			capacitiesCheck.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					chartPanel.updateView();
				}
			});
			add(capacitiesCheck, c);
			c.gridy++;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			supplyEdgesModel = new CheckBoxTableModel<SupplyEdge>();
			supplyEdgesModel.addTableModelListener(new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if(e.getType()==TableModelEvent.UPDATE)
							chartPanel.updateView();
				}
			});
			JTable supplyEdgesTable = new JTable(supplyEdgesModel);
			supplyEdgesTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
			supplyEdgesTable.getTableHeader().setReorderingAllowed(false);
			supplyEdgesTable.getColumnModel().getColumn(0).setMaxWidth(25);
			supplyEdgesTable.getColumnModel().getColumn(1).setHeaderValue("Filter Supply Edges");
			supplyEdgesTable.setShowGrid(false);
			JScrollPane supplyEdgesScroll = new JScrollPane(supplyEdgesTable);
			supplyEdgesScroll.setPreferredSize(new Dimension(150,150));
			add(supplyEdgesScroll, c);
			c.gridy++;
			c.weighty = 0;
			c.fill = GridBagConstraints.NONE;
			JPanel supplyEdgesButtonPanel = new JPanel();
			supplyEdgesButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			JButton selectAllSupplyEdgesButton = new JButton("Select All");
			selectAllSupplyEdgesButton.setToolTipText("Select All Supply Edges");
			selectAllSupplyEdgesButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					supplyEdgesModel.selectAll();
				}
			});
			supplyEdgesButtonPanel.add(selectAllSupplyEdgesButton);
			JButton deselectAllSupplyEdgesButton = new JButton("Deselect All");
			deselectAllSupplyEdgesButton.setToolTipText("Deselect All Supply Edges");
			deselectAllSupplyEdgesButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					supplyEdgesModel.deselectAll();
				}
			});
			supplyEdgesButtonPanel.add(deselectAllSupplyEdgesButton);
			add(supplyEdgesButtonPanel, c);
			c.gridy++;
			c.weighty = 0;
			c.anchor = GridBagConstraints.LINE_END;
			c.fill = GridBagConstraints.NONE;
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
			startDate.setEnabled(getScenario()!=null);
			endDate.setEnabled(getScenario()!=null);
			transportDisplayCombo.setEnabled(getScenario()!=null);
			demandsCheck.setEnabled(getScenario()!=null);
			capacitiesCheck.setEnabled(getScenario()!=null);
			
			if(getScenario()!=null) {
				startDate.setSelectableDateRange(getScenario().getStartDate(), null);
				endDate.setSelectableDateRange(getScenario().getStartDate(), null);
			}
			
			supplyEdgesModel.clear();
			updateView();
		}
		
		/**
		 * Updates the view.
		 */
		public void updateView() {
			// update supply edges
			List<SupplyEdge> deselectedSupplyEdges = supplyEdgesModel.getDeselectedObjects();
			TableModelListener[] modelListeners = supplyEdgesModel.getTableModelListeners();
			for(TableModelListener l : modelListeners) {
				supplyEdgesModel.removeTableModelListener(l);
			}
			supplyEdgesModel.clear();
			ArrayList<SupplyEdge> edges = new ArrayList<SupplyEdge>();
			if(demandsTab.getSimulator()!=null) {
				for(SupplyEdge edge : demandsTab.getSimulator().getSupplyEdges()) {
					edges.add(edge);
				}
			}
			Collections.reverse(edges);
			for(SupplyEdge edge : edges) {
				supplyEdgesModel.addObject(edge, !deselectedSupplyEdges.contains(edge));
			}
			for(TableModelListener l : modelListeners) {
				supplyEdgesModel.addTableModelListener(l);
			}
			supplyEdgesModel.fireTableDataChanged();
			
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
			double minTime =  optionsPanel.startDate.getDate()==null?
					0:Math.max(0, 
							DateFunctions.getDaysBetween(
									getScenario().getStartDate(), 
									optionsPanel.startDate.getDate()));
			double maxTime = optionsPanel.endDate.getDate()==null?
					Double.MAX_VALUE:Math.max(minTime, 
							DateFunctions.getDaysBetween(
									getScenario().getStartDate(), 
									optionsPanel.endDate.getDate()));
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			
			JFreeChart chart = ChartFactory.createTimeSeriesChart(
					null, 		// title
					"Date", 	// x-axis label
					"Node", 	// y-axis label
					dataset, 	// data
					false, 
					true, 
					false);
			
			XYPlot plot = (XYPlot) chart.getPlot();
			SymbolAxis rangeAxis = createRangeAxis();
			plot.setRangeAxis(rangeAxis);
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
			double maxCapacity = 0;
			double minCapacity = Double.MAX_VALUE;
			double maxDemands = 0;
			double minDemands = Double.MAX_VALUE;
			for(SupplyEdge e : optionsPanel.supplyEdgesModel.getSelectedObjects()) {
				double capacity = getCapacity(e);
				if(Math.abs(capacity)<minCapacity) minCapacity = Math.abs(capacity);
				if(Math.abs(capacity)>maxCapacity) maxCapacity = Math.abs(capacity);
				
				SupplyPoint p = e.getPoint();
				if(demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()<minDemands) 
					minDemands = demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass();
				if(demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()>maxDemands) 
					maxDemands = demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass();
			}
			for(SupplyEdge e : optionsPanel.supplyEdgesModel.getSelectedObjects()) {
				if(e.getStartTime() >= minTime && e.getEndTime() <= maxTime) {
					double capacity = getCapacity(e);
					TimeSeries s = new TimeSeries(e.toString(), Hour.class);
					s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), e.getStartTime())), 
							node2Number(e.getOrigin(), rangeAxis));
					s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), e.getEndTime())), 
							node2Number(e.getDestination(), rangeAxis));
					((TimeSeriesCollection)dataset).addSeries(s);
					r.setSeriesShape(dataset.getSeriesCount() - 1, 
							new Ellipse2D.Double(0,0,0,0));
					int thickness = (int)(MIN_THICKNESS + (MAX_THICKNESS-MIN_THICKNESS)*(Math.abs(capacity)-minCapacity)/(maxCapacity-minCapacity+1));
					r.setSeriesStroke(dataset.getSeriesCount() - 1, new BasicStroke(thickness));
					if(capacity > 0) r.setSeriesPaint(dataset.getSeriesCount() - 1, new Color(0x66,0xff,0x66));
					else if(capacity == 0) r.setSeriesPaint(dataset.getSeriesCount() - 1, new Color(0x66,0x66,0x66));
					else r.setSeriesPaint(dataset.getSeriesCount() - 1, new Color(0xff,0,0));
				}
				
				SupplyPoint p = e.getPoint();
				if(p.getTime() >= minTime && p.getTime() <= maxTime) {
					TimeSeries s = new TimeSeries(p.toString(), Hour.class);
					s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), p.getTime())), 
							node2Number(p.getNode(), rangeAxis));
					((TimeSeriesCollection)dataset).addSeries(s);
					r.setSeriesPaint(dataset.getSeriesCount() - 1, new Color(0x00,0x99,0xff));
					double size;
					if(demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()==0) {
						size = 0;
					} else {
						size = MIN_SIZE+(MAX_SIZE-MIN_SIZE)*(demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()-minDemands)/(maxDemands-minDemands+1);
					}
					r.setSeriesShape(dataset.getSeriesCount() - 1, 
							new Ellipse2D.Double(-size/2D, -size/2D, size, size));
				}
				if(optionsPanel.demandsCheck.isSelected()&&demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()>0) {
					double x = new Hour(DateFunctions.getDate(getScenario().getStartDate(), p.getTime())).getFirstMillisecond();
					//x -= (p.getEdge().getEndTime()-p.getEdge().getStartTime())*24*3600*1000*2;
					double y = node2Number(p.getNode(), rangeAxis);// + 0.25;
					DecimalFormat format = new DecimalFormat("0");
					XYTextAnnotation annotation = new XYTextAnnotation(format.format(demandsTab.getSimulator().getAggregatedNodeDemands().get(p).getTotalMass()) + " kg", x, y);
					annotation.setPaint(Color.BLACK);
					r.addAnnotation(annotation);
				}
			}
			if(optionsPanel.capacitiesCheck.isSelected()) {
				for(SupplyEdge e : demandsTab.getSimulator().getSupplyEdges()) {
					double x1 = new Hour(DateFunctions.getDate(getScenario().getStartDate(), (e.getStartTime()+e.getEndTime())/2D)).getFirstMillisecond();
					//x -= (e.getEndTime()-e.getStartTime())*24*3600*1000/2D;
					double y1 = (node2Number(e.getDestination(), rangeAxis) + node2Number(e.getOrigin(), rangeAxis))/2D;
					DecimalFormat format = new DecimalFormat("0");
					double capacity = getCapacity(e);
					XYTextAnnotation capacityAnnotation;
					if(optionsPanel.transportDisplayCombo.getSelectedItem()==DEMANDS) {
						capacityAnnotation = new XYTextAnnotation(
							format.format(Math.abs(capacity)) + " kg", x1, y1);
					} else {
						capacityAnnotation = new XYTextAnnotation(
								format.format(capacity) + " kg", x1, y1);
					}
					double angle1 = Math.atan(-(
							(node2Number(e.getDestination(), rangeAxis)-node2Number(e.getOrigin(), rangeAxis))
							/(plot.getRangeAxis().getUpperBound()-plot.getRangeAxis().getLowerBound()))
							/((new Hour(DateFunctions.getDate(getScenario().getStartDate(), e.getEndTime())).getFirstMillisecond()-new Hour(DateFunctions.getDate(getScenario().getStartDate(), e.getStartTime())).getFirstMillisecond())
								/(plot.getDomainAxis().getUpperBound()-plot.getDomainAxis().getLowerBound())));
					capacityAnnotation.setRotationAngle(angle1);
					capacityAnnotation.setPaint(Color.BLACK);
					r.addAnnotation(capacityAnnotation);
				}
			}
				
			setChart(chart);
		}
		
		private double getCapacity(SupplyEdge edge) {
			double capacity = 0;
			if(optionsPanel.transportDisplayCombo.getSelectedItem()==RAW_CAPACITY) {
				for(I_Carrier carrier : edge.getCarriers()) {
					capacity += carrier.getMaxCargoMass();
				}
			} else if(optionsPanel.transportDisplayCombo.getSelectedItem()==REMAINING_CAPACITY)
				capacity = edge.getNetCargoMass();
			else if(optionsPanel.transportDisplayCombo.getSelectedItem()==DEMANDS)
				capacity = -demandsTab.getSimulator().getAggregatedEdgeDemands().get(edge).getTotalMass();
			else
				capacity = edge.getNetCargoMass() - demandsTab.getSimulator().getAggregatedEdgeDemands().get(edge).getTotalMass();
			return capacity;
		}
		
		/**
		 * Creates the range axis.
		 * 
		 * @return the symbol axis
		 */
		private SymbolAxis createRangeAxis() {
			Vector<String> nodeLabels = new Vector<String>();
			for (Node n : getScenario().getNetwork().getNodes()) {
				nodeLabels.add(n.getName());
			}
			String[] array = {};
			return new SymbolAxis(null, nodeLabels.toArray(array));
		}
		
		/**
		 * Node2 number.
		 * 
		 * @param node the node
		 * @param axis the axis
		 * 
		 * @return the int
		 */
		private int node2Number(Node node, SymbolAxis axis) {
			for (int i = 0; i < axis.getSymbols().length; i++) {
				if (axis.getSymbols()[i].equals(node.getName())) {
					return i;
				}
			}
			return -1;
		}
	}
}
