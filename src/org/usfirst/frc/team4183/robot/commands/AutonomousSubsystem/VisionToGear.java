package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class VisionToGear extends Command {
	
	private final double DELAY = 0.5;
	
    public VisionToGear() {
    	requires( Robot.autonomousSubsystem);
    }

    protected void initialize() {
    	Robot.visionSubsystem.setGearMode();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
    	if( timeSinceInitialized() > DELAY )
    		return CommandUtils.stateChange(this, new MeasureGear());
    	
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
