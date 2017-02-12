package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem.Idle;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class AutonomousSubsystem extends Subsystem {

	// Nothing in here now...it's pretty much a dummy,
	// just to make the state machine work.

    public void initDefaultCommand() {
        setDefaultCommand(new Idle());
    }
}

