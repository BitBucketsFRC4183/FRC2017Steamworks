package org.usfirst.frc.team4183.robot.subsystems;


import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {
	
	private final CANTalon gearHandlerMotor = new CANTalon(RobotMap.GEAR_HANDLER_MOTOR_ID);
	private static final double MOTOR_SPEED_PVBUS = 1.0;
	
	public void enable() {}
	
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
		gearHandlerMotor.set(MOTOR_SPEED_PVBUS);
	}
	
	public void spinRollerGear() {
		gearHandlerMotor.set(-MOTOR_SPEED_PVBUS);
	}
	
	public void stopRoller() {
		gearHandlerMotor.set(0);
	}
	
}
