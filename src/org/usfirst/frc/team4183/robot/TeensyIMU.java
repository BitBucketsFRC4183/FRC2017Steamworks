package org.usfirst.frc.team4183.robot;


import java.io.PrintWriter;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class TeensyIMU {
	
	private SerialPort serialPort;
	private double unwrappedYaw = 0.0;
	PrintWriter pw;
	private final boolean DEBUG_THREAD = true;
	private final int IMUMESSAGELEN = 39;


	public double getYawDeg() {
		return unwrappedYaw;
	}


	public TeensyIMU(){
		
		System.out.println("Starting Teensy");
		
		try {
			pw = new PrintWriter("imutest-"+System.currentTimeMillis()+".txt");
		} catch(Exception e) {
			e.printStackTrace();
		}

		serialPort = findTeensy();
		if( serialPort == null) {
			System.out.println("No Teensy found");
			return;
		}
			
		// Start thread for reading in serial data
		new Thread(new TeensyRunnable()).start();
		
		// Start thread to print out something at a reasonable rate (testing)
		if( DEBUG_THREAD) {
			new Thread() { 
				public void run() {
					while(true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}	
						System.out.format("Yaw %.2f\n", getYawDeg());						
					}
				}
			}.start();
		}
	}
	
	private SerialPort findTeensy() {
		
		String[] portNames = SerialPortList.getPortNames();

		System.out.print("Port list:");
		for(String portName : portNames)
			System.out.print(" " + portName);
		System.out.println();		
		
		for( String portName : portNames) {
			
			try {				
				System.out.println("Trying port:" + portName);
				SerialPort port = new SerialPort(portName);
				port.openPort();

				// Set params
				// (Baud rate doesn't matter with Teensy,
				// because it's USB all the way thru)
				port.setParams(SerialPort.BAUDRATE_115200,
						SerialPort.DATABITS_8, 
						SerialPort.STOPBITS_1, 
						SerialPort.PARITY_NONE);
				
				// Spotting Teensy is a bit tricky.
				// Opening the port doesn't cause a reboot (like it does with Arduino).
				// If the Teensy has just been powered up, then it waits in bootloader;
				// opening the port will cause the user code to start.
				// BUT if it hasn't just been powered up (maybe the RoboRIO restarted,
				// but the Teensy remained powered), then the Teensy will just continue to run
				// the user program, which means it'll be streaming its messages.
				// So we have to look for either of 2 things:
				// 1) The "TeensyIMU" ident which is sent at the beginning of program
				// 2) A legitimate-looking TeensyIMU message
				// If we see either of those within 1 second, then we've found it.
				String inBuff = "", inStr;				
				long tQuit = System.currentTimeMillis() + 1000;
				while( System.currentTimeMillis() < tQuit) {
					
					// Get input & append to inBuff
					if( (inStr = port.readString()) != null) {
						inBuff += inStr;
					}
					
					if(inBuff.contains("TeensyIMU")) {
						System.out.format("Found TeensyIMU ident on port %s\n", portName);
						return port;
					}
					String[] lines = inBuff.split("\n");
					for( String line : lines) 
						if( line.endsWith("\r") && (line.length() == IMUMESSAGELEN+1) ) {
							System.out.format("Found TeensyIMU message on port %s\n", portName);
							return port;
						}
				}
				
				port.closePort();
				
			}
			catch( Exception ex) {}			
		}
		
		// Not found
		return null;
	}
	
	
	private float hexToDouble(String str){		
		//Parses string as decimal
		Long i = Long.parseLong(str, 16);
		//Converts newly created decimals to floating point    
		return Float.intBitsToFloat(i.intValue());
	}

	private long hexToLong(String str){
		return Long.parseLong(str,16);		
	}

	
	class TeensyRunnable implements Runnable {

		@Override
		public void run() {
			
			// Raw data
			String rawIn;
			
			// Buffer for raw data
			String inBuffer = "";
			
			while(true) {

				try {

					if( (rawIn = serialPort.readString()) == null) 
						continue;  // Nothing to read

					inBuffer += rawIn;  // Append new input
					
					// Process the input lines.
					// Lines ending is \r\n, but we have to watch out for 
					// fragment line at end of inBuffer.
					String[] lines = inBuffer.split("\n");
					inBuffer = "";
					for( int i = 0 ; i < lines.length ; i++) {						
						String line = lines[i];
						
						if( line.endsWith("\r")) {
							// Full line, chop off the \r
							line = line.substring(0, line.length()-1);							
							if( line.length() == IMUMESSAGELEN)
								processImuMsg(line);
						}
						else if( i == lines.length-1) {
							// Last "line" didn't end in \r, is actually a fragment,
							// put it back in buffer to be completed by further input					
							inBuffer = line;
						}						
					}
					
				} catch (SerialPortException e) {
					e.printStackTrace();
				}

				// Thread wait
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private double prevYaw = Double.NaN;
		private double prevTime = Double.NaN;
		private double unwrapAccum = 0.0;
		private double prevWrappedYaw = Double.NaN;
		
		private void processImuMsg( String msg) {
						
			// poseData[]:
			// 0: IMU time, usecs: 8 hex chars representing 4-byte long
			// 1: Fusion status, boolean: 2 hex chars representing 1 byte boolean
			// 2: Roll, radians: 8 hex chars representing 4-byte IEEE 754 single-precision float
			// 3: Pitch, radians: same as Roll
			// 4: Yaw, radians: same as Roll
			// Full message string is:
			// "tttttttt,ff,rrrrrrrr,pppppppp,yyyyyyyy,"
			String[] poseData = msg.split(",");
			if( poseData.length != 5)
				return;

			long imutime = hexToLong(poseData[0]);
			double wrappedYaw = hexToDouble(poseData[4])*(180.0/Math.PI);

			// Run the yaw angle unwrapper
			if( !Double.isNaN(prevWrappedYaw)) {
				if( wrappedYaw - prevWrappedYaw < -180.0 )
					unwrapAccum += 360.0;
				if( wrappedYaw - prevWrappedYaw > 180.0)
					unwrapAccum -= 360.0;
			}
			prevWrappedYaw = wrappedYaw;
			unwrappedYaw = wrappedYaw + unwrapAccum;
			
			// Calculate yaw rate (gotta watch out for excessive rate)
			if( !Double.isNaN(prevYaw)) {
				double timeDelta = (imutime - prevTime)/1000000.0;			
				double yawRate = (unwrappedYaw - prevYaw)/timeDelta;
				
//				System.out.format("IMU Time:%d YawRate:%f Yaw:%f\n", imutime, yawRate, unwrappedYaw);			
//				pw.format("IMU Time:%d YawRate:%f Yaw:%f\n", imutime, yawRate, unwrappedYaw);
			}
			
			prevTime = imutime;
			prevYaw = unwrappedYaw;
		}	
	}
}



