package org.usfirst.frc.team4183.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stopwatch {
	
	private String name;
	private Reporter reporter;
	private long startNanos;
	private List<Double> list = new ArrayList<>();
	private long lastReport = System.currentTimeMillis();
	private long REPORT_INTVL_MILLIS = 1000;
	private boolean isRunning = false;
	
	public interface Reporter {
		void report( String name, double max, double min, double avg);
	}
	
	public Stopwatch( String name, Reporter reporter ) {
		this.name = name;
		this.reporter = reporter;		
	}
	
	public void start() {
		startNanos = System.nanoTime();
		isRunning = true;
	}
	
	public void stop() {
		
		if( !isRunning)
			return;
		
		double interval = (System.nanoTime() - startNanos)/1.0e6;
		list.add(interval);
		
		if(System.currentTimeMillis() - lastReport > REPORT_INTVL_MILLIS) {
			double sum = 0.0;
			for( Double d : list) {
				sum += d;
			}
			
			reporter.report(name, Collections.max(list), Collections.min(list), sum/list.size());
			list.clear();
			lastReport = System.currentTimeMillis();
		}
	}
}
