package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem.Idle;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbReverse extends Command {

    public ClimbReverse() {
    	requires(Robot.climbSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.lightingControl.set(LightingObjects.CLIMB_SUBSYSTEM, 
					              LightingControl.FUNCTION_BLINK, 
					              LightingControl.COLOR_ORANGE,
					              0,		// nspace - don't care
					              300);		// period_ms
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climbSubsystem.onReverse();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// Always pause when exiting so a resume is possible
    	if ( OI.btnIdle.get() ||
    		((timeSinceInitialized() > 0.200) && 
    		 ((Robot.climbSubsystem.isPastCurrentLimit()) || Robot.climbSubsystem.bumperSwitch() )
    		)
    	) {
    		return CommandUtils.stateChange( this, new ClimbPaused() ); 
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
