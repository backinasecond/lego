package de.gruppe2.barcode;

import de.gruppe2.Settings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class describes the Behavior to simple drive forward.
 * 
 * @author Team Curiosity
 * 
 */
public class DriveForward implements Behavior {
	private DifferentialPilot pilot;
	private boolean suppressed = false;

	/**
	 * Constructs a new DriveForward Behavior
	 */
	public DriveForward() {
		this.pilot = Settings.PILOT;
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

		Settings.PILOT.setTravelSpeed(20);
		Settings.PILOT.forward();
		
		while (!suppressed) {
			pilot.travel(5, true);
		}
	}

	/**
	 * Initiates the cleanup when this Behavior is suppressed
	 */
	public void suppress() {
		suppressed = true;
	}
}