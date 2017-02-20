package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

	public Idle() {
		requires( Robot.autonomousSubsystem);
		setRunWhenDisabled(true);  // Idle state needs this!
	}


	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		int station;
		station = DriverStation.getInstance().getLocation();
		if( Robot.runMode == Robot.RunMode.AUTO ) {
			if (station == 1){
				return CommandUtils.stateChange(this, new TurnBy(180.0, new End()));

			}
			if (station == 2) {
				// This is a few inches short of the bass line, feel free to change this number!
				return CommandUtils.stateChange(this, new DriveBy(7.00, new End()));

			}	
			if (station == 3) {
				return CommandUtils.stateChange(this, new TurnBy(-180, new End()));
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
		end();
	}
}
