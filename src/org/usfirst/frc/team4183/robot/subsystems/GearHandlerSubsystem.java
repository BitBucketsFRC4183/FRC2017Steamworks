package org.usfirst.frc.team4183.robot.subsystems;


import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {
	
	private final DoubleSolenoid gearGateSolenoid = new DoubleSolenoid(RobotMap.GEAR_HANDLER_PNEUMA_OPEN_CHANNEL, RobotMap.GEAR_HANDLER_PNEUMA_CLOSED_CHANNEL); 

	private final CANTalon gearHandlerMotor = new CANTalon(RobotMap.GEAR_HANDLER_MOTOR_ID);
	private final double GEAR_RX_MOTOR_DRIVE = -0.5;
	private final double BALL_RX_MOTOR_DRIVE = 0.5;
	
	public GearHandlerSubsystem(){
	}
	
	public void enable(){
	}
		
	public void disable() {
		stopRoller();
		closeGate();
	}
	
	public void initDefaultCommand() {
	    setDefaultCommand(new Idle());
	}
	
	public void openGate() {
		gearGateSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	
	public void closeGate() {
		gearGateSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void spinRollerBalls() {
		gearHandlerMotor.set(BALL_RX_MOTOR_DRIVE);
	}
	
	public void spinRollerGear() {
		gearHandlerMotor.set(GEAR_RX_MOTOR_DRIVE);
	}
	
	public void stopRoller() {
		gearHandlerMotor.set(0);
	}
	
	
}
