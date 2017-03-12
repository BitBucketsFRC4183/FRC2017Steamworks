package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.utils.ControlLoop;
import org.usfirst.frc.team4183.utils.LogWriterFactory;
import org.usfirst.frc.team4183.utils.MinMaxDeadzone;
import org.usfirst.frc.team4183.utils.RateLimit;
import org.usfirst.frc.team4183.utils.SettledDetector;

import edu.wpi.first.wpilibj.command.Command;



public class DriveStraight extends Command implements ControlLoop.ControlLoopUser {
		
	// Proportional gain
	private final static double Kp = 0.03;

	// Largest drive that will be applied
	private final double MAX_DRIVE = 1.0;
	
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	// THIS MUST BE LARGE ENOUGH TO MOVE THE ROBOT from stopped position
	// if it isn't, you can get stuck in this state.
	// But if this is TOO BIG, you'll get limit cycling, and also get stuck.
	private final double MIN_DRIVE = RobotMap.DRIVESTRAIGHT_MIN_DRIVE;
	
	// Size of dead zone in inches - also used to determine when done.
	private final double DEAD_ZONE_INCH = 0.5;
	
	// Settled detector lookback for dead zone
	private final long SETTLED_MSECS = 100;
	
	// Used to determine when done.
	// Also used to detect if we've hit something and stopped short of final distance.
	private final double STOPPED_RATE_IPS = 0.2;
	
	// Settled detector lookback for hangup
	// (must be long compared to dead zone lookback to avoid spurious exit)
	private final long HANGUP_MSECS = 800;
	
	// Limits ramp rate of drive signal
	private final double RATE_LIM_PER_SEC = 3.0;
			
	private final double distanceInch;
	
	private ControlLoop cloop;
	private RateLimit rateLimit;
	private MinMaxDeadzone deadZone;
	private SettledDetector settledDetector; 
	private SettledDetector hangupDetector;
	
	private boolean WRITE_LOG_FILE = true;
	private static LogWriterFactory logFactory = new LogWriterFactory("DriveStraight");
	private LogWriterFactory.Writer logWriter;

	
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
		settledDetector = new SettledDetector( SETTLED_MSECS, DEAD_ZONE_INCH);
		hangupDetector = new SettledDetector( HANGUP_MSECS, STOPPED_RATE_IPS);
		logWriter = logFactory.create( WRITE_LOG_FILE);	
		
		// Setup DriveSubsystem for autonomous control
		Robot.driveSubsystem.setAutonomousControl(true);
		
		// Make sure turn stick is zero (it should be, but...)
		OI.axisTurn.set(0.0);
		
		// Put DriveSubsystem into "Align Lock" (drive straight)
		OI.btnAlignLock.push();
		
		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.enableLogging("DriveStraight");
		cloop.start();
	}
		
	@Override
	protected boolean isFinished() {
		
		if( settledDetector.isSettled()
			&&
			Robot.driveSubsystem.getFwdVelocity_ips() < STOPPED_RATE_IPS
		) {
			return true;
		}
		
		if( hangupDetector.isSettled()) {
			return true;
		}

		return false;
	}
	
	@Override
	protected void end() {
	
		// Don't forget to stop the control loop!
		cloop.stop();
		
		logWriter.close();
		
		// Put DriveSubsystem out of "Align Lock"
		OI.btnAlignLock.release();
		
		// Restore DriveSubsystem to normal control
		Robot.driveSubsystem.setAutonomousControl(false);
				
		// Set output to zero before leaving
		OI.axisForward.set(0.0);
		OI.axisTurn.set(0.0);
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

		logWriter.writeLine( 
				String.format("%f %f", 
					error, Robot.driveSubsystem.getFwdVelocity_ips())
				); 

		settledDetector.set(error);
		hangupDetector.set( Robot.driveSubsystem.getFwdVelocity_ips());


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
