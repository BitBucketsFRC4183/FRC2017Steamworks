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
	private final double gearRecieveMotorSpeed_PVBUS;
	private final double ballReceiveMotorSpeed_PVBUS;
	
	public GearHandlerSubsystem(){
		Preferences prefs = Preferences.getInstance();
		gearRecieveMotorSpeed_PVBUS = prefs.getDouble("GearLoadSpeed", -0.1);
		ballReceiveMotorSpeed_PVBUS = prefs.getDouble("BallLoadSpeed", 0.1);
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
		gearHandlerMotor.set(ballReceiveMotorSpeed_PVBUS);
	}
	
	public void spinRollerGear() {
		gearHandlerMotor.set(gearRecieveMotorSpeed_PVBUS);
	}
	
	public void stopRoller() {
		gearHandlerMotor.set(0);
	}
	
	
}
