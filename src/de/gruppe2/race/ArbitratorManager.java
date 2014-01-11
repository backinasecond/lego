package de.gruppe2.race;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.barcode.DriveForward;
import de.gruppe2.barcode.ReadCodes;
import de.gruppe2.bridgeFollow.BridgeBefore;
import de.gruppe2.bridgeFollow.BridgeFollow;
import de.gruppe2.bridgeFollow.BridgeStart;
import de.gruppe2.maze.MazeWallFollowBehaviour;
import de.gruppe2.maze.MazeWallHitBehaviour;
import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.util.LightDetectionBehaviour;

/**
 * This class manages the different arbitrators for all the different levels.
 */
public class ArbitratorManager {
	private CustomArbitrator arbitrator;

	/**
	 * Read barcode behavior (also at start)
	 */
	private final static Behavior BARCODE_DRIVE_FORWARD = new DriveForward();
	private final static Behavior BARCODE_READ_CODE = new ReadCodes();
	private final static Behavior[] BARDCODE_READ_BEHAVIOURS = { BARCODE_DRIVE_FORWARD, BARCODE_READ_CODE };

	/**
	 * Bridge behavior.
	 */
	private final static Behavior BRIDGE_BEFORE = new BridgeBefore();
	private final static Behavior BRIDGE_START = new BridgeStart();
	private final static Behavior BRIDGE_FOLLOW = new BridgeFollow();
	private final static Behavior BRIDGE_END = new LightDetectionBehaviour(Settings.LIGHT_BRIDGE_DEFAULT);
	private final static Behavior[] BRIDGE_BEHAVIOURS = { BRIDGE_START, BRIDGE_FOLLOW, BRIDGE_END, BRIDGE_BEFORE };

	/**
	 * Maze behavior.
	 */
    private final static Behavior MAZE_WALL_FOLLOW = new MazeWallFollowBehaviour();
	private final static Behavior MAZE_WALL_HIT = new MazeWallHitBehaviour();
    private final static Behavior MAZE_LINE_DETECTION = new LightDetectionBehaviour(Settings.LIGHT_LINE_DEFAULT);
    private final static Behavior[] MAZE_SOLVER_BEHAVIOURS = { MAZE_WALL_FOLLOW, MAZE_WALL_HIT, MAZE_LINE_DETECTION };

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
	 * Follow tape behavior.
	 */
	/*
	 * private Behavior t1 = new TapeFollow();
	 * private Behavior t2 = new TapeGapFound();
	 * private Behavior t3 = new TapeObstacleFound();
	 * private Behavior t4 = new ReadCodes();
	 * private Behavior t5 = new SensorHeadPosition();
	 * // private Behavior t6 = new MotorAStall();
	 * private Behavior[] tapeBehavior = { t1, t2, t3, t4, t5 };
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
		changeState(Settings.FIRST_LEVEL);
	}

	/**
	 * Changes the current arbitrator, according to the given {@code RobotState}.
	 * 
	 * @param state Given {@code RobotState} to change arbitrator
	 */
	public void changeState(RobotState state) {
		if (state != null && state != RobotState.START) {
			this.arbitrator.stop();
		}
		System.out.println(state.toString() + " mode selected");
		
		switch (state) {
		case START:
			Settings.PILOT.stop();
			changeState(RobotState.BARCODE);
			break;
		case BARCODE:
			Settings.PILOT.stop();
			this.arbitrator = new CustomArbitrator(BARDCODE_READ_BEHAVIOURS);
			break;
		case RELOCATE:
			Motor.A.removeListener();
			Motor.B.removeListener();
			Motor.C.removeListener();
			Settings.PILOT.stop();
			System.out.println("Relocating. Press ENTER to continue.");
			Button.waitForAnyPress();
			changeState(RobotState.BARCODE);
			break;
		case BRIDGE:
			Settings.BRIDGE_STATE = BridgeState.START;
			this.arbitrator = new CustomArbitrator(BRIDGE_BEHAVIOURS);
			break;
		case MAZE:
			Settings.AT_MAZE = true;
			CalibrateSonic.calibrateHorizontally();
			this.arbitrator = new CustomArbitrator(MAZE_SOLVER_BEHAVIOURS);
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
		System.out.println(state);
		if (state != RobotState.RELOCATE) {
			Thread thread = new Thread(this.arbitrator);
			thread.start();
		}
	}
}
