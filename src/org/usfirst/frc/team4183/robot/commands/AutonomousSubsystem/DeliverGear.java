package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DeliverGear extends Command {

	private final double DELAY = 0.5;
	
    public DeliverGear() {
    	requires( Robot.autonomousSubsystem);
    }

    protected void initialize() {
    	OI.btnSpitGearA.push();
    	OI.btnSpitGearB.push();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
    	if( timeSinceInitialized() > DELAY) {
    		// Back up, then End
    		return CommandUtils.stateChange(this, new DriveStraight(-1.0, new End()));
    	}
        return false;
    }

    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
