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
		private final CANTalon leftFrontMotor;
		private final CANTalon leftRearMotor; 
		private final CANTalon rightFrontMotor;
		private final CANTalon rightRearMotor;
	    
		private final RobotDrive robotDrive;
		
		private double lowSensitivityGain = 0.5;		// Half-control seems nice
		private final double ALIGN_LOOP_GAIN = 0.05;
		private final int ENCODER_PULSES_PER_REV = 360;
		
		private double yawSetPoint;
		
		public DriveSubsystem() {
			Preferences prefs = Preferences.getInstance();
			lowSensitivityGain = prefs.getDouble("LowSensitivityGain", lowSensitivityGain);

			leftFrontMotor = new CANTalon(RobotMap.LEFT_FRONT_MOTOR_ID);
			leftRearMotor = new CANTalon(RobotMap.LEFT_REAR_MOTOR_ID);
			rightFrontMotor = new CANTalon(RobotMap.RIGHT_FRONT_MOTOR_ID);
			rightRearMotor = new CANTalon(RobotMap.RIGHT_REAR_MOTOR_ID);			
	
			robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor);
			robotDrive.setSafetyEnabled(false);

			leftFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			leftFrontMotor.configEncoderCodesPerRev(ENCODER_PULSES_PER_REV); 
			rightFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			rightFrontMotor.configEncoderCodesPerRev(ENCODER_PULSES_PER_REV);
		}	

		public void enable() {}
		
		public void disable() {
			robotDrive.arcadeDrive(0.0, 0.0);			
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
		
		
		public void setAlignDrive(boolean start) {
			if(start) {
				yawSetPoint = Robot.imu.getYawDeg();
			} 
		}
		
		public void doAlignDrive(double speed, double turn) {
			
			// Turn stick is + to the right,
			// +yaw is CCW looking down,
			// so + stick should lower the setpoint. 
			yawSetPoint += -0.3 * Deadzone.f(turn, .05);

			if(OI.btnLowSensitiveDrive.get())
				speed *= lowSensitivityGain;
			
			if(OI.btnInvertAxis.get()) {
				speed *= -1.0;
			}
			
			double error = ALIGN_LOOP_GAIN * (yawSetPoint - Robot.imu.getYawDeg());

			robotDrive.arcadeDrive(speed, error);			
		}
		
		
		public void setLockDrive( boolean start) {

			if( start) {
				setupClosedLoopMaster(leftFrontMotor);
				setupClosedLoopMaster(rightFrontMotor);

				leftRearMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
				leftRearMotor.reverseOutput(false); // Follow the front
				rightRearMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
				rightRearMotor.reverseOutput(false); // Follow the front
			}
			else {
				leftFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				leftRearMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				rightFrontMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				rightRearMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);							
			}
		}
		
		private void setupClosedLoopMaster( CANTalon m) {
			
			m.changeControlMode(CANTalon.TalonControlMode.Position);
			m.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			m.configEncoderCodesPerRev(ENCODER_PULSES_PER_REV);
			m.reverseSensor(false);  // TODO true or false? I think false...
			m.setPosition(0.0);
			
			// TODO magic numbers
			m.setPID(0.2, 0.0, 0.0);  // TODO Gain?? depends on gearing & position pulses
			m.setF(0.0);
			m.setIZone(0);
			m.setCloseLoopRampRate(50.0);  // Smoothes things a bit
			double allowedErr_deg = 2.0;
			double npu_per_deg = (4*ENCODER_PULSES_PER_REV)/360.0;
			m.setAllowableClosedLoopErr((int)(allowedErr_deg * npu_per_deg));
			m.configNominalOutputVoltage(+4.0, -4.0);
			m.configPeakOutputVoltage(+12.0, -12.0);			
		}
		
		public void doLockDrive() {
			leftFrontMotor.set(0.0);
			leftRearMotor.set(leftFrontMotor.getDeviceID());
			rightFrontMotor.set(0.0);
			rightRearMotor.set(rightFrontMotor.getDeviceID());			
		}
		
		
		public double getPositionFt() {
			// TODO: calibrate this.
			// Constant below is assuming 4" wheel
			// 1.047 ft/rot = (4" * pi) in/rot * 1/12 ft/in
			// Also make sure that moving forward INCREASES the position
			// (it should, because the reverseSensor() call should've made it so)
			return 1.047 * leftFrontMotor.getPosition();
		}
		
		public void initDefaultCommand() {
			setDefaultCommand(new Idle());
		}
}

