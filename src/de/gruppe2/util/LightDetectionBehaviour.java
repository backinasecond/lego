package de.gruppe2.util;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class LightDetectionBehaviour implements Behavior {

	private int wantedLightValue;

	public LightDetectionBehaviour(int lightValue) {
		this.wantedLightValue = lightValue;
	}

	@Override
	public boolean takeControl() {
		int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

		if (Math.abs(currentLightValue - wantedLightValue) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
			System.out.println("Value light: " + currentLightValue);
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		Settings.PILOT.stop();
		
		switch (wantedLightValue) {
		case Settings.LIGHT_LINE_DEFAULT:
			Settings.ARBITRATOR_MANAGER.changeState(RobotState.BARCODE);
		}
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
	}

}
