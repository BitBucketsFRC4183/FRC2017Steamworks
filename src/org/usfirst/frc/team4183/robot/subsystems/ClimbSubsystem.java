package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
/**
 *
 */
public class ClimbSubsystem extends Subsystem {

	private CANTalon climbMotor;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
public ClimbSubsystem(){
		climbMotor = new CANTalon(RobotMap.climbMotor);
		
}

public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
	climbMotor.getOutputCurrent(); 
    }
public double getCurrent()
{
	double current = climbMotor.getOutputCurrent();
	SmartDashboard.putNumber("ClimbMotorCurrent", current);
	return current;
}

}





