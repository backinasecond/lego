package de.gruppe2.util;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.robotics.subsumption.Behavior;

public class LightDetectionBehaviour implements Behavior{

	private int wantedLightValue;
	
	public LightDetectionBehaviour(int lightValue) {
		this.wantedLightValue = lightValue;
	}
	
	@Override
	public boolean takeControl() {
		int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();
		
		if (Math.abs(currentLightValue - wantedLightValue) < 50) {
			System.out.println("Value light: " + currentLightValue);
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
