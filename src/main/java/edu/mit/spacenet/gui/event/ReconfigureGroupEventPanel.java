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
package edu.mit.spacenet.gui.event;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.StateType;
import edu.mit.spacenet.gui.component.CheckBoxTree;
import edu.mit.spacenet.gui.component.CheckBoxTreeModel;
import edu.mit.spacenet.simulator.event.ReconfigureGroupEvent;

/**
 * A panel for viewing and editing a reconfigure group event.
 * 
 * @author Paul Grogan
 */
public class ReconfigureGroupEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private ReconfigureGroupEvent event;
	
	private CheckBoxTreeModel elementModel;
	private CheckBoxTree elementTree;
	private JComboBox ddlStateType;
	
	/**
	 * Instantiates a new reconfigure group event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public ReconfigureGroupEventPanel(EventDialog eventDialog, ReconfigureGroupEvent event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("New State: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Elements: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlStateType = new JComboBox();
		for(StateType t : StateType.values()) {
			ddlStateType.addItem(t);
		}
		ddlStateType.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1271331296677711150L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof StateType)
					label.setIcon(((StateType)value).getIcon());
				return label;
			}
		});
		add(ddlStateType, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		elementModel = new CheckBoxTreeModel();
		elementModel.setChildrenChecked(false);
		elementTree = new CheckBoxTree(elementModel);
		elementTree.setRootVisible(false);
		elementTree.setShowsRootHandles(true);
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,50));
		add(elementScroll, c);
	}
	private void initialize() {
		ddlStateType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateView();
			}
		});
		ddlStateType.setSelectedItem(event.getStateType());
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementModel.setChildrenChecked(false);
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(event.getElements());
		elementTree.expandAll();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public ReconfigureGroupEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setStateType((StateType)ddlStateType.getSelectedItem());
		event.getElements().clear();
		for(I_Element element : elementModel.getTopCheckedElements()) {
			event.getElements().add(getEventDialog().getElement(element.getUid()));
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		Set<I_Element> checkedElements = elementModel.getTopCheckedElements();
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementModel.setChildrenChecked(false);
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(checkedElements);
		elementTree.expandAll();
	}
}
