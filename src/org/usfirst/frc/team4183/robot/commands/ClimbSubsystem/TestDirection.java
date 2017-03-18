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
    	Robot.climbSubsystem.on(1.0);
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if (timeSinceInitialized() > .5) {
    		return CommandUtils.stateChange(this, new OpControl());	
    	}
    	if (Robot.climbSubsystem.isPastCurrentLimit()) {
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
