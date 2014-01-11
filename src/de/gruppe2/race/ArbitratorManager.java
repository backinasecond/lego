package de.gruppe2.race;

import de.gruppe2.util.CalibrateSonic;
import de.gruppe2.RobotState;
import de.gruppe2.Settings;
import de.gruppe2.Settings.BridgeState;
import de.gruppe2.barcode.DriveForward;
import de.gruppe2.barcode.ReadCodes;
import de.gruppe2.bridgeFollow.BridgeBefore;
import de.gruppe2.bridgeFollow.BridgeEnd;
import de.gruppe2.bridgeFollow.BridgeFollow;
import de.gruppe2.bridgeFollow.BridgeStart;
import de.gruppe2.maze.MazeWallFollowBehaviour;
import de.gruppe2.maze.MazeWallHitBehaviour;
import de.gruppe2.util.LightDetectionBehaviour;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class manages the different arbitrators for all the different levels.
 */
public class ArbitratorManager {
	private Arbitrator arbitrator;

	/**
	 * Read barcode behavior (also at start)
	 */
	private final static Behavior BARCODE_DRIVE_FORWARD = new DriveForward();
	private final static Behavior BARCODE_READ_CODE = new ReadCodes();
	private final static Behavior[] BARDCODE_READ_BEHAVIOURS = { BARCODE_DRIVE_FORWARD, BARCODE_READ_CODE };

	/**
	 * Bridge behavior.
	 */
	/*
	 * private Behavior b0 = new DriveForward();
	 * private Behavior b1 = new HitWallBeforeBridge();
	 * private Behavior b2 = new DriveUntilAbyss();
	 * private Behavior b3 = new AbyssDetected();
	 * private Behavior b4 = new ReachedEndOfBridge();
	 * private Behavior b5 = new ReadCodes();
	 * private Behavior b6 = new SensorHeadPosition();
	 * private Behavior[] bridgeBehavior = { b0, b1, b2, b3, b4, b5, b6 };
	 */
	private Behavior b0 = new BridgeBefore();
	private Behavior b1 = new BridgeStart();
	private Behavior b2 = new BridgeFollow();
	private Behavior b3 = new LightDetectionBehaviour(Settings.LIGHT_BRIDGE_DEFAULT);
	private Behavior[] bridgeBehavior = { b0, b1, b2, b3 };

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
		if (state == null) {
			System.out.println("null state was passed");

			return;
		}
		System.out.println(state.toString() + " mode selected");

		switch (state) {
		case START:
			System.out.println("Race start.");
			Settings.PILOT.stop();
			this.arbitrator = new Arbitrator(this.BARDCODE_READ_BEHAVIOURS);
			break;
		case BARCODE:
			System.out.println("Read Barcode");
			Settings.PILOT.stop();
			this.arbitrator = new Arbitrator(this.BARDCODE_READ_BEHAVIOURS);
			break;
		case RELOCATE:
			Motor.A.removeListener();
			Motor.B.removeListener();
			Motor.C.removeListener();
			Settings.PILOT.stop();
			System.out.println("Relocating. Press ENTER to continue.");
			Button.waitForAnyPress();
			this.arbitrator = new Arbitrator(this.BARDCODE_READ_BEHAVIOURS);
			break;
		case BRIDGE:
			System.out.println("Drive Bridge");
			Settings.BRIDGE_STATE = BridgeState.START;
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.60);
            Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
            CalibrateSonic.calibrateVertically();

			this.arbitrator = new Arbitrator(this.bridgeBehavior);
			break;
		case MAZE:
			System.out.println("Solve Maze");
			Settings.AT_MAZE = true;
			Settings.PILOT.setTravelSpeed(Settings.PILOT.getMaxTravelSpeed() * 0.60);
            Settings.PILOT.setRotateSpeed(Settings.PILOT.getMaxRotateSpeed() / 5);
			this.arbitrator = new Arbitrator(this.MAZE_SOLVER_BEHAVIOURS);
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

		this.arbitrator.start();
	}
}
