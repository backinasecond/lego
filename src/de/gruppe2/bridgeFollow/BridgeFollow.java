package de.gruppe2.bridgeFollow;

import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import de.gruppe2.Settings;

public class BridgeFollow implements Behavior {
	
	static DifferentialPilot pilot = Settings.PILOT;
	static UltrasonicSensor sonic = Settings.SONIC_SENSOR;

	private boolean suppressed = false;

	/**
	 * Light threshold.
	 */
	static final int blackWhiteThreshold = 450;

	/**
	 * Thread sleep time.
	 */
	static final int sleep = 10;

	/**
	 * Turn rate.
	 */
	private static int tr = 90;

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {

		suppressed = false;
		
		float rotatedAngle = 0;
		float movementIncrement = -10;
		boolean rotateLeft = true;
		boolean rotateRight = true;
		boolean startAngleCount = false;

		while (!suppressed) {
//			if (light.getNormalizedLightValue() > blackWhiteThreshold) {
//				// On white, turn right
//				
//				pilot.steer(-tr, -10, true);
//				rotateLeft = true;
//				rotateRight = true;
//				startAngleCount = false;
//			} else if(rotateLeft){
//				// On black, turn left
//				if(!startAngleCount) {
//					pilot.travel(50, false);
//					startAngleCount = true;
//					pilot.rotate(170, true);
//					rotatedAngle = pilot.getAngleIncrement();
//				}
//						
//				if (getDifferenceAngle(rotatedAngle) > 160) {
//					pilot.rotate(-160);
//					startAngleCount = false;
//					rotateLeft = false;
//				}
//			} else if(rotateRight) {
//				if(!startAngleCount) {
//					startAngleCount = true;
//					pilot.rotate(-170, true);
//					rotatedAngle = pilot.getAngleIncrement();
//				}
//				// Nothing found left for 90 degrees and still on black, turn right
//				
//				if (getDifferenceAngle(rotatedAngle) > 160) {
//					pilot.rotate(160);
//					rotateRight = false;
//				}
//			} else {
//				// Nothing found left or right. Drive straight on.
//				
//				if(movementIncrement < 0) {
//					movementIncrement = pilot.getMovementIncrement();
//				}
//				pilot.forward();
//				if(pilot.getMovementIncrement() - movementIncrement > 100) {
//					// Line has ended. Exit.
//					System.exit(0);
//				}
//			}
			Delay.msDelay(sleep);
		}
		pilot.stop();
	}

	@Override
	public void suppress() {
		suppressed = true;

	}
}
