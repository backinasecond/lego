package de.gruppe2.util;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class LightDetectionBehaviour implements Behavior {

	private int wantedLightValue;

	private boolean suppressed = false;

	public LightDetectionBehaviour(int lightValue) {
		this.wantedLightValue = lightValue;
	}

	@Override
	public boolean takeControl() {
		if (!suppressed) {

			int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

			if (Math.abs(currentLightValue - wantedLightValue) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
				suppressed = true;
				return true;
			} 
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void action() {
		suppressed = false;

		if (!suppressed) {
			System.out.println("Light action");
			switch (wantedLightValue) {
			case Settings.LIGHT_LINE_DEFAULT:
				Settings.ARBITRATOR_MANAGER.changeState(RobotState.BARCODE);
			}
		}
	}

	@Override
	public void suppress() {
		// Actually there is nothing to suppress, but for completeness
		suppressed = true;
	}
}