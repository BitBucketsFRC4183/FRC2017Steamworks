package org.usfirst.frc.team4183.robot;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	// Autonomous & misc...
	
	// Smallest drive that will be applied in TurnBy
	// (unless error falls within dead zone, then drive goes to 0)
	// THIS MUST BE LARGE ENOUGH TO ROTATE THE ROBOT from stopped position;
	// if it isn't, you will get stuck in TurnBy.
	// But if this is TOO BIG, you'll get limit cycling, and also be stuck.
	public static final double TURNBY_MIN_DRIVE = 0.17;
	
	// Similar to above but for DriveStraight.
	// Doesn't seem to be as finicky though.
	public static final double DRIVESTRAIGHT_MIN_DRIVE = 0.2;
		
	// Nominal value assuming 4" wheel:
	// (4" * pi) in/rot = 12.57
	// The precise value must be determined by calibration.
	public static final double INCH_PER_WHEEL_ROT = 12.5 * 1.0353360615;

	
	// Drive Subsystem
	public static final int LEFT_FRONT_MOTOR_ID = 3;
	public static final int LEFT_REAR_MOTOR_ID = 4;
	public static final int RIGHT_FRONT_MOTOR_ID = 1;
	public static final int RIGHT_REAR_MOTOR_ID = 2; 
		
	
	// Climb Subsystem
	public static final int CLIMB_MOTOR_ID = 6;	
	public static final int LEFT_SWITCH_PORT = 0; 
	public static final int RIGHT_SWITCH_PORT = 1; 
	public static final boolean INVERT_BUMPER_SWITCH = false;
		

	// Ball Manipulator
	public static final int BALL_SUBSYSTEM_TOP_ROLLER_MOTOR_ID = 7;
	public static final int BALL_SUBSYSTEM_CONVEYER_MOTOR_ID = 5;
	public static final int BALL_SUBSYSTEM_SWEEPER_MOTOR_ID = 9;
				
	// Gear Handler
	public static final int GEAR_HANDLER_MOTOR_ID = 8;
	
	// Pneumatics
	public static final int HOPPER_OPEN_PNEUMA_CHANNEL = 0;
	public static final int HOPPER_CLOSE_PNEUMA_CHANNEL = 1;
	public static final int GEAR_HANDLER_PNEUMA_CLOSED_CHANNEL = 2;
	public static final int GEAR_HANDLER_PNEUMA_OPEN_CHANNEL = 3;
	public static final int CLIMB_PNEUMA_RELEASE_CHANNEL = 4;
	public static final int CLIMB_PNEUMA_HOLD_CHANNEL = 7;
	public static final int BALLSUB_INTAKE_PNEUMA_CHANNEL = 5;
	public static final int BALLSUB_SHOOT_PNEUMA_CHANNEL = 6;
	
	public static final int PNEUMATICS_CONTROL_MODULE_ID = 0;
}
