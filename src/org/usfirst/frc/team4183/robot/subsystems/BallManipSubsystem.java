package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.BallManipSubsystem.Idle;


public class BallManipSubsystem extends Subsystem {
	
	
	
	private CANTalon shooterMotor;
	private CANTalon conveyer;
	private CANTalon intakeMotor;
	
	double shooterVel;
	double conveyerVel;
	double intakeVel;
	
	
	public BallManipSubsystem(){
		shooterMotor = new CANTalon(RobotMap.shooterMotor);
		conveyer = new CANTalon(RobotMap.conveyerMotor);
		intakeMotor = new CANTalon(RobotMap.intakeMotor);
		
	}
	
	public void enable() {}
	
	public void disable() {}
	
    public void initDefaultCommand() {
        setDefaultCommand(new Idle());
    }
}

