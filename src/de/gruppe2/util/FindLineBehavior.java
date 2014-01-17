package de.gruppe2.util;

import de.gruppe2.Settings;
import lejos.robotics.subsumption.Behavior;

public class FindLineBehavior implements Behavior{

	private boolean lineFound = false;
	private boolean suppressed = false;
	
	@Override
	public boolean takeControl() {
		return !lineFound;
	}

	@Override
	public void action() {
		suppressed = false;
		
		Settings.PILOT.setTravelSpeed(120);
		
		Settings.PILOT.steer(-15);
		
		while(!suppressed && Settings.PILOT.isMoving()) {
			if (Math.abs(Settings.LIGHT_SENSOR.getNormalizedLightValue() - Settings.LIGHT_LINE_DEFAULT) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
				lineFound = true;
				break;
			}
		}
		
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
	public void reset() {
		lineFound = false;
	}
}