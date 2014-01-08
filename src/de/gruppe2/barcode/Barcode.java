package de.gruppe2.barcode;

import de.gruppe2.Settings;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Barcode {

	private final int THRESHOLD = 50;

	private LightSensor lightSensor;

	public Barcode() {
		this.lightSensor = Settings.LIGHT_SENSOR;
		this.lightSensor.setFloodlight(true);
	}

	private int count = 0;

	public int recognizeBarcode() {
		LCD.clear();
		System.out.println("Read Barcode");

		boolean counting = true;
		boolean onLine = false;
		int lineCount = 0;

		DifferentialPilot pilot = Settings.PILOT;
		pilot.setTravelSpeed(20);
		pilot.forward();
		
		while (counting) {
			int lightValue = lightSensor.getNormalizedLightValue();

			if (count++ % 100 == 0) {
				// System.out.println(lightValue);
			}

			// Count new line if not counted yet and light level is in range of
			// the line
			if (!onLine && lightValue > Settings.LIGHT_LINE_DEFAULT - THRESHOLD
					&& lightValue < Settings.LIGHT_LINE_DEFAULT + THRESHOLD) {
				lineCount++;
				onLine = true;

				System.out.println("Linie: " + lineCount);

				count = 0;

				// Necessary for start a new movement and check it with
				pilot.forward();
			}
			// Drive on foil until new line is detected or 10cm without a new
			// line
			else if (onLine
					&& lightValue > Settings.LIGHT_BLACK_DEFAULT - THRESHOLD
					&& lightValue < Settings.LIGHT_BLACK_DEFAULT + THRESHOLD) {
				onLine = false;

				System.out.println("Keine Linie");

				// Necessary for start a new movement and check it with
				pilot.forward();
			} 
			// If at least one line was detected and it was not on line for
			// 10 cm, stop reading
			else if (count != 0 && !onLine
					&& pilot.getMovementIncrement() > 100) {
				LCD.clear();
				System.out.println("CodeNumber: " + lineCount);
				counting = false;
			}
		}

		pilot.stop();

		/*
		 * switch (numOfTapes) { case 13:
		 * Settings.arbiMgr.changeState(RobotState.RACE); break; case 5:
		 * Settings.arbiMgr.changeState(RobotState.BRIDGE); break; case 7:
		 * Settings.arbiMgr.changeState(RobotState.MAZE); break; case 4:
		 * Settings.arbiMgr.changeState(RobotState.SWAMP); break; case 3:
		 * Settings.arbiMgr.changeState(RobotState.BT_GATE); break; case 11:
		 * Settings.arbiMgr.changeState(RobotState.TURNTABLE); break; case 12:
		 * Settings.arbiMgr.changeState(RobotState.SLIDER); break; case 10:
		 * Settings.arbiMgr.changeState(RobotState.SEESAW); break; case 6:
		 * Settings.arbiMgr.changeState(RobotState.SUSPENSION_BRIDGE); break;
		 * case 9: Settings.arbiMgr.changeState(RobotState.LINE_OBSTACLE);
		 * break; case 8: Settings.arbiMgr.changeState(RobotState.COLOR_GATE);
		 * break; case 14:
		 * Settings.arbiMgr.changeState(RobotState.END_OPPONENT); break;
		 * default: System.out.println("No codes read!"); break; }
		 */

		return lineCount;
	}

	public void calibrateLow() {
		lightSensor.calibrateLow();
		System.out.println("calibrated low: " + lightSensor.getLow());
	}

	public void calibrateHigh() {
		lightSensor.calibrateHigh();
		System.out.println("calibrated high: " + lightSensor.getHigh());
	}

	private float getMovementIncrement() {
		float leftWheelDiameter = 81.6f;
		float _leftDegPerDistance = (float) (360 / (Math.PI * leftWheelDiameter));

		int _leftTC = getLeftCount();

		return (getLeftCount() - _leftTC) / _leftDegPerDistance;
	}

	private int getLeftCount() {
		RegulatedMotor _left = Motor.A;
		byte _parity = (byte) 1;
		return _parity * _left.getTachoCount();
	}
}
