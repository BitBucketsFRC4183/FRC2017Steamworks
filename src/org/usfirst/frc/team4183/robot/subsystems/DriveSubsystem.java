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

		// Nominal value assuming 4" wheel:
		// 12.57 in/rot = (4" * pi) in/rot
		private final double INCH_PER_WHEEL_ROT = RobotMap.INCH_PER_WHEEL_ROT;

		// Can adjust these to make the robot drive straight with 
		// zero turn stick in DriverControl.
		// +Values will add +yaw correct (CCW viewed from top) when going forward.
		private final double YAW_CORRECT_VELOCITY = 0.0;  // Multiplied by inch/sec so value will be small!
		private final double YAW_CORRECT_ACCEL = 0.0;
		
		private final double LOW_SENS_GAIN = 0.6;		
		private final double ALIGN_LOOP_GAIN = 0.04;

		// The counts-per-rev is printed on the encoder -
		// it's the 1st number after the "E4P" or "E4T"
		private final int ENCODER_PULSES_PER_REV = 250; 
		private final boolean REVERSE_SENSOR = false;  

		
		private final CANTalon leftFrontMotor;
		private final CANTalon leftRearMotor; 
		private final CANTalon rightFrontMotor;
		private final CANTalon rightRearMotor;
	    
		private final RobotDrive robotDrive;
		
		private double yawSetPoint;		
		private boolean linearAxis;
		
		private final boolean HUMAN_WANTS_BRAKING = true;
		
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
			
			// Human control by default
			setAutonomousControl(false);
		}	

		
		public void disable() {
			robotDrive.arcadeDrive(0.0, 0.0);			
		}
		
		// Human driver: 
		//  -likes "squared controls" (easier to drive)
		//  -wants braking set according to HUMAN_WANTS_BRAKING
		// Autonomous "driver":
		//  -hates squared controls (messes up the control loops)
		//  -Always wants braking, to settle quicker
		public void setAutonomousControl( boolean auto) {			
			linearAxis = auto;
			
			if( auto)
				setBraking(true);
			else
				setBraking(HUMAN_WANTS_BRAKING);
		}
	
		private void setBraking( boolean setting) {
			leftFrontMotor.enableBrakeMode(setting);
			leftRearMotor.enableBrakeMode(setting);
			rightFrontMotor.enableBrakeMode(setting);
			rightRearMotor.enableBrakeMode(setting);
		}
		
		private double shapeAxis( double x) {
			if( linearAxis) 
				return x;
			else {
				x = Deadzone.f( x, .05);
				return Math.signum(x) * (x*x);
			}
		}
		
		// +turnStick produces right turn (CW from above, -yaw angle)
		public void arcadeDrive(double fwdStick, double turnStick) {
			
			if(OI.btnLowSensitiveDrive.get()) {
				fwdStick *= LOW_SENS_GAIN;
				turnStick *= LOW_SENS_GAIN;
			}
			if(OI.btnInvertAxis.get()) {
				fwdStick *= -1.0;
			}
			
			fwdStick = shapeAxis(fwdStick);
			turnStick = shapeAxis(turnStick);
						
			if( fwdStick == 0.0 && turnStick == 0.0) {
				setAllMotorsZero();
			}
			else {
				// Turn stick is + to the right;
				// but arcadeDrive 2nd arg + produces left turn
				// (this is +yaw when yaw is defined according to right-hand-rule
				// with z-axis up, so arguably correct).
				// Anyhow need the - sign on turnStick to make it turn correctly.
				robotDrive.arcadeDrive( fwdStick, -turnStick + yawCorrect(), false);
			}
		}
		
		
		public void setAlignDrive(boolean start) {
			if(start) {
				yawSetPoint = Robot.imu.getYawDeg();
			} 
		}
		
		public void doAlignDrive(double fwdStick, double turnStick) {
						
			if(OI.btnLowSensitiveDrive.get())
				fwdStick *= LOW_SENS_GAIN;
			
			if(OI.btnInvertAxis.get()) {
				fwdStick *= -1.0;
			}
			
			fwdStick = shapeAxis(fwdStick);
			turnStick = shapeAxis(turnStick);
						
			if( fwdStick == 0.0 && turnStick == 0.0) {
				setAllMotorsZero();
			}
			else {
				
				// Turn stick is + to the right,
				// +yaw is CCW looking down,
				// so + stick should lower the setpoint. 
				yawSetPoint += -0.3 * turnStick;
				
				double error = ALIGN_LOOP_GAIN * (yawSetPoint - Robot.imu.getYawDeg());				
				robotDrive.arcadeDrive( fwdStick, error + yawCorrect(), false);
			}
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
			m.configNominalOutputVoltage(+4.0, -4.0);
			m.configPeakOutputVoltage(+12.0, -12.0);			
		}
		
		public void doLockDrive(double value) {
			leftFrontMotor.set(value);
			leftRearMotor.set(leftFrontMotor.getDeviceID());
			rightFrontMotor.set(value);
			rightRearMotor.set(rightFrontMotor.getDeviceID());			
		}
		
		private double yawCorrect() {
			return YAW_CORRECT_VELOCITY * getFwdVelocity_ips() 
					+ YAW_CORRECT_ACCEL * getFwdCurrent();
		}
		
		private void setAllMotorsZero() {
			leftFrontMotor.set(0.0);
			leftRearMotor.set(0.0);
			rightFrontMotor.set(0.0);
			rightRearMotor.set(0.0);			
		}
		
		public double getPosition_inch() {
			return getLeftPosition_inch();
		}
		
		public double getLeftPosition_inch() {
			return INCH_PER_WHEEL_ROT * leftFrontMotor.getPosition();			
		}
		
		public double getRightPosition_inch() {
			// Right motor encoder reads -position when going forward!
			return -INCH_PER_WHEEL_ROT * rightFrontMotor.getPosition();						
		}

		public double getFwdVelocity_ips() {
			// Right side motor reads -velocity when going forward!
			double fwdSpeedRpm = (leftFrontMotor.getSpeed() - rightFrontMotor.getSpeed())/2.0;
			return (INCH_PER_WHEEL_ROT / 60.0) * fwdSpeedRpm;
		}
		
		public double getFwdCurrent() {
			// OutputCurrent always positive so apply sign of drive voltage to get real answer.
			// Also, right side has -drive when going forward!
			double leftFront = leftFrontMotor.getOutputCurrent() * Math.signum( leftFrontMotor.getOutputVoltage());
			double leftRear = leftRearMotor.getOutputCurrent() * Math.signum( leftRearMotor.getOutputVoltage());
			double rightFront = -rightFrontMotor.getOutputCurrent() * Math.signum( rightFrontMotor.getOutputVoltage());
			double rightRear = -rightRearMotor.getOutputCurrent() * Math.signum( rightRearMotor.getOutputVoltage());
			return (leftFront + leftRear + rightFront + rightRear)/4.0;
		}
		
		public void initDefaultCommand() {
			setDefaultCommand(new Idle());
		}
}

