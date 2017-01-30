package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;


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
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here,
    	// then delete the "throw"
        //setDefaultCommand(new MySpecialCommand());

    	throw new RuntimeException("Define a Default Command!");
    }
}

