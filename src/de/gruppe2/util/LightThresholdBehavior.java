package de.gruppe2.util;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class LightThresholdBehavior implements Behavior{

	private int thresholdLightValue;

	private boolean suppressed = false;

	public LightThresholdBehavior(int lightValue) {
		this.thresholdLightValue = lightValue;
	}

	@Override
	public boolean takeControl() {
		if (!suppressed) {

			int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

			if (currentLightValue < thresholdLightValue) {
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
			switch (thresholdLightValue) {
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
