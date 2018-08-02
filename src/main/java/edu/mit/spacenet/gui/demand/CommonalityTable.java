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
package edu.mit.spacenet.gui.demand;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.mit.spacenet.data.ElementPreview;
import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PartApplication;
import edu.mit.spacenet.domain.resource.I_Item;
import edu.mit.spacenet.scenario.Scenario;

/**
 * Table component for displaying the commonality of parts between elements.
 * 
 * @author Paul Grogan
 */
public class CommonalityTable extends JTable {
	private static final long serialVersionUID = -59617276707632343L;
	private CommonalityTab commonalityTab;
	private CustomTableModel model;
	
	/**
	 * The constructor.
	 * 
	 * @param commonalityTab the commonality tab
	 */
	public CommonalityTable(CommonalityTab commonalityTab) {
		this.commonalityTab = commonalityTab;
	}
	
	/**
	 * Initializes the table for a new or updated scenario.
	 */
	public void initialize() {
		model = new CustomTableModel(commonalityTab.getScenario());
		setModel(model);
		updateView();
	}
	
	/**
	 * Requests that the table update its information.
	 */
	public void updateView() {
		((CustomTableModel)getModel()).forceUpdate();
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		getColumnModel().getColumn(0).setHeaderValue("Part Type");
		getColumnModel().getColumn(0).setPreferredWidth(75);
		renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		for(ElementPreview element : ((CustomTableModel)getModel()).getElements()) {
			getColumnModel().getColumn(((CustomTableModel)getModel()).getElements().indexOf(element)+1).setHeaderValue(element.NAME);
			getColumnModel().getColumn(((CustomTableModel)getModel()).getElements().indexOf(element)+1).setCellRenderer(renderer);
		}
	}
	
	/**
	 * The Class CustomTableModel.
	 */
	private class CustomTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -4268705523869375904L;
		private Scenario scenario;
		private List<I_Item> parts;
		private List<ElementPreview> elements;
		
		/**
		 * Instantiates a new custom table model.
		 * 
		 * @param scenario the scenario
		 */
		public CustomTableModel(Scenario scenario) {
			this.scenario = scenario;
			parts = new ArrayList<I_Item>();
			elements = new ArrayList<ElementPreview>();
		}
		
		/**
		 * Force update.
		 */
		public void forceUpdate() {
			parts.clear();
			elements.clear();
			
			ArrayList<Integer> tids = new ArrayList<Integer>();
			for(I_Element element : scenario.getElements()) {
				if(!tids.contains(element.getTid())) {
					if(element.getParts().size() > 0) {
						for(ElementPreview e :scenario.getDataSource().getElementPreviewLibrary()) {
							if(e.ID==element.getTid()) {
								elements.add(e);
								tids.add(element.getTid());
								for(PartApplication a : element.getParts()) {
									if(!parts.contains(a.getPart())) parts.add(a.getPart());
								}
								break;
							}
						}
					}
				}
			}
			fireTableStructureChanged();
		}
		
		/**
		 * Gets the elements.
		 * 
		 * @return the elements
		 */
		public List<ElementPreview> getElements() {
			return elements;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#getColumnCount()
		 */
		public int getColumnCount() {
			if(elements == null) return 1;
			else return elements.size() + 1;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#getRowCount()
		 */
		public int getRowCount() {
			if(parts == null) return 0;
			else return parts.size();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			if(col==0) {
				return parts.get(row);
			} else {
				ElementPreview e = elements.get(col-1);
				try {
					I_Element element = scenario.getDataSource().loadElement(e.ID);
					for(PartApplication a : element.getParts()) {
						if(a.getPart().equals(parts.get(row))) {
							DecimalFormat format = new DecimalFormat("0.0%");
							return format.format(a.getPart().getUnitMass()*a.getQuantity()/element.getMass());
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, 
							"An error occurred while accessing the data source to load the element", 
							"SpaceNet Error", 
							JOptionPane.ERROR_MESSAGE);
				}
				return "0.0%";
			}
		}
	}
}
