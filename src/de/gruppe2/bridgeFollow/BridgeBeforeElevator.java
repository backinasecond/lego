package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.util.CalibrateSonic;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class BridgeBeforeElevator implements Behavior {
	
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return Settings.BRIDGE_STATE == BridgeState.BEFORE_ELEVATOR;
	}

	@Override
	public void action() {
		suppressed = false;
		// Position robot on center before elevator
		/*Settings.PILOT.travel(50);
		Settings.PILOT.rotate(-70);
		Settings.PILOT.travel(130);
		Settings.PILOT.rotate(80);
		*/

		
		alignRobotBeforeElevator();

		int cntNotBlue;
		// Wait for blue color to move
		boolean isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
		if(isBlue) {
			System.out.println("Wait for blue 1");
			cntNotBlue = 0;
			while(cntNotBlue < 10) {
				isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
				if(isBlue) cntNotBlue = 0;
				else cntNotBlue++;
				Delay.msDelay(100);
			}
		}
		System.out.println("Wait for blue 2");
		int cntBlue = 0;
		Delay.msDelay(3000);
		while(cntBlue < 10) {
			isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
			if(isBlue) cntBlue++;
			else cntBlue = 0;
			Delay.msDelay(100);
		}
		Delay.msDelay(2000);
		
		System.out.println("go");
		long startTime = System.currentTimeMillis();
		Settings.PILOT.forward();
		Settings.PILOT.steer(-11);
		while(!Settings.TOUCH_L.isPressed() && System.currentTimeMillis() - startTime < 5000) {
			Thread.yield();
		}
		Settings.PILOT.stop();
		Settings.PILOT.travel(-60);
		
		/*startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < 11000) {
			Thread.yield();
		}
		*/
		while(System.currentTimeMillis() - startTime < 10000) {
			Thread.yield();
		}
		
		startTime = System.currentTimeMillis();
		Settings.PILOT.rotate(30);
		while(System.currentTimeMillis() - startTime < 3000) {
			Thread.yield();
		}
		
		Settings.PILOT.travel(340);
		Settings.BRIDGE_STATE = BridgeState.END;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

	private void alignRobotBeforeElevator() {
		
		/*Settings.PILOT.travel(160);
		
		Settings.PILOT.rotate(-80, true);
		while(Settings.SONIC_SENSOR.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
			Thread.yield();
		}
		Settings.PILOT.stop();
		
		// Check if robot is parallel to the abyss or not
		Settings.PILOT.travel(-30);
		if(Settings.SONIC_SENSOR.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
			// Robot is not parallel to the abyss
			Settings.PILOT.rotate(-80);
		} else {
			Settings.PILOT.rotate(-90);
		}
		
		Settings.PILOT.travel(90);
		Settings.PILOT.rotate(90);
		
		*/
		Settings.PILOT.travel(30);
		
		if(Settings.SONIC_SENSOR.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
			// Robot is not parallel to the abyss
			Settings.PILOT.rotate(-10);
			System.out.println("near ground: " + Settings.SONIC_SENSOR.getDistance());
		} else {
			System.out.println("far away: " + Settings.SONIC_SENSOR.getDistance());
		}
	}
	
	private void adjust() {
		int curDisIdx = 0;

		int[] lastDistances = new int[30];
		for (int j = 0; j < lastDistances.length; j++) {
			lastDistances[j] = 999;
		}

		int i = 0;
		Settings.PILOT.setRotateSpeed(Settings.PILOT.getRotateMaxSpeed() * 0.5);
		Settings.PILOT.rotate(90, true);
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (i % 1300 == 0 && curDisIdx < lastDistances.length / 2) {
				lastDistances[curDisIdx] = Settings.SONIC_SENSOR.getDistance();
				curDisIdx++;
			}
			i++;
		}
		Settings.PILOT.rotate(-90, false);
		Settings.PILOT.rotate(-90, true);
		i = 0;
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (i % 1300 == 0 && curDisIdx < lastDistances.length) {
				lastDistances[curDisIdx] = Settings.SONIC_SENSOR.getDistance();
				curDisIdx++;
			}
			i++;
		}
		java.util.Arrays.sort(lastDistances);
		Settings.PILOT.rotate(180, true);
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (Math.abs(lastDistances[0] + 2 - Settings.SONIC_SENSOR.getDistance()) < 2) {
				Settings.PILOT.stop();
				break;
			}
		}
	}
}
