package de.gruppe2.Tests;

import de.gruppe2.Settings;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class DriveTests implements FeatureListener, MoveListener {

	static DifferentialPilot pilot;
	int MAX_DETECT = 10;

	public static void main(String[] args) {
		System.out.println("Starting Wrath!");

		pilot = Settings.PILOT;

		pilot.setRotateSpeed(60);
//		pilot.rotateLeft();
		pilot.rotate(180);
//		while (Button.ENTER.isUp()) {
//			if (Button.LEFT.isDown()) {
//				System.out.println("l " + pilot.getAngleIncrement());
//				break;
//			}
//		}
//		pilot.setRotateSpeed(45);
//		pilot.rotateRight();
//		while (Button.ENTER.isUp()) {
//			if (Button.RIGHT.isDown()) {
//				System.out.println("r " + pilot.getAngleIncrement());
//				break;
//			}
//		}
//		pilot.stop();
		Button.waitForAnyPress();
		// pilot.addMoveListener(this);

		// pilot.setTravelSpeed(100);
		// pilot.travel(300);
		// pilot.setRotateSpeed(45);
		// // pilot.rotateLeft();
		// pilot.rotate(250);
		// Button.waitForAnyPress();

		// UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		// RangeFeatureDetector rangeDetector = new RangeFeatureDetector(us,
		// MAX_DETECT, 100);
		// rangeDetector.addListener(this);
		// pilot.forward();
		// Button.ENTER.waitForPressAndRelease();
	}

	@Override
	public void featureDetected(Feature feature, FeatureDetector detector) {
		// int range = (int) feature.getRangeReading().getRange();
		// System.out.println("Range:" + range);
		//
		// pilot.travel(-300);
		// pilot.forward();
	}

	@Override
	public void moveStarted(Move event, MoveProvider mp) {
		// TODO Auto-generated method stub
	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		// TODO Auto-generated method stub
	}
}
