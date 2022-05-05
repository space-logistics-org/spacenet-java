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
import java.awt.Component;
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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.CheckBoxTableModel;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.gui.renderer.VisibilityTableCellHeaderRenderer;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.SimScavenge;
import edu.mit.spacenet.util.DateFunctions;

/**
 * Component for commonality analysis.
 * 
 * @author Paul Grogan
 */
public class CommonalityTab extends JSplitPane {
  private static final long serialVersionUID = -6405124067606236705L;
  private static final String CUMULATIVE_MASS = "Total Cumulative Mass";
  private static final String CUMULATIVE_UNITS = "Cumulative Units";
  private static final String MASS_BY_SOURCE = "Mass by Source";
  private DemandsTab demandsTab;

  private CommonalityTable commonalityTable;
  private ChartPanel chartPanel;
  private OptionsPanel optionsPanel;

  /**
   * The constructor.
   * 
   * @param tab the demands tab component
   */
  public CommonalityTab(DemandsTab tab) {
    super(JSplitPane.VERTICAL_SPLIT);
    this.demandsTab = tab;

    buildPanel();

    setName("Commonality Analysis");
    setDividerSize(0);
    setBorder(BorderFactory.createEmptyBorder());
    setResizeWeight(1);
    setDividerLocation(100);
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    commonalityTable = new CommonalityTable(this);
    JScrollPane commonalityScroll = new JScrollPane(commonalityTable);
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(400, 100));
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    topPanel.setBorder(BorderFactory.createTitledBorder("Commonality Matrix (% of Mass Common)"));
    topPanel.add(commonalityScroll);
    setTopComponent(topPanel);

    JSplitPane bottomPanel = new JSplitPane();
    bottomPanel.setBorder(BorderFactory.createTitledBorder("Scavenging History"));
    chartPanel = new ChartPanel();
    bottomPanel.setLeftComponent(chartPanel);
    optionsPanel = new OptionsPanel();
    bottomPanel.setRightComponent(optionsPanel);
    bottomPanel.setOneTouchExpandable(true);
    bottomPanel.setDividerSize(10);
    bottomPanel.setResizeWeight(1);
    bottomPanel.setDividerLocation(490);
    setBottomComponent(bottomPanel);
  }

  /**
   * Initializes the panel.
   */
  public void initialize() {
    commonalityTable.initialize();
    optionsPanel.initialize();
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
   * Checks if auto refresh is enabled.
   * 
   * @return true, if auto refresh is enabled
   */
  private boolean isAutoRefresh() {
    return SpaceNetSettings.getInstance().isAutoRefresh();
  }

  /**
   * Requests that the component and associated charts update based on the underlying model.
   */
  public void updateView() {
    // TODO commonalityTable.updateView();
    optionsPanel.updateView();
    chartPanel.updateView();
  }

  /**
   * The Class OptionsPanel.
   */
  private class OptionsPanel extends JPanel {
    private static final long serialVersionUID = -4853327538110812961L;

    private JComboBox<String> optionCombo;
    private CheckBoxTableModel<I_Item> partsModel;
    private CheckBoxTableModel<I_Element> elementsModel;
    private JButton refreshButton;

    /**
     * Instantiates a new options panel.
     */
    public OptionsPanel() {
      setLayout(new GridBagLayout());
      setBorder(BorderFactory.createTitledBorder("Chart Options"));
      buildPanel();
    }

    /**
     * Builds the panel.
     */
    private void buildPanel() {
      GridBagConstraints c = new GridBagConstraints();
      c.insets = new Insets(2, 2, 2, 2);
      c.gridx = 0;
      c.gridy = 0;
      c.anchor = GridBagConstraints.LINE_END;
      add(new JLabel("Display: "), c);
      c.gridx++;
      c.anchor = GridBagConstraints.LINE_START;
      c.fill = GridBagConstraints.HORIZONTAL;
      optionCombo = new JComboBox<String>();
      optionCombo.addItem(CUMULATIVE_MASS);
      optionCombo.addItem(CUMULATIVE_UNITS);
      optionCombo.addItem(MASS_BY_SOURCE);
      optionCombo.setSelectedItem(CUMULATIVE_MASS);
      optionCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == ItemEvent.SELECTED)
            chartPanel.updateView();
        }
      });
      add(optionCombo, c);
      c.gridy++;
      JPanel prop0 = new JPanel();
      prop0.setPreferredSize(new Dimension(1, 15));
      add(prop0, c);
      c.gridx = 0;
      c.gridy++;
      c.gridwidth = 2;
      c.weightx = 1;
      c.weighty = 1;
      c.fill = GridBagConstraints.BOTH;
      elementsModel = new CheckBoxTableModel<I_Element>();
      elementsModel.addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
          if (e.getType() == TableModelEvent.UPDATE)
            chartPanel.updateView();
        }
      });
      JTable elementsTable = new JTable(elementsModel);
      elementsTable.getColumnModel().getColumn(0)
          .setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
      elementsTable.getTableHeader().setReorderingAllowed(false);
      elementsTable.getColumnModel().getColumn(0).setMaxWidth(25);
      elementsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Source Elements");
      elementsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 3205543512740776042L;

        // custom renderer to show the element icons and id's
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          if (value instanceof I_Element) {
            setIcon(((I_Element) value).getIcon());
            setText(((I_Element) value).getName() + " [" + ((I_Element) value).getUid() + "]");
          }
          return this;
        }
      });
      elementsTable.setShowGrid(false);
      JScrollPane elementsScroll = new JScrollPane(elementsTable);
      elementsScroll.setPreferredSize(new Dimension(150, 150));
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
      prop2.setPreferredSize(new Dimension(1, 15));
      add(prop2, c);
      c.gridy++;
      c.weighty = 1;
      partsModel = new CheckBoxTableModel<I_Item>();
      partsModel.addTableModelListener(new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
          if (e.getType() == TableModelEvent.UPDATE)
            chartPanel.updateView();
        }
      });
      JTable partsTable = new JTable(partsModel);
      partsTable.getColumnModel().getColumn(0)
          .setHeaderRenderer(new VisibilityTableCellHeaderRenderer());
      partsTable.getTableHeader().setReorderingAllowed(false);
      partsTable.getColumnModel().getColumn(0).setMaxWidth(25);
      partsTable.getColumnModel().getColumn(1).setHeaderValue("Filter Parts");
      partsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
        private static final long serialVersionUID = 3205543512740776042L;

        // custom renderer to show the part icons and id's
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          if (value instanceof I_Item) {
            setIcon(((I_Item) value).getResourceType().getIcon());
            setText(((I_Item) value).getName() + " [" + ((I_Item) value).getTid() + "]");
          }
          return this;
        }
      });
      partsTable.setShowGrid(false);
      JScrollPane partsScroll = new JScrollPane(partsTable);
      partsScroll.setPreferredSize(new Dimension(150, 200));
      add(partsScroll, c);
      c.gridy++;
      c.weighty = 0;
      c.fill = GridBagConstraints.NONE;
      JPanel partsButtonPanel = new JPanel();
      partsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
      JButton selectAllPartsButton = new JButton("Select All");
      selectAllPartsButton.setToolTipText("Select All Parts");
      selectAllPartsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          partsModel.selectAll();
        }
      });
      partsButtonPanel.add(selectAllPartsButton);
      JButton deselectAllPartsButton = new JButton("Deselect All");
      deselectAllPartsButton.setToolTipText("Deselect All Parts");
      deselectAllPartsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          partsModel.deselectAll();
        }
      });
      partsButtonPanel.add(deselectAllPartsButton);
      add(partsButtonPanel, c);
      c.gridy++;
      JPanel prop3 = new JPanel();
      prop3.setPreferredSize(new Dimension(1, 15));
      add(prop3, c);
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
     * Initialize.
     */
    public void initialize() {
      elementsModel.clear();
      updateView();
    }

    /**
     * Update view.
     */
    public void updateView() {
      SortedSet<I_Element> scavengedElements = new TreeSet<I_Element>();
      SortedSet<I_Item> scavengedItems = new TreeSet<I_Item>();
      for (SimScavenge scavenge : demandsTab.getSimulator().getScavengedParts()) {
        scavengedElements.add(scavenge.getElement());
        scavengedItems.add(scavenge.getItem());
      }
      // update scavenged elements
      List<I_Element> deselectedElements = elementsModel.getDeselectedObjects();
      TableModelListener[] modelListeners = elementsModel.getTableModelListeners();
      for (TableModelListener l : modelListeners) {
        elementsModel.removeTableModelListener(l);
      }
      elementsModel.clear();
      if (demandsTab.getSimulator() != null) {
        for (I_Element element : scavengedElements) {
          elementsModel.addObject(element, !deselectedElements.contains(element));
        }
      }
      for (TableModelListener l : modelListeners) {
        elementsModel.addTableModelListener(l);
      }
      elementsModel.fireTableDataChanged();

      // update scavenged parts
      List<I_Item> deselectedParts = partsModel.getDeselectedObjects();
      modelListeners = partsModel.getTableModelListeners();
      for (TableModelListener l : modelListeners) {
        partsModel.removeTableModelListener(l);
      }
      partsModel.clear();
      if (demandsTab.getSimulator() != null) {
        for (I_Item item : scavengedItems) {
          partsModel.addObject(item, !deselectedParts.contains(item));
        }
      }
      for (TableModelListener l : modelListeners) {
        partsModel.addTableModelListener(l);
      }
      partsModel.fireTableDataChanged();

      // update auto refresh button visibility
      if (isAutoRefresh()) {
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
    private static final long serialVersionUID = -4805521416303511037L;

    /**
     * Instantiates a new chart panel.
     */
    public ChartPanel() {
      setPreferredSize(new Dimension(600, 400));
    }

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
      if (isAutoRefresh())
        updateChart();
    }

    /**
     * Update chart.
     */
    public void updateChart() {
      TimeSeriesCollection dataset = new TimeSeriesCollection();

      JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null,
          optionsPanel.optionCombo.getSelectedItem().equals(CUMULATIVE_UNITS) ? "Units"
              : "Mass (kg)",
          dataset, !optionsPanel.optionCombo.getSelectedItem().equals(CUMULATIVE_MASS), // legend
          true, false);
      XYPlot plot = (XYPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.WHITE);
      plot.setDomainGridlinePaint(Color.GRAY);
      plot.setRangeGridlinePaint(Color.GRAY);
      plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
      plot.setRangeGridlinesVisible(true);
      plot.setDomainGridlinesVisible(true);

      XYItemRenderer r = chart.getXYPlot().getRenderer();
      if (optionsPanel.optionCombo.getSelectedItem().equals(CUMULATIVE_UNITS)) {
        for (I_Item item : optionsPanel.partsModel.getSelectedObjects()) {
          TimeSeries s = new TimeSeries(item.getName() + " [" + item.getTid() + "]", Hour.class);
          double amount = 0;
          for (SimScavenge scavenge : demandsTab.getSimulator().getScavengedParts()) {
            if (optionsPanel.elementsModel.getSelectedObjects().contains(scavenge.getElement())
                && scavenge.getItem().equals(item)) {
              s.addOrUpdate(
                  new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime()))
                      .previous(),
                  amount);
              amount += scavenge.getAmount();
              s.addOrUpdate(
                  new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime())),
                  amount);
            }
          }
          dataset.addSeries(s);
          r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(0, 0, 0, 0));
        }
      } else if (optionsPanel.optionCombo.getSelectedItem().equals(CUMULATIVE_MASS)) {
        TimeSeries s = new TimeSeries("Total", Hour.class);
        double amount = 0;
        for (SimScavenge scavenge : demandsTab.getSimulator().getScavengedParts()) {
          if (optionsPanel.elementsModel.getSelectedObjects().contains(scavenge.getElement())
              && optionsPanel.partsModel.getSelectedObjects().contains(scavenge.getItem())) {
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime()))
                    .previous(),
                amount);
            amount += scavenge.getItem().getUnitMass() * scavenge.getAmount();
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime())),
                amount);
          }
        }
        dataset.addSeries(s);
        r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(0, 0, 0, 0));

      } else {
        for (I_Element element : optionsPanel.elementsModel.getSelectedObjects()) {
          TimeSeries s =
              new TimeSeries(element.getName() + " [" + element.getUid() + "]", Hour.class);
          double amount = 0;
          for (SimScavenge scavenge : demandsTab.getSimulator().getScavengedParts()) {
            if (scavenge.getElement().equals(element)
                && optionsPanel.partsModel.getSelectedObjects().contains(scavenge.getItem())) {
              s.addOrUpdate(
                  new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime()))
                      .previous(),
                  amount);
              amount += scavenge.getItem().getUnitMass() * scavenge.getAmount();
              s.addOrUpdate(
                  new Hour(DateFunctions.getDate(getScenario().getStartDate(), scavenge.getTime())),
                  amount);
            }
          }
          dataset.addSeries(s);
          r.setSeriesShape(dataset.getSeriesCount() - 1, new Ellipse2D.Double(0, 0, 0, 0));
        }
      }
      setChart(chart);
    }
  }
}
