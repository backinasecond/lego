package de.gruppe2.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.gruppe2.Settings;
import lejos.nxt.Button;
import lejos.util.Delay;

public class LightTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> values = new ArrayList<Integer>();
		
		Settings.PILOT.setTravelSpeed(20);
		System.out.println("Recording values");
		//Settings.PILOT.travel(40, true);
		while(Button.ENTER.isUp())
		{
			values.add(new Integer(Settings.LIGHT_SENSOR.getNormalizedLightValue()));
			Delay.msDelay(20);
		}
		
		int sum = 0;
		int min = 2000;
		int max = 0;
		
		System.out.println("Sorting " + values.size() + " values");
		Object[] sortedValues = values.toArray();
		Arrays.sort(sortedValues);
		
		System.out.println("Evaluating values");
		int i = 1;
		for(Object e : sortedValues) {
			if(i > 50 && i <= sortedValues.length - 50)
			{
				Integer j = (Integer) e;
				sum += j;
				if(j < min) min = j;
				if(j > max) max = j;	
			}
			i++;
		}
		
		if(values.size() < 100) {
			System.out.println("Too few values recorded (" + values.size() + ")");
			while(Button.ENTER.isUp()) {
				Thread.yield();
			}
			return;
		}
		
		System.out.println("Recorded " + values.size() + "  values:");
		System.out.println("Mean value: " + sum/(sortedValues.length - 100));
		System.out.println("Min value: " + min);
		System.out.println("Max value: " + max);
		
		while(Button.ENTER.isUp()) {
			Thread.yield();
		}
	}

}
