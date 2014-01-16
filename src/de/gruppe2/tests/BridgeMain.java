package de.gruppe2.tests;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;
import de.gruppe2.bridgeFollow.BridgeBefore;
import de.gruppe2.bridgeFollow.BridgeBeforeElevator;
import de.gruppe2.bridgeFollow.BridgeFollow;
import de.gruppe2.bridgeFollow.BridgeStart;
import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.util.LightThresholdBehavior;

public class BridgeMain implements ButtonListener {

	public BridgeMain() {
		Button.ESCAPE.addButtonListener(this);
	}

	public static void main(String[] args) throws Exception {
		new BridgeMain();

		Behavior b0 = new BridgeBefore();
		Behavior b1 = new BridgeFollow();
		Behavior b2 = new BridgeStart();
		Behavior b3 = new BridgeBeforeElevator();

		Behavior[] behaviors = { b1, b2, b3, b0 };

		//RConsole.open();
		Button.waitForAnyPress();
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
		//RConsole.close();
	}

	@Override
	public void buttonPressed(Button b) {
		stopRunning();
	}

	@Override
	public void buttonReleased(Button b) {
		stopRunning();
	}

	private void stopRunning() {
		// Stop the arbitrator, the main program and the motors.
		System.exit(0);
	}
}
