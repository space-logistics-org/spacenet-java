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
package edu.mit.spacenet.gui.resource;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.Item;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.domain.resource.ResourceType;
import edu.mit.spacenet.gui.data.DataSourceDialog;
import edu.mit.spacenet.gui.renderer.ResourceTypeListCellRenderer;

/**
 * A dialog box for editing resources.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class ResourceEditorDialog extends JDialog {
	private static final long serialVersionUID = -8753859556068289559L;
	private I_Resource resource;
	private ResourceEditorPanel resourcePanel;
	private DataSourceDialog dialog;
	
	private JComboBox resourceTypeCombo;
	private JButton okButton;
	private JButton cancelButton;
	
	/**
	 * Instantiates a new resource editor dialog.
	 * 
	 * @param editorFrame the editor frame
	 * @param resource the resource
	 */
	private ResourceEditorDialog(DataSourceDialog editorFrame, I_Resource resource) {
		super(editorFrame, "Edit Resource", true);
		this.resource = resource;
		this.dialog = editorFrame;
		buildDialog();
		initialize();
	}
	
	/**
	 * Builds the dialog.
	 */
	private void buildDialog(){
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Type: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		resourceTypeCombo = new JComboBox();
		resourceTypeCombo.setRenderer(new ResourceTypeListCellRenderer());
		for(ResourceType t : ResourceType.values()) 
			if(t!=ResourceType.GENERIC) resourceTypeCombo.addItem(t);
		resourceTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED 
						&& (resource.getResourceType()!=e.getItem())) {
					switch((ResourceType)e.getItem()) {
					case RESOURCE:
						resource = new Resource();
						reset();
						break;
					case ITEM:
						resource = new Item();
						reset();
						break;
					}
				}
			}
		});
		
		resourceTypeCombo.setRenderer(new ResourceTypeListCellRenderer());
		
		resourceTypeCombo.setEnabled(resource.getTid()<0);
		
		contentPanel.add(resourceTypeCombo, c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		resourcePanel = ResourceEditorPanelFactory.createResourceEditorPanel(this, resource);
		contentPanel.add(resourcePanel, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		okButton = new JButton(resource.getTid()<0?"Add":"Save", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveResource();
			}
		});
		buttonPanel.add(okButton);
		
		cancelButton = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("icons/database.png")));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, c);
		
		setContentPane(contentPanel);
	}
	
	/**
	 * Initializes the components.
	 */
	private void initialize() {
		resourceTypeCombo.setSelectedItem(resource.getResourceType());
	}
	
	/**
	 * Gets the resource.
	 * 
	 * @return the resource
	 */
	public I_Resource getResource(){
		return this.resource;
	}
	
	/**
	 * Resets the component for when the type of resource is changed.
	 */
	private void reset() {
		getContentPane().remove(resourcePanel);
		resourcePanel = ResourceEditorPanelFactory.createResourceEditorPanel(this, resource);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		getContentPane().add(resourcePanel, c);
		validate();
		repaint();
	}
	
	/**
	 * Saves the resource.
	 */
	public void saveResource() {
		if(resourcePanel.isResourceValid()) {
			try {
				resourcePanel.saveResource();
				dialog.getDataSource().saveResource(getResource());
				dialog.loadDataSource();
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(dialog, 
						"SpaceNet Errror",
						"An error occurred while saving the resource.", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Creates and shows the GUI.
	 * 
	 * @param dialog the editor frame
	 * @param resource the resource
	 */
	public static void createAndShowGUI(DataSourceDialog dialog, I_Resource resource) {
		ResourceEditorDialog d = new ResourceEditorDialog(dialog, resource);
		d.setMinimumSize(new Dimension(300,150));
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
}
