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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.io.XMLFileFilter;

/**
 * The command to save a scenario with a new filename.
 * 
 * @author Paul Grogan
 */
public class SaveScenarioAsCommand implements I_Command {
	private SpaceNetFrame spaceNetFrame;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNetFrame the SpaceNet frame
	 */
	public SaveScenarioAsCommand(SpaceNetFrame spaceNetFrame) {
		this.spaceNetFrame=spaceNetFrame;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.gui.command.I_Command#execute()
	 */
	public void execute() {
		JFileChooser fileChooser = new JFileChooser(SpaceNetSettings.getInstance().getDefaultDirectory()) {
			private static final long serialVersionUID = 5853237903722516861L;
			public void approveSelection() {
				File file = getSelectedFile();
				if (file != null && file.exists()) {
					int answer = JOptionPane.showOptionDialog(spaceNetFrame, 
							"File " + file.getAbsolutePath() + " already exists. Overwrite?", 
							"Save Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
				    if (answer == JOptionPane.NO_OPTION) {
						return;
				    }
				}
				super.approveSelection();
		    }
		};
		fileChooser.setFileFilter(new XMLFileFilter());
		if (fileChooser.showSaveDialog(spaceNetFrame) == JFileChooser.APPROVE_OPTION) {
			String filepath = fileChooser.getSelectedFile().getAbsolutePath();
			if(!filepath.substring(filepath.length() - 4, filepath.length()).equals(".xml")) {
				filepath += ".xml";
			}
			spaceNetFrame.getScenarioPanel().getScenario().setFilePath(filepath);
			SaveScenarioCommand command = new SaveScenarioCommand(spaceNetFrame);
			command.execute();
		}
	}
}
