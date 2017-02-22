package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WaitForBounce extends Command {

    public WaitForBounce() {
        requires(Robot.climbSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.lightingControl.set(LightingObjects.CLIMB_SUBSYSTEM, 
	              LightingControl.FUNCTION_ON, 
	              LightingControl.COLOR_ORANGE,
	              0,		// nspace - don't care
	              0);		// period_ms - don't care
    	
    	Robot.climbSubsystem.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (timeSinceInitialized() >= 1.0)
        {
        	return CommandUtils.stateChange(this, new ClimbForward());
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
