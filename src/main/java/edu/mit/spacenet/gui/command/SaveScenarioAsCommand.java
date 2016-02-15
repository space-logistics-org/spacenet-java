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
