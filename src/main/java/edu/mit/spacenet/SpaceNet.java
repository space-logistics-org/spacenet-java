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
package edu.mit.spacenet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.SplashScreen;
import edu.mit.spacenet.io.XStreamEngine;
import edu.mit.spacenet.scenario.Scenario;

/**
 * This class is used to launch the SpaceNet application.
 * 
 * @author Paul Grogan
 */
public class SpaceNet {
	
	/**
	 * Launches the SpaceNet application.
	 * 
	 * @param args the args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		Option headless = Option.builder("h")
				.longOpt("headless")
				.argName("analysis")
				.hasArg()
				.desc("Headless execution.")
				.build();
		options.addOption(headless);
		Option file = Option.builder("s")
				.longOpt("scenario")
				.argName("scenario")
				.hasArg()
				.desc("Scenario file path.")
				.build();
		options.addOption(file);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter helper = new HelpFormatter();
		
		try {
			CommandLine line = parser.parse(options, args);
			String scenarioFilePath = null;
			if(line.hasOption(file)) {
				scenarioFilePath = new File(line.getOptionValue(file)).getAbsolutePath();
			}
			if(line.hasOption(headless)) {
				if(scenarioFilePath == null) {
					System.err.println("Missing scenario file path.");
					helper.printHelp("Usage:", options);
					System.exit(0);
				}
				System.out.println(line.getOptionValue(headless));

				Scenario scenario = XStreamEngine.openScenario(scenarioFilePath);
				scenario.setFilePath(scenarioFilePath);
				
				throw new UnsupportedOperationException("Not yet implemented!");
			} else {
				startGUI(scenarioFilePath);
			}
		} catch(ParseException ex) {
			System.err.println("Parsing failed: " + ex.getMessage());
			helper.printHelp("Usage:", options);
			System.exit(0);
		} catch(IOException ex) {
			System.err.println("Read scenario file failed: " + ex.getMessage());
			System.exit(0);
		}
	}
	
	public static void startGUI(String scenarioFilePath) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.put("TextArea.margin", new Insets(3,3,3,3));
					UIManager.put("Button.margin", new Insets(3,3,3,3));
					UIManager.put("ComboBox.background", Color.WHITE);
					UIManager.put("ProgressBar.background", Color.WHITE);
				} catch(Exception e) {
					e.printStackTrace();
				}
				// a customized swing worker is used to trigger the splash 
				// screen in a separate thread so the progress bar can spin 
				// while the components are loaded in the main frame
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					private SplashScreen splash;
					@Override
					protected Void doInBackground() {
						// create and display splash screen while application is 
						// loading
						splash = new SplashScreen();
						splash.setSize(500, 225);
						splash.setLocationRelativeTo(null);
						splash.setVisible(true);
						try {
							//TODO check if off screen (e.g. last used with different monitor settings
							Rectangle virtualBounds = new Rectangle();
							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
							GraphicsDevice[] gs = ge.getScreenDevices();
							for(int j=0; j<gs.length; j++) {
								GraphicsDevice gd = gs[j];
								GraphicsConfiguration[] gc = gd.getConfigurations();
								for(int i=0; i<gc.length; i++) {
									virtualBounds = virtualBounds.union(gc[i].getBounds());
								}
							}
							if(SpaceNetSettings.getInstance().getLastBounds()==null
									|| SpaceNetSettings.getInstance().getLastBounds().equals(new Rectangle())
									|| virtualBounds.intersection(SpaceNetSettings.getInstance().getLastBounds()).equals(new Rectangle())) {
								SpaceNetFrame.getInstance().setLocationRelativeTo(null);
								SpaceNetFrame.getInstance().setSize(new Dimension(1024,768));
							} else {
								SpaceNetFrame.getInstance().setBounds(virtualBounds.intersection(SpaceNetSettings.getInstance().getLastBounds()));
							}
							SpaceNetFrame.getInstance().setVisible(true);
							SpaceNetFrame.getInstance().openScenario(scenarioFilePath);
						} catch(Exception ex) {
							// display error message if one occurs... since this
							// is inside a worker thread, the stack trace will
							// not be printed unless directed handled here
							JOptionPane.showMessageDialog(null, 
									"An error of type \"" + 
									ex.getClass().getSimpleName() + 
									"\" occurred while launching SpaceNet.\n", 
									"SpaceNet Error",
									JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
						// dispose of the (finished) splash screen
						splash.setVisible(false);
						splash.dispose();
						return null;
					}
				};
				
				// execute the worker
				worker.execute();
            }
        });
	}
}