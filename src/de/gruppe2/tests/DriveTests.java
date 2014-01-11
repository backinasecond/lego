package de.gruppe2.tests;

import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class DriveTests{

	static DifferentialPilot pilot = Settings.PILOT;

	public static void main(String[] args) {
		AllWheelPilot pilot = new AllWheelPilot(Settings.WHEEL_DIAMETER, Settings.TRACK_WIDTH, Settings.MOTOR_LEFT, Settings.MOTOR_RIGHT);
		
		System.out.println("Steer Test");
		
		Button.waitForAnyPress();
		
		pilot.steer(-150, 110, false);
		
//		Button.waitForAnyPress();
//		
//		pilot.steer(200, 110, false);
//		
//		Button.waitForAnyPress();
	}
}
