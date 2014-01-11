package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BridgeStart implements Behavior {
	
	static DifferentialPilot pilot = Settings.PILOT;
	static UltrasonicSensor sonic = Settings.SONIC_SENSOR;
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.bridgeState == BridgeState.ON_BRIDGE;
	}

	@Override
	public void action() {
		suppressed = false;
		
		while (!suppressed) {
			if(sonic.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD)
			{
				// Robot is near ground
				pilot.steer(-160, -30, false);
				Settings.bridgeState = BridgeState.FOLLOW_LINE;
				break;
			} else {
				// Robot is too far away from ground
				pilot.steer(10, 10, true);
			}

			Thread.yield();
		}
		pilot.stop();
		
	}

	@Override
	public void suppress() {
		suppressed = true;		
	}

}
