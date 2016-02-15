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

import java.awt.Color;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.simulator.event.DemandEvent;

/**
 * A panel for viewing and editing a demand event.
 * 
 * @author Paul Grogan
 */
public class DemandEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private DemandEvent event;
	
	private ElementTree elementTree;
	private DemandTable demandsList;
	private JScrollPane demandsScroll;
	private JButton btnAddDemand, btnRemoveDemand;
	
	/**
	 * Instantiates a new demand event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public DemandEventPanel(EventDialog eventDialog, DemandEvent event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Element: "), c);
		c.gridy++;
		add(new JLabel("Demands: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = .5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		elementTree = new ElementTree();
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,100));
		add(elementScroll, c);
		
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1;
		c.weighty = 1;
		demandsList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		demandsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		demandsScroll = new JScrollPane(demandsList);
		demandsScroll.setPreferredSize(new Dimension(400,50));
		demandsScroll.setBackground(Color.WHITE);
		demandsScroll.getViewport().setBackground(Color.WHITE);
		add(demandsScroll, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		JPanel demandButtonPanel = new JPanel();
		demandButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		btnAddDemand = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
		btnAddDemand.setMargin(new Insets(3,3,3,3));
		btnAddDemand.setToolTipText("Add Demand");
		demandButtonPanel.add(btnAddDemand);
		btnRemoveDemand = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
		btnRemoveDemand.setMargin(new Insets(3,3,3,3));
		btnAddDemand.setToolTipText("Remove Demand");
		btnRemoveDemand.setEnabled(false);
		demandButtonPanel.add(btnRemoveDemand);
		add(demandButtonPanel, c);
	}
	private void initialize() {
		elementTree.setRoot(getEventDialog().getSimNode());
		if(event.getElement()!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(event.getElement().getUid()));
		}
		elementTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				getEventDialog().setOkButtonEnabled(elementTree.getSelection()!=null);
			}
		});
		for(Demand demand : event.getDemands()) {
			demandsList.addDemand(demand);
		}
		demandsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(demandsList.getSelectedRows().length >= 1) {
					btnRemoveDemand.setEnabled(true);
				} else {
					btnRemoveDemand.setEnabled(false);
				}
			}
		});
		btnAddDemand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demandsList.addDemand(new Demand());
			}
		});
		btnRemoveDemand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Demand> demands = new ArrayList<Demand>();
				for(int row : demandsList.getSelectedRows()) {
					demands.add(demandsList.getDemand(row));
				}
				for(Demand demand : demands) {
					demandsList.remove(demand);
				}
				updateView();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public DemandEvent getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setElement(getEventDialog().getElement(elementTree.getElementSelection().getUid()));
		
		event.getDemands().clear();
		for(Demand demand: demandsList.getDemands()) {
			if(demand.getResource()!=null)
				event.getDemands().add(demand);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		I_Element selectedElement = elementTree.getElementSelection();
		elementTree.setRoot(getEventDialog().getSimNode());
		if(selectedElement != null
				&& getEventDialog().getSimElement(selectedElement.getUid())!=null)
			elementTree.setSelection(getEventDialog().getSimElement(selectedElement.getUid()));
	}
}