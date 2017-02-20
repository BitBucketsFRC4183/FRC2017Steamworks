package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

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
    	
    	if( Robot.runMode == Robot.RunMode.AUTO ) {
    		// This is just for testing
    		
    		// Build the chain backwards (last to first)
    		/*
    		Command n3 = new End();
    		Command n2 = new TurnBy(45.0, n3);
    		Command n1 = new DriveBy(3.0, n2);
    		*/
    		
    		/*
    		Command n2 = new End();
    		Command n1 = new DriveBy(3.0, n2);
    		 */
    		
    		Command n2 = new End();
    		Command n1 = new TurnBy(45.0, n2);
    		
    		
    		return CommandUtils.stateChange(this, n1); 
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
