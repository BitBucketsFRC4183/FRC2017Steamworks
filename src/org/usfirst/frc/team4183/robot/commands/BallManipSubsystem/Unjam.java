package org.usfirst.frc.team4183.robot.commands.BallManipSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Unjam extends Command {
	
	OI.ButtonEvent btnUnjam;
	
    public Unjam() {
        requires(Robot.ballManipSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	OI.btnUnjam.push();
    	
    	btnUnjam = OI.getBtnEvt(OI.btnUnjam);
    	
    	//add lights
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ballManipSubsystem.setTopRollerUnjam();
    	Robot.ballManipSubsystem.setConveyerReverse();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (btnUnjam.onReleased()){
        	return CommandUtils.stateChange(this, new WaitingForShooterSpeed());
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
