package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Idle extends Command {

    public Idle() {
    	requires( Robot.autonomousSubsystem);
    	setRunWhenDisabled(true);  // Idle state needs this!
    }
    
    Command testChain() {
    	
    	// Note all State chains are built in reverse order,
    	// last state listed first.
    	// Read from bottom to top!

		// These are just for testing.

    	Command c;
    	c = new VisionToGear();
    	c = new TurnBy(45.0, c);
    	c = new DriveStraight(3.0, c);
    	
		
		/*
		c = new End();
		c = new DriveBy(3.0, c);
		*/

		/*
		c = new End();
		c = new TurnBy(90.0, c);
		*/
		return c;		   	
    }
    
    Command location1chain() {
    	Command c;
    	c = new VisionToGear();
    	c = new TurnBy(45.0, c);  // TODO actual angle
    	c = new DriveStraight(8.0, c);  // TODO actual distance
    	return c;
    }
    
    private Command location2chain() {
    	Command c;
    	c = new VisionToGear();
    	c = new TurnBy(0.0, c);  // TODO actual angle
    	c = new DriveStraight(8.0, c);  // TODO actual distance
    	return c;
    }
    
    private Command location3chain() {   	
    	Command c;
    	c = new VisionToGear();
    	c = new TurnBy(-45.0, c);  // TODO actual angle
    	c = new DriveStraight(8.0, c);   // TODO actual distance
    	return c;
    }
    
    private Command buildAutoChain() {
    	
    	// Positions are numbered 1,2,3  L-to-R when viewed from behind.
    	// This is true for both Red and Blue ends of the field.

    	int location = DriverStation.getInstance().getLocation();

    	// FIXME for testing only!! Take this out!!
    	location = 4;

    	Command firstState = null;
    	switch(location) {
    	case 1:
    		firstState = location1chain();
    		break;
    	case 2:
    		firstState = location2chain();
    		break;
    	case 3:
    		firstState = location3chain();
    		break;

    	default:
    		firstState = testChain();
    	}
    	
    	return firstState;
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
    		return CommandUtils.stateChange(this, buildAutoChain()); 
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
