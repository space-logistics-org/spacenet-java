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
package edu.mit.spacenet.simulator.event;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.element.PropulsiveVehicle;
import edu.mit.spacenet.domain.network.edge.Burn;
import edu.mit.spacenet.domain.network.edge.BurnType;
import edu.mit.spacenet.simulator.I_Simulator;
import edu.mit.spacenet.simulator.SimError;
import edu.mit.spacenet.simulator.SimSpatialError;
import edu.mit.spacenet.util.Formulae;

/**
 * Event that represents a propulsive maneuver that may be composed of one or
 * more burns or stages of individual elements.
 * 
 * @author Paul Grogan
 */
public class BurnEvent extends AbstractEvent {
	private SortedSet<I_Element> elements;
	private Burn burn;
	private List<BurnStageItem> burnStageSequence;

	/**
	 * The default constructor initializes the items and burn stage sequence
	 * structures.
	 */
	public BurnEvent() {
		super();
		this.elements = new TreeSet<I_Element>();
		this.burn = new Burn();
		this.burnStageSequence = new ArrayList<BurnStageItem>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#execute(edu.mit.spacenet.simulator.I_Simulator)
	 */
	public void execute(I_Simulator simulator) throws SimError {		
		double deltaVReq = burn.getDeltaV();
		
		int pcounter = Integer.MIN_VALUE;
		
		for(BurnStageItem i : burnStageSequence) {
			if(i.getBurnStage().equals(BurnStageItem.BURN)) {
				PropulsiveVehicle v = (PropulsiveVehicle)i.getElement();
				
				double stackMass = 0;
				for(I_Element element : elements) {
					stackMass += element.getTotalMass();
				}
				if(burn.getBurnType()==BurnType.OMS) {
					System.out.printf("%.3f: %s %.2f %s", getTime(), "Starting " + v.getName() + " Burn (OMS) for target", deltaVReq, "m/s delta-V\n");
					if(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp()) > v.getOmsFuelTank().getAmount()) {
						double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getOmsIsp(), v.getOmsFuelTank().getAmount());
						deltaVReq -= deltaVAchieved;
						v.getOmsFuelTank().setAmount(0);
						/*
						DemandSet demands = new DemandSet();
						Demand demand = new Demand();
						demand.setResource(v.getOmsFuelTank().getResource());
						demand.setAmount(v.getOmsFuelTank().getAmount());
						demands.add(demand);
						DemandEvent d = new DemandEvent();
						d.setTime(getTime());
						d.setPriority(pcounter++);
						d.setParent(this);
						d.setLocation(getLocation());
						d.setElement(v.getOmsFuelTank());
						//d.setElement(v);
						d.setDemands(demands);
						//simulator.schedule(d);
						d.execute(simulator);
						*/
						System.out.printf("%.3f: %s %.2f %s %.2f %s\n", getTime(), v.getName() + " Burn (OMS) achieved", deltaVAchieved, "m/s delta-V,", deltaVReq, "m/s delta-V remaining");
					} else {
						v.getOmsFuelTank().remove(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp()));
						/*
						DemandSet demands = new DemandSet();
						Demand demand = new Demand();
						demand.setResource(v.getOmsFuelTank().getResource());
						demand.setAmount(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getOmsIsp()));
						demands.add(demand);
						DemandEvent d = new DemandEvent();
						d.setTime(getTime());
						d.setPriority(pcounter++);
						d.setParent(this);
						d.setLocation(getLocation());
						d.setElement(v.getOmsFuelTank());
						//d.setElement(v);
						d.setDemands(demands);
						//simulator.schedule(d);
						d.execute(simulator);
						*/
						System.out.printf("%.3f: %s %.2f %s, %.2f %s\n", getTime(), v.getName() + " Burn (OMS) achieved", deltaVReq, "m/s delta-V", v.getOmsFuelTank().getCargoMass(), "kg fuel remaining");
						deltaVReq = 0;
					}
				} else if(burn.getBurnType()==BurnType.RCS) {
					System.out.printf("%.3f: %s %.2f %s", getTime(), "Starting " + v.getName() + " Burn (RCS) for target", deltaVReq, "m/s delta-V\n");
					if(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp()) > v.getRcsFuelTank().getAmount()) {
						double deltaVAchieved = Formulae.getAchievedDeltaV(stackMass, v.getRcsIsp(), v.getRcsFuelTank().getAmount());
						deltaVReq -= deltaVAchieved;
						v.getRcsFuelTank().setAmount(0);
						/*
						DemandSet demands = new DemandSet();
						Demand demand = new Demand();
						demand.setResource(v.getRcsFuelTank().getResource());
						demand.setAmount(v.getRcsFuelTank().getAmount());
						demands.add(demand);
						DemandEvent d = new DemandEvent();
						d.setTime(getTime());
						d.setPriority(pcounter++);
						d.setParent(this);
						d.setLocation(getLocation());
						d.setElement(v.getRcsFuelTank());
						//d.setElement(v);
						d.setDemands(demands);
						//simulator.schedule(d);
						d.execute(simulator);
						*/
						System.out.printf("%.3f: %s %.2f %s %.2f %s\n", getTime(), v.getName() + " Burn (RCS) achieved", deltaVAchieved, "m/s delta-V,", deltaVReq, "m/s delta-V remaining");
					} else {
						v.getRcsFuelTank().remove(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp()));
						/*
						DemandSet demands = new DemandSet();
						Demand demand = new Demand();
						demand.setResource(v.getRcsFuelTank().getResource());
						demand.setAmount(Formulae.getRequiredFuelMass(stackMass, deltaVReq, v.getRcsIsp()));
						demands.add(demand);
						DemandEvent d = new DemandEvent();
						d.setTime(getTime());
						d.setPriority(pcounter++);
						d.setParent(this);
						d.setLocation(getLocation());
						d.setElement(v.getRcsFuelTank());
						//d.setElement(v);
						d.setDemands(demands);
						//simulator.schedule(d);
						d.execute(simulator);
						*/
						System.out.printf("%.3f: %s %.2f %s, %.2f %s\n", getTime(), v.getName() + " Burn (RCS) achieved", deltaVReq, "m/s delta-V", v.getRcsFuelTank().getCargoMass(), "kg fuel remaining");
						deltaVReq = 0;
					}
				}
			} else if(i.getBurnStage().equals(BurnStageItem.STAGE)) {
				RemoveEvent stage = new RemoveEvent();
				stage.setTime(getTime());
				stage.setPriority(pcounter++);
				stage.setParent(this);
				stage.setLocation(getLocation());
				stage.getElements().add(i.getElement());
				//simulator.schedule(d);
				stage.execute(simulator);
				elements.remove(i.getElement());
			}
		}
		if(deltaVReq > 0) {
			DecimalFormat format = new DecimalFormat("0.0");
			throw new SimSpatialError(simulator.getTime(), this, "Insufficient delta-v achieved, - remaining delta-v: " + format.format(deltaVReq) + " m/s.");
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.AbstractEvent#print(int)
	 */
	@Override
	public void print(int tabOrder) {
		String s = "";
		for(BurnStageItem i : burnStageSequence) {
			if(i.getBurnStage().equals(BurnStageItem.BURN)) s+= i.getElement().getName() + " [B], ";
			else if(i.getBurnStage().equals(BurnStageItem.STAGE)) s+= i.getElement().getName() + " [S], ";
		}
		s = s.substring(0, s.length() - 2);
		super.print(tabOrder);
		System.out.printf("%s% .1f %s\n", burn.getBurnType() + " Burn for",burn.getDeltaV(), "delta-V: " + s);
	}
	
	/**
	 * Gets the set of elements to include in the maneuver.
	 * 
	 * @return the set of elements
	 */
	public SortedSet<I_Element> getElements() {
		return elements;
	}
	
	/**
	 * Sets the set of elements to include in the maneuver.
	 * 
	 * @param elements the set of elements
	 */
	public void setElements(SortedSet<I_Element> elements) {
		this.elements = elements;
	}
	
	/**
	 * Gets the burn.
	 * 
	 * @return the burn
	 */
	public Burn getBurn() {
		return burn;
	}
	
	/**
	 * Sets the burn.
	 * 
	 * @param burn the burn
	 */
	public void setBurn(Burn burn) {
		this.burn = burn;
	}
	
	/**
	 * Gets the burn / stage sequence.
	 * 
	 * @return the burn / stage sequence
	 */
	public List<BurnStageItem> getBurnStageSequence() {
		return burnStageSequence;
	}
	
	/**
	 * Sets the burn / stage sequence.
	 * 
	 * @param burnStageSequence the burn / stage sequence
	 */
	public void setBurnStateSequence(List<BurnStageItem> burnStageSequence) {
		this.burnStageSequence = burnStageSequence;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.spacenet.domain.event.I_Event#getEventType()
	 */
	public EventType getEventType() {
		return EventType.BURN;
	}
}
