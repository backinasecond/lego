package de.gruppe2.motor;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

public class AllWheelPilot extends DifferentialPilot {

	private  RegulatedMotor leftMotor;
	private  RegulatedMotor rightMotor;
	
	public AllWheelPilot(double wheelDiameter, double trackWidth,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		super(wheelDiameter, trackWidth, leftMotor, rightMotor);
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}
	
	public void rotate(double angle) {
		this.leftMotor.setSpeed(20);
		this.rightMotor.setSpeed(20);
		
		super.rotate(angle * 1.5);
		/*if (angle > 0) {
			super.rotate(angle * 2.4);
		} else {
			super.rotate(angle * 2.6);
		}*/
	}

}
