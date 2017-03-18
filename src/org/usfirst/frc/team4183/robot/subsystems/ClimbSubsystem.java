package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.Idle;

import org.usfirst.frc.team4183.utils.TalonCurrentLogger;

public class ClimbSubsystem extends Subsystem {

	private static final double CLIMB_MOTOR_CURRENT_LIMIT_AMPS = 40.0;
	private CANTalon climbMotorA;
	private CANTalon climbMotorB; 
	private TalonCurrentLogger loggerA;
	private TalonCurrentLogger loggerB;
	
	
	private boolean loggerRun = true;  //Set this to turn on or off the loggers
	private double direction = -1.0; 
	

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public ClimbSubsystem(){
		climbMotorA = new CANTalon(RobotMap.CLIMB_MOTOR_A_ID);
		climbMotorB = new CANTalon(RobotMap.CLIMB_MOTOR_B_ID);
		
		loggerA = new TalonCurrentLogger(climbMotorA, "climbA");
		loggerB = new TalonCurrentLogger(climbMotorB, "climbB");
	}
	

	public void startLogger()
	{
		if (loggerRun) {
			loggerA.start();
			loggerB.start(); 
		}
	}
	
	public void enable() {
	}
	
	public void disable() {
		climbMotorA.set(0.0);
		climbMotorB.set(0.0);
	}
	
	public void off()
	{
		climbMotorA.set(0.0);
		climbMotorB.set(0.0);
	}
	
	public void on(double speed)
	{
		if (speed < 0.0) {
			speed = 0.0;
		}
		climbMotorA.set(direction*speed);
		climbMotorB.set(direction*speed);
	}
	
	public void reverse() {			
		direction *= -1.0;
	}
	
	
	public double getCurrent()
	{
		double climbMotorACurrent = climbMotorA.getOutputCurrent();
		double climbMotorBCurrent = climbMotorB.getOutputCurrent();
		if (climbMotorACurrent > climbMotorBCurrent)
		{
			return climbMotorACurrent;  
		}
		else
		{
			return climbMotorBCurrent; 
		}
	}
	
	public boolean isPastCurrentLimit()
	{
		return (getCurrent() >= CLIMB_MOTOR_CURRENT_LIMIT_AMPS);
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new Idle());
	}

}





