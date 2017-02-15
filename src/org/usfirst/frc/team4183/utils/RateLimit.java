package org.usfirst.frc.team4183.utils;

public class RateLimit {
	
	private final double maxChangePerMsec;
	private double prev = 0.0;
	private double prevMsec;
	
	/**
	 * 
	 * @param maxChangePerSecond units are per second
	 */
	public RateLimit( double maxChangePerSecond) {
		this.maxChangePerMsec = Math.abs(maxChangePerSecond)/1000.0;
		prevMsec = System.currentTimeMillis();
	}
	
	public double f( double x) {
						
		double delta = maxChangePerMsec * (System.currentTimeMillis() - prevMsec);
		if( x > prev + delta) x = prev + delta;
		if( x < prev - delta) x = prev - delta;
		
		prev = x;
		prevMsec = System.currentTimeMillis();
		
		return x;
	}

}
