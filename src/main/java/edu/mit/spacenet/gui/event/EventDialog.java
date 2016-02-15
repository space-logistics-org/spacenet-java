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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.Location;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.gui.mission.MissionPanel;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.PreSimulator;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.util.DateFunctions;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * The dialog used to view and edit events.
 * 
 * @author Paul Grogan
 */
public class EventDialog extends JDialog {
	private static final long serialVersionUID = -4959320811142445509L;

	private MissionPanel missionPanel;
	private I_Event event;
	protected PreSimulator preSim;
	
	private double initTime;
	private int initPriority;
	private Location initLocation;
	
	private SpinnerNumberModel timeModel;
	private JSpinner timeSpinner;
	private JTextField nameText;
	private JComboBox priorityCombo, nodeCombo;
	private JButton okButton, cancelButton;
	
	private AbstractEventPanel eventPanel;
	
	/**
	 * Instantiates a new event dialog.
	 * 
	 * @param missionPanel the mission panel
	 * @param event the event
	 */
	public EventDialog(MissionPanel missionPanel, I_Event event) {
		super(SpaceNetFrame.getInstance(), event.getEventType().getName(), true);
		this.missionPanel = missionPanel;
		this.event = event;
		initTime = event.getTime();
		initPriority = event.getPriority();
		initLocation = event.getLocation();
		preSim = new PreSimulator(missionPanel.getMissionSplitPane().getMission().getScenario());
		preSim.simulate(DateFunctions.getDaysBetween(missionPanel.getMissionSplitPane().getMission().getScenario().getStartDate(), 
				missionPanel.getMissionSplitPane().getMission().getStartDate()) + initTime, initPriority);
		buildDialog();
		initialize();
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		contentPanel.add(buildHeaderPanel(), c);
		c.gridy++;
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setPreferredSize(new Dimension(1,1));
		contentPanel.add(separator, c);
		c.gridy++;
		c.weighty = 1;
		eventPanel = EventPanelFactory.createEventPanel(this, event);
		contentPanel.add(eventPanel, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		contentPanel.add(buttonPanel, c);
		okButton = new JButton("OK", new ImageIcon(getClass().getClassLoader().getResource("icons/cog_go.png")));
		buttonPanel.add(okButton);
		cancelButton = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("icons/cog_delete.png")));
		buttonPanel.add(cancelButton);

		setContentPane(contentPanel);
		getRootPane().setDefaultButton(okButton);
		if(eventPanel instanceof SpaceTransportPanel)
			setMinimumSize(new Dimension(400,600)); // hack for panic layout
		else
			setMinimumSize(new Dimension(300,400));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		eventPanel.updateView(); // TODO this should not be needed
	}
	
	/**
	 * Builds the header panel.
	 *
	 * @return the header panel
	 */
	private JPanel buildHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		headerPanel.add(new JLabel("Name: "), c);
		c.gridy = 1;
		headerPanel.add(new JLabel("Node: "), c);
		c.gridy = 2;
		headerPanel.add(new JLabel("Time: "), c);
		c.gridx+=2;
		c.weightx = .5;
		headerPanel.add(new JLabel("Priority: "), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		nameText = new JTextField(event.getName());
		headerPanel.add(nameText, c);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		nodeCombo = new ContainerComboBox();
		for(Node n : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getNetwork().getNodes()) {
			nodeCombo.addItem(n);
			if(n.equals(event.getLocation())) nodeCombo.setSelectedItem(n);
		}
		headerPanel.add(nodeCombo, c);
		
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		timeModel = new SpinnerNumberModel(event.getTime(), 0, Double.MAX_VALUE, GlobalParameters.getTimePrecision());
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setPreferredSize(new Dimension(100,20));
		timeSpinner.setToolTipText("Execution time (days) relative to the start of the mission");
		headerPanel.add(new UnitsWrapper(timeSpinner, "days"), c);
		
		c.gridx+=2;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		priorityCombo = new JComboBox();
		priorityCombo.setPreferredSize(new Dimension(50,20));
		priorityCombo.setToolTipText("Priority over concurrent events");
		for(int i = 1; i <= 5; i++) priorityCombo.addItem(i);
		priorityCombo.setSelectedItem(Math.max(1, Math.min(event.getPriority(),5)));
		headerPanel.add(priorityCombo, c);
		return headerPanel;
	}

	/**
	 * Initializes the panel for a new event.
	 */
	private void initialize() {
		nodeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					event.setLocation((Node)nodeCombo.getSelectedItem());
					runSimulation();
				}
			}
		});
		priorityCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					event.setPriority(((Integer)priorityCombo.getSelectedItem()).intValue());
					runSimulation();
				}
			}
		});
		timeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				event.setTime(Double.parseDouble(timeModel.getValue().toString()));
				runSimulation();
			}
		});
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveEvent();
				missionPanel.getMissionSplitPane().getMissionsTab().updateView();
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				event.setTime(initTime);
				event.setPriority(initPriority);
				event.setLocation(initLocation);
				dispose();
			}
		});
	}
	
	/**
	 * Save event.
	 */
	private void saveEvent() {
		event.setTime(timeModel.getNumber().doubleValue());
		event.setPriority((Integer)priorityCombo.getSelectedItem());
		event.setName(nameText.getText());
		event.setLocation((Node)nodeCombo.getSelectedItem());
		eventPanel.saveEvent();
		if(!missionPanel.getMissionSplitPane().getMission().getEventList().contains(event)) {
			missionPanel.getMissionSplitPane().getMission().getEventList().add(event);
		}
	}
	
	/**
	 * Runs the simulation.
	 */
	private void runSimulation() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			public Void doInBackground() throws Exception {
				SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Pre-simulating...");
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				preSim = new PreSimulator(missionPanel.getMissionSplitPane().getMissionsTab().getScenarioPanel().getScenario());
				double time = DateFunctions.getDaysBetween(getScenario().getStartDate(), 
						missionPanel.getMissionSplitPane().getMission().getStartDate()) + timeModel.getNumber().doubleValue();
				int priority = ((Integer)priorityCombo.getSelectedItem()).intValue();
				preSim.simulate(time, priority);
				return null;
			}
			public void done() {
				eventPanel.updateView(); // TODO change to repaint method call
				SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
				setCursor(Cursor.getDefaultCursor());
			}
		};
		worker.execute();
	}
	
	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public I_Event getEvent() {
		return event;
	}
	
	/**
	 * Gets the mission panel.
	 * 
	 * @return the mission panel
	 */
	public MissionPanel getMissionPanel() {
		return missionPanel;
	}
	
	/**
	 * Gets the scenario.
	 *
	 * @return the scenario
	 */
	private Scenario getScenario() {
		return missionPanel.getMissionSplitPane().getMissionsTab().getScenarioPanel().getScenario();
	}
	
	/**
	 * Sets the ok button enabled.
	 * 
	 * @param enabled the new ok button enabled
	 */
	public void setOkButtonEnabled(boolean enabled) {
		okButton.setEnabled(enabled);
	}
	
	/**
	 * Creates the and show gui.
	 * 
	 * @param missionPanel the mission panel
	 * @param event the event
	 */
	public static void createAndShowGUI(MissionPanel missionPanel, I_Event event) {
		EventDialog d = new EventDialog(missionPanel, event);
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
	
	/**
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public Node getNode() {
		return (Node)nodeCombo.getSelectedItem();
	}
	
	/**
	 * Gets the sim node.
	 * 
	 * @return the sim node
	 */
	public Node getSimNode() {
		return preSim.getScenario().getNetwork().getNodeByTid(((Node)nodeCombo.getSelectedItem()).getTid());
	}
	
	/**
	 * Gets the sim element.
	 * 
	 * @param uid the uid
	 * 
	 * @return the sim element
	 */
	public I_Element getSimElement(int uid) {
		return preSim.getScenario().getNetwork().getRegistrar().get(uid);
	}
	
	/**
	 * Gets the element.
	 * 
	 * @param uid the uid
	 * 
	 * @return the element
	 */
	public I_Element getElement(int uid) {
		return getScenario().getElementByUid(uid);
	}
}
