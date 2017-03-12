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



public class TurnBy extends Command implements ControlLoop.ControlLoopUser {
		
	// Proportional gain
	private final static double Kp = 0.01;

	// Largest drive that will be applied
	private final double MAX_DRIVE = 0.7;
	
	// Smallest drive that will be applied
	// (unless error falls within dead zone, then drive goes to 0)
	// THIS MUST BE LARGE ENOUGH TO ROTATE THE ROBOT from stopped position;
	// if it isn't, you can get stuck in this state.
	// But if this is TOO BIG, you'll get limit cycling, and also get stuck.
	private final double MIN_DRIVE = RobotMap.TURNBY_MIN_DRIVE; 
	
	// Size of dead zone in degrees - also used to determine when done.
	private final double DEAD_ZONE_DEG = 1.0;

	// Settled detector lookback for dead zone
	private final long SETTLED_MSECS = 200;
	
	// Also used to determine when done
	private final double STOPPED_RATE_DPS = 0.5;
		
	// Limits ramp rate of drive signal
	private final double RATE_LIM_PER_SEC = 3.0;
		
	private final double degreesToTurn;
	
	private ControlLoop cloop;
	private RateLimit rateLimit;
	private MinMaxDeadzone deadZone;
	private SettledDetector settledDetector;
	
	private double zeroPoint = 0.0;
	

	private boolean WRITE_LOG_FILE = true;
	private static LogWriterFactory logFactory = new LogWriterFactory("TurnBy");
	private LogWriterFactory.Writer logWriter;
	
	
	public TurnBy( double degreesToTurn) {		
		requires( Robot.autonomousSubsystem);
		
		this.degreesToTurn = degreesToTurn;		
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		zeroPoint =  Robot.imu.getYawDeg();
		double setPoint = degreesToTurn;
		
		// Make helpers
		rateLimit = new RateLimit( RATE_LIM_PER_SEC);
		deadZone = new MinMaxDeadzone( DEAD_ZONE_DEG, MIN_DRIVE, MAX_DRIVE);
		settledDetector = new SettledDetector( SETTLED_MSECS, DEAD_ZONE_DEG);
		logWriter = logFactory.create( WRITE_LOG_FILE);	
	
		// Setup DriveSubsystem for autonomous control
		Robot.driveSubsystem.setAutonomousControl(true);
		
		// Make sure forward stick is 0 (it should be, but...)
		OI.axisForward.set(0.0);

		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.enableLogging("TurnBy");
		cloop.start();
	}
	

	
	@Override
	protected boolean isFinished() {
		
		if( settledDetector.isSettled() 
			&&
			Robot.imu.getYawRateDps() < STOPPED_RATE_DPS
		) {
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void end() {
	
		// Don't forget to stop the control loop!
		cloop.stop();
		
		logWriter.close();

		// Restore DriveSubsystem to normal control
		Robot.driveSubsystem.setAutonomousControl(false);

		// Set output to zero before leaving
		OI.axisTurn.set(0.0);
		OI.axisForward.set(0.0);		
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	@Override
	public double getFeedback() {
		return (540 + Robot.imu.getYawDeg() - zeroPoint)%360 - 180;
	}
	
	@Override
	public void setError( double error) {
		
		logWriter.writeLine( 
				String.format("%f %f", error, Robot.imu.getYawRateDps())); 
			
		settledDetector.set(error);
							
		double x1 = Kp * error;
			
		// Apply drive non-linearities
		double x2 = rateLimit.f(x1);
		double x3 = deadZone.f(x2, error);		
		
		// Debug
		//System.out.format("error=%f x1=%f x2=%f x3=%f\n", error, x1, x2, x3);
		

		// Dither signal
		// Commented out 'cause we didn't have enough test time to be confident
		// with the amplitude and frequency parameters.
		// It does seem to help a lot, but worried that dither might interfere with
		// settledDetector and get us stuck in this state.
		
		double ditherFreq = 4.0;  // Maybe try something higher freq?
		double ditherAmpl = 0.07;
		double s = Math.sin(2.0*Math.PI*ditherFreq*System.currentTimeMillis()/1000.0);
		x3 += ditherAmpl*s;

		// Set the output
		// - sign required because + stick produces right turn,
		// but right turn is actually a negative yaw angle
		// (using our yaw angle convention: right-hand-rule w/z-axis up)		
		OI.axisTurn.set( -x3);
		
	}
}
