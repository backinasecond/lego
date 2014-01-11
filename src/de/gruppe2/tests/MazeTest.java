package de.gruppe2.tests;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;
import de.gruppe2.maze.MazeWallFollowBehaviour;
import de.gruppe2.maze.MazeWallHitBehaviour;
import de.gruppe2.util.LightDetectionBehaviour;

public class MazeTest implements ButtonListener {


        public MazeTest() {
                Button.ESCAPE.addButtonListener(this);
        }

        public static void main(String[] args) throws Exception {
                new MazeTest();
                Settings.AT_MAZE = true;
                
                Behavior wallHit = new MazeWallHitBehaviour();
                Behavior wallFollow = new MazeWallFollowBehaviour();
                Behavior lineDetection = new LightDetectionBehaviour(Settings.LIGHT_LINE_DEFAULT);
                
                Behavior[] bArray = { wallFollow, wallHit, lineDetection };

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
