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
		if(chartTabs.getSelectedComponent().equals(processBatChart)) {
			processBatChart.updateView();
		} else if(chartTabs.getSelectedComponent().equals(elementBatChart)) {
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
