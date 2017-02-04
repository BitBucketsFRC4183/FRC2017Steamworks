package org.usfirst.frc.team4183.robot.subsystems;


import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {
	
	DoubleSolenoid gearGateSolenoid = new DoubleSolenoid(RobotMap.GEAR_HANDLER_PNEUMA_OPEN_CHANNEL, RobotMap.GEAR_HANDLER_PNEUMA_CLOSED_CHANNEL); 
	
	public void enable() {
	}
	
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
		
	}
	
	public void spinRollerGear() {
		
	}
	
	public void stopRoller() {
		
	}
	
}
