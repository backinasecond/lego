package de.gruppe2;

import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class SonicTest {

	public static void main(String[] args) {
		UltrasonicSensor sonic = Settings.SONIC_SENSOR;

		while (Button.ENTER.isUp()) {
			System.out.println(sonic.getDistance());
			Delay.msDelay(200);
		}
	}
}
