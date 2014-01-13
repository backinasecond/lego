package de.gruppe2.lineFollow;

import de.gruppe2.Settings;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class LineFollow2 implements Behavior {

	private boolean lineLeft = false;
	private boolean suppressed = false;

	static LightSensor lightSensor = Settings.LIGHT_SENSOR;

	private static int LINE_EDGE_COLOR = (Settings.LIGHT_LINE_DEFAULT + Settings.LIGHT_BLACK_DEFAULT) / 2;
	private static int COLOR_THRESHOLD = Math.abs(LINE_EDGE_COLOR
			- Settings.LIGHT_LINE_DEFAULT);
	private static int PROPORTIONAL_RANGE = COLOR_THRESHOLD - 20;

	// The speed of both motors, if the turn value is 0
	private static int TARGET_POWER = 160; // (TP)

	// Multiply this slope with the error and you get a value ranging from -1 to
	// 1
	private static float SLOPE = 2.0f / (2.0f * COLOR_THRESHOLD);

	// Multiply this value with the error and you get a value ranging from
	// -TARGET_POWER to TARGET_POWER
	private static float KP = SLOPE * TARGET_POWER;
	private static float KI = 0.1f;
	private static float KD = 100;

	@Override
	public boolean takeControl() {
		// Only take control if line was not left yet
		return !lineLeft;
	}

	@Override
	public void action() {
		suppressed = false;

		System.out.println("Line edge color: " + LINE_EDGE_COLOR);
		System.out.println("Color threshold: " + COLOR_THRESHOLD);
		System.out.println("Slope: " + SLOPE);
		System.out.println("KP: " + KP);
		Button.ENTER.waitForPressAndRelease();

		int degreesTurnedRight = 0;
		float integral = 0;
		boolean isRotating = false;
		while (!suppressed) {
			// Get difference between wanted light value and current light
			// value.
			// If error is positive, the robot is on the line and should steer
			// right to get back to the edge.
			// If error is negative, the robot is not on the line and should
			// steer left to get back to the edge.
			int error = LINE_EDGE_COLOR - lightSensor.getNormalizedLightValue();

			// If error is negative and not in the proportional range, robot is
			// on the line and should steer right
			if (error < -PROPORTIONAL_RANGE) {
				// TODO: Der Roboter befindet sich gerade nicht auf der Linie,
				// sondern irgendwo auf der Plane. Er sollte
				// sich nun h�chstens 90 Grad nach links drehen und schauen ob
				// er auf dem Weg die Linie wieder findet. Wenn
				// nicht ein Drehung nach rechts um h�chstens 180 Grad. Findet
				// er dann auch nichts, ist er am Ende der
				// Linie. --> Setze lineLeft auf true
				// Settings.PILOT.rotate(90, true);
				System.out.println("1 " + error);
				// RConsole.println("1");
				// Settings.PILOT.rotate(-90, true);
				// degreesTurnedRight += 20;
				isRotating = false;
			}
			// If error is positive and not in the proportional range, robot is
			// not on the line and should steer left
			else if (error > PROPORTIONAL_RANGE) {
				System.out.println("2 " + error);
				RConsole.println("2");
				if (!isRotating) {
					isRotating = true;
					Settings.PILOT.rotate(-110, true);
					System.out.println("------------");
				}

				if (Settings.PILOT.getAngleIncrement() > 100) {
					Settings.PILOT.rotate(200, true);
					System.out.println("rotating 2");
				}
			}
			// If the error is in the proportional range the robot should steer
			// a little right and left according to the
			// error
			else {
				isRotating = false;
				System.out.println("3 " + error);
				RConsole.println("3");
				degreesTurnedRight = 0;
				// Calculate the strength of the turn necessary to get back to
				// the edge
				// Turn will range from -TARGET_POWER to TARGET_POWER
				// Turn is positive when the error is positive --> robot on line
				// --> steer left
				// Turn is negative when the error is negative --> robot not on
				// line --> steer right
				integral = (2 / 3) * integral + error;
				float turn = KP * error + KI * integral;

				// This case should never happen, but it may be good for
				// debugging
				if (turn > TARGET_POWER || turn < -TARGET_POWER) {
					System.out
							.println("Error! The Motor speed will be set negative");
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

}
