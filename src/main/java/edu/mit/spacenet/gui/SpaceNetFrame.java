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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.io.ScenarioFileFilter;
import edu.mit.spacenet.io.XStreamEngine;
import edu.mit.spacenet.io.gson.scenario.GsonEngine;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.util.IconLibrary;
import edu.mit.spacenet.util.IdGenerator;

/**
 * The frame to contain the SpaceNet application.
 * 
 * @author Paul Grogan
 */
public class SpaceNetFrame extends JFrame {
	private static final long serialVersionUID = -6005760557525734285L;
	
	private static SpaceNetFrame instance;
	
	/**
	 * Gets the singleton reference to the SpaceNet frame.
	 * 
	 * @return the singleton frame
	 */
	public static SpaceNetFrame getInstance() {
		if(instance==null) instance = new SpaceNetFrame();
		return instance;
	}
	
	private JFileChooser scenarioChooser;
	private SpaceNetSettingsDialog spaceNetSettingsDialog;
	private AboutDialog aboutDialog;
	private SpaceNetMenuBar menuBar;
	private JPanel contentPanel;
	private ScenarioPanel scenarioPanel;
	private SpaceNetStatusBar statusBar;
	
	private SpaceNetFrame() {
		scenarioChooser = new JFileChooser(SpaceNetSettings.getInstance().getDefaultDirectory()) {
			private static final long serialVersionUID = 5853237903722516861L;
			public void approveSelection() {
				if(getDialogType()==JFileChooser.SAVE_DIALOG) {
					File file = getSelectedFile();
					if (file != null && file.exists()) {
						int answer = JOptionPane.showOptionDialog(this, 
								"Scenario '" + file.getAbsolutePath() + "' already exists. Overwrite?", 
								"Save Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					    if (answer == JOptionPane.NO_OPTION) {
							return;
					    }
					}
				}
				super.approveSelection();
		    }
		};
		scenarioChooser.setFileFilter(new ScenarioFileFilter());
		scenarioPanel = new ScenarioPanel(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(800,600));
		// try to specify multiple icon sizes (used for windows 7 task bar)
	    try {
	        java.lang.reflect.Method method = Class.forName("java.awt.Window").getDeclaredMethod("setIconImages", java.util.List.class);
			ArrayList<Image> images = new ArrayList<Image>();
			images.add(IconLibrary.getIcon("spacenet_icon_16").getImage());
			images.add(IconLibrary.getIcon("spacenet_icon_32").getImage());
	        method.invoke(this,images);
	    } catch( Exception e ) {
	    	// otherwise assign small icon only
	        setIconImage(IconLibrary.getIcon("spacenet_icon_16").getImage());
	    }
		buildFrame();
	}
	
	/**
	 * Builds the frame.
	 */
	private void buildFrame() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitSpaceNet();
			}
		});

		menuBar = new SpaceNetMenuBar();
		setJMenuBar(menuBar);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBackground(Color.LIGHT_GRAY);
		add(contentPanel, c);
		c.gridy++;
		c.weighty = 0;
		statusBar = new SpaceNetStatusBar();
		add(statusBar, c);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if(scenarioPanel.getScenario()==null) setTitle("SpaceNet 2.5r2");
		else setTitle("SpaceNet 2.5r2 | " + scenarioPanel.getScenario().getName());
	}
	
	/**
	 * Gets the about dialog.
	 * 
	 * @return the about dialog
	 */
	public AboutDialog getAboutDialog() {
		if(aboutDialog==null) aboutDialog = new AboutDialog();
		return aboutDialog;
	}
	
	/**
	 * Gets the settings dialog.
	 * 
	 * @return the settings dialog
	 */
	public SpaceNetSettingsDialog getSpaceNetSettingsDialog() {
		if(spaceNetSettingsDialog==null) spaceNetSettingsDialog = new SpaceNetSettingsDialog();
		return spaceNetSettingsDialog;
	}
	
	/**
	 * Gets the status bar.
	 * 
	 * @return the status bar
	 */
	public SpaceNetStatusBar getStatusBar() {
		return statusBar;
	}
	
	/**
	 * Creates and displays a new scenario.
	 */
	public void newScenario() {
		setScenario(new Scenario());
	}
	
	/**
	 * Opens a saved scenario from a passed file path.
	 * 
	 * @param filePath the file path of the saved scenario
	 */
	public void openScenario(String filePath) {
		try {
			String extension = FilenameUtils.getExtension(filePath);
			if(extension.equals("xml")) {
				Scenario scenario = XStreamEngine.openScenario(filePath);
				scenario.setFilePath(filePath);
				setScenario(scenario);
			} else if(extension.equals("json")) {
				Scenario scenario = GsonEngine.openScenario(filePath);
				scenario.setFilePath(filePath);
				setScenario(scenario);
			}
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, 
					"The scenario file path (" + filePath + ") is invalid.", 
					"SpaceNet Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, 
					"The open process failed due to an I/O exception.", 
					"SpaceNet Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Closes the scenario.
	 */
	public void closeScenario() {
    	setScenario(null);
	}
	
	/**
	 * Saves the open scenario.
	 */
	public void saveScenario() {
		try {
			String extension = FilenameUtils.getExtension(scenarioPanel.getScenario().getFileName());
			if(extension.equals("xml")) {
				XStreamEngine.saveScenario(scenarioPanel.getScenario());
			} else if(extension.equals("json")) {
				GsonEngine.saveScenario(scenarioPanel.getScenario());
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, 
					"The scenario file path (" + scenarioPanel.getScenario().getFilePath() + ") is invalid.", 
					"SpaceNet Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, 
					"The save process failed due to an I/O exception.", 
					"SpaceNet Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Sets the scenario (and updates the id generator last value) and updates 
	 * the scenario panel, or closes the scenario if null.
	 * 
	 * @param scenario the new scenario
	 */
	private void setScenario(Scenario scenario) {
		if(scenario==null) {
			scenarioPanel.setScenario(null);
	    	contentPanel.removeAll();
		} else {
			int lastUid = 0;
			for(I_Element element : scenario.getElements()) {
				if(element.getUid()>lastUid) lastUid = element.getUid();
			}
			for(I_Element container : scenario.getManifest().getContainers()) {
				if(container.getUid()>lastUid) lastUid = container.getUid();
			}
			IdGenerator.setLastUid(lastUid);
			scenarioPanel.setScenario(scenario);
	    	contentPanel.removeAll();
			contentPanel.add(scenarioPanel, BorderLayout.CENTER);
		}
		validate();
		repaint();
	}
	
	/**
	 * Gets the scenario panel component.
	 * 
	 * @return the scenario panel.
	 */
	public ScenarioPanel getScenarioPanel() {
		return scenarioPanel;
	}
	
	/**
	 * Gets the scenario file chooser.
	 * 
	 * @return the scenario file chooser
	 */
	public JFileChooser getScenarioChooser() {
		return scenarioChooser;
	}
	
	/**
	 * Exit space net.
	 */
	public void exitSpaceNet() {
		if(getScenarioPanel().getScenario() != null) {
			int answer = JOptionPane.showOptionDialog(this, 
					"Save '" + scenarioPanel.getScenario().getName() + "' before exiting?", 
					"Close Warning", JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, null, null);
		    if (answer == JOptionPane.YES_OPTION) {
		    	saveScenario();
		    }
		    if (answer != JOptionPane.CANCEL_OPTION) {
		    	SpaceNetSettings.saveSettings();
		    	System.exit(0);
		    }
		} else {
	    	SpaceNetSettings.saveSettings();
	    	System.exit(0);
		}
	}
}
