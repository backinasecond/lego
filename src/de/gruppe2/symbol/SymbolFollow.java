package de.gruppe2.symbol;

import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.Symbol;

public class SymbolFollow implements Behavior {
	
	private final String L1 = "L";
	private final String L2 = "R";
	private final String U1 = "LL";
	private final String U2 = "RR";
	private final String Z1 = "LR";
	private final String Z2 = "RL";
	private final String M1 = "RLR";
	private final String M2 = "LRL";
	
	private boolean rotatedMoreThan50Degree = false;
	private String directions = "";

	private static boolean DEBUG = false;
	private boolean symbolRecognized = false;
	private boolean suppressed = false;

	static LightSensor lightSensor = Settings.LIGHT_SENSOR;

	private static int LINE_EDGE_COLOR = (Settings.LIGHT_LINE_DEFAULT + Settings.LIGHT_BLACK_DEFAULT) / 2;
	private static int COLOR_THRESHOLD = Math.abs(LINE_EDGE_COLOR - Settings.LIGHT_LINE_DEFAULT) + 50;
	private static int PROPORTIONAL_RANGE = COLOR_THRESHOLD - 50;

	// The speed of both motors, if the turn value is 0
	private static int TARGET_POWER = 250; // (TP)

	// Multiply this slope with the error and you get a value ranging from -1 to
	// 1
	private static float SLOPE = 2.0f / (2.0f * COLOR_THRESHOLD);

	// Multiply this value with the error and you get a value ranging from
	// -TARGET_POWER to TARGET_POWER
	private static float KP = SLOPE * TARGET_POWER;
	private static float KI = 0.1f;

	@Override
	public boolean takeControl() {
		// Only take control if line was not left yet
		return !symbolRecognized;
	}

	@Override
	public void action() {
		suppressed = false;

		Settings.detectedSymbol = Symbol.NONE;
		float integral = 0;
		boolean isRotatingLeft = false;
		boolean isRotatingRight = false;
		int error;
		float turn;
		float angleIncrement = 0;

		while (!suppressed && !symbolRecognized) {
			// Get difference between wanted light value and current light
			// value.
			// If error is positive, the robot is on the line and should steer
			// right to get back to the edge.
			// If error is negative, the robot is not on the line and should
			// steer left to get back to the edge.
			error = LINE_EDGE_COLOR - lightSensor.getNormalizedLightValue();
			angleIncrement = Settings.PILOT.getAngleIncrement();

			if (isRotatingLeft && lightSensor.getNormalizedLightValue() < 450) {
				if (!isRotatingRight) {
					if(angleIncrement > 170) {
						if (DEBUG) {
							System.out.println("1");
						}
	
						Settings.PILOT.rotate(-420, true);
						isRotatingRight = true;
						rotatedMoreThan50Degree = false;
					} else if(angleIncrement > 60) {
						rotatedMoreThan50Degree = true;
					} 
				}
				// Rotating right
				else {
					if (angleIncrement < -410) {
						if (DEBUG) {
							System.out.println("2");
						}
	
						// No line found. Adjusting robot
						Settings.PILOT.rotate(175);
						
						// End of symbol recognized
						symbolRecognized = true;
					} else if(angleIncrement < -250) {
						rotatedMoreThan50Degree = true;
					}
				}
			}
			// If error is negative and not in the proportional range, robot is
			// on the line and should steer right
			else if (error < -PROPORTIONAL_RANGE) {
				if (DEBUG) {
					//System.out.println("3");
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
					Settings.PILOT.travel(40);
					Settings.PILOT.rotate(200, true);
				}
			}
			// If the error is in the proportional range the robot should steer
			// a little right and left according to the
			// error
			else {
				if (DEBUG) {
					//System.out.println("5");
					//System.out.println("5 " + lightSensor.getNormalizedLightValue());
				}
				
				if(isRotatingRight && rotatedMoreThan50Degree) {
					directions += "R";
				}
				else if(isRotatingLeft && rotatedMoreThan50Degree) {
					directions += "L";
				} 

				isRotatingLeft = false;
				isRotatingRight = false;
				rotatedMoreThan50Degree = false;
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
			
			if(symbolRecognized)
			{
				System.out.println("Left line: " + directions);
				
				if(directions.equals(L1) || directions.equals(L2)) {
					System.out.println("Symbol L");
					Settings.detectedSymbol = Symbol.L;
				} else if(directions.equals(U1) || directions.equals(U2)) {
					System.out.println("Symbol U");
					Settings.detectedSymbol = Symbol.U;
				} else if(directions.equals(Z1) || directions.equals(Z2)) {
					System.out.println("Symbol Z");
					Settings.detectedSymbol = Symbol.Z;
				} else if(directions.equals(M1) || directions.equals(M2)) {
					System.out.println("Symbol M");
					Settings.detectedSymbol = Symbol.M;
				}
				
				// Change to line follower if symbol was recognized
				Settings.ARBITRATOR_MANAGER.changeState(RobotState.TO_TURNTABLE);
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}
