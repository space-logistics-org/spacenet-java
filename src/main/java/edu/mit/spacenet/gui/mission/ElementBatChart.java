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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.MiniSimulator;
import edu.mit.spacenet.simulator.SimState;
import edu.mit.spacenet.util.DateFunctions;

/**
 * A bat-style chart to display the element location history with associated
 * control panel.
 * 
 * @author Paul Grogan
 */
public class ElementBatChart extends JSplitPane {
	private static final long serialVersionUID = -6405124067606236705L;
	private MissionsSplitPane missionsSplitPane;
	private JDateChooser startDate, endDate;
	private CheckBoxTableModel<I_Element> elementsModel;
	private ChartPanel batChartPanel;
	private JButton refreshButton;
	
	/**
	 * Instantiates a new element bat chart.
	 * 
	 * @param tab the missions tab
	 */
	public ElementBatChart(MissionsSplitPane missionsSplitPane) {
		this.missionsSplitPane = missionsSplitPane;
		
		batChartPanel = new SNChartPanel();
		batChartPanel.setPreferredSize(new Dimension(600,400));
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
		c.gridy++;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = 1;
		elementsModel = new CheckBoxTableModel<I_Element>();
		elementsModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && isAutoRefresh()) updateChart();
			}
		});
		JTable elementsTable = new JTable(elementsModel);
		elementsTable.getColumnModel().getColumn(0).setHeaderRenderer(new DefaultTableCellRenderer() {
			// custom renderer to display the eye header icon
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
                setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/eye.png")));
                setHorizontalAlignment(JLabel.CENTER);
                return this;
			}
		});
		elementsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			// custom renderer to display the element icons
			private static final long serialVersionUID = 1L;
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(value instanceof I_Element) setIcon(((I_Element)value).getIcon());
                return this;
			}
		});
		elementsTable.getTableHeader().setReorderingAllowed(false);
		elementsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		elementsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Elements");
		elementsTable.setShowGrid(false);
		JScrollPane elementsScroll = new JScrollPane(elementsTable);
		elementsScroll.setPreferredSize(new Dimension(150,200));
		controlPanel.add(elementsScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllElementsButton = new JButton("Select All");
		selectAllElementsButton.setToolTipText("Select All Elements");
		selectAllElementsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				elementsModel.selectAll();
			}
		});
		buttonPanel.add(selectAllElementsButton);
		JButton deselectAllElementsButton = new JButton("Deselect All");
		deselectAllElementsButton.setToolTipText("Deselect All Elements");
		deselectAllElementsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				elementsModel.deselectAll();
			}
		});
		buttonPanel.add(deselectAllElementsButton);
		controlPanel.add(buttonPanel, c);
		c.gridy++;
		JPanel prop = new JPanel();
		prop.setOpaque(false);
		prop.setPreferredSize(new Dimension(1,15));
		controlPanel.add(prop, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		refreshButton = new JButton("Refresh", new ImageIcon(getClass().getClassLoader().getResource("icons/arrow_refresh.png")));
		refreshButton.setVisible(false);
		refreshButton.setMargin(new Insets(3,3,3,3));
		refreshButton.setToolTipText("Refresh Chart");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshChart();
			}
		});
		controlPanel.add(refreshButton, c);
		
		setRightComponent(controlPanel);

		setName("Element Bat Chart");
		setOneTouchExpandable(true);
		setDividerSize(10);
		setBorder(BorderFactory.createEmptyBorder());
		setResizeWeight(1);
		setDividerLocation(490);
	}
	
	/**
	 * Initializes the chart for a new scenario.
	 */
	public void initialize() {
		startDate.setEnabled(getScenario()!=null);
		endDate.setEnabled(getScenario()!=null);
		
		if(getScenario()!=null) {
			startDate.setSelectableDateRange(getScenario().getStartDate(), null);
			endDate.setSelectableDateRange(getScenario().getStartDate(), null);
			refreshChart();
		}
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
	 * Gets the simulator.
	 * 
	 * @return the simulator
	 */
	private MiniSimulator getSimulator() {
		return missionsSplitPane.getMissionsTab().getSimulator();
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
	 * Updates the view.
	 */
	public void updateView() {
		if(isAutoRefresh()) {
			refreshButton.setVisible(false);
			refreshChart();
		} else {
			refreshButton.setVisible(true);
		}
	}
	private boolean isUpdating = false;
	
	/**
	 * Refreshes the chart. Uses a swing worker to run the process in a separate
	 * thread so the GUI does not freeze.
	 */
	private void refreshChart() {
		if(isUpdating) return;
		isUpdating = true;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			public Void doInBackground() throws Exception {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Refreshing Chart...");
				List<I_Element> selectedElements = elementsModel.getSelectedObjects();
				elementsModel.clear();
				for(I_Element element : getScenario().getElements()) {
					elementsModel.addObject(element, selectedElements.contains(element));
				}
				try {
					updateChart();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}
			public void done() {
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
				isUpdating = false;
			}
		};
		worker.execute();
	}
	
	/**
	 * Updates the chart (time-intensive for large campaigns or for many
	 * elements.
	 */
	private void updateChart() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				null, 
				"Date", 
				"Node", 
				dataset, 
				true, 
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

		double delta = -0.5;
		for(I_Element element : elementsModel.getSelectedObjects()) {
			delta += 1d/(elementsModel.getSelectedObjects().size()+1);
			TimeSeries s = new TimeSeries(element.getName(), Hour.class);
			for(SimState state : getSimulator().getLocationHistory()) {
				Date date = DateFunctions.getDate(getScenario().getStartDate(), state.getTime());
				if(state.getLocation(element) != null 
						&& state.getLocation(element) instanceof Node
						&& (startDate.getDate()==null || date.after(startDate.getDate()))
						&& (endDate.getDate()==null || date.before(endDate.getDate()))) {
					s.addOrUpdate(new Hour(date), delta + node2Number((Node)state.getLocation(element), rangeAxis));
				}
			}
			/* TODO: this section may attempt at removing "unnecessary" points,
			 * i.e. the points where the element is at the same nodeboth before
			 * and after.
			for(int i=1; i < s.getItemCount()-1; i++) {
				if(s.getDataItem(i).getValue().equals(s.getDataItem(i+1).getValue())
						&& s.getDataItem(i).getValue().equals(s.getDataItem(i-1).getValue())) {
					s.delete(s.getTimePeriod(i));
					i=1;
				}
			}
			*/
			if(s.getItemCount()>0) {
				((TimeSeriesCollection)dataset).addSeries(s);
				r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(0,0,0,0));
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
	 * Converts a node to its numeric value used in plotting.
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
}