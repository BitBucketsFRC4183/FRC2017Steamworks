package org.usfirst.frc.team4183.robot.commands.BallManipSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

    public Idle() {
        requires(Robot.ballManipSubsystem);
    	setRunWhenDisabled(true);  // Idle state needs this!
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.ballManipSubsystem.disable();
    	Robot.lightingControl.setSleeping(LightingObjects.BALL_SUBSYSTEM);    	
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
        	return CommandUtils.stateChange(this, new WaitingForShooterSpeed());
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
