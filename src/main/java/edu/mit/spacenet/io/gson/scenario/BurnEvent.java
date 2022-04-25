package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class BurnEvent extends Event {
	public List<UUID> elements;
	public UUID burn;
	public List<BurnStageAction> actions;
	
	public static BurnEvent createFrom(edu.mit.spacenet.simulator.event.BurnEvent event, Context context) {
		BurnEvent e = new BurnEvent();
		e.name = event.getName();
		e.mission_time = PeriodDuration.of(
				Period.ofDays((int) event.getTime()), 
				Duration.ofSeconds((long) (event.getTime() - (int) event.getTime())*24*60*60)
			);
		e.priority = event.getPriority();
		e.elements = context.getUUIDs(event.getElements());
		e.burn = context.getUUID(event.getBurn());
		e.actions = BurnStageAction._createFrom(event.getBurnStageSequence(), context);
		return e;
	}
	
	@Override
	public edu.mit.spacenet.simulator.event.BurnEvent toSpaceNet(Context context) {
		edu.mit.spacenet.simulator.event.BurnEvent e = new edu.mit.spacenet.simulator.event.BurnEvent();
		e.setName(name);
		e.setTime(mission_time.getPeriod().getDays() + mission_time.getDuration().getSeconds() / (24*60*60d));
		e.setPriority(priority);
		e.setElements(Element.toSpaceNet(elements, context));
		e.setBurn((edu.mit.spacenet.domain.network.edge.Burn) context.getObject(burn));
		e.setBurnStateSequence(BurnStageAction._toSpaceNet(actions, context));
		return e;
	}

}
