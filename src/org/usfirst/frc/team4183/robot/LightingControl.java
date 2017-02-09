package org.usfirst.frc.team4183.robot;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class LightingControl 
{
	public enum LightingObjects
	{
		// Currently planning on lighting on these controls
		DRIVE_SUBSYSTEM,
		GEAR_SUBSYSTEM,
		BALL_SUBSYSTEM,
		CLIMB_SUBSYSTEM,
		
		MAX_LIGHTING
	};
	
	private SerialPort serialPort;

//	String format is simple: 'NFCnbbbpppp'
//	    N - Strip Number 0..9
//	    F - Function
//	        0 (zero) = off
//	        1      = Solid ON
//	        S      = Snore on 3 second period
//	        B      = Blink with period pppp msec
//	        F      = Forward Chase n pixels (n >= 2, 1 on and n-1 off) with period pppp msec
//	        R      = Reverse Change n pixels (n >= 2, 1 on and n-1 off) with period pppp msec
//	        C      = Cylon (3-light, only tail chase with 25%, 50%, 100% of desired brightness) with period pppp msec
//	    C - Color Code
//	        0 = black (OFF)
//	        W = white
//	        R = red
//	        G = green
//	        B = blue
//	        C = cyan
//	        M = magenta
//	        Y = yellow
//	        O = orange
//	        V = violet
//	    n - Used only in F and R functions to space the pixels (ignored in all other modes)
//	    bbb - Brightness from 000 to 255
//	    pppp - Period in milliseconds between transitions
	
	public static final String FUNCTION_OFF = "0";
	public static final String FUNCITON_ON = "1";
	public static final String FUNCTION_SNORE = "S";
	public static final String FUNCTION_BLINK = "B";
	public static final String FUNCTION_FORWARD = "F";
	public static final String FUNCTION_REVERSE = "R";
	public static final String FUNCTION_CYLON = "C";
	
	public static final String COLOR_BLACK = "0";
	public static final String COLOR_WHITE = "W";
	public static final String COLOR_RED = "R";
	public static final String COLOR_GREEN = "G";
	public static final String COLOR_BLUE = "B";
	public static final String COLOR_CYAN = "C";
	public static final String COLOR_MAGENTA = "M";
	public static final String COLOR_YELLOW = "Y";
	public static final String COLOR_ORANGE = "O";
	public static final String COLOR_VIOLET = "V";
	
	private static final String FORMAT = "%u%c%c%u%03u%04u";
	
	public LightingControl() {
		
		if( (serialPort = findLightBoard() ) == null)
		{
			System.out.println("No BucketLights board found!");
			return;
		}
		
		setAllSleeping();
	}
	
	private SerialPort findLightBoard() 
	{

		String[] portNames = SerialPortList.getPortNames();

		System.out.print("Port list:");
		for(String portName : portNames)
			System.out.print(" " + portName);
		System.out.println();		

		for( String portName : portNames) 
		{

			try 
			{				
				System.out.println("Trying port:" + portName);
				SerialPort port = new SerialPort(portName);
				port.openPort();

				port.setParams(SerialPort.BAUDRATE_38400,
						SerialPort.DATABITS_8, 
						SerialPort.STOPBITS_1, 
						SerialPort.PARITY_NONE);
				
				String inStr, inBuf = "";
				long tQuit = System.currentTimeMillis() + 1000;
				while( System.currentTimeMillis() < tQuit) 
				{
					if( (inStr = serialPort.readString()) != null)
					{
						inBuf += inStr;
						if( inBuf.contains("Bucket"))
						{
							System.out.format("Found BucketLights on port %s\n", portName);	        	
							return port;							
						}
					}
				}

				port.closePort();

			}
			catch( Exception ex) {}	
		}

		// Not found
		return null;
	}


	public void set(LightingObjects lightingObject, String function, String color, int nspace, int brightness, int period_msec)
	{
		if (serialPort != null)
		{
			try
			{
				String command = String.format(FORMAT,
											   lightingObject,
											   function,
											   color,
											   nspace,
											   brightness,
											   period_msec);
				
				serialPort.writeString(command);
			}
			catch (SerialPortException e) 
			{
				// pass
			}
		}		
	}	
	
	public void setAll(String function, String color, int nspace, int brightness, int period_msec)
	{
		for (LightingObjects lightingObject: LightingObjects.values())
		{
			set(lightingObject,
			    function,
			    color,
			    nspace,
			    brightness,
			    period_msec);
		}
	}
	
	public void setAllSleeping()
	{
		setAll(FUNCTION_SNORE,
			   COLOR_VIOLET,
			   0,	// nspace - don't care
			   32,	// brightness - safer
			   0);	// period_msec - don't care
	}

	public void setAllOff()
	{
		setAll(FUNCTION_OFF,
			   COLOR_BLACK,
			   0,	// nspace - don't care
			   0,	// brightness - off
			   0);	// period_msec - don't care
	}
		
}
