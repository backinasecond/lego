package de.gruppe2.turntable;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class WallHitBehaviour implements Behavior {

	/**
	 * Constructs a new HitWall Behavior
	 */
	public WallHitBehaviour() {
	}

	/**
	 * This Behavior takes control if the TouchSensor is pressed.
	 */
	@Override
	public boolean takeControl() {
		return Settings.TOUCH_L.isPressed();
	}

	/**
	 * The robot turns right.
	 * 
	 */
	@Override
	public void action() {
		Settings.PILOT.stop();

		Settings.ARBITRATOR_MANAGER.changeState(RobotState.TURNTABLE);
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	@Override
	public void suppress() {
	}

}
