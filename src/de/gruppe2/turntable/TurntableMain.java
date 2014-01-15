package de.gruppe2.turntable;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class TurntableMain {
	
	public static void main(String[] args) {
		Behavior t1 = new SymbolFollow();

		Behavior[] turntableArray = { t1 };

		Arbitrator turntableArbitrator = new Arbitrator(turntableArray);
		turntableArbitrator.start();
	}
}
