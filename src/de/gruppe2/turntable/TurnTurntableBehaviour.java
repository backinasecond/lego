package de.gruppe2.turntable;

import lejos.robotics.subsumption.Behavior;

public class TurnTurntableBehaviour implements Behavior {

	@Override
	public boolean takeControl() {
		System.out.println("Starting turn");
		
		return true;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
