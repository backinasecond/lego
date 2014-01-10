package de.gruppe2.maze;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.CalibrateSonic;
import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class MazeWallFollow implements Behavior {

	private boolean suppressed = false;

	private UltrasonicSensor sonic;
	private AllWheelPilot pilot;

	private int distanceToWall;
	private int curDisIdx = 0;
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

		for (int i = 0; i < lastDistances.length; i++) {
			lastDistances[i] = sonic.getDistance();
		}

//		boolean isScratchingAtWall = false;

		while (!suppressed && !Settings.TOUCH_L.isPressed()) {

			lastDistances[curDisIdx] = sonic.getDistance();
//			isScratchingAtWall = isScratchingWall();

			if (lastDistances[curDisIdx] == 255) {
				// pilot.rotate(-30, false);
//				pilot.steer(-90, -45, true);
				pilot.rotate(-45, true);
			} else if (lastDistances[curDisIdx] < 7) {
				pilot.steer(-100, -30, true);
			} else if (lastDistances[curDisIdx] <= distanceToWall) {
				pilot.steer(-100, -10, true);
			} else if (lastDistances[curDisIdx] > distanceToWall && lastDistances[curDisIdx] < 40) {
				pilot.steer(30, 10, true);
			} else if (lastDistances[curDisIdx] >= 40) {
				pilot.stop();
				// pilot.travel(100, false);
				// pilot.rotate(60, true);
				pilot.steer(60, 90, true);
				while (pilot.isMoving() && !suppressed)
					;
//				if ()
			}
			curDisIdx = (curDisIdx + 1) % lastDistances.length;
		}
		pilot.stop();
	}

	private boolean isScratchingWall() {
		boolean scratchingAtWall = false;
		int cnt = 0;
		int i = 0;
		int k = (curDisIdx + 1) % lastDistances.length;
		while (i < lastDistances.length) {

			if (lastDistances[k] == 255) {
				scratchingAtWall = true;
				if (cnt == 0) {
					cnt = lastDistances.length - 1 - i;
				} else {
					cnt--;
				}
			}

			k = (k + 1) % lastDistances.length;
			i++;
		}
		return scratchingAtWall && (cnt > 0);
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}
}