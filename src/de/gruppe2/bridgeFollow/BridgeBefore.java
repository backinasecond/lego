package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.robotics.subsumption.Behavior;

public class BridgeBefore implements Behavior {
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.bridgeState == BridgeState.START;
	}

	@Override
	public void action() {
		suppressed = false;
		
		while(Settings.LIGHT_SENSOR.getNormalizedLightValue() < Settings.LIGHT_BRIDGE_DEFAULT && !suppressed)
		{
			Settings.PILOT.forward();
			Thread.yield();
		}
		
		if(Settings.LIGHT_SENSOR.getNormalizedLightValue() > Settings.LIGHT_BRIDGE_DEFAULT)
		{
			// We are on the bridge now
			Settings.bridgeState = BridgeState.ON_BRIDGE;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
