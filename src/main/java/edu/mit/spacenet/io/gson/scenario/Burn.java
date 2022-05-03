package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.threeten.extra.PeriodDuration;

public class Burn {
	public UUID id;
	public PeriodDuration time;
	public double delta_v;

	public static Burn createFrom(edu.mit.spacenet.domain.network.edge.Burn burn, Context context) {
		Burn b = new Burn();
		b.id = context.getJsonIdFromJavaObject(burn);
		b.time = PeriodDuration.of(
				Period.ofDays((int) burn.getTime()), 
				Duration.ofSeconds((long) (burn.getTime() - (int) burn.getTime())*24*60*60)
			);
		b.delta_v = burn.getDeltaV();
		return b;
	}
	
	public static List<Burn> createFrom(Collection<edu.mit.spacenet.domain.network.edge.Burn> burns, Context context) {
		List<Burn> bs = new ArrayList<Burn>();
		for(edu.mit.spacenet.domain.network.edge.Burn burn : burns) {
			bs.add(Burn.createFrom(burn, context));
		}
		return bs;
	}
	
	public edu.mit.spacenet.domain.network.edge.Burn toSpaceNet(Context context) {
		edu.mit.spacenet.domain.network.edge.Burn b = new edu.mit.spacenet.domain.network.edge.Burn();
		context.put(b, id, this);
		b.setTid(context.getJavaId(id));
		b.setTime(time.getPeriod().getDays() + time.getDuration().getSeconds() / (24*60*60d));
		b.setDeltaV(delta_v);
		return b;
	}
	
	public static List<edu.mit.spacenet.domain.network.edge.Burn> toSpaceNet(Collection<Burn> burns, Context context) {
		List<edu.mit.spacenet.domain.network.edge.Burn> bs = new ArrayList<edu.mit.spacenet.domain.network.edge.Burn>();
		for(Burn b : burns) {
			bs.add(b.toSpaceNet(context));
		}
		return bs;
	}
}
