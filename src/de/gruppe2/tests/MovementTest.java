package de.gruppe2.tests;

import lejos.nxt.Button;
import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class MovementTest {

	public static void main(String[] args) {
		AllWheelPilot pilot = new AllWheelPilot(Settings.WHEEL_DIAMETER, Settings.TRACK_WIDTH, Settings.MOTOR_LEFT, Settings.MOTOR_RIGHT);
		
		System.out.println("90 Grad Drehung links");
		Button.ENTER.waitForPress();
		pilot.rotate(90, true);
		
		System.out.println("90 Grad Drehung rechts");
		Button.ENTER.waitForPress();
		pilot.rotate(-90, true);
		
		System.out.println("180 Grad Drehung links");
		Button.ENTER.waitForPress();
		pilot.rotate(180, true);
	}

}
