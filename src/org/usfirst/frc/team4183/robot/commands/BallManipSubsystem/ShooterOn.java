package org.usfirst.frc.team4183.robot.commands.BallManipSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ShooterOn extends Command {

    public ShooterOn() {
        requires(Robot.ballManipSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Subsystem actions must be called every cycle to keep
    	// things running
    	Robot.ballManipSubsystem.setTopRollerToShootingSpeed();
    	Robot.ballManipSubsystem.setConveyerOn();
    	Robot.ballManipSubsystem.setSweeperOff();
    	Robot.ballManipSubsystem.setFlapModeShoot();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(OI.btnBallIdle.get()){
        	return CommandUtils.stateChange(this, new Idle());  
        }
        if(OI.btnIntakeOn.get()){
        	return CommandUtils.stateChange(this, new IntakeOn());
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
