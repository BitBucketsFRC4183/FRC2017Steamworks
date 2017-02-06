package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.BallManipSubsystem.Idle;


public class BallManipSubsystem extends Subsystem {
		
	private CANTalon topRollerMotor;		// Used for shooting AND intake (i.e., multi-speed)
	private CANTalon conveyerMotor;
	private CANTalon sweeperMotor;
	
	private final double shooterRpm;		    //speed of top roller when shooting
	private final double intakeDrive;		    //speed of top roller when intake
	private final double conveyorDrive;	        //open loop control of conveyer in fraction vbus
	private final double sweeperDrive;			//open loop control of sweeper in fraction vbus
	
	/* Used to limit range of real-time shooter RPM adjustment
	 * not used for now (see "animate()" below) - tjw	
	private final double MAX_SHOOTER_RPM = 4500.0;
	private final double MIN_SHOOTER_RPM = 3800.0;
	*/
	
	private final double P_VALUE = 0.6;
	private final double I_VALUE = 1.2*0.001;
	private final double D_VALUE = .02*1000.0;
	private final double F_VALUE = 0.13;
	
	DoubleSolenoid flapSolenoid = new DoubleSolenoid(RobotMap.BALLSUB_INTAKE_PNEUMA_CHANNEL, RobotMap.BALLSUB_SHOOT_PNEUMA_CHANNEL);
	
	public BallManipSubsystem(){
		
		// Load preferences
		Preferences prefs = Preferences.getInstance();
		shooterRpm = prefs.getDouble("ShooterRpm", 4200.0);
		intakeDrive = prefs.getDouble("IntakeDrive", 0.3);
		conveyorDrive = prefs.getDouble("ConveyorDrive", 0.8);	
		sweeperDrive = prefs.getDouble("SweeperDrive", 0.1);
    	
		topRollerMotor = new CANTalon(RobotMap.BALL_SUBSYSTEM_TOP_ROLLER_MOTOR_ID);
		conveyerMotor = new CANTalon(RobotMap.BALL_SUBSYSTEM_CONVEYER_MOTOR_ID);
		sweeperMotor = new CANTalon(RobotMap.BALL_SUBSYSTEM_SWEEPER_MOTOR_ID);
		
		initializeMotorModes();
	}
	
    private void initializeMotorModes(){
    	topRollerMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	topRollerMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	topRollerMotor.configEncoderCodesPerRev(RobotMap.SHOOTER_ROLLER_PULSES_PER_REV);
    	
    	topRollerMotor.reverseOutput(false);
    	topRollerMotor.reverseSensor(false);
    	
    	topRollerMotor.setPID(P_VALUE, I_VALUE, D_VALUE);
    	topRollerMotor.setF(F_VALUE);
    	
    	topRollerMotor.setIZone(0);
    	topRollerMotor.setCloseLoopRampRate(0.0);
    	topRollerMotor.setAllowableClosedLoopErr(0);
    	topRollerMotor.configNominalOutputVoltage(0.0, 0.0);
    	topRollerMotor.configPeakOutputVoltage(+12.0, -12.0);
    }
	
	public void enable() {
		// Enable closed-loop motor;
		// motor won't actually turn on until set() is done
		topRollerMotor.enableControl();
	}
	
	public void disable() {
		setTopRollerOff();
		setConveyerOff();
		setSweeperOff();
		setFlapModeShoot();
	}
		
    public void initDefaultCommand() {
        setDefaultCommand(new Idle());
    }
    
    public void setTopRollerToIntakeSpeed(){
    	topRollerMotor.set(intakeDrive);
    }
          
    public void setTopRollerToShootingSpeed(){
    	topRollerMotor.set(shooterRpm);
    }
    
	public void setTopRollerOff(){
		// Stop closed-loop motor (immediate)
		// NOTE: Motor is disabled rather than setting speed to 0
		// because this is a velocity-closed-loop motor, 
		// and if loop is not closed (position sensor not connected)
		// setting speed to 0 will not actually stop the motor.
		topRollerMotor.disableControl();
	}
	
    public void setConveyerOn(){
    	conveyerMotor.set(conveyorDrive);
    }
    
    public void setConveyerOff(){
    	conveyerMotor.set(0);
    }
    
    public void setSweeperOn(){
    	sweeperMotor.set(sweeperDrive);
    }
    
    public void setSweeperOff(){
    	sweeperMotor.set(0);
    }
    
    public void setFlapModeIntake(){
    	flapSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    
    public void setFlapModeShoot(){
    	flapSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    
    // Called from Robot in all modes except Test
    // to give this subsystem a little runtime.
    public void animate() {
    	
    	/* Allows the operator to adjust the shooter RPM setpoint --
    	 * disabled for now. tjw.
    	 
    	// Change the Shooter RPM setpoint
    	double value = OI.axisShootRpm.get();
    	if( (value > -0.1) && (value < 0.1) )
    		value = 0.0;
    	    	 
    	shooterRpm += 15.0*value;
    	if( shooterRpm > MAX_SHOOTER_RPM) shooterRpm = MAX_SHOOTER_RPM;
    	if( shooterRpm < MIN_SHOOTER_RPM) shooterRpm = MIN_SHOOTER_RPM;
    	
    	SmartDashboard.putNumber("ShooterRpm(SetPt)", shooterRpm);
    	SmartDashboard.putNumber("ShooterRpm(Actual)", topRollerMotor.get());    	
    	*/
    }  
}

