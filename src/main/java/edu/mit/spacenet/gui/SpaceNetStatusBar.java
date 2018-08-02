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
package edu.mit.spacenet.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * The status bar sits at the bottom of the main SpaceNet application and
 * provides feedback on long-duration processes (e.g. simulation, loading, etc)
 * with a status label and progress bar, similar to many web browsers.
 * 
 * @author Paul Grogan
 */
public class SpaceNetStatusBar extends JPanel {
	private static final long serialVersionUID = 8714221468753419089L;
	
	private JLabel statusLabel;
	private JProgressBar progressBar;
	
	/**
	 * The constructor.
	 */
	public SpaceNetStatusBar() {
		buildStatusBar();
	}
	private void buildStatusBar() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.weightx = .9;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		statusLabel = new JLabel();
		add(statusLabel, c);
		c.gridx++;
		c.weightx = .1;
		progressBar = new JProgressBar();
		add(progressBar, c);
	}
	
	/**
	 * Sets the status message in the lower-left hand corner and triggers the
	 * indeterminate progress bar.
	 * 
	 * @param message the status message to display
	 */
	public void setStatusMessage(String message) {
		setStatusMessage(message, true);
	}
	
	/**
	 * Sets the status message in the lower-left hand corner with the option to
	 * trigger the progress bar.
	 * 
	 * @param message the message to display
	 * @param triggerProcess whether to trigger the progress bar
	 */
	public void setStatusMessage(String message, boolean triggerProcess) {
		statusLabel.setText(message);
		if(triggerProcess) progressBar.setIndeterminate(true);
	}
	
	/**
	 * Clears the status message and resets the progress bar.
	 */
	public void clearStatusMessage() {
		statusLabel.setText("");
		progressBar.setIndeterminate(false);
	}
}
