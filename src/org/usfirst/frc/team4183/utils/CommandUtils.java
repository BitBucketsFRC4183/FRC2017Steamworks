package org.usfirst.frc.team4183.utils;

import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class CommandUtils {
	
	/**
	 * In a Command, when performing a state transition,
	 * call this function rather than doing nextCommand.start() directly.
	 * Specifically: in isFinished(), check your events, and if a state transition 
	 * is indicated, do "return CommandUtils.stateChange( nextState)".
	 * 
	 * This function always returns true so your isFinished() will return true,
	 * which is correct because you're leaving the state.
	 * 
	 * @param next  The Command that represents the next state
	 */
	public static boolean stateChange( Command from, Command to) {
		Robot.instance().stateChange(from, to);		
		return true;
	}

}
