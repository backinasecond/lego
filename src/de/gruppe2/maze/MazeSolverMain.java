package de.gruppe2.maze;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class MazeSolverMain implements ButtonListener {


        public MazeSolverMain() {
                Button.ESCAPE.addButtonListener(this);
        }

        public static void main(String[] args) throws Exception {
                new MazeSolverMain();
                
                
                // MAZE
                Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.60);
                Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
                
                Behavior b1 = new MazeWallHit();
                Behavior b2 = new MazeWallFollow(7);
                
                Behavior[] bArray = { b2, b1 };

                Arbitrator arbitrator = new Arbitrator(bArray);                
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
