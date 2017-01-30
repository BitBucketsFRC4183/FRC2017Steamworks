package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.DriveSubsystem.IdleState;

import edu.wpi.first.wpilibj.GyroBase;
/**
 *
 */
public class DriveSubsystem extends Subsystem {
		private final CANTalon LEFT_MOTOR_0;
		private final CANTalon LEFT_MOTOR_1; 
		private final CANTalon RIGHT_MOTOR_0;
		private final CANTalon RIGHT_MOTOR_1;
	    
		private final RobotDrive drive;
		
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
		public DriveSubsystem() {
			LEFT_MOTOR_0 = new CANTalon(RobotMap.LEFT_MOTOR0_ID);
			LEFT_MOTOR_1 = new CANTalon(RobotMap.LEFT_MOTOR1_ID);
			RIGHT_MOTOR_0 = new CANTalon(RobotMap.RIGHT_MOTOR0_ID);
			RIGHT_MOTOR_1 = new CANTalon(RobotMap.RIGHT_MOTOR1_ID);
	
			drive = new RobotDrive(LEFT_MOTOR_0, LEFT_MOTOR_1, RIGHT_MOTOR_0, RIGHT_MOTOR_1);
	
			LEFT_MOTOR_0.setFeedbackDevice(RobotMap.DRIVE_ENCODER1);
			LEFT_MOTOR_0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV); 
			RIGHT_MOTOR_0.setFeedbackDevice(RobotMap.DRIVE_ENCODER2);
			RIGHT_MOTOR_0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV2);
	
		}	
		public void GyroBase(){
	
		}

		public void arcadeDrive(double speed, double turnAngle) {
			drive.arcadeDrive(speed, turnAngle);
		}
		
		public void initDefaultCommand() {
		    // Set the default command for a subsystem here,
			// then delete the "throw"
		    //setDefaultCommand(new MySpecialCommand());

			throw new RuntimeException("Define a Default Command!");        // Set the default command for a subsystem here.
		}
}

