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
package edu.mit.spacenet.gui.simulation;

import java.awt.Color;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_ResourceContainer;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.Network;
import edu.mit.spacenet.gui.component.CapacityPanel;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.gui.component.FuelPanel;
import edu.mit.spacenet.gui.visualization.NetworkPanel;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.util.DateFunctions;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Visualization of the network with options to run animation over the course of the simulation.
 * 
 * @author Paul Grogan
 */
public class NetworkHistoryVisualization extends JSplitPane {
  private static final long serialVersionUID = -6405124067606236705L;
  private static int numLabels = 5;
  private static double stepsPerDay = 1 / GlobalParameters.getSingleton().getTimePrecision();

  private SimulationTab tab;
  private DecimalFormat decimalFormat = new DecimalFormat("0.0");

  private JSlider timeSlider;
  private JToggleButton playButton;
  private Animation animation;
  private JButton startButton, rewindButton, stopButton, fastforwardButton, endButton;
  private JSpinner rateSpinner;
  private JCheckBox constantTimeCheck;

  private NetworkPanel networkPanel;
  private JLabel dateLabel, relativeTimeLabel;
  private ElementTree elementTree;
  private CapacityPanel capacityPanel;
  private FuelPanel fuelPanel;
  private JLabel stateLabel, stateValueLabel, massLabel, massValueLabel, volumeLabel,
      volumeValueLabel;

  /**
   * Instantiates a new network history visualization.
   * 
   * @param tab the simulation tab
   */
  public NetworkHistoryVisualization(SimulationTab tab) {
    this.tab = tab;

    JSplitPane visualizationPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JPanel sliderPanel = new JPanel();
    sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
    timeSlider = new JSlider();
    timeSlider.setEnabled(false);
    timeSlider.setFocusable(false);
    timeSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        updateElements();
      }
    });
    sliderPanel.add(timeSlider);
    visualizationPanel.setTopComponent(sliderPanel);

    networkPanel = new NetworkPanel();
    networkPanel.setBackground(Color.BLACK);
    networkPanel.setPreferredSize(new Dimension(600, 400));
    visualizationPanel.setBottomComponent(networkPanel);

    visualizationPanel.setDividerSize(0);
    visualizationPanel.setDividerLocation(75);
    visualizationPanel.setBorder(BorderFactory.createEmptyBorder());
    setLeftComponent(visualizationPanel);

    JPanel controlPanel = new JPanel();
    controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 1;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 2;

    JPanel buttonPanel = new JPanel();
    startButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_start.png")));
    startButton.setMargin(new Insets(0, 0, 0, 0));
    startButton.setEnabled(false);
    startButton.setFocusable(false);
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playButton.isSelected()) {
          playButton.setSelected(false);
        }
        timeSlider.setValue(timeSlider.getMinimum());
      }
    });
    buttonPanel.add(startButton);
    rewindButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_rewind.png")));
    rewindButton.setMargin(new Insets(0, 0, 0, 0));
    rewindButton.setEnabled(false);
    rewindButton.setFocusable(false);
    rewindButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playButton.isSelected()) {
          playButton.setSelected(false);
        }
        timeSlider.setValue(Math.max(timeSlider.getMinimum(), timeSlider.getValue() - 1));
      }
    });
    buttonPanel.add(rewindButton);
    stopButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_stop.png")));
    stopButton.setMargin(new Insets(0, 0, 0, 0));
    stopButton.setEnabled(false);
    stopButton.setFocusable(false);
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playButton.isSelected()) {
          playButton.setSelected(false);
        }
      }
    });
    buttonPanel.add(stopButton);
    playButton = new JToggleButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_play.png")));
    playButton.setSelectedIcon(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_pause.png")));
    playButton.setMargin(new Insets(0, 0, 0, 0));
    playButton.setEnabled(false);
    playButton.setFocusable(false);
    playButton.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          if (timeSlider.getValue() == timeSlider.getMaximum())
            timeSlider.setValue(timeSlider.getMinimum());
          animation = new Animation();
          animation.execute();
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
          animation.cancel(true);
        }
      }
    });
    buttonPanel.add(playButton);
    fastforwardButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_fastforward.png")));
    fastforwardButton.setMargin(new Insets(0, 0, 0, 0));
    fastforwardButton.setEnabled(false);
    fastforwardButton.setFocusable(false);
    fastforwardButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playButton.isSelected()) {
          playButton.setSelected(false);
        }
        timeSlider.setValue(Math.min(timeSlider.getMaximum(), timeSlider.getValue() + 1));
      }
    });
    buttonPanel.add(fastforwardButton);
    endButton = new JButton(
        new ImageIcon(getClass().getClassLoader().getResource("icons/control_end.png")));
    endButton.setMargin(new Insets(0, 0, 0, 0));
    endButton.setEnabled(false);
    endButton.setFocusable(false);
    endButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (playButton.isSelected()) {
          playButton.setSelected(false);
        }
        timeSlider.setValue(timeSlider.getMaximum());
      }
    });
    buttonPanel.add(endButton);
    controlPanel.add(buttonPanel, c);
    c.gridy++;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JLabel rateLabel = new JLabel("Frames per second: ");
    controlPanel.add(rateLabel, c);
    c.gridx++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    rateSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
    controlPanel.add(rateSpinner, c);
    c.gridx--;
    c.gridy++;
    c.gridwidth = 2;
    constantTimeCheck = new JCheckBox("Constant Time per Frame");
    controlPanel.add(constantTimeCheck, c);
    c.gridy++;
    c.weighty = 1;
    JPanel detailsPanel = new JPanel();
    controlPanel.add(detailsPanel, c);
    detailsPanel.setLayout(new GridBagLayout());
    detailsPanel.setBorder(BorderFactory.createTitledBorder("Element Details"));
    c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 2;
    c.weightx = 1;
    dateLabel = new JLabel();
    detailsPanel.add(dateLabel, c);
    c.gridy++;
    relativeTimeLabel = new JLabel();
    detailsPanel.add(relativeTimeLabel, c);
    c.gridy++;
    detailsPanel.add(new JLabel("Elements: "), c);
    c.gridy++;
    c.weighty = 1;
    elementTree = new ElementTree();
    elementTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        updateCapacities();
      }
    });
    JScrollPane elementScroll = new JScrollPane(elementTree);
    elementScroll.setPreferredSize(new Dimension(150, 200));
    detailsPanel.add(elementScroll, c);
    c.gridy++;
    c.weighty = 0;
    c.weightx = 0;
    c.gridwidth = 1;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    stateLabel = new JLabel("State: ");
    stateLabel.setVisible(false);
    detailsPanel.add(stateLabel, c);
    c.gridx++;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;
    stateValueLabel = new JLabel();
    stateValueLabel.setVisible(false);
    detailsPanel.add(stateValueLabel, c);
    c.gridy++;
    c.gridx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    massLabel = new JLabel("Mass: ");
    massLabel.setVisible(false);
    detailsPanel.add(massLabel, c);
    c.gridx++;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;
    massValueLabel = new JLabel();
    massValueLabel.setVisible(false);
    detailsPanel.add(massValueLabel, c);
    c.gridy++;
    c.gridx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    volumeLabel = new JLabel("Volume: ");
    volumeLabel.setVisible(false);
    detailsPanel.add(volumeLabel, c);
    c.gridx++;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;
    volumeValueLabel = new JLabel();
    volumeValueLabel.setVisible(false);
    detailsPanel.add(volumeValueLabel, c);
    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 2;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.CENTER;
    capacityPanel = new CapacityPanel();
    capacityPanel.setVisible(false);
    detailsPanel.add(capacityPanel, c);
    c.gridy++;
    fuelPanel = new FuelPanel();
    fuelPanel.setVisible(false);
    detailsPanel.add(fuelPanel, c);

    controlPanel.setMinimumSize(new Dimension(150, 50));
    setRightComponent(controlPanel);
    setName("Network History");
    setOneTouchExpandable(true);
    setDividerSize(10);
    setBorder(BorderFactory.createEmptyBorder());
    setResizeWeight(1);
    setDividerLocation(700);
  }

  /**
   * Gets the scenario.
   * 
   * @return the scenario
   */
  public Scenario getScenario() {
    return tab.getScenario();
  }

  /**
   * Initialize.
   */
  public void initialize() {
    networkPanel.setNetwork(getScenario().getNetwork());
    updateView();
  }

  /**
   * Update view.
   */
  public void updateView() {
    tab.getScenarioPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    if (tab.getSimulator().getNetworkHistory().size() > 0) {
      timeSlider.setEnabled(true);
      timeSlider.setMinimum(0);
      double maxTime = tab.getSimulator().getTime();
      int maxVal = (int) Math.ceil(maxTime * stepsPerDay);
      while (maxVal % numLabels != 0)
        maxVal++;
      timeSlider.setMaximum(maxVal);
      Hashtable<Integer, JLabel> labelDictionary = new Hashtable<Integer, JLabel>();
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
      for (int i = 0; i <= numLabels; i++) {
        Date date = DateFunctions.getDate(getScenario().getStartDate(),
            maxVal * i / numLabels / stepsPerDay);
        labelDictionary.put(maxVal * i / numLabels, new JLabel(dateFormat.format(date)));
      }
      timeSlider.setLabelTable(labelDictionary);
      timeSlider.setMajorTickSpacing(maxVal / numLabels);
      timeSlider.setMinorTickSpacing(0);
      timeSlider.setPaintTicks(true);
      timeSlider.setPaintLabels(true);
      timeSlider.setValue(timeSlider.getMinimum());

      startButton.setEnabled(true);
      rewindButton.setEnabled(true);
      playButton.setEnabled(true);
      stopButton.setEnabled(true);
      fastforwardButton.setEnabled(true);
      endButton.setEnabled(true);
      updateElements();
    } else {
      timeSlider.setMinimum(0);
      timeSlider.setMaximum(0);
      timeSlider.setLabelTable(null);
      timeSlider.setMajorTickSpacing(0);
      timeSlider.setValue(0);
      timeSlider.setEnabled(false);
      startButton.setEnabled(false);
      rewindButton.setEnabled(false);
      playButton.setEnabled(false);
      stopButton.setEnabled(false);
      fastforwardButton.setEnabled(false);
      endButton.setEnabled(false);
      updateElements();
      updateCapacities();
    }
    tab.getScenarioPanel().setCursor(Cursor.getDefaultCursor());
  }

  private void updateElements() {
    if (tab.getSimulator().getNetworkHistory().size() > 0) {
      Network network;
      double time;
      if (timeSlider.getValue() == timeSlider.getMaximum()) {
        network = tab.getSimulator().getScenario().getNetwork();
        time = tab.getSimulator().getTime();
      } else {
        int i = 0;
        while (tab.getSimulator().getNetworkHistory().get(i + 1).getTime()
            * stepsPerDay <= timeSlider.getValue()
            && i < tab.getSimulator().getNetworkHistory().size() - 2) {
          i++;
        }
        network = tab.getSimulator().getNetworkHistory().get(i).getNetwork();
        time = timeSlider.getValue() / stepsPerDay;
      }
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy kk:mm");
      dateLabel.setText(
          "Date: " + dateFormat.format(DateFunctions.getDate(getScenario().getStartDate(), time)));
      relativeTimeLabel
          .setText("Relative Time: " + GlobalParameters.getSingleton().getRoundedTime(time));
      elementTree.setRoot(network);
      networkPanel.setNetwork(network);
    } else {
      dateLabel.setText("Date: ");
      relativeTimeLabel.setText("Relative Time: ");
      elementTree.setRoot(null);
      networkPanel.setNetwork(tab.getScenario().getNetwork());
    }
    networkPanel.repaint();
  }

  /**
   * Update capacities.
   */
  private void updateCapacities() {
    if (elementTree.getSelection() instanceof I_Element) {
      I_Element element = (I_Element) elementTree.getSelection();
      if (element.getCurrentState() == null) {
        stateValueLabel.setText(null);
        stateValueLabel.setIcon(null);
        stateValueLabel.setVisible(false);
        stateLabel.setVisible(false);
      } else {
        stateValueLabel.setText(element.getCurrentState().toString());
        stateValueLabel.setIcon(element.getCurrentState().getStateType().getIcon());
        stateValueLabel.setVisible(true);
        stateLabel.setVisible(true);
      }
      massValueLabel.setText(decimalFormat.format(element.getMass()) + " kg");
      volumeValueLabel.setText(decimalFormat.format(element.getVolume()) + " m^3");
      massLabel.setVisible(true);
      massValueLabel.setVisible(true);
      if (GlobalParameters.getSingleton().isVolumeConstrained()) {
        volumeLabel.setVisible(true);
        volumeValueLabel.setVisible(true);
      } else {
        volumeLabel.setVisible(false);
        volumeValueLabel.setVisible(false);
      }
    } else {
      stateValueLabel.setText(null);
      stateValueLabel.setIcon(null);
      stateValueLabel.setVisible(false);
      massValueLabel.setVisible(false);
      massLabel.setVisible(false);
      volumeValueLabel.setVisible(false);
      volumeLabel.setVisible(false);
    }

    if (elementTree.getSelection() instanceof I_Carrier) {
      I_Carrier carrier = (I_Carrier) elementTree.getSelection();
      capacityPanel.setVisible(true);
      capacityPanel.updateCapacities(carrier, carrier.getCargoMass(), carrier.getCargoVolume(),
          carrier.getCrewSize());
    } else if (elementTree.getSelection() instanceof I_ResourceContainer) {
      I_ResourceContainer container = (I_ResourceContainer) elementTree.getSelection();
      capacityPanel.setVisible(true);
      capacityPanel.updateCapacities(container, container.getCargoMass(),
          container.getCargoVolume());
    } else if (elementTree.getSelection() instanceof Location) {
      Location location = (Location) elementTree.getSelection();
      capacityPanel.setVisible(true);
      capacityPanel.updateCapacities(location, location.getCargoMass(), location.getCargoVolume(),
          location.getCrewSize());
    } else {
      capacityPanel.setVisible(false);
    }
    if (elementTree.getSelection() instanceof PropulsiveVehicle) {
      PropulsiveVehicle vehicle = (PropulsiveVehicle) elementTree.getSelection();
      fuelPanel.setVisible(true);
      fuelPanel.updateFuel(vehicle);
    } else if (elementTree.getSelection() instanceof SurfaceVehicle) {
      SurfaceVehicle vehicle = (SurfaceVehicle) elementTree.getSelection();
      fuelPanel.setVisible(true);
      fuelPanel.updateFuel(vehicle);
    } else {
      fuelPanel.setVisible(false);
    }
  }

  /**
   * The Class Animation.
   */
  class Animation extends SwingWorker<Void, Void> {

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#doInBackground()
     */
    @Override
    public Void doInBackground() {
      if (constantTimeCheck.isSelected()) {
        while (timeSlider.getValue() < timeSlider.getMaximum() && !isCancelled()) {
          timeSlider.setValue(timeSlider.getValue() + 1);
          try {
            Thread.sleep((1000 / (Integer) rateSpinner.getValue()));
          } catch (InterruptedException e) {
          }
        }
      } else {
        int i = 0;
        while (i < tab.getSimulator().getNetworkHistory().size() && timeSlider.getValue()
            / stepsPerDay > tab.getSimulator().getNetworkHistory().get(i).getTime()) {
          i++;
        }
        while (i < tab.getSimulator().getNetworkHistory().size() && !isCancelled()) {
          timeSlider.setValue(
              (int) (tab.getSimulator().getNetworkHistory().get(i).getTime() * stepsPerDay));
          i++;
          try {
            Thread.sleep((1000 / (Integer) rateSpinner.getValue()));
          } catch (InterruptedException e) {
          }
        }
      }
      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jdesktop.swingworker.SwingWorker#done()
     */
    public void done() {
      playButton.setSelected(false);
    }
  }
}
