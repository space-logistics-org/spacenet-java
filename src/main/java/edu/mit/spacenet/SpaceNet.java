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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
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
import org.threeten.extra.PeriodDuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.gson.typeadapters.UtcDateTypeAdapter;

import edu.mit.spacenet.domain.element.ElementType;
import edu.mit.spacenet.domain.model.DemandModelType;
import edu.mit.spacenet.domain.network.edge.EdgeType;
import edu.mit.spacenet.domain.network.node.NodeType;
import edu.mit.spacenet.gui.SpaceNetFrame;
import edu.mit.spacenet.gui.SpaceNetSettings;
import edu.mit.spacenet.gui.SplashScreen;
import edu.mit.spacenet.io.XStreamEngine;
import edu.mit.spacenet.io.gson.demands.AggregatedDemandsAnalysis;
import edu.mit.spacenet.io.gson.demands.RawDemandsAnalysis;
import edu.mit.spacenet.io.gson.scenario.AddResources;
import edu.mit.spacenet.io.gson.scenario.BurnEvent;
import edu.mit.spacenet.io.gson.scenario.Carrier;
import edu.mit.spacenet.io.gson.scenario.ConsumablesDemandModel;
import edu.mit.spacenet.io.gson.scenario.ConsumeResources;
import edu.mit.spacenet.io.gson.scenario.CreateElements;
import edu.mit.spacenet.io.gson.scenario.CrewMember;
import edu.mit.spacenet.io.gson.scenario.DemandModel;
import edu.mit.spacenet.io.gson.scenario.Edge;
import edu.mit.spacenet.io.gson.scenario.Element;
import edu.mit.spacenet.io.gson.scenario.EvaEvent;
import edu.mit.spacenet.io.gson.scenario.Event;
import edu.mit.spacenet.io.gson.scenario.Exploration;
import edu.mit.spacenet.io.gson.scenario.FlightEdge;
import edu.mit.spacenet.io.gson.scenario.FlightTransport;
import edu.mit.spacenet.io.gson.scenario.ImpulseDemandModel;
import edu.mit.spacenet.io.gson.scenario.LagrangeNode;
import edu.mit.spacenet.io.gson.scenario.Location;
import edu.mit.spacenet.io.gson.scenario.MoveElements;
import edu.mit.spacenet.io.gson.scenario.Node;
import edu.mit.spacenet.io.gson.scenario.OrbitalNode;
import edu.mit.spacenet.io.gson.scenario.PeriodDurationTypeAdpater;
import edu.mit.spacenet.io.gson.scenario.PropulsiveVehicle;
import edu.mit.spacenet.io.gson.scenario.RatedDemandModel;
import edu.mit.spacenet.io.gson.scenario.ReconfigureElement;
import edu.mit.spacenet.io.gson.scenario.ReconfigureElements;
import edu.mit.spacenet.io.gson.scenario.RemoveElements;
import edu.mit.spacenet.io.gson.scenario.ResourceContainer;
import edu.mit.spacenet.io.gson.scenario.ResourceTank;
import edu.mit.spacenet.io.gson.scenario.SpaceEdge;
import edu.mit.spacenet.io.gson.scenario.SpaceTransport;
import edu.mit.spacenet.io.gson.scenario.SparingByMassDemandModel;
import edu.mit.spacenet.io.gson.scenario.SurfaceEdge;
import edu.mit.spacenet.io.gson.scenario.SurfaceNode;
import edu.mit.spacenet.io.gson.scenario.SurfaceTransport;
import edu.mit.spacenet.io.gson.scenario.SurfaceVehicle;
import edu.mit.spacenet.io.gson.scenario.TransferResources;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.DemandSimulator;
import edu.mit.spacenet.simulator.event.EventType;

/**
 * This class is used to launch the SpaceNet application.
 * 
 * @author Paul Grogan
 */
public class SpaceNet {
	public static enum HeadlessMode {
		DEMANDS_RAW("demands-raw"),
		DEMANDS_AGGREGATED("demands-agg");
		
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
	
	private static void runDemandSimulator(String scenarioFilePath, String outputFilePath, boolean isOverwriteConfirmed, boolean isRawDemands) {
		Scenario scenario = null;
		if(scenarioFilePath.endsWith("xml")) {
			try {
				scenario = XStreamEngine.openScenario(scenarioFilePath);
				scenario.setFilePath(scenarioFilePath);
				
				// FIXME temporary code
				BufferedWriter out = new BufferedWriter(new FileWriter(scenarioFilePath.replace("xml", "json")));
				RuntimeTypeAdapterFactory<Location> locationAdapterFactory = RuntimeTypeAdapterFactory
						.of(Location.class, "type")
						.registerSubtype(SurfaceNode.class, Node.TYPE_MAP.inverse().get(NodeType.SURFACE))
						.registerSubtype(OrbitalNode.class, Node.TYPE_MAP.inverse().get(NodeType.ORBITAL))
						.registerSubtype(LagrangeNode.class, Node.TYPE_MAP.inverse().get(NodeType.LAGRANGE))
						.registerSubtype(SurfaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SURFACE))
						.registerSubtype(SpaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SPACE))
						.registerSubtype(FlightEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.FLIGHT));
				RuntimeTypeAdapterFactory<Node> nodeAdapterFactory = RuntimeTypeAdapterFactory
						.of(Node.class, "type")
						.registerSubtype(SurfaceNode.class, Node.TYPE_MAP.inverse().get(NodeType.SURFACE))
						.registerSubtype(OrbitalNode.class, Node.TYPE_MAP.inverse().get(NodeType.ORBITAL))
						.registerSubtype(LagrangeNode.class, Node.TYPE_MAP.inverse().get(NodeType.LAGRANGE));
				RuntimeTypeAdapterFactory<Edge> edgeAdapterFactory = RuntimeTypeAdapterFactory
						.of(Edge.class, "type")
						.registerSubtype(SurfaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SURFACE))
						.registerSubtype(SpaceEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.SPACE))
						.registerSubtype(FlightEdge.class, Edge.TYPE_MAP.inverse().get(EdgeType.FLIGHT));
				RuntimeTypeAdapterFactory<Event> eventAdapterFactory = RuntimeTypeAdapterFactory
						.of(Event.class, "type")
						.registerSubtype(CreateElements.class, Event.TYPE_MAP.inverse().get(EventType.CREATE))
						.registerSubtype(MoveElements.class, Event.TYPE_MAP.inverse().get(EventType.MOVE))
						.registerSubtype(RemoveElements.class, Event.TYPE_MAP.inverse().get(EventType.REMOVE))
						.registerSubtype(AddResources.class, Event.TYPE_MAP.inverse().get(EventType.ADD))
						.registerSubtype(ConsumeResources.class, Event.TYPE_MAP.inverse().get(EventType.DEMAND))
						.registerSubtype(TransferResources.class, Event.TYPE_MAP.inverse().get(EventType.TRANSFER))
						.registerSubtype(BurnEvent.class, Event.TYPE_MAP.inverse().get(EventType.BURN))
						.registerSubtype(ReconfigureElement.class, Event.TYPE_MAP.inverse().get(EventType.RECONFIGURE))
						.registerSubtype(ReconfigureElements.class, Event.TYPE_MAP.inverse().get(EventType.RECONFIGURE_GROUP))
						.registerSubtype(EvaEvent.class, Event.TYPE_MAP.inverse().get(EventType.EVA))
						.registerSubtype(Exploration.class, Event.TYPE_MAP.inverse().get(EventType.EXPLORATION))
						.registerSubtype(SpaceTransport.class, Event.TYPE_MAP.inverse().get(EventType.SPACE_TRANSPORT))
						.registerSubtype(SurfaceTransport.class, Event.TYPE_MAP.inverse().get(EventType.SURFACE_TRANSPORT))
						.registerSubtype(FlightTransport.class, Event.TYPE_MAP.inverse().get(EventType.FLIGHT_TRANSPORT));
				RuntimeTypeAdapterFactory<DemandModel> demandModelAdapterFactory = RuntimeTypeAdapterFactory
						.of(DemandModel.class, "type")
						.registerSubtype(RatedDemandModel.class, DemandModel.TYPE_MAP.inverse().get(DemandModelType.RATED))
						.registerSubtype(ImpulseDemandModel.class, DemandModel.TYPE_MAP.inverse().get(DemandModelType.TIMED_IMPULSE))
						.registerSubtype(SparingByMassDemandModel.class, DemandModel.TYPE_MAP.inverse().get(DemandModelType.SPARING_BY_MASS))
						.registerSubtype(ConsumablesDemandModel.class, DemandModel.TYPE_MAP.inverse().get(DemandModelType.CREW_CONSUMABLES));
				RuntimeTypeAdapterFactory<Element> elementAdapterFactory = RuntimeTypeAdapterFactory
						.of(Element.class, "type")
						.registerSubtype(Element.class, Element.TYPE_MAP.inverse().get(ElementType.ELEMENT))
						.registerSubtype(CrewMember.class, Element.TYPE_MAP.inverse().get(ElementType.CREW_MEMBER))
						.registerSubtype(ResourceContainer.class, Element.TYPE_MAP.inverse().get(ElementType.RESOURCE_CONTAINER))
						.registerSubtype(ResourceTank.class, Element.TYPE_MAP.inverse().get(ElementType.RESOURCE_TANK))
						.registerSubtype(Carrier.class, Element.TYPE_MAP.inverse().get(ElementType.CARRIER))
						.registerSubtype(PropulsiveVehicle.class, Element.TYPE_MAP.inverse().get(ElementType.PROPULSIVE_VEHICLE))
						.registerSubtype(SurfaceVehicle.class, Element.TYPE_MAP.inverse().get(ElementType.SURFACE_VEHICLE));
				
				Gson gson = new GsonBuilder()
						.registerTypeAdapter(Date.class, new UtcDateTypeAdapter())
						.registerTypeAdapter(PeriodDuration.class, new PeriodDurationTypeAdpater())
						.registerTypeAdapterFactory(locationAdapterFactory)
						.registerTypeAdapterFactory(nodeAdapterFactory)
						.registerTypeAdapterFactory(edgeAdapterFactory)
						.registerTypeAdapterFactory(eventAdapterFactory)
						.registerTypeAdapterFactory(demandModelAdapterFactory)
						.registerTypeAdapterFactory(elementAdapterFactory)
						.setPrettyPrinting()
						.create();
				
				
				gson.toJson(
					edu.mit.spacenet.io.gson.scenario.Scenario.createFrom(scenario),
					out
				);
				out.close();

				BufferedReader in = new BufferedReader(new FileReader(scenarioFilePath.replace("xml", "json")));
				scenario = gson.fromJson(in, edu.mit.spacenet.io.gson.scenario.Scenario.class).toSpaceNet();
				in.close();
				
				scenario.setFilePath(scenarioFilePath.replace(".xml", "_2.xml"));
				XStreamEngine.saveScenario(scenario);
				// FIXME
			} catch(IOException ex) {
				System.err.println("Failed to read scenario file: " + ex.getMessage());
				System.exit(1);
			}
		} else {
			try {
				BufferedReader in = new BufferedReader(new FileReader(scenarioFilePath));
				scenario = new Gson().fromJson(in, edu.mit.spacenet.io.gson.scenario.Scenario.class).toSpaceNet();
				in.close();
			} catch(IOException ex) {
				System.err.println("Failed to read scenario file: " + ex.getMessage());
				System.exit(1);
			}
		}
		
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