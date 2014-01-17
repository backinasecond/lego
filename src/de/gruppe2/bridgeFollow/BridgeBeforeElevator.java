package de.gruppe2.bridgeFollow;

import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.util.CalibrateSonic;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class BridgeBeforeElevator implements Behavior {
	
	private final static int BLUE_COLOR = 505;
	
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.BRIDGE_STATE == BridgeState.BEFORE_ELEVATOR;
	}

	@Override
	public void action() {
		suppressed = false;
		
		alignRobotBeforeElevator();

		int cntNotBlue;
		// Wait until no blue color occurs, then elevator is down.
		boolean isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > BLUE_COLOR;
		if(isBlue) {
			System.out.println("Wait for blue 1");
			cntNotBlue = 0;
			while(cntNotBlue < 10) {
				isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > BLUE_COLOR;
				if(isBlue) cntNotBlue = 0;
				else cntNotBlue++;
				Delay.msDelay(40);
			}
		}
		
		// Wait again for blue color, then elevator is up.
		System.out.println("Wait for blue 2");
		int cntBlue = 0;
		Delay.msDelay(3000);
		while(cntBlue < 10) {
			isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > BLUE_COLOR;
			if(isBlue) cntBlue++;
			else cntBlue = 0;
			Delay.msDelay(40);
		}
		//Delay.msDelay(2000);
		
		// Elevator is up. Now drive a slight right curve forward until
		// 1. the touch sensor is pressed or
		// 2. we drove more than 60 cm
		System.out.println("go");
		long startTime = System.currentTimeMillis();
		Settings.PILOT.steer(13);
		while(!Settings.TOUCH_L.isPressed() && System.currentTimeMillis() - startTime < 5000) {
			Thread.yield();
		}
		Settings.PILOT.stop();

		Settings.PILOT.travel(-60);
		
		// Rotate slightly to the right
		Settings.PILOT.rotate(-5);
		
		// Now wait until elevator is down
		startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < 11000) {
			Thread.yield();
		}
		
		// Leave elevator
		Settings.PILOT.travel(340);
		Settings.BRIDGE_STATE = BridgeState.END;
		Settings.ARBITRATOR_MANAGER.changeState(RobotState.BARCODE);
	}

	@Override
	public void suppress() {
		suppressed = true;		
	}

	private void alignRobotBeforeElevator() {
		Settings.PILOT.travel(150);
		
		if(Settings.SONIC_SENSOR.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
			// Robot is not parallel to the abyss, rotate it slightly
			Settings.PILOT.rotate(-28);
			System.out.println("near ground: " + Settings.SONIC_SENSOR.getDistance());
		} else {
			Settings.PILOT.rotate(-20);
			System.out.println("far away: " + Settings.SONIC_SENSOR.getDistance());
		}
	}
}
