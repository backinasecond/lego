package de.gruppe2.race;

import de.gruppe2.Settings;
import lejos.robotics.subsumption.Behavior;

public class RaceEnd implements Behavior {

	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		while (!suppressed ) {
			Thread.yield();
		}
		
		// Stop Motor
		Settings.PILOT.stop();
		
		Settings.isRunning = false;
	}

	@Override
	public void suppress() {
		suppressed = true;

	}

}
