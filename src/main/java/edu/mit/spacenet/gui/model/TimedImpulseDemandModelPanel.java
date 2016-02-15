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

import edu.mit.spacenet.domain.model.TimedImpulseDemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;

/**
 * The panel for viewing and editing timed impulse demand models.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class TimedImpulseDemandModelPanel extends AbstractDemandModelPanel {
	private static final long serialVersionUID = -966209133130231178L;
	
	private TimedImpulseDemandModel demandModel;
	
	private DemandTable demandsList;
	private JScrollPane demandsScroll;
	private JButton btnRemoveDemand;
	
	/**
	 * Instantiates a new timed impulse demand model panel.
	 * 
	 * @param demandModelDialog the demand model dialog
	 * @param demandModel the demand model
	 */
	public TimedImpulseDemandModelPanel(DemandModelDialog demandModelDialog,
			TimedImpulseDemandModel demandModel) {
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
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Demands: "), c);

		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		demandsList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		demandsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		demandsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(demandsList.getSelectedRows().length >= 1) {
					btnRemoveDemand.setEnabled(true);
				} else {
					btnRemoveDemand.setEnabled(false);
				}
			}
		});
		demandsScroll = new JScrollPane(demandsList);
		demandsScroll.setPreferredSize(new Dimension(400,150));
		add(demandsScroll, c);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel demandButtonPanel = new JPanel();
		demandButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		add(demandButtonPanel, c);
		
		JButton btnAddDemand = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
		btnAddDemand.setToolTipText("Add a Demand");
		btnAddDemand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demandsList.addDemand(new Demand());
			}
		});
		demandButtonPanel.add(btnAddDemand);
		btnRemoveDemand = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
		btnRemoveDemand.setToolTipText("Remove Demand");
		btnRemoveDemand.setEnabled(false);
		btnRemoveDemand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Demand> demands = new ArrayList<Demand>();
				for(int row : demandsList.getSelectedRows()) {
					demands.add(demandsList.getDemand(row));
				}
				for(Demand demand : demands) {
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
		for(Demand demand : demandModel.getDemands()) {
			demandsList.addDemand(demand);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#getDemandModel()
	 */
	public TimedImpulseDemandModel getDemandModel() {
		return demandModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#saveDemandModel()
	 */
	public void saveDemandModel() {
		if(demandsList.isEditing()) demandsList.getCellEditor().stopCellEditing();
		demandModel.getDemands().clear();
		for(Demand demand : demandsList.getDemands()) {
			if(demand.getResource()!=null)
				demandModel.getDemands().add(demand);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#isDemandModelValid()
	 */
	@Override
	public boolean isDemandModelValid() {
		return true;
	}
}
