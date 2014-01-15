package de.gruppe2.barcode;

import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class BarcodeReader implements Behavior {

	private boolean suppressed = false;

	private boolean firstLineRecognized = false;

	private boolean codeReadFinished = false;

	public BarcodeReader() {
		Settings.LIGHT_SENSOR.setFloodlight(true);
	}

	@Override
	public boolean takeControl() {
		int lightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

		if (lightValue > Settings.LIGHT_LINE_DEFAULT - Settings.COLOR_DIFFERENCE_THRESHOLD && lightValue < Settings.LIGHT_LINE_DEFAULT + Settings.COLOR_DIFFERENCE_THRESHOLD) {
			firstLineRecognized = true;
		}

		return (!codeReadFinished && firstLineRecognized);
	}

	@Override
	public void action() {
		LCD.clear();
		suppressed = false;

		System.out.println("Read Barcode");

		Settings.PILOT.setTravelSpeed(120);
		Settings.PILOT.forward();

		boolean counting = true;
		boolean onLine = false;
		int lineCount = 0;

		int[] lightValues = new int[4];
		int lightValue;

		while (!suppressed && counting) {

			for (int i = 0; i < lightValues.length; i++) {
				lightValues[i] = Settings.LIGHT_SENSOR.getNormalizedLightValue();
			}

			java.util.Arrays.sort(lightValues);

			lightValue = (lightValues[lightValues.length / 2] + lightValues[(lightValues.length / 2) + 1]) / 2;

			// Count new line if not counted yet and light level is in range of
			// the line
			if (!onLine && lightValue > Settings.LIGHT_LINE_DEFAULT - Settings.COLOR_DIFFERENCE_THRESHOLD && lightValue < Settings.LIGHT_LINE_DEFAULT + Settings.COLOR_DIFFERENCE_THRESHOLD) {
				lineCount++;
				onLine = true;

				System.out.println("Linie: " + lineCount);

				// Necessary for start a new movement and check it with
				// getMovementIncrement()
				Settings.PILOT.forward();
			}
			// Drive on foil until new line is detected or 10cm without a new
			// line
			else if (onLine && lightValue > Settings.LIGHT_BLACK_DEFAULT - Settings.COLOR_DIFFERENCE_THRESHOLD && lightValue < Settings.LIGHT_BLACK_DEFAULT + Settings.COLOR_DIFFERENCE_THRESHOLD) {
				onLine = false;

				System.out.println("Keine Linie");

				// Necessary for start a new movement and check it with
				// getMovementIncrement()
				Settings.PILOT.forward();
			}
			// If at least one line was detected and it was not on line for
			// 10 cm, stop reading
			else if (lineCount != 0 && !onLine && Settings.PILOT.getMovementIncrement() > 100) {
				LCD.clear();
				System.out.println("CodeNumber: " + lineCount);
				counting = false;
			}
		}

		// Stop roboter after code was read. Next behavior must start it again
		Settings.PILOT.stop();

		boolean switchState = true;
		
		switch (lineCount) {
		case 3:
			Settings.CURRENT_LEVEL = RobotState.START_RACE;
			break;
		case 4:
			Settings.CURRENT_LEVEL = RobotState.LINE_FOLLOWER;
			break;
		case 5:
			Settings.CURRENT_LEVEL = RobotState.BRIDGE;
			break;
		case 6:
			Settings.CURRENT_LEVEL = RobotState.MAZE;
			break;
		case 7:
			Settings.CURRENT_LEVEL = RobotState.SYMBOL_RECOGNIZER;
			break;
		case 8: 
			Settings.CURRENT_LEVEL = RobotState.SHOOTING_RANGE;
			break;
		default:
			System.out.println("No known code read!");
			switchState = false;
			break;
		}
		
		if(switchState) {
			Settings.ARBITRATOR_MANAGER.changeState(Settings.CURRENT_LEVEL);
		}

		codeReadFinished = true;
	}
	
	/*
	 * Needed to start a new code reading segment.
	 */
	public void reset() {
		suppressed = false;
		firstLineRecognized = false;
		codeReadFinished = false;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}