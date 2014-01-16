package de.gruppe2.turntable;

import de.gruppe2.Settings;
import de.gruppe2.Settings.Symbol;
import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class TurnTurntableBehaviour implements Behavior {

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		TurnTableControl turntableControl = new TurnTableControl();
		
		Settings.detectedSymbol = Symbol.L;
		
		while (!turntableControl.connectToTurntable()) {
			
		}
		
        turntableControl.sendSymbol(Settings.detectedSymbol);
		
        System.out.println("Drücke Knopf");
		Button.waitForAnyPress();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
