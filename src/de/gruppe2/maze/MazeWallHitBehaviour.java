package de.gruppe2.maze;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class MazeWallHitBehaviour implements Behavior {

    private boolean suppressed = false;    
    
    /**
     * Constructs a new HitWall Behavior
     */
    public MazeWallHitBehaviour() {
    }
    
    /**
     * This Behavior takes control if the TouchSensor is pressed.
     */
    @Override
    public boolean takeControl() {
            return (Settings.TOUCH_L.isPressed());
    }

    /**
     * The robot turns right.
     * 
     */
    @Override
    public void action() {
            suppressed = false;
            Settings.readState = true;
//            Settings.atStartOfMaze = false;
            
            Settings.PILOT.travel(-90);
            Settings.PILOT.rotate(-80);
            while( Settings.PILOT.isMoving() && !suppressed );
            Settings.PILOT.stop();
    }

    /**
     * Initiates the cleanup when this Behavior is suppressed
     */
    @Override
    public void suppress() {
            suppressed = true;
    }

}
