package de.gruppe2.turntable;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class WallHitBehaviour implements Behavior {

	private int sonicTachoCount;

	/**
	 * Constructs a new HitWall Behavior
	 */
	public WallHitBehaviour() {
		sonicTachoCount = Settings.MOTOR_SONIC.getTachoCount();
	}

	/**
	 * This Behavior takes control if the TouchSensor is pressed.
	 */
	@Override
	public boolean takeControl() {
		Settings.MOTOR_SONIC.flt();
		
		System.out.println(Math.abs(sonicTachoCount - Settings.MOTOR_SONIC.getTachoCount()));

		return Math.abs(sonicTachoCount - Settings.MOTOR_SONIC.getTachoCount()) > 1;
	}

	/**
	 * The robot turns right.
	 * 
	 */
	@Override
	public void action() {
		Settings.PILOT.stop();

		Settings.PILOT.rotate(-220);

		Settings.ARBITRATOR_MANAGER.changeState(RobotState.TURNTABLE);
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	@Override
	public void suppress() {
	}

}
