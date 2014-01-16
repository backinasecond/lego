package de.gruppe2.raceTrack;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class RaceTrackHitBehaviour implements Behavior {

    private boolean suppressed = false;    
    
    /**
     * Constructs a new HitWall Behavior
     */
    public RaceTrackHitBehaviour() {
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
            
            Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 1);
            
            if (Settings.SONIC_SENSOR.getDistance() > 2 * Settings.RACE_TRACK_WALL_DISTANCE_THRESHOLD) {
                Settings.PILOT.travel(50);
                Settings.PILOT.rotate(50);
            } else {
                Settings.PILOT.travel(-300);
                Settings.PILOT.rotate(-90);            	
            }
            
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
