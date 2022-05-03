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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.element.ElementFactory;
import edu.mit.spacenet.domain.element.ElementIcon;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.element.State;
import edu.mit.spacenet.domain.resource.ResourceType;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.data.DataSourceDialog;
import edu.mit.spacenet.gui.event.EventDialog;
import edu.mit.spacenet.gui.renderer.ElementTypeListCellRenderer;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * Component for viewing and editing elements.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class ElementDialog extends JDialog {
  private static final long serialVersionUID = 5400688589831319535L;

  private I_Element element;
  private EventDialog eventDialog;
  private DataSourceDialog dataSourceDialog;

  private JComboBox<ElementType> typeCombo;
  private JTextField nameText;
  private JComboBox<ElementIcon> iconCombo;
  private JComboBox<ClassOfSupply> classOfSupplyCombo;
  private JComboBox<Environment> environmentCombo;
  private SpinnerNumberModel accommodationMassModel, massModel, volumeModel;
  private JSpinner accommodationMassSpinner, massSpinner, volumeSpinner;
  private I_State initialState;
  private DefaultListModel<I_State> statesModel;
  private DefaultListModel<PartApplication> partsModel;
  private JList<I_State> statesList;
  private JList<PartApplication> partsList;
  private JButton editStateButton, setInitialStateButton, removeStateButton, editPartButton,
      removePartButton;
  private List<AbstractElementPanel> elementPanels;
  private JButton okButton;

  /**
   * The constructor.
   * 
   * @param dialog the event dialog
   * @param element the element
   */
  public ElementDialog(EventDialog dialog, I_Element element) {
    super(dialog, "Edit Element", true);
    this.eventDialog = dialog;
    this.element = element;
    buildDialog();
    initialize();
    setMinimumSize(new Dimension(300, 150));
    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
  }

  /**
   * Instantiates a new element dialog.
   * 
   * @param dialog the dialog
   * @param element the element
   */
  public ElementDialog(DataSourceDialog dialog, I_Element element) {
    super(dialog, "Edit Element", true);
    this.dataSourceDialog = dialog;
    this.element = element;
    buildDialog();
    initialize();
    setMinimumSize(new Dimension(300, 150));
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
    contentPanel.add(new JLabel("Type: "), c);
    c.gridy++;
    contentPanel.add(new JLabel("Name: "), c);
    c.gridy++;
    contentPanel.add(new JLabel("Icon: "), c);
    c.gridy++;
    contentPanel.add(new JLabel("Class of Supply: "), c);
    c.gridy++;
    contentPanel.add(new JLabel("Environment: "), c);
    c.gridx += 2;
    contentPanel.add(new JLabel("Accommodation Mass: "), c);
    c.gridy++;
    c.gridx = 0;
    contentPanel.add(new JLabel("Mass: "), c);
    c.gridx += 2;
    contentPanel.add(new JLabel("Volume: "), c);

    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 1;
    c.gridy = 0;
    c.gridwidth = 3;
    typeCombo = new JComboBox<ElementType>();
    for (ElementType t : ElementType.values())
      if (t != ElementType.RESOURCE_TANK)
        typeCombo.addItem(t); // TODO add support / datasource
    typeCombo.setRenderer(new ElementTypeListCellRenderer());
    typeCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (typeCombo.getSelectedItem() != element.getElementType())
          reset((ElementType) typeCombo.getSelectedItem());
      }
    });
    contentPanel.add(typeCombo, c);

    c.gridy++;
    c.fill = GridBagConstraints.HORIZONTAL;
    nameText = new JTextField();
    contentPanel.add(nameText, c);
    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 3;
    JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    iconCombo = new JComboBox<ElementIcon>();
    for (ElementIcon i : ElementIcon.values()) {
      iconCombo.addItem(i);
    }
    iconCombo.setRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = -2255885956722142642L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setIcon(((ElementIcon) value).getIcon());
        setText(((ElementIcon) value).getName());
        return this;
      }
    });
    iconPanel.add(iconCombo);
    JPanel dividerPanel = new JPanel();
    dividerPanel.setPreferredSize(new Dimension(5, 1));
    iconPanel.add(dividerPanel);
    JButton resetIconButton = new JButton("Reset");
    resetIconButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        iconCombo.setSelectedItem(element.getElementType().getIconType());
      }
    });
    iconPanel.add(resetIconButton);
    contentPanel.add(iconPanel, c);

    c.gridy++;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 3;
    classOfSupplyCombo = new JComboBox<ClassOfSupply>();
    for (ClassOfSupply cos : ClassOfSupply.values())
      classOfSupplyCombo.addItem(cos);
    contentPanel.add(classOfSupplyCombo, c);

    c.gridy++;
    c.gridwidth = 1;
    environmentCombo = new JComboBox<Environment>();
    for (Environment e : Environment.values())
      environmentCombo.addItem(e);
    contentPanel.add(environmentCombo, c);
    environmentCombo.setToolTipText("Required environment for nesting");

    c.gridx += 2;
    accommodationMassModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getMassPrecision());
    accommodationMassSpinner = new JSpinner(accommodationMassModel);
    accommodationMassSpinner.setPreferredSize(new Dimension(150, 20));
    accommodationMassSpinner.setEnabled(false); // TODO disabled
    contentPanel.add(new UnitsWrapper(accommodationMassSpinner, "kg"), c);
    accommodationMassSpinner
        .setToolTipText("Additional mass of COS 5 required to nest inside a carrier [kilograms]");

    c.gridy++;
    c.gridx = 1;
    c.weightx = 1;
    massModel = new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getMassPrecision());
    massSpinner = new JSpinner(massModel);
    massSpinner.setPreferredSize(new Dimension(150, 20));
    contentPanel.add(new UnitsWrapper(massSpinner, "kg"), c);
    massSpinner.setToolTipText("Dry element mass [kilograms]");

    c.gridx += 2;
    c.weightx = 1;
    volumeModel =
        new SpinnerNumberModel(0, 0, Double.MAX_VALUE, GlobalParameters.getVolumePrecision());
    volumeSpinner = new JSpinner(volumeModel);
    volumeSpinner.setPreferredSize(new Dimension(150, 20));
    contentPanel.add(new UnitsWrapper(volumeSpinner, "m^3"), c);
    volumeSpinner.setToolTipText("Element volume [cubic meters]");

    c.gridy++;
    c.gridx = 0;
    c.weightx = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 1;
    contentPanel.add(buildStatesPanel(), c);

    c.gridx += 2;
    contentPanel.add(buildPartsPanel(), c);

    c.gridy++;
    c.gridx = 0;
    c.gridwidth = 4;
    c.weighty = 1;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    elementPanels = ElementPanelFactory.createElementPanel(this, element);
    for (AbstractElementPanel panel : elementPanels) {
      c.weighty = panel.isVerticallyExpandable() ? 1 : 0;
      contentPanel.add(panel, c);
      c.gridy++;
    }

    c.gridy += 10; // give space for up to 10 more element panels
    c.weighty = 0;
    c.anchor = GridBagConstraints.LAST_LINE_END;
    c.fill = GridBagConstraints.NONE;
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    contentPanel.add(buttonPanel, c);
    okButton = new JButton("OK",
        new ImageIcon(getClass().getClassLoader().getResource("icons/brick_go.png")));
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveElement();
      }
    });
    buttonPanel.add(okButton);
    JButton cancelButton = new JButton("Cancel",
        new ImageIcon(getClass().getClassLoader().getResource("icons/brick_delete.png")));
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    buttonPanel.add(cancelButton);

    getRootPane().setDefaultButton(okButton);
    setContentPane(contentPanel);
  }

  /**
   * Builds the states panel.
   * 
   * @return the j panel
   */
  private JPanel buildStatesPanel() {
    JPanel statesPanel = new JPanel();
    statesPanel.setBorder(BorderFactory.createTitledBorder("States"));
    statesPanel.setLayout(new BorderLayout());

    statesModel = new DefaultListModel<I_State>();
    statesList = new JList<I_State>(statesModel);
    statesList.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 1271331296677711150L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
            cellHasFocus);
        label.setIcon((((I_State) value).getStateType()).getIcon());
        if (value.equals(initialState)) {
          label.setFont(label.getFont().deriveFont(Font.BOLD));
        } else {
          label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }
        return label;
      }
    });
    statesList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && statesList.getSelectedIndex() >= 0) {
          StateDialog.createAndShowGUI(getThis(), statesList.getSelectedValue());
        }
      }
    });
    statesList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!statesList.isSelectionEmpty()) {
          partsList.clearSelection();
        }
        if (statesList.getSelectedIndices().length == 1) {
          editStateButton.setEnabled(true);
          removeStateButton.setEnabled(true);
          setInitialStateButton.setEnabled(!statesList.getSelectedValue().equals(initialState));
        } else if (statesList.getSelectedIndices().length > 1) {
          editStateButton.setEnabled(false);
          setInitialStateButton.setEnabled(false);
          removeStateButton.setEnabled(true);
        } else {
          editStateButton.setEnabled(false);
          setInitialStateButton.setEnabled(false);
          removeStateButton.setEnabled(false);
        }
      }
    });
    JScrollPane statesScroll = new JScrollPane(statesList);
    statesScroll.setPreferredSize(new Dimension(250, 75));
    statesPanel.add(statesScroll, BorderLayout.CENTER);

    JPanel statesButtonPanel = new JPanel();
    statesButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    JButton addStateButton = new JButton("Add",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock_add.png")));
    addStateButton.setToolTipText("Add State");
    addStateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        StateDialog.createAndShowGUI(getThis(), new State());
      }
    });
    statesButtonPanel.add(addStateButton);
    editStateButton = new JButton("Edit",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock_edit.png")));
    editStateButton.setToolTipText("Edit State");
    editStateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        StateDialog.createAndShowGUI(getThis(), statesList.getSelectedValue());
      }
    });
    editStateButton.setEnabled(false);
    statesButtonPanel.add(editStateButton);
    setInitialStateButton = new JButton("Set Initial",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock.png")));
    setInitialStateButton.setToolTipText("Set Initial");
    setInitialStateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        initialState = statesList.getSelectedValue();
        statesList.repaint();
      }
    });
    setInitialStateButton.setEnabled(false);
    statesButtonPanel.add(setInitialStateButton);
    removeStateButton = new JButton("Remove",
        new ImageIcon(getClass().getClassLoader().getResource("icons/clock_delete.png")));
    removeStateButton.setToolTipText("Remove State");
    removeStateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (I_State state : statesList.getSelectedValuesList()) {
          statesModel.removeElement(state);
          if (state.equals(initialState)) {
            if (statesModel.size() > 0) {
              initialState = statesModel.firstElement();
            } else {
              initialState = null;
            }
          }
        }
        statesList.repaint();
      }
    });
    removeStateButton.setEnabled(false);
    statesButtonPanel.add(removeStateButton);
    statesPanel.add(statesButtonPanel, BorderLayout.SOUTH);

    return statesPanel;
  }

  private JPanel buildPartsPanel() {
    JPanel partsPanel = new JPanel();
    partsPanel.setBorder(BorderFactory.createTitledBorder("Parts"));
    partsPanel.setLayout(new BorderLayout());

    partsModel = new DefaultListModel<PartApplication>();
    partsList = new JList<PartApplication>(partsModel);
    partsList.setCellRenderer(new DefaultListCellRenderer() {
      private static final long serialVersionUID = 1271331296677711150L;

      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
            cellHasFocus);
        label.setIcon(ResourceType.ITEM.getIcon());
        return label;
      }
    });
    partsList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && partsList.getSelectedIndex() >= 0) {
          PartApplicationDialog.createAndShowGUI(getThis(), partsList.getSelectedValue());
        }
      }
    });
    partsList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!partsList.isSelectionEmpty()) {
          statesList.clearSelection();
        }
        if (partsList.getSelectedIndices().length == 1) {
          editPartButton.setEnabled(true);
          removePartButton.setEnabled(true);
        } else if (statesList.getSelectedIndices().length > 1) {
          editPartButton.setEnabled(false);
          removePartButton.setEnabled(true);
        } else {
          editPartButton.setEnabled(false);
          removePartButton.setEnabled(false);
        }
      }
    });
    JScrollPane partsScroll = new JScrollPane(partsList);
    partsScroll.setPreferredSize(new Dimension(250, 75));
    partsPanel.add(partsScroll, BorderLayout.CENTER);

    JPanel partsButtonPanel = new JPanel();
    partsButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
    JButton addPartButton = new JButton("Add",
        new ImageIcon(getClass().getClassLoader().getResource("icons/wrench_add.png")));
    addPartButton.setToolTipText("Add Part");
    addPartButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PartApplicationDialog.createAndShowGUI(getThis(), new PartApplication());
      }
    });
    partsButtonPanel.add(addPartButton);
    editPartButton = new JButton("Edit",
        new ImageIcon(getClass().getClassLoader().getResource("icons/wrench_edit.png")));
    editPartButton.setToolTipText("Edit Part");
    editPartButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PartApplicationDialog.createAndShowGUI(getThis(), partsList.getSelectedValue());
      }
    });
    editPartButton.setEnabled(false);
    partsButtonPanel.add(editPartButton);
    removePartButton = new JButton("Remove",
        new ImageIcon(getClass().getClassLoader().getResource("icons/wrench_delete.png")));
    removePartButton.setToolTipText("Remove Part");
    removePartButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (PartApplication part : partsList.getSelectedValuesList()) {
          partsModel.removeElement(part);
        }
      }
    });
    removePartButton.setEnabled(false);
    partsButtonPanel.add(removePartButton);
    partsPanel.add(partsButtonPanel, BorderLayout.SOUTH);

    return partsPanel;
  }

  /**
   * Initializes the dialog for a new element.
   */
  private void initialize() {
    typeCombo.setSelectedItem(element.getElementType());
    typeCombo.setEnabled(element.getTid() <= 0);
    nameText.setText(element.getName());
    iconCombo.setSelectedItem(element.getIconType());
    if (element.getClassOfSupply() != null)
      classOfSupplyCombo.setSelectedItem(element.getClassOfSupply());
    environmentCombo.setSelectedItem(element.getEnvironment());
    massModel.setValue(element.getMass());
    volumeModel.setValue(element.getVolume());
    for (I_State state : element.getStates()) {
      statesModel.addElement(state);
    }
    initialState = element.getCurrentState();
    for (PartApplication part : element.getParts()) {
      partsModel.addElement(part);
    }
  }

  /**
   * Resets the dialog to accommodate a different element type.
   */
  private void reset(ElementType type) {
    element = ElementFactory.createElement(type);
    for (AbstractElementPanel elementPanel : elementPanels) {
      getContentPane().remove(elementPanel);
    }

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2);
    c.gridy = 6;
    c.gridx = 0;
    c.gridwidth = 4;
    c.weighty = 1;
    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    elementPanels = ElementPanelFactory.createElementPanel(this, element);
    for (AbstractElementPanel panel : elementPanels) {
      getContentPane().add(panel, c);
      c.gridy++;
    }
    initialize();
    pack();
    validate();
    repaint();
  }

  /**
   * Checks if is element valid.
   * 
   * @return true, if is element valid
   */
  public boolean isElementValid() {
    if (nameText.getText() == "") {
      JOptionPane.showMessageDialog(this, "Please enter an element name.", "SpaceNet Warning",
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    for (AbstractElementPanel panel : elementPanels) {
      if (!panel.isElementValid())
        return false;
    }
    return true;
  }

  /**
   * Saves the element.
   */
  private void saveElement() {
    if (isElementValid()) {
      element.setName(nameText.getText());
      if (eventDialog != null) {
        if (element.getElementType().equals(iconCombo.getSelectedItem())) {
          element.setIconType(null);
        } else
          element.setIconType((ElementIcon) iconCombo.getSelectedItem());
      }
      element.setClassOfSupply((ClassOfSupply) classOfSupplyCombo.getSelectedItem());
      element.setEnvironment((Environment) environmentCombo.getSelectedItem());
      element.setMass(massModel.getNumber().doubleValue());
      element.setVolume(volumeModel.getNumber().doubleValue());
      element.getStates().clear();
      for (int i = 0; i < statesModel.size(); i++) {
        element.getStates().add(statesModel.get(i));
      }
      element.setCurrentState(initialState);
      element.getParts().clear();
      for (int i = 0; i < partsModel.getSize(); i++) {
        element.getParts().add(partsModel.get(i));
      }
      for (AbstractElementPanel panel : elementPanels) {
        panel.saveElement();
      }

      if (dataSourceDialog != null) {
        try {
          dataSourceDialog.getDataSource().saveElement(getElement());
          dataSourceDialog.loadDataSource();
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(dataSourceDialog, "SpaceNet Errror",
              "An error occurred while saving the element.", JOptionPane.ERROR_MESSAGE);
        }
      }
      if (eventDialog != null)
        eventDialog.repaint();
      else if (dataSourceDialog != null)
        dataSourceDialog.repaint();
      dispose();
    }
  }

  /**
   * Gets this.
   * 
   * @return this
   */
  private ElementDialog getThis() {
    return this;
  }

  /**
   * Gets the element.
   * 
   * @return the element
   */
  public I_Element getElement() {
    return element;
  }

  /**
   * Gets the event dialog.
   * 
   * @return the event dialog
   */
  public EventDialog getCreateEventDialog() {
    return eventDialog;
  }

  /**
   * Gets the data source dialog.
   * 
   * @return the data source dialog
   */
  public DataSourceDialog getDataSourceDialog() {
    return dataSourceDialog;
  }

  /**
   * Gets whether the states model contains a state.
   * 
   * @param state the state
   * 
   * @return whether the model contains the state
   */
  public boolean containsState(I_State state) {
    return statesModel.contains(state);
  }

  /**
   * Adds a state to the states model.
   * 
   * @param state the state
   */
  public void addState(I_State state) {
    if (initialState == null)
      initialState = state;
    statesModel.addElement(state);
  }

  /**
   * Gets whether the parts application model contains a part.
   * 
   * @param part the part
   * 
   * @return whether the model contains the part
   */
  public boolean containsPart(PartApplication part) {
    return partsModel.contains(part);
  }

  /**
   * Adds a part to the parts application model.
   * 
   * @param part the part
   */
  public void addPart(PartApplication part) {
    partsModel.addElement(part);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Container#paint(java.awt.Graphics)
   */
  public void paint(Graphics g) {
    super.paint(g);
    partsList.repaint();
    statesList.repaint();
  }

  /**
   * Creates and shows the dialog.
   * 
   * @param eventDialog the parent event dialog component
   * @param element the element
   */
  public static void createAndShowGUI(EventDialog eventDialog, I_Element element) {
    ElementDialog d = new ElementDialog(eventDialog, element);
    d.pack();
    d.setLocationRelativeTo(d.getParent());
    d.setVisible(true);
  }

  /**
   * Creates and shows the dialog.
   * 
   * @param element the element
   * @param dialog the dialog
   */
  public static void createAndShowGUI(DataSourceDialog dialog, I_Element element) {
    ElementDialog d = new ElementDialog(dialog, element);
    d.pack();
    d.setLocationRelativeTo(d.getParent());
    d.setVisible(true);
  }
}
