package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;


import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.robot.LightingControl;


import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestDirection extends Command {

	
    public TestDirection() {
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
    	// Use relatively low drive to avoid stress on climber
    	Robot.climbSubsystem.on( 0.2);
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// After .5 sec, inrush & transients are done
    	if (timeSinceInitialized() > 0.5) {
    		
    		// Measured 1-1.3A if free running, 4.4A if blocked;
    		// thus 2.5A as the threshold
    		if( Robot.climbSubsystem.getCurrent() > 2.5)
    			Robot.climbSubsystem.reverse();
    		
    		return CommandUtils.stateChange(this, new OpControl());	
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
