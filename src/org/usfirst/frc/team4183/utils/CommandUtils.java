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
	public static boolean stateChange( Command next) {
		
		if( Robot.instance().isStateTestMode() ) {
			
			// In State Test Mode, we want to execute only the Command (=State)
			// being tested and disallow all subsequent Commands.
			// So an attempt to transition out of the Command under test should be denied;
			// only the Subsystem's default commands (if any) should remain.
			//
			// The removeAll() might seem like overkill (why not just NOT do
			// the next.start() call?) but its to handle the case where the 
			// Command under test is actually a CommandGroup, and the Command calling
			// this function is a member of that Group.
			// 
			// If we didn't do the removeAll(), then the Group would fire up its
			// next sequential Command. Not what we want.
			//
			// If the calling Command is not a member of a CommandGroup, 
			// then end() will be called on it;
			// but if the calling Command is a member of a CommandGroup,
			// then it will see a call on interrupted() instead (not my fault).
					
			Scheduler.getInstance().removeAll();
		}
		else
			next.start();
		
		return true;
	}

}
