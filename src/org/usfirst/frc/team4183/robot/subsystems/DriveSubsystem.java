package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Encoder; 
import edu.wpi.first.wpilibj.RobotDrive;
import org.usfirst.frc.team4183.robot.RobotMap;
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
	leftMotor0 = new CANTalon(RobotMap.leftMotor0);
	leftMotor1 = new CANTalon(RobotMap.leftMotor1);
	rightMotor0 = new CANTalon(RobotMap.rightMotor0);
	rightMotor1 = new CANTalon(RobotMap.rightMotor1);
	RobotDrive drive = new RobotDrive(leftMotor0, leftMotor1, rightMotor0, rightMotor1);
}	
	
public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

