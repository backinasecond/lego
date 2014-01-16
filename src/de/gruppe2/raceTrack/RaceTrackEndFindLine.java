package de.gruppe2.raceTrack;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class RaceTrackEndFindLine implements Behavior {

	private int wantedLightValue = Settings.LIGHT_LINE_DEFAULT;

	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		if (!suppressed) {

			int currentLightValue = Settings.LIGHT_SENSOR.getNormalizedLightValue();

			if (Math.abs(currentLightValue - wantedLightValue) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
				suppressed = true;
				return true;
			} 
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void action() {
		suppressed = false;

		if (!suppressed) {
			switch (wantedLightValue) {
			case Settings.LIGHT_LINE_DEFAULT:
				Settings.RACE_TRACK_END = true;
			}
		}
	}

	@Override
	public void suppress() {
		// Actually there is nothing to suppress, but for completeness
		suppressed = true;
	}
}