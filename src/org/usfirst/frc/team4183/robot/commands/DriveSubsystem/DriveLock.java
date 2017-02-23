package org.usfirst.frc.team4183.robot.commands.DriveSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class DriveLock extends Command 
{

    public DriveLock() 
    {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	Robot.lightingControl.set(LightingObjects.DRIVE_SUBSYSTEM,
                                  LightingControl.FUNCTION_ON,
                                  LightingControl.COLOR_GREEN,
                                  0,	// nspace - don't care
                                  0);	// period_msec - don't care   
    	
    	
        Robot.driveSubsystem.setLockDrive(true);
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	if(OI.btnUnjam.get()){
    		Robot.driveSubsystem.doLockDrive(.2*squareWave(0.5));
    	}
    	Robot.driveSubsystem.doLockDrive();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if( OI.btnDriveLock.get())
    	{
    		// Stay in state
    		return false; 
    	}
    	
    	if (OI.btnAlignLock.get() && !(OI.btnDriveLock.get() || OI.btnUnjam.get())) //second part might need fix
    	{
    		return CommandUtils.stateChange(this, new AlignLock());
    	}
    	else
    	{
    		return CommandUtils.stateChange(this, new DriverControl());
    	}
    	
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	Robot.driveSubsystem.setLockDrive(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
