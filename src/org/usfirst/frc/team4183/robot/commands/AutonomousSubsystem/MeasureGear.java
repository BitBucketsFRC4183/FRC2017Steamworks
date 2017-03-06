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
	private final int REQUIRED_SAMPLES = 15;

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
    	}
    	else 
    		nanCnt++;
    	
    	
    	double distSample 
    		= Robot.visionSubsystem.getGearDistance_inch();
    	
    	if( !Double.isNaN(distSample)) {
    		distSamples.add(distSample);
    	}
    	else
    		nanCnt++;    	
    }

    protected boolean isFinished() {

    	return ( 
    		distSamples.size() >= REQUIRED_SAMPLES 
    		&& 
    		yawSamples.size() >= REQUIRED_SAMPLES
    	);
    }

    protected void end() {
    	
    	if( distSamples.size() == 0)
    		distSamples.add(Double.NaN);
    	if( yawSamples.size() == 0)
    		yawSamples.add(Double.NaN);

		// Get median value of the collected samples
    	Collections.sort(distSamples);
    	double distance = distSamples.get(distSamples.size()/2);
    	Collections.sort(yawSamples);
    	double yaw = yawSamples.get(yawSamples.size()/2);
    	

    	if( SD_DEBUG) {
    		SmartDashboard.putString("MeasGearDist", 
    			String.format("%.1f (%.1f<>%.1f)", 
    					distance, Collections.min(distSamples), Collections.max(distSamples)));    		
    		SmartDashboard.putString("MeasGearYaw",
    			String.format("%.1f (%.1f<>%.1f)", 
    					yaw, Collections.min(yawSamples), Collections.max(yawSamples)));
    		SmartDashboard.putString("Pct_NaNs", String.format("%.1f", (100.0*nanCnt)/totCnt) );  		
    	}

    	// Stash the measured data for use by subsequent states
		Scripter.measuredDistance_inch = distance;
		Scripter.measuredYaw_deg = yaw;		
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
