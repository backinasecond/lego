package de.gruppe2.tests;

import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class SteerTest {

	public static void main(String[] args) {
		AllWheelPilot pilot = new AllWheelPilot(Settings.WHEEL_DIAMETER, Settings.TRACK_WIDTH, Settings.MOTOR_LEFT, Settings.MOTOR_RIGHT);
		
		pilot.steer(50, 90);
	}

}
