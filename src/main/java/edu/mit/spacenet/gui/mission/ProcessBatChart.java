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
package edu.mit.spacenet.gui.mission;

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
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import com.toedter.calendar.JDateChooser;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.SimState;
import edu.mit.spacenet.simulator.event.BurnEvent;
import edu.mit.spacenet.simulator.event.CreateEvent;
import edu.mit.spacenet.simulator.event.DemandEvent;
import edu.mit.spacenet.simulator.event.EvaEvent;
import edu.mit.spacenet.simulator.event.EventType;
import edu.mit.spacenet.simulator.event.ExplorationProcess;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.MoveEvent;
import edu.mit.spacenet.simulator.event.ReconfigureEvent;
import edu.mit.spacenet.simulator.event.ReconfigureGroupEvent;
import edu.mit.spacenet.simulator.event.RemoveEvent;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.simulator.event.TransferEvent;
import edu.mit.spacenet.util.DateFunctions;

/**
 * A bat-style chart that displays scenario events. If used from a missions 
 * split pane, the control panel is visible, if used from a mission split pane
 * (while editing one mission), it will be hidden.
 * 
 * @author Paul Grogan
 */
public class ProcessBatChart extends JSplitPane {
	private static final long serialVersionUID = -6405124067606236705L;
	private MissionsSplitPane missionsSplitPane;
	private JDateChooser startDate, endDate;
	private CheckBoxTableModel<Mission> missionsModel;
	private CheckBoxTableModel<EventType> eventsModel;
	private ChartPanel batChartPanel;
	private JCheckBox waitCheck;
	private JButton refreshButton;
	
	private ChartUpdater chartUpdater;
	
	/**
	 * Instantiates a new process bat chart within a mission split pane.
	 * 
	 * @param missionsSplitPane the missions split pane
	 */
	public ProcessBatChart(MissionsSplitPane missionsSplitPane) {
		this.missionsSplitPane = missionsSplitPane;
		buildChart();
	}
	
	/**
	 * Builds the chart.
	 */
	private void buildChart() {
		batChartPanel = new SNChartPanel();
		batChartPanel.setPreferredSize(new Dimension(500,300));
		setLeftComponent(batChartPanel);
		
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
		c.fill = GridBagConstraints.BOTH;
		controlPanel.add(new JLabel("Start Date: "), c);
		c.gridy++;
		controlPanel.add(new JLabel("End Date: "), c);
		c.gridy--;
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		startDate = new JDateChooser();
		startDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equals("date") && isAutoRefresh()) updateChart();
			}
		});
		controlPanel.add(startDate, c);
		c.gridy++;
		endDate = new JDateChooser();
		endDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equals("date") && isAutoRefresh()) updateChart();
			}
		});
		controlPanel.add(endDate, c);
		c.gridx--;
		c.gridwidth = 2;
		c.gridy++;
		c.weighty = 1;
		missionsModel = new CheckBoxTableModel<Mission>();
		missionsModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && isAutoRefresh()) updateChart();
			}
		});
		JTable missionsTable = new JTable(missionsModel);
		missionsTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
		missionsTable.getTableHeader().setReorderingAllowed(false);
		missionsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		missionsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Missions");
		missionsTable.setShowGrid(false);
		JScrollPane missionsScroll = new JScrollPane(missionsTable);
		missionsScroll.setPreferredSize(new Dimension(150,100));
		controlPanel.add(missionsScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		JPanel missionsButtonPanel = new JPanel();
		missionsButtonPanel.setOpaque(false);
		missionsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllMissionsButton = new JButton("Select All");
		selectAllMissionsButton.setToolTipText("Select All Missions");
		selectAllMissionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				missionsModel.selectAll();
			}
		});
		missionsButtonPanel.add(selectAllMissionsButton);
		JButton deselectAllMissionsButton = new JButton("Deselect All");
		deselectAllMissionsButton.setToolTipText("Deselect All Missions");
		deselectAllMissionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				missionsModel.deselectAll();
			}
		});
		missionsButtonPanel.add(deselectAllMissionsButton);
		controlPanel.add(missionsButtonPanel, c);
		c.weightx = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridy++;
		JPanel prop1 = new JPanel();
		prop1.setOpaque(false);
		prop1.setPreferredSize(new Dimension(1,15));
		controlPanel.add(prop1, c);
		c.gridy++;
		c.weighty = 1;
		eventsModel = new CheckBoxTableModel<EventType>();
		for(EventType t : EventType.values()) {
			eventsModel.addObject(t);
		}
		eventsModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && isAutoRefresh()) updateChart();
			}
		});
		JTable eventsTable = new JTable(eventsModel);
		eventsTable.getColumnModel().getColumn(0).setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
		eventsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			// custom renderer to show the event type icon
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(value instanceof EventType) setIcon(((EventType)value).getIcon());
                return this;
			}
		});
		eventsTable.getTableHeader().setReorderingAllowed(false);
		eventsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		eventsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Event Types");
		eventsTable.setShowGrid(false);
		JScrollPane eventsScroll = new JScrollPane(eventsTable);
		eventsScroll.setPreferredSize(new Dimension(150,100));
		controlPanel.add(eventsScroll, c);
		c.gridy++;
		c.weighty = 0;
		waitCheck = new JCheckBox("Draw Elements Waiting (Null Process)");
		waitCheck.setOpaque(false);
		waitCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(isAutoRefresh()) updateChart();
			}
		});
		controlPanel.add(waitCheck, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		JPanel eventsButtonPanel = new JPanel();
		eventsButtonPanel.setOpaque(false);
		eventsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllEventsButton = new JButton("Select All");
		selectAllEventsButton.setToolTipText("Select All Events");
		selectAllEventsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventsModel.selectAll();
				waitCheck.setSelected(true);
			}
		});
		eventsButtonPanel.add(selectAllEventsButton);
		JButton deselectAllEventsButton = new JButton("Deselect All");
		deselectAllEventsButton.setToolTipText("Deselect All Events");
		deselectAllEventsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventsModel.deselectAll();
				waitCheck.setSelected(false);
			}
		});
		eventsButtonPanel.add(deselectAllEventsButton);
		controlPanel.add(eventsButtonPanel, c);
		c.gridy++;
		JPanel prop2 = new JPanel();
		prop2.setOpaque(false);
		prop2.setPreferredSize(new Dimension(1,15));
		controlPanel.add(prop2, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		refreshButton = new JButton("Refresh", new ImageIcon(getClass().getClassLoader().getResource("icons/arrow_refresh.png")));
		refreshButton.setVisible(false);
		refreshButton.setMargin(new Insets(3,3,3,3));
		refreshButton.setToolTipText("Refresh Chart");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateChart();
			}
		});
		controlPanel.add(refreshButton, c);
		
		setRightComponent(controlPanel);
		
		setName("Process Bat Chart");
		setOneTouchExpandable(true);
		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(1);
		
		setDividerSize(10);
		setDividerLocation(490);
	}
	
	/**
	 * Initializes the component for a new scenario.
	 */
	public void initialize() {
		startDate.setEnabled(getScenario()!=null);
		endDate.setEnabled(getScenario()!=null);
		if(getScenario()!=null) {
			startDate.setSelectableDateRange(getScenario().getStartDate(), null);
			endDate.setSelectableDateRange(getScenario().getStartDate(), null);
		}
		missionsModel.clear();
		updateView();
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return missionsSplitPane.getMissionsTab().getScenarioPanel().getScenario();
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
	 * Updates the view. (Refreshes the chart if auto-refresh is enabled, 
	 * ensures manual refresh button is visible otherwise).
	 */
	public void updateView() {
		// update mission list
		List<Mission> deselectedMissions = missionsModel.getDeselectedObjects();
		TableModelListener[] modelListeners = missionsModel.getTableModelListeners();
		for(TableModelListener l : modelListeners) {
			missionsModel.removeTableModelListener(l);
		}
		missionsModel.clear();
		for(Mission mission : getScenario().getMissionList()) {
			missionsModel.addObject(mission, !deselectedMissions.contains(mission));
		}
		for(TableModelListener l : modelListeners) {
			missionsModel.addTableModelListener(l);
		}
		missionsModel.fireTableDataChanged();
		
		// update auto refresh button
		if(isAutoRefresh()) {
			refreshButton.setVisible(false);
		} else {
			refreshButton.setVisible(true);
		}

		// update chart
		if(isAutoRefresh()) updateChart();
	}
	
	/**
	 * Refreshes the chart (time-intensive for large campaigns). Uses a swing
	 * worker to run the update process in a separate thread so the GUI does 
	 * not freeze.
	 */
	public void updateChart() {
		while(chartUpdater != null && !chartUpdater.isDone()) {
			// wait until current update is complete
		}
		chartUpdater = new ChartUpdater();
		chartUpdater.execute();
	}
	
	/**
	 * Updates the chart (Time-intensive for large campaigns).
	 */
	private void rebuildChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, 
				"Date", 
				"Node", 
				dataset, 
				false, 
				true, 
				false);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		SymbolAxis rangeAxis = createRangeAxis();
		plot.setRangeAxis(rangeAxis);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		XYItemRenderer r = chart.getXYPlot().getRenderer();

		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}
		
		for(Mission mission : missionsModel.getSelectedObjects()) {
			for(I_Event event : mission.getEventList()) {
				if((startDate.getDate()==null||DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime()).compareTo(startDate.getDate())>=0) 
						&& (endDate.getDate()==null||DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime()).compareTo(endDate.getDate())<=0)
						&& event.getLocation() instanceof Node) {
					 if(event instanceof SpaceTransport 
							 && ((SpaceTransport)event).getEdge() != null
							 && eventsModel.getSelectedObjects().contains(EventType.SPACE_TRANSPORT)) {
						TimeSeries s = new TimeSeries(event.getName(), Hour.class);
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
								node2Number(((SpaceTransport) event).getEdge().getOrigin(), rangeAxis));
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((SpaceTransport)event).getEdge().getDuration())), 
								node2Number(((SpaceTransport) event).getEdge().getDestination(), rangeAxis));
						((TimeSeriesCollection)dataset).addSeries(s);
						r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
						r.setSeriesShape(dataset.getSeriesCount() - 1, 
								new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
					} else if(event instanceof SurfaceTransport 
							&& ((SurfaceTransport)event).getEdge() != null
							&& eventsModel.getSelectedObjects().contains(EventType.SURFACE_TRANSPORT)) {
						TimeSeries s = new TimeSeries(event.getName(), Hour.class);
						if(((SurfaceTransport)event).isReversed()) {
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number(((SurfaceTransport) event).getEdge().getDestination(), rangeAxis));
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((SurfaceTransport)event).getDuration())), 
									node2Number(((SurfaceTransport) event).getEdge().getOrigin(), rangeAxis));
						} else {
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number(((SurfaceTransport) event).getEdge().getOrigin(), rangeAxis));
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((SurfaceTransport)event).getDuration())), 
									node2Number(((SurfaceTransport) event).getEdge().getDestination(), rangeAxis));
						}
						((TimeSeriesCollection)dataset).addSeries(s);
						r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GREEN);
						r.setSeriesShape(dataset.getSeriesCount() - 1, 
								new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
					} else if(event instanceof FlightTransport 
							&& ((FlightTransport)event).getEdge() != null
							&& eventsModel.getSelectedObjects().contains(EventType.FLIGHT_TRANSPORT)) {
						TimeSeries s = new TimeSeries(event.getName(), Hour.class);
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
								node2Number(((FlightTransport) event).getEdge().getOrigin(), rangeAxis));
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((FlightTransport)event).getEdge().getDuration())), 
								node2Number(((FlightTransport) event).getEdge().getDestination(), rangeAxis));
						((TimeSeriesCollection)dataset).addSeries(s);
						r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.YELLOW);
						r.setSeriesShape(dataset.getSeriesCount() - 1, 
								new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
					} else if(event instanceof ExplorationProcess 
							&& event.getLocation() != null
							&& eventsModel.getSelectedObjects().contains(EventType.EXPLORATION)) {
						TimeSeries s = new TimeSeries(event.getName(), Hour.class);
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
								node2Number((Node)event.getLocation(), rangeAxis));
						s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((ExplorationProcess)event).getDuration())), 
								node2Number((Node)event.getLocation(), rangeAxis));
						((TimeSeriesCollection)dataset).addSeries(s);
						r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.CYAN);
						r.setSeriesShape(dataset.getSeriesCount() - 1, 
								new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
					} else if(event.getLocation() != null){
						if(event instanceof CreateEvent
								&& eventsModel.getSelectedObjects().contains(EventType.CREATE)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GREEN);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof MoveEvent
								&& eventsModel.getSelectedObjects().contains(EventType.MOVE)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.ORANGE);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof TransferEvent
								&& eventsModel.getSelectedObjects().contains(EventType.TRANSFER)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.ORANGE);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof RemoveEvent
								&& eventsModel.getSelectedObjects().contains(EventType.REMOVE)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLACK);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof DemandEvent
								&& eventsModel.getSelectedObjects().contains(EventType.DEMAND)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLUE);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof ReconfigureEvent
								&& eventsModel.getSelectedObjects().contains(EventType.RECONFIGURE)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.PINK);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof ReconfigureGroupEvent
								&& eventsModel.getSelectedObjects().contains(EventType.RECONFIGURE_GROUP)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.PINK);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof BurnEvent
								&& eventsModel.getSelectedObjects().contains(EventType.BURN)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						} else if(event instanceof EvaEvent
								&& eventsModel.getSelectedObjects().contains(EventType.EVA)) {
							TimeSeries s = new TimeSeries(event.getName(), Hour.class);
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime())), 
									node2Number((Node)event.getLocation(), rangeAxis));
							s.addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), DateFunctions.getDaysBetween(getScenario().getStartDate(), mission.getStartDate()) + event.getTime() + ((EvaEvent)event).getEvaDuration()/24)), 
									node2Number((Node)event.getLocation(), rangeAxis));
							((TimeSeriesCollection)dataset).addSeries(s);
							r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.CYAN);
							r.setSeriesShape(dataset.getSeriesCount() - 1, 
									new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
						}
					}
				}
			}
		}
		if(waitCheck.isSelected()) {
			// this section uses the simulator data to plot the time-history
			// of element locations
			Mission firstMission = missionsModel.getSelectedObjects().size()>0?missionsModel.getSelectedObjects().get(0):null;
			double minTime1 = firstMission==null?Double.MAX_VALUE:DateFunctions.getDaysBetween(getScenario().getStartDate(), firstMission.getStartDate());
			Mission lastMission = missionsModel.getSelectedObjects().size()>0?missionsModel.getSelectedObjects().get(missionsModel.getSelectedObjects().size()-1):null;
			double maxTime1 = lastMission==null?0:DateFunctions.getDaysBetween(getScenario().getStartDate(), DateFunctions.getDate(lastMission.getStartDate(), lastMission.getDuration()));

			double minTime2 = startDate.getDate()==null?0:DateFunctions.getDaysBetween(getScenario().getStartDate(), startDate.getDate());
			double maxTime2 = endDate.getDate()==null?Double.MAX_VALUE:DateFunctions.getDaysBetween(getScenario().getStartDate(), endDate.getDate());
			
			double minTime = Math.max(minTime1, minTime2);
			double maxTime = Math.min(maxTime1, maxTime2);
			
			for(I_Element element : getScenario().getElements()) {
				Node prevNode = null;
				List<TimeSeries> series = new ArrayList<TimeSeries>();
				int i = -1;
				for(SimState state : missionsSplitPane.getMissionsTab().getSimulator().getLocationHistory()) {
					if(prevNode!=null && prevNode.equals(state.getLocation(element))) {
						series.get(i).addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), Math.min(Math.max(minTime, state.getTime()), maxTime))), 
										node2Number((Node)state.getLocation(element), rangeAxis));
					} else if(state.getLocation(element) instanceof Node) {
						prevNode = (Node)state.getLocation(element);
						series.add(new TimeSeries(element + "waiting at " + prevNode + " (" + i + ")"));
						i++;
						series.get(i).addOrUpdate(new Hour(DateFunctions.getDate(getScenario().getStartDate(), Math.min(Math.max(minTime, state.getTime()), maxTime))), 
								node2Number((Node)state.getLocation(element), rangeAxis));
					} else {
						prevNode = null;
					}
				}
				for(TimeSeries s : series) {
					((TimeSeriesCollection)dataset).addSeries(s);
					r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GRAY);
					r.setSeriesShape(dataset.getSeriesCount() - 1, 
							new Ellipse2D.Double(0, 0, 0, 0));
				}
			}
		}
		
		batChartPanel.setChart(chart);
	}
	
	/**
	 * Creates the range axis (list of nodes on the vertical axis).
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
	 * Converts a node to its numeric representation for the symbol axis.
	 * 
	 * @param node the node
	 * @param axis the axis
	 * 
	 * @return the number
	 */
	private int node2Number(Node node, SymbolAxis axis) {
		for (int i = 0; i < axis.getSymbols().length; i++) {
			if (axis.getSymbols()[i].equals(node.getName())) {
				return i;
			}
		}
		return -1;
	}
	

	/**
	 * An updater to process the charts in a separate thread.
	 */
	private class ChartUpdater extends SwingWorker<Void, Void> {
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
		 */
		protected Void doInBackground() {
			try {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Refreshing Charts...");
				missionsSplitPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				rebuildChart();
				missionsSplitPane.setCursor(Cursor.getDefaultCursor());
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
}