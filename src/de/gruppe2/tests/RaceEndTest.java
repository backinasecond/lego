package de.gruppe2.tests;

import de.gruppe2.Settings;
import de.gruppe2.raceTrack.RaceTrackEnd;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class RaceEndTest {

	public static void main(String[] args) {
		Behavior raceTrackEnd = new RaceTrackEnd();
		Settings.RACE_TRACK_END = true;
		Behavior[] b = {raceTrackEnd};
		Arbitrator a = new Arbitrator(b);
		a.start();
		
	}
}
