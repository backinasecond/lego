//package de.gruppe2.barcode;
//
//import lejos.nxt.LightSensor;
//import lejos.nxt.SensorPort;
//import lejos.util.Delay;
//import lejos.util.Timer;
//import lejos.util.TimerListener;
//
//public class Barcode {
//	
//	private final static SensorPort LIGHT_SENSOR_PORT = SensorPort.S1;
//	private final int LOW_THRESHOLD = 20;
//	private final int HIGH_THRESHOLD = 60;
//	
//	// State where light sensor is not over the line
//	private boolean notOnLine = true;
//	
//	private final int BARCODE_END_THRESHOLD = 10;
//	private int 
//	
//	private LightSensor l;
//	
//	public Barcode() {
//		this.l = new LightSensor(LIGHT_SENSOR_PORT);
//		this.l.setFloodlight(true);
//	}
//	
//	public int recognizeBarcode() {
//		int lineCount = 0;
//		
//		TimerListener el = new TimerListener() {			
//			@Override
//			public void timedOut() {
//				int currentLightValue = l.getLightValue();
//				
//				if (notOnLine) {
//					if (currentLightValue < LOW_THRESHOLD) {
//						
//					}
//					if (count > 10)
//				}
//				
//			}
//		};
//		Timer readTimer = new Timer();
//		
//		
//
//		System.out.println("LichtNorm: " + Integer.toString(l.readNormalizedValue()));
//		System.out.println("Value: " + Integer.toString(l.readValue()));
//		System.out.println("Light Value: " + Integer.toString());
//		
//		Delay.msDelay(500);
//		
//		return 0;
//	}
//	
//	
//	public void calibrateLow() {
//		l.calibrateLow();
//		System.out.println("calibrated low: " + l.getLow());
//	}
//	
//	public void calibrateHigh() {
//		l.calibrateHigh();
//		System.out.println("calibrated high: " + l.getHigh());
//	}
//	
//	
//}
