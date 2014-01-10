package de.gruppe2.tests;

import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import de.gruppe2.Settings;

public class DriveTests{

	static DifferentialPilot pilot = Settings.PILOT;

	public static void main(String[] args) {
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
