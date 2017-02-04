package org.usfirst.frc.team4183.robot.subsystems;


import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {
	
	private final DoubleSolenoid gearGateSolenoid = new DoubleSolenoid(RobotMap.GEAR_HANDLER_PNEUMA_OPEN_CHANNEL, RobotMap.GEAR_HANDLER_PNEUMA_CLOSED_CHANNEL); 
	private final CANTalon gearHandlerMotor = new CANTalon(RobotMap.GEAR_HANDLER_MOTOR_ID);
	private static final double GEAR_RECEIVE_MOTOR_SPEED_PVBUS = 1.0;
	private static final double BALL_RECEIVE_MOTOR_SPEED_PVBUS = -1.0;
	
	public void enable() {}
	
	public void disable() {
		stopRoller();
		closeGate();
		gearGateSolenoid.set(DoubleSolenoid.Value.kOff);
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
		gearHandlerMotor.set(BALL_RECEIVE_MOTOR_SPEED_PVBUS);
	}
	
	public void spinRollerGear() {
		gearHandlerMotor.set(GEAR_RECEIVE_MOTOR_SPEED_PVBUS);
	}
	
	public void stopRoller() {
		gearHandlerMotor.set(0);
	}
	
}
