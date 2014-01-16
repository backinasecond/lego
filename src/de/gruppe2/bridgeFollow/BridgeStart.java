package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.util.CalibrateSonic;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * Drive slightly left until robot is near the abyss. Then align it
 * parallel to the bridge
 */
public class BridgeStart implements Behavior {
	
	static DifferentialPilot pilot = Settings.PILOT;
	static UltrasonicSensor sonic = Settings.SONIC_SENSOR;
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.BRIDGE_STATE == BridgeState.ON_BRIDGE;
	}

	@Override
	public void action() {
		suppressed = false;
		
		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.80);
        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
		
		while (!suppressed) {
			if(sonic.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD)
			{
				// Robot is near ground, align it
				Settings.PILOT.rotate(-20, true);
				while(sonic.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
					Thread.yield();
				}
				Settings.BRIDGE_STATE = BridgeState.FOLLOW_LINE;
				break;
			} else {
				// Robot is too far away from abyss, drive slightly to the left
				pilot.steer(5, 10, true);
			}

			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;		
	}

}
