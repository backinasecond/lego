package de.gruppe2.raceTrack;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class RaceTrackEnd implements Behavior {

	private boolean suppressed = false;
	private int curDisIdx = 0;
	private boolean findLine = false;

	@Override
	public boolean takeControl() {
		return Settings.RACE_TRACK_END;
	}

	@Override
	public void action() {
		suppressed = false;

		if (!findLine) {
			adjust();
			int dis = Settings.SONIC_SENSOR.getDistance();
			// best distance = 38
			System.out.println("dis " + dis);
			Settings.PILOT.travel(400, false);
			if (dis < 25) {
				Settings.PILOT.steer(-30, -50, true);
			} else if (dis < 38) {
				Settings.PILOT.steer(-15, -40, true);
			} else if (dis == 38) {
				Settings.PILOT.travel(30);
			} else if (dis > 48) {
				Settings.PILOT.steer(10, 50, true);
			} else if (dis > 38) {
				Settings.PILOT.steer(5, 40, true);
			}
			
			while(Settings.PILOT.isMoving()) {
				if (Math.abs(Settings.LIGHT_SENSOR.getNormalizedLightValue() - Settings.LIGHT_LINE_DEFAULT) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
					Settings.ARBITRATOR_MANAGER.changeState(RobotState.LINE_FOLLOWER);
					break;
				}
			}
			
			findLine = true;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	private void adjust() {
		curDisIdx = 0;

		int[] lastDistances = new int[30];
		for (int j = 0; j < lastDistances.length; j++) {
			lastDistances[j] = 999;
		}

		int i = 0;
		Settings.PILOT.setRotateSpeed(Settings.PILOT.getRotateMaxSpeed() * 0.5);
		Settings.PILOT.rotate(90, true);
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (i % 1300 == 0 && curDisIdx < lastDistances.length / 2) {
				lastDistances[curDisIdx] = Settings.SONIC_SENSOR.getDistance();
				curDisIdx++;
			}
			i++;
		}
		Settings.PILOT.rotate(-90, false);
		Settings.PILOT.rotate(-90, true);
		i = 0;
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (i % 1300 == 0 && curDisIdx < lastDistances.length) {
				lastDistances[curDisIdx] = Settings.SONIC_SENSOR.getDistance();
				curDisIdx++;
			}
			i++;
		}
		java.util.Arrays.sort(lastDistances);
		Settings.PILOT.rotate(180, true);
		while (!suppressed && Settings.PILOT.isMoving()) {
			if (Math.abs(lastDistances[0] + 2 - Settings.SONIC_SENSOR.getDistance()) < 2) {
				Settings.PILOT.stop();
				break;
			}
		}
	}

}
