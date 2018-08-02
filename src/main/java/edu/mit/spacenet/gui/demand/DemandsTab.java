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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.scenario.ItemDiscretization;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.scenario.SupplyEdge.SupplyPoint;
import edu.mit.spacenet.simulator.DemandSimulator;
import edu.mit.spacenet.simulator.SimDemand;

/**
 * Component for viewing and editing the demands associated with a scenario.
 * 
 * @author Paul Grogan
 */
public class DemandsTab extends JSplitPane {
	private static final long serialVersionUID = 1065899429343403823L;
	private static String RAW_DEMANDS = "Raw Demands";
	private static String AGGREGATED_DEMANDS = "Aggregated Demands";
	private static String ID_OUTPUT = "Object IDs";
	private static String NAME_OUTPUT = "Object Names";
	private static String COMMA_DELIMITED = "Comma Delimited";
	private static String SEMICOLON_DELIMITED = "Semicolon Delimited";
	private static String TAB_DELIMITED = "Tab Delimited";

	private JPanel controlPanel;
	private JComboBox discretizationCombo;
	private JSlider aggregationSlider;
	private JCheckBox scavengeSparesCheck, packingDemandsCheck, demandsSatisfiedCheck;
	
	private JTextField directoryPathText, fileNameText;
	private JCheckBox overwriteCheck;
	private JButton browseButton, exportButton;
	private JComboBox demandsCombo, referenceCombo, delimiterCombo;
	private JFileChooser directoryChooser;
	private ExportWorker exportWorker;
	
	private JTabbedPane tabbedPane;

	private ScenarioFeasibilityTab feasibilityTab;
	private SupplyNetworkTab networkTab;
	private ElementDemandsTab elementDemandsTab;
	private MissionDemandsTab missionDemandTab;
	private LocationDemandsTab locationDemandTab;
	private CommonalityTab commonalityTab;
	private RepairabilityTab repairabilityTab;
	
	private ScenarioPanel scenarioPanel;
	private DemandSimulator simulator;
	private SimWorker simWorker;
	
	/**
	 * The constructor.
	 * 
	 * @param scenarioPanel the scenario panel
	 */
	public DemandsTab(ScenarioPanel scenarioPanel) {
		this.scenarioPanel = scenarioPanel;
		
		controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		controlPanel.add(buildOptionsPanel(), c);
		c.gridy++;
		controlPanel.add(buildDisplayPanel(), c);
		c.gridy++;
		controlPanel.add(buildExportPanel(), c);
		c.gridy++;
		c.weighty = 1;
		controlPanel.add(new JPanel(), c);
		
		setLeftComponent(new JScrollPane(controlPanel));
		
		tabbedPane = new JTabbedPane();
		networkTab = new SupplyNetworkTab(this);
		tabbedPane.add(networkTab);
		feasibilityTab = new ScenarioFeasibilityTab(this);
		tabbedPane.add(feasibilityTab);
		elementDemandsTab = new ElementDemandsTab(this);
		tabbedPane.add(elementDemandsTab);
		missionDemandTab = new MissionDemandsTab(this);
		tabbedPane.add(missionDemandTab);
		locationDemandTab = new LocationDemandsTab(this);
		tabbedPane.add(locationDemandTab);
		commonalityTab = new CommonalityTab(this);
		tabbedPane.add(commonalityTab);
		repairabilityTab = new RepairabilityTab(this);
		tabbedPane.add(repairabilityTab);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateTab();
			}
		});
		setRightComponent(tabbedPane);
		
		setResizeWeight(.1);
		setDividerLocation(250);
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	/**
	 * Initializes the demands tab.
	 */
	public void initialize() {
		discretizationCombo.setEnabled(getScenario()!=null);
		aggregationSlider.setEnabled(getScenario()!=null);
		scavengeSparesCheck.setEnabled(getScenario()!=null);
		packingDemandsCheck.setEnabled(getScenario()!=null);
		
		browseButton.setEnabled(getScenario()!=null);
		fileNameText.setEnabled(getScenario()!=null);
		demandsCombo.setEnabled(getScenario()!=null);
		referenceCombo.setEnabled(getScenario()!=null);
		delimiterCombo.setEnabled(getScenario()!=null);
		
		if(getScenario()!=null) {
			discretizationCombo.setSelectedItem(getScenario().getItemDiscretization());
			aggregationSlider.setValue((int)(getScenario().getItemAggregation()*4));
			aggregationSlider.setEnabled(getScenario().getItemDiscretization()!=ItemDiscretization.NONE);
			scavengeSparesCheck.setSelected(getScenario().isScavengeSpares());
			
			fileNameText.setText("demands.txt");
			
			//TODO: this is a hack, simulator should be initialized, not re-created
			simulator = new DemandSimulator(getScenario());
			simulator.setDemandsSatisfied(false);
			packingDemandsCheck.setSelected(simulator.isPackingDemandsAdded());
			demandsSatisfiedCheck.setSelected(simulator.isDemandsSatisfied());
			simWorker = new SimWorker(true);
			simWorker.execute();
		}
	}
	
	/**
	 * Builds the options panel, used for accessing scenario options.
	 * 
	 * @return the options panel
	 */
	private JPanel buildOptionsPanel() {
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Demand Options"));
		optionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.weightx = 0;
		optionsPanel.add(new JLabel("Item Discretization by: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		optionsPanel.add(new JLabel("Item Aggregation at: "), c);
		c.gridy = 0;
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		discretizationCombo = new JComboBox();
		discretizationCombo.setToolTipText("Discretize demands for items to integer values at the selected level");
		for(ItemDiscretization t : ItemDiscretization.values()) {
			discretizationCombo.addItem(t);
		}
		discretizationCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(!discretizationCombo.getSelectedItem().equals(getScenario().getItemDiscretization())) {
					getScenario().setItemDiscretization((ItemDiscretization)discretizationCombo.getSelectedItem());
					aggregationSlider.setEnabled(getScenario().getItemDiscretization()!=ItemDiscretization.NONE);
					updateView();
				}
			}
		});
		optionsPanel.add(discretizationCombo, c);
		c.gridy++;
		aggregationSlider = new JSlider(JSlider.VERTICAL, 0,4,1);
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
		aggregationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(!aggregationSlider.getValueIsAdjusting()
						&& aggregationSlider.getValue()/4d!= getScenario().getItemAggregation()) {
					getScenario().setItemAggregation(aggregationSlider.getValue()/4d);
					updateView();
				}
			}
		});
		aggregationSlider.setPreferredSize(new Dimension(50,100));
		optionsPanel.add(aggregationSlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		scavengeSparesCheck = new JCheckBox("Scavenge Spares");
		scavengeSparesCheck.setToolTipText("Scavenge spare parts from co-located decommissioned elements");
		scavengeSparesCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(scavengeSparesCheck.isSelected()!=getScenario().isScavengeSpares()) {
					getScenario().setScavengeSpares(scavengeSparesCheck.isSelected());
					updateView();
				}
			}
		});
		optionsPanel.add(scavengeSparesCheck, c);
		return optionsPanel;
	}
	
	/**
	 * Builds the display panel, used for controlling the display options.
	 * 
	 * @return the display panel
	 */
	private JPanel buildDisplayPanel() {
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.PAGE_AXIS));
		displayPanel.setBorder(BorderFactory.createTitledBorder("Display Options"));
		packingDemandsCheck = new JCheckBox("Estimate Logistics Container Masses");
		packingDemandsCheck.setToolTipText("Estimate mass (Generic COS 5) for containers using resource packing factors.");
		packingDemandsCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(packingDemandsCheck.isSelected()!=simulator.isPackingDemandsAdded()) {
					simulator.setPackingDemandsAdded(e.getStateChange()==ItemEvent.SELECTED);
					updateView();
				}
			}
		});
		displayPanel.add(packingDemandsCheck);
		demandsSatisfiedCheck = new JCheckBox("Consume Existing Resources");
		demandsSatisfiedCheck.setToolTipText("Consume existing resources when demanded.");
		demandsSatisfiedCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(demandsSatisfiedCheck.isSelected()!=simulator.isDemandsSatisfied()) {
					simulator.setDemandsSatisfied(e.getStateChange()==ItemEvent.SELECTED);
					updateView();
				}
			}
		});
		displayPanel.add(demandsSatisfiedCheck);
		return displayPanel;
	}
	
	private JPanel buildExportPanel() {
		directoryChooser = new JFileChooser();
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JPanel exportPanel = new JPanel();
		exportPanel.setBorder(BorderFactory.createTitledBorder("Export Demands"));
		exportPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		exportPanel.add(new JLabel("Directory: "), c);
		c.gridy++;
		exportPanel.add(new JLabel("File Name: "), c);
		c.gridy+=2;
		exportPanel.add(new JLabel("Demands: "), c);
		c.gridy++;
		exportPanel.add(new JLabel("Output: "), c);
		c.gridy++;
		exportPanel.add(new JLabel("Delimiter: "), c);
		c.gridy = 0;
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		directoryPathText = new JTextField();
		directoryPathText.setEnabled(false);
		exportPanel.add(directoryPathText, c);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridx++;
		c.weightx = 0;
		browseButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/folder_explore.png")));
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(directoryPathText.getText()=="") {
					directoryChooser.setCurrentDirectory(
							new File(SpaceNetSettings.getInstance().getDefaultDirectory()));
				}
				int returnVal = directoryChooser.showOpenDialog(getThis());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					directoryPathText.setText(directoryChooser.getSelectedFile().getAbsolutePath());
				}
				exportButton.setEnabled(directoryPathText.getText()!="" 
						&& fileNameText.getText()!="");
			}
		});
		exportPanel.add(browseButton, c);
		c.gridy++;
		c.gridx--;
		c.gridwidth = 2;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		fileNameText = new JTextField(15);
		fileNameText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				exportButton.setEnabled(directoryPathText.getText()!=""
						&& fileNameText.getText()!="");
			}
		});
		exportPanel.add(fileNameText, c);
		c.gridy++;
		overwriteCheck = new JCheckBox("Overwrite existing files", false);
		exportPanel.add(overwriteCheck, c);
		c.gridy++;
		demandsCombo = new JComboBox();
		demandsCombo.addItem(RAW_DEMANDS);
		demandsCombo.addItem(AGGREGATED_DEMANDS);
		exportPanel.add(demandsCombo, c);
		c.gridy++;
		referenceCombo = new JComboBox();
		referenceCombo.addItem(ID_OUTPUT);
		referenceCombo.addItem(NAME_OUTPUT);
		exportPanel.add(referenceCombo, c);
		c.gridy++;
		delimiterCombo = new JComboBox();
		delimiterCombo.addItem(TAB_DELIMITED);
		delimiterCombo.addItem(COMMA_DELIMITED);
		delimiterCombo.addItem(SEMICOLON_DELIMITED);
		exportPanel.add(delimiterCombo, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		exportButton = new JButton("Export", new ImageIcon(getClass().getClassLoader().getResource("icons/page_white_edit.png")));
		exportButton.setEnabled(false);
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportDemands();
			}
		});
		exportPanel.add(exportButton, c);
		return exportPanel;
	}
	
	/**
	 * Gets the scenario panel component.
	 * 
	 * @return the scenario panel
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}
	
	/**
	 * Gets this.
	 * 
	 * @return this
	 */
	private DemandsTab getThis() {
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
	 * Gets the demand simulator.
	 * 
	 * @return the demand simulator
	 */
	public DemandSimulator getSimulator() {
		return simulator;
	}
	
	/**
	 * A SwingWorker subclass that manages the time-intensive simulation in a
	 * separate thread.
	 */
	private class SimWorker extends SwingWorker<Void, Void> {
		private boolean isInitialization;
		
		/**
		 * Instantiates a new sim worker.
		 * 
		 * @param isInitialization the is initialization
		 */
		public SimWorker(boolean isInitialization) {
			this.isInitialization = isInitialization;
		}
		
		/**
		 * Instantiates a new sim worker.
		 */
		public SimWorker() {
			this(false);
		}
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
		 */
		public Void doInBackground() {
			try {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Simulating Demands...");
				scenarioPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				simulator.simulate();
				scenarioPanel.setCursor(Cursor.getDefaultCursor());
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#done()
		 */
		public void done() {
			// update tab only after simulation has completed
			if(isInitialization) {
				feasibilityTab.initialize();
				networkTab.initialize();
				elementDemandsTab.initialize();
				missionDemandTab.initialize();
				locationDemandTab.initialize();
				commonalityTab.initialize();
				repairabilityTab.initialize();
			} else {
				updateTab();
			}
		}
	}
	
	/**
	 * Export demands.
	 */
	private void exportDemands() {
		while(exportWorker != null && !exportWorker.isDone() 
				&& simWorker != null && !simWorker.isDone()) {
			// wait until previous simulation and export is complete
		}
		exportWorker = new ExportWorker();
		exportWorker.execute();
	}
	
	/**
	 * Write file.
	 */
	private void writeFile() {
		String filePath = directoryPathText.getText() + 
			System.getProperty("file.separator") + fileNameText.getText();
		char delimiter = ',';
		if(delimiterCombo.getSelectedItem()==COMMA_DELIMITED) {
			delimiter = ',';
		} else if(delimiterCombo.getSelectedItem()==SEMICOLON_DELIMITED) {
			delimiter = ';';
		} else if(delimiterCombo.getSelectedItem()==TAB_DELIMITED) {
			delimiter = '\t';
		}
		try {
			File file = new File(filePath);
			if(file.exists() && !overwriteCheck.isSelected()) {
				int answer = JOptionPane.showOptionDialog(getThis(), 
						"Overwrite existing file " + fileNameText.getText() + "?", 
						"SpaceNet Warning", JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE, null, null, null);
			    if (answer == JOptionPane.NO_OPTION) return;
			    else overwriteCheck.setSelected(true);
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
			if(demandsCombo.getSelectedItem()==RAW_DEMANDS) {
				out.write("Time" + delimiter + "Location" + delimiter + 
						"Element" + delimiter + "Resource" + delimiter + 
						"Amount" + System.getProperty("line.separator"));
				for(SimDemand simDemand : simulator.getUnsatisfiedDemands()) {
					for(Demand demand : simDemand.getDemands()) {
						if(referenceCombo.getSelectedItem()==ID_OUTPUT) {
							out.write("" + simDemand.getTime());
							out.write(delimiter);
							out.write("" + simDemand.getLocation().getTid());
							out.write(delimiter);
							if(simDemand.getElement()!=null) {
								out.write("" + simDemand.getElement().getUid());
							}
							out.write(delimiter);
							out.write("" + demand.getResource().getTid());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						} else if(referenceCombo.getSelectedItem()==NAME_OUTPUT) {
							out.write("" + simDemand.getTime());
							out.write(delimiter);
							out.write(simDemand.getLocation().getName());
							out.write(delimiter);
							if(simDemand.getElement()!=null) {
								out.write("" + simDemand.getElement().getName());
							}
							out.write(delimiter);
							out.write(demand.getResource().getName());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						}
						out.write(System.getProperty("line.separator"));
					}
				}
			} else if(demandsCombo.getSelectedItem()==AGGREGATED_DEMANDS) {
				out.write("Time1" + delimiter + "Time2" + delimiter + "Node1" + 
						delimiter + "Node2" + delimiter + "Resource" + 
						delimiter + "Amount" + System.getProperty("line.separator"));
				for(SupplyEdge supplyEdge : simulator.getSupplyEdges()) {
					for(Demand demand : simulator.getAggregatedEdgeDemands().get(supplyEdge)) {
						if(referenceCombo.getSelectedItem()==ID_OUTPUT) {
							out.write("" + supplyEdge.getStartTime());
							out.write(delimiter);
							out.write("" + supplyEdge.getEndTime());
							out.write(delimiter);
							out.write("" + supplyEdge.getOrigin().getTid());
							out.write(delimiter);
							out.write("" + supplyEdge.getDestination().getTid());
							out.write(delimiter);
							out.write("" + demand.getResource().getTid());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						} else if(referenceCombo.getSelectedItem()==NAME_OUTPUT) {
							out.write("" + supplyEdge.getStartTime());
							out.write(delimiter);
							out.write("" + supplyEdge.getEndTime());
							out.write(delimiter);
							out.write(supplyEdge.getOrigin().getName());
							out.write(delimiter);
							out.write(supplyEdge.getDestination().getName());
							out.write(delimiter);
							out.write(demand.getResource().getName());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						}
						out.write(System.getProperty("line.separator"));
					}
					SupplyPoint supplyPoint = supplyEdge.getPoint();
					for(Demand demand : simulator.getAggregatedNodeDemands().get(supplyPoint)) {
						if(referenceCombo.getSelectedItem()==ID_OUTPUT) {
							out.write("" + supplyPoint.getNode().getTid());
							out.write(delimiter);
							out.write(delimiter);
							out.write("" + supplyPoint.getTime());
							out.write(delimiter);
							out.write(delimiter);
							out.write("" + demand.getResource().getTid());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						} else if(referenceCombo.getSelectedItem()==NAME_OUTPUT) {
							out.write(supplyPoint.getNode().getName());
							out.write(delimiter);
							out.write(delimiter);
							out.write("" + supplyPoint.getTime());
							out.write(delimiter);
							out.write(delimiter);
							out.write(demand.getResource().getName());
							out.write(delimiter);
							out.write("" + demand.getAmount());
						}
						out.write(System.getProperty("line.separator"));
					}
				}
			}
			out.close();
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(this, 
					"An error of type \"" + 
					ex.getClass().getSimpleName() + 
					"\" occurred while exporting the demands", 
					"SpaceNet Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * The Class ExportWorker.
	 */
	private class ExportWorker extends SwingWorker<Void, Void> {
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
		 */
		protected Void doInBackground() {
			try {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Exporting Demands...");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				writeFile();
				setCursor(Cursor.getDefaultCursor());
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
	
	/**
	 * Requests that the component and any nested components update themselves.
	 */
	public void updateView() {
		discretizationCombo.setSelectedItem(getScenario().getItemDiscretization());
		aggregationSlider.setValue((int)(getScenario().getItemAggregation()*4));
		aggregationSlider.setEnabled(getScenario().getItemDiscretization()!=ItemDiscretization.NONE);
		scavengeSparesCheck.setSelected(getScenario().isScavengeSpares());
		
		while(simWorker != null && !simWorker.isDone()) {
			// lock UI while previous simulation is running
		}
		simWorker = new SimWorker();
		simWorker.execute();
	}
	
	/**
	 * Updates the currently-visible tab.
	 */
	private void updateTab() {
		if(tabbedPane.getSelectedComponent().equals(feasibilityTab)) {
			feasibilityTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(networkTab)) {
			networkTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(elementDemandsTab)) { 
			elementDemandsTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(missionDemandTab)) { 
			missionDemandTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(locationDemandTab)) { 
			locationDemandTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(repairabilityTab)) {
			repairabilityTab.updateView();
		} else if(tabbedPane.getSelectedComponent().equals(commonalityTab)) {
			commonalityTab.updateView();
		}
	}
}
