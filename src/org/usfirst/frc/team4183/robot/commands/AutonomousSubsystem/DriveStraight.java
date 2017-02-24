package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.utils.ControlLoop;
import org.usfirst.frc.team4183.utils.MinMaxDeadzone;
import org.usfirst.frc.team4183.utils.RateLimit;
import org.usfirst.frc.team4183.utils.SettledDetector;

import edu.wpi.first.wpilibj.command.Command;



public class DriveStraight extends Command implements ControlLoop.ControlLoopUser {
	
	// TODO the loop gain constants & NL params need testing
	
	// Proportional gain
	private final static double Kp = 0.6;

	// Largest drive that will be applied
	private final double MAX_DRIVE = 0.8;
	
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	// THIS MUST BE LARGE ENOUGH TO MOVE THE ROBOT from stopped position
	// if it isn't, you can get stuck in this state.
	private final double MIN_DRIVE = 0.45; // Yeah this does seem high
	
	// Size of dead zone in feet - also used to determine when done
	private final double DEAD_ZONE_FT = 1.0/12.0;
	
	// Time to settled
	private final long SETTLED_MSECS = 1000;  // TODO try to reduce this	
	
	// Limits ramp rate of drive signal
	private final double RATE_LIM_PER_SEC = 2.0;
	
	private final double distanceFt;
	
	private ControlLoop cloop;
	private RateLimit rateLimit;
	private MinMaxDeadzone deadZone;
	private SettledDetector settledDetector; 
	
	
	public DriveStraight( double distanceFt) {		
		requires( Robot.autonomousSubsystem);
		
		this.distanceFt = distanceFt;
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		double setPoint = distanceFt + Robot.driveSubsystem.getPositionFt();
		
		// Make helpers
		rateLimit = new RateLimit( RATE_LIM_PER_SEC);
		deadZone = new MinMaxDeadzone( DEAD_ZONE_FT, MIN_DRIVE, MAX_DRIVE);
		settledDetector = new SettledDetector(SETTLED_MSECS, DEAD_ZONE_FT);
		
		// Put DriveSubsystem into "Align Lock" (drive straight)
		OI.btnAlignLock.push();
		
		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.start();
	}
		
	@Override
	protected boolean isFinished() {
		
		if (settledDetector.isSettled()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void end() {
	
		// Don't forget to stop the loop!
		cloop.stop();
		
		// Put DriveSubsystem out of "Align Lock"
		OI.btnAlignLock.release();
				
		// Set output to zero before leaving
		OI.axisForward.set(0.0);				
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	@Override
	public double getFeedback() {
		// Debug
		// System.out.format( "positionFt=%f\n", Robot.driveSubsystem.getPositionFt());
		return Robot.driveSubsystem.getPositionFt();
	}
	
	@Override
	public void setError( double error) {
	
		settledDetector.set(error); 
		
		double x1 = Kp * error;
			
		// Apply drive non-linearities
		double x2 = rateLimit.f(x1);
		double x3 = deadZone.f(x2, error);
		
		// Debug
		//System.out.format("error=%f x1=%f x2=%f x3=%f\n", error, x1, x2, x3);
		
		// Set the output
		OI.axisForward.set( x3);						
	}
	
}
