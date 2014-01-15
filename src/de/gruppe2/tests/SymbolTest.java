package de.gruppe2.tests;

import de.gruppe2.Settings;
import de.gruppe2.symbol.SymbolFollow;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class SymbolTest {
	
	static DifferentialPilot pilot = Settings.PILOT;
	
	public static void main(String[] args) {
		
		double speed = pilot.getMaxTravelSpeed() * Settings.TAPE_FOLLOW_SPEED;
		pilot.setTravelSpeed(speed);
		// pilot.setTravelSpeed(pilot.getMaxTravelSpeed() * 0.4);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);
		
		Behavior t1 = new SymbolFollow();

		Behavior[] symbolArray = { t1 };

		Arbitrator symbolArbitrator = new Arbitrator(symbolArray);
		symbolArbitrator.start();
	}
}
