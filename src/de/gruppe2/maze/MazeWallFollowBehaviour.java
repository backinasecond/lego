package de.gruppe2.maze;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.CalibrateSonic;
import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class MazeWallFollow implements Behavior {

	private boolean suppressed = false;

	private final UltrasonicSensor sonic;
	private final AllWheelPilot pilot;
	private final NXTRegulatedMotor MOTOR_SONIC = Settings.MOTOR_SONIC;

	private int distanceToWall;
	private int curDisIdx = 0;
	private int sonicTachoCount;
	private int[] lastDistances = new int[5];

	/**
	 * Constructs a new MazeWallFollow Behavior.
	 * 
	 * @param distance
	 *            - the distance in which the wall is followed.
	 */
	public MazeWallFollow(int distance) {
		distanceToWall = distance;
		pilot = Settings.PILOT;
		sonic = Settings.SONIC_SENSOR;
		CalibrateSonic.calibrateHorizontally();
		sonicTachoCount = MOTOR_SONIC.getTachoCount();
		MOTOR_SONIC.flt();
	}

	/**
	 * The Behavior takes control if the robot is not in the swamp
	 */
	@Override
	public boolean takeControl() {
		return Settings.atStartOfMaze;
	}

	/**
	 * The robot moves along the Wall and turns to the left if the wall is too
	 * far away. Note for pilot.steer: negative values (both) -> right curve!
	 */
	@Override
	public void action() {
		suppressed = false;
		
		while (!suppressed && !Settings.TOUCH_L.isPressed()) {
			lastDistances[curDisIdx] = sonic.getDistance();

			if (Math.abs(sonicTachoCount - MOTOR_SONIC.getTachoCount()) > 1) {
				System.out.println("SCRATCHING");
				pilot.rotate(-30, true);
				MOTOR_SONIC.rotateTo(0, true);
				sonicTachoCount = MOTOR_SONIC.getTachoCount();
			} else if (lastDistances[curDisIdx] < 7) {
				pilot.steer(-90, -15, true);
			} else if (lastDistances[curDisIdx] <= distanceToWall) {
				pilot.steer(-20, -10, true);
			} else if (lastDistances[curDisIdx] > distanceToWall && lastDistances[curDisIdx] < 35) {
				pilot.steer(20, 15, true);
				if (lastDistances[curDisIdx] < lastDistances[(curDisIdx + 1) % lastDistances.length]) {
					System.out.println("TO WALL " + curDisIdx);
					pilot.steer(-45, -30, true);
				}
			} else if (lastDistances[curDisIdx] >= 35) {
//				pilot.stop();
				System.out.println("LEFT CURVE");
				pilot.steer(70, 90, true);
				while (pilot.isMoving() && !suppressed)
					;
			}
			curDisIdx = (curDisIdx + 1) % lastDistances.length;
			sonicTachoCount = MOTOR_SONIC.getTachoCount();
		}
		pilot.stop();
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}
}