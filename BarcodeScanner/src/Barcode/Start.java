package Barcode;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class Start {

	public static void main(String[] args) {
		// LCD.drawString("Program 2", 0, 0);
		// Button.waitForAnyPress();

		final Barcode barcode = new Barcode();

		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				barcode.calibrateLow();
				Delay.msDelay(2000);
			}

			public void buttonReleased(Button b) {
			}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				barcode.calibrateHigh();
				Delay.msDelay(2000);
			}

			public void buttonReleased(Button b) {
			}
		});

		while (Button.ESCAPE.isUp()) {
			barcode.recognizeBarcode();
		}

	}

}
