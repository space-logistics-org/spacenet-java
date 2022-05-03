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
package edu.mit.spacenet.gui.model;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.domain.model.RatedDemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;

/**
 * The panel for viewing and editing rated demand models.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class RatedDemandModelPanel extends AbstractDemandModelPanel {
  private static final long serialVersionUID = -966209133130231178L;

  private RatedDemandModel demandModel;

  private DemandTable demandsList;
  private JScrollPane demandsScroll;
  private JButton btnRemoveDemand;

  /**
   * Instantiates a new rated demand model panel.
   * 
   * @param demandModelDialog the demand model dialog
   * @param demandModel the demand model
   */
  public RatedDemandModelPanel(DemandModelDialog demandModelDialog, RatedDemandModel demandModel) {
    super(demandModelDialog, demandModel);
    this.demandModel = demandModel;
    buildPanel();
    initialize();
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    add(new JLabel("Demands: "), c);

    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 1;
    c.weightx = 1;
    c.weighty = 1;
    demandsList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario()
        .getDataSource().getResourceLibrary());
    demandsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    demandsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (demandsList.getSelectedRows().length >= 1) {
          btnRemoveDemand.setEnabled(true);
        } else {
          btnRemoveDemand.setEnabled(false);
        }
      }
    });
    demandsList.getColumnModel().getColumn(2).setHeaderValue("Amt/Day");
    demandsScroll = new JScrollPane(demandsList);
    demandsScroll.setPreferredSize(new Dimension(400, 150));
    add(demandsScroll, c);

    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    c.weighty = 0;
    JPanel demandButtonPanel = new JPanel();
    demandButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    add(demandButtonPanel, c);

    JButton btnAddDemand = new JButton("Add",
        new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
    btnAddDemand.setMargin(new Insets(3, 3, 3, 3));
    btnAddDemand.setToolTipText("Add a Demand");
    btnAddDemand.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        demandsList.addDemand(new Demand());
      }
    });
    demandButtonPanel.add(btnAddDemand);
    btnRemoveDemand = new JButton("Remove",
        new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
    btnRemoveDemand.setMargin(new Insets(3, 3, 3, 3));
    btnRemoveDemand.setToolTipText("Remove Demand");
    btnRemoveDemand.setEnabled(false);
    btnRemoveDemand.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ArrayList<Demand> demands = new ArrayList<Demand>();
        for (int row : demandsList.getSelectedRows()) {
          demands.add(demandsList.getDemand(row));
        }
        for (Demand demand : demands) {
          demandsList.remove(demand);
        }
      }
    });
    demandButtonPanel.add(btnRemoveDemand);
  }

  /**
   * Initializes the panel for a new demand model.
   */
  private void initialize() {
    demandsList.removeAllDemands();
    for (Demand demand : demandModel.getDemandRates()) {
      demandsList.addDemand(demand);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#getDemandModel()
   */
  @Override
  public RatedDemandModel getDemandModel() {
    return demandModel;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#saveDemandModel()
   */
  public void saveDemandModel() {
    if (demandsList.isEditing())
      demandsList.getCellEditor().stopCellEditing();
    demandModel.getDemandRates().clear();
    for (Demand demand : demandsList.getDemands()) {
      if (demand.getResource() != null)
        demandModel.getDemandRates().add(demand);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#isDemandModelValid()
   */
  @Override
  public boolean isDemandModelValid() {
    return true;
  }
}
