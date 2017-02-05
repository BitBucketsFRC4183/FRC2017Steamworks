package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.Idle;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 */
public class ClimbSubsystem extends Subsystem {

	private static final double CLIMB_MOTOR_SPEED_PVBUS = 1.0;
	private static final double CLIMB_MOTOR_CURRENT_LIMIT_AMPS = 40.0;
	private CANTalon climbMotor;
	private DigitalInput leftSwitch; 
	private DigitalInput rightSwitch;

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public ClimbSubsystem(){
		climbMotor = new CANTalon(RobotMap.CLIMB_MOTOR_ID);
		leftSwitch = new DigitalInput(RobotMap.LEFT_SWITCH_PORT);
		rightSwitch = new DigitalInput(RobotMap.RIGHT_SWITCH_PORT);
	}
	
	public void off()
	{
		climbMotor.set(0.0);
	}
	public void onForward()
	{
		climbMotor.set(CLIMB_MOTOR_SPEED_PVBUS);
	}
	public void onReverse()
	{
		climbMotor.set(-CLIMB_MOTOR_SPEED_PVBUS);
	}
	
	public double getCurrent()
	{
		return climbMotor.getOutputCurrent();
	}
	
	public boolean isPastCurrentLimit()
	{
		return (getCurrent() >= CLIMB_MOTOR_CURRENT_LIMIT_AMPS);
	}
	public boolean bumperSwitch() {	
		boolean invertSwitch = RobotMap.INVERT_BUMPER_SWITCH;
		return (invertSwitch ^ leftSwitch.get() ) || ( invertSwitch ^ rightSwitch.get() );
	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new Idle());
	}
}





