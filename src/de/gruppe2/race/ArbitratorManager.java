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
import de.gruppe2.bridgeFollow.BridgeBeforeElevator;
import de.gruppe2.bridgeFollow.BridgeFollow;
import de.gruppe2.bridgeFollow.BridgeStart;
import de.gruppe2.lineFollow.LineFollow;
import de.gruppe2.maze.MazeLightDetectionBehaviour;
import de.gruppe2.maze.MazeWallFollowBehaviour;
import de.gruppe2.maze.MazeWallHitBehaviour;
import de.gruppe2.raceTrack.RaceTrackEnd;
import de.gruppe2.raceTrack.RaceTrackEndLine;
import de.gruppe2.raceTrack.RaceTrackFollowBehaviour;
import de.gruppe2.raceTrack.RaceTrackHitBehaviour;
import de.gruppe2.shootingRange.ShootingRangeHandler;
import de.gruppe2.shootingRange.ShootingRangeLineFollow;
import de.gruppe2.symbol.SymbolFollow;
import de.gruppe2.turntable.TurnTableLineFollow;
import de.gruppe2.turntable.TurnTurntableBehaviour;
import de.gruppe2.turntable.WallHitBehaviour;
import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.util.FindLineBehavior;
import de.gruppe2.util.SteerForward;

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
	private final static Behavior BRIDGE_ELEVATOR = new BridgeBeforeElevator();
	//private final static Behavior BRIDGE_END = new LightThresholdBehavior(Settings.LIGHT_BLACK_DEFAULT);
	private final static Behavior[] BRIDGE_BEHAVIOURS = { BRIDGE_START, BRIDGE_FOLLOW, BRIDGE_ELEVATOR, BRIDGE_BEFORE };

	/**
	 * Maze behavior.
	 */
    private final static Behavior MAZE_WALL_FOLLOW = new MazeWallFollowBehaviour();
	private final static Behavior MAZE_WALL_HIT = new MazeWallHitBehaviour();
    private final static Behavior MAZE_LINE_DETECTION = new MazeLightDetectionBehaviour(Settings.LIGHT_LINE_DEFAULT);
    private final static Behavior[] MAZE_BEHAVIOURS = { MAZE_WALL_FOLLOW, MAZE_WALL_HIT, MAZE_LINE_DETECTION };

	/**
	 * Race track behavior.
	 */
	private final static Behavior RACE_TRACK_FOLLOW_BEHAVIOR = new RaceTrackFollowBehaviour(); 
	private final static Behavior RACE_TRACK_HIT_BEHAVIOR = new RaceTrackHitBehaviour();
	private final static Behavior RACE_TRACK_END = new RaceTrackEnd();
	private final static Behavior RACE_TRACK_LINE_DETECTION = new RaceTrackEndLine();
	private final static Behavior[] RACE_TRACK_BEHAVIORS = { RACE_TRACK_FOLLOW_BEHAVIOR, RACE_TRACK_HIT_BEHAVIOR, RACE_TRACK_LINE_DETECTION, RACE_TRACK_END };
    
	/**
	 * Follow line behavior.
	 */
	private final static Behavior LINE_FOLLOW = new LineFollow();
	private final static FindLineBehavior LINE_FIND_LINE = new FindLineBehavior();
	private final static Behavior[] LINE_BEHAVIOURS = { LINE_FOLLOW, LINE_FIND_LINE };
	
	/**
	 * Symbol recognizer behavior.
	 */
	private final static Behavior SYMBOL_RECOGNIZER = new SymbolFollow();
	private final static Behavior SYMBOL_LINE_FOLLOW = new LineFollow(RobotState.TURNTABLE);
	private final static FindLineBehavior SYMBOL_FIND_LINE = new FindLineBehavior();
	private final static Behavior[] SYMBOL_BEHAVIOURS = { SYMBOL_LINE_FOLLOW, SYMBOL_RECOGNIZER, SYMBOL_FIND_LINE };
	
	/**
	 * Test  behavior.
	 */
	private final static Behavior TEST_LINE_FOLLOW = new TurnTableLineFollow();
	private final static Behavior TEST_DRIVE_FORWARD = new DriveForward(320);
	private final static Behavior TEST_WALL_HIT = new WallHitBehaviour();
	private final static Behavior[] TEST_BEHAVIOURS = { TEST_DRIVE_FORWARD, TEST_LINE_FOLLOW, TEST_WALL_HIT};
	
	/**
	 * Turntable  behavior.
	 */
	private final static Behavior TURNTABLE_STEER_FORWARD = new SteerForward(320, -10);
    private final static Behavior TURNTABLE_TURN = new TurnTurntableBehaviour();
	private final static Behavior[] TURNTABLE_BEHAVIOURS = { TURNTABLE_STEER_FORWARD, TURNTABLE_TURN};

	
	/**
	 * ShootingRange Behavior.
	 */
	private final static ShootingRangeHandler SHOOTING_RANGE_HANDLER = new ShootingRangeHandler();
	private final static ShootingRangeLineFollow SHOOTING_RANGE_LINE = new ShootingRangeLineFollow(null);
	private final static ShootingRangeLineFollow SHOOTING_RANGE_LINE_2 = new ShootingRangeLineFollow(RobotState.BARCODE);
	private final static FindLineBehavior SHOOTING_RANGE_FIND_LINE = new FindLineBehavior();
	private final static FindLineBehavior SHOOTING_RANGE_FIND_LINE_2 = new FindLineBehavior();
	private final static Behavior[] SHOOTING_RANGE_BEHAVIORS = {SHOOTING_RANGE_LINE_2, SHOOTING_RANGE_FIND_LINE_2, SHOOTING_RANGE_HANDLER, SHOOTING_RANGE_LINE, SHOOTING_RANGE_FIND_LINE};
//	private final static Behavior[] SHOOTING_RANGE_BEHAVIORS = {SHOOTING_RANGE};
	

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
		
		Settings.PILOT.setTravelSpeed(0);
		Settings.PILOT.setRotateSpeed(0);
		Settings.MOTOR_SONIC.stop();
		
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
			// TODO: Move this to class
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 1.0);
	        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
			arbitrator = new CustomArbitrator(RACE_TRACK_BEHAVIORS);
			break;
		case LINE_FOLLOWER:
			LINE_FIND_LINE.reset();
			arbitrator = new CustomArbitrator(LINE_BEHAVIOURS);
			break;
		case BRIDGE:
			Settings.BRIDGE_STATE = BridgeState.START;
			arbitrator = new CustomArbitrator(BRIDGE_BEHAVIOURS);
			break;
		case MAZE:
			CalibrateSonic.calibrateHorizontally();
			// TODO: Move this to class
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.80);
	        Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
			arbitrator = new CustomArbitrator(MAZE_BEHAVIOURS);
			break;
		case SYMBOL_RECOGNIZER:
			SYMBOL_FIND_LINE.reset();
			Settings.PILOT.setRotateSpeed(Settings.PILOT.getRotateMaxSpeed() * Settings.TAPE_ROTATE_SPEED);
			arbitrator = new CustomArbitrator(SYMBOL_BEHAVIOURS);
			break;
		case TURNTABLE:
			arbitrator = new CustomArbitrator(TURNTABLE_BEHAVIOURS);
			break;
		case SHOOTING_RANGE:
			SHOOTING_RANGE_FIND_LINE.reset();
			SHOOTING_RANGE_FIND_LINE_2.reset();
			SHOOTING_RANGE_LINE.reset();
			SHOOTING_RANGE_LINE_2.reset();
			SHOOTING_RANGE_HANDLER.reset();
			CalibrateSonic.calibrateHorizontally();
			arbitrator = new CustomArbitrator(SHOOTING_RANGE_BEHAVIORS);
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
		case TEST:
			arbitrator = new CustomArbitrator(TEST_BEHAVIOURS);
			break;
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
