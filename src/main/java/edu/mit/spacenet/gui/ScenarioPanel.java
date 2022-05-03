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
package edu.mit.spacenet.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.JDateChooser;

import edu.mit.spacenet.data.DataSourceType;
import edu.mit.spacenet.gui.command.LoadDataSourceCommand;
import edu.mit.spacenet.gui.data.DataSourceDialog;
import edu.mit.spacenet.gui.demand.DemandsTab;
import edu.mit.spacenet.gui.manifest.ManifestTab;
import edu.mit.spacenet.gui.mission.MissionsTab;
import edu.mit.spacenet.gui.network.NetworkTab;
import edu.mit.spacenet.gui.simulation.SimulationTab;
import edu.mit.spacenet.scenario.Scenario;

/**
 * The main component for displaying and organizing scenario information. A JSplitPane with the top
 * component being the "details panel" (red background), and the bottom component being a
 * JTabbedView with the five major modules (Network, Missions, Demands, Manifest, Simulation).
 * 
 * @author Paul Grogan
 */
public class ScenarioPanel extends JSplitPane {
  private static final long serialVersionUID = 6919650461629452601L;

  private SpaceNetFrame spaceNetFrame;
  private Scenario scenario;

  private OptionsDialog optionsDialog;

  private JTextField nameText, ownerText;
  private JDateChooser startDate;
  private JLabel dataSourceLabel, lastLoadLabel;
  private JButton optionsButton, dataSourceButton, reloadButton;
  private JTextArea descriptionText;

  private JTabbedPane tabs;
  private NetworkTab networkTab;
  private MissionsTab missionsTab;
  private DemandsTab demandsTab;
  private ManifestTab manifestTab;
  private SimulationTab simulationTab;

  /**
   * Instantiates a new scenario panel.
   *
   * @param spaceNetFrame the space net frame
   */
  public ScenarioPanel(SpaceNetFrame spaceNetFrame) {
    super(JSplitPane.VERTICAL_SPLIT);
    this.spaceNetFrame = spaceNetFrame;
    optionsDialog = new OptionsDialog(this);

    buildPanel();

    setBackground(Color.LIGHT_GRAY);
    setDividerSize(5);
    setOneTouchExpandable(true);
    setDividerLocation(100);
    setResizeWeight(0);
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    JPanel detailsPanel = new JPanel();
    detailsPanel.setForeground(Color.WHITE);
    detailsPanel.setBackground(new Color(153, 51, 51));
    detailsPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    detailsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.gridheight = 3;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    ImageIcon icon =
        new ImageIcon(getClass().getClassLoader().getResource("icons/spacenet_splash.png"));
    icon.setImage(icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
    detailsPanel.add(new JLabel(icon), c);

    c.gridx++;
    c.weightx = .05;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    detailsPanel.add(new HeaderLabel("Name: "), c);
    c.gridy++;
    detailsPanel.add(new HeaderLabel("Start Date: "), c);
    c.gridy++;
    detailsPanel.add(new HeaderLabel("Created By: "), c);

    c.gridx++;
    c.gridy = 0;
    c.weightx = .25;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    nameText = new JTextField(20);
    nameText.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        scenario.setName(nameText.getText());
        spaceNetFrame.repaint(); // update frame title
      }
    });
    detailsPanel.add(nameText, c);
    c.gridy++;
    startDate = new JDateChooser();
    startDate.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("date")) {
          scenario.setStartDate(startDate.getDate());
        }
      }
    });
    detailsPanel.add(startDate, c);
    c.gridy++;
    ownerText = new JTextField(15);
    ownerText.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        scenario.setCreatedBy(ownerText.getText());
      }
    });
    detailsPanel.add(ownerText, c);

    c.gridx++;
    c.gridy = 0;
    c.weightx = .05;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    detailsPanel.add(new HeaderLabel("Description: "), c);

    c.gridx++;
    c.weightx = .4;
    c.weighty = 1;
    c.gridheight = 4;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    descriptionText = new JTextArea();
    descriptionText.setLineWrap(true);
    descriptionText.setWrapStyleWord(true);
    descriptionText.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
    descriptionText.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        scenario.setDescription(descriptionText.getText());
      }
    });
    JScrollPane descriptionScroll = new JScrollPane(descriptionText);
    descriptionScroll.setPreferredSize(new Dimension(250, 50));
    detailsPanel.add(descriptionScroll, c);

    c.gridx++;
    c.gridy = 0;
    c.weightx = .05;
    c.weighty = 0;
    c.gridheight = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    detailsPanel.add(new HeaderLabel("Options: "), c);
    c.gridy++;
    detailsPanel.add(new HeaderLabel("Data Source: "), c);

    c.gridx++;
    c.gridy = 0;
    c.weightx = .1;
    c.anchor = GridBagConstraints.LINE_START;
    optionsButton = new JButton("Edit",
        new ImageIcon(getClass().getClassLoader().getResource("icons/cog_edit.png")));
    optionsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        optionsDialog.showDialog();
      }
    });
    detailsPanel.add(optionsButton, c);
    c.gridy++;
    c.fill = GridBagConstraints.HORIZONTAL;
    JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
    dataPanel.setOpaque(false);
    dataSourceLabel = new JLabel("");
    dataSourceLabel.setForeground(Color.WHITE);
    dataPanel.add(dataSourceLabel);
    dataSourceButton = new JButton("Edit",
        new ImageIcon(getClass().getClassLoader().getResource("icons/database_edit.png")));
    dataSourceButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DataSourceDialog.createAndShowGUI(getThis());
      }
    });
    dataPanel.add(dataSourceButton);
    reloadButton = new JButton("Reload",
        new ImageIcon(getClass().getClassLoader().getResource("icons/database_refresh.png")));
    reloadButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        LoadDataSourceCommand command = new LoadDataSourceCommand(getThis());
        command.execute();
      }
    });
    dataPanel.add(reloadButton);
    detailsPanel.add(dataPanel, c);
    c.gridy++;
    lastLoadLabel = new JLabel();
    lastLoadLabel.setForeground(Color.WHITE);
    detailsPanel.add(lastLoadLabel, c);

    setTopComponent(detailsPanel);

    tabs = new JTabbedPane();

    networkTab = new NetworkTab(this);
    tabs.addTab("Network",
        new ImageIcon(getClass().getClassLoader().getResource("icons/world.png")), networkTab);

    missionsTab = new MissionsTab(this);
    tabs.addTab("Missions",
        new ImageIcon(getClass().getClassLoader().getResource("icons/flag_red.png")), missionsTab);

    demandsTab = new DemandsTab(this);
    tabs.addTab("Demands",
        new ImageIcon(getClass().getClassLoader().getResource("icons/comment.png")), demandsTab);

    manifestTab = new ManifestTab(this);
    tabs.addTab("Manifest",
        new ImageIcon(getClass().getClassLoader().getResource("icons/book_open.png")), manifestTab);

    simulationTab = new SimulationTab(this);
    tabs.addTab("Simulation",
        new ImageIcon(getClass().getClassLoader().getResource("icons/lightning.png")),
        simulationTab);

    setBottomComponent(tabs);

    tabs.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (!tabs.getSelectedComponent().equals(networkTab)
            && scenario.getNetwork().getNodes().size() == 0) {
          tabs.setSelectedComponent(networkTab);
          JOptionPane.showMessageDialog(spaceNetFrame, "At least one network node is required.",
              "SpaceNet Warning", JOptionPane.WARNING_MESSAGE, null);
        } else if (!tabs.getSelectedComponent().equals(networkTab)
            && !tabs.getSelectedComponent().equals(missionsTab)
            && scenario.getMissionList().size() == 0) {
          tabs.setSelectedComponent(missionsTab);
          JOptionPane.showMessageDialog(spaceNetFrame, "At least one mission is required.",
              "SpaceNet Warning", JOptionPane.WARNING_MESSAGE, null);
        } else if (tabs.getSelectedComponent().equals(networkTab)) {
          // network tab is initialized when the scenario is
          // loaded and is not dependent on any other components
        } else if (tabs.getSelectedComponent().equals(missionsTab)) {
          missionsTab.initialize();
        } else if (tabs.getSelectedComponent().equals(demandsTab)) {
          demandsTab.initialize();
        } else if (tabs.getSelectedComponent().equals(manifestTab)) {
          // do not re-initialize manifest
        } else if (tabs.getSelectedComponent().equals(simulationTab)) {
          // do not re-initialize scenario
        }
        spaceNetFrame.repaint();
      }
    });
  }

  private void initialize() {
    nameText.setEnabled(scenario != null);
    startDate.setEnabled(scenario != null);
    ownerText.setEnabled(scenario != null);
    optionsButton.setEnabled(scenario != null);
    dataSourceButton.setEnabled(scenario != null);
    reloadButton.setEnabled(scenario != null);
    descriptionText.setEnabled(scenario != null);
    if (scenario != null) {
      nameText.setText(scenario.getName());
      startDate.setDate(scenario.getStartDate());
      ownerText.setText(scenario.getCreatedBy());
      descriptionText.setText(scenario.getDescription());
      if (scenario.getDataSource() == null) {
        dataSourceLabel.setText(DataSourceType.NONE.getName());
        dataSourceLabel.setIcon(DataSourceType.NONE.getIcon());
      } else {
        dataSourceLabel.setText(getScenario().getDataSource().getDataSourceType().getName());
        dataSourceLabel.setIcon(getScenario().getDataSource().getDataSourceType().getIcon());
      }
      networkTab.initialize();
      manifestTab.initialize();
      simulationTab.initialize();
      editNetwork();
    }
  }

  /**
   * Opens the tab to edit the network.
   */
  public void editNetwork() {
    tabs.setSelectedComponent(networkTab);
  }

  /**
   * Gets whether the network tab is being edited.
   * 
   * @return whether the network is being edited
   */
  public boolean isEditingNetwork() {
    return tabs.getSelectedComponent().equals(networkTab);
  }

  /**
   * Opens the tab to edit the missions.
   */
  public void editMissions() {
    tabs.setSelectedComponent(missionsTab);
  }

  /**
   * Gets whether the missions tab is being edited.
   * 
   * @return whether the missions are being edited
   */
  public boolean isEditingMissions() {
    return tabs.getSelectedComponent().equals(missionsTab);
  }

  /**
   * Opens the tab to edit the demands.
   */
  public void editDemands() {
    tabs.setSelectedComponent(demandsTab);
  }

  /**
   * Gets whether the demands tab is being edited.
   * 
   * @return whether the demands tab is being edited
   */
  public boolean isEditingDemands() {
    return tabs.getSelectedComponent().equals(demandsTab);
  }

  /**
   * Opens the tab to edit the manifest.
   */
  public void editManifest() {
    tabs.setSelectedComponent(manifestTab);
  }

  /**
   * Gets whether the manifest tab is being edited.
   * 
   * @return whether the manifest tab is being edited
   */
  public boolean isEditingManifest() {
    return tabs.getSelectedComponent().equals(manifestTab);
  }

  /**
   * Opens the tab to edit the simulation.
   */
  public void editSimulation() {
    tabs.setSelectedComponent(simulationTab);
  }

  /**
   * Gets whether the simulation tab is being edited.
   * 
   * @return whether the simulation tab is being edited
   */
  public boolean isEditingSimulation() {
    return tabs.getSelectedComponent().equals(simulationTab);
  }

  /**
   * A custom JLabel class to format the labels in the scenario details panel.
   */
  private class HeaderLabel extends JLabel {
    private static final long serialVersionUID = -7501636123809460913L;

    public HeaderLabel(String text) {
      super(text);
      setFont(getFont().deriveFont(Font.BOLD));
      setForeground(Color.WHITE);
    }
  }

  /**
   * Gets this.
   * 
   * @return this
   */
  private ScenarioPanel getThis() {
    return this;
  }

  /**
   * Gets the scenario.
   * 
   * @return the scenario
   */
  public Scenario getScenario() {
    return scenario;
  }

  /**
   * Sets the scenario and refreshes all visible components.
   * 
   * @param scenario the new scenario
   */
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
    initialize();
  }

  /**
   * Orders the scenario panel to update itself by reloading information from the model. Also orders
   * the current open tab to update itself.
   */
  public void updateView() {
    SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Updating...");
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    if (getScenario().getDataSource() == null) {
      reloadButton.setEnabled(false);
      dataSourceLabel.setText(DataSourceType.NONE.getName());
      dataSourceLabel.setIcon(DataSourceType.NONE.getIcon());
      lastLoadLabel.setText(null);
    } else {
      reloadButton.setEnabled(true);
      dataSourceLabel.setText(getScenario().getDataSource().getName());
      dataSourceLabel.setIcon(getScenario().getDataSource().getDataSourceType().getIcon());
      SimpleDateFormat format = new SimpleDateFormat("MMM, d yyyy h:mm a zzz");
      lastLoadLabel.setText(
          "Last Loaded: " + (getScenario().getDataSource().getLastLoadDate() == null ? "Never"
              : format.format(getScenario().getDataSource().getLastLoadDate())));
    }

    if (isEditingNetwork()) {
      networkTab.updateView();
    } else if (isEditingMissions()) {
      missionsTab.updateView();
    } else if (isEditingDemands()) {
      demandsTab.updateView();
    } else if (isEditingManifest()) {
      manifestTab.updateView();
    } else if (isEditingSimulation()) {
      simulationTab.updateView();
    }

    SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
    setCursor(Cursor.getDefaultCursor());
  }

  /**
   * Gets the parent SpaceNet frame.
   * 
   * @return the parent SpaceNet frame
   */
  public SpaceNetFrame getSpaceNetFrame() {
    return spaceNetFrame;
  }

  /**
   * Gets the options dialog.
   * 
   * @return the options dialog
   */
  public OptionsDialog getOptionsDialog() {
    return optionsDialog;
  }
}
