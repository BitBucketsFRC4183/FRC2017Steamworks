package org.usfirst.frc.team4183.robot.commands.DriveSubsystem;

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
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.driveSubsystem);
    	setRunWhenDisabled(true);  // Idle state needs this!
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.driveSubsystem.disable();
    	Robot.lightingControl.setSleeping(LightingObjects.DRIVE_SUBSYSTEM);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if( Robot.runMode == Robot.RunMode.TELEOP || 
    		Robot.runMode == Robot.RunMode.AUTO
    	) 
    	{
    		return CommandUtils.stateChange(this, new DriverControl());
    	}
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Robot.driveSubsystem.enable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
