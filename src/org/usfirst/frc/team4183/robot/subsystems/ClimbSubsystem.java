package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbSubsystem.Idle;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
/**
 *
 */
public class ClimbSubsystem extends Subsystem {

	private static final double CLIMB_MOTOR_SPEED_PVBUS = 1.0;
	private static final double CLIMB_MOTOR_CURRENT_LIMIT_AMPS = 35.0;
	private CANTalon climbMotor;
	private DigitalInput leftSwitch; 
	private DigitalInput rightSwitch;
	
	private boolean lastDirectionForward = true;
	private boolean paused = false; 	// Used to keep track of a pause condition within external commands
	
	private DoubleSolenoid climbSolenoid = new DoubleSolenoid(RobotMap.CLIMB_PNEUMA_RELEASE_CHANNEL, RobotMap.CLIMB_PNEUMA_HOLD_CHANNEL);

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public ClimbSubsystem(){
		climbMotor = new CANTalon(RobotMap.CLIMB_MOTOR_ID);
		leftSwitch = new DigitalInput(RobotMap.LEFT_SWITCH_PORT);
		rightSwitch = new DigitalInput(RobotMap.RIGHT_SWITCH_PORT);
	}
	
	// Only call this from an Idle state
	// It is not fool proof.. like rebooting while on the rope
	// Seriously, think about it.
	public void initialize()
	{
		lastDirectionForward = true;
		paused = false;
	}
	
	public void enable() {
		// Deploy
		climbSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void disable() {
		climbSolenoid.set(DoubleSolenoid.Value.kForward);
		climbMotor.set(0.0);
	}
	
	public void off()
	{
		climbMotor.set(0.0);
	}
	public void onForward()
	{
		lastDirectionForward = true;
		climbMotor.set(-CLIMB_MOTOR_SPEED_PVBUS);
	}
	public void onReverse()
	{	
		lastDirectionForward = false;
		climbMotor.set(CLIMB_MOTOR_SPEED_PVBUS);
	}
	
	public boolean isForward()
	{
		return lastDirectionForward;
	}
	
	public boolean wasPaused()
	{
		return paused;
	}
	public void setPaused()
	{
		paused = true;
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





