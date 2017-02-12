package org.usfirst.frc.team4183.utils;

import java.io.PrintWriter;

import com.ctre.CANTalon;

public class TalonLogger extends LoggerBase {
	
	private CANTalon motor;
	
	TalonLogger( CANTalon _motor) {
		super( "talon.txt");
		motor = _motor;		
	}

	@Override
	protected void writeLine( PrintWriter writer, long millis) {

		double sp = motor.getSetpoint();       // Setpoint (input)
		double fb = motor.get();               // Feedback value
		double err = motor.getError();         // Error value (native units)
		double ov = motor.getOutputVoltage();  // Drive voltage
		
		writer.format("%6d %9.1f %9.1f %9.1f %9.1f\n", millis, sp, fb, err, ov);
	}
}
