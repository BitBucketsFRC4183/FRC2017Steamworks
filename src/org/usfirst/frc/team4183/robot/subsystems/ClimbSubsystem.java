package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.Idle;
/**
 *
 */
public class ClimbSubsystem extends Subsystem {

	private CANTalon climbMotor;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public ClimbSubsystem(){
		climbMotor = new CANTalon(RobotMap.CLIMB_MOTOR);
	}

	public void enable() {}

	public void disable() {}

	public void on( double drive) {
		climbMotor.set(drive);
	}
	
	public double getCurrent()
	{
		return climbMotor.getOutputCurrent();
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new Idle());
	}
}





