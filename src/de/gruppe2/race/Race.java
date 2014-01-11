package de.gruppe2.race;

import de.gruppe2.Settings;
/**
 * This is the main class. It initializes stuff and starts the main arbitrator
 * 
 * @author Curiosity
 */
public class Race {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Settings.ARBITRATOR_MANAGER = new ArbitratorManager();
		Settings.ARBITRATOR_MANAGER.startManager();
	}
}
