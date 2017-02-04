package org.usfirst.frc.team4183.robot.subsystems;


import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {
	
	private final CANTalon BALL_GEAR_INTAKE_MOTOR = new CANTalon(RobotMap.GEAR_HANDLER_MOTOR);
	private static final double MOTOR_SPEED = 1.0;
	
	public void enable() {
		
	}
	
	public void disable() {
		stopRoller();
		closeGate();
	}
	
	public void initDefaultCommand() {
	    setDefaultCommand(new Idle());
	}
	
	public void openGate() {
		
	}
	
	public void closeGate() {
		
	}
	
	public void spinRollerBalls() {
		BALL_GEAR_INTAKE_MOTOR.set(MOTOR_SPEED);
	}
	
	public void spinRollerGear() {
		BALL_GEAR_INTAKE_MOTOR.set(-MOTOR_SPEED);
	}
	
	public void stopRoller() {
		BALL_GEAR_INTAKE_MOTOR.set(0);
	}
	
}
