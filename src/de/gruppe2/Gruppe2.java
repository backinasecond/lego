package de.gruppe2;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class Gruppe2 {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting DiffPilot");
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		pilot.setTravelSpeed(100);
		pilot.travel(100);
		pilot.setRotateSpeed(30);
		pilot.rotate(180);
		Button.waitForAnyPress();
	}

}
