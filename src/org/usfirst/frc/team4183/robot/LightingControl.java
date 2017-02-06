package org.usfirst.frc.team4183.robot;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class LightingControl {
	private SerialPort serialPort =  new SerialPort(SerialPortList.getPortNames()[0]);


	public void initDefaultCommand() {
		System.out.println("Light Change");
		try {
			serialPort.openPort();
			System.out.print("Opening port 1");
			serialPort.setParams(SerialPort.BAUDRATE_115200, 
					SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_1, 
					SerialPort.PARITY_NONE);
		} catch (SerialPortException ex) {
			// TODO Auto-generated catch block
			System.out.println(ex);
		}
		for (int i=0;i<100; i++) 
		{
		try {
			serialPort.writeByte((byte)'b');
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		try{
			serialPort.closePort(); 
			System.out.println("Closing Port");
		} catch (SerialPortException ex) {
			// TODO Auto-generated catch block
			System.out.println(ex); }

	}


}
