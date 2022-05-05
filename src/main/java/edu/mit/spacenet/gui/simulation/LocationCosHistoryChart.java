/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.SimNetwork;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Chart for displaying the aggregate resources broken down by class of supply at various locations.
 * 
 * @author Paul Grogan
 */
public class LocationCosHistoryChart extends JSplitPane {
  private static final long serialVersionUID = -6405124067606236705L;
  private SimulationTab tab;
  private ContainerComboBox<Location> locationCombo;
  private CheckBoxTableModel<ClassOfSupply> cosModel;
  private ChartPanel chartPanel;
  private JCheckBox linearizeDataCheck;

  /**
   * Instantiates a new location cos history chart.
   * 
   * @param tab the simulation tab
   */
  public LocationCosHistoryChart(SimulationTab tab) {
    this.tab = tab;

    chartPanel = new SNChartPanel();
    chartPanel.setPreferredSize(new Dimension(600, 400));
    setLeftComponent(chartPanel);

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridBagLayout());
    controlPanel.setBorder(BorderFactory.createTitledBorder("Chart Options"));
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    controlPanel.add(new JLabel("Location: "), c);
    c.gridy = 0;
    c.gridx++;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    locationCombo = new ContainerComboBox<Location>();
    locationCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          updateView();
        }
      }
    });
    controlPanel.add(locationCombo, c);
    c.gridx--;
    c.gridy++;
    c.gridwidth = 2;
    c.weightx = 0;
    c.weighty = 1;
    cosModel = new CheckBoxTableModel<ClassOfSupply>();
    for (int i = 1; i <= 10; i++) {
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
    cosTable.getColumnModel().getColumn(0)
        .setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
    cosTable.setShowGrid(false);
    JScrollPane cosScroll = new JScrollPane(cosTable);
    cosScroll.setPreferredSize(new Dimension(150, 200));
    controlPanel.add(cosScroll, c);
    c.gridy++;
    c.weighty = 0;
    c.fill = GridBagConstraints.NONE;
    JPanel buttonPanel = new JPanel();
    buttonPanel.setOpaque(false);
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
    prop.setOpaque(false);
    prop.setPreferredSize(new Dimension(1, 15));
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

    controlPanel.setMinimumSize(new Dimension(150, 50));
    setRightComponent(controlPanel);

    setName("Location History");
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
    locationCombo.removeAllItems();
    updateChart();
  }

  /**
   * Update view.
   */
  public void updateView() {
    tab.getScenarioPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    Location selectedLocation = (Location) locationCombo.getSelectedItem();
    ItemListener locationListener = locationCombo.getItemListeners()[0];
    locationCombo.removeItemListener(locationListener);
    locationCombo.removeAllItems();
    for (Location location : getScenario().getNetwork().getLocations()) {
      locationCombo.addItem(location);
    }
    if (selectedLocation != null)
      locationCombo.setSelectedItem(selectedLocation);
    locationCombo.addItemListener(locationListener);
    updateChart();
    tab.getScenarioPanel().setCursor(Cursor.getDefaultCursor());
  }

  private void updateChart() {
    TimeSeriesCollection dataset = new TimeSeriesCollection();

    JFreeChart chart =
        ChartFactory.createTimeSeriesChart(null, "Date", "Mass (kg)", dataset, true, true, false);

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

    if (locationCombo.getSelectedItem() != null) {
      for (ClassOfSupply cos : cosModel.getSelectedObjects()) {
        TimeSeries s = new TimeSeries("COS " + cos.getId(), Hour.class);
        double amount = 0;
        for (SimNetwork network : tab.getSimulator().getNetworkHistory()) {
          Location location;
          if (locationCombo.getSelectedItem() instanceof Node) {
            location = network.getNetwork()
                .getNodeByTid(((Node) locationCombo.getSelectedItem()).getTid());
          } else {
            location = network.getNetwork()
                .getEdgeByTid(((Edge) locationCombo.getSelectedItem()).getTid());
          }
          Date date = DateFunctions.getDate(getScenario().getStartDate(), network.getTime());
          if (!linearizeDataCheck.isSelected() && amount > 0)
            s.addOrUpdate(new Hour(date).previous(), amount);
          amount = location.getTotalMass(cos, tab.getSimulator());
          s.addOrUpdate(new Hour(date), amount);
        }
        dataset.addSeries(s);

        r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1, -1, 2, 2));
        r.setSeriesPaint(dataset.getSeriesCount() - 1, cos.getColor());
      }
    }

    chartPanel.setChart(chart);
  }
}
