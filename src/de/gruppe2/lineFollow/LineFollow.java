package de.gruppe2.lineFollow;

import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;

public class LineFollow implements Behavior {

	private static boolean DEBUG = true;
	private boolean lineLeft = false;
	private boolean suppressed = false;

	static LightSensor lightSensor = Settings.LIGHT_SENSOR;

	private static int LINE_EDGE_COLOR = (Settings.LIGHT_LINE_DEFAULT + Settings.LIGHT_BLACK_DEFAULT) / 2;
	private static int COLOR_THRESHOLD = Math.abs(LINE_EDGE_COLOR - Settings.LIGHT_LINE_DEFAULT) + 50;
	private static int PROPORTIONAL_RANGE = COLOR_THRESHOLD - 50;

	// The speed of both motors, if the turn value is 0
	private static int TARGET_POWER = 320; // (TP)

	// Multiply this slope with the error and you get a value ranging from -1 to
	// 1
	private static float SLOPE = 2.0f / (2.0f * COLOR_THRESHOLD);

	// Multiply this value with the error and you get a value ranging from
	// -TARGET_POWER to TARGET_POWER
	private static float KP = SLOPE * TARGET_POWER;
	private static float KI = 0.1f;

	private RobotState nextState = RobotState.BARCODE;

	/**
	 * Constructor of a LineFollow behavior. After the end of the line will be recognized, the barcode scanner starts.
	 */
	public LineFollow() {
	}

	/**
	 * Constructor of a LineFollow behavior. Use this constructor to set the state that will be started after the end of
	 * line was recognized.
	 * 
	 * @param nextState State that will be started after the end of line was recognized.
	 */
	public LineFollow(RobotState nextState) {
		setNextState(nextState);
	}

	@Override
	public boolean takeControl() {
		// Only take control if line was not left yet
		return !lineLeft;
	}

	@Override
	public void action() {
		suppressed = false;
		lineLeft = false;

		float integral = 0;
		boolean isRotatingLeft = false;
		boolean isRotatingRight = false;
		int error;
		float turn;

		while (!suppressed && !lineLeft) {
			// Get difference between wanted light value and current light
			// value.
			// If error is positive, the robot is on the line and should steer
			// right to get back to the edge.
			// If error is negative, the robot is not on the line and should
			// steer left to get back to the edge.
			error = LINE_EDGE_COLOR - lightSensor.getNormalizedLightValue();

			if (isRotatingLeft && lightSensor.getNormalizedLightValue() < 450) {
				if (!isRotatingRight && Settings.PILOT.getAngleIncrement() > 150) {
					if (DEBUG) {
						System.out.println("1");
					}

					Settings.PILOT.rotate(-340, true);
					isRotatingRight = true;
				}
				// Rotating right
				else if (Settings.PILOT.getAngleIncrement() < -330) {
					if (DEBUG) {
						System.out.println("2");
					}

					// No line found. Adjusting robot
					Settings.PILOT.rotate(120);
					reachedEndOfLine();
				}

			}
			// If error is negative and not in the proportional range, robot is
			// on the line and should steer right
			else if (error < -PROPORTIONAL_RANGE) {
				if (DEBUG) {
					// System.out.println("3");
				}
				Settings.PILOT.steer(-20, -20, true);
				isRotatingLeft = false;
			}
			// If error is positive and not in the proportional range, robot is
			// not on the line and should steer left
			else if (error > PROPORTIONAL_RANGE) {
				if (DEBUG) {
					System.out.println("4 " + lightSensor.getNormalizedLightValue());
				}

				integral = 0;
				if (!isRotatingLeft) {
					isRotatingLeft = true;
					isRotatingRight = false;

					Settings.PILOT.travel(30);

					Settings.PILOT.rotate(150, true);
				}
			}
			// If the error is in the proportional range the robot should steer
			// a little right and left according to the
			// error
			else {
				if (DEBUG) {
					// System.out.println("5");
					System.out.println("5 " + lightSensor.getNormalizedLightValue());
				}

				isRotatingLeft = false;
				// Calculate the strength of the turn necessary to get back to
				// the edge
				// Turn will range from -TARGET_POWER to TARGET_POWER
				// Turn is positive when the error is positive --> robot on line
				// --> steer left
				// Turn is negative when the error is negative --> robot not on
				// line --> steer right
				integral = (2 / 3) * integral + error;
				turn = KP * error + KI * integral;
				turn *= 0.8;

				// This case should never happen, but it may be good for
				// debugging
				if (turn > TARGET_POWER || turn < -TARGET_POWER) {
					RConsole.println("Error! The Motor speed will be set negative");
					System.out.println("Error! Negative speed");
					turn = TARGET_POWER;
				}

				Settings.MOTOR_LEFT.setSpeed(TARGET_POWER - turn);
				Settings.MOTOR_RIGHT.setSpeed(TARGET_POWER + turn);

				Settings.MOTOR_LEFT.forward();
				Settings.MOTOR_RIGHT.forward();
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

	private void reachedEndOfLine() {
		Settings.PILOT.travel(8, false); // needed to ignore the line (so it will not be counted into the barcode reading)
		Settings.ARBITRATOR_MANAGER.changeState(this.nextState);
		lineLeft = true;
	}

	/**
	 * Set state that will be started after the end of line was recognized.
	 * 
	 * @param next State state that will be started after the end of line was recognized.
	 */
	private void setNextState(RobotState nextState) {
		this.nextState = nextState;
	}

}
