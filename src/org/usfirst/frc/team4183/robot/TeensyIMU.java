package org.usfirst.frc.team4183.robot;

import java.io.PrintWriter;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class TeensyIMU {
	
	private SerialPort serialPort;
	private final int IMUMESSAGELEN = 39;
	private double yaw = 0.0;
	PrintWriter pw;


	public double getYawDeg() {
		return yaw;
	}


	public TeensyIMU(){
		
		System.out.println("Starting Teensy");
		serialPort = new SerialPort(SerialPortList.getPortNames()[0]);
		//System.out.println(serialPort.getPortName());
		
		try {
			pw = new PrintWriter("imutest-"+System.currentTimeMillis()+".txt");
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			//Open serial port
			serialPort.openPort();
			//Set params.
			serialPort.setParams(SerialPort.BAUDRATE_115200, 
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1, 
					SerialPort.PARITY_NONE);
		}catch (SerialPortException ex) {
			System.out.println(ex);
		}
		
		// Thread for reading in serial data
		new Thread(new TeensyRunnable()).start();		 
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

		private boolean firsttime = true;

		private double prevYaw;
		private double prevTime;

		private double unwrapAccum = 0.0;
		private double prevWrappedYaw;

		@Override
		public void run() {

			// Raw data
			String rawIn;
			
			// Used as a buffer for raw data
			String inBuffer = "";
			
			while(true) {

				try {

					if( (rawIn=serialPort.readString()) == null) 
						continue;

					inBuffer += rawIn;

					int indx2 = inBuffer.lastIndexOf('\n');
					if( indx2 != -1) {

						int indx1 = inBuffer.lastIndexOf('\n', indx2-1) + 1;
						String line = inBuffer.substring( indx1, indx2-1);

						if( line.length() == IMUMESSAGELEN) {
							processLine(line);
							firsttime = false;
						}

						inBuffer = inBuffer.substring(indx2+1);
					}


				} catch (SerialPortException e) {
					e.printStackTrace();
				}

				// Thread wait
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		
		
		private void processLine( String line) {
						
			// poseData[]:
			// 0: IMU time, usecs: 8 hex bytes representing 4-byte long
			// 1: Fusion status, boolean: 2 hex bytes representing 1 byte boolean
			// 2: Roll, radians: 8 hex bytes representing 4-byte IEEE 754 single-precision float
			// 3: Pitch, radians: same as Roll
			// 4: Yaw, radians: same as Roll
			String[] poseData = line.split(",");
			if( poseData.length < 5)
				return;


			long imutime = hexToLong(poseData[0]);
			double wrappedYaw = hexToDouble(poseData[4])*(180.0/Math.PI);

			// Run the yaw angle unwrapper
			if( !firsttime) {
				if( wrappedYaw - prevWrappedYaw < -180.0 )
					unwrapAccum += 360.0;
				if( wrappedYaw - prevWrappedYaw > 180.0)
					unwrapAccum -= 360.0;
			}
			yaw = wrappedYaw + unwrapAccum;
			prevWrappedYaw = wrappedYaw;

			
			// Calculate yaw rate (gotta watch out for excessive rate)
			if( !firsttime) {
				double timeDelta = (imutime - prevTime)/1000000.0;			
				double yawRate = (yaw - prevYaw)/timeDelta;
				System.out.format("IMU Time:%d Yaw:%f YawRate:%f", imutime, yaw, yawRate);			
//				pw.format("IMU Time:%d Yaw:%f YawRate:%f", imutime, yaw, yawRate);
			}		
			prevTime = imutime;
			prevYaw = yaw;

		}	
	}
}



