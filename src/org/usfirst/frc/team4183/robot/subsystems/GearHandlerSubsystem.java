package org.usfirst.frc.team4183.robot.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;

public class GearHandlerSubsystem extends Subsystem {

	public void enable() {}
	
	public void disable() {}
	
	public void initDefaultCommand() {
	    // Set the default command for a subsystem here,
		// then delete the "throw"
	    //setDefaultCommand(new MySpecialCommand());

		throw new RuntimeException("Define a Default Command!");
	}
	
}
