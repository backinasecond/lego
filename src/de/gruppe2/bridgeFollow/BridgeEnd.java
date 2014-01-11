package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.robotics.subsumption.Behavior;

public class BridgeEnd implements Behavior{

	@Override
	public boolean takeControl() {
		if(Settings.BRIDGE_STATE == BridgeState.START) return false;
		
		int lightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();
		if(lightValue < Settings.LIGHT_BLACK_DEFAULT)
		{
			Settings.BRIDGE_STATE = BridgeState.END;

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
