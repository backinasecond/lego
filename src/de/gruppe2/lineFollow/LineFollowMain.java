package de.gruppe2.lineFollow;

import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class LineFollowMain {

	static DifferentialPilot pilot = Settings.PILOT;
	static LightSensor light = Settings.LIGHT_SENSOR;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
        int lights[] = new int[300];

        for (int j = 0; j < lights.length; j++) {
                Settings.PILOT.travel(1, true);
                lights[j] = light.getNormalizedLightValue();
        }
        
        java.util.Arrays.sort(lights);
        
        int min = 0;
        int max = 100;
        int tempSum = 0;
        
        for (int j = 0; j < 10; j++) {
                tempSum += lights[j];
        }
        
        min = tempSum / 10;
        tempSum = 0;
        
        for (int j = lights.length - 10; j < lights.length; j++) {
                tempSum += lights[j];
        }
        
        max = tempSum / 10;
        System.out.println("min: " + min + ", max: " + max);
        light.setLow(min);
		light.setHigh(max);
		*/
		double speed = pilot.getMaxTravelSpeed() * Settings.TAPE_FOLLOW_SPEED;
		pilot.setTravelSpeed(speed);
		// pilot.setTravelSpeed(pilot.getMaxTravelSpeed() * 0.4);
		pilot.setRotateSpeed(pilot.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);

//		Settings.motorAAngle = 0;

		Behavior t1 = new LineFollow2();

		Behavior[] tapeFollowArray = { t1 };

		//RConsole.open();
		Arbitrator lineFollowArbitrator = new Arbitrator(tapeFollowArray);
		lineFollowArbitrator.start();
		//RConsole.close();
	}

}
