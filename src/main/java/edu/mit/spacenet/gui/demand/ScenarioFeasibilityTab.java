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
package edu.mit.spacenet.gui.demand;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.NodeListCellRenderer;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Component for visualizing the feasibility of a scenario based on the used and remaining delivery
 * capacity.
 * 
 * @author Paul Grogan
 */
public class ScenarioFeasibilityTab extends JSplitPane {
  private static final long serialVersionUID = -7246327673230474018L;

  private static final String RAW_CAPACITY = "Raw Delivery Capacity";
  private static final String REMAINING_CAPACITY = "Remaining Delivery Capacity";
  private static final String ESTIMATED_DEMANDS = "Estimated Demands";

  private DemandsTab demandsTab;

  private OptionsPanel optionsPanel;
  private ChartPanel chartPanel;

  /**
   * Instantiates a new scenario feasibility tab.
   * 
   * @param demandsTab the demands tab
   */
  public ScenarioFeasibilityTab(DemandsTab demandsTab) {
    this.demandsTab = demandsTab;

    chartPanel = new ChartPanel();
    setLeftComponent(chartPanel);

    optionsPanel = new OptionsPanel(this);
    setRightComponent(optionsPanel);

    setName("Scenario Feasibility");
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
   * A custom JPanel to hold the scenario feasibility chart options.
   */
  private class OptionsPanel extends JPanel {
    private static final long serialVersionUID = -8521945559229981641L;

    private ScenarioFeasibilityTab feasibilityTab;

    private JComboBox<Node> nodeCombo;
    private CheckBoxTableModel<SupplyEdge> supplyEdgesModel;
    private CheckBoxTableModel<String> dataModel;
    private JCheckBox cumulativeCheck, timeDomainCheck, legendCheck, smoothDemandsCheck;
    private JButton refreshButton;

    /**
     * Instantiates a new options panel.
     */
    public OptionsPanel(ScenarioFeasibilityTab feasibilityTab) {
      this.feasibilityTab = feasibilityTab;
      buildPanel();
    }

    /**
     * Builds the panel.
     */
    private void buildPanel() {
      setLayout(new GridBagLayout());
      setBorder(BorderFactory.createTitledBorder("Chart Options"));
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(2, 2, 2, 2);
      c.gridx = 0;
      c.gridy = 0;
      c.anchor = GridBagConstraints.LINE_START;
      c.gridwidth = 2;
      nodeCombo = new JComboBox<Node>();
      nodeCombo.setRenderer(new NodeListCellRenderer());
      nodeCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == ItemEvent.SELECTED)
            feasibilityTab.updateView();
        }
      });
      JPanel nodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
      nodePanel.add(new JLabel("Feasibility at Node: "));
      nodePanel.add(nodeCombo);
      add(nodePanel, c);
      c.gridwidth = 1;
      c.gridy++;
      cumulativeCheck = new JCheckBox("Cumulative Plot");
      cumulativeCheck.setSelected(true);
      cumulativeCheck.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          chartPanel.updateView();
          timeDomainCheck.setEnabled(cumulativeCheck.isSelected());
          if (!cumulativeCheck.isSelected())
            timeDomainCheck.setSelected(false);
        }
      });
      add(cumulativeCheck, c);
      c.gridx++;
      timeDomainCheck = new JCheckBox("Time Domain");
      timeDomainCheck.setSelected(true);
      timeDomainCheck.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          chartPanel.updateView();
        }
      });
      add(timeDomainCheck, c);
      c.gridy++;
      c.gridx = 0;
      legendCheck = new JCheckBox("Legend");
      legendCheck.setSelected(true);
      legendCheck.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          chartPanel.updateView();
        }
      });
      add(legendCheck, c);
      c.gridx++;
      smoothDemandsCheck = new JCheckBox("Smooth Demands", true);
      smoothDemandsCheck.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          chartPanel.updateView();
        }
      });
      add(smoothDemandsCheck, c);
      c.gridy++;
      c.gridx = 0;
      c.gridwidth = 2;
      c.weightx = 1;
      c.weighty = 1;
      c.fill = GridBagConstraints.BOTH;
      supplyEdgesModel = new CheckBoxTableModel<SupplyEdge>();
      supplyEdgesModel.addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
          if (e.getType() == TableModelEvent.UPDATE)
            chartPanel.updateView();
        }
      });
      JTable supplyEdgesTable = new JTable(supplyEdgesModel);
      supplyEdgesTable.getColumnModel().getColumn(0)
          .setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
      supplyEdgesTable.getTableHeader().setReorderingAllowed(false);
      supplyEdgesTable.getColumnModel().getColumn(0).setMaxWidth(25);
      supplyEdgesTable.getColumnModel().getColumn(1).setHeaderValue("Filter Supply Edges");
      supplyEdgesTable.setShowGrid(false);
      JScrollPane supplyEdgesScroll = new JScrollPane(supplyEdgesTable);
      supplyEdgesScroll.setPreferredSize(new Dimension(150, 150));
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
      c.weightx = 0;
      c.fill = GridBagConstraints.BOTH;
      c.gridy++;
      c.weighty = 1;
      dataModel = new CheckBoxTableModel<String>();
      dataModel.addObject(RAW_CAPACITY);
      dataModel.addObject(REMAINING_CAPACITY);
      dataModel.addObject(ESTIMATED_DEMANDS);
      dataModel.addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
          if (e.getType() == TableModelEvent.UPDATE)
            chartPanel.updateView();
        }
      });
      JTable dataTable = new JTable(dataModel);
      dataTable.getColumnModel().getColumn(0)
          .setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
      dataTable.getTableHeader().setReorderingAllowed(false);
      dataTable.getColumnModel().getColumn(0).setMaxWidth(25);
      dataTable.getColumnModel().getColumn(1).setHeaderValue("Filter Data");
      dataTable.setShowGrid(false);
      JScrollPane dataScroll = new JScrollPane(dataTable);
      dataScroll.setPreferredSize(new Dimension(150, 50));
      add(dataScroll, c);
      c.gridy++;
      c.weighty = 0;
      c.fill = GridBagConstraints.NONE;
      JPanel dataButtonPanel = new JPanel();
      dataButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
      JButton selectAllDataButton = new JButton("Select All");
      selectAllDataButton.setToolTipText("Select All Data");
      selectAllDataButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dataModel.selectAll();
        }
      });
      dataButtonPanel.add(selectAllDataButton);
      JButton deselectAllDataButton = new JButton("Deselect All");
      deselectAllDataButton.setToolTipText("Deselect All Data");
      deselectAllDataButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dataModel.deselectAll();
        }
      });
      dataButtonPanel.add(deselectAllDataButton);
      add(dataButtonPanel, c);
      c.gridy++;
      c.anchor = GridBagConstraints.LINE_END;
      refreshButton = new JButton("Refresh",
          new ImageIcon(getClass().getClassLoader().getResource("icons/arrow_refresh.png")));
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
     * Initializes the options panel.
     */
    public void initialize() {
      nodeCombo.setEnabled(getScenario() != null);
      cumulativeCheck.setEnabled(getScenario() != null);
      timeDomainCheck.setEnabled(getScenario() != null);

      if (getScenario() != null) {
        // initialize nodes
        Object selectedNode = nodeCombo.getSelectedItem();
        ItemListener[] nodeListeners = nodeCombo.getItemListeners();
        for (ItemListener l : nodeListeners) {
          nodeCombo.removeItemListener(l);
        }
        nodeCombo.removeAllItems();
        for (Node node : getScenario().getNetwork().getNodes()) {
          nodeCombo.addItem(node);
        }
        if (selectedNode != null) {
          nodeCombo.setSelectedItem(selectedNode);
        } else if (getScenario().getMissionList().size() > 0
            && getScenario().getMissionList().get(0).getDestination() != null) {
          nodeCombo.setSelectedItem(getScenario().getMissionList().get(0).getDestination());
        } else {
          nodeCombo.setSelectedIndex(0);
        }
        for (ItemListener l : nodeListeners) {
          nodeCombo.addItemListener(l);
        }
      }
      supplyEdgesModel.clear();
      updateView();
    }

    /**
     * Updates the options panel.
     */
    public void updateView() {
      // update supply edges
      List<SupplyEdge> deselectedSupplyEdges = supplyEdgesModel.getDeselectedObjects();
      TableModelListener[] modelListeners = supplyEdgesModel.getTableModelListeners();
      for (TableModelListener l : modelListeners) {
        supplyEdgesModel.removeTableModelListener(l);
      }
      supplyEdgesModel.clear();
      ArrayList<SupplyEdge> edges = new ArrayList<SupplyEdge>();
      if (demandsTab.getSimulator() != null) {
        for (SupplyEdge edge : demandsTab.getSimulator().getSupplyEdges()) {
          if (edge.getDestination().equals(nodeCombo.getSelectedItem()))
            edges.add(edge);
        }
      }
      Collections.reverse(edges);
      for (SupplyEdge edge : edges) {
        if (edge.getDestination().equals(nodeCombo.getSelectedItem())) {
          supplyEdgesModel.addObject(edge, !deselectedSupplyEdges.contains(edge));
        }
      }
      for (TableModelListener l : modelListeners) {
        supplyEdgesModel.addTableModelListener(l);
      }
      supplyEdgesModel.fireTableDataChanged();

      // update auto refresh button visibility
      if (isAutoRefresh()) {
        refreshButton.setVisible(false);
      } else {
        refreshButton.setVisible(true);
      }
    }
  }

  private class ChartPanel extends SNChartPanel {
    private static final long serialVersionUID = -4706484000256730980L;

    public ChartPanel() {
      setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Initialize the chart panel for a new scenario.
     */
    public void initialize() {
      updateChart();
    }

    /**
     * Updates the chart panel.
     */
    public void updateView() {
      if (isAutoRefresh())
        updateChart();
    }

    /**
     * Updates the chart.
     */
    public void updateChart() {
      JFreeChart chart;
      if (optionsPanel.cumulativeCheck.isSelected() && optionsPanel.timeDomainCheck.isSelected()) {
        chart = buildTimeSeriesChart();
      } else if (optionsPanel.cumulativeCheck.isSelected()) {
        chart = buildLineChart();
      } else {
        chart = buildBarChart();
      }
      setChart(chart);
    }

    /**
     * Builds the time series chart.
     * 
     * @return the chart
     */
    private JFreeChart buildTimeSeriesChart() {
      TimeSeriesCollection dataset = new TimeSeriesCollection();
      JFreeChart chart = ChartFactory.createTimeSeriesChart(null, // title
          null, // x-axis label
          "Mass (kg)", // y-axis label
          dataset, // data
          optionsPanel.legendCheck.isSelected(), // legend
          true, //
          false); //
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

      TimeSeries rawCapacitySeries = new TimeSeries("Raw Capacity", Hour.class);
      dataset.addSeries(rawCapacitySeries);
      r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLUE);
      r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1, -1, 2, 2));
      TimeSeries remainingCapacitySeries = new TimeSeries("Remaining Capacity", Hour.class);
      dataset.addSeries(remainingCapacitySeries);
      r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GREEN);
      r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1, -1, 2, 2));
      TimeSeries estimatedDemandsSeries = new TimeSeries("Estimated Demands", Hour.class);
      dataset.addSeries(estimatedDemandsSeries);
      r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
      r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(-1, -1, 2, 2));

      double rawCapacity = 0;
      double remainingCapacity = 0;
      double estimatedDemands = 0;
      SupplyEdge prevEdge = null;

      for (SupplyEdge edge : optionsPanel.supplyEdgesModel.getAllObjects()) {
        Hour hour = new Hour(
            DateFunctions.getDate(demandsTab.getScenario().getStartDate(), edge.getStartTime()));

        if (optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge) && prevEdge != null) {
          if (optionsPanel.dataModel.getSelectedObjects().contains(ESTIMATED_DEMANDS)
              && !optionsPanel.smoothDemandsCheck.isSelected())
            estimatedDemandsSeries.addOrUpdate(hour.previous(), estimatedDemands);
          if (optionsPanel.dataModel.getSelectedObjects().contains(RAW_CAPACITY))
            rawCapacitySeries.addOrUpdate(hour.previous(), rawCapacity);
          if (optionsPanel.dataModel.getSelectedObjects().contains(REMAINING_CAPACITY))
            remainingCapacitySeries.addOrUpdate(hour.previous(), remainingCapacity);
        }

        estimatedDemands +=
            demandsTab.getSimulator().getAggregatedEdgeDemands().get(edge).getTotalMass();
        estimatedDemands += demandsTab.getSimulator().getAggregatedNodeDemands()
            .get(edge.getPoint()).getTotalMass();

        if (optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge)) {
          rawCapacity += edge.getMaxCargoMass();
          remainingCapacity += edge.getNetCargoMass();

          if (optionsPanel.dataModel.getSelectedObjects().contains(ESTIMATED_DEMANDS))
            estimatedDemandsSeries.addOrUpdate(hour, estimatedDemands);
          if (optionsPanel.dataModel.getSelectedObjects().contains(RAW_CAPACITY))
            rawCapacitySeries.addOrUpdate(hour, rawCapacity);
          if (optionsPanel.dataModel.getSelectedObjects().contains(REMAINING_CAPACITY))
            remainingCapacitySeries.addOrUpdate(hour, remainingCapacity);

          prevEdge = edge;
        }
      }
      return chart;
    }

    /**
     * Builds the line chart.
     * 
     * @return the chart
     */
    private JFreeChart buildLineChart() {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      JFreeChart chart = ChartFactory.createLineChart(null, // title
          "", // x-axis label
          "Mass (kg)", // y-axis label
          dataset, // data
          PlotOrientation.VERTICAL, // chart orientation
          optionsPanel.legendCheck.isSelected(), // legend
          true, //
          false); //

      CategoryItemRenderer r = chart.getCategoryPlot().getRenderer();

      double rawCapacity = 0;
      double remainingCapacity = 0;
      double estimatedDemands = 0;

      for (SupplyEdge edge : optionsPanel.supplyEdgesModel.getAllObjects()) {
        estimatedDemands +=
            demandsTab.getSimulator().getAggregatedEdgeDemands().get(edge).getTotalMass();
        estimatedDemands += demandsTab.getSimulator().getAggregatedNodeDemands()
            .get(edge.getPoint()).getTotalMass();

        if (optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge)) {
          rawCapacity += edge.getMaxCargoMass();
          remainingCapacity += edge.getNetCargoMass();

          if (optionsPanel.dataModel.getSelectedObjects().contains(ESTIMATED_DEMANDS)) {
            dataset.addValue(estimatedDemands, ESTIMATED_DEMANDS, edge);
            r.setSeriesPaint(dataset.getRowCount() - 1, Color.RED);
          }
          if (optionsPanel.dataModel.getSelectedObjects().contains(RAW_CAPACITY)) {
            dataset.addValue(rawCapacity, RAW_CAPACITY, edge);
            r.setSeriesPaint(dataset.getRowCount() - 1, Color.BLUE);
          }
          if (optionsPanel.dataModel.getSelectedObjects().contains(REMAINING_CAPACITY)) {
            dataset.addValue(remainingCapacity, REMAINING_CAPACITY, edge);
            r.setSeriesPaint(dataset.getRowCount() - 1, Color.GREEN);
          }
        }
      }

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setDomainGridlinePaint(Color.GRAY);
      plot.setRangeGridlinePaint(Color.GRAY);
      plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
      plot.setRangeGridlinesVisible(true);
      plot.setDomainGridlinesVisible(true);
      return chart;
    }

    /**
     * Builds the bar chart.
     * 
     * @return the chart
     */
    private JFreeChart buildBarChart() {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      JFreeChart chart = ChartFactory.createBarChart(null, // title
          "", // x-axis label
          "Mass (kg)", // y-axis label
          dataset, // data
          PlotOrientation.VERTICAL, // chart orientation
          optionsPanel.legendCheck.isSelected(), // legend
          true, //
          false); //

      BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
      r.setItemMargin(0);

      for (SupplyEdge edge : optionsPanel.supplyEdgesModel.getSelectedObjects()) {
        if (optionsPanel.dataModel.getSelectedObjects().contains(ESTIMATED_DEMANDS)
            && optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge)) {
          dataset.addValue(
              demandsTab.getSimulator().getAggregatedEdgeDemands().get(edge).getTotalMass()
                  + demandsTab.getSimulator().getAggregatedNodeDemands().get(edge.getPoint())
                      .getTotalMass(),
              ESTIMATED_DEMANDS, edge);
          r.setSeriesPaint(dataset.getRowCount() - 1, Color.RED);
        }
        if (optionsPanel.dataModel.getSelectedObjects().contains(RAW_CAPACITY)
            && optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge)) {
          dataset.addValue(edge.getMaxCargoMass(), RAW_CAPACITY, edge);
          r.setSeriesPaint(dataset.getRowCount() - 1, Color.BLUE);
        }
        if (optionsPanel.dataModel.getSelectedObjects().contains(REMAINING_CAPACITY)
            && optionsPanel.supplyEdgesModel.getSelectedObjects().contains(edge)) {
          dataset.addValue(edge.getNetCargoMass(), REMAINING_CAPACITY, edge);
          r.setSeriesPaint(dataset.getRowCount() - 1, Color.GREEN);
        }
      }

      CategoryPlot plot = (CategoryPlot) chart.getPlot();
      plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
      plot.setBackgroundPaint(Color.WHITE);
      plot.setDomainGridlinePaint(Color.GRAY);
      plot.setRangeGridlinePaint(Color.GRAY);
      plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
      plot.setRangeGridlinesVisible(true);
      plot.setDomainGridlinesVisible(true);
      return chart;
    }
  }
}
