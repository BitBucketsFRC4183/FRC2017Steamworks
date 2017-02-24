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
	
	// At this distance, getting close
	private final double PRETTY_CLOSE_FT = 2.0;
	
	// Exit tolerance;
	// THIS MUST BE LARGER than "DEAD_ZONE_FT" in DriveStraight,
	// or you can get stuck in an infinite state loop.
	private final double ALLOWABLE_ERR_FT = 1.5/12.0;
	
	// If there seems to be some systematic error, can try to correct it here.
	// A positive value will push the overall path to the left (from behind),
	// CCW (from above).
	private final double YAW_FUDGE_DEG = 0.0;
		
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

    	if( distSamples.size() < REQUIRED_SAMPLES 
    		|| 
    		yawSamples.size() < REQUIRED_SAMPLES) {

    		return false;
    	}

    	// Use median value of the collected samples
    	Collections.sort(distSamples);
    	double distance = distSamples.get(distSamples.size()/2);
    	Collections.sort(yawSamples);
    	double yaw = yawSamples.get(yawSamples.size()/2);

		SmartDashboard.putNumber("GearYaw", yaw);
		SmartDashboard.putNumber("GearDist", distance);

    	if( Math.abs(distance) < ALLOWABLE_ERR_FT ) {
    		
    		// Done measuring, move on.
    		// As usual, state chain built last-to-first.
    		Command c = new DeliverGear();
    		c = new TurnBy(-5.0, c);
    		c = new TurnBy(5.0, c);
    		c = new TurnBy( -yaw, c);
    		return CommandUtils.stateChange(this, c);
    	}
    	else if( distance < PRETTY_CLOSE_FT) {

    		// Not done, but pretty close; 
    		// drive the remaining distance & look again.
    		Command c = new MeasureGear();
    		c = new DriveStraight( distance, c);
    		c = new TurnBy( -yaw + YAW_FUDGE_DEG, c);
    		return CommandUtils.stateChange(this, c);
    	}
    	else {

    		// Not close yet, drive 1/2 the distance & look again
    		Command c = new MeasureGear();
    		c = new DriveStraight( distance*0.5, c);
    		c = new TurnBy( -yaw + YAW_FUDGE_DEG, c);
    		return CommandUtils.stateChange(this, c);
    	}    		
    }

    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
