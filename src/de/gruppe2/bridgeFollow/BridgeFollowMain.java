package de.gruppe2.bridgeFollow;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;
import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.util.LightThresholdBehavior;

public class BridgeFollowMain implements ButtonListener {

	public BridgeFollowMain() {
		Button.ESCAPE.addButtonListener(this);
	}

	public static void main(String[] args) throws Exception {
		new BridgeFollowMain();

		Behavior b0 = new BridgeBefore();
		Behavior b1 = new BridgeFollow();
		Behavior b2 = new BridgeStart();
		Behavior b3 = new LightThresholdBehavior(430);

		Behavior[] behaviors = { b1, b2, b3, b0 };

		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
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
