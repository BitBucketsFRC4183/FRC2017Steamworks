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
	public static final int LEFT_MOTOR0_ID = 1;
	public static final int LEFT_MOTOR1_ID = 2;
	public static final int RIGHT_MOTOR0_ID = 3;
	public static final int RIGHT_MOTOR1_ID = 4; 
	
	public static final CANTalon.FeedbackDevice DRIVE_ENCODER = CANTalon.FeedbackDevice.QuadEncoder;
	public static final int DRIVE_PULSES_PER_REV = 2048; 
	
	
	// Climb Subsystem
	public static final int CLIMB_MOTOR_ID = 9;
	public static final int LEFT_SWITCH_PORT = 0; 
	public static final int RIGHT_SWITCH_PORT = 1; 
	public static final boolean INVERT_BUMPER_SWITCH = false;
		

	// Ball Manipulator
	public static final int BALL_SUBSYSTEM_TOP_ROLLER_MOTOR_ID = 5; 
	public static final int BALL_SUBSYSTEM_CONVEYER_MOTOR_ID = 6;
	public static final int BALL_SUBSYSTEM_SWEEPER_MOTOR_ID = 7;
	
	public static final int SHOOTER_ROLLER_PULSES_PER_REV = 256;
	
	public static final int BALLSUB_INTAKE_PNEUMA_CHANNEL = 0;
	public static final int BALLSUB_SHOOT_PNEUMA_CHANNEL = 1;
	
	// Gear Handler
	public static final int GEAR_HANDLER_MOTOR_ID = 8;
	
	public static final int GEAR_HANDLER_PNEUMA_OPEN_CHANNEL = 2;
	public static final int GEAR_HANDLER_PNEUMA_CLOSED_CHANNEL = 3;
	
	public static final int PNEUMATICS_CONTROL_MODULE_ID = 0;
}
