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
package edu.mit.spacenet.gui.mission;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JDateChooser;

import edu.mit.spacenet.domain.model.I_DemandModel;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.event.EventDialog;
import edu.mit.spacenet.gui.model.DemandModelDialog;
import edu.mit.spacenet.simulator.event.I_Event;

/**
 * A panel used to display and edit mission-specific information.
 */
public class MissionPanel extends JPanel {
	private static final long serialVersionUID = -1544896538049800449L;

	private MissionSplitPane missionSplitPane;
	
	private JComboBox<Node> ddlOrigin;
	private JComboBox<Node> ddlDestination;
	private JComboBox<Node> ddlReturnOrigin;
	private JComboBox<Node> ddlReturnDestination;
	private JTextField txtName;
	private JDateChooser calStartDate;
	private DefaultListModel<I_DemandModel> demandModelsModel;
	private JList<I_DemandModel> demandModelsList;
	private EventsTable eventsTable;
	private JButton btnEditDemandModel, btnRemoveDemandModel, btnEditEvent, btnRemoveEvent;
	
	/**
	 * Instantiates a new mission panel.
	 * 
	 * @param missionSplitPane the mission split pane
	 */
	public MissionPanel(MissionSplitPane missionSplitPane) {
		this.missionSplitPane = missionSplitPane;
		buildPanel();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.weightx = 0;
		c.gridy = 0;
		c.gridx = 0;
		add(new JLabel("Name: "), c);
		c.gridy++;
		add(new JLabel("Start Date: "), c);
		c.gridy++;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		JPanel departurePanel = new JPanel();
		departurePanel.setBorder(BorderFactory.createTitledBorder("Departure Trip"));
		departurePanel.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.insets = new Insets(2,2,2,2);
		g.anchor = GridBagConstraints.LINE_END;
		departurePanel.add(new JLabel("From: "), g);
		g.gridy++;
		departurePanel.add(new JLabel("To: "), g);
		g.gridy--;
		g.gridx++;
		g.anchor = GridBagConstraints.LINE_START;
		g.fill = GridBagConstraints.BOTH;
		g.weightx = 1;
		ddlOrigin = new ContainerComboBox<Node>();
		ddlOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					missionSplitPane.getMission().setOrigin((Node)ddlOrigin.getSelectedItem());
				}
			}
		});
		departurePanel.add(ddlOrigin, g);
		g.gridy++;
		ddlDestination = new ContainerComboBox<Node>();
		ddlDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					missionSplitPane.getMission().setDestination((Node)ddlDestination.getSelectedItem());
				}
			}
		});
		departurePanel.add(ddlDestination, g);
		JPanel returnPanel = new JPanel();
		returnPanel.setBorder(BorderFactory.createTitledBorder("Return Trip"));
		returnPanel.setLayout(new GridBagLayout());
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.insets = new Insets(2,2,2,2);
		g.anchor = GridBagConstraints.LINE_END;
		returnPanel.add(new JLabel("From: "), g);
		g.gridy++;
		returnPanel.add(new JLabel("To: "), g);
		g.gridy--;
		g.gridx++;
		g.anchor = GridBagConstraints.LINE_START;
		g.fill = GridBagConstraints.BOTH;
		g.weightx = 1;
		ddlReturnOrigin = new ContainerComboBox<Node>();
		ddlReturnOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				missionSplitPane.getMission().setReturnOrigin((Node)ddlReturnOrigin.getSelectedItem());
			}
		});
		returnPanel.add(ddlReturnOrigin, g);
		g.gridy++;
		ddlReturnDestination = new ContainerComboBox<Node>();
		ddlReturnDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				missionSplitPane.getMission().setReturnDestination((Node)ddlReturnDestination.getSelectedItem());
			}
		});
		returnPanel.add(ddlReturnDestination, g);

		JPanel nodePanel = new JPanel();
		nodePanel.setBorder(BorderFactory.createEmptyBorder());
		nodePanel.setLayout(new BoxLayout(nodePanel, BoxLayout.LINE_AXIS));
		nodePanel.add(departurePanel);
		nodePanel.add(returnPanel);
		add(nodePanel, c);
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Demand Models: "), c);
		c.gridy+=3;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Events: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		txtName = new JTextField(20);
		txtName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				missionSplitPane.getMission().setName(txtName.getText());
				eventsTable.updateView();
			}
		});
		add(txtName, c);
		c.gridy++;
		calStartDate = new JDateChooser();
		calStartDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equals("date")) {
					missionSplitPane.getMission().setStartDate(calStartDate.getDate());
					updateView();
				}
			}
		});
		add(calStartDate, c);
		c.gridy+=3;
		c.gridwidth = 2;
		c.gridx = 0;
		c.weighty = 0.1;
		c.weightx = 1;
		demandModelsModel = new DefaultListModel<I_DemandModel>();
		demandModelsList = new JList<I_DemandModel>(demandModelsModel);
		demandModelsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1271331296677711150L;
			public Component getListCellRendererComponent(JList<?> list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setIcon(((I_DemandModel)value).getDemandModelType().getIcon());
				return label;
			}
		});
		demandModelsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && demandModelsList.getSelectedIndex()>=0) {
					DemandModelDialog.createAndShowGUI(getThis(), demandModelsList.getSelectedValue());
				}
			}
		});
		demandModelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		demandModelsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateButtons();
			}
		});
		JScrollPane demandModelsScroll = new JScrollPane(demandModelsList);
		demandModelsScroll.setPreferredSize(new Dimension(125,20));
		add(demandModelsScroll, c);
		c.gridy++;
		c.gridx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		JPanel demandsButtonPanel = new JPanel();
		demandsButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		demandsButtonPanel.add(new AddMissionDemandModelButton(this));
		
		btnEditDemandModel = new JButton("Edit", new ImageIcon(getClass().getClassLoader().getResource("icons/comment_edit.png")));
		btnEditDemandModel.setMargin(new Insets(3,3,3,3));
		btnEditDemandModel.setToolTipText("Edit Demand Model");
		btnEditDemandModel.setEnabled(false);
		btnEditDemandModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DemandModelDialog.createAndShowGUI(getThis(), demandModelsList.getSelectedValue());
			}
		});
		demandsButtonPanel.add(btnEditDemandModel);
		
		btnRemoveDemandModel = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/comment_delete.png")));
		btnRemoveDemandModel.setMargin(new Insets(3,3,3,3));
		btnRemoveDemandModel.setToolTipText("Remove Demand Model");
		btnRemoveDemandModel.setEnabled(false);
		btnRemoveDemandModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				missionSplitPane.getMission().getDemandModels().remove(demandModelsList.getSelectedValue());
				updateView();
			}
		});
		demandsButtonPanel.add(btnRemoveDemandModel);
		add(demandsButtonPanel, c);
		
		c.gridy+=2;
		c.weighty = 0.9;
		c.fill = GridBagConstraints.BOTH;
		eventsTable = new EventsTable(this);
		eventsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && eventsTable.getSelectedRow()>=0) {
					if(eventsTable.isEditing()) eventsTable.getCellEditor().stopCellEditing();
					EventDialog.createAndShowGUI(getThis(), eventsTable.getEvent(eventsTable.getSelectedRow()));
				}
			}
		});
		eventsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateButtons();
			}
		});
		JScrollPane eventScroll = new JScrollPane(eventsTable);
		eventScroll.setBackground(Color.WHITE);
		eventScroll.getViewport().setBackground(Color.WHITE);
		eventScroll.setPreferredSize(new Dimension(150,150));
		add(eventScroll, c);
		
		JPanel eventButtonPanel = new JPanel();
		eventButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		eventButtonPanel.add(new AddEventButton(this));
		
		btnEditEvent = new JButton("Edit", new ImageIcon(getClass().getClassLoader().getResource("icons/cog_edit.png")));
		btnEditEvent.setMargin(new Insets(3,3,3,3));
		btnEditEvent.setToolTipText("Edit Event");
		btnEditEvent.setEnabled(false);
		btnEditEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(eventsTable.isEditing()) eventsTable.getCellEditor().stopCellEditing();
				EventDialog.createAndShowGUI(getThis(), eventsTable.getEvent(eventsTable.getSelectedRow()));
			}
		});
		eventButtonPanel.add(btnEditEvent);
		
		btnRemoveEvent = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/cog_delete.png")));
		btnRemoveEvent.setMargin(new Insets(3,3,3,3));
		btnRemoveEvent.setToolTipText("Remove Event");
		btnRemoveEvent.setEnabled(false);
		btnRemoveEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(eventsTable.isEditing()) eventsTable.getCellEditor().stopCellEditing();
				ArrayList<I_Event> events = new ArrayList<I_Event>();
				for(int row : eventsTable.getSelectedRows()) {
					events.add(eventsTable.getEvent(row));
				}
				JPanel warningPanel = new JPanel();
				warningPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				c.insets = new Insets(2,2,2,2);
				c.gridx = 0;
				c.gridy = 0;
				c.weightx = 0;
				c.weighty = 0;
				c.anchor = GridBagConstraints.LINE_START;
				c.fill = GridBagConstraints.BOTH;
				warningPanel.add(new JLabel("Permanently delete the following events?"), c);
				c.gridy++;
				c.weighty = 1;
				DefaultListModel<I_Event> eventsList = new DefaultListModel<I_Event>();
				for(I_Event v : events) {
					eventsList.addElement(v);
				}
				warningPanel.add(new JScrollPane(new JList<I_Event>(eventsList)), c);
				int answer = JOptionPane.showOptionDialog(missionSplitPane, 
						warningPanel, 
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
			    if (answer == JOptionPane.YES_OPTION) {
			    	for(I_Event event : events) {
			    		missionSplitPane.getMission().getEventList().remove(event);
					}
			    	
			    }
			    missionSplitPane.getMissionsTab().updateView();
			}
		});
		eventButtonPanel.add(btnRemoveEvent);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		add(eventButtonPanel, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		JButton btnOk = new JButton("Save", new ImageIcon(getClass().getClassLoader().getResource("icons/application_go.png")));
		btnOk.setMargin(new Insets(3,3,3,3));
		btnOk.setToolTipText("Save Mission");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				missionSplitPane.getMissionsTab().editMissions();
			}
		});
		add(btnOk, c);
	}
	
	/**
	 * Gets this.
	 * 
	 * @return this
	 */
	private MissionPanel getThis() { return this; }
	
	/**
	 * Initializes the panel for a new mission.
	 */
	public void initialize() {
		txtName.setEnabled(missionSplitPane.getMission()!=null);
		calStartDate.setEnabled(missionSplitPane.getMission()!=null);
		ddlOrigin.setEnabled(missionSplitPane.getMission()!=null);
		ddlDestination.setEnabled(missionSplitPane.getMission()!=null);
		ddlReturnOrigin.setEnabled(missionSplitPane.getMission()!=null);
		ddlReturnDestination.setEnabled(missionSplitPane.getMission()!=null);
		demandModelsList.setEnabled(missionSplitPane.getMission()!=null);
		eventsTable.setEnabled(missionSplitPane.getMission()!=null);
		if(missionSplitPane.getMission()!=null) {
			txtName.setText(missionSplitPane.getMission().getName());
			calStartDate.setDate(missionSplitPane.getMission().getStartDate());
			Node origin = missionSplitPane.getMission().getOrigin();
			Node destination = missionSplitPane.getMission().getDestination();
			Node returnOrigin = missionSplitPane.getMission().getReturnOrigin();
			Node returnDestination = missionSplitPane.getMission().getReturnDestination();
			ddlOrigin.removeAllItems();
			ddlDestination.removeAllItems();
			ddlReturnOrigin.removeAllItems();
			ddlReturnDestination.removeAllItems();
			ddlReturnOrigin.addItem(null);
			ddlReturnDestination.addItem(null);
			for(Node n : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getNetwork().getNodes()) {
				ddlOrigin.addItem(n);
				ddlDestination.addItem(n);
				ddlReturnOrigin.addItem(n);
				ddlReturnDestination.addItem(n);
			}
			ddlOrigin.setSelectedItem(origin);
			ddlDestination.setSelectedItem(destination);
			ddlReturnOrigin.setSelectedItem(returnOrigin);
			ddlReturnDestination.setSelectedItem(returnDestination);
			
			demandModelsModel.clear();
			for(I_DemandModel m : missionSplitPane.getMission().getDemandModels()) {
				demandModelsModel.addElement(m);
			}
		}
		eventsTable.initialize();
		updateButtons();
	}

	/**
	 * Updates the demand model and event-related buttons.
	 */
	private void updateButtons() {
		if(demandModelsList.getSelectedIndices().length == 1) {
			btnEditDemandModel.setEnabled(true);
			btnRemoveDemandModel.setEnabled(true);
		} else if(demandModelsList.getSelectedIndices().length > 1) {
			btnEditDemandModel.setEnabled(false);
			btnRemoveDemandModel.setEnabled(true);
		} else {
			btnEditDemandModel.setEnabled(false);
			btnRemoveDemandModel.setEnabled(false);
		}
		if(eventsTable.getSelectedRowCount() == 1) {
			btnEditEvent.setEnabled(true);
			btnRemoveEvent.setEnabled(true);
		} else if(eventsTable.getSelectedRowCount() > 1) {
			btnEditEvent.setEnabled(false);
			btnRemoveEvent.setEnabled(true);
		} else {
			btnEditEvent.setEnabled(false);
			btnRemoveEvent.setEnabled(false);
		}
	}
	
	/**
	 * Updates the view.
	 */
	public void updateView() {
		Collections.sort(missionSplitPane.getMission().getEventList());
		eventsTable.updateView();
		demandModelsModel.clear();
		for(I_DemandModel m : missionSplitPane.getMission().getDemandModels()) {
			demandModelsModel.addElement(m);
		}
		updateButtons();
	}
	
	/**
	 * Gets the mission split pane.
	 * 
	 * @return the mission split pane
	 */
	public MissionSplitPane getMissionSplitPane() {
		return missionSplitPane;
	}
	
	/**
	 * Gets the events table.
	 * 
	 * @return the events table
	 */
	public EventsTable getEventsTable() {
		return eventsTable;
	}
}
