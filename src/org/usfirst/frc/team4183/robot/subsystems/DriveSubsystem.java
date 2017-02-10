package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.DriveSubsystem.Idle;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
		private final CANTalon leftMotor0;
		private final CANTalon leftMotor1; 
		private final CANTalon rightMotor0;
		private final CANTalon rightMotor1;
	    
		private final RobotDrive drive;
		
		private double lowSensitivityGain = 0.2;
		private final double ALIGN_LOOP_GAIN = 0.01;
		
		private double yawSetPoint;
		
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
		public DriveSubsystem() {
			Preferences prefs = Preferences.getInstance();
			leftMotor0 = new CANTalon(RobotMap.LEFT_MOTOR0_ID);
			leftMotor1 = new CANTalon(RobotMap.LEFT_MOTOR1_ID);
			rightMotor0 = new CANTalon(RobotMap.RIGHT_MOTOR0_ID);
			rightMotor1 = new CANTalon(RobotMap.RIGHT_MOTOR1_ID);
			
			lowSensitivityGain = prefs.getDouble("Low Sensitivity Gain", lowSensitivityGain);
	
			drive = new RobotDrive(leftMotor0, leftMotor1, rightMotor0, rightMotor1);
			drive.setSafetyEnabled(false);
			
			leftMotor0.setFeedbackDevice(RobotMap.DRIVE_ENCODER);
			leftMotor0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV); 
			rightMotor0.setFeedbackDevice(RobotMap.DRIVE_ENCODER);
			rightMotor0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV);
		}	

		public void enable() {}
		
		public void disable() {}
		
		public void driveStraight(boolean start) {
			if(start) {
				yawSetPoint = Robot.IMU.getYawDeg();
			} 
		}
		
		public void alignDrive(double speed) {
			double turnAngle = ALIGN_LOOP_GAIN * (yawSetPoint - Robot.IMU.getYawDeg());
			
			if(OI.btnLowSensitiveDrive.get()) {
				drive.arcadeDrive(speed * lowSensitivityGain, turnAngle * lowSensitivityGain);
			} else {
				drive.arcadeDrive(speed, turnAngle);
			}
		}
		
		public void arcadeDrive(double speed, double turnAngle) {
			if(OI.btnLowSensitiveDrive.get()) {
				drive.arcadeDrive(speed * lowSensitivityGain, turnAngle * lowSensitivityGain);
			} else {
				drive.arcadeDrive(speed, turnAngle);
			}
		}
		
		public void initDefaultCommand() {
			setDefaultCommand(new Idle());
		}
}

