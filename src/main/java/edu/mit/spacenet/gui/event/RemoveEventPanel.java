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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.gui.component.CheckBoxTree;
import edu.mit.spacenet.gui.component.CheckBoxTreeModel;
import edu.mit.spacenet.simulator.event.RemoveEvent;

/**
 * A panel for viewing and editing a remove event.
 * 
 * @author Paul Grogan
 */
public class RemoveEventPanel extends AbstractEventPanel {
  private static final long serialVersionUID = 769918023169742283L;

  private RemoveEvent event;

  private CheckBoxTreeModel elementModel;
  private CheckBoxTree elementTree;

  /**
   * Instantiates a new removes the event panel.
   * 
   * @param eventDialog the event dialog
   * @param event the event
   */
  public RemoveEventPanel(EventDialog eventDialog, RemoveEvent event) {
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
    c.anchor = GridBagConstraints.FIRST_LINE_END;
    add(new JLabel("Elements: "), c);

    c.gridx = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    elementModel = new CheckBoxTreeModel();
    elementTree = new CheckBoxTree(elementModel);
    elementTree.setRootVisible(false);
    elementTree.setShowsRootHandles(true);
    JScrollPane elementScroll = new JScrollPane(elementTree);
    elementScroll.setPreferredSize(new Dimension(200, 50));
    add(elementScroll, c);
  }

  private void initialize() {
    elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
    elementTree.setModel(elementModel);
    elementModel.setCheckedElements(event.getElements());
    elementTree.expandAll();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
   */
  @Override
  public RemoveEvent getEvent() {
    return event;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
   */
  @Override
  public void saveEvent() {
    event.getElements().clear();
    for (I_Element element : elementModel.getTopCheckedElements()) {
      event.getElements().add(getEventDialog().getElement(element.getUid()));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
   */
  @Override
  public void updateView() {
    Set<I_Element> checkedElements = elementModel.getTopCheckedElements();
    elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
    elementTree.setModel(elementModel);
    elementModel.setCheckedElements(checkedElements);
    elementTree.expandAll();
  }
}
