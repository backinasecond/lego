package de.gruppe2.maze;

import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class MazeWallHit implements Behavior {

    private boolean suppressed = false;
    
    private TouchSensor touch_r;
    private DifferentialPilot pilot;
    
    
    /**
     * Constructs a new HitWall Behavior
     */
    public MazeWallHit() {
            touch_r = Settings.TOUCH_L;
            pilot = Settings.PILOT;
    }
    
    /**
     * This Behavior takes control if the TouchSensor is pressed.
     */
    @Override
    public boolean takeControl() {
            return (touch_r.isPressed());
    }

    /**
     * The robot turns right.
     * 
     */
    @Override
    public void action() {
            suppressed = false;
            Settings.readState = true;
            Settings.atStartOfMaze = false;
            
            pilot.travel(-70);
            pilot.rotate(-120);
            while( pilot.isMoving() && !suppressed );
            pilot.stop();
    }

    /**
     * Initiates the cleanup when this Behavior is suppressed
     */
    @Override
    public void suppress() {
            suppressed = true;
    }

}
