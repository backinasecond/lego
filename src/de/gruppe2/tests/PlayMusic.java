package de.gruppe2.tests;

import lejos.nxt.Sound;

public class PlayMusic {

	private static final int durationQuarterNote = 500;
	private static final int durationQuarterNotePoint = 750;
	private static final int durationEighthNote = 250;
	
	/*private static final short[] note = {587, durationQuarterNotePoint,
										 523, durationQuarterNote,
										 587, durationEigthNote,
										 523, };
	 */
	
	private static final short[] weAreTheChampions = {698, durationQuarterNote * 2,
										 659, durationEighthNote,
										 698, durationEighthNote,
										 659, durationQuarterNote,
										 523, durationQuarterNotePoint,
										 440, durationEighthNote,
										 587, durationQuarterNote,
										 440, durationQuarterNote * 2,
										 0, durationEighthNote * 4,
										 523, durationEighthNote,
										 698, durationQuarterNote * 2,
										 784, durationEighthNote,
										 880, durationEighthNote,
										 1046, durationQuarterNote,
										 880, durationQuarterNote,
										 587, durationEighthNote,
										 659, durationEighthNote,
										 587, durationQuarterNote * 2,
										 0, durationQuarterNote * 2,
										 587, durationQuarterNotePoint,
										 523, durationQuarterNote,
										 587, durationEighthNote,
										 523, durationQuarterNotePoint,
										 466, durationQuarterNotePoint,
										 932, durationQuarterNote,
										 880, durationQuarterNote,
										 932, durationEighthNote,
										 880, durationQuarterNotePoint,
										 784, durationQuarterNotePoint,
										 880, durationQuarterNotePoint,
										 698, durationQuarterNote,
										 932, durationEighthNote,
										 880, durationQuarterNotePoint,
										 698, durationQuarterNote,
										 932, durationEighthNote,
										 830, durationQuarterNote,
										 698, durationQuarterNote,
										 932, durationEighthNote,
										 830, durationQuarterNotePoint,
										 698, durationQuarterNotePoint,
										 622, durationEighthNote,
										 523, durationEighthNote,
										 698, durationQuarterNotePoint * 2};
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		short[] note = weAreTheChampions;
		for(int i = 0; i < note.length; i += 2) {
			int n = note[i];
			if (n != 0) {
				Sound.playTone(n, note[i+1], 100);
			}
			Thread.sleep(note[i+1]);
		}

	}

}
