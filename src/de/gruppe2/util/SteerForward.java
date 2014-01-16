package de.gruppe2.util;

import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import lejos.robotics.subsumption.Behavior;

/**
 * Steer forward until line found.
 *
 */
public class SteerForward implements Behavior {
	private boolean suppressed = false;
	private int travelSpeed;
	private int turnRate;
	private boolean lineFound = false;

	/**
	 * Constructs a new SteerForward behavior.
	 * 
	 * @param travelSpeed Will define the speed of the movement.
	 * @param turnRate Will define the turn rate
	 */
	public SteerForward(int travelSpeed, int turnRate) {
		this.travelSpeed = travelSpeed;
		this.turnRate = turnRate;
	}

	/**
	 * Takes always Control. returns true
	 */
	public boolean takeControl() {
		return !lineFound;
	}

	/**
	 * Steers forward as long as this Behavior is active
	 */
	public void action() {
		suppressed = false;
		lineFound = false;

		Settings.PILOT.setTravelSpeed(this.travelSpeed);
		Settings.PILOT.steer(turnRate);

		while(!suppressed && Settings.PILOT.isMoving()) {
			if (Math.abs(Settings.LIGHT_SENSOR.getNormalizedLightValue() - Settings.LIGHT_LINE_DEFAULT) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
				lineFound = true;
				Settings.ARBITRATOR_MANAGER.changeState(RobotState.LINE_FOLLOWER);
				break;
			}
		}
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	public void suppress() {
		suppressed = true;
	}
}
