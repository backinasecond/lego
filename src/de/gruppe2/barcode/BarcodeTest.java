package de.gruppe2.barcode;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class BarcodeTest {

	public static void main(String[] args) {
		LCD.drawString("Barcode Scanner", 0, 0);
		//Button.waitForAnyPress();

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
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				barcode.recognizeBarcode();
			}

			public void buttonReleased(Button b) {
			}
		});
		
		while (!Button.ESCAPE.isDown()) {
			
		}
		
	}

}
