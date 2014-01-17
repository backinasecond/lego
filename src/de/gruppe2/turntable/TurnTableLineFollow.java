package de.gruppe2.turntable;

import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.Symbol;

public class TurnTableLineFollow implements Behavior {
	
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
	private static int TARGET_POWER = 320; // (TP)

	// Multiply this slope with the error and you get a value ranging from -1 to
	// 1
	private static float SLOPE = 2.0f / (2.0f * COLOR_THRESHOLD);

	// Multiply this value with the error and you get a value ranging from
	// -TARGET_POWER to TARGET_POWER
	private static float KP = SLOPE * TARGET_POWER;
	private static float KI = 0.1f;
	
	private boolean finished = false;

	@Override
	public boolean takeControl() {
		return !finished;
	}

	@Override
	public void action() {
		suppressed = false;

		float integral = 0;
		boolean isRotatingLeft = false;
		boolean isRotatingRight = false;
		int error;
		float turn;
		float angleIncrement = 0;
		long startTime = 0;
		finished = false;

		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * Settings.TAPE_FOLLOW_SPEED);
		Settings.PILOT.setRotateSpeed(Settings.PILOT.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);
		
		while (!suppressed && !finished) {
			System.out.println("turntableline");
			// Get difference between wanted light value and current light
			// value.
			// If error is positive, the robot is on the line and should steer
			// right to get back to the edge.
			// If error is negative, the robot is not on the line and should
			// steer left to get back to the edge.
			error = LINE_EDGE_COLOR - lightSensor.getNormalizedLightValue();
			angleIncrement = Settings.PILOT.getAngleIncrement();

			if (isRotatingLeft && lightSensor.getNormalizedLightValue() < 450) {
				if (DEBUG) {
					System.out.println("1 " + angleIncrement);
				}
				if (!isRotatingRight) {
					if(angleIncrement > 170) {
						Settings.PILOT.rotate(-420, true);
						isRotatingRight = true;
						rotatedMoreThan50Degree = false;
					} else if(angleIncrement > 50) {
						rotatedMoreThan50Degree = true;
					} 
				}
				// Rotating right
				else {
					if (angleIncrement < -410) {
						if (DEBUG) {
							System.out.println("errrroooooor");
						}
	
						// No line found. Adjusting robot
						Settings.PILOT.rotate(130);
					} else if(angleIncrement < -250) {
						rotatedMoreThan50Degree = true;
					}
				}
			}
			// If error is negative and not in the proportional range, robot is
			// on the line and should steer right
			else if (error < -PROPORTIONAL_RANGE) {
				if (DEBUG) {
					System.out.println("2");
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
					Settings.PILOT.travel(60);
					Settings.PILOT.rotate(200, true);
				}
			}
			// If the error is in the proportional range the robot should steer
			// a little right and left according to the
			// error
			else {
				if(startTime > 0) {
					// Wait for some time and the drive straight forward until 
					// touch sensor is pressed
					if(System.currentTimeMillis() - startTime > 7000) {
						System.out.println("Follow 7 seconds");
						
						finished = true;
					}
				}
			
				if(!finished) {
					if (DEBUG) {
						//System.out.println("5");
						System.out.println("5 " + lightSensor.getNormalizedLightValue());
					}
					
					if(isRotatingLeft && rotatedMoreThan50Degree) {
						directions += "L";
						startTime = System.currentTimeMillis();
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
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
}
