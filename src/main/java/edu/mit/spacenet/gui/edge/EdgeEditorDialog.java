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
package edu.mit.spacenet.gui.edge;

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

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.gui.data.DataSourceDialog;
import edu.mit.spacenet.gui.renderer.EdgeTypeListCellRenderer;

/**
 * A dialog box used to edit edges.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class EdgeEditorDialog extends JDialog {
	private static final long serialVersionUID = -2553839031446779844L;
	
	private Edge edge;
	private AbstractEdgeEditorPanel edgePanel;
	private DataSourceDialog dialog;
	
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<EdgeType> edgeTypeCombo;
	
	/**
	 * Instantiates a new edge editor dialog editor.
	 * 
	 * @param editorFrame the editor frame
	 * @param edge the edge
	 */
	private EdgeEditorDialog(DataSourceDialog dialog, Edge edge) {
		super(dialog, "Edit Edge", true);
		this.dialog = dialog;
		this.edge = edge;
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
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		edgeTypeCombo = new JComboBox<EdgeType>();
		for(EdgeType t : EdgeType.values()) {
			if (t != EdgeType.TIME_DEPENDENT) {
				edgeTypeCombo.addItem(t);
			}
		}
		edgeTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED && (edge.getEdgeType()!=e.getItem())) {
					switch((EdgeType)e.getItem()) {
					case FLIGHT:
						edge = new FlightEdge();
						reset();
						break;
					case SPACE:
						edge = new SpaceEdge();
						reset();
						break;
					case SURFACE:
						edge = new SurfaceEdge();
						reset();
						break;
					case TIME_DEPENDENT:
						break;
					}
				}
			}
		});
		edgeTypeCombo.setRenderer(new EdgeTypeListCellRenderer());
		edgeTypeCombo.setEnabled(edge.getTid()<0);
		contentPanel.add(edgeTypeCombo, c);
		
		/*
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		JButton addNodeButton = new JButton("Add Nodes", new ImageIcon(getClass().getClassLoader().getResource("icons/asterisk_add.png")));
		addNodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NodeEditorDialog.createAndShowGUI(dialog, new OrbitalNode());
			}
		});
		contentPanel.add(addNodeButton,c);
		*/
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		edgePanel = EdgeEditorPanelFactory.createEdgePanel(this, edge);
		contentPanel.add(edgePanel, c);
		
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.NONE;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		okButton = new JButton(edge.getTid()<0?"Add":"Save", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveEdge();
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
		if(edge!=null){
			edgeTypeCombo.setSelectedItem(edge.getEdgeType());
		}
	}
	
	/**
	 * Resets the components after the edge type is changed.
	 */
	private void reset() {
		getContentPane().remove(edgePanel);
		edgePanel = EdgeEditorPanelFactory.createEdgePanel(this, edge);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		getContentPane().add(edgePanel, c);
		validate();
		pack();
		repaint();
	}
	
	/**
	 * Gets the edge panel.
	 * 
	 * @return the edge panel
	 */
	public AbstractEdgeEditorPanel getEdgePanel(){
		return this.edgePanel;
	}
	
	/**
	 * Gets the edge.
	 * 
	 * @return the edge
	 */
	public Edge getEdge(){
		return this.edge;
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
	 * Save the edge.
	 */
	public void saveEdge(){
		if(edgePanel.isEdgeValid()) {
			edgePanel.saveEdge();
			try {
				dialog.getDataSource().saveEdge(getEdge());
				dialog.loadDataSource();
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(dialog, 
						"SpaceNet Errror",
						"An error occurred while saving the edge.", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Creates and shows the GUI.
	 * 
	 * @param editorFrame the editor frame
	 * @param edge the edge
	 */
	public static void createAndShowGUI(DataSourceDialog dialog, Edge edge) {
		EdgeEditorDialog d = new EdgeEditorDialog(dialog, edge);
		d.setMinimumSize(new Dimension(300,150));
		d.pack();
		d.setLocationRelativeTo(d.getParent());
		d.setVisible(true);
	}
}
