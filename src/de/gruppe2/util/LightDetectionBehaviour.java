package de.gruppe2.util;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class LightDetectionBehaviour implements Behavior{

	private int wantedLightValue;
	private final int LIGHT_THRESHOLD = 50;
	
	public LightDetectionBehaviour(int lightValue) {
		this.wantedLightValue = lightValue;
	}
	
	@Override
	public boolean takeControl() {
		int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();
		
		if (Math.abs(currentLightValue - wantedLightValue) < LIGHT_THRESHOLD) {
			System.out.println("Value light: " + currentLightValue);
			
			switch(wantedLightValue) {
			case Settings.LIGHT_LINE_DEFAULT:
				Settings.ARBITRATOR_MANAGER.changeState(RobotState.BARCODE);
			}
			return true;
		}
		
//		if(lightValue < Settings.LIGHT_BLACK_DEFAULT)
//		{
//			System.out.println("Value Black: " + lightValue);
//			return true;
//		}
		return false;
	}

	@Override
	public void action() {
		
		Settings.PILOT.stop();

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
	}

}
