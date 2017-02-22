package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import org.usfirst.frc.team4183.utils.CommandUtils;
import org.usfirst.frc.team4183.utils.ControlLoop;
import org.usfirst.frc.team4183.utils.MinMaxDeadzone;
import org.usfirst.frc.team4183.utils.RateLimit;
import org.usfirst.frc.team4183.utils.SettledDetector;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToGearpeg extends Command {


	private static class YawLoopUser implements ControlLoop.ControlLoopUser {

		// TODO value?? Same as AlignLock? Should be if Vision feeds back degrees.
		private double Kp = 0.05;  

		@Override
		public double getFeedback() {

			// TODO Get & return yaw angle error IN DEGREES from Vision.
			// This can just theta = K * Screen error, calibrate K using known angle.
			// Might as well define the signs same as IMU.
			// Vision should return robot yaw (pose) in target c.s. (0 -> target centered).
			// where +yaw is CCW viewed from top (r.h.r. z-axis up).
			//return Robot.visionSubsystem.getYawRefToTarget();
			return 0;
		}

		@Override
		public void setError(double error) {

			double x = Kp * error;

			// TODO sign??
			// If Vision defined same as IMU (see above) then this sign is correct.
			// (same as in TurnBy)
			// gain must be + from error to feedback
			// +error -> -stick -> +yaw -> +
			OI.axisTurn.set( -x);	
		}		
	}

	private static class DistLoopUser implements ControlLoop.ControlLoopUser {

		// TODO value?? 
		// Same as DriveBy? Should be if vision returns distance to target in Feet
		private double Kp = 1.0;
		// TODO value? Same as DriveBy? Might depend on Vision noise.
		// Gotta make sure it eventually settles within this value.
		// Should this same value be used for both MinMaxDeadzone AND SettledDetector?
		private double DEAD_ZONE_FT = 0.1;  

		// TODO settle time (msecs) param
		SettledDetector settledDetector 
		= new SettledDetector(800, DEAD_ZONE_FT);

		// TODO rate param
		private RateLimit rateLimit = new RateLimit(2.0);

		// TODO min, max drive params
		private MinMaxDeadzone deadZone 
		= new MinMaxDeadzone( DEAD_ZONE_FT, 0.4, 0.7);

		private boolean isSettled() {
			return settledDetector.isSettled();
		}

		@Override
		public double getFeedback() {
			// TODO Auto-generated method stub
			// Get & return distance to target from Vision
			return 0;
		}

		@Override
		public void setError(double error) {
			settledDetector.set(error);

			double x1 = Kp * error;

			// Apply drive non-linearities
			double x2 = rateLimit.f(x1);
			double x3 = deadZone.f(x2, error);		

			// Debug
			//System.out.format("DistLoopUser error=%f x1=%f x2=%f x3=%f\n", error, x1, x2, x3);

			// Set the output
			// TODO sign? Probably negative because forward stick decreases distance
			OI.axisForward.set( -x3);			
		}		
	}


	private final Command nextCommand;
	private ControlLoop yawCloop;
	private ControlLoop distCloop;
	private DistLoopUser distLoopUser;

	public DriveToGearpeg( Command nextCommand) {
		this.nextCommand = nextCommand;
		requires( Robot.autonomousSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {

		// TODO Tell Vision to look for gear

		// TODO yaw setpoint should 0.
		// Vision yaw error value should be 0-centered.
		double yawSetpoint = 0.0;
		yawCloop = new ControlLoop( new YawLoopUser(), yawSetpoint);
		yawCloop.start();

		// TODO distance setpoint should be what??
		double distSetpoint = 0.0;
		distLoopUser = new DistLoopUser();
		distCloop = new ControlLoop( distLoopUser, distSetpoint);
		distCloop.start();
	}


	@Override
	protected boolean isFinished() {

		if( distLoopUser.isSettled()) {
			if( nextCommand != null)
				CommandUtils.stateChange(this, nextCommand);
			else
				return true;
		}

		return false;
	}

	@Override
	protected void end() {

		// Don't forget to stop the Control Loops!!
		yawCloop.stop();
		distCloop.stop();

		// Set output to zero before leaving
		OI.axisTurn.set(0.0);
		OI.axisForward.set(0.0);

		// TODO any exit actions on Vision?
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
