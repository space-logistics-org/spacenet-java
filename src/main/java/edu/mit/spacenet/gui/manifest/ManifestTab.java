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
package edu.mit.spacenet.gui.manifest;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.component.DropDownButton;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.scenario.Manifest;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.scenario.SupplyEdge;
import edu.mit.spacenet.scenario.SupplyPoint;
import edu.mit.spacenet.simulator.DemandSimulator;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The tab component used to view and edit the scenario manifest.
 * 
 * @author Paul Grogan
 */
public class ManifestTab extends JSplitPane {
  private static final long serialVersionUID = 8829941616137676449L;
  private static String ID_OUTPUT = "Object IDs";
  private static String NAME_OUTPUT = "Object Names";
  private static String COMMA_DELIMITED = "Comma Delimited";
  private static String SEMICOLON_DELIMITED = "Semicolon Delimited";
  private static String TAB_DELIMITED = "Tab Delimited";
  private static String NO_AGGREGATION = "No Aggregation";
  private static String CONTAINER_AGGREGATION = "Container Aggregation";
  private static String CARRIER_AGGREGATION = "Carrier Aggregation";
  private static String TRANSPORT_AGGREGATION = "Transport Aggregation";

  private ScenarioPanel scenarioPanel;
  private DemandSimulator simulator;
  private SimWorker simWorker;
  private ManifestWorker manifestWorker;
  private AggregatedDemandsTable aggregatedDemandsTable;
  private JButton clearButton, resetButton, autoManifestButton, unpackButton, autoPackButton;

  private JTextField directoryPathText, fileNameText;
  private JCheckBox overwriteCheck;
  private JButton browseButton, exportButton;
  private JComboBox<String> referenceCombo;
  private JComboBox<String> delimiterCombo;
  private JComboBox<String> aggregationCombo;
  private JFileChooser directoryChooser;
  private ExportWorker exportWorker;

  private PackedDemandsTable packedDemandsTable;
  private JButton editContainerButton, removeContainerButton;
  private JLabel containerNameLabel, containerMassLabel, containerVolumeLabel,
      containerEnvironmentLabel, containerCargoEnvironmentLabel;
  private ContainerContentsTable containerContentsTable;
  private JButton unpackContentsButton;
  private JProgressBar containerMassCapacity, containerVolumeCapacity;
  private JButton packButton, unmanifestButton;

  private ElementTree carrierTree;
  private CarrierContentsTable carrierContentsTable;
  private JButton unmanifestContentsButton;
  private JLabel carrierEnvironmentLabel;
  private JProgressBar carrierMassCapacity, carrierVolumeCapacity;
  private JButton manifestButton;

  private ManifestedDemandsTable manifestedDemandsTable;

  /**
   * Instantiates a new manifest tab.
   * 
   * @param scenarioPanel the scenario panel
   */
  public ManifestTab(ScenarioPanel scenarioPanel) {
    this.scenarioPanel = scenarioPanel;

    setLeftComponent(buildDemandsPanel());
    JSplitPane rightPanel = new JSplitPane();
    rightPanel.setLeftComponent(buildPackingPanel());
    rightPanel.setRightComponent(buildManifestingPanel());
    setRightComponent(rightPanel);

    setResizeWeight(1 / 3D);
    setDividerLocation(1 / 3D);
    rightPanel.setResizeWeight(1 / 2D);
    rightPanel.setDividerLocation(1 / 2D);
    rightPanel.setBorder(BorderFactory.createEmptyBorder());
    setBorder(BorderFactory.createEmptyBorder());
  }

  /**
   * The Class ExportWorker.
   */
  private class ExportWorker extends SwingWorker<Void, Void> {

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    protected Void doInBackground() {
      try {
        SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Exporting Manifest...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        writeFile();
        setCursor(Cursor.getDefaultCursor());
        SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return null;
    }
  }

  /**
   * Export manifest.
   */
  private void exportManifest() {
    while (exportWorker != null && !exportWorker.isDone() && simWorker != null
        && !simWorker.isDone()) {
      // wait until previous simulation and export is complete
    }
    exportWorker = new ExportWorker();
    exportWorker.execute();
  }

  /**
   * Builds the demands panel.
   * 
   * @return the demands panel
   */
  private JPanel buildDemandsPanel() {
    JPanel demandsPanel = new JPanel();
    demandsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    demandsPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.NONE;
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
    clearButton = new JButton("Clear Manifest");
    clearButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cross.png")));
    clearButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getManifest().reset();
        updateView();
      }
    });
    buttonPanel.add(clearButton);
    resetButton = new JButton("Reset Manifest");
    resetButton.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("icons/book_open_refresh.png")));
    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        while (simWorker != null && !simWorker.isDone()) {
          // wait until previous reset is finished
        }
        getManifest().reset();
        simWorker = new SimWorker();
        simWorker.execute();
        updateView();
      }
    });
    buttonPanel.add(resetButton);
    autoManifestButton = new JButton("Auto-Manifest");
    autoManifestButton.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("icons/book_open_lightning.png")));
    autoManifestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        while (manifestWorker != null && !manifestWorker.isDone()) {
          // wait until previous auto-manifesting process is finished
        }
        manifestWorker = new ManifestWorker();
        manifestWorker.execute();
      }
    });
    buttonPanel.add(autoManifestButton);
    demandsPanel.add(buttonPanel, c);

    c.gridy++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    demandsPanel.add(new JLabel("Aggregated Demands"), c);
    c.gridy++;
    c.weighty = 1;
    aggregatedDemandsTable = new AggregatedDemandsTable(this);
    aggregatedDemandsTable.getSelectionModel()
        .addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            if (aggregatedDemandsTable.getSelectedRow() >= 0) {
              // packedDemandsTable.clearSelection();
              // manifestedDemandsTable.clearSelection();
            }
            updateButtons();
          }
        });
    JScrollPane aggregatedDemandsScroll = new JScrollPane(aggregatedDemandsTable);
    demandsPanel.add(aggregatedDemandsScroll, c);
    c.gridy++;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;

    JPanel packButtonPanel = new JPanel();
    packButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    unpackButton = new JButton("Unpack");
    unpackButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/box_out.png")));
    unpackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int row : aggregatedDemandsTable.getSelectedRows()) {
          getManifest().unpackDemand(aggregatedDemandsTable.getDemand(row));
        }
        updateView();
      }
    });
    unpackButton.setEnabled(false);
    packButtonPanel.add(unpackButton);
    autoPackButton = new JButton("Auto-Pack");
    autoPackButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/box_lightning.png")));
    autoPackButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int row : aggregatedDemandsTable.getSelectedRows()) {
          getManifest().autoPackDemand(aggregatedDemandsTable.getDemand(row));
        }
        updateView();
      }
    });
    autoPackButton.setEnabled(false);
    packButtonPanel.add(autoPackButton);
    demandsPanel.add(packButtonPanel, c);

    c.gridy++;
    c.fill = GridBagConstraints.HORIZONTAL;
    demandsPanel.add(buildExportPanel(), c);

    return demandsPanel;
  }

  /**
   * Write file.
   */
  private void writeFile() {
    String filePath =
        directoryPathText.getText() + System.getProperty("file.separator") + fileNameText.getText();
    char delimiter = ',';
    if (delimiterCombo.getSelectedItem() == COMMA_DELIMITED) {
      delimiter = ',';
    } else if (delimiterCombo.getSelectedItem() == SEMICOLON_DELIMITED) {
      delimiter = ';';
    } else if (delimiterCombo.getSelectedItem() == TAB_DELIMITED) {
      delimiter = '\t';
    }
    try {
      File file = new File(filePath);
      if (file.exists() && !overwriteCheck.isSelected()) {
        int answer = JOptionPane.showOptionDialog(getThis(),
            "Overwrite existing file " + fileNameText.getText() + "?", "SpaceNet Warning",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        if (answer == JOptionPane.NO_OPTION)
          return;
        else
          overwriteCheck.setSelected(true);
      }
      BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

      if (aggregationCombo.getSelectedItem() == NO_AGGREGATION) {
        out.write("Origin" + delimiter + "Departure" + delimiter + "Destination" + delimiter
            + "Arrival" + delimiter + "Carrier" + delimiter + "Cargo Environment" + delimiter
            + "Cargo Mass Available" + delimiter + "Cargo Volume Available" + delimiter
            + "Container" + delimiter + "Environment" + delimiter + "Mass" + delimiter + "Volume"
            + delimiter + "Cargo Environment" + delimiter + "Cargo Mass Available" + delimiter
            + "Cargo Volume Available" + delimiter + "Resource" + delimiter + "Environment"
            + delimiter + "Mass" + delimiter + "Volume" + System.getProperty("line.separator"));
        for (SupplyEdge edge : this.getManifest().getSupplyEdges()) {
          for (I_Carrier carrier : edge.getCarriers()) {
            for (I_ResourceContainer container : this.getManifest().getManifestedContainers(edge,
                carrier)) {
              for (Demand demand : this.getManifest().getPackedDemands(container)) {
                if (referenceCombo.getSelectedItem() == NAME_OUTPUT) {
                  out.write(edge.getOrigin().getName() + delimiter + edge.getStartTime() + delimiter
                      + edge.getDestination().getName() + delimiter + edge.getEndTime() + delimiter
                      + carrier.getName() + delimiter + carrier.getCargoEnvironment() + delimiter
                      + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                      + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                      + container.getName() + delimiter + container.getEnvironment() + delimiter
                      + container.getMass() + delimiter + container.getVolume() + delimiter
                      + container.getCargoEnvironment() + delimiter
                      + (container.getMaxCargoMass() - container.getCargoMass()) + delimiter
                      + (container.getMaxCargoVolume() - container.getCargoVolume()) + delimiter
                      + demand.getResource().getName() + delimiter
                      + demand.getResource().getEnvironment() + delimiter + demand.getMass()
                      + delimiter + demand.getVolume() + System.getProperty("line.separator"));
                } else {
                  out.write("" + edge.getOrigin().getTid() + delimiter + edge.getStartTime()
                      + delimiter + edge.getDestination().getTid() + delimiter + edge.getEndTime()
                      + delimiter + carrier.getUid() + delimiter + carrier.getCargoEnvironment()
                      + delimiter + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                      + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                      + container.getUid() + delimiter + container.getEnvironment() + delimiter
                      + container.getMass() + delimiter + container.getVolume() + delimiter
                      + container.getCargoEnvironment() + delimiter
                      + (container.getMaxCargoMass() - container.getCargoMass()) + delimiter
                      + (container.getMaxCargoVolume() - container.getCargoVolume()) + delimiter
                      + demand.getResource().getTid() + delimiter
                      + demand.getResource().getEnvironment() + delimiter + demand.getMass()
                      + delimiter + demand.getVolume() + System.getProperty("line.separator"));
                }
              }
            }
          }
        }
      } else if (aggregationCombo.getSelectedItem() == CONTAINER_AGGREGATION) {
        out.write("Origin" + delimiter + "Departure" + delimiter + "Destination" + delimiter
            + "Arrival" + delimiter + "Carrier" + delimiter + "Cargo Environment" + delimiter
            + "Cargo Mass Available" + delimiter + "Cargo Volume Available" + delimiter
            + "Container" + delimiter + "Environment" + delimiter + "Mass" + delimiter + "Volume"
            + delimiter + "Cargo Environment" + delimiter + "Cargo Mass Available" + delimiter
            + "Cargo Volume Available" + delimiter + "Packed Mass" + delimiter + "Packed Volume"
            + System.getProperty("line.separator"));
        for (SupplyEdge edge : this.getManifest().getSupplyEdges()) {
          for (I_Carrier carrier : edge.getCarriers()) {
            for (I_ResourceContainer container : this.getManifest().getManifestedContainers(edge,
                carrier)) {
              double packedMass = 0;
              double packedVolume = 0;
              for (Demand demand : this.getManifest().getPackedDemands(container)) {
                packedMass += demand.getMass();
                packedVolume += demand.getVolume();
              }
              if (referenceCombo.getSelectedItem() == NAME_OUTPUT) {
                out.write(edge.getOrigin().getName() + delimiter + edge.getStartTime() + delimiter
                    + edge.getDestination().getName() + delimiter + edge.getEndTime() + delimiter
                    + carrier.getName() + delimiter + carrier.getCargoEnvironment() + delimiter
                    + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                    + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                    + container.getName() + delimiter + container.getEnvironment() + delimiter
                    + container.getMass() + delimiter + container.getVolume() + delimiter
                    + container.getCargoEnvironment() + delimiter
                    + (container.getMaxCargoMass() - container.getCargoMass()) + delimiter
                    + (container.getMaxCargoVolume() - container.getCargoVolume()) + delimiter
                    + packedMass + delimiter + packedVolume + System.getProperty("line.separator"));
              } else {
                out.write("" + edge.getOrigin().getTid() + delimiter + edge.getStartTime()
                    + delimiter + edge.getDestination().getTid() + delimiter + edge.getEndTime()
                    + delimiter + carrier.getUid() + delimiter + carrier.getCargoEnvironment()
                    + delimiter + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                    + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                    + container.getUid() + delimiter + container.getEnvironment() + delimiter
                    + container.getMass() + delimiter + container.getVolume() + delimiter
                    + container.getCargoEnvironment() + delimiter
                    + (container.getMaxCargoMass() - container.getCargoMass()) + delimiter
                    + (container.getMaxCargoVolume() - container.getCargoVolume()) + delimiter
                    + packedMass + delimiter + packedVolume + System.getProperty("line.separator"));
              }
            }
          }
        }
      } else if (aggregationCombo.getSelectedItem() == CARRIER_AGGREGATION) {
        out.write("Origin" + delimiter + "Departure" + delimiter + "Destination" + delimiter
            + "Arrival" + delimiter + "Carrier" + delimiter + "Cargo Environment" + delimiter
            + "Cargo Mass Available" + delimiter + "Cargo Volume Available" + delimiter
            + "Manifested Mass" + delimiter + "Manifested Volume"
            + System.getProperty("line.separator"));
        for (SupplyEdge edge : this.getManifest().getSupplyEdges()) {
          for (I_Carrier carrier : edge.getCarriers()) {
            double manifestedMass = 0;
            double manifestedVolume = 0;
            for (I_ResourceContainer container : this.getManifest().getManifestedContainers(edge,
                carrier)) {
              manifestedMass += container.getMass();
              manifestedVolume += container.getVolume();
              for (Demand demand : this.getManifest().getPackedDemands(container)) {
                manifestedMass += demand.getMass();
              }
            }
            if (referenceCombo.getSelectedItem() == NAME_OUTPUT) {
              out.write(edge.getOrigin().getName() + delimiter + edge.getStartTime() + delimiter
                  + edge.getDestination().getName() + delimiter + edge.getEndTime() + delimiter
                  + carrier.getName() + delimiter + carrier.getCargoEnvironment() + delimiter
                  + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                  + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                  + manifestedMass + delimiter + manifestedVolume
                  + System.getProperty("line.separator"));
            } else {
              out.write("" + edge.getOrigin().getTid() + delimiter + edge.getStartTime() + delimiter
                  + edge.getDestination().getTid() + delimiter + edge.getEndTime() + delimiter
                  + carrier.getUid() + delimiter + carrier.getCargoEnvironment() + delimiter
                  + (carrier.getMaxCargoMass() - carrier.getCargoMass()) + delimiter
                  + (carrier.getMaxCargoVolume() - carrier.getCargoVolume()) + delimiter
                  + manifestedMass + delimiter + manifestedVolume
                  + System.getProperty("line.separator"));
            }
          }
        }
      } else if (aggregationCombo.getSelectedItem() == TRANSPORT_AGGREGATION) {
        out.write("Origin" + delimiter + "Departure" + delimiter + "Destination" + delimiter
            + "Arrival" + delimiter + "Cargo Mass Available" + delimiter + "Cargo Volume Available"
            + delimiter + "Manifested Mass" + delimiter + "Manifested Volume"
            + System.getProperty("line.separator"));
        for (SupplyEdge edge : this.getManifest().getSupplyEdges()) {
          double cargoMass = 0;
          double cargoVolume = 0;
          double manifestedMass = 0;
          double manifestedVolume = 0;
          for (I_Carrier carrier : edge.getCarriers()) {
            cargoMass += (carrier.getMaxCargoMass() - carrier.getCargoMass());
            cargoVolume += (carrier.getMaxCargoVolume() - carrier.getCargoVolume());

            for (I_ResourceContainer container : this.getManifest().getManifestedContainers(edge,
                carrier)) {
              manifestedMass += container.getMass();
              manifestedVolume += container.getVolume();
              for (Demand demand : this.getManifest().getPackedDemands(container)) {
                manifestedMass += demand.getMass();
              }
            }
          }
          if (referenceCombo.getSelectedItem() == NAME_OUTPUT) {
            out.write(edge.getOrigin().getName() + delimiter + edge.getStartTime() + delimiter
                + edge.getDestination().getName() + delimiter + edge.getEndTime() + delimiter
                + cargoMass + delimiter + cargoVolume + delimiter + manifestedMass + delimiter
                + manifestedVolume + System.getProperty("line.separator"));
          } else {
            out.write("" + edge.getOrigin().getTid() + delimiter + edge.getStartTime() + delimiter
                + edge.getDestination().getTid() + delimiter + edge.getEndTime() + delimiter
                + cargoMass + delimiter + cargoVolume + delimiter + manifestedMass + delimiter
                + manifestedVolume + System.getProperty("line.separator"));
          }
        }
      }
      out.close();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(this,
          "An error of type \"" + ex.getClass().getSimpleName()
              + "\" occurred while exporting the demands",
          "SpaceNet Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private JPanel buildExportPanel() {
    directoryChooser = new JFileChooser();
    directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    JPanel exportPanel = new JPanel();
    exportPanel.setBorder(BorderFactory.createTitledBorder("Export Manifest"));
    exportPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(2, 2, 2, 2);
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    exportPanel.add(new JLabel("Directory: "), c);
    c.gridy++;
    exportPanel.add(new JLabel("File Name: "), c);
    c.gridy += 2;
    exportPanel.add(new JLabel("Aggregation: "), c);
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
    browseButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/folder_explore.png")));
    browseButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (directoryPathText.getText() == "") {
          directoryChooser
              .setCurrentDirectory(new File(SpaceNetSettings.getInstance().getDefaultDirectory()));
        }
        int returnVal = directoryChooser.showOpenDialog(getThis());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          directoryPathText.setText(directoryChooser.getSelectedFile().getAbsolutePath());
        }
        exportButton.setEnabled(directoryPathText.getText() != "" && fileNameText.getText() != "");
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
        exportButton.setEnabled(directoryPathText.getText() != "" && fileNameText.getText() != "");
      }
    });
    exportPanel.add(fileNameText, c);
    c.gridy++;
    overwriteCheck = new JCheckBox("Overwrite existing files", false);
    exportPanel.add(overwriteCheck, c);
    c.gridy++;
    aggregationCombo = new JComboBox<String>();
    aggregationCombo.addItem(NO_AGGREGATION);
    aggregationCombo.addItem(CONTAINER_AGGREGATION);
    aggregationCombo.addItem(CARRIER_AGGREGATION);
    aggregationCombo.addItem(TRANSPORT_AGGREGATION);
    exportPanel.add(aggregationCombo, c);
    c.gridy++;
    referenceCombo = new JComboBox<String>();
    referenceCombo.addItem(NAME_OUTPUT);
    referenceCombo.addItem(ID_OUTPUT);
    exportPanel.add(referenceCombo, c);
    c.gridy++;
    delimiterCombo = new JComboBox<String>();
    delimiterCombo.addItem(TAB_DELIMITED);
    delimiterCombo.addItem(COMMA_DELIMITED);
    delimiterCombo.addItem(SEMICOLON_DELIMITED);
    exportPanel.add(delimiterCombo, c);
    c.gridy++;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    exportButton = new JButton("Export",
        new ImageIcon(getClass().getClassLoader().getResource("icons/page_white_edit.png")));
    exportButton.setEnabled(false);
    exportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exportManifest();
      }
    });
    exportPanel.add(exportButton, c);
    return exportPanel;
  }

  /**
   * Gets this.
   * 
   * @return this
   */
  private ManifestTab getThis() {
    return this;
  }


  /**
   * Builds the packing panel.
   * 
   * @return the packing panel
   */
  private JPanel buildPackingPanel() {
    JPanel packingPanel = new JPanel();
    packingPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    packingPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 4;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    packingPanel.add(new JLabel("Available Logistics Containers"), c);
    c.gridy++;
    c.weighty = 1;
    packedDemandsTable = new PackedDemandsTable(this);
    packedDemandsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (packedDemandsTable.getSelectedRow() >= 0) {
          // manifestedDemandsTable.clearSelection();
        }
        updateContainer();
        updateButtons();
      }
    });
    JScrollPane packedDemandsScroll = new JScrollPane(packedDemandsTable);
    packingPanel.add(packedDemandsScroll, c);
    c.gridy++;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel containerButtonPanel = new JPanel();
    containerButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    DropDownButton addContainerButton = new AddContainerButton(this);
    containerButtonPanel.add(addContainerButton);
    editContainerButton = new JButton("Edit");
    editContainerButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/package_edit.png")));
    editContainerButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // TODO add edit container dialog
      }
    });
    editContainerButton.setEnabled(false);
    containerButtonPanel.add(editContainerButton);
    removeContainerButton = new JButton("Remove");
    removeContainerButton.setIcon(
        new ImageIcon(getClass().getClassLoader().getResource("icons/package_delete.png")));
    removeContainerButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HashSet<I_ResourceContainer> containers = new HashSet<I_ResourceContainer>();
        for (int row : packedDemandsTable.getSelectedRows()) {
          containers.add(packedDemandsTable.getContainer(row));
        }
        for (I_ResourceContainer container : containers)
          getManifest().removeContainer(container);
        packedDemandsTable.getSelectionModel().clearSelection();
        updateView();
      }
    });
    removeContainerButton.setEnabled(false);
    containerButtonPanel.add(removeContainerButton);
    packingPanel.add(containerButtonPanel, c);
    c.gridy++;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    c.weightx = 0;
    c.gridwidth = 1;
    packingPanel.add(new JLabel("Name: "), c);
    c.gridx += 2;
    packingPanel.add(new JLabel("Environment: "), c);
    c.gridx -= 2;
    c.gridy++;
    packingPanel.add(new JLabel("Total Mass: "), c);
    c.gridx += 2;
    packingPanel.add(new JLabel("Total Volume: "), c);
    c.gridx -= 2;
    c.gridy++;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    packingPanel.add(new JLabel("Contents: "), c);
    c.anchor = GridBagConstraints.LINE_END;
    c.gridy += 2;
    packingPanel.add(new JLabel("Environment: "), c);
    c.gridy++;
    packingPanel.add(new JLabel("Cargo Mass: "), c);
    c.gridy++;
    packingPanel.add(new JLabel("Cargo Volume: "), c);
    c.gridx++;
    c.weightx = 1;
    c.gridy -= 6;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    containerNameLabel = new JLabel();
    packingPanel.add(containerNameLabel, c);
    c.gridx += 2;
    containerEnvironmentLabel = new JLabel();
    packingPanel.add(containerEnvironmentLabel, c);
    c.gridx -= 2;
    c.gridy++;
    containerMassLabel = new JLabel();
    packingPanel.add(containerMassLabel, c);
    c.gridx += 2;
    containerVolumeLabel = new JLabel();
    packingPanel.add(containerVolumeLabel, c);
    c.gridx -= 2;
    c.gridy++;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weighty = 0.4;
    c.gridwidth = 3;
    containerContentsTable = new ContainerContentsTable(this);
    containerContentsTable.getSelectionModel()
        .addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            if (containerContentsTable.getSelectedRowCount() == 1) {
              for (int row = 0; row < aggregatedDemandsTable.getRowCount(); row++) {
                if (aggregatedDemandsTable.getDemand(row)
                    .equals(getManifest().getDemand(containerContentsTable.getSelectedDemand()))) {
                  aggregatedDemandsTable.getSelectionModel().setSelectionInterval(row, row);
                  JViewport viewport = (JViewport) aggregatedDemandsTable.getParent();
                  Rectangle rect = aggregatedDemandsTable.getCellRect(row, 0, true);
                  Point pt = viewport.getViewPosition();
                  rect.setLocation(rect.x - pt.x, rect.y - pt.y);
                  viewport.scrollRectToVisible(rect);
                  break;
                }
              }
            }
            updateButtons();
          }
        });
    JScrollPane containerContentsScroll = new JScrollPane(containerContentsTable);
    packingPanel.add(containerContentsScroll, c);
    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    c.weighty = 0;
    unpackContentsButton = new JButton("Unpack");
    unpackContentsButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/box_out.png")));
    unpackContentsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HashSet<Demand> demands = new HashSet<Demand>();
        for (int row : containerContentsTable.getSelectedRows()) {
          demands.add(containerContentsTable.getDemand(row));
        }
        for (Demand demand : demands) {
          getManifest().unpackDemand(demand, packedDemandsTable.getSelectedContainer());
        }
        updateView();
      }
    });
    unpackContentsButton.setEnabled(false);
    packingPanel.add(unpackContentsButton, c);
    c.gridy++;
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.LINE_START;
    c.gridwidth = 3;
    containerCargoEnvironmentLabel = new JLabel();
    packingPanel.add(containerCargoEnvironmentLabel, c);
    c.gridy++;
    containerMassCapacity = new JProgressBar(0, 100);
    containerMassCapacity.setStringPainted(true);
    containerMassCapacity.setString("");
    packingPanel.add(containerMassCapacity, c);
    c.gridy++;
    containerVolumeCapacity = new JProgressBar(0, 100);
    containerVolumeCapacity.setStringPainted(true);
    containerVolumeCapacity.setString("");
    packingPanel.add(containerVolumeCapacity, c);
    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 4;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel packButtonPanel = new JPanel();
    packButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    packButton = new JButton("Pack");
    packButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/box_in.png")));
    packButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int row : aggregatedDemandsTable.getSelectedRows()) {
          getManifest().packDemand(aggregatedDemandsTable.getDemand(row),
              packedDemandsTable.getSelectedContainer());
        }
        updateView();
      }
    });
    packButton.setEnabled(false);
    packButtonPanel.add(packButton);
    unmanifestButton = new JButton("Unmanifest");
    unmanifestButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/book_delete.png")));
    unmanifestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ArrayList<I_ResourceContainer> containers = new ArrayList<I_ResourceContainer>();
        ArrayList<SupplyEdge> edges = new ArrayList<SupplyEdge>();
        for (int row : packedDemandsTable.getSelectedRows()) {
          containers.add(packedDemandsTable.getContainer(row));
          edges.add(packedDemandsTable.getSupplyEdge(row));
        }
        for (int i = 0; i < containers.size(); i++) {
          getManifest().unmanifestContainer(containers.get(i), edges.get(i));
        }
        updateView();
      }
    });
    unmanifestButton.setEnabled(false);
    packButtonPanel.add(unmanifestButton);
    packingPanel.add(packButtonPanel, c);
    return packingPanel;
  }

  /**
   * Builds the manifesting panel.
   * 
   * @return the manifesting panel
   */
  private JPanel buildManifestingPanel() {
    JPanel manifestingPanel = new JPanel();
    manifestingPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    manifestingPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    manifestingPanel.add(new JLabel("Available Carriers"), c);
    c.gridy++;
    c.weighty = 1;
    manifestedDemandsTable = new ManifestedDemandsTable(this);
    manifestedDemandsTable.getSelectionModel()
        .addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent e) {
            if (manifestedDemandsTable.getSelectedRow() >= 0
                && aggregatedDemandsTable.getSelectedRow() >= 0
                && packedDemandsTable.getSelectedRow() < 0) {
              // aggregatedDemandsTable.clearSelection();
            }
            updateCarrier();
            updateButtons();
          }
        });
    JScrollPane manifestedDemandsScroll = new JScrollPane(manifestedDemandsTable);
    manifestingPanel.add(manifestedDemandsScroll, c);
    c.gridy++;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    c.weightx = 0;
    c.weighty = 0;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    manifestingPanel.add(new JLabel("Carriers: "), c);
    c.gridy++;
    manifestingPanel.add(new JLabel("Containers: "), c);
    c.anchor = GridBagConstraints.LINE_END;
    c.gridy += 2;
    manifestingPanel.add(new JLabel("Environment: "), c);
    c.gridy++;
    manifestingPanel.add(new JLabel("Cargo Mass: "), c);
    c.gridy++;
    manifestingPanel.add(new JLabel("Cargo Volume: "), c);
    c.gridx++;
    c.weightx = 1;
    c.gridy -= 5;
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.weighty = 0.4;
    carrierTree = new ElementTree();
    carrierTree.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    carrierTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        updateElement();
        updateButtons();
      }
    });
    JScrollPane carrierTreeScroll = new JScrollPane(carrierTree);
    carrierTreeScroll.setPreferredSize(new Dimension(150, 300));
    manifestingPanel.add(carrierTreeScroll, c);
    c.gridy++;
    carrierContentsTable = new CarrierContentsTable(this);
    carrierContentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (carrierContentsTable.getSelectedRowCount() == 1) {
          for (int row = 0; row < packedDemandsTable.getRowCount(); row++) {
            if (manifestedDemandsTable.getSelectedSupplyEdge()
                .equals(packedDemandsTable.getSupplyEdge(row))
                && carrierContentsTable.getSelectedContainer()
                    .equals(packedDemandsTable.getContainer(row))) {
              packedDemandsTable.getSelectionModel().setSelectionInterval(row, row);
              JViewport viewport = (JViewport) packedDemandsTable.getParent();
              Rectangle rect = packedDemandsTable.getCellRect(row, 0, true);
              Point pt = viewport.getViewPosition();
              rect.setLocation(rect.x - pt.x, rect.y - pt.y);
              viewport.scrollRectToVisible(rect);
              break;
            }
          }
        }
        updateButtons();
      }
    });
    JScrollPane carrierContentsScroll = new JScrollPane(carrierContentsTable);
    manifestingPanel.add(carrierContentsScroll, c);
    c.gridy++;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    c.weighty = 0;
    unmanifestContentsButton = new JButton("Unmanifest");
    unmanifestContentsButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/book_delete.png")));
    unmanifestContentsButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HashSet<I_ResourceContainer> containers = new HashSet<I_ResourceContainer>();
        for (int row : carrierContentsTable.getSelectedRows()) {
          containers.add(carrierContentsTable.getContainer(row));
        }
        for (I_ResourceContainer container : containers) {
          getManifest().unmanifestContainer(container,
              manifestedDemandsTable.getSelectedSupplyEdge());
        }
        updateView();
      }
    });
    unmanifestContentsButton.setEnabled(false);
    manifestingPanel.add(unmanifestContentsButton, c);
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    c.gridy++;
    carrierEnvironmentLabel = new JLabel();
    manifestingPanel.add(carrierEnvironmentLabel, c);
    c.gridy++;
    carrierMassCapacity = new JProgressBar(0, 100);
    carrierMassCapacity.setStringPainted(true);
    carrierMassCapacity.setString("");
    manifestingPanel.add(carrierMassCapacity, c);
    c.gridy++;
    carrierVolumeCapacity = new JProgressBar(0, 100);
    carrierVolumeCapacity.setStringPainted(true);
    carrierVolumeCapacity.setString("");
    manifestingPanel.add(carrierVolumeCapacity, c);
    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel manifestingButtonPanel = new JPanel();
    manifestingButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    manifestButton = new JButton("Manifest");
    manifestButton
        .setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/book_add.png")));
    manifestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ArrayList<I_ResourceContainer> containers = new ArrayList<I_ResourceContainer>();

        for (int row : packedDemandsTable.getSelectedRows()) {
          containers.add(packedDemandsTable.getContainer(row));
        }
        for (int i = 0; i < containers.size(); i++) {
          getManifest().manifestContainer(containers.get(i),
              manifestedDemandsTable.getSelectedSupplyEdge(),
              (I_Carrier) carrierTree.getSelection());
        }
        updateView();
      }
    });
    manifestButton.setEnabled(false);
    manifestingButtonPanel.add(manifestButton);
    manifestingPanel.add(manifestingButtonPanel, c);
    return manifestingPanel;
  }

  /**
   * Initializes the panel for a new scenario.
   */
  public void initialize() {
    resetButton.setEnabled(getScenario() != null);
    autoManifestButton.setEnabled(getScenario() != null);
    unpackButton.setEnabled(getScenario() != null);
    autoPackButton.setEnabled(getScenario() != null);

    if (getScenario() != null) {
      simulator = new DemandSimulator(getScenario());
      simulator.setPackingDemandsAdded(false);
      simulator.setItemsRepaired(true);
      simWorker = new SimWorker();
      simWorker.execute();
    }
    updateView();
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
   * Gets the getManifest().
   * 
   * @return the manifest
   */
  public Manifest getManifest() {
    return getScenario().getManifest();
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
   * Update view.
   */
  public void updateView() {
    int selectedAggregatedRow = aggregatedDemandsTable.getSelectedRows().length == 1
        ? aggregatedDemandsTable.getSelectedRow()
        : -1;
    int selectedPackedRow =
        packedDemandsTable.getSelectedRows().length == 1 ? packedDemandsTable.getSelectedRow() : -1;
    int selectedContainerContentsRow =
        containerContentsTable.getSelectedRowCount() == 1 ? containerContentsTable.getSelectedRow()
            : -1;
    int selectedManifestedRow = manifestedDemandsTable.getSelectedRows().length == 1
        ? manifestedDemandsTable.getSelectedRow()
        : -1;
    int selectedCarrierRow =
        carrierTree.getSelectionCount() == 1 ? carrierTree.getSelectionRows()[0] : -1;
    int selectedCarrierContentsRow =
        carrierContentsTable.getSelectedRowCount() == 1 ? carrierContentsTable.getSelectedRow()
            : -1;
    aggregatedDemandsTable.updateView();
    packedDemandsTable.updateView();
    manifestedDemandsTable.updateView();
    aggregatedDemandsTable.getSelectionModel().setSelectionInterval(selectedAggregatedRow,
        selectedAggregatedRow);
    if (selectedPackedRow < packedDemandsTable.getRowCount())
      packedDemandsTable.getSelectionModel().setSelectionInterval(selectedPackedRow,
          selectedPackedRow);
    if (selectedContainerContentsRow < containerContentsTable.getRowCount())
      containerContentsTable.getSelectionModel().setSelectionInterval(selectedContainerContentsRow,
          selectedContainerContentsRow);
    manifestedDemandsTable.getSelectionModel().setSelectionInterval(selectedManifestedRow,
        selectedManifestedRow);
    if (selectedCarrierRow < carrierTree.getRowCount())
      carrierTree.setSelectionRow(selectedCarrierRow);
    if (selectedCarrierContentsRow < carrierContentsTable.getRowCount())
      carrierContentsTable.getSelectionModel().setSelectionInterval(selectedCarrierContentsRow,
          selectedCarrierContentsRow);
    updateButtons();
  }

  private void updateButtons() {
    if (aggregatedDemandsTable.getSelectedRowCount() > 0) {
      for (int row : aggregatedDemandsTable.getSelectedRows()) {
        unpackButton
            .setEnabled(getManifest().canUnpackDemand(aggregatedDemandsTable.getDemand(row)));
        if (unpackButton.isEnabled())
          break;
      }
    } else
      unpackButton.setEnabled(false);

    if (aggregatedDemandsTable.getSelectedRowCount() > 0) {
      for (int row : aggregatedDemandsTable.getSelectedRows()) {
        autoPackButton
            .setEnabled(getManifest().canAutoPackDemand(aggregatedDemandsTable.getDemand(row)));
        if (autoPackButton.isEnabled())
          break;
      }
    } else
      autoPackButton.setEnabled(false);

    removeContainerButton.setEnabled(packedDemandsTable.getSelectedRowCount() > 0);

    if (containerContentsTable.getSelectedRowCount() > 0
        && packedDemandsTable.getSelectedRowCount() == 1) {
      for (int row : containerContentsTable.getSelectedRows()) {
        unpackContentsButton.setEnabled(getManifest().canUnpackDemand(
            containerContentsTable.getDemand(row), packedDemandsTable.getSelectedContainer()));
        if (unpackContentsButton.isEnabled())
          break;
      }
    } else
      unpackContentsButton.setEnabled(false);

    if (aggregatedDemandsTable.getSelectedRowCount() > 0
        && packedDemandsTable.getSelectedRowCount() == 1) {
      for (int row : aggregatedDemandsTable.getSelectedRows()) {
        packButton.setEnabled(getManifest().canPackDemand(aggregatedDemandsTable.getDemand(row),
            packedDemandsTable.getSelectedContainer()));
        if (packButton.isEnabled())
          break;
      }
    } else
      packButton.setEnabled(false);

    if (packedDemandsTable.getSelectedRowCount() > 0) {
      for (int row : packedDemandsTable.getSelectedRows()) {
        unmanifestButton.setEnabled(getManifest().canUnmanifestContainer(
            packedDemandsTable.getContainer(row), packedDemandsTable.getSupplyEdge(row)));
        if (unmanifestButton.isEnabled())
          break;
      }
    } else
      unmanifestButton.setEnabled(false);

    if (carrierContentsTable.getSelectedRowCount() > 0
        && manifestedDemandsTable.getSelectedRowCount() == 1)
      for (int row : carrierContentsTable.getSelectedRows()) {
        unmanifestContentsButton
            .setEnabled(getManifest().canUnmanifestContainer(carrierContentsTable.getContainer(row),
                manifestedDemandsTable.getSelectedSupplyEdge()));
        if (unmanifestContentsButton.isEnabled())
          break;
      }
    else
      unmanifestContentsButton.setEnabled(false);

    if (packedDemandsTable.getSelectedRowCount() > 0
        && manifestedDemandsTable.getSelectedRowCount() == 1 && carrierTree.getSelectionCount() == 1
        && carrierTree.getSelection() instanceof I_Carrier)
      for (int row : packedDemandsTable.getSelectedRows()) {
        manifestButton.setEnabled(getManifest().canManifestContainer(
            packedDemandsTable.getContainer(row), manifestedDemandsTable.getSelectedSupplyEdge(),
            (I_Carrier) carrierTree.getSelection()));
        if (manifestButton.isEnabled())
          break;
      }
    else
      manifestButton.setEnabled(false);
  }

  private void updateContainer() {
    if (packedDemandsTable.getSelectedRowCount() == 1) {
      I_ResourceContainer container =
          packedDemandsTable.getContainer(packedDemandsTable.getSelectedRow());
      SupplyPoint point = packedDemandsTable.getSupplyPoint(packedDemandsTable.getSelectedRow());
      double cargoMass = getManifest().getCargoMass(container, point);
      double cargoVolume = getManifest().getCargoVolume(container, point);

      DecimalFormat massFormat = new DecimalFormat("0.00");
      DecimalFormat volumeFormat = new DecimalFormat("0.000");
      containerNameLabel.setText(container.getName());
      containerEnvironmentLabel.setText(container.getEnvironment().toString());
      containerMassLabel.setText(massFormat.format(container.getMass() + cargoMass) + " kg");
      containerVolumeLabel.setText(volumeFormat.format(container.getVolume()) + " m^3");
      containerContentsTable.setContainer(container, point);
      containerCargoEnvironmentLabel.setText(container.getCargoEnvironment().toString());

      if (cargoMass
          - container.getMaxCargoMass() > GlobalParameters.getSingleton().getMassPrecision() / 2d)
        containerMassCapacity.setForeground(new Color(153, 0, 0));
      else
        containerMassCapacity.setForeground(new Color(0, 153, 0));
      if (container.getMaxCargoMass() == 0)
        containerMassCapacity.setValue(100);
      else
        containerMassCapacity.setValue((int) (100 * cargoMass / container.getMaxCargoMass()));
      containerMassCapacity.setString(massFormat.format(cargoMass) + " / "
          + massFormat.format(container.getMaxCargoMass()) + " kg");

      if (cargoVolume - container.getMaxCargoVolume() > GlobalParameters.getSingleton()
          .getVolumePrecision() / 2d)
        containerVolumeCapacity.setForeground(new Color(153, 0, 0));
      else
        containerVolumeCapacity.setForeground(new Color(0, 153, 0));
      if (container.getMaxCargoVolume() == 0)
        containerVolumeCapacity.setValue(100);
      else
        containerVolumeCapacity.setValue((int) (100 * cargoVolume / container.getMaxCargoVolume()));
      containerVolumeCapacity.setString(volumeFormat.format(cargoVolume) + " / "
          + volumeFormat.format(container.getMaxCargoVolume()) + " m^3");
    } else {
      containerNameLabel.setText(null);
      containerEnvironmentLabel.setText(null);
      containerMassLabel.setText(null);
      containerVolumeLabel.setText(null);
      containerContentsTable.setContainer(null, null);
      containerCargoEnvironmentLabel.setText(null);
      containerMassCapacity.setValue(0);
      containerMassCapacity.setString("");
      containerVolumeCapacity.setValue(0);
      containerVolumeCapacity.setString("");
    }
  }

  private void updateCarrier() {
    if (manifestedDemandsTable.getSelectedRowCount() == 1) {
      I_Carrier superCarrier =
          manifestedDemandsTable.getCarrier(manifestedDemandsTable.getSelectedRow());
      carrierTree.setRoot(superCarrier);
      carrierTree.setRootVisible(true);
      carrierTree.setSelection(superCarrier);
    } else {
      carrierTree.setRoot(null);
      carrierTree.setRootVisible(false);
      carrierEnvironmentLabel.setText(null);
      carrierMassCapacity.setValue(0);
      carrierMassCapacity.setString("");
      carrierVolumeCapacity.setValue(0);
      carrierVolumeCapacity.setString("");
    }
  }

  private void updateElement() {
    if (carrierTree.getSelectionCount() > 0 && carrierTree.getSelection() instanceof I_Carrier) {
      SupplyEdge edge =
          manifestedDemandsTable.getSupplyEdge(manifestedDemandsTable.getSelectedRow());
      I_Carrier carrier = (I_Carrier) carrierTree.getSelection();

      carrierContentsTable.setCarrier(edge, carrier);

      double cargoMass = getManifest().getCargoMass(carrier, edge.getPoint());
      double cargoVolume = getManifest().getCargoVolume(carrier, edge.getPoint());

      DecimalFormat massFormat = new DecimalFormat("0.00");
      carrierEnvironmentLabel.setText(carrier.getCargoEnvironment().toString());
      if (cargoMass - carrier.getMaxCargoMass() > GlobalParameters.getSingleton().getMassPrecision()
          / 2d)
        carrierMassCapacity.setForeground(new Color(153, 0, 0));
      else
        carrierMassCapacity.setForeground(new Color(0, 153, 0));
      if (carrier.getMaxCargoMass() == 0)
        carrierMassCapacity.setValue(100);
      else
        carrierMassCapacity.setValue((int) (100 * cargoMass / carrier.getMaxCargoMass()));
      carrierMassCapacity.setString(massFormat.format(cargoMass) + " / "
          + massFormat.format(carrier.getMaxCargoMass()) + " kg");

      DecimalFormat volumeFormat = new DecimalFormat("0.000");
      if (cargoVolume
          - carrier.getMaxCargoVolume() > GlobalParameters.getSingleton().getVolumePrecision() / 2d)
        carrierVolumeCapacity.setForeground(new Color(153, 0, 0));
      else
        carrierVolumeCapacity.setForeground(new Color(0, 153, 0));
      if (carrier.getMaxCargoVolume() == 0)
        carrierVolumeCapacity.setValue(100);
      else
        carrierVolumeCapacity.setValue((int) (100 * cargoVolume / carrier.getMaxCargoVolume()));
      carrierVolumeCapacity.setString(volumeFormat.format(cargoVolume) + " / "
          + volumeFormat.format(carrier.getMaxCargoVolume()) + " m^3");
    } else {
      carrierEnvironmentLabel.setText(null);
      carrierMassCapacity.setValue(0);
      carrierMassCapacity.setString("");
      carrierVolumeCapacity.setValue(0);
      carrierVolumeCapacity.setString("");
      carrierContentsTable.setCarrier(null, null);
    }
  }

  /**
   * A SwingWorker subclass that manages the time-intensive simulation in a separate thread.
   */
  private class SimWorker extends SwingWorker<Void, Void> {
    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    public Void doInBackground() {
      try {
        SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Simulating Demands...");
        scenarioPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        simulator.simulate();
        getManifest().importDemands(simulator);
        scenarioPanel.setCursor(Cursor.getDefaultCursor());
        SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#done()
     */
    protected void done() {
      // initialize demands panel components
      aggregatedDemandsTable.initialize();
      // initialize packing panel components
      packedDemandsTable.initialize();
      containerContentsTable.initialize();
      // initialize manifesting panel components
      manifestedDemandsTable.initialize();
      carrierContentsTable.initialize();
    }
  }

  /**
   * A SwingWorker subclass that manages the time-intensive auto-manifesting in a separate thread.
   */
  private class ManifestWorker extends SwingWorker<Void, Void> {
    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    public Void doInBackground() {
      try {
        SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Auto-Manifesting...");
        scenarioPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getManifest().autoManifest();
        updateView();
        scenarioPanel.setCursor(Cursor.getDefaultCursor());
        SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return null;
    }
  }
}
