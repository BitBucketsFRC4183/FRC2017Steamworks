package org.usfirst.frc.team4183.robot;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

// RoboRIO has one (1) USB DevicePort and two (2) USB HostPorts
// SerialManager exists to arbitrate which port is assigned to which object
// On the RoboRIO up to three (3) serial ports will be identified when
// both the TeensyIMU and Arduino (BucketLights) are attached; when this
// occurs the serial port name (e.g., ttyACM0 vs ttyACM1) is ambiguous,
// while the ttyS* port is not
//
// To resolve this we will simply get a list of the ports, look for the "Bucket"
// key word from the Arduino (BucketLights)
public class SerialManager 
{
	private boolean initialized = false;
	private SerialPort imuPort;
	private SerialPort lightingPort;

	// getting serial ports list into the array
	private String[] portNames;
	        

	// =============================================================================
	// WARNING WARNING
	// This is ugly code, but should get the job done for now... biggest issue
	// is the repeated logic, cut/paste, etc.
	// =============================================================================
	public void initialize()
	{
		// getting serial ports list into the array
		portNames = SerialPortList.getPortNames();
		        
		if (portNames.length == 0) {
		    System.out.println("WARNING: There are no serial-ports!");
		    return;
		}
		if (portNames.length == 0) {
		    System.out.println("WARNING: There are no serial-ports. LightingControl is DISABLED");
		    return;
		}

		// Display and select a port
		for (int i = 0; i < portNames.length; i++)
		{
			System.out.println("Found the following serial ports");
		    System.out.println(portNames[i]);
		    
		    // Only process the host ports that contain the tty prefix we expect
		    if (portNames[i].contains("ttyACM"))
		    {
		    	// We found one of the ports that looks like an ACM modem on USB
		    	// This is either a Teensy or Arduino (or similar)
		    	// More importantly, it is NOT the Device USB port, it is one of the Host USB ports
		    				    
			    // Open and wait for the word "Bucket"
			    SerialPort serialPort = new SerialPort(portNames[i]);
				try 
				{
					serialPort.openPort();
					
					// Start with desired Arduino settings
					// NOTE: Higher baud rate may not be reliable in
					// all conditions; verify before fielding higher
					// than 38400
					serialPort.setParams(SerialPort.BAUDRATE_38400, 
							SerialPort.DATABITS_8, 
							SerialPort.STOPBITS_1, 
							SerialPort.PARITY_NONE);
				
				} 
				catch (SerialPortException ex) 
				{
					ex.printStackTrace();
				}
				
				// Attempt to read "Bucket" from the serial port
				// If "Bucket" is indicated, then we found the BucketLight Arduino
				// Anything else is **ASSUMED** to be Teensy
				try 
				{
					if (lightingPort != null)
					{
						// Only perform the timeout once; i.e., if
						// the read is successful the first time through
						// there is no point is checking again because
						// we will assume the other port is the IMU
						String response = serialPort.readString(6, 2000);
						if (response.equals("Bucket"))
						{
							lightingPort = serialPort;
						}
						else
						{
							serialPort.closePort();
							imuPort = serialPort;
							
							// Open the port with previously seen settings for IMU
							// In reality the Teensy will likely force port to higher
							// rate (which works in this case)
							imuPort.openPort();
							imuPort.setParams(SerialPort.BAUDRATE_115200, 
									SerialPort.DATABITS_8, 
									SerialPort.STOPBITS_1, 
									SerialPort.PARITY_NONE);							
						}
					}
					else
					{	
						// The lighting port has been identified, the only
						// remaining port is the IMU port
						serialPort.closePort();
						imuPort = serialPort;
						
						// Open the port with previously seen settings for IMU
						// In reality the Teensy will likely force port to higher
						// rate (which works in this case)
						imuPort.openPort();
						imuPort.setParams(SerialPort.BAUDRATE_115200, 
								SerialPort.DATABITS_8, 
								SerialPort.STOPBITS_1, 
								SerialPort.PARITY_NONE);						
						
					}
				} 
				catch (SerialPortException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (SerialPortTimeoutException e) 
				{
					// pass
				}
	 
		    }
		}
		
		initialized = true;
	}

	public SerialPort getImuPort()
	{
		if (! initialized)
		{
			initialize();
		}
		return imuPort;
	}
	
	public SerialPort getLightingPort()
	{
		if (! initialized)
		{
			initialize();
		}
		return lightingPort;
	}

}
