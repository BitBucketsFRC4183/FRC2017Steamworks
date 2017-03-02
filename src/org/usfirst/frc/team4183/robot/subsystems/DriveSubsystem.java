package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;

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
	
		// TODO: calibrate this.
		// Constant below is assuming 4" wheel
		// 1.047 ft/rot = (4" * pi) in/rot * 1/12 ft/in
		// Tested (briefly) 2/21 seemed within 3%
		private final double FEET_PER_WHEEL_ROT = 1.047;
		
		// TODO: calibrate these
		// Make the robot go straight when turn stick zeroed (in DriverControl).
		// + value will inject +yaw (CCW from top, or pull to left viewed from behind).
		private final double YAW_CORRECT_STICK = 0.0;   // Correct based on fwd stick
		private final double YAW_CORRECT_VELOCITY = 0.0; // Correct based on fwd speed
		
		private final double LOW_SENS_GAIN = 0.6;		
		private final double ALIGN_LOOP_GAIN = 0.05;  // TODO See if this can be increased

		// The counts-per-rev is printed on the encoder -
		// it's the 1st number after the "E4P" or "E4T"
		private final int ENCODER_PULSES_PER_REV = 250; 
		private final boolean REVERSE_SENSOR = false;   // Verified correct 2/21

		
		private final CANTalon leftFrontMotor;
		private final CANTalon leftRearMotor; 
		private final CANTalon rightFrontMotor;
		private final CANTalon rightRearMotor;
	    
		private final RobotDrive robotDrive;
		
		private double yawSetPoint;
		
		public DriveSubsystem() {

			leftFrontMotor = new CANTalon(RobotMap.LEFT_FRONT_MOTOR_ID);
			leftRearMotor = new CANTalon(RobotMap.LEFT_REAR_MOTOR_ID);
			rightFrontMotor = new CANTalon(RobotMap.RIGHT_FRONT_MOTOR_ID);
			rightRearMotor = new CANTalon(RobotMap.RIGHT_REAR_MOTOR_ID);			
	
			robotDrive = new RobotDrive(leftFrontMotor, leftRearMotor, rightFrontMotor, rightRearMotor);
			robotDrive.setSafetyEnabled(false);

			// Set up the position encoders, can be used external to this class
			leftFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			leftFrontMotor.configEncoderCodesPerRev(ENCODER_PULSES_PER_REV); 
			leftFrontMotor.reverseSensor(REVERSE_SENSOR);
			leftFrontMotor.setPosition(0.0);


			rightFrontMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			rightFrontMotor.configEncoderCodesPerRev(ENCODER_PULSES_PER_REV);
			rightFrontMotor.reverseSensor(REVERSE_SENSOR);
			rightFrontMotor.setPosition(0.0);
		}	

		
		public void disable() {
			robotDrive.arcadeDrive(0.0, 0.0);			
		}
		
		// +turn produces right turn (CW from above, -yaw angle)
		public void arcadeDrive(double fwdStick, double turnStick) {
			
			if(OI.btnLowSensitiveDrive.get()) {
				fwdStick *= LOW_SENS_GAIN;
				turnStick *= LOW_SENS_GAIN;
			}
			if(OI.btnInvertAxis.get()) {
				fwdStick *= -1.0;
			}
			
			// Turn stick is + to the right;
			// but arcadeDrive 2nd arg + produces left turn
			// (this is +yaw when yaw is defined according to right-hand-rule
			// with z-axis up, so arguably correct).
			// Anyhow need the - sign to make it turn correctly.
			robotDrive.arcadeDrive(fwdStick, -turnStick + yawCorrect( fwdStick));
		}
		
		
		public void setAlignDrive(boolean start) {
			if(start) {
				yawSetPoint = Robot.imu.getYawDeg();
			} 
		}
		
		public void doAlignDrive(double fwdStick, double turnStick) {
			
			// Turn stick is + to the right,
			// +yaw is CCW looking down,
			// so + stick should lower the setpoint. 
			yawSetPoint += -0.3 * Deadzone.f(turnStick, .05);

			if(OI.btnLowSensitiveDrive.get())
				fwdStick *= LOW_SENS_GAIN;
			
			if(OI.btnInvertAxis.get()) {
				fwdStick *= -1.0;
			}
			
			double error = ALIGN_LOOP_GAIN * (yawSetPoint - Robot.imu.getYawDeg());

			robotDrive.arcadeDrive(fwdStick, error + yawCorrect( fwdStick));			
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
			m.reverseSensor(REVERSE_SENSOR); 
			m.setPosition(0.0);
			
			m.setPID(0.4, 0.0, 0.0); // Might be able to increase gain a bit
			m.setF(0.0);
			m.setIZone(0);
			m.setCloseLoopRampRate(50.0);    // Smoothes things a bit
			m.setAllowableClosedLoopErr(8);  // Specified in CANTalon "ticks"
			m.configNominalOutputVoltage(+4.5, -4.5);
			m.configPeakOutputVoltage(+12.0, -12.0);			
		}
		
		public void doLockDrive(double value) {
			leftFrontMotor.set(value);
			leftRearMotor.set(leftFrontMotor.getDeviceID());
			rightFrontMotor.set(value);
			rightRearMotor.set(rightFrontMotor.getDeviceID());			
		}
		
		private double yawCorrect( double fwdStick) {
			return YAW_CORRECT_STICK * fwdStick + YAW_CORRECT_VELOCITY*getFwdVelocity_fps();
		}	
		
		public double getPositionFt() {
			return getLeftPositionFt();
		}
		
		public double getLeftPositionFt() {
			return FEET_PER_WHEEL_ROT * leftFrontMotor.getPosition();			
		}
		
		public double getRightPositionFt() {
			return FEET_PER_WHEEL_ROT * rightFrontMotor.getPosition();						
		}

		public double getFwdVelocity_fps() {
			double fwdSpeedRpm = (leftFrontMotor.getSpeed() + rightFrontMotor.getSpeed())/2.0;
			return FEET_PER_WHEEL_ROT / 60.0 * fwdSpeedRpm;
		}
		
		public void initDefaultCommand() {
			setDefaultCommand(new Idle());
		}
}

