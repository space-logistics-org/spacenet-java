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

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import edu.mit.spacenet.domain.element.CrewMember;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * Element panel for viewing and editing crew member-specific inputs.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Ivo Ferreira
 */
public class CrewMemberPanel extends AbstractElementPanel {
  private static final long serialVersionUID = 6335483106289763369L;

  private CrewMember person;

  private SpinnerNumberModel availableTimeFractionModel;
  private JSpinner availableTimeFractionSpinner;

  /**
   * Instantiates a new crew member panel.
   * 
   * @param elementDialog the element dialog
   * @param person the person
   */
  public CrewMemberPanel(ElementDialog elementDialog, CrewMember person) {
    super(elementDialog, person);
    this.person = person;
    buildPanel();
    initialize();
  }

  /**
   * Builds the panel.
   */
  private void buildPanel() {
    setOpaque(false);
    setLayout(new FlowLayout(FlowLayout.LEFT));
    add(new JLabel("Available Time Fraction: "));

    availableTimeFractionModel = new SpinnerNumberModel(0, 0, 1, 0.01);
    availableTimeFractionSpinner = new JSpinner(availableTimeFractionModel);
    availableTimeFractionSpinner.setPreferredSize(new Dimension(60, 20));
    add(new UnitsWrapper(availableTimeFractionSpinner, ""));
    availableTimeFractionSpinner.setToolTipText(
        "Fraction of time crew member is available to perform tasks or exploration");
  }

  /**
   * Initializes the panel for a new crew member.
   */
  private void initialize() {
    availableTimeFractionModel.setValue(person.getAvailableTimeFraction());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
   */
  @Override
  public CrewMember getElement() {
    return person;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
   */
  @Override
  public void saveElement() {
    person.setAvailableTimeFraction(availableTimeFractionModel.getNumber().doubleValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
   */
  @Override
  public boolean isElementValid() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isVerticallyExpandable()
   */
  public boolean isVerticallyExpandable() {
    return false;
  }
}
