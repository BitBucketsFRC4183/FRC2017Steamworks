package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.Idle;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.ClimbForward;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 */
public class ClimbSubsystem extends Subsystem {

	private CANTalon climbMotor;
	private DigitalInput leftSwitch; 
	private DigitalInput rightSwitch;
	private final boolean invertSwitch = false;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public ClimbSubsystem(){
		climbMotor = new CANTalon(RobotMap.CLIMB_MOTOR_ID);
		leftSwitch = new DigitalInput(RobotMap.LEFT_SWITCH_PORT);
		rightSwitch = new DigitalInput(RobotMap.RIGHT_SWITCH_PORT);
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
	
	public boolean bumperSwitch() {	
		boolean invertSwitch = RobotMap.INVERT_BUMPER_SWITCH;
		return (invertSwitch ^ leftSwitch.get() ) || ( invertSwitch ^ rightSwitch.get() );
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new Idle());
	}
}





