package de.gruppe2.maze;

import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;
import de.gruppe2.motor.AllWheelPilot;

public class MazeWallHit implements Behavior {

    private boolean suppressed = false;
    
    private TouchSensor touch_r;
    private AllWheelPilot pilot;
    
    
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
//            Settings.atStartOfMaze = false;
            
            pilot.travel(-90);
            pilot.rotate(-80);
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
