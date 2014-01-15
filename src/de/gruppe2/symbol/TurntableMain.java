package de.gruppe2.symbol;

import de.gruppe2.Settings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class TurntableMain {
	
	static DifferentialPilot pilot = Settings.PILOT;
	
	public static void main(String[] args) {
		
		double speed = pilot.getMaxTravelSpeed() * Settings.TAPE_FOLLOW_SPEED;
		pilot.setTravelSpeed(speed);
		// pilot.setTravelSpeed(pilot.getMaxTravelSpeed() * 0.4);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);
		
		Behavior t1 = new SymbolFollow();

		Behavior[] turntableArray = { t1 };

		Arbitrator turntableArbitrator = new Arbitrator(turntableArray);
		turntableArbitrator.start();
	}
}
