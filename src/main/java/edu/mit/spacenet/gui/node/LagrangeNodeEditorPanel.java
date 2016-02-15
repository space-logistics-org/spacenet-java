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
package edu.mit.spacenet.gui.node;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.mit.spacenet.domain.network.node.Body;
import edu.mit.spacenet.domain.network.node.LagrangeNode;

/**
 * A panel to edit Lagrange nodes.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class LagrangeNodeEditorPanel extends AbstractNodeEditorPanel {
	private static final long serialVersionUID = -7968181121519566546L;
	
	private LagrangeNode node;
	private JTextField nameText;
	private JComboBox majorBodyCombo, minorBodyCombo, numberCombo;
	private JTextArea descriptionText;
	
	/**
	 * Instantiates a new lagrange node editor panel.
	 * 
	 * @param node the node
	 */
	public LagrangeNodeEditorPanel(NodeEditorDialog dialog, LagrangeNode node) {
		super(dialog);
		this.node = node;
		buildPanel();
		initialize();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel(){
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Name: "), c);
		
		c.gridx++;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		nameText= new JTextField();
		add(nameText, c);
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Major Body: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		majorBodyCombo= new JComboBox();
		for(Body t : Body.values()) majorBodyCombo.addItem(t);
		majorBodyCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1061418828847174455L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value!=null) setIcon(((Body)value).getIcon());
				return this;
			}
		});
		add(majorBodyCombo, c);
		majorBodyCombo.setToolTipText("The more massive body in the orbital equilibrium");
		
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Minor Body: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		minorBodyCombo= new JComboBox();
		for(Body t : Body.values()) minorBodyCombo.addItem(t);
		minorBodyCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1061418828847174455L;
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value!=null) setIcon(((Body)value).getIcon());
				return this;
			}
		});
		add(minorBodyCombo, c);
		minorBodyCombo.setToolTipText("The less massive body in the orbital equilibrium");
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("L.P. Number: "), c);
		
		c.gridx++;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		numberCombo= new JComboBox(new Integer[]{1,2,3,4,5});
		add(numberCombo, c);
		numberCombo.setToolTipText("1: between bodies; 2: outside minor body; 3: outside major body; 4: leading triangular; 5: lagging triangular");
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Description: "), c);
		
		c.gridx++;
		c.weighty = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(5,10);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionText.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
		add(new JScrollPane(descriptionText), c);
	}
	
	/**
	 * Initializes the components.
	 */
	private void initialize() {
		if(node!= null){
			nameText.setText(node.getName());
			majorBodyCombo.setSelectedItem(node.getBody());
			minorBodyCombo.setSelectedItem(node.getMinorBody());
			numberCombo.setSelectedItem(node.getNumber());
			descriptionText.setText(node.getDescription());
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.editor.gui.node.AbstractNodePanelEditor#getNode()
	 */
	public LagrangeNode getNode() {
		return node;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.editor.gui.node.AbstractNodePanelEditor#isNodeValid()
	 */
	public boolean isNodeValid() {
		if(nameText.getText().length()==0){
			JOptionPane.showMessageDialog(getDialog(), 
					"Please enter a node name.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if(majorBodyCombo.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(getDialog(), 
					"Please select a major celestial body.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if(minorBodyCombo.getSelectedItem()==null) {
			JOptionPane.showMessageDialog(getDialog(), 
					"Please select a minor celestial body.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.editor.gui.node.AbstractNodePanelEditor#saveNode()
	 */
	public void saveNode(){
		node.setName(nameText.getText());
		node.setBody((Body)majorBodyCombo.getSelectedItem());
		node.setMinorBody((Body)minorBodyCombo.getSelectedItem());
		node.setNumber((Integer)numberCombo.getSelectedItem());
		node.setDescription(descriptionText.getText());
	}
}
