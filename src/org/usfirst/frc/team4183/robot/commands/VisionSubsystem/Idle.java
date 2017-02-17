package org.usfirst.frc.team4183.robot.commands.VisionSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.subsystems.VisionSubsystem;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

	OI.ButtonEvent btnToggleCamMode;
	OI.ButtonEvent btnFrontCamMode;
	OI.ButtonEvent btnRearCamMode;

    public Idle() 
    {
        requires(Robot.visionSubsystem);
        setRunWhenDisabled(true);  // Required for Idle states!
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	btnToggleCamMode = OI.getBtnEvt(OI.btnToggleFrontCameraView);
    	btnFrontCamMode = OI.getBtnEvt(OI.btnSelectFrontCam);
    	btnRearCamMode = OI.getBtnEvt(OI.btnSelectRearCam);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(btnToggleCamMode.onPressed()) 
    	{
    		if(Robot.visionSubsystem.isBoilerMode()) 
    		{
    			Robot.visionSubsystem.setGearMode();
    		}
    		else if(Robot.visionSubsystem.isGearMode()) 
    		{
    			Robot.visionSubsystem.setBoilerMode();
    		}
    	}
    	if(btnFrontCamMode.onPressed()) 
    	{
    		Robot.visionSubsystem.setFrontCam();
    	}
    	else if(btnRearCamMode.onPressed()) 
    	{
    		Robot.visionSubsystem.setRearCam();
    	}
    	
    	// TODO: Not ideal but will do for now
    	if(Robot.visionSubsystem.isBlueAlliance())
    	{
    		if( ! VisionSubsystem.currentAllianceColor.equals(VisionSubsystem.BLUE_ALLIANCE)) 
    		{
    			Robot.visionSubsystem.setRedAlliance();
    		}
    	} 
    	else if(Robot.visionSubsystem.isRedAlliance()) 
    	{
    		if( ! VisionSubsystem.currentAllianceColor.equals(VisionSubsystem.RED_ALLIANCE)) 
    		{
    			Robot.visionSubsystem.setBlueAlliance();
    		}
    	}
    	
    	if( Robot.visionSubsystem.getAllianceNumber() != VisionSubsystem.currentAllianceLocation)
    	{
    		Robot.visionSubsystem.setAllianceNumber();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() 
    {
    	end();
    }
}
