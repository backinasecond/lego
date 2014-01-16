package de.gruppe2.turntable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import de.gruppe2.Settings;
import de.gruppe2.Settings.Symbol;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class TurnTableControl {
	private RemoteDevice remoteDevice;
	private BTConnection connection;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	public static final int COMMAND_L = 0;
	public static final int COMMAND_U = 1;
	public static final int COMMAND_Z = 2;
	public static final int COMMAND_SIGMA = 3;

	/**
	 * Connects to the brick that controls the turntable.
	 * Required before calling any other method.
	 * 
	 * @return true if the connection to the turntable was successful, false otherwise (brick not found, connection
	 *         failed, ...)
	 */
	public boolean connectToTurntable() {
		System.out.println("Connect");

		String turntableBrickName = Settings.TURNTABLE_BRICK_NAME;

		remoteDevice = Bluetooth.getKnownDevice(turntableBrickName);
		if (remoteDevice == null) {
			System.out.println("Device not found.");
			return false;
		}

		connection = Bluetooth.connect(remoteDevice);
		if (connection == null) {
			System.out.println("Connection failed.");
			return false;
		}

		System.out.println("Connected.");

		inputStream = connection.openDataInputStream();
		outputStream = connection.openDataOutputStream();

		return (inputStream != null && outputStream != null);
	}

	public boolean sendSymbol(Symbol symbol) {
		System.out.println("Sende " + symbol);
		
		int command = 0;

		switch (symbol) {
		case L:
			command = COMMAND_L;
			break;
		case M:
			command = COMMAND_SIGMA;
			break;
		case U:
			command = COMMAND_U;
			break;
		case Z:
			command = COMMAND_Z;
			break;
		case NONE:
			command = COMMAND_Z;
		}

		try {
			outputStream.writeInt(command);
			outputStream.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
