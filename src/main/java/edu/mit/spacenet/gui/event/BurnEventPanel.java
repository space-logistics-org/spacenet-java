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
package edu.mit.spacenet.gui.event;

import java.awt.Color;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.BurnType;
import edu.mit.spacenet.gui.component.CheckBoxNode;
import edu.mit.spacenet.gui.component.CheckBoxTree;
import edu.mit.spacenet.gui.component.CheckBoxTreeModel;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.simulator.event.BurnEvent;
import edu.mit.spacenet.simulator.event.BurnStageItem;
import edu.mit.spacenet.util.Formulae;

/**
 * A panel for viewing and editing a burn event.
 * 
 * @author Paul Grogan
 */
public class BurnEventPanel extends AbstractEventPanel {
  private static final long serialVersionUID = 769918023169742283L;

  private BurnEvent event;

  private JComboBox<BurnType> ddlBurnType;
  private SpinnerNumberModel burnDeltaVModel;
  private JSpinner burnDeltaVSpinner;
  private JButton btnAddBurn, btnAddStage, btnClear;
  private DefaultListModel<BurnStageItem> sequenceModel;
  private JList<BurnStageItem> sequenceList;
  private JProgressBar deltaV;
  private JLabel stackMassLabel;

  private CheckBoxTreeModel elementModel;
  private CheckBoxTree elementTree;

  /**
   * Instantiates a new burn event panel.
   * 
   * @param eventDialog the event dialog
   * @param event the event
   */
  public BurnEventPanel(EventDialog eventDialog, BurnEvent event) {
    super(eventDialog, event);
    this.event = event;
    buildPanel();
    initialize();
  }

  private void buildPanel() {
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);

    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Burn Type: "), c);
    c.gridy++;
    add(new JLabel("Required Delta-V: "), c);
    c.gridx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.gridy = 0;
    ddlBurnType = new JComboBox<BurnType>();
    for (BurnType t : BurnType.values())
      ddlBurnType.addItem(t);
    add(ddlBurnType, c);
    c.gridy++;
    burnDeltaVModel = new SpinnerNumberModel(0, 0, 10000, 1.0);
    burnDeltaVSpinner = new JSpinner(burnDeltaVModel);
    add(new UnitsWrapper(burnDeltaVSpinner, "m/s"), c);

    c.gridx = 0;
    c.gridy = 2;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    add(new JLabel("Elements: "), c);
    c.gridy += 2;
    c.anchor = GridBagConstraints.LINE_END;
    add(new JLabel("Delta-V: "), c);
    c.gridy++;
    add(new JLabel("Stack Mass: "), c);
    c.gridy++;
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    add(new JLabel("Sequence: "), c);

    c.gridy = 2;
    c.gridx = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    elementModel = new CheckBoxTreeModel();
    elementTree = new CheckBoxTree(elementModel);
    elementTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    elementTree.setRootVisible(false);
    elementTree.setShowsRootHandles(true);
    JScrollPane elementScroll = new JScrollPane(elementTree);
    elementScroll.setPreferredSize(new Dimension(200, 100));
    add(elementScroll, c);

    c.gridy++;
    c.weighty = 0;
    c.weightx = 0;
    c.anchor = GridBagConstraints.LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel sequenceButtonPanel = new JPanel();
    sequenceButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    btnAddBurn = new JButton("Burn");
    btnAddBurn.setToolTipText("Burn the selected propulsive vehicles");
    btnAddBurn.setIcon(BurnStageItem.BURN_ICON);
    btnAddBurn.setMargin(new Insets(3, 3, 3, 3));
    sequenceButtonPanel.add(btnAddBurn);
    btnAddStage = new JButton("Stage");
    btnAddStage.setToolTipText("Stage the selected elements");
    btnAddStage.setIcon(BurnStageItem.STAGE_ICON);
    btnAddStage.setMargin(new Insets(3, 3, 3, 3));
    sequenceButtonPanel.add(btnAddStage);
    add(sequenceButtonPanel, c);

    c.gridy++;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    deltaV = new JProgressBar(0, 100);
    deltaV.setStringPainted(true);
    add(deltaV, c);
    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    stackMassLabel = new JLabel();
    add(new UnitsWrapper(stackMassLabel, "kg"), c);

    c.gridy++;
    c.weighty = 1;
    c.weightx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.BOTH;
    sequenceModel = new DefaultListModel<BurnStageItem>();
    sequenceList = new JList<BurnStageItem>(sequenceModel);
    sequenceList.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 1271331296677711150L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
            cellHasFocus);
        if (((BurnStageItem) value).getBurnStage().equals(BurnStageItem.BURN)) {
          label.setIcon(BurnStageItem.BURN_ICON);
        } else {
          label.setIcon(BurnStageItem.STAGE_ICON);
        }

        return label;
      }
    });
    sequenceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    JScrollPane sequenceScroll = new JScrollPane(sequenceList);
    sequenceScroll.setPreferredSize(new Dimension(200, 100));
    add(sequenceScroll, c);

    c.gridy++;
    c.weighty = 0;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.LINE_END;
    btnClear = new JButton("Clear");
    btnClear.setToolTipText("Clear the selected burns or stages");
    btnClear.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cross.png")));
    btnClear.setMargin(new Insets(3, 3, 3, 3));
    add(btnClear, c);
  }

  private void initialize() {
    ddlBurnType.setSelectedItem(event.getBurn().getBurnType());
    burnDeltaVModel.setValue(event.getBurn().getDeltaV());
    for (BurnStageItem i : event.getBurnStageSequence()) {
      BurnStageItem bs = new BurnStageItem();
      bs.setBurnStage(i.getBurnStage());
      bs.setElement(getEventDialog().getSimElement(i.getElement().getUid()));
      sequenceModel.addElement(bs);
    }
    ddlBurnType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        BurnEvent event = (BurnEvent) getEvent();
        if (!ddlBurnType.getSelectedItem().equals(event.getBurn().getBurnType())) {
          event.getBurn()
              .setBurnType(BurnType.getInstance(ddlBurnType.getSelectedItem().toString()));
          updateView();
        }
      }
    });
    burnDeltaVSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        ((BurnEvent) getEvent()).getBurn().setDeltaV(burnDeltaVModel.getNumber().doubleValue());
        updateView();
      }
    });

    elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
    elementTree.setModel(elementModel);
    elementModel.setCheckedElements(event.getElements());
    elementModel.addTreeModelListener(new TreeModelListener() {
      public void treeNodesChanged(TreeModelEvent e) {
        updateView();
        updateButtons();
      }

      public void treeNodesInserted(TreeModelEvent e) {}

      public void treeNodesRemoved(TreeModelEvent e) {}

      public void treeStructureChanged(TreeModelEvent e) {}
    });

    elementTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        if (!elementTree.isSelectionEmpty()) {
          sequenceList.clearSelection();
        }
        updateButtons();
      }
    });
    btnAddBurn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sequenceModel.addElement(new BurnStageItem(BurnStageItem.BURN,
            ((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent()).getElement()));
        updateView();
      }
    });
    btnAddStage.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sequenceModel.addElement(new BurnStageItem(BurnStageItem.STAGE,
            ((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent()).getElement()));
        updateView();
      }
    });
    sequenceList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!sequenceList.isSelectionEmpty()) {
          elementTree.clearSelection();
        }
        updateButtons();
      }
    });
    btnClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (BurnStageItem o : sequenceList.getSelectedValuesList())
          sequenceModel.removeElement(o);
        updateView();
      }
    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
   */
  @Override
  public BurnEvent getEvent() {
    return event;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
   */
  @Override
  public void saveEvent() {
    event.setBurn(new Burn(0, BurnType.getInstance(ddlBurnType.getSelectedItem().toString()),
        burnDeltaVModel.getNumber().doubleValue()));

    event.getElements().clear();
    for (I_Element element : elementModel.getTopCheckedElements()) {
      event.getElements().add(getEventDialog().getElement(element.getUid()));
    }

    ArrayList<BurnStageItem> sequence = new ArrayList<BurnStageItem>();
    for (Object o : sequenceModel.toArray()) {
      BurnStageItem i = new BurnStageItem();
      i.setBurnStage(((BurnStageItem) o).getBurnStage());
      i.setElement(getEventDialog().getElement(((BurnStageItem) o).getElement().getUid()));
      sequence.add(i);
    }
    event.setBurnStateSequence(sequence);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
   */
  @Override
  public void updateView() {
    Set<I_Element> checkedElements = elementModel.getTopCheckedElements();
    int[] selectionRows = elementTree.getSelectionRows();
    elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
    elementTree.setModel(elementModel);
    elementModel.setCheckedElements(checkedElements);
    elementTree.setSelectionRows(selectionRows);
    elementModel.addTreeModelListener(new TreeModelListener() {
      public void treeNodesChanged(TreeModelEvent e) {
        updateView();
        updateButtons();
      }

      public void treeNodesInserted(TreeModelEvent e) {}

      public void treeNodesRemoved(TreeModelEvent e) {}

      public void treeStructureChanged(TreeModelEvent e) {}
    });

    for (Object o : sequenceModel.toArray()) {
      boolean isFound = false;
      for (I_Element element : elementModel.getTopCheckedElements()) {
        if (element.equals(((BurnStageItem) o).getElement()))
          isFound = true;
      }
      if (!isFound)
        sequenceModel.clear();
    }
    updateDeltaV();
  }

  private void updateButtons() {
    if (elementTree.getSelectionCount() > 0) {
      if (elementTree.getSelectionCount() == 1
          && ((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent()).isChecked()
          && elementModel.getTopCheckedElements()
              .contains(((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent())
                  .getElement())) {
        boolean isStaged = false;
        for (Object o : sequenceModel.toArray()) {
          if (((BurnStageItem) o).getElement().equals(
              ((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent()).getElement())
              && ((BurnStageItem) o).getBurnStage().equals(BurnStageItem.STAGE)) {
            isStaged = true;
            break;
          }
        }
        if (isStaged) {
          btnAddStage.setEnabled(false);
          btnAddBurn.setEnabled(false);
        } else {
          btnAddStage.setEnabled(true);
          if (((CheckBoxNode) elementTree.getSelectionPath().getLastPathComponent())
              .getElement() instanceof PropulsiveVehicle) {
            PropulsiveVehicle v = (PropulsiveVehicle) ((CheckBoxNode) elementTree.getSelectionPath()
                .getLastPathComponent()).getElement();
            BurnEvent event = (BurnEvent) this.event;
            if ((event.getBurn().getBurnType().equals(BurnType.OMS) && v.getOmsFuelTank() != null)
                || (event.getBurn().getBurnType().equals(BurnType.RCS)
                    && v.getRcsFuelTank() != null))
              btnAddBurn.setEnabled(true);
            else
              btnAddBurn.setEnabled(false);
          } else {
            btnAddBurn.setEnabled(false);
          }
        }
      } else {
        btnAddStage.setEnabled(false);
        btnAddBurn.setEnabled(false);
      }
    } else {
      btnAddStage.setEnabled(false);
      btnAddBurn.setEnabled(false);
    }
    if (sequenceList.getSelectedIndices().length > 0) {
      btnClear.setEnabled(true);
    } else {
      btnClear.setEnabled(false);
    }
  }

  private void updateDeltaV() {
    double stackMass = 0;
    double deltaVReq = burnDeltaVModel.getNumber().doubleValue();

    // System.out.println("deltaVReq: " + deltaVReq);

    for (I_Element e : elementModel.getTopCheckedElements())
      stackMass += e.getTotalMass();

    for (Object o : sequenceModel.toArray()) {
      BurnStageItem i = (BurnStageItem) o;
      if (i.getBurnStage().equals(BurnStageItem.STAGE)) {
        // staged elements are no longer in the contents model
        stackMass += i.getElement().getTotalMass();
      }
    }

    // System.out.println("stackMass: " + stackMass);

    for (Object o : sequenceModel.toArray()) {
      BurnStageItem i = (BurnStageItem) o;
      if (i.getBurnStage().equals(BurnStageItem.STAGE)) {
        // System.out.println("stage: " + i.getElement().getName());
        stackMass -= i.getElement().getTotalMass();
      } else if (i.getBurnStage().equals(BurnStageItem.BURN)) {
        // System.out.println("burn: " + i.getElement().getName());
        PropulsiveVehicle v = (PropulsiveVehicle) i.getElement();
        if (((BurnType) ddlBurnType.getSelectedItem()).equals(BurnType.OMS)) {
          if (Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp()) > v.getOmsFuelTank()
              .getAmount()) {
            double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getOmsIsp(),
                v.getOmsFuelTank().getAmount());
            // System.out.println("dv achieved: " + deltaVAchieved);
            deltaVReq -= deltaVAchieved;
            stackMass -= v.getOmsFuelTank().getAmount();
          } else {
            // System.out.println("dv achieved: " + deltaVReq);
            stackMass -= Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp());
            deltaVReq = 0;
          }
        } else {
          if (Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp()) > v.getRcsFuelTank()
              .getAmount()) {
            double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getRcsIsp(),
                v.getRcsFuelTank().getAmount());
            // System.out.println("dv achieved: " + deltaVAchieved);
            deltaVReq -= deltaVAchieved;
            stackMass -= v.getRcsFuelTank().getAmount();
          } else {
            // System.out.println("dv achieved: " + deltaVReq);
            stackMass -= Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp());
            deltaVReq = 0;
          }
        }
      }
    }
    if (deltaVReq == 0) {
      deltaV.setForeground(new Color(0, 153, 0));
      getEventDialog().setOkButtonEnabled(true);
    } else {
      deltaV.setForeground(new Color(153, 0, 0));
      getEventDialog().setOkButtonEnabled(false);
    }
    DecimalFormat format = new DecimalFormat("0.0");
    stackMassLabel.setText(format.format(stackMass));
    deltaV.setValue((int) (100 * ((burnDeltaVModel.getNumber().doubleValue() - deltaVReq)
        / burnDeltaVModel.getNumber().doubleValue())));
    deltaV.setString(format.format(burnDeltaVModel.getNumber().doubleValue() - deltaVReq) + " / "
        + burnDeltaVModel.getNumber() + " m/s");
  }
}
