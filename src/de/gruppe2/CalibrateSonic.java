package de.gruppe2;

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
		Settings.MOTOR_SONIC.rotate(-120, true);
	}
	
	/**
	 * Rotate sensor in order to use it for measuring the height.
	 */
	public static void calibrateVertically()
	{
		rotateSensorUntilBlocked();
		Settings.MOTOR_SONIC.rotate(-50, true);
	}
	
	/**
	 * Move sensor down until it is blocked by the wheel.
	 */
	public static void rotateSensorUntilBlocked()
	{
		int speed = Settings.MOTOR_SONIC.getSpeed();
		Settings.MOTOR_SONIC.setSpeed(20);
		Settings.MOTOR_SONIC.rotate(200, true);
		int lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
		
		Delay.msDelay(200);
		while(Math.abs(lastTachoCount - Settings.MOTOR_SONIC.getTachoCount()) > 3)
		{
			lastTachoCount = Settings.MOTOR_SONIC.getTachoCount();
			Delay.msDelay(200);
		}
		
		Settings.MOTOR_SONIC.stop();
		Settings.MOTOR_SONIC.setSpeed(speed);
	}
}
