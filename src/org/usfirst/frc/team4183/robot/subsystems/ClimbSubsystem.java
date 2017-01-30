package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.ClimbIdle;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4183.robot.commands.Climbfwd;
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
	public void idle(){
			
	}
	public void climbfwd(double drive){
		climbMotor.set(drive);
	}
	
	
public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new ClimbIdle());
    }



}





