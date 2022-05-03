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
package edu.mit.spacenet.gui.event;

import javax.swing.JPanel;

import edu.mit.spacenet.simulator.event.I_Event;

/**
 * The Class AbstractEventPanel.
 * 
 * @author Paul Grogan
 */
public abstract class AbstractEventPanel extends JPanel {
  private static final long serialVersionUID = 1383454658585330190L;

  private EventDialog eventDialog;

  /**
   * Instantiates a new abstract event panel.
   * 
   * @param eventDialog the event dialog
   * @param event the event
   */
  public AbstractEventPanel(EventDialog eventDialog, I_Event event) {
    this.eventDialog = eventDialog;
  }

  /**
   * Saves the event.
   */
  public abstract void saveEvent();

  /**
   * Updates the view.
   */
  public abstract void updateView();

  /**
   * Gets the event dialog.
   * 
   * @return the event dialog
   */
  public EventDialog getEventDialog() {
    return eventDialog;
  }

  /**
   * Gets the event.
   * 
   * @return the event
   */
  public abstract I_Event getEvent();
}
