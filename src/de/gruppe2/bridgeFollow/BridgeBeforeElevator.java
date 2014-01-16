package de.gruppe2.bridgeFollow;

import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class BridgeBeforeElevator implements Behavior {

	@Override
	public boolean takeControl() {
		return Settings.BRIDGE_STATE == BridgeState.BEFORE_ELEVATOR;
	}

	@Override
	public void action() {
		// Position robot on center before elevator
		/*Settings.PILOT.travel(50);
		Settings.PILOT.rotate(-70);
		Settings.PILOT.travel(130);
		Settings.PILOT.rotate(80);
		*/
		
		Settings.PILOT.rotate(-80, true);
		while(Settings.SONIC_SENSOR.getDistance() > Settings.BRIDGE_HEIGHT_THRESHOLD) {
			Thread.yield();
		}
		Settings.PILOT.stop();
		
		Settings.PILOT.travel(50);
		Settings.PILOT.rotate(-90);
		Settings.PILOT.travel(110);
		Settings.PILOT.rotate(90);
		
		// Wait for blue color to move
		boolean isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
		if(isBlue) {
			System.out.println("Wait for blue 1");
			while(isBlue) {
				isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
				Thread.yield();
			}
		}
		System.out.println("Wait for blue 2");
		Delay.msDelay(3000);
		while(!isBlue) {
			isBlue = Settings.LIGHT_SENSOR.getNormalizedLightValue() > 500;
		}
		Delay.msDelay(2000);
		
		System.out.println("go");
		Settings.PILOT.forward();
		while(!Settings.TOUCH_L.isPressed()) {
			Thread.yield();
		}
		Settings.PILOT.stop();
		Settings.PILOT.travel(-120);
		
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < 11000) {
			Thread.yield();
		}
		
		Settings.PILOT.travel(40);
		Settings.BRIDGE_STATE = BridgeState.END;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
