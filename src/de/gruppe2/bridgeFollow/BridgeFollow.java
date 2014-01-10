package de.gruppe2.bridgeFollow;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;

public class BridgeFollow implements Behavior {
	
	static DifferentialPilot pilot = Settings.PILOT;
	static UltrasonicSensor sonic = Settings.SONIC_SENSOR;
	
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.bridgeState == BridgeState.FOLLOW_LINE;
	}

	@Override
	public void action() {

		suppressed = false;

		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed());
		
		while (!suppressed) {
			System.out.println(sonic.getDistance());

			if(sonic.getDistance() > Settings.bridgeHeightThreshold)
			{
				// Robot is near ground
				pilot.steer(-50, -4, true);
			} else {
				// Robot is too far away from ground
				pilot.steer(25, 3, true);
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
