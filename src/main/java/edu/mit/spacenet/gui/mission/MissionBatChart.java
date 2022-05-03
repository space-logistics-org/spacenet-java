/*
 * Copyright 2010 MIT Strategic Engineering Research Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.mit.spacenet.gui.mission;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.gui.component.SNChartPanel;
import edu.mit.spacenet.scenario.Mission;
import edu.mit.spacenet.scenario.Scenario;
import edu.mit.spacenet.simulator.event.BurnEvent;
import edu.mit.spacenet.simulator.event.CreateEvent;
import edu.mit.spacenet.simulator.event.DemandEvent;
import edu.mit.spacenet.simulator.event.EvaEvent;
import edu.mit.spacenet.simulator.event.ExplorationProcess;
import edu.mit.spacenet.simulator.event.FlightTransport;
import edu.mit.spacenet.simulator.event.I_Event;
import edu.mit.spacenet.simulator.event.MoveEvent;
import edu.mit.spacenet.simulator.event.ReconfigureEvent;
import edu.mit.spacenet.simulator.event.ReconfigureGroupEvent;
import edu.mit.spacenet.simulator.event.RemoveEvent;
import edu.mit.spacenet.simulator.event.SpaceTransport;
import edu.mit.spacenet.simulator.event.SurfaceTransport;
import edu.mit.spacenet.simulator.event.TransferEvent;
import edu.mit.spacenet.util.DateFunctions;

/**
 * A bat-style chart that displays mission events.
 * 
 * @author Paul Grogan
 */
public class MissionBatChart extends SNChartPanel {
  private static final long serialVersionUID = -6405124067606236705L;

  private MissionSplitPane missionSplitPane;

  /**
   * Instantiates a new process bat chart within a mission split pane.
   * 
   * @param missionSplitPane the mission split pane
   */
  public MissionBatChart(MissionSplitPane missionSplitPane) {
    this.missionSplitPane = missionSplitPane;
  }

  /**
   * Initializes the component for a new mission.
   */
  public void initialize() {
    updateView();
  }

  /**
   * Gets the scenario.
   * 
   * @return the scenario
   */
  private Scenario getScenario() {
    return missionSplitPane.getMissionsTab().getScenarioPanel().getScenario();
  }

  /**
   * Gets the mission.
   * 
   * @return the mission
   */
  private Mission getMission() {
    return missionSplitPane.getMission();
  }

  /**
   * Updates the view.
   */
  public void updateView() {
    TimeSeriesCollection dataset = new TimeSeriesCollection();

    JFreeChart chart =
        ChartFactory.createTimeSeriesChart(null, "Date", "Node", dataset, false, true, false);

    XYPlot plot = (XYPlot) chart.getPlot();
    SymbolAxis rangeAxis = createRangeAxis();
    plot.setRangeAxis(rangeAxis);
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

    plot.setRangeGridlinesVisible(false);
    plot.setDomainGridlinesVisible(false);

    XYItemRenderer r = chart.getXYPlot().getRenderer();

    if (r instanceof XYLineAndShapeRenderer) {
      XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
      renderer.setBaseShapesVisible(true);
      renderer.setBaseShapesFilled(true);
    }

    for (I_Event event : getMission().getEventList()) {
      if (event.getLocation() instanceof Node) {
        if (event instanceof SpaceTransport && ((SpaceTransport) event).getEdge() != null) {
          TimeSeries s = new TimeSeries(event.getName(), Hour.class);
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime())),
              node2Number(((SpaceTransport) event).getEdge().getOrigin(), rangeAxis));
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime()
                      + ((SpaceTransport) event).getEdge().getDuration())),
              node2Number(((SpaceTransport) event).getEdge().getDestination(), rangeAxis));
          ((TimeSeriesCollection) dataset).addSeries(s);
          r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
          r.setSeriesShape(dataset.getSeriesCount() - 1,
              new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        } else if (event instanceof SurfaceTransport
            && ((SurfaceTransport) event).getEdge() != null) {
          TimeSeries s = new TimeSeries(event.getName(), Hour.class);
          if (((SurfaceTransport) event).isReversed()) {
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number(((SurfaceTransport) event).getEdge().getDestination(), rangeAxis));
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime()
                        + ((SurfaceTransport) event).getDuration())),
                node2Number(((SurfaceTransport) event).getEdge().getOrigin(), rangeAxis));
          } else {
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number(((SurfaceTransport) event).getEdge().getOrigin(), rangeAxis));
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime()
                        + ((SurfaceTransport) event).getDuration())),
                node2Number(((SurfaceTransport) event).getEdge().getDestination(), rangeAxis));
          }
          ((TimeSeriesCollection) dataset).addSeries(s);
          r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GREEN);
          r.setSeriesShape(dataset.getSeriesCount() - 1,
              new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        } else if (event instanceof FlightTransport
            && ((FlightTransport) event).getEdge() != null) {
          TimeSeries s = new TimeSeries(event.getName(), Hour.class);
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime())),
              node2Number(((FlightTransport) event).getEdge().getOrigin(), rangeAxis));
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime()
                      + ((FlightTransport) event).getEdge().getDuration())),
              node2Number(((FlightTransport) event).getEdge().getDestination(), rangeAxis));
          ((TimeSeriesCollection) dataset).addSeries(s);
          r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.YELLOW);
          r.setSeriesShape(dataset.getSeriesCount() - 1,
              new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        } else if (event instanceof ExplorationProcess && event.getLocation() != null) {
          TimeSeries s = new TimeSeries(event.getName(), Hour.class);
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime())),
              node2Number((Node) event.getLocation(), rangeAxis));
          s.addOrUpdate(
              new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                  DateFunctions.getDaysBetween(getScenario().getStartDate(),
                      getMission().getStartDate()) + event.getTime()
                      + ((ExplorationProcess) event).getDuration())),
              node2Number((Node) event.getLocation(), rangeAxis));
          ((TimeSeriesCollection) dataset).addSeries(s);
          r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.CYAN);
          r.setSeriesShape(dataset.getSeriesCount() - 1,
              new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        } else if (event.getLocation() != null) {
          if (event instanceof CreateEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.GREEN);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof MoveEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.ORANGE);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof TransferEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.ORANGE);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof RemoveEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLACK);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof DemandEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.BLUE);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof ReconfigureEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.PINK);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof ReconfigureGroupEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.PINK);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof BurnEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.RED);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          } else if (event instanceof EvaEvent) {
            TimeSeries s = new TimeSeries(event.getName(), Hour.class);
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime())),
                node2Number((Node) event.getLocation(), rangeAxis));
            s.addOrUpdate(
                new Hour(DateFunctions.getDate(getScenario().getStartDate(),
                    DateFunctions.getDaysBetween(getScenario().getStartDate(),
                        getMission().getStartDate()) + event.getTime()
                        + ((EvaEvent) event).getEvaDuration() / 24)),
                node2Number((Node) event.getLocation(), rangeAxis));
            ((TimeSeriesCollection) dataset).addSeries(s);
            r.setSeriesPaint(dataset.getSeriesCount() - 1, Color.CYAN);
            r.setSeriesShape(dataset.getSeriesCount() - 1,
                new Rectangle2D.Double(-4.0, -4.0, 8.0, 8.0));
          }
        }
      }
    }
    setChart(chart);
  }

  /**
   * Creates the range axis (list of nodes on the vertical axis).
   * 
   * @return the symbol axis
   */
  private SymbolAxis createRangeAxis() {
    Vector<String> nodeLabels = new Vector<String>();
    for (Node n : getScenario().getNetwork().getNodes()) {
      nodeLabels.add(n.getName());
    }
    String[] array = {};
    return new SymbolAxis(null, nodeLabels.toArray(array));
  }

  /**
   * Converts a node to its numeric representation for the symbol axis.
   * 
   * @param node the node
   * @param axis the axis
   * 
   * @return the number
   */
  private int node2Number(Node node, SymbolAxis axis) {
    for (int i = 0; i < axis.getSymbols().length; i++) {
      if (axis.getSymbols()[i].equals(node.getName())) {
        return i;
      }
    }
    return -1;
  }
}
