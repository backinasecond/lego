package de.gruppe2.shootingRange;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;

public class ShootingRangeHandler implements Behavior, ShootingRangeListener {

	private boolean suppressed = false;
	private boolean orientationLineFound = false;
	private ShootingRangeControl src;
	private boolean connected = false;
	private boolean success = false;
	private boolean alreadyShot = false;
	private int angle;

	public ShootingRangeHandler() {
		this.src = new ShootingRangeControl(this); 
	}
	
	@Override
	public boolean takeControl() {
		return !success;
	}

	@Override
	public void action() {
		suppressed = false;

		orientate();

		while (!connected) {
			System.out.println("TRY TO CONNECT");
			connected = src.connect();
			System.out.println(connected);
		}

		
		if (connected && !alreadyShot) {
			alreadyShot = true;
			shoot();
		}
	}
	
	private void shoot() {
		int dis = Settings.SONIC_SENSOR.getDistance();
		System.out.println(dis);
		if (dis > 70) {
			angle=100;
		} else if (dis  > 60) {
			angle=95;
		} else if (dis > 50){
			angle=85;
		} else {
			angle=75;
		}
		src.shoot(angle);
	}

	private void orientate() {
		if (!orientationLineFound) {
			Settings.PILOT.setTravelSpeed(60);
			Settings.PILOT.forward();
			while (!suppressed && Settings.PILOT.isMoving()) {
				if (Math.abs(Settings.LIGHT_SENSOR.getNormalizedLightValue() - Settings.LIGHT_LINE_DEFAULT) < Settings.COLOR_DIFFERENCE_THRESHOLD) {
					Settings.PILOT.stop();
					orientationLineFound = true;
					break;
				}
			}
			Settings.PILOT.rotate(-100, false);
		}
	}
	
	@Override
	public void suppress() {
		suppressed = true;
	}

	@Override
	public void shootSuccess() {
		success = true;
		System.out.println("SHOOT SUCCESS");
		Settings.PILOT.rotate(210);
		Settings.PILOT.travel(100);
		src.disconnect();
	}

	@Override
	public void shootFail() {
		System.out.println("SHOOT FAIL");
		angle+=10;
		shoot();
	}

	@Override
	public void shootInvalidAngle() {
		System.out.println("INVALID ANGLE");
	}

	@Override
	public void error(String message) {
		System.out.println("FUCK");
		src.disconnect();
	}
	
	public void reset() {
		connected = false;
		orientationLineFound = false;
		success = false;
		alreadyShot = false;
	}
}