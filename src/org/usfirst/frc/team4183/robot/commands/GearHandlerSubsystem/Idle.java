package org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command 
{

    public Idle() 
    {
        requires(Robot.gearHandlerSubsystem);
    	setRunWhenDisabled(true);  // Idle state needs this!
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.gearHandlerSubsystem.disable();
    	Robot.lightingControl.setSleeping(LightingObjects.GEAR_SUBSYSTEM);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if(Robot.oi.btnSpitGearA.get() && Robot.oi.btnSpitGearB.get()) 
    	{
    		return CommandUtils.stateChange(this, new GateOpen());
    	}
    	if(Robot.oi.btnWaitForGear.get()) 
    	{
    		return CommandUtils.stateChange(this, new WaitingForGear());
    	}
    	if(Robot.oi.btnWaitForBalls.get()) 
    	{
    		return CommandUtils.stateChange(this, new WaitingForBalls());
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
