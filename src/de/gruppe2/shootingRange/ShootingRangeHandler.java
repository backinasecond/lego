package de.gruppe2.shootingRange;

import lejos.robotics.subsumption.Behavior;
import de.gruppe2.Settings;
import de.gruppe2.util.CalibrateSonic;

public class ShootingRangeHandler implements Behavior, ShootingRangeListener {

	private boolean suppressed = false;
	private boolean orientationLineFound = false;
	private ShootingRangeControl src;
	private boolean connected = false;
	private boolean success = false;
	private boolean alreadyShot = false;
	private int angle;
	private int dis;
	private int shot = 0;

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
			int tmp = 0;
			for (int i = 0; i < 5; i++) {
				tmp += Settings.SONIC_SENSOR.getDistance();
			}
			dis = tmp / 5;
			shoot();
		}
	}

	private void shoot() {
		System.out.println(dis);
		if (dis > 60) {
			angle = 125;
		} else if (dis > 50) {
			angle = 105;
		} else if (dis > 40) {
			angle = 95;
		} else if (dis > 35) {
			angle = 90;
		} else if (dis > 30) {
			angle = 85;
		} else {
			angle = 80;
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
			Settings.PILOT.rotate(-90, false);
			Settings.PILOT.travel(-110, false);
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
		getOut();
		src.disconnect();
	}

	@Override
	public void shootFail() {
		System.out.println("SHOOT FAIL");
		shot++;
		if (shot == 2) {
			success = true;
			getOut();
			src.disconnect();
		} else {
			angle += 15;
			src.shoot(angle);
		}
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
		shot = 0;
	}

	private void getOut() {
		CalibrateSonic.calibrateVertically();
		Settings.PILOT.rotate(205);
		Settings.PILOT.travel(100);
	}
}