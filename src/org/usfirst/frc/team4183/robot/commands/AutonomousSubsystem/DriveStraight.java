package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
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
	private final double MAX_DRIVE = 0.65;
	
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	// THIS MUST BE LARGE ENOUGH TO MOVE THE ROBOT from stopped position
	// if it isn't, you can get stuck in this state.
	private final double MIN_DRIVE = 0.16;
	
	// Size of dead zone in inches - also used to determine when done
	private final double DEAD_ZONE_INCH = 1.0;  // Can this be reduced a bit?
	
	// Time to settled
	private final long SETTLED_MSECS = 800;  // TODO try to reduce this	
	
	// Limits ramp rate of drive signal
	private final double RATE_LIM_PER_SEC = 2.0;
	
	private final double distanceInch;
	
	private ControlLoop cloop;
	private RateLimit rateLimit;
	private MinMaxDeadzone deadZone;
	private SettledDetector settledDetector; 
	
	
	public DriveStraight( double distanceInch) {		
		requires( Robot.autonomousSubsystem);
		
		this.distanceInch = distanceInch;
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		double setPoint = distanceInch + Robot.driveSubsystem.getPosition_inch();
		
		// Make helpers
		rateLimit = new RateLimit( RATE_LIM_PER_SEC);
		deadZone = new MinMaxDeadzone( DEAD_ZONE_INCH, MIN_DRIVE, MAX_DRIVE);
		settledDetector = new SettledDetector(SETTLED_MSECS, DEAD_ZONE_INCH);
		
		// Set DriveSubsystem axis inputs to linear
		Robot.driveSubsystem.setLinearAxis(true);
		
		// Put DriveSubsystem into "Align Lock" (drive straight)
		OI.btnAlignLock.push();
		
		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.enableLogging("DriveStraight");
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
	
		// Don't forget to stop the control loop!
		cloop.stop();
		
		// Put DriveSubsystem out of "Align Lock"
		OI.btnAlignLock.release();
		
		// Restore DriveSubsystem axis inputs to normal
		Robot.driveSubsystem.setLinearAxis(false);
				
		// Set output to zero before leaving
		OI.axisForward.set(0.0);				
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	@Override
	public double getFeedback() {
		return Robot.driveSubsystem.getPosition_inch();
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
