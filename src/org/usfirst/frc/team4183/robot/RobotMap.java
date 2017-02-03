package org.usfirst.frc.team4183.robot;

import com.ctre.CANTalon;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	// tjw:
	// Current proto board has only 4 controllers;
	// until we get that fixed, I've assigned fantastic motor numbers in here
	// to make sure there's no collisions.
	// When testing a particular subsystem, put real numbers in here,
	// build & download, but DON'T commit that version.
	
	// Drive Subsystem
	public static final int LEFT_MOTOR0_ID = 20;
	public static final int LEFT_MOTOR1_ID = 21;
	public static final int RIGHT_MOTOR0_ID = 22;
	public static final int RIGHT_MOTOR1_ID = 23; 
	
	public static final CANTalon.FeedbackDevice DRIVE_ENCODER = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int DRIVE_PULSES_PER_REV = 2048; 
	
	
	// Climb Subsystem
	public static final int CLIMB_MOTOR = 31;
		

	// Ball Manipulator
	public static final int BALL_SUBSYSTEM_TOP_ROLLER_MOTOR_ID = 40; 
	public static final int BALL_SUBSYSTEM_CONVEYER_MOTOR_ID = 41;
	public static final int BALL_SUBSYSTEM_SWEEPER_MOTOR_ID = 42;
	
	public static final int SHOOTER_ROLLER_PULSES_PER_REV = 256;
	
	
	// Gear Handler
	public static final int GEAR_HANDLER_MOTOR = 51;
}
