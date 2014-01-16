package de.gruppe2.turntable;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class TurnTurntableBehaviour implements Behavior {

	@Override
	public boolean takeControl() {
		
		
		return true;
	}

	@Override
	public void action() {
		System.out.println("Starting turning table");
		
		Button.waitForAnyPress();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
