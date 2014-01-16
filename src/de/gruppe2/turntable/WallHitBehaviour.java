package de.gruppe2.turntable;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class WallHitBehaviour implements Behavior {
    
    /**
     * Constructs a new HitWall Behavior
     */
    public WallHitBehaviour() {
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
            Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 1);
            
            // Travel backward to get enough space for rotation
            Settings.PILOT.travel(-10);
            
            // Turn around to get in position for leaving the turn table
            Settings.PILOT.rotate(180);
    }

    /**
     * Initiates the cleanup when this Behavior is suppressed
     */
    @Override
    public void suppress() {
    }

}
