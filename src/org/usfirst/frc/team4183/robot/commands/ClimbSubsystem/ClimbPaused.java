package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbPaused extends Command {

    public ClimbPaused() {
        requires(Robot.climbSubsystem);
    }

    // We are so done...
    
    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.climbSubsystem.disable();
    	Robot.climbSubsystem.setPaused(); // Remember that we entered a paused condition
    									  // This is how the other states will know
    	
    	Robot.lightingControl.set(LightingObjects.CLIMB_SUBSYSTEM, 
					              LightingControl.FUNCTION_BLINK, 
					              LightingControl.COLOR_RED,
					              0,	// nspace - don't care
					              300);	// period_ms
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// Allow climb to resume in the direction it was going
    	// previously by checking for both buttons (this is a
    	// robot safety feature to ensure that user really wants
    	// to resume... if current or switch is triggered we be
    	// back here immediately, but at least it won't be software
    	// that holds the climb stopped if a swing or bounce or
    	// tight belt causes a current limit or switch error!)
    	if (OI.btnResumeClimb.get() && OI.btnClimbControl.get())
    	{
    		if (Robot.climbSubsystem.isForward())
    		{
    			return CommandUtils.stateChange(this, new ClimbForward());
    		}
    		else
    		{
    			return CommandUtils.stateChange(this, new ClimbReverse());
    		}
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
