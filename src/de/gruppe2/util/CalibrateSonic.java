package de.gruppe2.util;

import lejos.util.Delay;
import de.gruppe2.Settings;

/**
 * CalibrateSonic sensor
 */
public class CalibrateSonic {
	
	/**
	 * Rotate sensor in order to use it for measuring the distance.
	 */
	public static void calibrateHorizontally()
	{
		rotateSensorUntilBlocked();
		Settings.MOTOR_SONIC.rotate(-168, false);
		Settings.MOTOR_SONIC.resetTachoCount();
		System.out.println("Calib hor done");
		
	}
	
	/**
	 * Rotate sensor in order to use it for measuring the height.
	 */
	public static void calibrateVertically()
	{
		rotateSensorUntilBlocked();
		Settings.MOTOR_SONIC.rotate(-110, false);
		Settings.MOTOR_SONIC.resetTachoCount();
		System.out.println("Calib vert done");
	}
	
	/**
	 * Move sensor down until it is blocked by the wheel.
	 */
	public static void rotateSensorUntilBlocked()
	{
		int speed = Settings.MOTOR_SONIC.getSpeed();
		Settings.MOTOR_SONIC.setSpeed(100);
		Settings.MOTOR_SONIC.rotate(200, true);
		int lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
		
		Delay.msDelay(200);
		while(Math.abs(lastTachoCount - Settings.MOTOR_SONIC.getTachoCount()) > 3)
		{
//			System.out.println("rotate");
			lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
			Delay.msDelay(200);
		}
		Settings.MOTOR_SONIC.stop();
		Settings.MOTOR_SONIC.setSpeed(speed);
	}
}