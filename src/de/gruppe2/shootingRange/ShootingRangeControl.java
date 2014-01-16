package de.gruppe2.shootingRange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * Use this class to connect to the NXT device from the shooting range.
 * Remember to pair with the device "Shooter" (PIN: 1234).
 * 
 * @author Malte
 *
 */
public class ShootingRangeControl {

	private final static int BTCOMMAND_SHOOT_FAIL    = 0;
	private final static int BTCOMMAND_SHOOT_SUCCESS = 1;
	private final static int BTCOMMAND_INVALID_ANGLE = 2;
	
	private ShootingRangeListener listener;
	private NXTConnection connection = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	
	public ShootingRangeControl(ShootingRangeListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Connects to the shooting range via Bluetooth.
	 * 
	 * @return true if connection success
	 */
	public boolean connect() {
		RemoteDevice server = Bluetooth.getKnownDevice("Shooter");
		NXTConnection connection = Bluetooth.connect(server);
		
		if (connection != null) {
			in = connection.openDataInputStream();
			out = connection.openDataOutputStream();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Disconnects from the shooting range device.
	 * 
	 * @return true if disconnect success
	 */
	public boolean disconnect() {
		if (connection == null || connection.available() < 0)
			return false;
		
		try {
			in.close();
			out.close();
			connection.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Shoots the ball based on the angle.
	 * 
	 * @param angle
	 */
	public void shoot(int angle) {
		try {
			out.writeInt(angle);
			out.flush();
			
			int cmd = in.readInt();
			switch (cmd) {
			case BTCOMMAND_SHOOT_FAIL:
				listener.shootFail();
				break;
			case BTCOMMAND_SHOOT_SUCCESS:
				listener.shootSuccess();
				break;
			case BTCOMMAND_INVALID_ANGLE:
				listener.shootInvalidAngle();
				break;
			default:
				LCD.drawInt(cmd, 0, 4);
				listener.error("Unkown command!");
				break;
			}
		} catch (IOException e) {
			listener.error(e.getMessage());
		}
	}
}
