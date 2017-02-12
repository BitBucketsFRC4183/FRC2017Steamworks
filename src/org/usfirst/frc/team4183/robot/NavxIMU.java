package org.usfirst.frc.team4183.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavxIMU {
	
	private AHRS ahrs;
	private final boolean DEBUG_THREAD = false;
	
	NavxIMU() {
		
		System.out.print( "NavX AHRS startup...");
		
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		}
		catch (RuntimeException ex ) {
			ex.printStackTrace();
		}
		
		if( isConnected() ) 
			System.out.format( "success; firmware=%s\n", ahrs.getFirmwareVersion());
		else
			System.out.println( "failed");
		
		
		// Start thread to print out something at a reasonable rate (testing)
		if( DEBUG_THREAD) {
			new Thread() { 
				public void run() {
					while(true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}	
						System.out.format("isConnected=%b isCalibrating=%b Yaw=%.2f\n", 
								isConnected(), isCalibrating(), getYawDeg());						
					}
				}
			}.start();
		}				
	}
	
	
	// Return yaw angle according to right-hand-rule with z-axis up;
	// that is, +yaw is CCW looking down on robot.	
	public double getYawDeg() {
		
		if( !isConnected()) {
			System.err.println( "Error, Yaw requested but NavX not connected");
			return 0.0;
		}
		
		if( isCalibrating()) {
			System.err.println( "Warning, Yaw requested but NavX is calibrating");
		}
		
		// Need the - sign to get the Navx to agree with the yaw definition
		return -ahrs.getAngle();
	}

	public double getRateDeg() {
		return ahrs.getRate();
	}
	
	public boolean isConnected() {
		return ahrs.isConnected();
	}
	
	public boolean isCalibrating() {
		return ahrs.isCalibrating();
	}

}
