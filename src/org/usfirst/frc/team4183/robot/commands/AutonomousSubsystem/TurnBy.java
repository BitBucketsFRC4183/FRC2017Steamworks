package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.utils.ControlLoop;

import edu.wpi.first.wpilibj.command.Command;



public class TurnBy extends Command implements ControlLoop.ControlLoopUser {
	
	// TODO the loop gain constants & NL params work
	// but need further tuning.
	
	// Proportional gain
	private final static double Kp = 0.02; // purposely low for 1st pass

	// Largest drive that will be applied
	private final double MAX_DRIVE = 0.7;
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	private final double MIN_DRIVE = 0.4; // Yeah this does seem high
	// Size of dead zone in degrees
	private final double DEAD_ZONE_DEG = 1.0;
	
	// Used (along with dead zone) to determine when turn is complete.
	// If angular velocity (Degrees/sec) is greater than this,
	// we're not done yet.
	private final double ALLOWED_RATE_DPS = 1.0;
	

	private final Command nextState;
	private final double degreesToTurn;
	
	private ControlLoop cloop;
	
	// Require a no-arg constructor for use in state-testing mode
	public TurnBy() {
		this( 10.0, null);
	}
	
	public TurnBy( double degreesToTurn, Command nextState) {		
		requires( Robot.autonomousSubsystem);
		
		this.degreesToTurn = degreesToTurn;
		this.nextState = nextState;
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		double setPoint = degreesToTurn + Robot.imu.getYawDeg();
		
		// Fire up the loop
		cloop = new ControlLoop( this, setPoint);
		cloop.start();
	}
	

	
	@Override
	protected boolean isFinished() {
		
		// We are finished when loop error and angular velocity both small
		if( ( Math.abs(cloop.getError()) < DEAD_ZONE_DEG )
			&&
			( Math.abs(Robot.imu.getRateDeg()) < ALLOWED_RATE_DPS )
		) {
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
		OI.axisTurn.set(0.0);				
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	@Override
	public double getFeedback() {
		return Robot.imu.getYawDeg();
	}
	
	@Override
	public void setError( double error) {
		
		double inDrive = Kp * error;
			
		// Apply drive non-linearities
		double absDrv = Math.abs(inDrive);						
		if( absDrv > MAX_DRIVE) absDrv = MAX_DRIVE;

		if( absDrv < MIN_DRIVE) absDrv = MIN_DRIVE;
		if( Math.abs(error) < DEAD_ZONE_DEG) absDrv = 0.0;
		double outDrive = Math.signum(inDrive)*absDrv;
		
		// Set the output
		// - sign required because + stick produces right turn,
		// but right turn is actually a negative yaw angle
		// (using our yaw angle convention: right-hand-rule w/z-axis up)
		OI.axisTurn.set( -outDrive);						
	}
}
