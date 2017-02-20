package org.usfirst.frc.team4183.robot.commands.DriveSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

// TODO: This entire state may be unnecessary if autonomy simply controls via logical axis and logical buttons

public class AutonomousControl extends Command {

    public AutonomousControl() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.lightingControl.set(LightingObjects.DRIVE_SUBSYSTEM,
                LightingControl.FUNCTION_BLINK,
                LightingControl.COLOR_BLUE,
                0,			// nspace - don't care
                300);		// period_msec    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
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
