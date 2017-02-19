package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4183.robot.commands.HopperSubsystem.Idle;

/**
 *
 */
public class HopperSubsystem extends Subsystem 
{
	DoubleSolenoid hopperSolenoid = new DoubleSolenoid(RobotMap.HOPPER_OPEN_PNEUMA_CHANNEL, RobotMap.HOPPER_CLOSE_PNEUMA_CHANNEL);

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() 
    {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Idle()); 
    }
    
    public void open()
    {
    	hopperSolenoid.set(DoubleSolenoid.Value.kForward);    	
    }
    public void close()
    {
    	hopperSolenoid.set(DoubleSolenoid.Value.kReverse);    	
    }
}

