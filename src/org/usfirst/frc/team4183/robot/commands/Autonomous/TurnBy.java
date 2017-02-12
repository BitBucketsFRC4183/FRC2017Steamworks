package org.usfirst.frc.team4183.robot.commands.Autonomous;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;

import edu.wpi.first.wpilibj.command.Command;


// Probably I should have derived from PIDCommand but the code in there
// (and in PIDController) is so complicated I don't quite trust it.
// My faith in WPILib is not running real high right now.
// This is such a simple loop I decided to implement using my own thread,
// at least when it doesn't work, I'll have nobody else to blame! --tjw

public class TurnBy extends Command {
	
	// Proportional gain
	private final static double Kp = 0.02; // purposely low for 1st pass

	// Largest drive that will be applied
	private final double MAX_DRIVE = 0.6;
	// Smallest drive that will be applied 
	// (unless error falls within dead zone, then drive goes to 0)
	private final double MIN_DRIVE = 0.2;
	// Size of dead zone in degrees
	private final double DEAD_ZONE_DEG = 3.0;
	
	// Used (along with dead zone) to determine when turn is complete.
	// If angular velocity (Degrees/sec) is greater than this,
	// we're not done yet.
	private final double ALLOWED_RATE_DPS = 3.0;
	
	// Delay between iterations of control loop
	private final long LOOP_MSECS = 50;
	
	
	private final Command nextState;
	private final double degreesToTurn;
	
	private double setPoint;
	private LoopThread loopThread;
	
	// Require a no-arg constructor for use in state-testing mode
	TurnBy() {
		this( 90.0, null);
	}
	
	TurnBy( double degreesToTurn, Command nextState) {		
		requires( Robot.autonomousSubsystem);
		
		this.degreesToTurn = degreesToTurn;
		this.nextState = nextState;
	}

	@Override
	protected void initialize() {
		// Compute setPoint
		setPoint = degreesToTurn + Robot.imu.getYawDeg();
		
		// Start the control loop thread
		loopThread = new LoopThread();
		loopThread.setPriority(Thread.NORM_PRIORITY+2);
		loopThread.start();
	}
	
	@Override
	protected void execute() {
	}
	
	@Override
	protected boolean isFinished() {
		
		// We are finished when loop error and angular velocity both small
		if( ( Math.abs( getError()) < DEAD_ZONE_DEG )
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
		
		// Signal control loop to quit
		loopThread.quit();
		
		// Wait for thread to exit
		try {
			loopThread.join();
		} catch (InterruptedException e) {
			// Per this excellent article:
			// http://www.ibm.com/developerworks/library/j-jtp05236/
			Thread.currentThread().interrupt();
		}
		
		// Set output to zero before leaving
		OI.axisTurn.set(0.0);				
	}
	
	@Override
	protected void interrupted() {
		end();
	}
	
	
	private double getError() {
		return setPoint - Robot.imu.getYawDeg();
	}
	
	
	// This Thread implements the control loop
	private class LoopThread extends Thread {
						
		private void quit() {
			interrupt();
		}
		
		@Override
		public void run() {
						
			// Loop until signaled to quit
			while( !isInterrupted()) {

				double drive = Kp * getError();
				
				// Apply drive non-linearities
				double absDrv = Math.abs(drive);						
				if( absDrv > MAX_DRIVE) absDrv = MAX_DRIVE;
				if( absDrv < MIN_DRIVE) absDrv = MIN_DRIVE;
				if( Math.abs(getError()) < DEAD_ZONE_DEG) absDrv = 0.0;
				
				// Set the output
				// - sign required because + stick produces right turn,
				// but right turn is actually a negative yaw angle
				// (using our yaw angle convention: right-hand-rule w/z-axis up)
				OI.axisTurn.set( -Math.signum(drive)*absDrv);				
					
				// Delay
				try {
					Thread.sleep(LOOP_MSECS);
				} catch (InterruptedException e) {
					interrupt();
				}				
			}			
		}
	}	
}
