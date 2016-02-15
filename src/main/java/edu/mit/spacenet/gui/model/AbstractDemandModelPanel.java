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

import javax.swing.JPanel;

import edu.mit.spacenet.domain.model.I_DemandModel;

/**
 * An abstract class that serves as an interface to the demand model panels.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class AbstractDemandModelPanel extends JPanel {
	private static final long serialVersionUID = -4507539878695833125L;

	private DemandModelDialog demandModelDialog;
	
	/**
	 * Instantiates a new abstract demand model panel.
	 * 
	 * @param demandModelDialog the demand model dialog
	 * @param demandModel the demand model
	 */
	public AbstractDemandModelPanel(DemandModelDialog demandModelDialog, 
			I_DemandModel demandModel) {
		this.demandModelDialog = demandModelDialog;
	}
	
	/**
	 * Save demand model.
	 */
	public abstract void saveDemandModel();
	
	/**
	 * Gets the demand model dialog.
	 * 
	 * @return the demand model dialog
	 */
	public DemandModelDialog getDemandModelDialog() {
		return demandModelDialog;
	}

	/**
	 * Checks if is demand model valid.
	 * 
	 * @return true, if is demand model valid
	 */
	public abstract boolean isDemandModelValid();
	
	/**
	 * Gets the demand model.
	 * 
	 * @return the demand model
	 */
	public abstract I_DemandModel getDemandModel();
}