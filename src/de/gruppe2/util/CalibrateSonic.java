package de.gruppe2.util;

import de.gruppe2.Settings;
import lejos.util.Delay;

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
		Settings.MOTOR_SONIC.rotate(-143, false);
		Settings.MOTOR_SONIC.resetTachoCount();
		
	}
	
	/**
	 * Rotate sensor in order to use it for measuring the height.
	 */
	public static void calibrateVertically()
	{
		rotateSensorUntilBlocked();
		Settings.MOTOR_SONIC.rotate(-80, false);
		Settings.MOTOR_SONIC.resetTachoCount();
	}
	
	/**
	 * Move sensor down until it is blocked by the wheel.
	 */
	public static void rotateSensorUntilBlocked()
	{
		int speed = Settings.MOTOR_SONIC.getSpeed();
		Settings.MOTOR_SONIC.setSpeed(40);
		Settings.MOTOR_SONIC.rotate(200, true);
		int lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
		
		Delay.msDelay(200);
		while(Math.abs(lastTachoCount - Settings.MOTOR_SONIC.getTachoCount()) > 3)
		{
//			System.out.println("rotate");
			lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
			Delay.msDelay(200);
		}
		System.out.println("done");
		Settings.MOTOR_SONIC.stop();
		Settings.MOTOR_SONIC.setSpeed(speed);
	}
}