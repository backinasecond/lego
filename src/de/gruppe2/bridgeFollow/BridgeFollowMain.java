package de.gruppe2.bridgeFollow;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.CalibrateSonic;
import de.gruppe2.Settings;

public class BridgeFollowMain implements ButtonListener {


        public BridgeFollowMain() {
                Button.ESCAPE.addButtonListener(this);
        }

        public static void main(String[] args) throws Exception {
        	//RConsole.open();
        	
            new BridgeFollowMain();
            CalibrateSonic.calibrateVertically();
            
            // Bridge
            Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.60);
            Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
            
            Behavior b1 = new BridgeBefore();
            Behavior b2 = new BridgeStart();
            Behavior b3 = new BridgeFollow();
            Behavior b4 = new BridgeEnd();
            Behavior[] behaviors = { b1, b2, b3, b4 };

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
