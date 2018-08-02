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