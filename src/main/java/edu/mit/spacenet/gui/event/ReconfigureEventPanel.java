/*
 * Copyright (c) 2010 MIT Strategic Engineering Research Group
 * 
 * This file is part of SpaceNet 2.5r2.
 * 
 * SpaceNet 2.5r2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SpaceNet 2.5r2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SpaceNet 2.5r2.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.mit.spacenet.gui.event;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.simulator.event.ReconfigureEvent;

/**
 * A panel for viewing and editing a reconfigure event.
 * 
 * @author Paul Grogan
 */
public class ReconfigureEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private ReconfigureEvent event;
	
	private ElementTree elementTree;
	private JComboBox ddlCurrentState, ddlNewState;
	
	/**
	 * Instantiates a new reconfigure event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public ReconfigureEventPanel(EventDialog eventDialog, ReconfigureEvent event) {
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
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Element: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Current State: "), c);
		c.gridy++;
		add(new JLabel("New State: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		elementTree = new ElementTree();
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,50));
		add(elementScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ddlCurrentState = new JComboBox();
		ddlCurrentState.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1271331296677711150L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof I_State)
					label.setIcon((((I_State)value).getStateType()).getIcon());
				return label;
			}
		});
		ddlCurrentState.setEnabled(false);
		add(ddlCurrentState, c);
		c.gridy++;
		ddlNewState = new JComboBox();
		ddlNewState.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1271331296677711150L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof I_State)
					label.setIcon((((I_State)value).getStateType()).getIcon());
				return label;
			}
		});
		ddlNewState.setEnabled(false);
		add(ddlNewState, c);
	}
	private void initialize() {
		elementTree.setRoot(getEventDialog().getSimNode());
		if(event.getElement()!=null && getEventDialog().getSimElement(event.getElement().getUid())!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(event.getElement().getUid()));
			for(I_State simState : getEventDialog().getSimElement(event.getElement().getUid()).getStates()) {
				ddlNewState.addItem(simState);
				if(simState.equals(event.getState())) {
					ddlNewState.setSelectedItem(simState);
				}
			}
		}
		elementTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				updateStates();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public ReconfigureEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setElement(null);
		event.setState(null);
		
		if(elementTree.getElementSelection()!=null) {
			event.setElement(getEventDialog().getElement(((I_Element)elementTree.getElementSelection()).getUid()));
			if(ddlNewState.getSelectedItem()!=null&&event.getElement()!=null) {
				for(I_State s : event.getElement().getStates()) {
					if(s.equals(ddlNewState.getSelectedItem())) {
						event.setState(s);
						break;
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		I_Element selectedItem = elementTree.getElementSelection();
		TreeSelectionListener treeListener = elementTree.getTreeSelectionListeners()[0];
		elementTree.removeTreeSelectionListener(treeListener);
		elementTree.setRoot(getEventDialog().getSimNode());
		elementTree.setSelection(selectedItem);
		elementTree.addTreeSelectionListener(treeListener);
		updateStates();
	}
	private void updateStates() {
		I_State state = (I_State)ddlNewState.getSelectedItem();
		
		ddlCurrentState.removeAllItems();
		ddlNewState.removeAllItems();
		
		if(elementTree.getSelectionCount()==0) {
			ddlNewState.setEnabled(false);
			getEventDialog().setOkButtonEnabled(false);
		} else {
			ddlNewState.setEnabled(true);
			getEventDialog().setOkButtonEnabled(true);
			
			for(I_State s : elementTree.getElementSelection().getStates()) {
				ddlCurrentState.addItem(s);
				ddlNewState.addItem(s);
				
				if(elementTree.getElementSelection().getCurrentState().equals(s)) {
					ddlCurrentState.setSelectedItem(s);
				}
				if(s.equals(state)) {
					ddlNewState.setSelectedItem(s);
				}
			}
		}
	}
}
