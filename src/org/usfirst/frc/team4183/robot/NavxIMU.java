package org.usfirst.frc.team4183.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavxIMU {
	
	private AHRS ahrs;
	private final boolean DEBUG_THREAD = false;
	
	NavxIMU() {
		
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		}
		catch (RuntimeException ex ) {
			ex.printStackTrace();
		}
		
		// Start thread to print out something at a reasonable rate (testing)
		if( DEBUG_THREAD) {
			new Thread() { 
				public void run() {
					while(true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}	
						System.out.format("Yaw %.2f\n", getYawDeg());						
					}
				}
			}.start();
		}		
		
	}
	
	// Return yaw angle according to right-hand-rule with z-axis up;
	// that is, +yaw is CCW looking down on robot.	
	public double getYawDeg() {
		// Need the - sign to get the Navx to agree with the above
		return -ahrs.getAngle();
	}


}
