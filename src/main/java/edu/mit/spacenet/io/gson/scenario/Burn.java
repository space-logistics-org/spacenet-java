package edu.mit.spacenet.io.gson.scenario;

public class Burn {
	public double time;
	public double deltaV;

	public static Burn createFrom(edu.mit.spacenet.domain.network.edge.Burn burn) {
		Burn b = new Burn();
		b.time = burn.getTime();
		b.deltaV = burn.getDeltaV();
		return b;
	}
	
	public edu.mit.spacenet.domain.network.edge.Burn toSpaceNet() {
		edu.mit.spacenet.domain.network.edge.Burn b = new edu.mit.spacenet.domain.network.edge.Burn();
		b.setTime(time);
		b.setDeltaV(deltaV);
		return b;
	}
}
