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
	
	
	// Ball Manipulator Subsystem
	public static final int SHOOTER_MOTOR = 9; 
	public static final int CONVEYOR_MOTOR = 12;
	public static final int SWEEPER_MOTOR = 11;
		
	public static final CANTalon.FeedbackDevice SHOOTER_ENCODER = CANTalon.FeedbackDevice.QuadEncoder;	
	public static final int SHOOTER_PULSES_PER_REV = 256;
	
	
	// Gear Loader Subsystem
	public static final int LOADER_MOTOR = 21;
		
}
