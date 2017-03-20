package org.usfirst.frc.team4183.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavxIMU {
	
	private final AHRS ahrs;
	
	// 60 Hz is the default, we're just giving it the default,
	// but we need to know this number to compute yawRate.
	private final int AHRS_UPDATE_RATE = 60;  
	private final boolean DEBUG_THREAD = false;
	
	private double pose_x = 0.0;
	private double pose_y = 0.0;
	private double yawOffset = 0.0;
	
	NavxIMU() {
		
		System.out.println( "Starting NavX AHRS");				
		ahrs = new AHRS(SPI.Port.kMXP, (byte)AHRS_UPDATE_RATE);

		// Wait a bit in background, then:
		//   print connected & firmware info
		//   start the dead-reckoning thread
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				System.out.format("NavX isConnected=%b firmware=%s\n", 
						ahrs.isConnected(), ahrs.getFirmwareVersion());
				
				System.out.println( "Starting dead-reckoning thread");
				ReckonThread t = new ReckonThread();
				t.setPriority(Thread.NORM_PRIORITY+2);		
				t.start();
			}
		}.start();
		
		
		// Start thread to print out something at a reasonable rate (testing)
		if( DEBUG_THREAD) {
			new Thread() { 
				public void run() {
					while(true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}	
						System.out.format("isConnected=%b isCalibrating=%b Yaw=%.2f Rate=%.2f\n", 
								isConnected(), isCalibrating(), getYawDeg(), getYawRateDps());						
					}
				}
			}.start();
		}				
	}
	
	// Coordinate system postulates:
	// Robot local coord system:
	//   x-axis points forward
	//   y-axis points left
	//   z-axis points up
	//   +yaw angle is CCW viewed from top (R.H.R)
	//   
	// Global coord system:
	//   z-axis points up
	//   +yaw angle is CCW viewed from top (R.H.R)

	
	// Returns Robot Pose in global coords
	// Position is based on continual dead-reckoning using the shaft encoders,
	// plus yaw from the IMU gyro.
	// rtn[0] = x-coord, inches
	// rtn[1] = y-coord, inches
	// rtn[2] = yaw, degrees
	public synchronized double[] getRobotPose() {		
		double[] rtn = new double[3];
		rtn[0] = pose_x;
		rtn[1] = pose_y;
		rtn[2] = getYawDeg();
		return rtn;
	}
	
	// Set the Robot Pose in global coords.
	// x, y are position in inches; yaw is in degrees.
	public synchronized void setRobotPose( double x, double y, double yaw) {
		pose_x = x;
		pose_y = y;
		yawOffset = yaw;
	}	

	// Return yaw angle according to right-hand-rule with z-axis up;
	// that is, +yaw is CCW looking down on robot.	
	public synchronized double getYawDeg() {
		
		if( !isConnected()) {
			System.err.println( "Error, Yaw requested but NavX not connected");
			return 0.0;
		}
		
		if( isCalibrating()) {
			System.err.println( "Warning, Yaw requested but NavX is calibrating");
		}
		
		// Need the - sign to get the Navx to agree with the yaw definition
		return -ahrs.getAngle() + yawOffset;
	}

	public synchronized double getYawRateDps() {
		
		if( !isConnected()) {
			System.err.println( "Error, Rate requested but NavX not connected");
			return 0.0;
		}
		
		if( isCalibrating()) {
			System.err.println( "Warning, Rate requested but NavX is calibrating");
		}
		
		// The NavX getRate() does NOT return degrees per second (as claimed);
		// it actually returns just the difference between two successive yaw values in degrees
		// (so it actually returns degrees per delta-T).
		// They forgot to multiply by their own internal update rate
		// (60 HZ by default). So we do it here.
		// Need the - sign to get the Navx to agree with the yaw definition.		
		return -AHRS_UPDATE_RATE*ahrs.getRate();
	}
	
	public synchronized double getFwdAccel_G() {
		
		if( !isConnected()) {
			System.err.println( "Error, Accel requested but NavX not connected");
			return 0.0;
		}
		
		if( isCalibrating()) {
			System.err.println( "Warning, Accel requested but NavX is calibrating");
		}
		
		return ahrs.getRawAccelX();
	}
	
	public synchronized boolean isConnected() {
		return ahrs.isConnected();
	}
	
	public synchronized boolean isCalibrating() {
		return ahrs.isCalibrating();
	}

	
	// Thread that continually computes global pose
	private class ReckonThread extends Thread {
		
		private double prevPos = Double.NaN;
		private double prevYaw = Double.NaN;
		
		public void run() {			
			while(true) {
				
				double currYaw = getYawDeg();
				double currPos = Robot.driveSubsystem.getPosition_inch();
				
				if( !Double.isNaN(prevPos) ) {
					double deltaPos = currPos - prevPos;
					double yaw = (currYaw + prevYaw)/2.0;
					synchronized( NavxIMU.this) {
						pose_x += deltaPos*Math.cos( (Math.PI/180.0)*yaw );
						pose_y += deltaPos*Math.sin( (Math.PI/180.0)*yaw );
					}							
				}				
				prevPos = currPos;
				prevYaw = currYaw;
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}			
			}			
		}		
	}
	
}
