package Barcode;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class Start {

	public static void main(String[] args) {
		LCD.drawString("Program 2", 0, 0);
		Button.waitForAnyPress();
		
		LightSensor l = new LightSensor(SensorPort.S2);
		l.setFloodlight(100);
		
		
		Delay.msDelay(2000);
		
		while(Button.ESCAPE.isUp()) {
			if(Button.LEFT.isDown()) {
				l.calibrateHigh();
				System.out.println("calibrated high: " + l.getHigh());
				Delay.msDelay(2000);
			} else if (Button.RIGHT.isDown()) {
				l.calibrateLow();
				System.out.println("calibrated low: " + l.getLow());
				Delay.msDelay(2000);
			} else {
				System.out.println("Licht" + l.getLightValue());
			}
		}
		
	}

}
