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
		c.insets = new Insets(2,2,2,2);

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
		elementScroll.setPreferredSize(new Dimension(200,50));
		add(elementScroll, c);
	}
	private void initialize() {
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(event.getElements());
		elementTree.expandAll();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public RemoveEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
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
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(checkedElements);
		elementTree.expandAll();
	}
}
