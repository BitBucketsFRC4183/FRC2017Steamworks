package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.MotorRunCommand;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 *
 */
public class PrototypeSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private final int ENCODER_CODES_PER_ENCODER_REV = 256;	//Codes encoder is set to
	
	public double pValue = 0.0;
	public double iValue = 0.0;
	public double dValue = 0.0;
	
	public CANTalon[] motors;
	
	private int loopcnt = 0;
	
	public double motorVelocity = 0;
	
	public PrototypeSubsystem() {
		motors[0] = new CANTalon(RobotMap.testMotor1);
		/** This is where you would add more motors if its required*/
	}
	
	public void setPosition(double position) {

		CANTalon m = motors[0];
		m.set(position);
		
		// Must set all the motors to keep MotorSafety happy
		for (int i = 1 ; i < motors.length ; i++)
			motors[i].set(m.getDeviceID());

		if (++loopcnt >= 50) {
			loopcnt = 0;
			System.out.println(
					"Trg:" + position + " FB:" + m.get() + " Drv:" + m.getOutputVoltage() + " Err:" + m.getError());
		}
	}
	
	public void setSpeed(double speed) {

		CANTalon m = motors[0];
		m.set(speed);

		// Must set all the motors to keep MotorSafety happy
		for (int i = 1 ; i < motors.length ; i++)
			motors[i].set(m.getDeviceID());

		if (++loopcnt >= 50) {
			loopcnt = 0;
			System.out.println(
					"Trg:" + speed + " FB:" + m.get() + " Drv:" + m.getOutputVoltage() + " Err:" + m.getError());
		}
	}
	
	public void initSpeedMode() {
		System.out.println("Init Speed Mode");
		
		CANTalon m = motors[0];
		m.changeControlMode(CANTalon.TalonControlMode.Speed);
		
		for(CANTalon talon : motors) {
			talon.setInverted(false);
			talon.setVoltageRampRate(0.0);
		}
		
		m.reverseOutput(true);
		
		m.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		m.configEncoderCodesPerRev(ENCODER_CODES_PER_ENCODER_REV);
		
		m.reverseSensor(true);
		
		for(int i = 1; i < motors.length; i++) {
			motors[i].changeControlMode(CANTalon.TalonControlMode.Follower);
			motors[i].set(m.getDeviceID());
		}
		/** Reverse motors here as needed: */
		//motors[1].reverseOutput(true);
		
		m.setPID(pValue, iValue, dValue);
		m.setF(0.1);
		m.setIZone(0);
		m.setCloseLoopRampRate(0.0);  // Works better disabled
		m.setAllowableClosedLoopErr(0);
		m.configNominalOutputVoltage(0.0, 0.0);
		m.configPeakOutputVoltage(+12.0, -12.0);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new MotorRunCommand());
    }
}

