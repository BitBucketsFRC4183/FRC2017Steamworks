package org.usfirst.frc.team4183.robot.commands.BallManipSubsystem;

import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.LightingControl.LightingObjects;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooting extends Command {
	
	OI.ButtonEvent btnShooting;

    public Shooting() {
        requires(Robot.ballManipSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() { 
    	
    	// TODO: Change to execute on changes on alignment
       	Robot.lightingControl.set(LightingObjects.BALL_SUBSYSTEM,
                LightingControl.FUNCTION_ON,
                LightingControl.COLOR_ORANGE,
                0,		// nspace - don't care
                0);		// period_ms - don't care 
       	
    	btnShooting = OI.getBtnEvt(OI.btnShoot);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.ballManipSubsystem.setConveyerOn();
    	//Robot.ballManipSubsystem.setTopRollerToSpeed(SmartDashboard.getNumber("Top Motor Speed", 0.0)));
    	Robot.ballManipSubsystem.setTopRollerToShootingSpeed();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(btnShooting.onReleased()) {
    		Robot.ballManipSubsystem.setConveyerOff();
    		return CommandUtils.stateChange(this, new WaitingForTrigger());
    	}
    	if(!OI.sbtnShoot.get() && Robot.runMode.equals(Robot.RunMode.AUTO)) {
    		Robot.ballManipSubsystem.setConveyerOff();
    		return CommandUtils.stateChange(this, new Idle());
    	}
    	if(OI.btnIdle.get()) {
    		return CommandUtils.stateChange(this, new Idle());
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
