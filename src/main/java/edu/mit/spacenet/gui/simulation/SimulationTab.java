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
package edu.mit.spacenet.gui.simulation;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;

import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.FullSimulator;
import edu.mit.spacenet.simulator.SimError;

/**
 * Tab component for running the full simulation, analyzing errors, and 
 * visualizing the outputs.
 * 
 * @author Paul Grogan
 */
public class SimulationTab extends JSplitPane {
	private static final long serialVersionUID = 6299975123289250302L;
	
	private JPanel controlPanel;
	private JCheckBox detailedExplorationsCheck, detailedEvasCheck;
	private JButton simulateButton;
	private DefaultListModel errorsModel;
	private JList errorsList;
	private JTabbedPane tabs;
	
	private NetworkHistoryVisualization networkVisualization;
	private MoeHistoryChart moeChart;
	private LocationCosHistoryChart locationChart;
	private ElementCosHistoryChart elementChart;
	
	private FullSimulator simulator;
	
	private ScenarioPanel scenarioPanel;
	private boolean isSimulating = false;
	
	/**
	 * Instantiates a new simulation tab.
	 * 
	 * @param scenarioPanel the scenario panel
	 */
	public SimulationTab(ScenarioPanel scenarioPanel) {
		this.scenarioPanel = scenarioPanel;
		
		controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		detailedExplorationsCheck = new JCheckBox("Detailed Explorations");
		detailedExplorationsCheck.setToolTipText("Detailed explorations are not necessary if using mission-level demand models to capture EVA demands.");
		detailedExplorationsCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				getScenario().setDetailedExploration(detailedExplorationsCheck.isSelected());
			}
		});
		controlPanel.add(detailedExplorationsCheck, c);
		c.gridy++;
		detailedEvasCheck = new JCheckBox("Detailed EVAs");
		detailedEvasCheck.setToolTipText("Detailed EVAs are not necessary if using mission-level demand models to capture EVA demands.");
		detailedEvasCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				getScenario().setDetailedEva(detailedEvasCheck.isSelected());
			}
		});
		controlPanel.add(detailedEvasCheck, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		simulateButton = new JButton("Simulate", new ImageIcon(getClass().getClassLoader().getResource("icons/lightning_go.png")));
		simulateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isSimulating) return;
				isSimulating = true;
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					public Void doInBackground() {
						SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Running Simulation...");
						getScenarioPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						try {
							simulator.simulate();
							if(simulator.getErrors().size()>0) {
								JOptionPane.showMessageDialog(tabs, 
										"The simulation completed with " + simulator.getErrors().size() 
										+ " error" + (simulator.getErrors().size()==1?"":"s") + ".", 
										"SpaceNet Simulation Errors", 
										JOptionPane.WARNING_MESSAGE);
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
						return null;
					}
					public void done() {
						updateView();
						SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
						getScenarioPanel().setCursor(Cursor.getDefaultCursor());
						isSimulating = false;
					}
				};
				worker.execute();
			}
		});
		controlPanel.add(simulateButton, c);
		c.gridy++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		controlPanel.add(new JLabel("Simulation Errors: "), c);
		c.gridy++;
		c.weighty = .5;
		c.fill = GridBagConstraints.BOTH;
		errorsModel = new DefaultListModel();
		errorsList = new JList(errorsModel);
		errorsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 635852745823206059L;
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)  {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof SimError) {
					SimError error = (SimError)value;
					DecimalFormat format = new DecimalFormat("0.00");
					setText(format.format(error.getTime()) + ": " + error.getMessage());
					setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bullet_error.png")));
				}
				return this;
			}
		});
		JScrollPane errorsScroll = new JScrollPane(errorsList);
		errorsScroll.setPreferredSize(new Dimension(50,50));
		controlPanel.add(errorsScroll, c);
		
		c.gridy++;
		c.weighty = .5;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;

		setLeftComponent(new JScrollPane(controlPanel));
		
		tabs = new JTabbedPane();
		networkVisualization = new NetworkHistoryVisualization(this);
		tabs.add(networkVisualization);
		moeChart = new MoeHistoryChart(this);
		tabs.add(moeChart);
		locationChart = new LocationCosHistoryChart(this);
		tabs.add(locationChart);
		elementChart = new ElementCosHistoryChart(this);
		tabs.add(elementChart);
		
		setRightComponent(tabs);
		
		setResizeWeight(.1);
		setDividerLocation(150);
		setOneTouchExpandable(true);
		setDividerSize(10);
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	/**
	 * Initialize.
	 */
	public void initialize() {
		simulator = new FullSimulator(getScenario());
		errorsModel.clear();
		detailedExplorationsCheck.setSelected(getScenario().isDetailedExploration());
		detailedEvasCheck.setSelected(getScenario().isDetailedEva());
		networkVisualization.initialize();
		moeChart.initialize();
		locationChart.initialize();
		elementChart.initialize();
	}
	
	/**
	 * Gets the scenario panel.
	 * 
	 * @return the scenario panel
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}
	
	/**
	 * Gets the this.
	 * 
	 * @return the this
	 */
	public SimulationTab getThis() {
		return this;
	}
	
	/**
	 * Gets the scenario.
	 * 
	 * @return the scenario
	 */
	public Scenario getScenario() {
		return scenarioPanel.getScenario();
	}
	
	/**
	 * Gets the simulator.
	 * 
	 * @return the simulator
	 */
	public FullSimulator getSimulator() {
		return simulator;
	}
	
	/**
	 * Update view.
	 */
	public void updateView() {
		errorsModel.clear();
		detailedExplorationsCheck.setSelected(getScenario().isDetailedExploration());
		detailedEvasCheck.setSelected(getScenario().isDetailedEva());
		for(SimError error : simulator.getErrors()) {
			errorsModel.addElement(error);
		}
		networkVisualization.updateView();
		moeChart.updateView();
		locationChart.updateView();
		elementChart.updateView();
	}
}
