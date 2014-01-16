package de.gruppe2.turntable;

import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.lineFollow.LineFollow;

public class LineFollowTurntable extends LineFollow {

	private int colorValues[] = new int[20];
	int currentIndex = 0;

	private int sonicTachoCount;

	public LineFollowTurntable() {
		super();

		for (int i = 0; i < colorValues.length; i++) {
			colorValues[i] = 0;
		}

		sonicTachoCount = Settings.MOTOR_SONIC.getTachoCount();

	}

	public LineFollowTurntable(RobotState nextState) {
		super(nextState);

		for (int i = 0; i < colorValues.length; i++) {
			colorValues[i] = 0;
		}

		sonicTachoCount = Settings.MOTOR_SONIC.getTachoCount();
	}

	@Override
	protected boolean testLineEnd() {

		Settings.MOTOR_SONIC.flt();

		/*
		 * int valueInRange = 0;
		 * for (int i = 0; i < colorValues.length; i++) {
		 * //System.out.println(colorValues[i]);
		 * //System.out.println("diff" + i + " " + Math.abs(Settings.TURNTABLE_COLOR - colorValues[i]));
		 * if (Math.abs(Settings.TURNTABLE_COLOR - colorValues[i]) < 15) {
		 * valueInRange++;
		 * }
		 * }
		 * System.out.println(valueInRange);
		 * if (valueInRange >= colorValues.length * 0.8) {
		 * Settings.PILOT.stop();
		 * Button.ENTER.waitForPress();
		 * // Set lineLeft true --> takeControl in LineFollow will deliver false
		 * lineLeft = true;
		 * reachedEndOfLine();
		 * return true;
		 * } else {
		 * return false;
		 * }
		 */

		/*
		 * if (Settings.TOUCH_L.isPressed()) {
		 * Settings.PILOT.stop();
		 * Settings.PILOT.rotate(-240);
		 * lineLeft = true;
		 * reachedEndOfLine();
		 * return true;
		 * } else {
		 * return false;
		 * }
		 */

		if (Math.abs(sonicTachoCount - Settings.MOTOR_SONIC.getTachoCount()) > 1) {
			Settings.PILOT.stop();

			Settings.PILOT.rotate(-240);

			lineLeft = true;

			reachedEndOfLine();

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void loopHook() {
		// Save last color values
		colorValues[currentIndex] = Settings.LIGHT_SENSOR.getNormalizedLightValue();

		// System.out.println("index" + currentIndex);
		// System.out.println("value " + colorValues[currentIndex]);

		// Increase current index
		currentIndex = (currentIndex + 1) % colorValues.length;
	}
}
