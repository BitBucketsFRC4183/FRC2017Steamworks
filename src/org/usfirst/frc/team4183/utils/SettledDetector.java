package org.usfirst.frc.team4183.utils;

public class SettledDetector{
	
	private volatile long tlast;
	private long msecs; 
	private double allowedError; 
	
	public SettledDetector(long msecs, double allowedError) {
		this.msecs = msecs;
		this.allowedError = allowedError; 
	}
	
	public void set(double error) {
		if (Math.abs(error) > allowedError) {
			tlast = System.currentTimeMillis();
		}		
	}
	
	public boolean get() {
		return (System.currentTimeMillis() - tlast) > msecs; 
	}
}
