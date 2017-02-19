package org.usfirst.frc.team4183.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.CANTalon;

import org.usfirst.frc.team4183.robot.RobotMap;
import org.usfirst.frc.team4183.robot.commands.BallManipSubsystem.Idle;


public class BallManipSubsystem extends Subsystem {
		
	private CANTalon topRollerMotor;		// Used for shooting AND intake (i.e., multi-speed)
	private CANTalon conveyerMotor;
	private CANTalon sweeperMotor;
	
	private final double shooterRpm;		 //speed of top roller when shooting
	private final double intakeRpm;		    //speed of top roller when intake
	private final double conveyorDrive;	        //open loop control of conveyer in fraction vbus
	private final double sweeperDrive;			//open loop control of sweeper in fraction vbus
	
	/* Used to limit range of real-time shooter RPM adjustment
	 * not used for now (see "animate()" below) - tjw	
	private final double MAX_SHOOTER_RPM = 4500.0;
	private final double MIN_SHOOTER_RPM = 3800.0;
	*/
	
	// The *.001, *1000 are there because Talon doesn't scale by 
	// their own deltaT (=1msec) in their loop implementation, 
	// so we must do it for them here.
	// i.e. if we want a Ki of 0.5, then must pass 0.5*0.001;
	// if we want Kd of 0.01 must pass 0.01*1000.0 (thanks guys!!).
	private final double P_VALUE = 0.6;
	private final double I_VALUE = 1.2*0.001;
	private final double D_VALUE = .02*1000.0;
	private final double F_VALUE = 0.13;
	
	DoubleSolenoid flapSolenoid = new DoubleSolenoid(RobotMap.BALLSUB_INTAKE_PNEUMA_CHANNEL, RobotMap.BALLSUB_SHOOT_PNEUMA_CHANNEL);
	
	public BallManipSubsystem(){
		
		// Load preferences
		Preferences prefs = Preferences.getInstance();
		shooterRpm = prefs.getDouble("ShooterRpm", -4200.0);
		intakeRpm = prefs.getDouble("IntakeRpm", -500.0);
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
    	topRollerMotor.set(intakeRpm);
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
    
	/* Allows the operator to adjust the shooter RPM setpoint --
	 * disabled for now. tjw.
	 * To re-enable:
	 * 1) uncomment this fcn
	 * 2) call this fcn from Robot.disabledPeriodic, .autonomousPeriodic, and .teleopPeriodic
	 * 3) make a Logical Axis in OI (called axisShootRpm below)
	 * 4) define the MAX_SHOOTER_RPM and MIN_SHOOTER_RPM constants
    public void animate() {
    	

    	 
    	// Change the Shooter RPM setpoint
    	double value = OI.axisShootRpm.get();
    	if( (value > -0.1) && (value < 0.1) )
    		value = 0.0;
    	    	 
    	shooterRpm += 15.0*value;
    	if( shooterRpm > MAX_SHOOTER_RPM) shooterRpm = MAX_SHOOTER_RPM;
    	if( shooterRpm < MIN_SHOOTER_RPM) shooterRpm = MIN_SHOOTER_RPM;
    	
    	SmartDashboard.putNumber("ShooterRpm(SetPt)", shooterRpm);
    	SmartDashboard.putNumber("ShooterRpm(Actual)", topRollerMotor.get());    	
    	
    }
    */
}

