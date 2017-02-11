package org.usfirst.frc.team4183.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavxIMU {
	
	AHRS ahrs;
	
	NavxIMU() {
		
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		}
		catch (RuntimeException ex ) {
			ex.printStackTrace();
		}
	}
	
	public double getYawDeg() {
		return ahrs.getAngle();
	}

}
