package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.utils.ControlLoop;
import org.usfirst.frc.team4183.utils.MinMaxDeadzone;
import org.usfirst.frc.team4183.utils.RateLimit;
import org.usfirst.frc.team4183.utils.SettledDetector;

import edu.wpi.first.wpilibj.command.Command;



public class DriveBy extends Command implements ControlLoop.ControlLoopUser {
	
	// TODO the loop gain constants & NL params work
	// but need further tuning.
	
	// Proportional gain
	private final static double Kp = 0.03; // purposely low for 1st pass

	// Largest drive that will be applied
	private final double MAX_DRIVE = 0.8;
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	private final double MIN_DRIVE = 0.4; // Yeah this does seem high
	// Size of dead zone in degrees
	private final double DEAD_ZONE_METER = 0.03;
	//Time to settled
	private final long SETTLED_MSECS = 1000;	
	
	// Limits ramp rate of drive signal
	private final double RATE_LIM_PER_SEC = 2.0;
	
	private final Command nextState;
	private final double metersToGo;
	
	private ControlLoop cloop;
	private RateLimit rateLimit;
	private MinMaxDeadzone deadZone;
	private SettledDetector settledDetector; 
	
	
	public DriveBy( double metersToGo, Command nextState) {		
		requires( Robot.autonomousSubsystem);
		
		this.metersToGo = metersToGo;
		this.nextState = nextState;
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		double setPoint = metersToGo + Robot.driveSubsystem.getPosition();
		
		rateLimit = new RateLimit( RATE_LIM_PER_SEC);
		deadZone = new MinMaxDeadzone( DEAD_ZONE_METER, MIN_DRIVE, MAX_DRIVE);
		settledDetector = new SettledDetector(SETTLED_MSECS, DEAD_ZONE_METER);
		
		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.start();
	}
	

	
	@Override
	protected boolean isFinished() {
		
		// We are finished when loop error and angular velocity both small
		if (settledDetector.get()) {
			if( nextState != null)
				return CommandUtils.stateChange(this, nextState);
			else
				return true;
		}
		
		return false;
	}
	
	@Override
	protected void end() {
	
		// Don't forget to stop the loop!
		cloop.stop();
				
		// Set output to zero before leaving
		OI.axisForward.set(0.0);				
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	@Override
	public double getFeedback() {
		return Robot.driveSubsystem.getPosition();
	}
	
	@Override
	public void setError( double error) {
		
		settledDetector.set(error); 
		
		double x1 = Kp * error;
			
		// Apply drive non-linearities
		double x2 = deadZone.f(x1, error);		
		double x3 = rateLimit.f(x2);
		
		// Debug
		//System.out.format("error=%f x1=%f x2=%f x3=%f\n", error, x1, x2, x3);
		
		// Set the output
		// TODO need to determine sign
		OI.axisForward.set( -x3);						
	}
	
}
