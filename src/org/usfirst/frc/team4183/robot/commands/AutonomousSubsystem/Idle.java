package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

    public Idle() {
    	requires( Robot.autonomousSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	OI.axisForward.set(0.0);
    	OI.axisTurn.set(0.0);
    	OI.btnIdle.push();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if( Robot.runMode == Robot.RunMode.AUTO ) {
    		// TODO go to 1st state here
    		// This transition is just for testing
    		return CommandUtils.stateChange(this, new TurnBy(10.0, new End()));
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
