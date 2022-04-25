package edu.mit.spacenet.io.gson.scenario;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.threeten.extra.PeriodDuration;

public class Burn {
	public PeriodDuration time;
	public double delta_v;

	public static Burn createFrom(edu.mit.spacenet.domain.network.edge.Burn burn) {
		Burn b = new Burn();
		b.time = PeriodDuration.of(
				Period.ofDays((int) burn.getTime()), 
				Duration.ofSeconds((long) (burn.getTime() - (int) burn.getTime())*24*60*60)
			);
		b.delta_v = burn.getDeltaV();
		return b;
	}
	
	public static List<Burn> createFrom(Collection<edu.mit.spacenet.domain.network.edge.Burn> burns) {
		List<Burn> bs = new ArrayList<Burn>();
		for(edu.mit.spacenet.domain.network.edge.Burn burn : burns) {
			bs.add(Burn.createFrom(burn));
		}
		return bs;
	}
	
	public edu.mit.spacenet.domain.network.edge.Burn toSpaceNet() {
		edu.mit.spacenet.domain.network.edge.Burn b = new edu.mit.spacenet.domain.network.edge.Burn();
		b.setTime(time.getPeriod().getDays() + time.getDuration().getSeconds() / (24*60*60d));
		b.setDeltaV(delta_v);
		return b;
	}
	
	public static List<edu.mit.spacenet.domain.network.edge.Burn> toSpaceNet(Collection<Burn> burns) {
		List<edu.mit.spacenet.domain.network.edge.Burn> bs = new ArrayList<edu.mit.spacenet.domain.network.edge.Burn>();
		for(Burn b : burns) {
			bs.add(b.toSpaceNet());
		}
		return bs;
	}
}
