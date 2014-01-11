package de.gruppe2.tests;

import de.gruppe2.Settings;
import lejos.nxt.Button;

public class LightTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		while(Button.ENTER.isUp())
		{
			System.out.println("" + Settings.LIGHT_SENSOR.getNormalizedLightValue());
		}

	}

}
