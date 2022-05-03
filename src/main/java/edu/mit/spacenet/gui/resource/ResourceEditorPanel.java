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
package edu.mit.spacenet.gui.resource;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.ClassOfSupply;
import edu.mit.spacenet.domain.Environment;
import edu.mit.spacenet.domain.resource.I_Resource;
import edu.mit.spacenet.domain.resource.ResourceType;
import edu.mit.spacenet.gui.component.UnitsWrapper;
import edu.mit.spacenet.util.GlobalParameters;

/**
 * A panel for editing resources.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class ResourceEditorPanel extends JPanel {
	private static final long serialVersionUID = 4522726218623374399L;
	private I_Resource resource;
	private ResourceEditorDialog dialog;
	
	private JTextField nameText, unitsText;
	private JComboBox<ClassOfSupply> classOfSupplyCombo;
	private JComboBox<Environment> environmentCombo;
	private SpinnerNumberModel unitMassModel, unitVolumeModel, packingFactorModel;
	private JSpinner unitMassSpinner, unitVolumeSpinner, packingFactorSpinner;
	private JTextArea descriptionText;
	
	/**
	 * Instantiates a new resource panel editor.
	 * 
	 * @param resourcePanel1 the resource panel1
	 * @param resource the resource
	 */
	public ResourceEditorPanel(ResourceEditorDialog dialog, I_Resource resource) {
		this.resource = resource;
		this.dialog = dialog;
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
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Class of Supply: "), c);
		
		c.gridx++;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		classOfSupplyCombo= new JComboBox<ClassOfSupply>();
		for(ClassOfSupply t : ClassOfSupply.values()) classOfSupplyCombo.addItem(t);
		add(classOfSupplyCombo, c);
		
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Environment: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		environmentCombo= new JComboBox<Environment>();
		for(Environment t : Environment.values()) environmentCombo.addItem(t);
		add(environmentCombo, c);
		environmentCombo.setToolTipText("Required environment for storage");
		
		c.gridx++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Packing Factor: "), c);
		
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		packingFactorModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE, 0.01);
		packingFactorSpinner = new JSpinner(packingFactorModel);
		packingFactorSpinner.setPreferredSize(new Dimension(75,20));
		add(new UnitsWrapper(packingFactorSpinner,"-"), c);
		packingFactorSpinner.setToolTipText("Fraction of additional COS 5 required to pack resource in a container [unitless]");
		
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Units: "), c);
		
		c.gridx++;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		unitsText = new JTextField(5);
		add(unitsText, c);
		unitsText.setToolTipText("Units in which resource is measured");
		
		c.gridy++;
		c.gridwidth = 1;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Unit Mass: "), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		unitMassModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE, GlobalParameters.getMassPrecision());
		unitMassSpinner = new JSpinner(unitMassModel);
		unitMassSpinner.setPreferredSize(new Dimension(75,20));
		add(new UnitsWrapper(unitMassSpinner, "kg"), c);
		unitMassSpinner.setToolTipText("Mass of one unit of resource [kilograms]");
		
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Unit Volume: "), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		unitVolumeModel = new SpinnerNumberModel(0,0,Double.MAX_VALUE, GlobalParameters.getVolumePrecision());
		unitVolumeSpinner = new JSpinner(unitVolumeModel);
		unitVolumeSpinner.setPreferredSize(new Dimension(75,20));
		add(new UnitsWrapper(unitVolumeSpinner, "m^3"), c);
		unitVolumeSpinner.setToolTipText("Volume of one unit of resource [cubic meters]");
		
		c.gridy++;
		c.gridx = 0;
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
		if(resource!= null){
			nameText.setText(resource.getName());
			classOfSupplyCombo.setSelectedItem(resource.getClassOfSupply());
			environmentCombo.setSelectedItem(resource.getEnvironment());
			unitsText.setText(resource.getUnits());
			unitMassModel.setValue(resource.getUnitMass());
			unitVolumeModel.setValue(resource.getUnitVolume());
			packingFactorModel.setValue(resource.getPackingFactor());
			descriptionText.setText(resource.getDescription());

			unitsText.setEnabled(resource.getResourceType()!=ResourceType.ITEM);
		}
	}
	
	/**
	 * Gets the resource.
	 * 
	 * @return the resource
	 */
	public I_Resource getResource() {
		return resource;
	}
	
	/**
	 * Checks if the resource is valid.
	 * 
	 * @return true, if resource is valid
	 */
	public boolean isResourceValid() {
		if(nameText.getText().length()==0){
			JOptionPane.showMessageDialog(dialog, 
					"Please enter a resource name.",
					"SpaceNet Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * Saves changes to the resource.
	 */
	public void saveResource() {
		resource.setName(nameText.getText());
		resource.setClassOfSupply((ClassOfSupply)classOfSupplyCombo.getSelectedItem());
		resource.setEnvironment((Environment)environmentCombo.getSelectedItem());
		resource.setUnits(unitsText.getText());
		resource.setPackingFactor(packingFactorModel.getNumber().doubleValue());
		resource.setUnitMass(unitMassModel.getNumber().doubleValue());
		resource.setUnitVolume(unitVolumeModel.getNumber().doubleValue());
		resource.setDescription(descriptionText.getText());
	}
}
