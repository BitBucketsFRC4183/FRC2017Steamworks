package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.DriveSubsystem.Idle;
import org.usfirst.frc.team4183.utils.Deadzone;

/**
 *
 */
public class DriveSubsystem extends Subsystem {
		private final CANTalon leftMotor0;
		private final CANTalon leftMotor1; 
		private final CANTalon rightMotor0;
		private final CANTalon rightMotor1;
	    
		private final RobotDrive robotDrive;
		
		private double lowSensitivityGain = 0.5;		// Half-control seems nice
		private final double ALIGN_LOOP_GAIN = 0.05;
		
		private double yawSetPoint;
		
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
		public DriveSubsystem() {
			Preferences prefs = Preferences.getInstance();
			leftMotor0 = new CANTalon(RobotMap.LEFT_MOTOR0_ID);
			leftMotor1 = new CANTalon(RobotMap.LEFT_MOTOR1_ID);
			rightMotor0 = new CANTalon(RobotMap.RIGHT_MOTOR0_ID);
			rightMotor1 = new CANTalon(RobotMap.RIGHT_MOTOR1_ID);
			
			lowSensitivityGain = prefs.getDouble("LowSensitivityGain", lowSensitivityGain);
	
			robotDrive = new RobotDrive(leftMotor0, leftMotor1, rightMotor0, rightMotor1);
			robotDrive.setSafetyEnabled(false);
			
			leftMotor0.setFeedbackDevice(RobotMap.DRIVE_ENCODER);
			leftMotor0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV); 
			rightMotor0.setFeedbackDevice(RobotMap.DRIVE_ENCODER);
			rightMotor0.configEncoderCodesPerRev(RobotMap.DRIVE_PULSES_PER_REV);
		}	

		public void enable() {}
		
		public void disable() {
			robotDrive.arcadeDrive(0.0, 0.0);			
		}
		
		public void driveStraight(boolean start) {
			if(start) {
				yawSetPoint = Robot.imu.getYawDeg();
			} 
		}
		
		public void alignDrive(double speed, double turn) {
			
			/* No requirement for this but might be nice to have?
			 * Allows a bit of driver yaw adjust while in this mode.
			 * The 0.2 is chosen to give a max of 10 deg/sec setpoint change
			 * at full stick (a modest amount).
			 * 
			// Turn stick is + to the right,
			// +yaw is CCW looking down,
			// so + stick should lower the setpoint. 
			yawSetPoint += -0.2 * Deadzone.f(turn, .05);
			*/

			if(OI.btnLowSensitiveDrive.get())
				speed *= lowSensitivityGain;
			
			if(OI.btnInvertAxis.get()) {
				speed *= -1.0;
			}
			
			double error = ALIGN_LOOP_GAIN * (yawSetPoint - Robot.imu.getYawDeg());

			robotDrive.arcadeDrive(speed, error);			
		}
		
		public void arcadeDrive(double speed, double turn) {
			
			if(OI.btnLowSensitiveDrive.get()) {
				speed *= lowSensitivityGain;
				turn *= lowSensitivityGain;
			}
			if(OI.btnInvertAxis.get()) {
				speed *= -1.0;
			}
			
			// Turn stick is + to the right;
			// but arcadeDrive 2nd arg + produces left turn
			// (this is +yaw when yaw is defined according to right-hand-rule
			// with z-axis up, so arguably correct).
			// Anyhow need the - sign to make it turn correctly.
			robotDrive.arcadeDrive(speed, -turn);
		}
		
		
		public void initDefaultCommand() {
			setDefaultCommand(new Idle());
		}
}

