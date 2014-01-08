package de.gruppe2.motor;

import lejos.nxt.Button;
import de.gruppe2.Settings;

public class MovementTest {

	public static void main(String[] args) {
		AllWheelPilot pilot = new AllWheelPilot(Settings.WHEEL_DIAMETER, Settings.TRACK_WIDTH, Settings.MOTOR_LEFT, Settings.MOTOR_RIGHT);
		
		System.out.println("90 Grad Drehung links");
		Button.waitForAnyPress();
		pilot.rotate(90);
		
		System.out.println("90 Grad Drehung rechts");
		Button.waitForAnyPress();
		pilot.rotate(-90);
		
		System.out.println("180 Grad Drehung links");
		Button.waitForAnyPress();
		pilot.rotate(180);
	}

}
