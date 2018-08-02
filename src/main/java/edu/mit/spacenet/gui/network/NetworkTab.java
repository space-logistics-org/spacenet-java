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
package edu.mit.spacenet.gui.network;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.command.FilterNetworkCommand;
import edu.mit.spacenet.gui.component.SearchTextField;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.visualization.NetworkPanel;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.ScenarioType;

/**
 * The tab component to view and edit the scenario network.
 * 
 * @author Paul Grogan
 */
public class NetworkTab extends JSplitPane {
	private static final long serialVersionUID = 5930409573819745343L;
	private static String NODE_DETAILS = "Node Details Panel";
	private static String EDGE_DETAILS = "Edge Details Panel";
	
	private JComboBox scenarioTypeCombo, nodeTypeCombo, edgeTypeCombo;
	private JTabbedPane locationSelectionTabbedPane;
	private JPanel nodeSelectionPanel, edgeSelectionPanel;
	private SearchTextField nodeSearchText, edgeSearchText;
	private NodeTable nodeTable;
	private EdgeTable edgeTable;
	private JPanel detailsPanel;
	private NodeDetailsPanel nodeDetailsPanel;
	private EdgeDetailsPanel edgeDetailsPanel;
	
	private NetworkPanel networkPanel;
	
	private JCheckBox drawOrbitalLimitsCheck, simpleModeCheck, 
		displaySunCheck, blackBackgroundCheck;
	
	private ScenarioPanel scenarioPanel;
	
	/**
	 * Instantiates a new network tab.
	 * 
	 * @param scenarioPanel the scenario panel
	 */
	public NetworkTab(ScenarioPanel scenarioPanel) {
		this.scenarioPanel = scenarioPanel;
		
		buildControlPanel();
		buildDisplayPanel();
		
		setResizeWeight(0);
		setDividerLocation(250);
		setBorder(BorderFactory.createEmptyBorder());

		initialize();
	}
	private void buildControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		controlPanel.add(new JLabel("Scenario Type: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		scenarioTypeCombo = new JComboBox();
		for(ScenarioType type : ScenarioType.values()) {
			scenarioTypeCombo.addItem(type);
		}
		scenarioTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2255885956722142642L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setIcon(((ScenarioType)value).getIcon());
				return this;
			}
		});
		scenarioTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(getScenario().getScenarioType()!=scenarioTypeCombo.getSelectedItem()) {
					getScenario().setScenarioType((ScenarioType)scenarioTypeCombo.getSelectedItem());
					FilterNetworkCommand command = new FilterNetworkCommand(getScenario());
					command.execute();
					updateView();
				}
			}
		});
		controlPanel.add(scenarioTypeCombo, c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		locationSelectionTabbedPane = new JTabbedPane();
		
		nodeSelectionPanel = new JPanel();
		nodeSelectionPanel.setLayout(new BorderLayout());
		JPanel nodeSearchPanel = new JPanel();
		nodeSearchPanel.setLayout(new BoxLayout(nodeSearchPanel, BoxLayout.LINE_AXIS));
		nodeSearchText = new SearchTextField("Enter Search Term");
		nodeSearchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				nodeTable.updateView();
			}
		});
		nodeSearchPanel.add(nodeSearchText);
		nodeTypeCombo = new JComboBox();
		nodeTypeCombo.addItem("All");
		for(NodeType type : NodeType.values()) {
			nodeTypeCombo.addItem(type);
		}
		nodeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2255885956722142642L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof NodeType) setIcon(((NodeType)value).getIcon());
				return this;
			}
		});
		nodeTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				nodeTable.updateView();
			}
		});
		nodeSearchPanel.add(nodeTypeCombo);
		nodeSelectionPanel.add(nodeSearchPanel, BorderLayout.NORTH);
		nodeTable = new NodeTable(this);
		nodeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(nodeTable.getSelectedRow()>=0) {
					networkPanel.getNetworkLayer().getSelectedEdges().clear();
					networkPanel.getNetworkLayer().getSelectedNodes().clear();
					for(int row : nodeTable.getSelectedRows()) {
						networkPanel.getNetworkLayer().getSelectedNodes().add(((Node)nodeTable.getModel().getValueAt(row, 1)).getTid());
					}
					networkPanel.repaint();
				}
				if(nodeTable.getSelectedRows().length == 1) {
					nodeDetailsPanel.setNode((Node)nodeTable.getModel().getValueAt(nodeTable.getSelectedRow(), 1));
				} else {
					nodeDetailsPanel.setNode(null);
				}
			}
		});
		JScrollPane nodeScroll = new JScrollPane(nodeTable);
		nodeScroll.setPreferredSize(new Dimension(150,300));
		nodeSelectionPanel.add(nodeScroll, BorderLayout.CENTER);
		JPanel nodeButtonPanel = new JPanel();
		nodeButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllNodesButton = new JButton("Select All");
		selectAllNodesButton.setToolTipText("Select All Nodes");
		selectAllNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodeTable.checkAll();
			}
		});
		nodeButtonPanel.add(selectAllNodesButton);
		JButton deselectAllNodesButton = new JButton("Deselect All");
		deselectAllNodesButton.setToolTipText("Deselect All Nodes");
		deselectAllNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodeTable.uncheckAll();
			}
		});
		nodeButtonPanel.add(deselectAllNodesButton);
		nodeSelectionPanel.add(nodeButtonPanel, BorderLayout.SOUTH);
		locationSelectionTabbedPane.add(nodeSelectionPanel, "Nodes");
		
		edgeSelectionPanel = new JPanel();
		edgeSelectionPanel.setLayout(new BorderLayout());
		JPanel edgeSearchPanel = new JPanel();
		edgeSearchPanel.setLayout(new BoxLayout(edgeSearchPanel, BoxLayout.LINE_AXIS));
		edgeSearchText = new SearchTextField("Enter Search Term");
		edgeSearchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				edgeTable.updateView();
			}
		});
		edgeSearchPanel.add(edgeSearchText);
		edgeTypeCombo = new JComboBox();
		edgeTypeCombo.addItem("All");
		for(EdgeType type : EdgeType.values()) {
			if(type!=EdgeType.TIME_DEPENDENT)
				edgeTypeCombo.addItem(type);
		}
		edgeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2255885956722142642L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof EdgeType) setIcon(((EdgeType)value).getIcon());
				return this;
			}
		});
		edgeTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				edgeTable.updateView();
			}
		});
		edgeSearchPanel.add(edgeTypeCombo);
		edgeSelectionPanel.add(edgeSearchPanel, BorderLayout.NORTH);
		edgeTable = new EdgeTable(this);
		edgeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(edgeTable.getSelectedRow()>=0) {
					networkPanel.getNetworkLayer().getSelectedEdges().clear();
					networkPanel.getNetworkLayer().getSelectedNodes().clear();
					for(int row : edgeTable.getSelectedRows()) {
						networkPanel.getNetworkLayer().getSelectedEdges().add(((Edge)edgeTable.getModel().getValueAt(row, 1)).getTid());
					}
					networkPanel.repaint();
				}
				if(edgeTable.getSelectedRows().length == 1) {
					edgeDetailsPanel.setEdge((Edge)edgeTable.getModel().getValueAt(edgeTable.getSelectedRow(), 1));
				} else {
					edgeDetailsPanel.setEdge(null);
				}
			}
		});
		JScrollPane edgeScroll = new JScrollPane(edgeTable);
		edgeScroll.setPreferredSize(new Dimension(150,300));
		edgeSelectionPanel.add(edgeScroll, BorderLayout.CENTER);
		JPanel edgeButtonPanel = new JPanel();
		edgeButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JButton selectAllEdgesButton = new JButton("Select All");
		selectAllEdgesButton.setToolTipText("Select All Edges");
		selectAllEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeTable.checkAll();
			}
		});
		edgeButtonPanel.add(selectAllEdgesButton);
		JButton deselectAllEdgesButton = new JButton("Deselect All");
		deselectAllEdgesButton.setToolTipText("Deselect All Edges");
		deselectAllEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeTable.uncheckAll();
			}
		});
		edgeButtonPanel.add(deselectAllEdgesButton);
		edgeSelectionPanel.add(edgeButtonPanel, BorderLayout.SOUTH);
		locationSelectionTabbedPane.add(edgeSelectionPanel, "Edges");
		controlPanel.add(locationSelectionTabbedPane, c);
		
		c.gridy++;
		detailsPanel = new JPanel();
		detailsPanel.setLayout(new CardLayout());
		nodeDetailsPanel = new NodeDetailsPanel();
		detailsPanel.add(nodeDetailsPanel, NODE_DETAILS);
		edgeDetailsPanel = new EdgeDetailsPanel();
		detailsPanel.add(edgeDetailsPanel, EDGE_DETAILS);
		
		locationSelectionTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(locationSelectionTabbedPane.getSelectedIndex()==0) {
					((CardLayout)detailsPanel.getLayout()).show(detailsPanel, NODE_DETAILS);
					edgeTable.clearSelection();
				} else {
					((CardLayout)detailsPanel.getLayout()).show(detailsPanel, EDGE_DETAILS);
					nodeTable.clearSelection();
				}
			}
		});
		
		controlPanel.add(detailsPanel, c);
		
		setLeftComponent(new JScrollPane(controlPanel));
	}
	
	/**
	 * Builds the display panel.
	 */
	private void buildDisplayPanel() {
		JSplitPane displayPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		networkPanel = new NetworkPanel();
		networkPanel.setPreferredSize(new Dimension(600,400));
		displayPanel.setTopComponent(new JScrollPane(networkPanel));
		displayPanel.setResizeWeight(1);
		
		JPanel displayOptionsPanel = new JPanel();
		displayOptionsPanel.setLayout(new BoxLayout(displayOptionsPanel, BoxLayout.LINE_AXIS));
		JPanel bodyOptionsPanel = new JPanel();
		bodyOptionsPanel.setBorder(BorderFactory.createTitledBorder("Body Display Options"));
		bodyOptionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		bodyOptionsPanel.add(new JLabel("Size Scaling:"), c);
		c.gridy++;
		bodyOptionsPanel.add(new JLabel("Size Basis:"), c);
		c.gridy++;
		bodyOptionsPanel.add(new JLabel("Size Disparity:"), c);
		c.gridx+=2;
		c.gridy = 0;
		bodyOptionsPanel.add(new JLabel("Position Scaling:"), c);
		c.gridy++;
		bodyOptionsPanel.add(new JLabel("Position Basis:"), c);
		c.gridy++;
		bodyOptionsPanel.add(new JLabel("Position Disparity:"), c);
		c.gridy = 0;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		final SpinnerNumberModel sizeScalingModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getSizeScale(), 0, Double.MAX_VALUE, 0.1);
		final JSpinner sizeScalingSpinner = new JSpinner(sizeScalingModel);
		sizeScalingSpinner.setPreferredSize(new Dimension(60,20));
		sizeScalingSpinner.setToolTipText("A parameter that controls the rate of size increase.");
		sizeScalingSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setSizeScale(sizeScalingModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(sizeScalingSpinner, c);
		c.gridy++;
		final SpinnerNumberModel sizeBasisModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getSizeBasis(), 0, Double.MAX_VALUE, 0.1);
		final JSpinner sizeBasisSpinner = new JSpinner(sizeBasisModel);
		sizeBasisSpinner.setPreferredSize(new Dimension(60,20));
		sizeBasisSpinner.setToolTipText("A parameter that sets the median size (units of Earth diameters).");
		sizeBasisSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setSizeBasis(sizeBasisModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(sizeBasisSpinner, c);
		c.gridy++;
		final SpinnerNumberModel sizeDisparityModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getSizeDisparity(), 1.0, Double.MAX_VALUE, 0.1);
		final JSpinner sizeDisparitySpinner = new JSpinner(sizeDisparityModel);
		sizeDisparitySpinner.setPreferredSize(new Dimension(60,20));
		sizeDisparitySpinner.setToolTipText("A parameter that sets the maximum difference between the largest and smallest sizes.");
		sizeDisparitySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setSizeDisparity(sizeDisparityModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(sizeDisparitySpinner, c);
		c.gridy=0;
		c.gridx+=2;
		final SpinnerNumberModel locationScalingModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getLocationScale(), 0, Double.MAX_VALUE, 0.1);
		final JSpinner locationScalingSpinner = new JSpinner(locationScalingModel);
		locationScalingSpinner.setPreferredSize(new Dimension(60,20));
		locationScalingSpinner.setToolTipText("A parameter that controls the rate of distance increase.");
		locationScalingSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setLocationScale(locationScalingModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(locationScalingSpinner, c);
		c.gridy++;
		final SpinnerNumberModel locationBasisModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getLocationBasis(), 0, Double.MAX_VALUE, 0.1);
		final JSpinner locationBasisSpinner = new JSpinner(locationBasisModel);
		locationBasisSpinner.setPreferredSize(new Dimension(60,20));
		locationBasisSpinner.setToolTipText("A parameter that sets the median distance (units of AU).");
		locationBasisSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setLocationBasis(locationBasisModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(locationBasisSpinner, c);
		c.gridy++;
		final SpinnerNumberModel locationDisparityModel = new SpinnerNumberModel(networkPanel.getBodyLayer().getLocationDisparity(), 1.0, Double.MAX_VALUE, 0.1);
		final JSpinner locationDisparitySpinner = new JSpinner(locationDisparityModel);
		locationDisparitySpinner.setPreferredSize(new Dimension(60,20));
		locationDisparitySpinner.setToolTipText("A parameter that sets the maximum difference between the largest and smallest distances.");
		locationDisparitySpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getBodyLayer().setLocationDisparity(locationDisparityModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(locationDisparitySpinner, c);
		c.gridy=0;
		c.gridx++;
		drawOrbitalLimitsCheck = new JCheckBox("Draw Orbital Limits");
		drawOrbitalLimitsCheck.setSelected(networkPanel.getBodyLayer().isDrawOrbitalLimits());
		drawOrbitalLimitsCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				networkPanel.getBodyLayer().setDrawOrbitalLimits(drawOrbitalLimitsCheck.isSelected());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(drawOrbitalLimitsCheck, c);
		c.gridy++;
		simpleModeCheck = new JCheckBox("Inline Moon Mode");
		simpleModeCheck.setSelected(networkPanel.getBodyLayer().isSimpleMode());
		simpleModeCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				displaySunCheck.setEnabled(!simpleModeCheck.isSelected());
				if(simpleModeCheck.isSelected() && displaySunCheck.isSelected()) {
					displaySunCheck.setSelected(false);
				}
				networkPanel.getBodyLayer().setSimpleMode(simpleModeCheck.isSelected());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(simpleModeCheck, c);
		c.gridy++;
		displaySunCheck = new JCheckBox("Display Sun");
		displaySunCheck.setEnabled(!networkPanel.getBodyLayer().isSimpleMode());
		displaySunCheck.setSelected(!networkPanel.getBodyLayer().isSimpleMode() 
				&& networkPanel.getBodyLayer().isDisplaySun());
		displaySunCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				networkPanel.getBodyLayer().setDisplaySun(displaySunCheck.isSelected());
				networkPanel.repaint();
			}
		});
		bodyOptionsPanel.add(displaySunCheck, c);
		displayOptionsPanel.add(bodyOptionsPanel);
		
		JPanel networkOptionsPanel = new JPanel();
		networkOptionsPanel.setBorder(BorderFactory.createTitledBorder("Network Display Options"));
		networkOptionsPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		networkOptionsPanel.add(new JLabel("Altitude Scaling:"), c);
		c.gridy++;
		networkOptionsPanel.add(new JLabel("Altitude Basis:"), c);
		c.gridy=0;
		c.gridx+=2;
		networkOptionsPanel.add(new JLabel("Node Size:"), c);
		c.gridy++;
		networkOptionsPanel.add(new JLabel("Edge Width:"), c);
		c.gridy = 0;
		c.gridx = 1;;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1;
		final SpinnerNumberModel altitudeScalingModel = new SpinnerNumberModel(1/networkPanel.getNetworkLayer().getAltitudeScale(), 0.5, Double.MAX_VALUE, 0.5);
		final JSpinner altitudeScalingSpinner = new JSpinner(altitudeScalingModel);
		altitudeScalingSpinner.setPreferredSize(new Dimension(60,20));
		altitudeScalingSpinner.setToolTipText("A parameter that controls the rate of distance increase for orbital altitude.");
		altitudeScalingSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getNetworkLayer().setAltitudeScale(1/altitudeScalingModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		networkOptionsPanel.add(altitudeScalingSpinner, c);
		c.gridy++;
		final SpinnerNumberModel altitudeBasisModel = new SpinnerNumberModel(networkPanel.getNetworkLayer().getAltitudeBasis(), 0, Double.MAX_VALUE, 0.5);
		final JSpinner altitudeBasisSpinner = new JSpinner(altitudeBasisModel);
		altitudeBasisSpinner.setPreferredSize(new Dimension(60,20));
		altitudeBasisSpinner.setToolTipText("A parameter that sets the median altitude (units of kilometers).");
		altitudeBasisSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getNetworkLayer().setAltitudeBasis(altitudeBasisModel.getNumber().doubleValue());
				networkPanel.repaint();
			}
		});
		networkOptionsPanel.add(altitudeBasisSpinner, c);
		c.gridy=0;
		c.gridx+=2;
		final SpinnerNumberModel nodeSizeModel = new SpinnerNumberModel(networkPanel.getNetworkLayer().getNodeSize(), 0, 32, 4);
		final JSpinner nodeSizeSpinner = new JSpinner(nodeSizeModel);
		nodeSizeSpinner.setPreferredSize(new Dimension(60,20));
		nodeSizeSpinner.setToolTipText("A parameter that sets the node size (pixels).");
		nodeSizeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getNetworkLayer().setNodeSize(nodeSizeModel.getNumber().intValue());
				networkPanel.repaint();
			}
		});
		networkOptionsPanel.add(new UnitsWrapper(nodeSizeSpinner, "px"), c);
		c.gridy++;
		final SpinnerNumberModel edgeWidthModel = new SpinnerNumberModel(networkPanel.getNetworkLayer().getEdgeWidth(), 1, 5, 1);
		final JSpinner edgeWidthSpinner = new JSpinner(edgeWidthModel);
		edgeWidthSpinner.setPreferredSize(new Dimension(60,20));
		edgeWidthSpinner.setToolTipText("A parameter that sets the edge width (pixels).");
		edgeWidthSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				networkPanel.getNetworkLayer().setEdgeWidth(edgeWidthModel.getNumber().intValue());
				networkPanel.repaint();
			}
		});
		networkOptionsPanel.add(new UnitsWrapper(edgeWidthSpinner, "px"), c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 4;
		blackBackgroundCheck = new JCheckBox("Draw Black Background");
		blackBackgroundCheck.setSelected(networkPanel.isBlackBackground());
		blackBackgroundCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				networkPanel.setBlackBackground(blackBackgroundCheck.isSelected());
				networkPanel.repaint();
			}
		});
		networkOptionsPanel.add(blackBackgroundCheck, c);
		displayOptionsPanel.add(networkOptionsPanel);
		
		
		displayPanel.setBottomComponent(displayOptionsPanel);
		displayPanel.setBorder(BorderFactory.createEmptyBorder());
		displayPanel.setDividerSize(5);
		displayPanel.setLastDividerLocation(425);
		displayPanel.setDividerLocation(1000);
		displayPanel.setOneTouchExpandable(true);
		displayPanel.setResizeWeight(1);
		setRightComponent(displayPanel);
	}
	
	/**
	 * Initializes the network tab by enabling/disabling components and setting
	 * initial values.
	 */
	public void initialize() {
		scenarioTypeCombo.setEnabled(getScenario()!=null);
		nodeSearchText.setEnabled(getScenario()!=null);
		nodeTypeCombo.setEnabled(getScenario()!=null);
		nodeTable.setEnabled(getScenario()!=null);
		edgeSearchText.setEnabled(getScenario()!=null);
		edgeTypeCombo.setEnabled(getScenario()!=null);
		edgeTable.setEnabled(getScenario()!=null);
		if(getScenario()!=null) {
			scenarioTypeCombo.setSelectedItem(getScenario().getScenarioType());
			nodeTable.initialize();
			edgeTable.initialize();
			networkPanel.setNetwork(getScenario().getNetwork());
		}
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
	 * Gets the scenario panel.
	 * 
	 * @return the scenario panel
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}
	
	/**
	 * Gets the network panel.
	 * 
	 * @return the network panel
	 */
	public NetworkPanel getNetworkPanel() {
		return networkPanel;
	}
	
	/**
	 * Update view.
	 */
	public void updateView() {
		nodeTable.updateView();
		edgeTable.updateView();
		networkPanel.repaint();
	}
	
	/**
	 * Gets the node search text.
	 * 
	 * @return the node search text
	 */
	public String getNodeSearchText() {
		if(nodeSearchText.getText().equals(nodeSearchText.getDefaultText())) 
			return "";
		else return nodeSearchText.getText();
	}
	
	/**
	 * Gets the node type filter.
	 * 
	 * @return the node type filter
	 */
	public NodeType getNodeTypeFilter() {
		if(nodeTypeCombo.getSelectedItem() instanceof NodeType) {
			return (NodeType)nodeTypeCombo.getSelectedItem();
		} else return null;
	}
	
	/**
	 * Gets the edge search text.
	 * 
	 * @return the edge search text
	 */
	public String getEdgeSearchText() {
		if(edgeSearchText.getText().equals(edgeSearchText.getDefaultText())) 
			return "";
		else return edgeSearchText.getText();
	}
	
	/**
	 * Gets the edge type filter.
	 * 
	 * @return the edge type filter
	 */
	public EdgeType getEdgeTypeFilter() {
		if(edgeTypeCombo.getSelectedItem() instanceof EdgeType) {
			return (EdgeType)edgeTypeCombo.getSelectedItem();
		} else return null;
	}
}
