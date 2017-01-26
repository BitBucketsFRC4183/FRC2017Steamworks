package org.usfirst.frc.team4183.utils;

import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CommandUtils {
	
	/**
	 * In a Command, when performing state transition,
	 * call this function rather than doing nextCommand.start() directly.
	 * @param next  The Command that represents the next state
	 */
	public static void stateChange( Command next) {
		if( !Robot.instance().isCommandTestingMode() ) 
			next.start();
	}

}
