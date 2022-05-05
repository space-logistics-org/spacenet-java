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
package edu.mit.spacenet.gui.element;

import javax.swing.JPanel;

import edu.mit.spacenet.domain.element.I_Element;

/**
 * Abstract component that serves as an interface to the various element panels.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public abstract class AbstractElementPanel extends JPanel {
  private static final long serialVersionUID = 4132394191670439311L;

  private ElementDialog elementDialog;

  /**
   * The constructor.
   * 
   * @param elementDialog the element dialog component
   * @param element the element
   */
  public AbstractElementPanel(ElementDialog elementDialog, I_Element element) {
    this.elementDialog = elementDialog;
  }

  /**
   * Requests that the dialog save all element-specific data.
   */
  public abstract void saveElement();

  /**
   * Gets the element dialog component.
   * 
   * @return the element dialog
   */
  public ElementDialog getElementDialog() {
    return elementDialog;
  }

  /**
   * Checks if is vertically expandable.
   *
   * @return true, if is vertically expandable
   */
  public abstract boolean isVerticallyExpandable();

  /**
   * Gets the element.
   * 
   * @return the element
   */
  public abstract I_Element getElement();

  /**
   * Checks if is element valid.
   * 
   * @return true, if is element valid
   */
  public abstract boolean isElementValid();

}
