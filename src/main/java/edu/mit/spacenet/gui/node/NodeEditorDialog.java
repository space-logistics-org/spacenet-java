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
package edu.mit.spacenet.gui.node;

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

import edu.mit.spacenet.domain.network.node.LagrangeNode;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.domain.network.node.OrbitalNode;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.gui.data.DataSourceDialog;
import edu.mit.spacenet.gui.renderer.NodeTypeListCellRenderer;

/**
 * A dialog box used to edit nodes.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class NodeEditorDialog extends JDialog {
	private static final long serialVersionUID = 8072239734759253813L;
	
	private Node node;
	private JComboBox nodeTypeCombo;
	private AbstractNodeEditorPanel nodePanel;
	private DataSourceDialog dialog;
	private JButton okButton;
	private JButton cancelButton;
	
	/**
	 * Instantiates a new node editor dialog.
	 * 
	 * @param editorFrame the editor frame
	 * @param node the node1
	 */
	private NodeEditorDialog(DataSourceDialog dialog, Node node) {
		super(dialog, "Edit Node", true);
		this.dialog = dialog;
		this.node = node;
		buildDialog();
	}
	

	/**
	 * Builds the dialog.
	 */
	public void buildDialog(){
		
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
		nodeTypeCombo = new JComboBox();
		for(NodeType t : NodeType.values()) nodeTypeCombo.addItem(t);
		if(node!=null){
			nodeTypeCombo.setSelectedItem(node.getNodeType());
		}
		nodeTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& (node==null || node.getNodeType()!=e.getItem())) {
					switch((NodeType)e.getItem()) {
					case SURFACE:
						node = new SurfaceNode();
						reset();
						break;
					case ORBITAL:
						node = new OrbitalNode();
						reset();
						break;
					case LAGRANGE:
						node = new LagrangeNode();
						reset();
						break;
					}
				}
			}
		});
		nodeTypeCombo.setRenderer(new NodeTypeListCellRenderer());
		if(node.getTid()>0){
			nodeTypeCombo.setEnabled(false);
		}
		contentPanel.add(nodeTypeCombo, c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		nodePanel = NodeEditorPanelFactory.createNodePanel(this, node);
		contentPanel.add(nodePanel, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		okButton = new JButton(node.getTid()<0?"Add":"Save", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveNode();
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
	 * Gets the node.
	 * 
	 * @return the node
	 */
	public Node getNode(){
		return this.node;
	}
	
	/**
	 * Resets the components for changing to a different type of node.
	 */
	private void reset() {
		getContentPane().remove(nodePanel);
		nodePanel = NodeEditorPanelFactory.createNodePanel(this, node);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		getContentPane().add(nodePanel, c);
		validate();
		pack();
		repaint();
	}
	
	/**
	 * Saves the node.
	 */
	public void saveNode() {
		if(nodePanel.isNodeValid()) {
			nodePanel.saveNode();
			try {
				dialog.getDataSource().saveNode(getNode());
				dialog.loadDataSource();
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(dialog, 
						"SpaceNet Errror",
						"An error occurred while saving the node.", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Gets the dialog.
	 * 
	 * @return the dialog
	 */
	public DataSourceDialog getDialog() {
		return dialog;
	}
	
	/**
	 * Creates and shows the GUI.
	 * 
	 * @param DataSourcePanel the data source panel
	 * @param node1 the node1
	 */
	public static void createAndShowGUI (DataSourceDialog dialog, Node node1){
		NodeEditorDialog d = new NodeEditorDialog(dialog, node1);
		d.setMinimumSize(new Dimension(300,150));
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
}
