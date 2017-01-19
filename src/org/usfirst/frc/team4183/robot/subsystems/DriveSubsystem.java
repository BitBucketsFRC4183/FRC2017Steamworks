package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Encoder; 
import edu.wpi.first.wpilibj.RobotDrive;
/**
 *
 */
public class DriveSubsystem extends Subsystem {
		private CANTalon leftMotor0;
		private CANTalon leftMotor1; 
		private CANTalon rightMotor0;
		private CANTalon rightMotor1;
	    
    
		
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
public DriveSubsystem() {
	RobotDrive = new RobotDrive(leftMotor0, leftMotor1, rightMotor0, rightMotor1);
}	
	
public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

