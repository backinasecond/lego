package de.gruppe2.tests;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.util.Delay;
import de.gruppe2.Settings;

public class ReadColorTest {

	static LightSensor light = Settings.LIGHT_SENSOR;

	public static void main(String[] args) {
		System.out.println("Lichtsensor Test");
		System.out.println("Dr�cke Enter");
		Button.ENTER.waitForPressAndRelease();

		while (true) {
			Delay.msDelay(200);
			System.out.println(light.getNormalizedLightValue());
		}
	}

}
