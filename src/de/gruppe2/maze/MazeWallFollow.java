package de.gruppe2.maze;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class MazeWallFollow implements Behavior {

	private boolean suppressed = false;

	private UltrasonicSensor sonic;
	private DifferentialPilot pilot;

	private int distanceToWall;

	/**
	 * Constructs a new FollowWall Behavior.
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
	 * far away.
	 * Note for pilot.steer: negative values (both) -> right curve!
	 */
	@Override
	public void action() {
		suppressed = false;
		Settings.readState = true;
		int lastDistance;
		while (!suppressed && !Settings.TOUCH_L.isPressed()) {
			System.out.println(sonic.getDistance());
			lastDistance = sonic.getDistance();

			if (lastDistance < 7) {
				pilot.steer(-20, -20, true);
			} else if (lastDistance <= distanceToWall) {
				pilot.steer(-20, -20, true);
			} else if (lastDistance > distanceToWall && lastDistance < 40) {
				pilot.steer(8, 5, true);
			} else if (lastDistance >= 40) { 
				pilot.stop();
				pilot.travel(100, false);
	            pilot.rotate(50, true);
	            while( pilot.isMoving() && !suppressed );
			} else {
				pilot.steer(-8, -8, true);
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

}
