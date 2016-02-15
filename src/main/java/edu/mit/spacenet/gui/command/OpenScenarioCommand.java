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

import java.awt.Cursor;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import edu.mit.spacenet.gui.SpaceNetFrame;

/**
 * The command to open a saved scenario.
 * 
 * @author Paul Grogan
 */
public class OpenScenarioCommand implements I_Command {
	private SpaceNetFrame spaceNetFrame;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNetFrame the SpaceNet frame
	 */
	public OpenScenarioCommand(SpaceNetFrame spaceNetFrame) {
		this.spaceNetFrame=spaceNetFrame;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		int returnVal = spaceNetFrame.getScenarioChooser().showOpenDialog(spaceNetFrame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				public Void doInBackground() {
					SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Opening Scenario...");
					spaceNetFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					try {
						spaceNetFrame.openScenario(spaceNetFrame.getScenarioChooser().getSelectedFile().getAbsolutePath());
					} catch(Exception ex) { 
						// display error message if one occurs... since this
						// is inside a worker thread, the stack trace will
						// not be printed unless directed handled here
						JOptionPane.showMessageDialog(null, 
								"An error of type \"" + 
								ex.getClass().getSimpleName() + 
								"\" occurred while opening " + 
								spaceNetFrame.getScenarioChooser().getSelectedFile() + 
								".\n", 
								"SpaceNet Error",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
					return null;
				}
				public void done() {
					SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
					spaceNetFrame.setCursor(Cursor.getDefaultCursor());
				}
			};
			worker.execute();
		}
		
	}
}