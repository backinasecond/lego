package de.gruppe2.lineFollow;

import de.gruppe2.Settings;
import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class LineFollow implements Behavior {

	static DifferentialPilot pilot = Settings.PILOT;
	static LightSensor light = Settings.LIGHT;

	private boolean suppressed = false;

	/**
	 * Light threshold.
	 */
	static final int blackWhiteThreshold = 500;

	/**
	 * Thread sleep time.
	 */
	static final int sleep = 10;

	/**
	 * Number of steps before making turn steeper.
	 */
	static final int out = 150;

	/**
	 * Number of steps.
	 */
	private static int i = 0;

	/**
	 * Number of consecutive left turns.
	 */
	private static int l = 0;

	/**
	 * Number of consecutive right turns.
	 */
	private static int r = 0;

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
			System.out.println(light.getNormalizedLightValue());
			if (light.getNormalizedLightValue() > blackWhiteThreshold) {
				// On white, turn right
				System.out.println("right ");
				pilot.steer(-tr, -10, true);
				rotateLeft = true;
				rotateRight = true;
				startAngleCount = false;
			} else if(rotateLeft){
				if(!startAngleCount) {
					startAngleCount = true;
					rotatedAngle = pilot.getAngleIncrement();
				}
				// On black, turn left
				 System.out.println("left ");
				pilot.steer(tr, 10, true);
				
				if (getDifferenceAngle(pilot.getAngleIncrement()) > 90) {
					pilot.rotate(-90);
					rotateLeft = false;
				}
			} else if(rotateRight) {
				if(!startAngleCount) {
					startAngleCount = true;
					rotatedAngle = pilot.getAngleIncrement();
				}
				// Nothing found left for 90 degrees and still on black, turn right
				 System.out.println("right ");
				pilot.steer(-tr, -10, true);
				
				if (getDifferenceAngle(pilot.getAngleIncrement()) > 90) {
					pilot.rotate(90);
					rotateRight = false;
				}
			} else {
				// Nothing found left or right. Drive straight on.
//				if(movementIncrement)
//				pilot.forward();
//				if(pilot.)
			}
			
			
//			// between out and 2 * out
//			if (i > out && i <= 2 * out) {
//				// last out turns were in same direction
//				if (r > out || l > out) {
//					// make turn steeper
//					tr = 120;
//				}
//			} else if (i > 2 * out) { // more than 2 * out
//				if (r > 2 * out || l > 2 * out) {
//
//					// travel back until line found
//					// TODO
//					pilot.rotate(180);
//					pilot.travel(25);
//				}
//				// Set values to defaults.
//				i = 0;
//				l = 0;
//				r = 0;
//				tr = 90;
//			}
			i++;
			Delay.msDelay(sleep);
		}
		pilot.stop();
	}
	
	private int getDifferenceAngle(float currentRotatedAngle) {
//		return (int) pilot.getAngleIncrement() - currentRotatedAngle;
		return 0;
	}

	@Override
	public void suppress() {
		suppressed = true;

	}
}