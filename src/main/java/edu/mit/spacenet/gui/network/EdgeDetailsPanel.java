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
package edu.mit.spacenet.gui.network;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.edge.FlightEdge;
import edu.mit.spacenet.domain.network.edge.SpaceEdge;
import edu.mit.spacenet.domain.network.edge.SurfaceEdge;
import edu.mit.spacenet.gui.component.UnitsWrapper;

/**
 * A custom JPanel that displays the details (both generic and class-specific)
 * for an edge.
 * 
 * @author Paul Grogan
 */
public class EdgeDetailsPanel extends JPanel {
	private static final long serialVersionUID = 5485329322200614498L;
	private static String NO_EDGE = "No Edge Panel";
	private static String SURFACE_EDGE = "Surface Edge Panel";
	private static String SPACE_EDGE = "Space Edge Panel";
	private static String FLIGHT_EDGE = "Flight Edge Panel";

	private JLabel titleLabel, nodesLabel, descriptionLabel, distanceLabel, 
		spaceDurationLabel, flightDurationLabel, maxCrewLabel, maxCargoLabel;
	private JPanel edgeDetailsPanel, surfaceEdgeDetailsPanel, 
		spaceEdgeDetailsPanel, flightEdgeDetailsPanel;
	
	/**
	 * Instantiates a new edge details panel.
	 */
	public EdgeDetailsPanel() {
		setBorder(BorderFactory.createTitledBorder("Edge Details"));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		titleLabel = new JLabel();
		add(titleLabel, c);
		c.gridy++;
		nodesLabel = new JLabel();
		add(nodesLabel, c);
		c.gridy++;
		descriptionLabel = new JLabel();
		add(descriptionLabel, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		edgeDetailsPanel = new JPanel();
		edgeDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		edgeDetailsPanel.setLayout(new CardLayout());
		edgeDetailsPanel.add(new JPanel(), NO_EDGE);
		surfaceEdgeDetailsPanel = new JPanel();
		surfaceEdgeDetailsPanel.setLayout(new GridBagLayout());
		{
			GridBagConstraints g = new GridBagConstraints();
			g.gridx = 0;
			g.gridy = 0;
			g.fill = GridBagConstraints.NONE;
			g.anchor = GridBagConstraints.LINE_END;
			surfaceEdgeDetailsPanel.add(new JLabel("Distance: "), g);
			g.gridy = 0;
			g.gridx++;
			g.weightx = 1;
			g.anchor = GridBagConstraints.LINE_START;
			distanceLabel = new JLabel();
			surfaceEdgeDetailsPanel.add(new UnitsWrapper(distanceLabel, "km"), g);
			g.gridy++;
			g.weighty = 1;
			surfaceEdgeDetailsPanel.add(new JPanel(), g);
		}
		edgeDetailsPanel.add(surfaceEdgeDetailsPanel, SURFACE_EDGE);
		spaceEdgeDetailsPanel = new JPanel();
		spaceEdgeDetailsPanel.setLayout(new GridBagLayout());
		{
			GridBagConstraints g = new GridBagConstraints();
			g.gridx = 0;
			g.gridy = 0;
			g.fill = GridBagConstraints.NONE;
			g.anchor = GridBagConstraints.LINE_END;
			spaceEdgeDetailsPanel.add(new JLabel("Duration: "), g);
			g.gridy++;
			//TODO add burns label
			g.gridy = 0;
			g.gridx++;
			g.weightx = 1;
			g.anchor = GridBagConstraints.LINE_START;
			spaceDurationLabel = new JLabel();
			spaceEdgeDetailsPanel.add(new UnitsWrapper(spaceDurationLabel, "days"), g);
			g.gridy++;
			//TODO: add list of burns label
			g.gridy++;
			g.weighty = 1;
			spaceEdgeDetailsPanel.add(new JPanel(), g);
		}
		edgeDetailsPanel.add(spaceEdgeDetailsPanel, SPACE_EDGE);
		flightEdgeDetailsPanel = new JPanel();
		flightEdgeDetailsPanel.setLayout(new GridBagLayout());
		{
			GridBagConstraints g = new GridBagConstraints();
			g.gridx = 0;
			g.gridy = 0;
			g.fill = GridBagConstraints.NONE;
			g.anchor = GridBagConstraints.LINE_END;
			flightEdgeDetailsPanel.add(new JLabel("Duration: "), g);
			g.gridy++;
			flightEdgeDetailsPanel.add(new JLabel("Max Crew: "), g);
			g.gridy++;
			flightEdgeDetailsPanel.add(new JLabel("Max Cargo: "), g);
			g.gridy = 0;
			g.gridx++;
			g.weightx = 1;
			g.anchor = GridBagConstraints.LINE_START;
			flightDurationLabel = new JLabel();
			flightEdgeDetailsPanel.add(new UnitsWrapper(flightDurationLabel, "days"), g);
			g.gridy++;
			maxCrewLabel = new JLabel();
			flightEdgeDetailsPanel.add(maxCrewLabel, g);
			g.gridy++;
			maxCargoLabel = new JLabel();
			flightEdgeDetailsPanel.add(new UnitsWrapper(maxCargoLabel, "kg"), g);
			g.gridy++;
			g.weighty = 1;
			flightEdgeDetailsPanel.add(new JPanel(), g);
		}
		edgeDetailsPanel.add(flightEdgeDetailsPanel, FLIGHT_EDGE);
		add(edgeDetailsPanel, c);
	}
	
	/**
	 * Updates the display for a new edge, clears if null is passed.
	 * 
	 * @param edge the new edge
	 */
	public void setEdge(Edge edge) {
		if(edge==null) {
			titleLabel.setText("");
			nodesLabel.setText("");
			descriptionLabel.setText("");
		} else {
			titleLabel.setText(edge.getName() + " (" + edge.getEdgeType() + " Edge)");
			nodesLabel.setText(edge.getOrigin() + " to " + edge.getDestination());
			descriptionLabel.setText(edge.getDescription());
			switch(edge.getEdgeType()) {
			case SURFACE:
				((CardLayout)edgeDetailsPanel.getLayout()).show(edgeDetailsPanel, SURFACE_EDGE);
				distanceLabel.setText("" + ((SurfaceEdge)edge).getDistance());
				break;
			case SPACE:
				((CardLayout)edgeDetailsPanel.getLayout()).show(edgeDetailsPanel, SPACE_EDGE);
				spaceDurationLabel.setText("" + ((SpaceEdge)edge).getDuration());
				//TODO: add burns list
				break;
			case FLIGHT:
				((CardLayout)edgeDetailsPanel.getLayout()).show(edgeDetailsPanel, FLIGHT_EDGE);
				flightDurationLabel.setText("" + ((FlightEdge)edge).getDuration());
				maxCrewLabel.setText("" + ((FlightEdge)edge).getMaxCrewSize());
				maxCargoLabel.setText("" + ((FlightEdge)edge).getMaxCargoMass());
				break;
			default:
				((CardLayout)edgeDetailsPanel.getLayout()).show(edgeDetailsPanel, NO_EDGE);
			}
		}
	}
}
