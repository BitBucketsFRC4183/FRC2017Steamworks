package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.BallManipSubsystem.Idle;


public class BallManipSubsystem extends Subsystem {
	
	
	
	private CANTalon shooterMotor;
	private CANTalon conveyer;
	private CANTalon sweeperMotor;
	
	private final double shooterVel = 4200.0;		//velocity of top roller when shooting in rpm
	private final double shooterIntakeVel = 500.0;	//velocity of top roller when intake in rpm
	private final double conveyerDrive = 0.8;		
	private final double sweeperDrive = 0.1;
	
	private final double P_VALUE = 0.6;
	private final double I_VALUE = 1.2*0.001;
	private final double D_VALUE = .02*1000.0;
	private final double F_VALUE = 0.13;
	
	
	public BallManipSubsystem(){
		shooterMotor = new CANTalon(RobotMap.shooterMotor);
		conveyer = new CANTalon(RobotMap.conveyerMotor);
		sweeperMotor = new CANTalon(RobotMap.sweeperMotor);
		
		setupSpeedMode();
	}
	
	public void enable() {
	}
	
	public void disable() {
		topMotorOff();
		conveyerMotorOff();
		sweeperMotorOff();	
	}
		
    public void initDefaultCommand() {
        setDefaultCommand(new Idle());
    }
    
    public void topMotorIntakeSpeed(){
		shooterMotor.enableControl();
    	shooterMotor.set(shooterIntakeVel);
    }
    
    public void topMotorShooterSpeed(){
		shooterMotor.enableControl();
    	shooterMotor.set(shooterVel);
    }
    
	public void topMotorOff(){
		shooterMotor.disableControl();;
	}
	
    public void conveyerMotorOn(){
    	conveyer.set(conveyerDrive);
    }
    
    public void conveyerMotorOff(){
    	conveyer.set(0);
    }
    
    public void sweeperMotorOn(){
    	sweeperMotor.set(sweeperDrive);
    }
    
    public void sweeperMotorOff(){
    	sweeperMotor.set(0);
    }
    
    private void setupSpeedMode(){
    	shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	shooterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	shooterMotor.configEncoderCodesPerRev(RobotMap.SHOOTER_ROLLER_PULSES_PER_REV);
    	
    	shooterMotor.reverseOutput(false);
    	shooterMotor.reverseSensor(false);
    	
    	shooterMotor.setPID(P_VALUE, I_VALUE, D_VALUE);
    	shooterMotor.setF(F_VALUE);
    	
    	shooterMotor.setIZone(0);
    	shooterMotor.setCloseLoopRampRate(0.0);
    	shooterMotor.setAllowableClosedLoopErr(0);
    	shooterMotor.configNominalOutputVoltage(0.0, 0.0);
    	shooterMotor.configPeakOutputVoltage(+12.0, -12.0);
    }
}

