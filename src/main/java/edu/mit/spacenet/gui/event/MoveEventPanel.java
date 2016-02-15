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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.gui.component.CapacityPanel;
import edu.mit.spacenet.gui.component.CheckBoxTree;
import edu.mit.spacenet.gui.component.CheckBoxTreeModel;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.simulator.event.MoveEvent;

/**
 * A panel for viewing and editing a move event.
 * 
 * @author Paul Grogan
 */
public class MoveEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private MoveEvent event;
	
	private JComboBox containerCombo;
	private CapacityPanel capacityPanel;
	
	private CheckBoxTreeModel elementModel;
	private CheckBoxTree elementTree;
	
	/**
	 * Instantiates a new move event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public MoveEventPanel(EventDialog eventDialog, MoveEvent event) {
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
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Move to: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		containerCombo = new ContainerComboBox();
		add(containerCombo, c);
		c.gridy++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		capacityPanel = new CapacityPanel();
		add(capacityPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Elements: "), c);
		c.gridy++;
		c.gridwidth = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
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
		containerCombo.addItem(event.getContainer());
		containerCombo.setSelectedItem(event.getContainer());
		
		containerCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCapacities();
			}
		});
		
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(event.getElements());
		elementModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) { 
				updateCapacities(); 
			}
			public void treeNodesInserted(TreeModelEvent e) { }
			public void treeNodesRemoved(TreeModelEvent e) { }
			public void treeStructureChanged(TreeModelEvent e) { }
		});
		elementTree.expandAll();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public MoveEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		if(containerCombo.getSelectedItem() instanceof I_Element) {
			event.setContainer((I_Container)getEventDialog().getElement(((I_Element)containerCombo.getSelectedItem()).getUid()));
		} else {
			event.setContainer((I_Container)containerCombo.getSelectedItem());
		}
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
		I_Container container = (I_Container)containerCombo.getSelectedItem();
		containerCombo.removeAllItems();
		containerCombo.addItem(getEventDialog().getNode());
		if(getEventDialog().getNode().equals(container)) 
			containerCombo.setSelectedItem(container);
		for(I_Element element: getEventDialog().getSimNode().getCompleteContents()) {
			if(element instanceof I_Container) {
				containerCombo.addItem(element);
				if(element.equals(container)) {
					containerCombo.setSelectedItem(element);
				}
			}
		}
		Set<I_Element> checkedElements = elementModel.getTopCheckedElements();
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(checkedElements);
		elementModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) { 
				updateCapacities(); 
			}
			public void treeNodesInserted(TreeModelEvent e) { }
			public void treeNodesRemoved(TreeModelEvent e) { }
			public void treeStructureChanged(TreeModelEvent e) { }
		});
		elementTree.expandAll();
		updateCapacities();
	}
	
	/**
	 * Update capacities.
	 */
	private void updateCapacities() {
		double mass = 0;
		double volume = 0;
		int crew = 0;
		for(I_Element e : elementModel.getTopCheckedElements()) {
			if(!e.isInside((I_Container)containerCombo.getSelectedItem())) {
				if(e instanceof I_Carrier) crew += ((I_Carrier)e).getCrewSize();
				
				if(e instanceof CrewMember) crew++;
				else {
					mass += e.getTotalMass();
					volume += e.getTotalVolume();
				}
			}
		}
		if(containerCombo.getSelectedItem() instanceof I_Carrier) {
			I_Carrier v = (I_Carrier)containerCombo.getSelectedItem();
			mass += v.getCargoMass();
			volume += v.getCargoVolume();
			crew += v.getCrewSize();
			capacityPanel.updateCapacities(v, mass, volume, crew);
		} else {
			mass += getEventDialog().getSimNode().getCargoMass();
			volume += getEventDialog().getSimNode().getCargoVolume();
			crew += getEventDialog().getSimNode().getCrewSize();
			capacityPanel.updateCapacities(getEventDialog().getSimNode(), mass, volume, crew);
		}
	}
}
