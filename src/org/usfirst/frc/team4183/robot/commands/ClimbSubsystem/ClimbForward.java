package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;


import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbForward extends Command {

    public ClimbForward() {
    	requires(Robot.climbSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climbSubsystem.on(1.0);
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	if ( (timeSinceInitialized() < 1.0) && (Robot.climbSubsystem.getCurrent() >= 40) ) {
    		return CommandUtils.stateChange( this, new ClimbReverse() );
    		
    	}
    	if ( (timeSinceInitialized() > 1.0) && 
    	   ( (Robot.climbSubsystem.getCurrent() >=40) || Robot.climbSubsystem.bumperSwitch() )
    	) 
    	{
    		return CommandUtils.stateChange( this, new ClimbFinish() );
    	}
    	
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
