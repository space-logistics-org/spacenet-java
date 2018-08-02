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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.SortedMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.I_State;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.simulator.event.ExplorationProcess;

/**
 * A panel for viewing and editing an exploration process.
 * 
 * @author Paul Grogan
 */
public class ExplorationProcessPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private ExplorationProcess event;
	
	private SpinnerNumberModel durationModel, evaPerWeekModel, evaDurationModel;
	private JSpinner durationSpinner, evaPerWeekSpinner, evaDurationSpinner;
	private ElementTree elementTree;
	private JButton btnAddDemand, btnRemoveDemand;
	private DemandTable demandsList;
	
	private EvaTable contentsTable;
	
	/**
	 * Instantiates a new exploration process panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public ExplorationProcessPanel(EventDialog eventDialog, ExplorationProcess event) {
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
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Duration: "), c);
		c.gridy++;
		add(new JLabel("Number EVAs: "), c);
		c.gridy++;
		add(new JLabel ("EVA Duration: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Crew Location: "), c);
		c.gridy++;
		add(new JLabel("Crew: "), c);
		c.gridy++;
		add(new JLabel("Add'l Demands: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		durationModel = new SpinnerNumberModel(0, 0, 1000,.1);
		durationSpinner = new JSpinner(durationModel);
		durationSpinner.setPreferredSize(new Dimension(75, 20));
		add(new UnitsWrapper(durationSpinner, "days"), c);
		c.gridy++;
		evaPerWeekModel = new SpinnerNumberModel(0, 0, 14, 0.1);
		evaPerWeekSpinner = new JSpinner(evaPerWeekModel);
		evaPerWeekSpinner.setPreferredSize(new Dimension(75, 20));
		add(new UnitsWrapper(evaPerWeekSpinner, "per week"), c);
		c.gridy++;		
		evaDurationModel = new SpinnerNumberModel(0, 0, 20, 0.5);
		evaDurationSpinner = new JSpinner(evaDurationModel);
		evaDurationSpinner.setPreferredSize(new Dimension(75, 20));
		add(new UnitsWrapper(evaDurationSpinner, "hours"), c);
		c.gridy++; 
		c.weightx = 1;
		c.weighty = 0.5;
		c.fill = GridBagConstraints.BOTH;
		elementTree = new ElementTree();
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setBackground(Color.WHITE);
		elementScroll.getViewport().setBackground(Color.WHITE);
		elementScroll.setPreferredSize(new Dimension(300,100));
		add(elementScroll, c);
		c.gridy++;
		contentsTable = new EvaTable();
		contentsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane contentsScroll = new JScrollPane(contentsTable);
		contentsScroll.setBackground(Color.WHITE);
		contentsScroll.getViewport().setBackground(Color.WHITE);
		contentsScroll.setPreferredSize(new Dimension(300,100));
		add(contentsScroll, c);
		c.gridy++;
		demandsList = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		JScrollPane demandsScroll = new JScrollPane(demandsList);
		demandsScroll.setBackground(Color.WHITE);
		demandsScroll.getViewport().setBackground(Color.WHITE);
		demandsScroll.setPreferredSize(new Dimension(200,50));
		add(demandsScroll, c);
		
		c.gridy = 5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.gridy++;
		JPanel demandsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		btnAddDemand = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_put.png")));
		btnAddDemand.setMargin(new Insets(3,3,3,3));
		btnAddDemand.setToolTipText("Add Demand");
		demandsButtonPanel.add(btnAddDemand);
		btnRemoveDemand = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/basket_remove.png")));
		btnRemoveDemand.setMargin(new Insets(3,3,3,3));
		btnRemoveDemand.setToolTipText("Remove Demand");
		btnRemoveDemand.setEnabled(false);
		demandsButtonPanel.add(btnRemoveDemand);
		add(demandsButtonPanel, c);
	}
	private void initialize() {
		durationModel.setValue(event.getDuration());
		evaPerWeekModel.setValue(event.getEvaPerWeek());
		evaDurationModel.setValue(event.getEvaDuration());
		
		elementTree.setRoot(getEventDialog().getSimNode());
		if(event.getVehicle()!=null 
				&& getEventDialog().getSimElement(event.getVehicle().getUid())!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(event.getVehicle().getUid()));
		}
		
		elementTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				getEventDialog().setOkButtonEnabled(elementTree.getSelectionCount() > 0
						&& elementTree.getSelection() instanceof I_Carrier);
				updateView();
			}
		});
		
		for(CrewMember c : event.getStateMap().keySet()) {
			CrewMember simCrew = (CrewMember)getEventDialog().getSimElement(c.getUid());
			if(simCrew != null) {
				if(event.getStateMap().get(c) == null) {
					contentsTable.getModel().put(simCrew, null, true);
				} else {
					for(I_State simState : simCrew.getStates()) {
						if(simState.equals(event.getStateMap().get(c))) {
							contentsTable.getModel().put(simCrew, simState, true);
						}
					}
				}
			}
		}
		
		for(Demand demand : event.getDemands()) {
			demandsList.addDemand(demand);
		}
		
		contentsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(contentsTable.getSelectedRowCount()>0) {
					demandsList.clearSelection();
				}
			}
		});
		demandsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(!demandsList.getSelectionModel().isSelectionEmpty()) {
					contentsTable.clearSelection();
					btnRemoveDemand.setEnabled(true);
				} else {
					btnRemoveDemand.setEnabled(true);
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
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public ExplorationProcess getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setDuration(durationModel.getNumber().doubleValue());
		event.setEvaPerWeek(evaPerWeekModel.getNumber().doubleValue());
		event.setEvaDuration(evaDurationModel.getNumber().doubleValue());

		event.setVehicle((I_Carrier)getEventDialog().getElement(elementTree.getElementSelection().getUid()));

		event.getStateMap().clear();
		for(CrewMember simCrew : contentsTable.getModel().getData().keySet()) {
			CrewMember crew = (CrewMember)getEventDialog().getElement(simCrew.getUid());
			if(crew != null) {
				if(contentsTable.getModel().getData().get(simCrew)==null) {
					event.getStateMap().put(crew, null);
				} else {
					for(I_State state : crew.getStates()) {
						if(state.equals(contentsTable.getModel().getData().get(simCrew))) {
							event.getStateMap().put(crew, state);
							break;
						}
					}
				}
			}
		}
		
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
		I_Element selectedItem = (I_Element)elementTree.getElementSelection();
		TreeSelectionListener treeListener = elementTree.getTreeSelectionListeners()[0];
		elementTree.removeTreeSelectionListener(treeListener);
		elementTree.setRoot(getEventDialog().getSimNode());
		if(selectedItem!=null
				&& getEventDialog().getSimElement(selectedItem.getUid())!=null) {
			elementTree.setSelection(getEventDialog().getSimElement(selectedItem.getUid()));
		}
		elementTree.addTreeSelectionListener(treeListener);
		
		if(elementTree.getElementSelection() instanceof I_Carrier) {
			SortedMap<CrewMember, I_State> stateMap = contentsTable.getModel().getData();
			contentsTable.getModel().clear();
			for(I_Element element : ((I_Carrier)elementTree.getElementSelection()).getContents()) {
				if(element instanceof CrewMember) {
					if(stateMap.keySet().contains(element)) {
						contentsTable.getModel().put((CrewMember)element, stateMap.get(element), true);
					} else {
						contentsTable.getModel().put((CrewMember)element, stateMap.get(element), false);
					}
				}
			}
		} else {
			contentsTable.getModel().clear();
		}
	}
}