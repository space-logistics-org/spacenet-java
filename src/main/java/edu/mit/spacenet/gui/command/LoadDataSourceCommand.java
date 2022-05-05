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
package edu.mit.spacenet.gui.command;

import java.awt.Component;
import java.awt.Cursor;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import edu.mit.spacenet.domain.element.Carrier;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.element.ResourceContainer;
import edu.mit.spacenet.domain.element.SurfaceVehicle;
import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.data.DataSourceDialog;

/**
 * Command to load the libraries from the scenario data source.
 * 
 * @author Paul Grogan
 */
public class LoadDataSourceCommand implements I_Command {
  private DataSourceDialog dataSourceDialog;
  private ScenarioPanel scenarioPanel;

  /**
   * The constructor if called from the data source dialog.
   * 
   * @param dataSourceDialog the data source dialog component
   */
  public LoadDataSourceCommand(DataSourceDialog dataSourceDialog) {
    this.dataSourceDialog = dataSourceDialog;
    scenarioPanel = dataSourceDialog.getScenarioPanel();
  }

  /**
   * The constructor if called from the scenario panel.
   * 
   * @param scenarioPanel the scenario panel
   */
  public LoadDataSourceCommand(ScenarioPanel scenarioPanel) {
    this.scenarioPanel = scenarioPanel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.command.I_Command#execute()
   */
  public void execute() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      public Void doInBackground() {
        SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Loading Data Source...");
        scenarioPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          List<String> errors = scenarioPanel.getScenario().getDataSource().validateData();
          if (errors.size() > 0) {
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.PAGE_AXIS));
            errorPanel.add(new JLabel("The following errors occurred: "));
            DefaultListModel<String> errorsModel = new DefaultListModel<String>();
            for (String e : errors) {
              errorsModel.addElement(e);
            }
            errorPanel.add(new JScrollPane(new JList<String>(errorsModel)));
            errorPanel.add(new JLabel("Please make the necessary changes and reload database."));
            JOptionPane.showMessageDialog(getComponent(), errorPanel, "Load Error",
                JOptionPane.ERROR_MESSAGE);
          } else {
            if (dataSourceDialog == null) {
              scenarioPanel.getScenario().getDataSource().loadLibraries();
            } else {
              scenarioPanel.getScenario().getDataSource().loadLibraries(
                  dataSourceDialog.updateNodes(), dataSourceDialog.updateEdges(),
                  dataSourceDialog.updateResources());
            }
            if (dataSourceDialog != null && dataSourceDialog.updateInstantiatedElements()) {
              for (I_Element element : scenarioPanel.getScenario().getElements()) {
                if (element.getTid() > 0) {
                  I_Element mirror =
                      scenarioPanel.getScenario().getDataSource().loadElement(element.getTid());
                  if (mirror.getClass().equals(element.getClass())) {
                    element.setAccommodationMass(mirror.getAccommodationMass());
                    element.setClassOfSupply(mirror.getClassOfSupply());
                    element.setDescription(mirror.getDescription());
                    element.setEnvironment(mirror.getEnvironment());
                    element.setMass(mirror.getMass());
                    element.setStates(mirror.getStates());
                    element.setCurrentState(mirror.getCurrentState());
                    element.setParts(mirror.getParts());
                    element.setVolume(mirror.getVolume());
                    element.setIconType(mirror.getIconType());
                    if (element.getElementType() == ElementType.RESOURCE_CONTAINER) {
                      ((ResourceContainer) element)
                          .setMaxCargoMass(((ResourceContainer) mirror).getMaxCargoMass());
                      ((ResourceContainer) element)
                          .setMaxCargoVolume(((ResourceContainer) mirror).getMaxCargoVolume());
                      ((ResourceContainer) element)
                          .setCargoEnvironment(((ResourceContainer) mirror).getCargoEnvironment());
                      // NOTE: presently does not carry over any changes to contents
                    } else if (element.getElementType() == ElementType.CARRIER) {
                      ((Carrier) element).setMaxCrewSize(((Carrier) mirror).getMaxCrewSize());
                      ((Carrier) element).setMaxCargoMass(((Carrier) mirror).getMaxCargoMass());
                      ((Carrier) element).setMaxCargoVolume(((Carrier) mirror).getMaxCargoVolume());
                      ((Carrier) element)
                          .setCargoEnvironment(((Carrier) mirror).getCargoEnvironment());
                    } else if (element.getElementType() == ElementType.PROPULSIVE_VEHICLE) {
                      ((PropulsiveVehicle) element)
                          .setMaxCrewSize(((PropulsiveVehicle) mirror).getMaxCrewSize());
                      ((PropulsiveVehicle) element)
                          .setMaxCargoMass(((PropulsiveVehicle) mirror).getMaxCargoMass());
                      ((PropulsiveVehicle) element)
                          .setMaxCargoVolume(((PropulsiveVehicle) mirror).getMaxCargoVolume());
                      ((PropulsiveVehicle) element)
                          .setCargoEnvironment(((PropulsiveVehicle) mirror).getCargoEnvironment());
                      ((PropulsiveVehicle) element)
                          .setOmsIsp(((PropulsiveVehicle) mirror).getOmsIsp());
                      ((PropulsiveVehicle) element)
                          .setOmsFuelTank(((PropulsiveVehicle) mirror).getOmsFuelTank());
                      ((PropulsiveVehicle) element)
                          .setRcsIsp(((PropulsiveVehicle) mirror).getRcsIsp());
                      ((PropulsiveVehicle) element)
                          .setRcsFuelTank(((PropulsiveVehicle) mirror).getRcsFuelTank());
                    } else if (element.getElementType() == ElementType.SURFACE_VEHICLE) {
                      ((SurfaceVehicle) element)
                          .setMaxCrewSize(((SurfaceVehicle) mirror).getMaxCrewSize());
                      ((SurfaceVehicle) element)
                          .setMaxCargoMass(((SurfaceVehicle) mirror).getMaxCargoMass());
                      ((SurfaceVehicle) element)
                          .setMaxCargoVolume(((SurfaceVehicle) mirror).getMaxCargoVolume());
                      ((SurfaceVehicle) element)
                          .setCargoEnvironment(((SurfaceVehicle) mirror).getCargoEnvironment());
                      ((SurfaceVehicle) element)
                          .setMaxSpeed(((SurfaceVehicle) mirror).getMaxSpeed());
                      ((SurfaceVehicle) element)
                          .setFuelTank(((SurfaceVehicle) mirror).getFuelTank());
                    } else if (element.getElementType() == ElementType.CREW_MEMBER) {
                      ((CrewMember) element).setAvailableTimeFraction(
                          ((CrewMember) mirror).getAvailableTimeFraction());
                    }
                  }
                }
              }
            }
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(getComponent(),
              "There was a problem opening the data source.", "SpaceNet Error",
              JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
        return null;
      }

      public void done() {
        SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
        scenarioPanel.setCursor(Cursor.getDefaultCursor());

        if (scenarioPanel.getScenario().getNetwork().getLocations().size() == 0) {
          FilterNetworkCommand command = new FilterNetworkCommand(scenarioPanel.getScenario());
          command.execute();
        }

        if (dataSourceDialog != null)
          dataSourceDialog.updateTables();
        scenarioPanel.updateView();
      }
    };
    worker.execute();
  }

  private Component getComponent() {
    if (dataSourceDialog == null)
      return scenarioPanel;
    else
      return dataSourceDialog;
  }
}
