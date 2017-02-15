package org.usfirst.frc.team4183.robot.commands.GearHandlerSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WaitingForGear extends Command {

    public WaitingForGear() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.gearHandlerSubsystem);
    
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.gearHandlerSubsystem.closeGate();
    	OI.softDriveLock.push();
   }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.gearHandlerSubsystem.spinRollerGear();
    	Robot.lightingControl.set(LightingObjects.GEAR_SUBSYSTEM,
                                  LightingControl.FUNCTION_REVERSE,
                                  LightingControl.COLOR_GREEN,
                                  4,		// nspace - good for either 8 or 16 pixel strips
                                  200);		// period_msec    		
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(OI.btnIdle.get()) {
    		return CommandUtils.stateChange(this, new Idle());
    	}
    	if( OI.btnWaitForBalls.get()) {
    		return CommandUtils.stateChange(this, new WaitingForBalls());
    	}
    	
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	OI.softDriveLock.release();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
