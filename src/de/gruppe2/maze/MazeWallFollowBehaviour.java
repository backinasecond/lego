package de.gruppe2.maze;

import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class MazeWallFollowBehaviour implements Behavior {

	private boolean suppressed = false;

	private final NXTRegulatedMotor MOTOR_SONIC = Settings.MOTOR_SONIC;

	private int curDisIdx = 0;
	private int sonicTachoCount;
	private int[] lastDistances = new int[5];

	/**
	 * Constructs a new MazeWallFollow Behavior.
	 * 
	 * @param distance
	 *            - the distance in which the wall is followed.
	 */
	public MazeWallFollowBehaviour() {
	}

	/**
	 * The Behavior takes control if the robot is not in the swamp
	 */
	@Override
	public boolean takeControl() {
		return Settings.AT_MAZE;
	}

	/**
	 * The robot moves along the Wall and turns to the left if the wall is too
	 * far away. Note for pilot.steer: negative values (both) -> right curve!
	 */
	@Override
	public void action() {
		sonicTachoCount = MOTOR_SONIC.getTachoCount();
		MOTOR_SONIC.flt();

		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.60);
        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
		
		suppressed = false;
		
		while (!suppressed && !Settings.TOUCH_L.isPressed()) {
			lastDistances[curDisIdx] = Settings.SONIC_SENSOR.getDistance();

			if (Math.abs(sonicTachoCount - MOTOR_SONIC.getTachoCount()) > 1) {
				Settings.PILOT.rotate(-30, true);
				MOTOR_SONIC.rotateTo(0, true);
				sonicTachoCount = MOTOR_SONIC.getTachoCount();
			} else if (lastDistances[curDisIdx] < 6) {
				Settings.PILOT.steer(-100, -15, true);
			} else if (lastDistances[curDisIdx] <= Settings.MAZE_WALL_DISTANCE_THRESHOLD) {
				Settings.PILOT.steer(-20, -10, true);
			} else if (lastDistances[curDisIdx] > Settings.MAZE_WALL_DISTANCE_THRESHOLD && lastDistances[curDisIdx] < 35) {
				Settings.PILOT.steer(20, 15, true);
				if (lastDistances[curDisIdx] < lastDistances[(curDisIdx + 1) % lastDistances.length]) {
					Settings.PILOT.steer(-45, -30, true);
				}
			} else if (lastDistances[curDisIdx] >= 35) {
				Settings.PILOT.steer(70, 90, true);
				while (Settings.PILOT.isMoving() && !suppressed)
					;
			}
			curDisIdx = (curDisIdx + 1) % lastDistances.length;
			sonicTachoCount = MOTOR_SONIC.getTachoCount();
		}
		
		Settings.PILOT.stop();
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}
}