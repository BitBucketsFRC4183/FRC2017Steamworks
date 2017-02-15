package org.usfirst.frc.team4183.utils;

public class DeadZone {
	
	public final double minDrive, maxDrive, deadZone;
	
	public DeadZone( double minDrive, double maxDrive, double deadZone) {
		
		this.minDrive = minDrive;
		this.maxDrive = maxDrive;
		this.deadZone = deadZone;
	}
	
	public double f( double x, double error) {
		
		double sign = Math.signum(x);
		x = Math.abs(x);						
		if( x > maxDrive) x = maxDrive;
		if( x < minDrive) x = minDrive;
		if( Math.abs(error) < deadZone) x = 0.0;
		
		return sign*x;						
	}

}
