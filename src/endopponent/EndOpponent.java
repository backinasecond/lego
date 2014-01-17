package endopponent;

import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class EndOpponent implements Behavior {
	private boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		
		playWinnerSong();
		Delay.msDelay(2000);
		
		Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed());
		Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed());
		
		Settings.PILOT.steer(-10);
		
		while(!suppressed && !Settings.TOUCH_L.isPressed()) {
			Thread.yield();
		}
		
		Settings.ARBITRATOR_MANAGER.changeState(RobotState.MAZE);		
	}

	@Override
	public void suppress() {
		suppressed = true;		
	}
	
	public void playWinnerSong() {
		short[] note = { 500, 3000 };
		for(int i = 0; i < note.length; i += 2) {
			int n = note[i];
			if (n != 0) {
				Sound.playTone(n, note[i+1], 100);
			}
			try {
				Thread.sleep(note[i+1]);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}

	}

}
