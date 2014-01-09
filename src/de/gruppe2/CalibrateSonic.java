package de.gruppe2;

/**
 * CalibrateSonic 
 * Sonic sensor should look horizontally when program on nxt is started.
 */
public class CalibrateSonic {
	public static void calibrateHorizontally()
	{
		int angle = Settings.MOTOR_SONIC.getTachoCount();
		Settings.MOTOR_SONIC.rotate(-angle, false);
	}
	
	public static void calibrateVertically()
	{
		int angle = Settings.MOTOR_SONIC.getTachoCount();
		Settings.MOTOR_SONIC.rotate(70 - angle);
	}
}
