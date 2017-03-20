package org.usfirst.frc.team4183.robot.commands.ClimbSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.utils.Deadzone;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpControl extends Command {

    public OpControl() {
        requires(Robot.climbSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	 Robot.climbSubsystem.on(Deadzone.f(OI.axisClimb.get(), .05));
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (timeSinceInitialized() > .25
    		&&
    		Robot.climbSubsystem.isPastCurrentLimit()) {
    		
    		return CommandUtils.stateChange(this, new ClimbPaused());
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
