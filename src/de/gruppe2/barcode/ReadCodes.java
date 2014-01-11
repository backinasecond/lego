package de.gruppe2.barcode;

import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class ReadCodes implements Behavior {

	private boolean suppressed = false;
	
	private boolean firstLineRecognized = false;

	// Difference between color values of potential same colors
	private final int COLOR_DIFFERENCE_THRESHOLD = 50;

	private boolean codeReadFinished = false;

	public ReadCodes() {
		Settings.LIGHT_SENSOR.setFloodlight(true);
	}

	@Override
	public boolean takeControl() {
		int lightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

		if (lightValue > Settings.LIGHT_LINE_DEFAULT - COLOR_DIFFERENCE_THRESHOLD
				&& lightValue < Settings.LIGHT_LINE_DEFAULT + COLOR_DIFFERENCE_THRESHOLD) {
			firstLineRecognized = true;
		}

		return (!codeReadFinished && firstLineRecognized);
	}

	@Override
	public void action() {
		LCD.clear();
		System.out.println("Read Barcode");

		boolean counting = true;
		boolean onLine = false;
		int lineCount = 0;

		DifferentialPilot pilot = Settings.PILOT;
		pilot.setTravelSpeed(20);
		pilot.forward();

		while (counting) {
			while (!suppressed) {
				Thread.yield();
			}

			int lightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

			// Count new line if not counted yet and light level is in range of
			// the line
			if (!onLine && lightValue > Settings.LIGHT_LINE_DEFAULT - COLOR_DIFFERENCE_THRESHOLD
					&& lightValue < Settings.LIGHT_LINE_DEFAULT + COLOR_DIFFERENCE_THRESHOLD) {
				lineCount++;
				onLine = true;

				System.out.println("Linie: " + lineCount);

				// Necessary for start a new movement and check it with getMovementIncrement()
				pilot.forward();
			}
			// Drive on foil until new line is detected or 10cm without a new
			// line
			else if (onLine && lightValue > Settings.LIGHT_BLACK_DEFAULT - COLOR_DIFFERENCE_THRESHOLD
					&& lightValue < Settings.LIGHT_BLACK_DEFAULT + COLOR_DIFFERENCE_THRESHOLD) {
				onLine = false;

				System.out.println("Keine Linie");

				// Necessary for start a new movement and check it with getMovementIncrement()
				pilot.forward();
			}
			// If at least one line was detected and it was not on line for
			// 10 cm, stop reading
			else if (lineCount != 0 && !onLine && pilot.getMovementIncrement() > 100) {
				LCD.clear();
				System.out.println("CodeNumber: " + lineCount);
				counting = false;
			}
		}

		// Stop roboter after code was read. Next behavior must start it again
		pilot.stop();

		switch (lineCount) {
		case 13:
			Settings.ARBITRATOR_MANAGER.changeState(RobotState.MAZE);
			break;

		default:
			System.out.println("No codes read!");
			break;
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	/*
	 * public void calibrateLow() { lightSensor.calibrateLow();
	 * System.out.println("calibrated low: " + lightSensor.getLow()); }
	 * public void calibrateHigh() { lightSensor.calibrateHigh();
	 * System.out.println("calibrated high: " + lightSensor.getHigh()); }
	 */
}
