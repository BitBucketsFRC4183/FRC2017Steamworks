package org.usfirst.frc.team4183.utils;

public class RateLimiter {
	
	private final double maxRampPerMsec;
	private double prevY = 0.0;
	private double prevMsec;
	
	/**
	 * 
	 * @param maxRampPerSec units are per second
	 */
	public RateLimiter( double maxRampPerSec) {
		this.maxRampPerMsec = Math.abs(maxRampPerSec)/1000.0;
		prevMsec = System.currentTimeMillis();
	}
	
	public double limit( double x) {
				
		double absy = Math.abs(x);
		double sgny = Math.signum(x);
		
		double delta = maxRampPerMsec * (System.currentTimeMillis() - prevMsec);		
		if( absy > prevY + delta ) absy = prevY + delta;
		
		prevY = absy;
		prevMsec = System.currentTimeMillis();
		
		return sgny * absy;
	}

}
