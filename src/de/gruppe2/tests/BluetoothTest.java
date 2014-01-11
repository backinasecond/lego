package de.gruppe2.tests;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;


public class BluetoothTest {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		while (true) {
			System.out.println("Press Enter to connect to Gate!");
			Button.ENTER.waitForPress();
			
			System.out.println("Connecting...");
			RemoteDevice btrd = Bluetooth.getKnownDevice("Gate1");
			BTConnection conn = Bluetooth.connect(btrd);
			
			
			
			if(conn == null) {
				System.out.println("Connection failed!");
			} else {
				System.out.println("Connected!");
				System.out.println("Open Gate");
				DataOutputStream os = conn.openDataOutputStream();
				Delay.msDelay(1000);
				os.writeInt(0);
				os.flush();
				System.out.println("Sent!");
				
				Delay.msDelay(6000);
				System.out.println("Close Gate");
				os.writeInt(99);
				os.flush();
				conn.close();
				System.out.println("Connection closed.");
			}
		}
		
	}

}

