/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
  public AbstractDemandModelPanel(DemandModelDialog demandModelDialog, I_DemandModel demandModel) {
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
