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
package edu.mit.spacenet.gui.mission;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 * A panel to display the campaign missions and useful visualizations.
 */
public class MissionsSplitPane extends JSplitPane {
  private static final long serialVersionUID = -5912020298671757298L;

  private MissionsTab missionsTab;

  private MissionsPanel missionsPanel;
  private JTabbedPane chartTabs;
  private ProcessBatChart processBatChart;
  private ElementBatChart elementBatChart;

  /**
   * Instantiates a new missions split pane.
   * 
   * @param missionsTab the missions tab
   */
  public MissionsSplitPane(MissionsTab missionsTab) {
    super(JSplitPane.HORIZONTAL_SPLIT);
    this.missionsTab = missionsTab;

    missionsPanel = new MissionsPanel(this);
    setLeftComponent(missionsPanel);
    chartTabs = new JTabbedPane();
    processBatChart = new ProcessBatChart(this);
    chartTabs.add(processBatChart);
    elementBatChart = new ElementBatChart(this);
    chartTabs.add(elementBatChart);
    setRightComponent(chartTabs);
  }

  /**
   * Initializes the components for a new campaign.
   */
  public void initialize() {
    missionsPanel.initialize();
    processBatChart.initialize();
    elementBatChart.initialize();
  }

  /**
   * Updates the view.
   */
  public void updateView() {
    missionsPanel.updateView();
    if (chartTabs.getSelectedComponent().equals(processBatChart)) {
      processBatChart.updateView();
    } else if (chartTabs.getSelectedComponent().equals(elementBatChart)) {
      elementBatChart.updateView();
    }
  }

  /**
   * Gets the missions tab.
   * 
   * @return the missions tab
   */
  public MissionsTab getMissionsTab() {
    return missionsTab;
  }
}
