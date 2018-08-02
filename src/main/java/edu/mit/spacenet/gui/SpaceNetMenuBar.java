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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import edu.mit.spacenet.gui.command.CloseScenarioCommand;
import edu.mit.spacenet.gui.command.NewScenarioCommand;
import edu.mit.spacenet.gui.command.OpenScenarioCommand;
import edu.mit.spacenet.gui.command.SaveScenarioAsCommand;
import edu.mit.spacenet.gui.command.SaveScenarioCommand;
import edu.mit.spacenet.gui.data.DataSourceDialog;

/**
 * The menu bar that contains all of the items in the pull-down menus. Most menu
 * items use commands which abstract the actual triggering of the various
 * features so they can be used elsewhere (e.g. exit on window close OR on
 * selecting the exit command).
 * 
 * @author Paul Grogan
 */
public class SpaceNetMenuBar extends JMenuBar {
	private static final long serialVersionUID = -5251667632821734398L;
	
	private JMenu fileMenu, editMenu, toolsMenu, helpMenu;
	private JMenuItem newScenarioMenuItem, openScenarioMenuItem, 
		closeScenarioMenuItem, saveScenarioMenuItem, saveScenarioAsMenuItem, 
		exitMenuItem, optionsMenuItem, dataSourceMenuItem, preferencesMenuItem, 
		helpMenuItem, aboutMenuItem;
	private JRadioButtonMenuItem networkMenuItem, missionsMenuItem, 
		demandsMenuItem, manifestMenuItem, simulationMenuItem;
	private JCheckBoxMenuItem autoRefreshMenuItem;
	
	/**
	 * The constructor.
	 * 
	 * @param spaceNet the parent SpaceNet frame
	 */
	public SpaceNetMenuBar() {
	    buildFileMenu();
	    add(fileMenu);
	    
	    buildEditMenu();
	    add(editMenu);
	    
	    buildToolsMenu();
	    add(toolsMenu);
	    
	    buildHelpMenu();
	    add(helpMenu);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		boolean fileOpen = SpaceNetFrame.getInstance().getScenarioPanel().getScenario()!=null;
		newScenarioMenuItem.setEnabled(!fileOpen);
		openScenarioMenuItem.setEnabled(!fileOpen);
		closeScenarioMenuItem.setEnabled(fileOpen);
		saveScenarioMenuItem.setEnabled(fileOpen);
		saveScenarioAsMenuItem.setEnabled(fileOpen);
		editMenu.setEnabled(fileOpen);
		if(fileOpen) {
			networkMenuItem.setSelected(SpaceNetFrame.getInstance().getScenarioPanel().isEditingNetwork());
			missionsMenuItem.setSelected(SpaceNetFrame.getInstance().getScenarioPanel().isEditingMissions());
			demandsMenuItem.setSelected(SpaceNetFrame.getInstance().getScenarioPanel().isEditingDemands());
			manifestMenuItem.setSelected(SpaceNetFrame.getInstance().getScenarioPanel().isEditingManifest());
			simulationMenuItem.setSelected(SpaceNetFrame.getInstance().getScenarioPanel().isEditingSimulation());
		}
	}
	
	/**
	 * Builds the file menu.
	 */
	private void buildFileMenu() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
        newScenarioMenuItem = new JMenuItem("New Scenario", KeyEvent.VK_N);
        newScenarioMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newScenarioMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NewScenarioCommand command = new NewScenarioCommand(SpaceNetFrame.getInstance());
        		command.execute();
        	}
        });
        fileMenu.add(newScenarioMenuItem);
        
        openScenarioMenuItem = new JMenuItem("Open Scenario", KeyEvent.VK_O);
        openScenarioMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openScenarioMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		OpenScenarioCommand command = new OpenScenarioCommand(SpaceNetFrame.getInstance());
        		command.execute();
        	}
        });
        fileMenu.add(openScenarioMenuItem);
        
        closeScenarioMenuItem = new JMenuItem("Close Scenario", KeyEvent.VK_C);
        closeScenarioMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        closeScenarioMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CloseScenarioCommand command = new CloseScenarioCommand(SpaceNetFrame.getInstance());
        		command.execute();
        	}
        });
        fileMenu.add(closeScenarioMenuItem);
        
        fileMenu.addSeparator();
        
        saveScenarioMenuItem = new JMenuItem("Save Scenario", KeyEvent.VK_S);
        saveScenarioMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveScenarioMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SaveScenarioCommand command = new SaveScenarioCommand(SpaceNetFrame.getInstance());
        		command.execute();
        	}
        });
        fileMenu.add(saveScenarioMenuItem);
        
        saveScenarioAsMenuItem = new JMenuItem("Save Scenario As...", KeyEvent.VK_A);
        saveScenarioAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        saveScenarioAsMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SaveScenarioAsCommand command = new SaveScenarioAsCommand(SpaceNetFrame.getInstance());
        		command.execute();
        	}
        });
        fileMenu.add(saveScenarioAsMenuItem);
        
        fileMenu.addSeparator();
        
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SpaceNetFrame.getInstance().exitSpaceNet();
        	}
        });
        fileMenu.add(exitMenuItem);
	}
	
	/**
	 * Builds the edit menu.
	 */
	private void buildEditMenu() {
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		
		optionsMenuItem = new JMenuItem("Options");
		optionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().getOptionsDialog().showDialog();
			}
		});
		editMenu.add(optionsMenuItem);
		
		dataSourceMenuItem = new JMenuItem("Data Source");
		dataSourceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSourceDialog.createAndShowGUI(SpaceNetFrame.getInstance().getScenarioPanel());
			}
		});
		editMenu.add(dataSourceMenuItem);
		
		editMenu.add(new JSeparator());
		
		networkMenuItem = new JRadioButtonMenuItem("Network");
		networkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().editNetwork();
			}
		});
		editMenu.add(networkMenuItem);
		
		missionsMenuItem = new JRadioButtonMenuItem("Missions");
		missionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().editMissions();
			}
		});
		editMenu.add(missionsMenuItem);
        
        demandsMenuItem = new JRadioButtonMenuItem("Demands");
        demandsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().editDemands();
			}
		});
        editMenu.add(demandsMenuItem);
        
        manifestMenuItem = new JRadioButtonMenuItem("Manifest");
        manifestMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().editManifest();
			}
		});
        editMenu.add(manifestMenuItem);
        
        simulationMenuItem = new JRadioButtonMenuItem("Simulation");
        simulationMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceNetFrame.getInstance().getScenarioPanel().editSimulation();
			}
		});
        editMenu.add(simulationMenuItem);
	}
	
	/**
	 * Builds the tools menu.
	 */
	private void buildToolsMenu() {
		toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		
		autoRefreshMenuItem = new JCheckBoxMenuItem("Auto-Refresh Charts");
		autoRefreshMenuItem.setSelected(SpaceNetSettings.getInstance().isAutoRefresh());
		autoRefreshMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				SpaceNetSettings.getInstance().setAutoRefresh(e.getStateChange()==ItemEvent.SELECTED);
		    	SpaceNetSettings.saveSettings();
				SpaceNetFrame.getInstance().getScenarioPanel().updateView();
			}
        });
        toolsMenu.add(autoRefreshMenuItem);
        
    	toolsMenu.addSeparator();
        
        preferencesMenuItem = new JMenuItem("Preferences", KeyEvent.VK_P);
        preferencesMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SpaceNetFrame.getInstance().getSpaceNetSettingsDialog().showDialog();
        	}
        });
        toolsMenu.add(preferencesMenuItem);
        
	}
	
	/**
	 * Builds the help menu.
	 */
	private void buildHelpMenu() {
		helpMenu = new JMenu("Help");
	    helpMenu.setMnemonic(KeyEvent.VK_H);
	    
        helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
        helpMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(SpaceNetFrame.getInstance(), 
        				"For help with SpaceNet, please see:\n\n" +
        				"SpaceNet User's Manual (docs/manual)\n" +
        				"Quick Start Tutorial (docs/tutorial)\n" +
        				"SpaceNet Website (http://spacenet.mit.edu)", 
        				"SpaceNet Help", JOptionPane.PLAIN_MESSAGE);
        	}
        });
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(helpMenuItem);
        
        helpMenu.addSeparator();
        
        aboutMenuItem = new JMenuItem("About SpaceNet", KeyEvent.VK_A);
        aboutMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SpaceNetFrame.getInstance().getAboutDialog().showDialog();
        	}
        });
        helpMenu.add(aboutMenuItem);
	}
}
