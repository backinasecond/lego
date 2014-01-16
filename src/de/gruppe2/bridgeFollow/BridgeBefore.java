package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.util.CalibrateSonic;
import lejos.robotics.subsumption.Behavior;

public class BridgeBefore implements Behavior {
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.BRIDGE_STATE == BridgeState.START;
	}

	@Override
	public void action() {
		suppressed = false;
		
		CalibrateSonic.calibrateVertically();
		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 1.00);
        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
        
		while(Settings.LIGHT_SENSOR.getNormalizedLightValue() < Settings.LIGHT_BRIDGE_DEFAULT && !suppressed)
		{
			Settings.PILOT.forward();
			Thread.yield();
		}
		
		if(Settings.LIGHT_SENSOR.getNormalizedLightValue() > Settings.LIGHT_BRIDGE_DEFAULT)
		{
			// We are on the bridge now
			Settings.BRIDGE_STATE = BridgeState.ON_BRIDGE;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
