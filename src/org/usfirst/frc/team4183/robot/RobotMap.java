package org.usfirst.frc.team4183.robot;

import com.ctre.CANTalon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// No basis in reality, just random numbers I put
	public static int testMotor1 = 0;
	public static int leftMotor0 = 0;
	public static int leftMotor1 = 1;
	public static int rightMotor0 = 3;
	public static int rightMotor1 = 4; 
	public static final CANTalon.FeedbackDevice DRIVE_ENCODER1 = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int DRIVE_PULSES_PER_REV = 256; 
	public static final CANTalon.FeedbackDevice DRIVE_ENCODER2 = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int DRIVE_PULSES_PER_REV2 = 256; 
	public static int climbMotor = 5;

  // For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
