package de.gruppe2.turntable;

import de.gruppe2.Settings;
import de.gruppe2.Settings.Symbol;
import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class TurnTurntableBehaviour implements Behavior {

	boolean tableTurned = false;
	
	@Override
	public boolean takeControl() {
		return !tableTurned;
	}

	@Override
	public void action() {
		Settings.PILOT.setTravelSpeed(120);		
		Settings.PILOT.setRotateSpeed(100);
		
		
		
		TurnTableControl turnControl = new TurnTableControl();
		while (!turnControl.connectToTurntable()) {}
		
		turnControl.sendSymbol(Settings.detectedSymbol);
		
		Settings.PILOT.travel(-100, false);
		Settings.PILOT.rotate(-170, false);
		
		Delay.msDelay(7000);
		
		tableTurned = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
