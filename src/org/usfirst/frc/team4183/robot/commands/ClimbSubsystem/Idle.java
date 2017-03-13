package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;


/**
 *
 */
public class Idle extends Command {

    public Idle() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.climbSubsystem);
    	setRunWhenDisabled(true);  // Idle state needs this!
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.climbSubsystem.initialize(); // Reset some states when starting over (Makes it testable)
    	Robot.climbSubsystem.disable();
    	Robot.lightingControl.setSleeping(LightingObjects.CLIMB_SUBSYSTEM);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climbSubsystem.off();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if( OI.btnClimbControl.get() )
    	{
    		return CommandUtils.stateChange(this, new WaitForBounce());
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
