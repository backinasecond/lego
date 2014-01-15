package de.gruppe2.barcode;

import de.gruppe2.Settings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class describes the Behavior to simple drive forward.
 * 
 */
public class DriveForward implements Behavior {
	private boolean suppressed = false;
	private int travelSpeed;

	/**
	 * Constructs a new DriveForward behavior. The travel speed will be set to 120 degree per second
	 */
	public DriveForward() {
		this.travelSpeed = 120;
	}

	/**
	 * Constructs a new DriveForward behavior. The parameter travelSpeed will define the speed of the movement.
	 * 
	 * @param travelSpeed Will define the speed of the movement.
	 */
	public DriveForward(int travelSpeed) {
		this.travelSpeed = travelSpeed;
	}

	/**
	 * Takes always Control. returns true
	 */
	public boolean takeControl() {
		return true;
	}

	/**
	 * Moves forward as long as this Behavior is active
	 */
	public void action() {
		suppressed = false;

		Settings.PILOT.setTravelSpeed(this.travelSpeed);
		Settings.PILOT.forward();

		while (!suppressed) {
			Settings.PILOT.travel(5, true);
		}
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	public void suppress() {
		suppressed = true;
	}
}