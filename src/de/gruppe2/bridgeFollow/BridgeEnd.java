package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.robotics.subsumption.Behavior;

public class BridgeEnd implements Behavior{

	@Override
	public boolean takeControl() {
		if(Settings.bridgeState == BridgeState.START) return false;
		
		int lightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();
		if(lightValue < Settings.LIGHT_BLACK_DEFAULT)
		{
			Settings.bridgeState = BridgeState.END;
			//System.out.println("Value Black: " + lightValue);
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		// TODO
		Settings.PILOT.stop();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
	}

}
