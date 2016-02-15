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
package edu.mit.spacenet.gui.command;

import javax.swing.JOptionPane;

import edu.mit.spacenet.gui.SpaceNetFrame;

/**
 * The command to close a scenario.
 * 
 * @author Paul Grogan
 */
public class CloseScenarioCommand {
	private SpaceNetFrame spaceNetFrame;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNetFrame the SpaceNet frame component.
	 */
	public CloseScenarioCommand(SpaceNetFrame spaceNetFrame) {
		this.spaceNetFrame=spaceNetFrame;
	}
	
	/**
	 * Execute.
	 */
	public void execute() {
		int answer = JOptionPane.showOptionDialog(spaceNetFrame, 
				"Save " + spaceNetFrame.getScenarioPanel().getScenario().getName() + " before closing?", 
				"Close Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	    if(answer == JOptionPane.YES_OPTION) {
	    	SaveScenarioCommand command = new SaveScenarioCommand(spaceNetFrame);
	    	command.execute();
	    }
	    if(answer != JOptionPane.CANCEL_OPTION) spaceNetFrame.closeScenario();
	}
}
