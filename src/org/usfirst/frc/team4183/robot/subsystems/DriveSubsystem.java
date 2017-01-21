package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import org.usfirst.frc.team4183.robot.RobotMap;
import edu.wpi.first.wpilibj.GyroBase;
/**
 *
 */
public class DriveSubsystem extends Subsystem {
		private final CANTalon LEFT_MOTOR0_0;
		private final CANTalon LEFT_MOTOR0_1; 
		private final CANTalon RIGHT_MOTOR0_0;
		private final CANTalon RIGHT_MOTOR0_1;
	    
		
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
public DriveSubsystem() {
	LEFT_MOTOR0_0 = new CANTalon(RobotMap.LEFT_MOTOR0_0);
	LEFT_MOTOR0_1 = new CANTalon(RobotMap.LEFT_MOTOR0_1);
	RIGHT_MOTOR0_0 = new CANTalon(RobotMap.RIGHT_MOTOR_0);
	RIGHT_MOTOR0_1 = new CANTalon(RobotMap.RIGHT_MOTOR_1);
	RobotDrive Drive = new RobotDrive(LEFT_MOTOR0_0, LEFT_MOTOR0_1, RIGHT_MOTOR0_0, RIGHT_MOTOR0_1);
	LEFT_MOTOR0_0.setFeedbackDevice(RobotMap.DRIVE_ENCODER1);
	LEFT_MOTOR0_0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV); 
	RIGHT_MOTOR0_0.setFeedbackDevice(RobotMap.DRIVE_ENCODER2);
	RIGHT_MOTOR0_0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV2);
	
}	
public void GyroBase(){
	
}

	
public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

