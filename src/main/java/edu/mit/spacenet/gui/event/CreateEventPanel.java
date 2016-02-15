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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.I_Container;
import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.element.I_Carrier;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.CapacityPanel;
import edu.mit.spacenet.gui.component.ContainerComboBox;
import edu.mit.spacenet.gui.component.ElementTree;
import edu.mit.spacenet.gui.component.SearchTextField;
import edu.mit.spacenet.gui.element.ElementDialog;
import edu.mit.spacenet.gui.renderer.ElementPreviewListCellRenderer;
import edu.mit.spacenet.simulator.event.CreateEvent;

/**
 * A panel for viewing and editing a create event.
 * 
 * @author Paul Grogan
 */
public class CreateEventPanel extends AbstractEventPanel {
	private static final long serialVersionUID = 769918023169742283L;
	
	private CreateEvent event;
	
	private JComboBox containerCombo, elementTypeCombo;
	private CapacityPanel capacityPanel;
	private JList elementLibraryList;
	private ElementTree elementTree;
	private SearchTextField searchText;
	private JButton addElementButton, editElementButton, removeElementButton;
	
	private DefaultListModel elementLibraryModel;
	
	/**
	 * Instantiates a new creates the event panel.
	 * 
	 * @param eventDialog the event dialog
	 * @param event the event
	 */
	public CreateEventPanel(EventDialog eventDialog, CreateEvent event) {
		super(eventDialog, event);
		this.event = event;
		buildPanel();
		initialize();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		add(buildLeftPanel(), c);
		c.gridx++;
		add(buildRightPanel(), c);
	}
	
	/**
	 * Builds the left panel.
	 *
	 * @return the left panel
	 */
	private JPanel buildLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		leftPanel.add(new JLabel("Create in: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		containerCombo = new ContainerComboBox();
		leftPanel.add(containerCombo, c);
		c.gridy++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		capacityPanel = new CapacityPanel();
		leftPanel.add(capacityPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		leftPanel.add(new JLabel("Elements: "), c);
		c.gridy++;
		c.gridwidth = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		elementTree = new ElementTree();
		elementTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				editElementButton.setEnabled(elementTree.getSelectionCount() == 1);
				removeElementButton.setEnabled(elementTree.getSelectionCount() >= 1);
			}
		});
		elementTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && elementTree.getSelectionCount()==1) {
					ElementDialog.createAndShowGUI(getEventDialog(), elementTree.getElementSelection());
				}
			}
		});
		elementTree.setRootVisible(false);
		elementTree.setShowsRootHandles(true);
		JScrollPane elementScroll = new JScrollPane(elementTree);
		elementScroll.setPreferredSize(new Dimension(100,200));
		leftPanel.add(elementScroll, c);
		c.weighty = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		editElementButton = new JButton("Edit", new ImageIcon(getClass().getClassLoader().getResource("icons/brick_edit.png")));
		editElementButton.setToolTipText("Edit Element");
		editElementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ElementDialog.createAndShowGUI(getEventDialog(), elementTree.getElementSelection());
			}
		});
		editElementButton.setEnabled(false);
		buttonPanel.add(editElementButton);
		removeElementButton = new JButton("Remove", new ImageIcon(getClass().getClassLoader().getResource("icons/brick_delete.png")));
		removeElementButton.setToolTipText("Remove Element");
		removeElementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashSet<I_Element> elements = new HashSet<I_Element>();
				for(TreePath p : elementTree.getSelectionPaths()) {
					elements.add((I_Element)((DefaultMutableTreeNode)p.getLastPathComponent()).getUserObject());
				}
		    	for(I_Element element : elements) {
		    		elementTree.getModel().removeElement(element);
		    	}
		    	updateView();
			}
		});
		removeElementButton.setEnabled(false);
		buttonPanel.add(removeElementButton);
		leftPanel.add(buttonPanel, c);
		return leftPanel;
	}
	
	/**
	 * Builds the right panel.
	 *
	 * @return the right panel
	 */
	private JPanel buildRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(BorderFactory.createTitledBorder("Element Library"));
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.LINE_AXIS));
		searchText = new SearchTextField("Enter Search Term");
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateElementLibrary();
			}
		});
		filterPanel.add(searchText);
		elementTypeCombo = new JComboBox();
		elementTypeCombo.addItem("All");
		for(ElementType t : ElementType.values()) {
			elementTypeCombo.addItem(t);
		}
		elementTypeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateElementLibrary();
			}
		});
		filterPanel.add(elementTypeCombo);
		rightPanel.add(filterPanel, c);
		c.gridy++;
		c.weighty = 1;
		elementLibraryModel = new DefaultListModel();
		elementLibraryList = new JList(elementLibraryModel);
		elementLibraryList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				addElementButton.setEnabled(elementLibraryList.getSelectedIndex()>=0);
			}
		});
		elementLibraryList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && elementLibraryList.getSelectedIndex()>=0) {
					try {
						addElement(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().loadElement(
								((ElementPreview)elementLibraryList.getSelectedValue()).ID));
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, 
								"An error occurred while accessing the data source to load the element", 
								"Error Loading Element", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		elementLibraryList.setCellRenderer(new ElementPreviewListCellRenderer());
		JScrollPane elementLibraryScroll = new JScrollPane(elementLibraryList);
		rightPanel.add(elementLibraryScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		addElementButton = new JButton("Add", new ImageIcon(getClass().getClassLoader().getResource("icons/brick_add.png")));
		addElementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Object o : elementLibraryList.getSelectedValues()) {
					try {
						addElement(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().loadElement(((ElementPreview)o).ID));
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, 
								"An error occurred while accessing the data source to load the element", 
								"Error Loading Element", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		addElementButton.setEnabled(false);
		rightPanel.add(addElementButton, c);
		return rightPanel;
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		containerCombo.addItem(event.getContainer());
		containerCombo.setSelectedItem(event.getContainer());
		
		containerCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCapacities();
			}
		});
		for(I_Element element : event.getElements()) {
			elementTree.getModel().addElement(element);
		}
		updateElementLibrary();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.event.AbstractEventPanel#getEvent()
	 */
	@Override
	public CreateEvent getEvent() {
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
		for(I_Element element : elementTree.getModel().getElements()) {
			event.getElements().add(element);
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
		updateCapacities();
		elementTree.getModel().hackedUpdate();
	}
	
	/**
	 * Gets the letter for copying elements.
	 *
	 * @param copyNumber the copy number
	 * @return the letter
	 */
	private String getLetter(int copyNumber) {
		switch(copyNumber) {
		case 1:return "A"; case 2:return "B"; case 3:return "C";
		case 4:return "D"; case 5:return "E"; case 6:return "F";
		case 7:return "G"; case 8:return "H"; case 9:return "I";
		case 10:return "J"; case 11:return "K"; case 12:return "L";
		case 13:return "M"; case 14:return "N"; case 15:return "O";
		case 16:return "P"; case 17:return "Q"; case 18:return "R";
		case 19:return "S"; case 20:return "T"; case 21:return "U";
		case 22:return "V"; case 23:return "W"; case 24:return "X";
		case 25:return "Y"; case 26:return "Z"; default:return "?";
		}
	}
	
	/**
	 * Adds the element.
	 *
	 * @param element the element
	 */
	private void addElement(I_Element element) {
		element.setName(getEventDialog().getMissionPanel().getMissionSplitPane().getMission().getName().substring(0,Math.min(getEventDialog().getMissionPanel().getMissionSplitPane().getMission().getName().length(), 5)) + " | " + element.getName());
		int numElements = 0;
		for(I_Element existing : elementTree.getModel().getElements()) {
			if(element.getTid()==existing.getTid()) {
				numElements++;
			}
		}
		if(numElements == 1) {
			for(I_Element existing : elementTree.getModel().getElements()) {
				if(element.getTid()==existing.getTid() 
						&& !existing.getName().subSequence(existing.getName().length()-2, existing.getName().length()).equals(" A")) {
					existing.setName(existing.getName() + " " + getLetter(numElements));
				}
			}
		}
		if(numElements > 0) element.setName(element.getName() + " " + getLetter(++numElements));
		elementTree.getModel().addElement(element);
		updateCapacities();
	}
	
	/**
	 * Update element library.
	 */
	private void updateElementLibrary() {
		elementLibraryModel.removeAllElements();
		for(ElementPreview prev : SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getElementPreviewLibrary()) {
			if((searchText.getText().equals(searchText.getDefaultText())
					|| prev.NAME.toLowerCase().contains(searchText.getText().toLowerCase()))
					&& (elementTypeCombo.getSelectedItem().equals("All")
					|| prev.TYPE == ((ElementType)elementTypeCombo.getSelectedItem()))) {
				elementLibraryModel.addElement(prev);
			}
		}
	}
	
	/**
	 * Update capacities.
	 */
	private void updateCapacities() {
		double mass = 0;
		double volume = 0;
		int crew = 0;
		for(I_Element e : elementTree.getModel().getElements()) {
			if(e instanceof CrewMember)
				crew++;
			else {
				mass += e.getTotalMass();
				volume += e.getTotalVolume();
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
