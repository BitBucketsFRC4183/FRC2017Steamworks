package org.usfirst.frc.team4183.utils;

public class PWM {
	
	double fHZ;
	
	public PWM( double fHZ) {
		this.fHZ = fHZ;
	}

	public double get( double x) {
		
		double sgn = Math.signum(x);
		x = Math.abs(x);
		
		if( x > 1.0) 
			x = 1.0;
		double y = ((fHZ * System.currentTimeMillis()/1000.0) % 1.0) < x
					? 1.0 : 0.0; 
		
		return sgn * y;
	}
}
