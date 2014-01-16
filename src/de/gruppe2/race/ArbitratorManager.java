package de.gruppe2.race;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.barcode.BarcodeReader;
import de.gruppe2.barcode.DriveForward;
import de.gruppe2.bridgeFollow.BridgeBefore;
import de.gruppe2.bridgeFollow.BridgeFollow;
import de.gruppe2.bridgeFollow.BridgeStart;
import de.gruppe2.lineFollow.LineFollow;
import de.gruppe2.maze.MazeWallFollowBehaviour;
import de.gruppe2.maze.MazeWallHitBehaviour;
import de.gruppe2.raceTrack.RaceTrackEnd;
import de.gruppe2.raceTrack.RaceTrackEndFindLine;
import de.gruppe2.raceTrack.RaceTrackFollowBehaviour;
import de.gruppe2.raceTrack.RaceTrackHitBehaviour;
import de.gruppe2.symbol.SymbolFollow;
import de.gruppe2.turntable.TurnTurntableBehaviour;
import de.gruppe2.turntable.WallHitBehaviour;
import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.util.LightDetectionBehaviour;
import de.gruppe2.util.LightThresholdBehavior;

/**
 * This class manages the different arbitrators for all the different levels.
 */
public class ArbitratorManager {
	private static CustomArbitrator arbitrator;
	private static Thread thread;

	
	/**
	 * Read barcode behavior (also at start)
	 */
	private final static Behavior BARCODE_DRIVE_FORWARD = new DriveForward();
	private final static BarcodeReader BARCODE_READ_CODE = new BarcodeReader();
	private final static Behavior[] BARCODE_READ_BEHAVIOURS = { BARCODE_DRIVE_FORWARD, BARCODE_READ_CODE };
	
	/**
	 * Bridge behavior.
	 */
	private final static Behavior BRIDGE_BEFORE = new BridgeBefore();
	private final static Behavior BRIDGE_START = new BridgeStart();
	private final static Behavior BRIDGE_FOLLOW = new BridgeFollow();
	private final static Behavior BRIDGE_END = new LightThresholdBehavior(Settings.LIGHT_BLACK_DEFAULT);
	private final static Behavior[] BRIDGE_BEHAVIOURS = { BRIDGE_START, BRIDGE_FOLLOW, BRIDGE_END, BRIDGE_BEFORE };

	/**
	 * Maze behavior.
	 */
    private final static Behavior MAZE_WALL_FOLLOW = new MazeWallFollowBehaviour();
	private final static Behavior MAZE_WALL_HIT = new MazeWallHitBehaviour();
    private final static Behavior MAZE_LINE_DETECTION = new LightDetectionBehaviour(Settings.LIGHT_LINE_DEFAULT);
    private final static Behavior[] MAZE_BEHAVIOURS = { MAZE_WALL_FOLLOW, MAZE_WALL_HIT, MAZE_LINE_DETECTION };

	/**
	 * Race track behavior.
	 */
	private final static Behavior RACE_TRACK_FOLLOW_BEHAVIOR = new RaceTrackFollowBehaviour(); 
	private final static Behavior RACE_TRACK_HIT_BEHAVIOR = new RaceTrackHitBehaviour();
	private final static Behavior RACE_TRACK_END = new RaceTrackEnd();
	private final static Behavior RACE_TRACK_LINE_DETECTION = new RaceTrackEndFindLine();
	private final static Behavior[] RACE_TRACK_BEHAVIORS = { RACE_TRACK_FOLLOW_BEHAVIOR, RACE_TRACK_HIT_BEHAVIOR, RACE_TRACK_LINE_DETECTION, RACE_TRACK_END };
    
	/**
	 * Follow line behavior.
	 */
	private final static Behavior LINE_FOLLOW = new LineFollow();
	private final static Behavior[] LINE_BEHAVIOURS = { LINE_FOLLOW };
	
	/**
	 * Symbol recognizer behavior.
	 */
	private final static Behavior SYMBOL_RECOGNIZER = new SymbolFollow();
	private final static Behavior SYMBOL_LINE_FOLLOW = new LineFollow(RobotState.TURNTABLE);
	private final static Behavior[] SYMBOL_BEHAVIOURS = { SYMBOL_LINE_FOLLOW, SYMBOL_RECOGNIZER };
	
	
	/**
	 * Turntable  behavior.
	 */
    private final static Behavior TURNTABLE_DRIVE_FORWARD = new DriveForward();
    private final static Behavior TURNTABLE_WALL_HIT = new WallHitBehaviour();
    private final static Behavior TURNTABLE_TURN = new TurnTurntableBehaviour();
    
	private final static Behavior[] TURNTABLE_BEHAVIOURS = { TURNTABLE_TURN, TURNTABLE_DRIVE_FORWARD, TURNTABLE_WALL_HIT};


	/**
	 * Bluetooth Gate behavior.
	 */
	/*
	 * private Behavior bt1 = new LabyrinthGate();
	 * private Behavior bt2 = new ReadCodes();
	 * private Behavior bt3 = new SensorHeadPosition();
	 * private Behavior[] btgateBehavior = { bt1, bt2, bt3 };
	 */

	/**
	 * Turntable behavior.
	 */
	/*
	 * private Behavior tt1 = new TapeFollow();
	 * private Behavior tt2 = new TurntablePark();
	 * private Behavior tt3 = new TurntableRotate();
	 * private Behavior tt4 = new TurntableConnect();
	 * private Behavior tt5 = new TurntableBegin();
	 * private Behavior tt6 = new ReadCodes();
	 * private Behavior tt7 = new SensorHeadPosition();
	 * private Behavior[] turnTableArray = { tt1, tt2, tt3, tt4, tt5, tt6, tt7 };
	 */


	/**
	 * End Opponent behavior.
	 */
	/*
	 * private Behavior e1 = new RaceEnd();
	 * private Behavior e2 = new SensorHeadPosition();
	 * private Behavior[] endBehavior = { e1, e2 };
	 */

	/**
	 * Instantiate an {@code ArbitratorManager}
	 */
	public ArbitratorManager() {
		Settings.isRunning = true;
	}
	
	public void startManager() {
		changeState(Settings.CURRENT_LEVEL);
	}

	/**
	 * Changes the current arbitrator, according to the given {@code RobotState}.
	 * 
	 * @param state Given {@code RobotState} to change arbitrator
	 */
	public void changeState(RobotState state) {
		if (state != null && state != RobotState.START && arbitrator != null) {
			System.out.println("ARBITRATOR STOPPED");
			arbitrator.stop();
		}
		System.out.println(state.toString() + " mode selected");
		
		// This variable is necessary to not start a new thread when the method is called recursivly
		boolean startThread = true;
		
		switch (state) {
		case START:
			startThread = false;
			changeState(RobotState.BARCODE);
			break;
		case BARCODE:
			BARCODE_READ_CODE.reset();
			arbitrator = new CustomArbitrator(BARCODE_READ_BEHAVIOURS);
			break;
		case RACE_TRACK:
//			CalibrateSonic.calibrateHorizontally(); // do it manually to save time at beginning
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 1.0);
	        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
			arbitrator = new CustomArbitrator(RACE_TRACK_BEHAVIORS);
			break;
		case LINE_FOLLOWER:
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * Settings.TAPE_FOLLOW_SPEED);
			Settings.PILOT.setRotateSpeed(Settings.PILOT.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);
			arbitrator = new CustomArbitrator(LINE_BEHAVIOURS);
			break;
		case BRIDGE:
			Settings.BRIDGE_STATE = BridgeState.START;
			arbitrator = new CustomArbitrator(BRIDGE_BEHAVIOURS);
			break;
		case MAZE:
			CalibrateSonic.calibrateHorizontally();
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.80);
	        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
			arbitrator = new CustomArbitrator(MAZE_BEHAVIOURS);
			break;
		case SYMBOL_RECOGNIZER:
			arbitrator = new CustomArbitrator(SYMBOL_BEHAVIOURS);
			break;
		case TURNTABLE:
			arbitrator = new CustomArbitrator(TURNTABLE_BEHAVIOURS);
			break;
		case SHOOTING_RANGE:
			break;
		case RELOCATE:
			Motor.A.removeListener();
			Motor.B.removeListener();
			Motor.C.removeListener();
			System.out.println("Relocating. Press ENTER to continue.");
			Button.waitForAnyPress();
			
			startThread = false;
			changeState(RobotState.BARCODE);
			break;
/*
		case BT_GATE:
			pilot.setTravelSpeed(pilot.getMaxTravelSpeed() / 2);
			pilot.setRotateSpeed(pilot.getMaxRotateSpeed() / 4);
			Motor.A.setSpeed(Motor.A.getMaxSpeed() / 5);
			Settings.motorAAngle = Settings.SENSOR_FRONT;

			this.arbitrator = new CustomArbitrator(this.btgateBehavior);
			break;
		case TURNTABLE:
			Settings.bluetooth = false;
			Settings.readState = false;
			Settings.motorAAngle = Settings.SENSOR_FRONT;

			this.arbitrator = new CustomArbitrator(this.turnTableArray);
			break;
		case LINE_OBSTACLE:
			double speed = pilot.getMaxTravelSpeed() * Settings.tapeFollowSpeed;
			pilot.setTravelSpeed(speed);
			pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
			Settings.motorAAngle = Settings.SENSOR_FRONT;

			pilot.rotate(-30);
			pilot.travel(30, false);

			this.arbitrator = new CustomArbitrator(this.tapeBehavior);
			break;
		case END_OPPONENT:
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed());
			pilot.setRotateSpeed(pilot.getMaxRotateSpeed() / 4);
			Motor.A.setSpeed(Motor.A.getMaxSpeed() / 5);
			Settings.motorAAngle = Settings.SENSOR_FRONT;
			this.arbitrator = new CustomArbitrator(this.endBehavior);
			break;
			*/
		default:
			System.out.println("No valid arbitrator selected!");
			break;
		}

		// update the thread to run the selected arbitrator
		if (state != RobotState.RELOCATE && startThread) {
			thread = new Thread(arbitrator);
			thread.start();
		}
		System.out.println(state.toString() + " finished.");
	}
}
