package org.usfirst.frc.team4183.robot;

import com.ctre.CANTalon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// Drive Subsystem
	public static final int LEFT_MOTOR0_ID = 0;
	public static final int LEFT_MOTOR1_ID = 1;
	public static final int RIGHT_MOTOR0_ID = 3;
	public static final int RIGHT_MOTOR1_ID = 4; 
	
	public static final CANTalon.FeedbackDevice DRIVE_ENCODER = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int DRIVE_PULSES_PER_REV = 2048; 
	
	
	// Climb Subsystem
	public static final int CLIMB_MOTOR = 10;
	
	

	//stuff for the BALL MANIPULATOR is below here
	public static int shooterMotor = 9; 
	public static int conveyerMotor = 12;
	public static int sweeperMotor = 11;
	
	
	public static final int SHOOTER_ROLLER_PULSES_PER_REV = 256;
	
	
	
	
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
