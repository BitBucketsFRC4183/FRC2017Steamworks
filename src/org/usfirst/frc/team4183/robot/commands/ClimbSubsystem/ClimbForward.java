package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;


import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbForward extends Command {

    public ClimbForward() {
    	requires(Robot.climbSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.lightingControl.set(LightingObjects.CLIMB_SUBSYSTEM, 
					              LightingControl.FUNCTION_BLINK, 
					              LightingControl.COLOR_GREEN,
					              0,		// nspace - don't care
					              300);		// period_ms
    	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climbSubsystem.onForward();
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if(OI.btnIdle.get()) {
    		return CommandUtils.stateChange(this, new ClimbPaused());
    	}
    	
    	double timeSince = timeSinceInitialized();
    	if (Robot.climbSubsystem.wasPaused())
    	{
    		// Rules for handling limits and switches are different when
    		// entering from a previously paused state because we will
    		// need to ignore immediate current spikes due to restarting
    		// the motor and we no longer need to worry about reversal
    		// I.e., if the current limit or switch is present after
    		// 200 ms we will stop again... if there is a switch problem
    		// or current problem but the user still needs climb then
    		// we can inche up the rope as needed
	    	if ( (timeSince >= 0.200) && 
	 	    	   ( (Robot.climbSubsystem.isPastCurrentLimit()) || Robot.climbSubsystem.bumperSwitch() )
	 	    	) 
 	    	{
 	    		return CommandUtils.stateChange( this, new ClimbPaused() );
 	    	}
    		
    	}
    	else
    	{
    		// First time through we will assume that we've just started climing
    		// for the first time and will just let any current error signal a
    		// need to reverse the direction (ratched problem); this MAY protect
    		// the drive, but can't guarantee that the chain/belt won't get damaged
    		// but we will use this to do the best we can
	    	if ( (timeSince < 1.0) && (Robot.climbSubsystem.isPastCurrentLimit()) ) {
	    		return CommandUtils.stateChange( this, new ClimbReverse() );
	    		
	    	}
	    	// After one second a current limit or switch definitely means
	    	// we have a problem or are at the top... pause and wait for
	    	// further instructions
	    	if ( (timeSince >= 1.0) && 
	    	   ( (Robot.climbSubsystem.isPastCurrentLimit()) || Robot.climbSubsystem.bumperSwitch() )
	    	) 
	    	{
	    		return CommandUtils.stateChange( this, new ClimbPaused() );
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
    }
}
