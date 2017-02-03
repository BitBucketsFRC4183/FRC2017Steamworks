package org.usfirst.frc.team4183.robot.commands.BallManipSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

    public Idle() {
        requires(Robot.ballManipSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.ballManipSubsystem.disable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (OI.btnIntakeOn.get()){
        	return CommandUtils.stateChange(this, new IntakeOn());
        }
        if (OI.btnShooterStart.get()){
        	return CommandUtils.stateChange(this, new ShooterOn());
        }
    	return false;
        
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballManipSubsystem.enable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
