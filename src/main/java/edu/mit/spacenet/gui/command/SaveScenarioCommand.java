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

import javax.swing.SwingWorker;

import edu.mit.spacenet.gui.SpaceNetFrame;

/**
 * The command to save a scenario with the current filename (launches a save as
 * command if no file name exists).
 * 
 * @author Paul Grogan
 */
public class SaveScenarioCommand implements I_Command {
	private SpaceNetFrame spaceNetFrame;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNetFrame the SpaceNet frame
	 */
	public SaveScenarioCommand(SpaceNetFrame spaceNetFrame) {
		this.spaceNetFrame=spaceNetFrame;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		if(spaceNetFrame.getScenarioPanel().getScenario().getFilePath() == null) {
			SaveScenarioAsCommand command = new SaveScenarioAsCommand(spaceNetFrame);
			command.execute();
		} else {
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				public Void doInBackground() {
					SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Saving Scenario " 
							+ spaceNetFrame.getScenarioPanel().getScenario().getFileName()
							+ "...");
					spaceNetFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					spaceNetFrame.saveScenario();
					return null;
				}
				public void done() {
					SpaceNetFrame.getInstance().getStatusBar().clearStatusMessage();
					spaceNetFrame.setCursor(Cursor.getDefaultCursor());
					SpaceNetFrame.getInstance().getStatusBar().setStatusMessage("Scenario " 
							+ spaceNetFrame.getScenarioPanel().getScenario().getFileName() 
							+ " Saved Successfully", false);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	try {
			            		Thread.sleep(3000);
			            	} catch(InterruptedException e) { }
			            	spaceNetFrame.getStatusBar().setStatusMessage("", false);
			            }
		            });
					
				}
			};
			worker.execute();
		}
	}
}
