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
package edu.mit.spacenet.gui.data;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.spacenet.data.DataSourceType;
import edu.mit.spacenet.data.Database;
import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.data.InMemoryDataSource;
import edu.mit.spacenet.data.Spreadsheet_2_5;
import edu.mit.spacenet.domain.element.Element;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.domain.network.node.SurfaceNode;
import edu.mit.spacenet.domain.resource.Resource;
import edu.mit.spacenet.gui.ScenarioPanel;
import edu.mit.spacenet.gui.command.LoadDataSourceCommand;
import edu.mit.spacenet.gui.edge.EdgeEditorDialog;
import edu.mit.spacenet.gui.element.ElementDialog;
import edu.mit.spacenet.gui.node.NodeEditorDialog;
import edu.mit.spacenet.gui.resource.ResourceEditorDialog;

/**
 * Dialog for editing loading data sources.
 * 
 * @author Paul Grogan
 */
public class DataSourceDialog extends JDialog {
	private static final long serialVersionUID = -3048824253945313736L;
	
	private ScenarioPanel scenarioPanel;
	private I_DataSource dataSource;
	
	private JComboBox<DataSourceType> dataSourceTypeCombo;
	private AbstractDataSourcePanel dataSourcePanel;
	
	private JCheckBox nodesCheck, edgesCheck, resourcesCheck, elementsCheck;
	private JButton loadButton;
	private JTabbedPane libraryTabs;
	private DataSourceObjectTableModel nodeModel, edgeModel, resourceModel, elementModel;
	private DataSourceObjectTable nodeTable, edgeTable, resourceTable, elementTable;
	private JButton addButton, editButton, deleteButton;
	
	/**
	 * Instantiates a new data source dialog.
	 *
	 * @param scenarioPanel the scenario panel
	 */
	private DataSourceDialog(ScenarioPanel scenarioPanel) {
		super(scenarioPanel.getSpaceNetFrame(), "Edit Data Source", true);
		this.scenarioPanel = scenarioPanel;
		if(scenarioPanel.getScenario()!=null) {
			dataSource = scenarioPanel.getScenario().getDataSource();
		}
		buildDialog();
	}
	
	private void buildDialog() {
		final JPanel contentPanel = new JPanel();
		contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		contentPanel.add(new JLabel("Type: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		dataSourceTypeCombo = new JComboBox<DataSourceType>();
		for(DataSourceType t : DataSourceType.values()) {
			dataSourceTypeCombo.addItem(t);
		}
		dataSourceTypeCombo.setSelectedItem(dataSource==null?DataSourceType.NONE:dataSource.getDataSourceType());
		dataSourceTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2255885956722142642L;
			public Component getListCellRendererComponent(JList<?> list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setIcon(((DataSourceType)value).getIcon());
				return this;
			}
		});
		dataSourceTypeCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED
						&& (dataSource==null&&e.getItem()!=DataSourceType.NONE
								|| dataSource.getDataSourceType()!=e.getItem())) {
					int answer = JOptionPane.YES_OPTION;
					if(getScenarioPanel().getScenario().getDataSource()!=null) {
						answer = JOptionPane.showConfirmDialog(getThis(), 
								"Changing the data source will reset all data. Continue?",
								"Confirm Data Source Change", JOptionPane.YES_NO_OPTION);
					}
					if(answer==JOptionPane.YES_OPTION) {
						switch((DataSourceType)dataSourceTypeCombo.getSelectedItem()) {
						case EXCEL_2_5:
							getScenarioPanel().getScenario().setDataSource(new Spreadsheet_2_5());
							break;
						case SQL_DB:
							getScenarioPanel().getScenario().setDataSource(new Database());
							break;
						case IN_MEMORY:
							getScenarioPanel().getScenario().setDataSource(new InMemoryDataSource());
							break;
						case NONE:
							getScenarioPanel().getScenario().setDataSource(null);
							break;
						}
						
						dispose();
						getScenarioPanel().updateView();
						DataSourceDialog.createAndShowGUI(getScenarioPanel());
					} else {
						updateTables();
					}
				}
			}
		});
		contentPanel.add(dataSourceTypeCombo, c);
		
		c.gridy++;
		c.weightx = 0;
		c.gridx = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		dataSourcePanel = DataSourcePanelFactory.createDataSourcePanel(this, dataSource);
		contentPanel.add(dataSourcePanel, c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		contentPanel.add(new JLabel("Update: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel checkPanel = new JPanel(new GridLayout(2,2));
		nodesCheck = new JCheckBox("Nodes", true);
		checkPanel.add(nodesCheck);
		edgesCheck = new JCheckBox("Edges", true);
		checkPanel.add(edgesCheck);
		resourcesCheck = new JCheckBox("Resources", true);
		checkPanel.add(resourcesCheck);
		elementsCheck = new JCheckBox("Instantiated Elements", false);
		checkPanel.add(elementsCheck);
		contentPanel.add(checkPanel, c);
		
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		loadButton = new JButton("Load", new ImageIcon(getClass().getClassLoader().getResource("icons/database_refresh.png")));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataSource();
			}
		});
		contentPanel.add(loadButton, c);
		
		c.gridy++;
		c.gridx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		contentPanel.add(new JLabel("Data: "), c);
		c.gridx++;
		c.weighty = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		libraryTabs = new JTabbedPane();
		contentPanel.add(libraryTabs, c);
		
		nodeModel = new DataSourceObjectTableModel(DataSourceObjectTableModel.NODE, dataSource);
		nodeTable = new DataSourceObjectTable(nodeModel);
		nodeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		nodeTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2&&nodeTable.getSelectedRowCount()==1) {
					NodeEditorDialog.createAndShowGUI(getThis(), 
							dataSource.getNodeLibrary().get(nodeTable.getSelectedRow()));
				}
			}
		});
		JScrollPane nodeScroll = new JScrollPane(nodeTable);
		nodeScroll.setPreferredSize(new Dimension(400,200));
		libraryTabs.addTab("Nodes", new ImageIcon(getClass().getClassLoader().getResource("icons/bullet_black.png")), nodeScroll);
		
		edgeModel = new DataSourceObjectTableModel(DataSourceObjectTableModel.EDGE, dataSource);
		edgeTable = new DataSourceObjectTable(edgeModel);
		edgeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		edgeTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2&&edgeTable.getSelectedRowCount()==1) {
					EdgeEditorDialog.createAndShowGUI(getThis(), 
							dataSource.getEdgeLibrary().get(edgeTable.getSelectedRow()));
				}
			}
		});
		JScrollPane edgeScroll = new JScrollPane(edgeTable);
		edgeScroll.setPreferredSize(new Dimension(400,200));
		libraryTabs.addTab("Edges", new ImageIcon(getClass().getClassLoader().getResource("icons/edge_red.png")), edgeScroll);
		
		resourceModel = new DataSourceObjectTableModel(DataSourceObjectTableModel.RESOURCE, dataSource);
		resourceTable = new DataSourceObjectTable(resourceModel);
		resourceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		resourceTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2&&resourceTable.getSelectedRowCount()==1) {
					ResourceEditorDialog.createAndShowGUI(getThis(), 
							dataSource.getResourceLibrary().get(resourceTable.getSelectedRow()));
				}
			}
		});
		JScrollPane resourceScroll = new JScrollPane(resourceTable);
		resourceScroll.setPreferredSize(new Dimension(400,200));
		libraryTabs.addTab("Resources", new ImageIcon(getClass().getClassLoader().getResource("icons/bullet_blue.png")), resourceScroll);
		
		elementModel = new DataSourceObjectTableModel(DataSourceObjectTableModel.ELEMENT, dataSource);
		elementTable = new DataSourceObjectTable(elementModel);
		elementTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		elementTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2&&elementTable.getSelectedRowCount()==1) {
					ElementPreview elementpreview = dataSource.getElementPreviewLibrary().get(elementTable.getSelectedRow());
					int tid = elementpreview.ID;
					try {
						ElementDialog.createAndShowGUI(getThis(), (I_Element)dataSource.loadElement(tid));
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(getThis(), 
								"An error occurred while loading the element from the data source.", 
								"Element Load Error", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JScrollPane elementScroll = new JScrollPane(elementTable);
		elementScroll.setPreferredSize(new Dimension(400,200));
		libraryTabs.addTab("Elements", new ImageIcon(getClass().getClassLoader().getResource("icons/lunar_lander.png")), elementScroll);
		libraryTabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				nodeTable.clearSelection();
				edgeTable.clearSelection();
				resourceTable.clearSelection();
				elementTable.clearSelection();
				repaint();
			}
		});
		
		c.gridy++;
		c.gridwidth = 2;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		addButton = new JButton("Add New", new ImageIcon(getClass().getClassLoader().getResource("icons/add.png")));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(libraryTabs.getSelectedIndex()) {
				case 0: 
					NodeEditorDialog.createAndShowGUI(getThis(), new SurfaceNode());
					break;
				case 1: 
					EdgeEditorDialog.createAndShowGUI(getThis(), new SurfaceEdge());
					break;
				case 2:
					ResourceEditorDialog.createAndShowGUI(getThis(), new Resource());
					break;
				case 3:
					ElementDialog.createAndShowGUI(getThis(), new Element());
					//TODO turn sizing model back on SizingModelDialogEditor.createAndShowGUI(getThis(), ElementType.ELEMENT);
					break;
				}
			}
		});
		editButtonPanel.add(addButton);
		editButton = new JButton("Edit", new ImageIcon(getClass().getClassLoader().getResource("icons/page_white_edit.png")));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(libraryTabs.getSelectedIndex()) {
				case 0: 
					NodeEditorDialog.createAndShowGUI(getThis(), 
						dataSource.getNodeLibrary().get(nodeTable.getSelectedRow()));
					break;
				case 1: 
					EdgeEditorDialog.createAndShowGUI(getThis(), 
							dataSource.getEdgeLibrary().get(edgeTable.getSelectedRow()));
						break;
				case 2:
					ResourceEditorDialog.createAndShowGUI(getThis(), 
							dataSource.getResourceLibrary().get(resourceTable.getSelectedRow()));
						break;
				case 3:
					ElementPreview elementpreview = dataSource.getElementPreviewLibrary().get(elementTable.getSelectedRow());
					int tid = elementpreview.ID;
					try {
						ElementDialog.createAndShowGUI(getThis(), (I_Element)dataSource.loadElement(tid));
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(getThis(), 
								"An error occurred while loading the element from the data source.", 
								"Element Load Error", 
								JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				
			}
		});
		editButton.setEnabled(false);
		editButtonPanel.add(editButton);
		deleteButton = new JButton("Delete", new ImageIcon(getClass().getClassLoader().getResource("icons/delete.png")));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(getThis(), 
						"Are you sure you want to permanently delete objects?", 
						"SpaceNet Warning", 
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE);
				if(response == JOptionPane.YES_OPTION) {
					ArrayList<String> errorMessages= new ArrayList<String>(0);
					switch(libraryTabs.getSelectedIndex()) {
					case 0:
						for(int i : reverse(nodeTable.getSelectedRows())) {
							try {
								boolean isSuccessful = dataSource.deleteNode(dataSource.getNodeLibrary().get(i).getTid());
								if(!isSuccessful) {
									errorMessages.add("Node "+ dataSource.getNodeLibrary().get(i).getTid());
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						loadDataSource();
						break;
					case 1:
						for(int i : reverse(edgeTable.getSelectedRows())) {
							try {
								boolean isSuccessful = dataSource.deleteEdge(dataSource.getEdgeLibrary().get(i).getTid());
								if(!isSuccessful) {
									errorMessages.add("Edge "+ dataSource.getEdgeLibrary().get(i).getTid());
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						loadDataSource();
						break;
					case 2:
						for (int i : reverse(resourceTable.getSelectedRows())) {
							try {
								boolean isSuccessful = dataSource.deleteResource(dataSource.getResourceLibrary().get(i).getTid());
								if(!isSuccessful) {
									errorMessages.add("Resource "+ dataSource.getResourceLibrary().get(i).getTid());
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						loadDataSource();
						break;
					case 3:
						for(int i : reverse(elementTable.getSelectedRows())){
							try {
								boolean isSuccessful = dataSource.deleteElement(dataSource.getElementPreviewLibrary().get(i).ID);
								if(!isSuccessful) {
									errorMessages.add("Element "+dataSource.getElementPreviewLibrary().get(i).ID);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						loadDataSource();
						break;
					}
					if(errorMessages.size() > 0) {
						String tempError = "The following objects could not be deleted because of existing references: \n";
						for(int j=0; j<errorMessages.size(); j++){
							tempError=tempError.concat(" - "+errorMessages.get(j)+"\n");
						}
						JOptionPane.showMessageDialog(getThis(),
								tempError,
								"SpaceNet Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		deleteButton.setEnabled(false);
		editButtonPanel.add(deleteButton);
		contentPanel.add(editButtonPanel, c);
		c.gridy++;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		JButton okButton = new JButton("OK", new ImageIcon(getClass().getClassLoader().getResource("icons/database_go.png")));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataSourcePanel.saveData();
				scenarioPanel.updateView();
				dispose();
			}
		});
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel", new ImageIcon(getClass().getClassLoader().getResource("icons/database.png")));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, c);
		
		setContentPane(contentPanel);
		updateTables();
	}
	
	private static final int[] reverse(int[] array) {
		int[] out = new int[array.length];
		for(int i=0; i<array.length; i++)
			out[i] = array[array.length-1-i];
		return out;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		
		nodesCheck.setEnabled(dataSourcePanel.canLoad());
		edgesCheck.setEnabled(dataSourcePanel.canLoad());
		resourcesCheck.setEnabled(dataSourcePanel.canLoad());
		elementsCheck.setEnabled(dataSourcePanel.canLoad());
		loadButton.setEnabled(dataSourcePanel.canLoad());
		addButton.setEnabled(dataSourcePanel.canLoad());
		editButton.setEnabled(dataSourcePanel.canLoad() && 
				(nodeTable.getSelectedRowCount()==1 
						|| edgeTable.getSelectedRowCount()==1 
						|| resourceTable.getSelectedRowCount()==1
						|| elementTable.getSelectedRowCount()==1));
		deleteButton.setEnabled(dataSourcePanel.canLoad() && 
				(nodeTable.getSelectedRowCount()>0
						|| edgeTable.getSelectedRowCount()>0
						|| resourceTable.getSelectedRowCount()>0
						|| elementTable.getSelectedRowCount()>0));
	}
	
	/**
	 * Requests that the dialog update its fields based on the model and also
	 * requests the data source panel to update itself.
	 */
	public void updateTables() {
		nodeModel.setDataSource(getDataSource());
		edgeModel.setDataSource(getDataSource());
		resourceModel.setDataSource(getDataSource());
		elementModel.setDataSource(getDataSource());
		repaint();
	}
	
	/**
	 * Gets the this.
	 * 
	 * @return the this
	 */
	private DataSourceDialog getThis() {
		return this;
	}
	
	/**
	 * Gets the scenario panel.
	 * 
	 * @return the scenario panel
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public I_DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * Loads the data source.
	 */
	public void loadDataSource() {
		dataSourcePanel.saveData();
		
		LoadDataSourceCommand command = new LoadDataSourceCommand(this);
		command.execute();
	}
	
	/**
	 * Gets whether the already-instantiated elements should be updated.
	 * 
	 * @return whether the instantiated elements should be updated
	 */
	public boolean updateInstantiatedElements() {
		return elementsCheck.isSelected();
	}
	
	/**
	 * Update nodes.
	 * 
	 * @return true, if existing nodes are to be updated
	 */
	public boolean updateNodes() {
		return nodesCheck.isSelected();
	}
	
	/**
	 * Update edges.
	 * 
	 * @return true, if existing edges are to be updated
	 */
	public boolean updateEdges() {
		return edgesCheck.isSelected();
	}
	
	/**
	 * Update resources.
	 * 
	 * @return true, if existing resources are to be updated
	 */
	public boolean updateResources() {
		return resourcesCheck.isSelected();
	}
	
	/**
	 * Builds and displays the data source dialog.
	 * 
	 * @param scenarioPanel the scenario panel component
	 */
	public static void createAndShowGUI(ScenarioPanel scenarioPanel) {
		DataSourceDialog dialog = new DataSourceDialog(scenarioPanel);
		dialog.pack();
		dialog.setLocationRelativeTo(dialog.getParent());
		dialog.setVisible(true);
	}
}