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
package edu.mit.spacenet.gui.model;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.spacenet.domain.model.SparingByMassDemandModel;
import edu.mit.spacenet.domain.resource.Demand;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.component.DemandTable;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * The panel for viewing and editing sparing by mass demand models.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class SparingByMassDemandModelPanel extends AbstractDemandModelPanel {
	private static final long serialVersionUID = -966209133130231178L;
	
	private SparingByMassDemandModel demandModel;
	
	private JCheckBox partsListEnabledCheck;
	private SpinnerNumberModel unpressRateModel, pressRateModel;
	private JSpinner unpressRateSpinner, pressRateSpinner;
	private DemandTable demandsTable;
	
	/**
	 * Instantiates a new sparing by mass demand model panel.
	 * 
	 * @param demandModelDialog the demand model dialog
	 * @param demandModel the demand model
	 */
	public SparingByMassDemandModelPanel(DemandModelDialog demandModelDialog,
			SparingByMassDemandModel demandModel) {
		super(demandModelDialog, demandModel);
		this.demandModel = demandModel;
		buildPanel();
		initialize();
	}
	
	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		partsListEnabledCheck = new JCheckBox("Use element parts to generate spares resource types");
		partsListEnabledCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDemands();
			}
		});
		add(partsListEnabledCheck, c);
		partsListEnabledCheck.setToolTipText("Use part applications to guide the resource types of demanded spares");
		
		c.gridy++;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("Unpress. Rate: "), c);
		c.gridy++;
		add(new JLabel("Press. Rate: "), c);
		c.gridy++;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Annual Demands: "), c);
		
		c.gridx++;
		c.gridy = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		unpressRateModel = new SpinnerNumberModel(0.00,0.00,1000,.5);
		unpressRateSpinner = new JSpinner(unpressRateModel);
		unpressRateSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDemands();
			}
		});
		add(new UnitsWrapper(unpressRateSpinner, "% mass / year"), c);
		unpressRateSpinner.setToolTipText("Unpressurized spares demands per year [percent]");
		
		c.gridy++;
		pressRateModel = new SpinnerNumberModel(0.00,0.00,1000,.5);
		pressRateSpinner = new JSpinner(pressRateModel);
		pressRateSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDemands();
			}
		});
		add(new UnitsWrapper(pressRateSpinner, "% mass / year"), c);
		pressRateSpinner.setToolTipText("Pressurized spares demands per year [percent]");
		
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		demandsTable = new DemandTable(SpaceNetFrame.getInstance().getScenarioPanel().getScenario().getDataSource().getResourceLibrary());
		demandsTable.setEnabled(false);
		JScrollPane demandsScroll = new JScrollPane(demandsTable);
		demandsScroll.setPreferredSize(new Dimension(400,100));
		add(demandsScroll, c);
	}
	
	/**
	 * Update demands.
	 */
	private void updateDemands() {
		demandsTable.removeAllDemands();
		for(Demand demand : demandModel.generateDemands(365.25, 
				pressRateModel.getNumber().doubleValue()/100d, 
				unpressRateModel.getNumber().doubleValue()/100d, 
				partsListEnabledCheck.isSelected())) {
			demandsTable.addDemand(demand);
		}
		repaint();
	}
	
	/**
	 * Initializes the panel for a new demand model.
	 */
	private void initialize() {
		demandModel.setElement(getDemandModelDialog().getStateDialog().getElementDialog().getElement());
		unpressRateModel.setValue(demandModel.getUnpressurizedSparesRate()*100);
		pressRateModel.setValue(demandModel.getPressurizedSparesRate()*100);
		partsListEnabledCheck.setSelected(demandModel.isPartsListEnabled());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#getDemandModel()
	 */
	public SparingByMassDemandModel getDemandModel() {
		return demandModel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#saveDemandModel()
	 */
	public void saveDemandModel() {
		demandModel.setElement(getDemandModelDialog().getStateDialog().getElementDialog().getElement());
		demandModel.setUnpressurizedSparesRate(unpressRateModel.getNumber().doubleValue()/100d);
		demandModel.setPressurizedSparesRate(pressRateModel.getNumber().doubleValue()/100d);
		demandModel.setPartsListEnabled(partsListEnabledCheck.isSelected());
	}

	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.model.AbstractDemandModelPanel#isDemandModelValid()
	 */
	@Override
	public boolean isDemandModelValid() {
		return true;
	}
}
