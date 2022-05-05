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
package edu.mit.spacenet.gui.element;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.domain.model.DemandModelFactory;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.gui.component.DropDownButton;
import edu.mit.spacenet.gui.model.DemandModelDialog;
import edu.mit.spacenet.gui.renderer.DemandModelListCellRenderer;
import edu.mit.spacenet.gui.renderer.StateTypeListCellRenderer;

/**
 * A dialog for viewing and editing element operational states.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class StateDialog extends JDialog {
  private static final long serialVersionUID = -2572640622351398778L;

  private JTextField txtName;
  private JComboBox<StateType> ddlType;
  private DefaultListModel<I_DemandModel> demandModelsModel;
  private JList<I_DemandModel> demandModelsList;
  private JScrollPane demandModelsScroll;
  private DropDownButton btnAddDemandModel;
  private JMenuItem addTimedImpulseDemandModel, addRatedDemandModel, addSparingByMassDemandModel;
  private JButton btnEditDemandModel, btnRemoveDemandModel, btnOk, btnCancel;
  private JPanel demandModelButtonPanel, buttonPanel;

  private I_State state;
  private ElementDialog elementDialog;

  /**
   * Instantiates a new state dialog.
   * 
   * @param elementDialog the element dialog
   * @param state the state
   */
  public StateDialog(ElementDialog elementDialog, I_State state) {
    super(elementDialog, "Operational State");
    this.elementDialog = elementDialog;
    this.state = state;

    buildDialog();
    initialize();
  }

  /**
   * Builds the dialog.
   */
  private void buildDialog() {
    JPanel contentPanel = new JPanel();
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    contentPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0;
    c.weighty = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;

    contentPanel.add(new JLabel("Name"), c);
    c.gridy++;
    contentPanel.add(new JLabel("Type: "), c);
    c.gridy++;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    contentPanel.add(new JLabel("Demand Models: "), c);

    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridy = 0;
    c.gridx = 1;
    c.weightx = 1;
    txtName = new JTextField(25);
    contentPanel.add(txtName, c);

    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    ddlType = new JComboBox<StateType>();
    for (StateType t : StateType.values())
      ddlType.addItem(t);
    ddlType.setRenderer(new StateTypeListCellRenderer());
    contentPanel.add(ddlType, c);

    c.gridy++;
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 1;
    demandModelsModel = new DefaultListModel<I_DemandModel>();
    demandModelsList = new JList<I_DemandModel>(demandModelsModel);
    demandModelsList.setCellRenderer(new DemandModelListCellRenderer());
    demandModelsList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && demandModelsList.getSelectedIndex() >= 0) {
          DemandModelDialog.createAndShowGUI(getThis(), demandModelsList.getSelectedValue());
        }
      }
    });
    demandModelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    demandModelsList.setVisibleRowCount(3);
    demandModelsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (demandModelsList.getSelectedIndex() >= 0) {
          btnEditDemandModel.setEnabled(true);
          btnRemoveDemandModel.setEnabled(true);
        } else {
          btnEditDemandModel.setEnabled(false);
          btnRemoveDemandModel.setEnabled(false);
        }
      }
    });
    demandModelsScroll = new JScrollPane(demandModelsList);
    contentPanel.add(demandModelsScroll, c);

    c.gridy++;
    c.weighty = 0;
    c.fill = GridBagConstraints.NONE;
    demandModelButtonPanel = new JPanel();
    demandModelButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    contentPanel.add(demandModelButtonPanel, c);

    btnAddDemandModel = new DropDownButton("Add",
        new ImageIcon(getClass().getClassLoader().getResource("icons/comment_add.png")));
    btnAddDemandModel.setToolTipText("Add Demand Model");
    demandModelButtonPanel.add(btnAddDemandModel);

    addTimedImpulseDemandModel = new JMenuItem(DemandModelType.TIMED_IMPULSE.getName(),
        DemandModelType.TIMED_IMPULSE.getIcon());
    addTimedImpulseDemandModel
        .setToolTipText("Add a one-time demand for resources at a given time");
    addTimedImpulseDemandModel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DemandModelDialog.createAndShowGUI(getThis(), DemandModelFactory
            .createDemandModel(getElementDialog().getElement(), DemandModelType.TIMED_IMPULSE));
      }
    });
    btnAddDemandModel.getMenu().add(addTimedImpulseDemandModel);
    addRatedDemandModel =
        new JMenuItem(DemandModelType.RATED.getName(), DemandModelType.RATED.getIcon());
    addRatedDemandModel
        .setToolTipText("Add a recurring demand for resources based on a demand rate");
    addRatedDemandModel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DemandModelDialog.createAndShowGUI(getThis(), DemandModelFactory
            .createDemandModel(getElementDialog().getElement(), DemandModelType.RATED));
      }
    });
    btnAddDemandModel.getMenu().add(addRatedDemandModel);
    addSparingByMassDemandModel = new JMenuItem(DemandModelType.SPARING_BY_MASS.getName(),
        DemandModelType.SPARING_BY_MASS.getIcon());
    addSparingByMassDemandModel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DemandModelDialog.createAndShowGUI(getThis(), DemandModelFactory
            .createDemandModel(getElementDialog().getElement(), DemandModelType.SPARING_BY_MASS));
      }
    });
    btnAddDemandModel.getMenu().add(addSparingByMassDemandModel);
    demandModelButtonPanel.add(btnAddDemandModel);

    btnEditDemandModel = new JButton("Edit",
        new ImageIcon(getClass().getClassLoader().getResource("icons/comment_edit.png")));
    btnEditDemandModel.setToolTipText("Edit Demand Model");
    btnEditDemandModel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DemandModelDialog.createAndShowGUI(getThis(), demandModelsList.getSelectedValue());
      }
    });
    btnEditDemandModel.setEnabled(false);
    demandModelButtonPanel.add(btnEditDemandModel);

    btnRemoveDemandModel = new JButton("Remove",
        new ImageIcon(getClass().getClassLoader().getResource("icons/comment_delete.png")));
    btnRemoveDemandModel.setToolTipText("Remove Demand Model");
    btnRemoveDemandModel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        demandModelsModel.removeElement(demandModelsList.getSelectedValue());
        repaint();
      }
    });
    btnRemoveDemandModel.setEnabled(false);
    demandModelButtonPanel.add(btnRemoveDemandModel);

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.LINE_END;
    btnOk = new JButton("OK",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock_go.png")));
    btnOk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveState();
      }
    });
    getRootPane().setDefaultButton(btnOk);
    btnCancel = new JButton("Cancel",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock_delete.png")));
    btnCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    buttonPanel.add(btnOk);
    buttonPanel.add(btnCancel);
    contentPanel.add(buttonPanel, c);

    setContentPane(contentPanel);
    setModal(true);
  }

  /**
   * Initializes the dialog components for a new state.
   */
  private void initialize() {
    txtName.setText(state.getName());
    ddlType.setSelectedItem(state.getStateType());
    for (I_DemandModel model : state.getDemandModels()) {
      demandModelsModel.addElement(model);
    }
    repaint();
  }

  /**
   * Checks if is state valid.
   * 
   * @return true, if is state valid
   */
  private boolean isStateValid() {
    if (txtName.getText().length() == 0) {
      JOptionPane.showMessageDialog(this, "Please enter a state name.", "SpaceNet Warning",
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Save state.
   */
  private void saveState() {
    if (isStateValid()) {
      state.setName(txtName.getText());
      state.setStateType((StateType) ddlType.getSelectedItem());
      state.getDemandModels().clear();
      for (Object o : demandModelsModel.toArray()) {
        state.getDemandModels().add((I_DemandModel) o);
      }
      if (!elementDialog.containsState(state)) {
        elementDialog.addState(state);
      }
      elementDialog.repaint();
      dispose();
    }
  }

  /**
   * Gets the this.
   * 
   * @return the this
   */
  public StateDialog getThis() {
    return this;
  }

  /**
   * Gets the element dialog.
   * 
   * @return the element dialog
   */
  public ElementDialog getElementDialog() {
    return elementDialog;
  }

  /**
   * Gets the state.
   * 
   * @return the state
   */
  public I_State getState() {
    return state;
  }

  /**
   * Contains demand model.
   * 
   * @param demandModel the demand model
   * 
   * @return true, if successful
   */
  public boolean containsDemandModel(I_DemandModel demandModel) {
    return demandModelsModel.contains(demandModel);
  }

  /**
   * Adds the demand model.
   * 
   * @param demandModel the demand model
   */
  public void addDemandModel(I_DemandModel demandModel) {
    demandModelsModel.addElement(demandModel);
  }

  /**
   * Creates the and show gui.
   * 
   * @param elementDialog the element dialog
   * @param state the state
   */
  public static void createAndShowGUI(ElementDialog elementDialog, I_State state) {
    StateDialog d = new StateDialog(elementDialog, state);
    d.pack();
    d.setLocationRelativeTo(d.getParent());
    d.setVisible(true);
  }
}
