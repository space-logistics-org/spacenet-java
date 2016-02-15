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
		
		availableTimeFractionModel = new SpinnerNumberModel(0,0,1,0.01);
		availableTimeFractionSpinner = new JSpinner(availableTimeFractionModel);
		availableTimeFractionSpinner.setPreferredSize(new Dimension(60,20));
		add(new UnitsWrapper(availableTimeFractionSpinner, ""));
		availableTimeFractionSpinner.setToolTipText("Fraction of time crew member is available to perform tasks or exploration");
	}
	
	/**
	 * Initializes the panel for a new crew member.
	 */
	private void initialize() {
		availableTimeFractionModel.setValue(person.getAvailableTimeFraction());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#getElement()
	 */
	@Override
	public CrewMember getElement() {
		return person;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#saveElement()
	 */
	@Override
	public void saveElement() {
		person.setAvailableTimeFraction(availableTimeFractionModel.getNumber().doubleValue());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isElementValid()
	 */
	@Override
	public boolean isElementValid() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.element.AbstractElementPanel#isVerticallyExpandable()
	 */
	public boolean isVerticallyExpandable() {
		return false;
	}
}
