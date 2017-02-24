package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MeasureGear extends Command {
	

		
	// Number for non-NAN samples needed before acting
	private final int REQUIRED_SAMPLES = 30;

	private final boolean SD_DEBUG = true;
	private int totCnt = 0, nanCnt = 0;
	
	private List<Double> yawSamples = new ArrayList<>();
	private List<Double> distSamples = new ArrayList<>();

    public MeasureGear() {
    	requires( Robot.autonomousSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
    	
    	totCnt += 2;
    	
    	double yawSample = Robot.visionSubsystem.getGearAngle_deg();
    	if( !Double.isNaN(yawSample)) {
    		yawSamples.add(yawSample);
    		
    		if( SD_DEBUG) 
    			SmartDashboard.putNumber("GearYaw", yawSample);
    	}
    	else 
    		nanCnt++;
    	
    	
    	double distSample 
    		= Robot.visionSubsystem.getGearDistance_ft();
    	
    	if( !Double.isNaN(distSample)) {
    		distSamples.add(distSample);
    		if( SD_DEBUG) 
    			SmartDashboard.putNumber("GearDist", yawSample);
    	}
    	else
    		nanCnt++;
    	
    	if( SD_DEBUG)
    		SmartDashboard.putNumber("Pct_NaNs", (100.0*nanCnt)/totCnt);  		
    }

    protected boolean isFinished() {

    	return ( 
    		distSamples.size() >= REQUIRED_SAMPLES 
    		&& 
    		yawSamples.size() >= REQUIRED_SAMPLES
    	);
    }

    protected void end() {
    	
    	// Get median value of the collected samples
    	Collections.sort(distSamples);
    	double distance = distSamples.get(distSamples.size()/2);
    	Collections.sort(yawSamples);
    	double yaw = yawSamples.get(yawSamples.size()/2);

    	if( SD_DEBUG) {
    		SmartDashboard.putNumber("GearDist", distance);
    		SmartDashboard.putNumber("GearYaw", yaw);
    		SmartDashboard.putNumber("Pct_NaNs", (100.0*nanCnt)/totCnt);  		
    	}

    	// Stash the measured data for use by subsequent states
		Scripter.measuredDistance = distance;
		Scripter.measuredYaw = yaw;		
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
