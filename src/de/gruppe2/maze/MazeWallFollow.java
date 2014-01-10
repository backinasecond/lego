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
	private int lastDistanceIndex = 4;
	private int[] lastDistances = new int[lastDistanceIndex + 1];

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
		CalibrateSonic.calibrateHorizontally();
		suppressed = false;

		for (int i = 0; i < lastDistances.length; i++) {
			lastDistances[i] = sonic.getDistance();
		}

		boolean isDrivingToWall = false;
		boolean isScratchingAtWall = false;

		while (!suppressed && !Settings.TOUCH_L.isPressed()) {
			System.out.println("- " + lastDistances[0] + " " + lastDistances[1]
					+ " " + lastDistances[2] + " " + lastDistances[3] + " "
					+ lastDistances[4] + " " + "\n");

			addElement(sonic.getDistance());

			isDrivingToWall = isDrivingToWall();
			isScratchingAtWall = isScratchingAtWall();

			if (isScratchingAtWall) {
				pilot.rotate(-10, false);
				pilot.travel(10, true);
			} else {
				if (lastDistances[lastDistanceIndex] < 6) {
					pilot.steer(-10, -10, true);
				} else if (lastDistances[lastDistanceIndex] <= distanceToWall) {
					pilot.steer(-10, -10, true);
				} else if (lastDistances[lastDistanceIndex] > distanceToWall
						&& lastDistances[lastDistanceIndex] < 40) {
					if (isDrivingToWall) {
						pilot.steer(-20, -20, true);
					} else {
						pilot.steer(10, 10, true);
					}
				} else if (lastDistances[lastDistanceIndex] >= 40) {
					pilot.stop();
					pilot.travel(100, false);
					pilot.rotate(45, true);
					while (pilot.isMoving() && !suppressed);
				}
			}
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

	private boolean isDrivingToWall() {
		int j = 0;
		for (int i = 0; i < lastDistances.length - 1; i++) {
			if (lastDistances[i] > lastDistances[i + 1]) {
				j++;
			} else if (lastDistances[i] < lastDistances[i + 1]) {
				j--;
			}
		}
		return (j > 0);
	}

	private void addElement(int distance) {
		for (int i = 0; i < lastDistances.length - 1; i++) {
			lastDistances[i] = lastDistances[i + 1];
		}
		lastDistances[lastDistances.length - 1] = distance;
	}

	private boolean isScratchingAtWall() {
		boolean scratchingAtWall = false;
		int cnt = 0;
		for (int i = 0; i < lastDistances.length-1; i++) {
			if (lastDistances[i] == 255) {
				cnt++;
			}
		}
		if (cnt > 0 && cnt < lastDistances.length) {
			scratchingAtWall = true;
		} 
		return scratchingAtWall;
	}

}
