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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.BurnType;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.CheckBoxNode;
import edu.mit.spacenet.gui.component.CheckBoxTree;
import edu.mit.spacenet.gui.component.CheckBoxTreeModel;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.simulator.event.BurnStageItem;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.util.Formulae;

/**
 * A panel for viewing and editing a space transport.
 * 
 * @author Paul Grogan
 */
public class SpaceTransportPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private SpaceTransport event;
	
	private JLabel lblDestination, lblDuration;
	private JComboBox ddlTrajectory;
	private JButton btnAddBurn, btnAddStage;
	private JTabbedPane tabbedBurnPane;
	
	private CheckBoxTreeModel elementModel;
	private CheckBoxTree elementTree;
	private DecimalFormat deltaVFormat = new DecimalFormat("0.0");
	
	/**
	 * Instantiates a new space transport panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public SpaceTransportPanel(EventDialog eventDialog, SpaceTransport event) {
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
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Trajectory: "), c);
		c.gridy+=2;
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Elements: "), c);
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		ddlTrajectory = new ContainerComboBox();
		add(ddlTrajectory, c);
		c.gridy++;
		JPanel trajectoryPanel = new JPanel();
		trajectoryPanel.setLayout(new BoxLayout(trajectoryPanel, BoxLayout.PAGE_AXIS));
		lblDestination = new JLabel();
		trajectoryPanel.add(lblDestination);
		lblDuration = new JLabel();
		trajectoryPanel.add(lblDuration);
		add(trajectoryPanel, c);
		c.gridy+=2;
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		elementModel = new CheckBoxTreeModel();
		elementTree = new CheckBoxTree(elementModel);
		elementTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		elementTree.setRootVisible(false);
		elementTree.setShowsRootHandles(true);
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(200,100));
		add(elementScroll, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel sequenceButtonPanel = new JPanel();
		sequenceButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		btnAddBurn = new JButton("Burn");
		btnAddBurn.setToolTipText("Burn the selected propulsive vehicles");
		btnAddBurn.setIcon(BurnStageItem.BURN_ICON);
		btnAddBurn.setMargin(new Insets(3,3,3,3));
		btnAddBurn.setEnabled(false);
		sequenceButtonPanel.add(btnAddBurn);
		btnAddStage = new JButton("Stage");
		btnAddStage.setToolTipText("Stage the selected elements");
		btnAddStage.setIcon(BurnStageItem.STAGE_ICON);
		btnAddStage.setMargin(new Insets(3,3,3,3));
		btnAddStage.setEnabled(false);
		sequenceButtonPanel.add(btnAddStage);
		add(sequenceButtonPanel, c);
		c.weightx = 1;
		c.weighty = 1;
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		tabbedBurnPane = new JTabbedPane();
		tabbedBurnPane.setTabPlacement(JTabbedPane.TOP);
		add(tabbedBurnPane, c);
	}
	private void initialize() {
		ddlTrajectory.addItem(event.getEdge());
		ddlTrajectory.setSelectedItem(event.getEdge());
		ddlTrajectory.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					// TODO: should probably give some kind of warning
					createBurnPanels();
				}
			}
		});

		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(event.getElements());
		elementModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				updateButtons();
			}
			public void treeNodesInserted(TreeModelEvent e) { }
			public void treeNodesRemoved(TreeModelEvent e) { }
			public void treeStructureChanged(TreeModelEvent e) { }
		});
		
		if(event.getEdge() != null) {
			createBurnPanels();
			for(int burn = 0; burn < event.getEdge().getBurns().size(); burn++) {
				BurnPanel panel = (BurnPanel)tabbedBurnPane.getComponentAt(burn);
				for(BurnStageItem i : event.getBurnStageSequence().get(burn)) {
					BurnStageItem bs = new BurnStageItem();
					bs.setBurnStage(i.getBurnStage());
					bs.setElement(getEventDialog().getSimElement(i.getElement().getUid()));
					panel.sequenceModel.addElement(bs);
				}
			}
		}
		elementTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if(!elementTree.isSelectionEmpty()) {
					((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceList.clearSelection();
				}
				updateButtons();
			}
		});
		btnAddBurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceModel.addElement(
						new BurnStageItem(BurnStageItem.BURN, 
								((CheckBoxNode)elementTree.getSelectionPath().getLastPathComponent()).getElement()));
				updateView();
			}
		});
		btnAddStage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceModel.addElement(
						new BurnStageItem(BurnStageItem.STAGE, 
								((CheckBoxNode)elementTree.getSelectionPath().getLastPathComponent()).getElement()));
				updateView();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public SpaceTransport getEvent() {
		return event;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#saveEvent()
	 */
	@Override
	public void saveEvent() {
		event.setEdge((SpaceEdge)ddlTrajectory.getSelectedItem());
		
		event.getElements().clear();
		for(I_Element element : elementModel.getTopCheckedElements()) {
			event.getElements().add(getEventDialog().getElement(element.getUid()));
		}
		
		event.getBurnStageSequence().clear();
		for(Component c : tabbedBurnPane.getComponents()) {
			ArrayList<BurnStageItem> sequence = new ArrayList<BurnStageItem>();
			for(Object o : ((BurnPanel)c).sequenceModel.toArray()) {
				BurnStageItem i = new BurnStageItem();
				i.setBurnStage(((BurnStageItem)o).getBurnStage());
				i.setElement(getEventDialog().getElement(((BurnStageItem)o).getElement().getUid()));
				sequence.add(i);
			}
			event.getBurnStageSequence().add(sequence);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#updateView()
	 */
	@Override
	public void updateView() {
		SpaceEdge trajectory = (SpaceEdge)ddlTrajectory.getSelectedItem();
		ItemListener trajectoryListener = ddlTrajectory.getItemListeners()[0];
		ddlTrajectory.removeItemListener(trajectoryListener);
		ddlTrajectory.removeAllItems();
		for(Edge e : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getNetwork().getEdges()) {
			if(e.getOrigin().equals(getEventDialog().getNode())
					&& e instanceof SpaceEdge) {
				ddlTrajectory.addItem(e);
				if(e.equals(trajectory)) {
					ddlTrajectory.setSelectedItem(e);
				}
			}
		}
		lblDestination.setText("Destination: " + 
				((SpaceEdge)ddlTrajectory.getSelectedItem()).getDestination());
		lblDuration.setText("Duration: " + 
				((SpaceEdge)ddlTrajectory.getSelectedItem()).getDuration() + " days");
		ddlTrajectory.addItemListener(trajectoryListener);
		if(tabbedBurnPane.getComponentCount() != ((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().size()) {
			createBurnPanels();
		}
		
		Set<I_Element> checkedElements = elementModel.getTopCheckedElements();
		int[] selectionRows = elementTree.getSelectionRows();
		elementModel = new CheckBoxTreeModel(getEventDialog().getSimNode());
		elementTree.setModel(elementModel);
		elementModel.setCheckedElements(checkedElements);
		elementTree.setSelectionRows(selectionRows);
		elementModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				updateButtons();
			}
			public void treeNodesInserted(TreeModelEvent e) { }
			public void treeNodesRemoved(TreeModelEvent e) { }
			public void treeStructureChanged(TreeModelEvent e) { }
		});
		
		for(int burn = 0; burn < ((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().size(); burn++) {
			BurnPanel burnPanel = (BurnPanel)tabbedBurnPane.getComponentAt(burn);
			for(Object o : burnPanel.sequenceModel.toArray()) {
				boolean isFound = false;
				for(I_Element element : elementModel.getTopCheckedElements()) {
					if(element.equals(((BurnStageItem)o).getElement())) isFound = true;
				}
				if(!isFound) burnPanel.sequenceModel.clear();
			}
		}
		updateDeltaV();
	}
	private class BurnPanel extends JPanel {
		private static final long serialVersionUID = -2491575968761743630L;
		public JProgressBar deltaV;
		public JLabel stackMassLabel;
		public DefaultListModel sequenceModel;
		public JList sequenceList;
		public JScrollPane sequenceScroll;
		public JButton btnClear;
		
		public BurnPanel() {
			super();
			this.setLayout(new GridBagLayout());
			setOpaque(false);
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(2,2,2,2);
			c.weightx = 0;
			c.weighty = 0;
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.LINE_END;
			add(new JLabel("Delta-V: "), c);
			c.gridy++;
			add(new JLabel("Stack Mass: "), c);
			c.gridy++;
			c.anchor = GridBagConstraints.FIRST_LINE_END;
			add(new JLabel("Sequence: "), c);
			
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			deltaV = new JProgressBar(0, 100);
			deltaV.setStringPainted(true);
			add(deltaV, c);
			c.gridy++;
			c.fill = GridBagConstraints.NONE;
			stackMassLabel = new JLabel();
			add(new UnitsWrapper(stackMassLabel, "kg"), c);
			
			c.gridy++;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			sequenceModel = new DefaultListModel();
			sequenceList = new JList(sequenceModel);
			sequenceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			sequenceList.setCellRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1271331296677711150L;
				public Component getListCellRendererComponent(JList list, Object value, 
						int index, boolean isSelected, boolean cellHasFocus) {
					JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if(((BurnStageItem)value).getBurnStage().equals(BurnStageItem.BURN)) {
						label.setIcon(BurnStageItem.BURN_ICON);
					} else {
						label.setIcon(BurnStageItem.STAGE_ICON);
					}
					
					return label;
				}
			});
			sequenceScroll = new JScrollPane(sequenceList);
			sequenceScroll.setPreferredSize(new Dimension(150,100));
			add(sequenceScroll, c);
			
			c.gridy++;
			c.weighty = 0;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_END;
			btnClear = new JButton("Clear");
			btnClear.setToolTipText("Clear the selected burns or stages");
			btnClear.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cross.png")));
			btnClear.setMargin(new Insets(3,3,3,3));
			btnClear.setEnabled(false);
			add(btnClear, c);
		}
	}
	private void createBurnPanels() {
		tabbedBurnPane.removeAll();
		SpaceEdge trajectory = ((SpaceEdge)ddlTrajectory.getSelectedItem());
		for(int burn = 0; burn < trajectory.getBurns().size(); burn++) {
			BurnPanel panel = new BurnPanel();
			panel.btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for(Object o : ((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceList.getSelectedValues())
						((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceModel.removeElement(o);
					updateView();
				}
			});
			panel.sequenceList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if(!((BurnPanel)tabbedBurnPane.getSelectedComponent()).sequenceList.isSelectionEmpty()) {
						elementTree.clearSelection();
					}
					updateButtons();
				}
			});
			tabbedBurnPane.add("Burn " + (burn+1), panel);
		}
	}
	private void updateDeltaV() {
		try {
			HashMap<I_Element, Double> massMap = new HashMap<I_Element, Double>();
			HashMap<PropulsiveVehicle, Double> omsFuelMap = new HashMap<PropulsiveVehicle, Double>();
			HashMap<PropulsiveVehicle, Double> rcsFuelMap = new HashMap<PropulsiveVehicle, Double>();
			for(Object o : elementModel.getTopCheckedElements()) {
				massMap.put((I_Element)o, ((I_Element)o).getTotalMass());
				if(o instanceof PropulsiveVehicle
						&& ((PropulsiveVehicle)o).getOmsIsp() > 0) {
					omsFuelMap.put((PropulsiveVehicle)o, ((PropulsiveVehicle)o).getOmsFuelTank().getAmount());
				}
				if(o instanceof PropulsiveVehicle
						&& ((PropulsiveVehicle)o).getRcsIsp() > 0) {
					rcsFuelMap.put((PropulsiveVehicle)o, ((PropulsiveVehicle)o).getRcsFuelTank().getAmount());
				}
			}
			
			for(int burn = 0; burn < ((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().size(); burn++) {
				BurnPanel burnPanel = (BurnPanel)tabbedBurnPane.getComponentAt(burn);
				Burn b = ((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().get(burn);

				double stackMass = 0;
				for(I_Element e : massMap.keySet()) {
					stackMass += massMap.get(e);
				}
				double deltaVReq = b.getDeltaV();
				
				//System.out.println("deltaVReq: " + deltaVReq);
				//System.out.println("stackMass: " + stackMass);
				
				for(Object o : burnPanel.sequenceModel.toArray()) {
					BurnStageItem i = (BurnStageItem)o;
					I_Element element = null;
					for(I_Element e : massMap.keySet()) {
						if(i.getElement().equals(e)) {
							element = e;
							break;
						}
					}
					if(i.getBurnStage().equals(BurnStageItem.STAGE)) {
						//System.out.println("stage: " + i.getElement().getName());
						stackMass -= massMap.get(element);
						massMap.put(element, 0d);
					} else if(i.getBurnStage().equals(BurnStageItem.BURN)) {
						//System.out.println("burn: " + i.getElement().getName());
						PropulsiveVehicle v = (PropulsiveVehicle)element;
						if(b.getBurnType().equals(BurnType.OMS)) {
							if(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp()) > omsFuelMap.get(v)) {
								double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getOmsIsp(), omsFuelMap.get(v));
								//System.out.println("dv achieved: " + deltaVAchieved);
								deltaVReq -= deltaVAchieved;
								stackMass -= omsFuelMap.get(v);
								massMap.put(v, massMap.get(v) - omsFuelMap.get(v));
								omsFuelMap.put(v, 0d);
							} else {
								//System.out.println("dv achieved: " + deltaVReq);
								double fuelUsed = Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp());
								deltaVReq = 0;
								stackMass -= fuelUsed;
								massMap.put(v, massMap.get(v) - fuelUsed);
								omsFuelMap.put(v, omsFuelMap.get(v) - fuelUsed);
							}
						} else {
							if(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp()) > rcsFuelMap.get(v)) {
								double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getRcsIsp(), rcsFuelMap.get(v));
								//System.out.println("dv achieved: " + deltaVAchieved);
								deltaVReq -= deltaVAchieved;
								stackMass -= rcsFuelMap.get(v);
								massMap.put(v, massMap.get(v) - rcsFuelMap.get(v));
								rcsFuelMap.put(v, 0d);
							} else {
								//System.out.println("dv achieved: " + deltaVReq);
								double fuelUsed = Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp());
								deltaVReq = 0;
								stackMass -= fuelUsed;
								massMap.put(v, massMap.get(v) - fuelUsed);
								rcsFuelMap.put(v, rcsFuelMap.get(v) - fuelUsed);
							}
						}
					}
				}
				if(deltaVReq == 0) {
					burnPanel.deltaV.setForeground(new Color(0, 153, 0));
					getEventDialog().setOkButtonEnabled(true);
				} else {
					burnPanel.deltaV.setForeground(new Color(153, 0, 0));
					//TODO annoying... getEventDialog().setOkButtonEnabled(false);
					getEventDialog().setOkButtonEnabled(true);
				}
				burnPanel.stackMassLabel.setText(deltaVFormat.format(stackMass));
				burnPanel.deltaV.setValue((int)(100*((b.getDeltaV() - deltaVReq)/b.getDeltaV())));
				burnPanel.deltaV.setString(deltaVFormat.format((b.getDeltaV() - deltaVReq)) + " / " + b.getDeltaV() + " m/s (" + b.getBurnType().getName() + ")");	
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"Sorry, an unhandled exception occurred.",
				    "Unhandled Exception in SpaceTransportGUI.updateDeltaV",
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	private void updateButtons() {
		if(elementTree.getSelectionCount() > 0) {
			if(elementTree.getSelectionCount() == 1
					&& ((CheckBoxNode)elementTree.getSelectionPath().getLastPathComponent()).isChecked()
					/* TODO: add check for previous stage */) { 
				btnAddStage.setEnabled(true);
				if(((CheckBoxNode)elementTree.getSelectionPath().getLastPathComponent()).getElement() instanceof PropulsiveVehicle) {
					PropulsiveVehicle v = (PropulsiveVehicle)((CheckBoxNode)elementTree.getSelectionPath().getLastPathComponent()).getElement();
					if((((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().get(tabbedBurnPane.getSelectedIndex()).getBurnType().equals(BurnType.OMS) && v.getOmsFuelTank() != null)
							||(((SpaceEdge)ddlTrajectory.getSelectedItem()).getBurns().get(tabbedBurnPane.getSelectedIndex()).getBurnType().equals(BurnType.RCS) && v.getRcsFuelTank() != null)) {
						btnAddBurn.setEnabled(true);
					} else {
						btnAddBurn.setEnabled(false);
					}
				} else {
					btnAddBurn.setEnabled(false);
				}
			} else {
				btnAddStage.setEnabled(false);
				btnAddBurn.setEnabled(false);
			}
		} else {
			btnAddStage.setEnabled(false);
			btnAddBurn.setEnabled(false);
		}
		BurnPanel panel = (BurnPanel)tabbedBurnPane.getSelectedComponent();
		if(panel.sequenceList.getSelectedIndices().length > 0) {
			panel.btnClear.setEnabled(true);
		} else {
			panel.btnClear.setEnabled(false);
		}
	}
}
