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
package edu.mit.spacenet.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.scenario.ItemDiscretization;

/**
 * Dialog that displays options related to a specific scenario.
 * 
 * @author Paul Grogan
 */
public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = -802497885876127342L;
	
	private ScenarioPanel scenarioPanel;
	
	private JPanel precisionPanel, constraintsPanel, demandsPanel, simulationPanel;
	private SpinnerNumberModel timeModel, demandModel, massModel, volumeModel;
	private JSpinner timeSpinner, demandSpinner, massSpinner, volumeSpinner;
	private JCheckBox volumeConstrained, environmentConstrained;
	
	private JComboBox discretizationCombo;
	private JSlider aggregationSlider;
	private JCheckBox scavengeSparesCheck;
	
	private JCheckBox explorationCheck, evaCheck;
	
	/**
	 * Instantiates a new options dialog.
	 *
	 * @param scenarioPanel the scenario panel
	 */
	public OptionsDialog(ScenarioPanel scenarioPanel) {
		super(scenarioPanel.getSpaceNetFrame(), "Scenario Options", true);
		this.scenarioPanel = scenarioPanel;
		
		buildDialog();
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		buildPrecisionPanel();
		buildConstraintsPanel();
		buildDemandsPanel();
		buildSimulationPanel();
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Precision", precisionPanel);
		tabbedPane.addTab("Constraints", constraintsPanel);
		tabbedPane.addTab("Demands", demandsPanel);
		tabbedPane.addTab("Simulation", simulationPanel);
		contentPanel.add(tabbedPane, c);
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveOptions();
				scenarioPanel.updateView();
				dispose();
			}
		});
		buttonPanel.add(okButton, c);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton, c);
		contentPanel.add(buttonPanel, c);
		
		setModal(true);
		setContentPane(contentPanel);
		setMinimumSize(new Dimension(300,250));
	}
	
	/**
	 * Builds the precision panel.
	 */
	private void buildPrecisionPanel() {
		precisionPanel = new JPanel();
		precisionPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		precisionPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		precisionPanel.add(new JLabel("Time Precision: "), c);
		c.gridy++;
		precisionPanel.add(new JLabel("Demand Precision: "), c);
		c.gridy++;
		precisionPanel.add(new JLabel("Mass Precision: "), c);
		c.gridy++;
		precisionPanel.add(new JLabel("Volume Precision: "), c);
		c.gridy++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		timeModel = new SpinnerNumberModel(0.001,0.001,1.000,0.001);
		timeSpinner = new JSpinner(timeModel);
		precisionPanel.add(new UnitsWrapper(timeSpinner, "days"), c);
		c.gridy++;
		demandModel = new SpinnerNumberModel(0.001,0.001,1.000,0.001);
		demandSpinner = new JSpinner(demandModel);
		precisionPanel.add(new UnitsWrapper(demandSpinner, "units"), c);
		c.gridy++;
		massModel = new SpinnerNumberModel(0.001,0.001,1.000,0.001);
		massSpinner = new JSpinner(massModel);
		precisionPanel.add(new UnitsWrapper(massSpinner, "kg"), c);
		c.gridy++;
		volumeModel = new SpinnerNumberModel(0.1,0.1,100.,0.1);
		volumeSpinner = new JSpinner(volumeModel);
		precisionPanel.add(new UnitsWrapper(volumeSpinner, "cm^3"), c);
		c.gridy++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		precisionPanel.add(new JLabel(), c);
	}
	
	/**
	 * Builds the constraints panel.
	 */
	private void buildConstraintsPanel() {
		constraintsPanel = new JPanel();
		constraintsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		constraintsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		volumeConstrained = new JCheckBox("Volume Constraints Enforced");
		volumeConstrained.setOpaque(false);
		constraintsPanel.add(volumeConstrained, c);
		c.gridy++;
		environmentConstrained = new JCheckBox("Environment Constraints Enforced");
		environmentConstrained.setOpaque(false);
		constraintsPanel.add(environmentConstrained, c);
		c.gridy++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		constraintsPanel.add(new JLabel(), c);
	}
	
	/**
	 * Builds the demands panel.
	 */
	private void buildDemandsPanel() {
		demandsPanel = new JPanel();
		demandsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		demandsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		demandsPanel.add(new JLabel("Item Discretization: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		demandsPanel.add(new JLabel("Item Aggregation: "), c);
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		discretizationCombo = new JComboBox();
		discretizationCombo.setToolTipText("Discretize demands for items to integer values at the selected level");
		for(ItemDiscretization t : ItemDiscretization.values()) {
			discretizationCombo.addItem(t);
		}
		discretizationCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					aggregationSlider.setEnabled(discretizationCombo.getSelectedItem()!=ItemDiscretization.NONE);
				}
			}
		});
		demandsPanel.add(discretizationCombo, c);
		c.gridy++;
		aggregationSlider = new JSlider(JSlider.VERTICAL,0,4,0);
		aggregationSlider.setOpaque(false);
		aggregationSlider.setFocusable(false);
		aggregationSlider.setToolTipText("Aggregate discretized item demands ahead or behind demands.");
		Hashtable<Integer, JLabel> labelTable  = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("First Demand", JLabel.LEFT));
		labelTable.put(new Integer(2), new JLabel("Half Demand", JLabel.LEFT));
		labelTable.put(new Integer(4), new JLabel("Unit Demand", JLabel.RIGHT));
		aggregationSlider.setLabelTable(labelTable);
		aggregationSlider.setPaintLabels(true);
		aggregationSlider.setMajorTickSpacing(1);
		aggregationSlider.setSnapToTicks(true);
		aggregationSlider.setPaintTicks(true);
		aggregationSlider.setPreferredSize(new Dimension(100,100));
		demandsPanel.add(aggregationSlider, c);
		c.gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		scavengeSparesCheck = new JCheckBox("Scavenge Spares");
		scavengeSparesCheck.setOpaque(false);
		demandsPanel.add(scavengeSparesCheck, c);
		c.gridy++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		demandsPanel.add(new JLabel(), c);
	}
	
	/**
	 * Builds the simulation panel.
	 */
	private void buildSimulationPanel() {
		simulationPanel = new JPanel();
		simulationPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		simulationPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		explorationCheck = new JCheckBox("Detailed Explorations");
		explorationCheck.setOpaque(false);
		simulationPanel.add(explorationCheck, c);
		c.gridy++;
		evaCheck = new JCheckBox("Detailed EVAs");
		evaCheck.setOpaque(false);
		simulationPanel.add(evaCheck, c);
		c.gridy++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		simulationPanel.add(new JLabel(), c);
	}
	
	/**
	 * Saves the options.
	 */
	private void saveOptions() {
		scenarioPanel.updateView();
		scenarioPanel.getScenario().setTimePrecision(timeModel.getNumber().doubleValue());
		scenarioPanel.getScenario().setDemandPrecision(demandModel.getNumber().doubleValue());
		scenarioPanel.getScenario().setMassPrecision(massModel.getNumber().doubleValue());
		scenarioPanel.getScenario().setVolumePrecision(volumeModel.getNumber().doubleValue()/1000000D);
		scenarioPanel.getScenario().setVolumeConstrained(volumeConstrained.isSelected());
		scenarioPanel.getScenario().setEnvironmentConstrained(environmentConstrained.isSelected());
		scenarioPanel.getScenario().setItemDiscretization((ItemDiscretization)discretizationCombo.getSelectedItem());
		scenarioPanel.getScenario().setItemAggregation(aggregationSlider.getValue()/4D);
		scenarioPanel.getScenario().setScavengeSpares(scavengeSparesCheck.isSelected());
		scenarioPanel.getScenario().setDetailedEva(evaCheck.isSelected());
		scenarioPanel.getScenario().setDetailedExploration(explorationCheck.isSelected());
	}
	
	/**
	 * Initializes the dialog with the options data.
	 */
	private void initialize() {
		timeModel.setValue(scenarioPanel.getScenario().getTimePrecision());
		demandModel.setValue(scenarioPanel.getScenario().getDemandPrecision());
		massModel.setValue(scenarioPanel.getScenario().getMassPrecision());
		volumeModel.setValue(scenarioPanel.getScenario().getVolumePrecision()*1000000);
		
		volumeConstrained.setSelected(scenarioPanel.getScenario().isVolumeConstrained());
		environmentConstrained.setSelected(scenarioPanel.getScenario().isEnvironmentConstrained());
		
		discretizationCombo.setSelectedItem(scenarioPanel.getScenario().getItemDiscretization());
		aggregationSlider.setEnabled(discretizationCombo.getSelectedItem()!=ItemDiscretization.NONE);
		aggregationSlider.setValue((int)(4*scenarioPanel.getScenario().getItemAggregation()));
		scavengeSparesCheck.setSelected(scenarioPanel.getScenario().isScavengeSpares());

		explorationCheck.setSelected(scenarioPanel.getScenario().isDetailedExploration());
		evaCheck.setSelected(scenarioPanel.getScenario().isDetailedEva());
	}
	
	/**
	 * Initializes the view components, re-positions the dialog to the center
	 * of the frame, and enabled visibility.
	 */
	public void showDialog() {
		initialize();
		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
