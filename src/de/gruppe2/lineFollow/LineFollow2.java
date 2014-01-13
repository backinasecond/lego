package de.gruppe2.lineFollow;

import de.gruppe2.Settings;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Behavior;

public class LineFollow2 implements Behavior {

	private boolean lineLeft = false;
	private boolean suppressed = false;

	static LightSensor lightSensor = Settings.LIGHT_SENSOR;

	private static int LINE_EDGE_COLOR = (Settings.LIGHT_LINE_DEFAULT + Settings.LIGHT_BLACK_DEFAULT) / 2;
	private static int COLOR_THRESHOLD = Math.abs(LINE_EDGE_COLOR - Settings.LIGHT_LINE_DEFAULT);
	private static int PROPORTIONAL_RANGE = COLOR_THRESHOLD - 10;

	// The speed of both motors, if the turn value is 0
	private static int TARGET_POWER = 50; // (TP)

	// Multiply this slope with the error and you get a value ranging from -1 to 1
	private static float SLOPE = 2.0f / (2.0f * COLOR_THRESHOLD);

	// Multiply this value with the error and you get a value ranging from -TARGET_POWER to TARGET_POWER
	private static float KP = SLOPE * TARGET_POWER;

	@Override
	public boolean takeControl() {
		// Only take control if line was not left yet
		return !lineLeft;
	}

	@Override
	public void action() {
		// Get difference between wanted light value and current light value.
		// If error is positive, the roboter is on the line and should steer left to get back to the edge.
		// If error is negative, the roboter is not on the line and should steer right to get back to the edge.
		int error = LINE_EDGE_COLOR - lightSensor.getNormalizedLightValue();

		// If error is negative and not in the proportional range, it should steer hard right because it is far away of
		// the edge
		if (error < -PROPORTIONAL_RANGE) {
			// TODO: Der Roboter befindet sich gerade nicht auf der Linie, sondern irgendwo auf der Plane. Er sollte
			// sich nun höchstens 90 Grad nach links drehen und schauen ob er auf dem Weg die Linie wieder findet. Wenn
			// nicht ein Drehung nach rechts um höchstens 180 Grad. Findet er dann auch nichts, ist er am Ende der
			// Linie. --> Setze lineLeft auf true

		}
		// If error is positive and not in the proportional range, it should steer hard left because it is far away of
		// the edge
		else if (error > PROPORTIONAL_RANGE) {

		}
		// If the error is in the proportional range the robot should steer a little left and right according to the
		// error
		else {
			// Calculate the strength of the turn necessary to get back to the edge
			// Turn will range from -TARGET_POWER to TARGET_POWER
			// Turn is positive when the error is positive --> robot on line --> steer left
			// Turn is negative when the error is negative --> robot not on line --> steer right
			float turn = KP * error;

			// This case should never happen, but it may be good for debugging
			if (turn > TARGET_POWER || turn < -TARGET_POWER) {
				System.out.println("Error! The Motor speed will be set negative");
			}
			
			Settings.MOTOR_LEFT.setSpeed(TARGET_POWER - turn);
			Settings.MOTOR_RIGHT.setSpeed(TARGET_POWER + turn);

		}

		while (!suppressed) {

		}

	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
