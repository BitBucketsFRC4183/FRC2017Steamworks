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
public class WaitingForShooterSpeed extends Command {

	public WaitingForShooterSpeed() {
		requires(Robot.ballManipSubsystem);
	}

	// Called just before this Command runs the first time
	protected void initialize() 
	{
		Robot.ballManipSubsystem.setFlapModeShoot();
		Robot.lightingControl.set(LightingObjects.BALL_SUBSYSTEM, 
				LightingControl.FUNCTION_BLINK, 
				LightingControl.COLOR_RED,
				0,		// nspace - don't care
				300);		// period_ms

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.ballManipSubsystem.setTopRollerToShootingSpeed();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if(OI.btnIdle.get()) {
			return CommandUtils.stateChange(this, new Idle());
		}

		//TODO Next transition should be top roller at shooting speed

		if(true) {
			return CommandUtils.stateChange(this, new WaitingForTrigger());
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
