package org.usfirst.frc.team4183.robot.commands.HopperSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Open extends Command {

    public Open() 
    {
        requires(Robot.hopperSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.hopperSubsystem.open();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if (OI.btnCloseHopper.get() || (OI.sbtnShake.get() && timeSinceInitialized() >= 0.3))
    	{
    		return CommandUtils.stateChange(this, new Idle());
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
