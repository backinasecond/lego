package de.gruppe2.turntable;

import de.gruppe2.Settings;
import lejos.robotics.subsumption.Behavior;

public class TurntableRecognizer implements Behavior {

	int count = 0;
	
	@Override
	public boolean takeControl() {
		
		boolean turntableRecognized = Math.abs(Settings.LIGHT_SENSOR.getNormalizedLightValue() - Settings.COLOR_TURNTABLE) < 20;
		
		if (turntableRecognized) {
			count++;
		} else {
			count = 0;
		}
		
		return count > 100;
	}

	@Override
	public void action() {
		Settings.PILOT.stop();
		
		System.out.println("Recognized");
	}

	@Override
	public void suppress() {
		
	}
	

}
