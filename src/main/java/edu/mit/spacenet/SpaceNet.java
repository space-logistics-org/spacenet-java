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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;

import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.SplashScreen;
import edu.mit.spacenet.io.XStreamEngine;
import edu.mit.spacenet.io.gson.demands.AggregatedDemandsAnalysis;
import edu.mit.spacenet.io.gson.demands.RawDemandsAnalysis;
import edu.mit.spacenet.io.gson.scenario.GsonEngine;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.DemandSimulator;

/**
 * This class is used to launch the SpaceNet application.
 * 
 * @author Paul Grogan
 */
public class SpaceNet {
	public static enum HeadlessMode {
		DEMANDS_RAW("demands-raw"),
		DEMANDS_AGGREGATED("demands-agg"),
		CONVERT_SCENARIO("convert");
		
		public final String label;
		
		private HeadlessMode(String label) {
			this.label = label;
		}
	}
	
	/**
	 * Launches the SpaceNet application.
	 * 
	 * @param args the args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		Option headless = Option.builder("h")
				.longOpt("headless")
				.argName("mode")
				.hasArg()
				.desc("Headless execution mode. Alternatives: " +
						HeadlessMode.DEMANDS_RAW.label + " (raw demand simulator)" +
						HeadlessMode.DEMANDS_AGGREGATED.label + " (aggregated demand simulator)" +
						HeadlessMode.CONVERT_SCENARIO.label + " (convert scenario format: xml <--> json)" +
						".")
				.build();
		options.addOption(headless);
		Option input = Option.builder("i")
				.longOpt("input")
				.argName("file path")
				.hasArg()
				.desc("Input file path.")
				.build();
		options.addOption(input);
		Option output = Option.builder("o")
				.longOpt("output")
				.argName("file path")
				.hasArg()
				.desc("Output file path.")
				.build();
		options.addOption(output);
		Option confirm = Option.builder("y")
				.longOpt("yes")
				.hasArg(false)
				.desc("Confirm overwrite output.")
				.build();
		options.addOption(confirm);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter helper = new HelpFormatter();
		
		try {
			CommandLine line = parser.parse(options, args);
			if(line.hasOption(headless)) {				
				String mode = line.getOptionValue(headless);
				
				if(mode.equalsIgnoreCase(HeadlessMode.DEMANDS_RAW.label) || mode.equalsIgnoreCase(HeadlessMode.DEMANDS_AGGREGATED.label)) {
					String scenarioFilePath = null;
					if(line.hasOption(input)) {
						scenarioFilePath = new File(line.getOptionValue(input)).getAbsolutePath();
					} else {
						System.err.println("Missing scenario file path.");
						helper.printHelp("Usage:", options);
						System.exit(0);
					}
					String outputFilePath = null;
					if(line.hasOption(output)) {
						outputFilePath = new File(line.getOptionValue(output)).getAbsolutePath();
					} else {
						System.err.println("Missing output file path.");
						helper.printHelp("Usage:", options);
						System.exit(0);
					}
					runDemandSimulator(scenarioFilePath, outputFilePath, line.hasOption(confirm), mode.equalsIgnoreCase(HeadlessMode.DEMANDS_RAW.label));
				} else if(mode.equalsIgnoreCase(HeadlessMode.CONVERT_SCENARIO.label)) {
					String inputFilePath = null;
					if(line.hasOption(input)) {
						inputFilePath = new File(line.getOptionValue(input)).getAbsolutePath();
					} else {
						System.err.println("Missing input file path.");
						helper.printHelp("Usage:", options);
						System.exit(0);
					}
					String outputFilePath = null;
					if(line.hasOption(output)) {
						outputFilePath = new File(line.getOptionValue(output)).getAbsolutePath();
					} else {
						System.err.println("Missing output file path.");
						helper.printHelp("Usage:", options);
						System.exit(0);
					}
					convertScenario(inputFilePath, outputFilePath, line.hasOption(confirm));
				} else {
					System.err.println("Unknown headless mode: " + mode);
					System.exit(0);
				}
				
			} else {
				String scenarioFilePath = null;
				if(line.hasOption(input)) {
					scenarioFilePath = new File(line.getOptionValue(input)).getAbsolutePath();
				}
				startGUI(scenarioFilePath);
			}
		} catch(ParseException ex) {
			System.err.println("Parsing failed: " + ex.getMessage());
			helper.printHelp("Usage:", options);
			System.exit(0);
		}
	}
	
	private static Scenario openScenario(String filePath) {
		String extension = FilenameUtils.getExtension(filePath);
		Scenario scenario = null;
		try {
			if(extension.equals("xml")) {
				scenario = XStreamEngine.openScenario(filePath);
				scenario.setFilePath(filePath);
			} else if(extension.equals("json")) {
				scenario = GsonEngine.openScenario(filePath);
				scenario.setFilePath(filePath);
			} else {
				throw new UnsupportedOperationException("Invalid file path: " + filePath);
			}
		} catch(IOException ex) {
			System.err.println("Failed to read scenario file: " + ex.getMessage());
			System.exit(1);
		}
		return scenario;
	}
	
	private static void saveScenario(Scenario scenario, boolean isOverwriteConfirmed) {
		File file = new File(scenario.getFilePath());
		if(file.exists() && !isOverwriteConfirmed) {
			Scanner in = new Scanner(System.in);
			String lastInput = "";
			do {
				System.out.println("Confirm to overwrite existing file " + scenario.getFilePath() + " (yes/no)");
				lastInput = in.nextLine();
				if(lastInput.equalsIgnoreCase("no") || lastInput.equalsIgnoreCase("n")) {
					in.close();
					return;
				}
			} while(!(lastInput.equalsIgnoreCase("yes") || lastInput.equalsIgnoreCase("y")));
			in.close();
		}
		String extension = FilenameUtils.getExtension(scenario.getFilePath());
		try {
			if(extension.equals("xml")) {
				XStreamEngine.saveScenario(scenario);
			} else if(extension.equals("json")) {
				GsonEngine.saveScenario(scenario);
			} else {
				throw new UnsupportedOperationException("Invalid file path: " + scenario);
			}
		} catch(IOException ex) {
			System.err.println("Failed to write scenario file: " + ex.getMessage());
			System.exit(1);
		}
	}
	
	private static void convertScenario(String inputFilePath, String outputFilePath, boolean isOverwriteConfirmed) {
		Scenario scenario = openScenario(inputFilePath);
		scenario.setFilePath(outputFilePath);
		saveScenario(scenario, isOverwriteConfirmed);
	}
	
	private static void runDemandSimulator(String scenarioFilePath, String outputFilePath, boolean isOverwriteConfirmed, boolean isRawDemands) {
		Scenario scenario = openScenario(scenarioFilePath);
		
		DemandSimulator simulator = new DemandSimulator(scenario);
		simulator.setDemandsSatisfied(false);
		simulator.simulate();

		File file = new File(outputFilePath);
		if(file.exists() && !isOverwriteConfirmed) {
			Scanner in = new Scanner(System.in);
			String lastInput = "";
			do {
				System.out.println("Confirm to overwrite existing file " + outputFilePath + " (yes/no)");
				lastInput = in.nextLine();
				if(lastInput.equalsIgnoreCase("no") || lastInput.equalsIgnoreCase("n")) {
					in.close();
					return;
				}
			} while(!(lastInput.equalsIgnoreCase("yes") || lastInput.equalsIgnoreCase("y")));
			in.close();
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outputFilePath));
			if(isRawDemands) {
				new Gson().toJson(
					RawDemandsAnalysis.createFrom(simulator),
					out
				);
			} else {
				new Gson().toJson(
					AggregatedDemandsAnalysis.createFrom(simulator),
					out
				);
			}
			out.close();
		} catch(IOException ex) {
			System.err.println("Failed to output file: " + ex.getMessage());
			System.exit(1);
		}
	}
	
	private static void startGUI(String scenarioFilePath) {
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
							if(scenarioFilePath != null) {
								SpaceNetFrame.getInstance().openScenario(scenarioFilePath);
							}
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