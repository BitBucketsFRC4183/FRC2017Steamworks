package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.BallManipSubsystem.Idle;


public class BallManipSubsystem extends Subsystem {
	
	boolean isShooting;
	
	private CANTalon shooterMotor;
	private CANTalon conveyerVerti;
	private CANTalon conveyerHoriz;
	
	double shooterVel;
	double vertiVel;
	double horizVel;
	
	
	public BallManipSubsystem(){
		shooterMotor = new CANTalon(RobotMap.shooterMotor);
		conveyerVerti = new CANTalon(RobotMap.vertiMotor);
		conveyerHoriz = new CANTalon(RobotMap.horizMotor);
		
	}
	
	public void enable() {}
	
	public void disable() {}
	
    public void initDefaultCommand() {
        setDefaultCommand(new Idle());
    }
}

